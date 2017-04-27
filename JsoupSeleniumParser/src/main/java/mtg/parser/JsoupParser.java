package mtg.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsoupParser {

	//selenium settings
	private static final String CHROMEDRIVER_PATHNAME = "C:/Users/owner/Downloads/chromedriver.exe";
	private static final String CHROMEDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
	private static final String NEXT_PAGE_BUTTON_XPATH_EXPRESSION = "//div[@onclick='PageSubmit(%d)']";
	private static final String ROOT_PATH_OF_WEBSITE = "http://mtgtop8.com/";

	/**
	 * according to "http://mtgtop8.com/" the value of COMPETITION has next values:
	 * =VI - for vintage
	 * =LE - for legacy
	 * =MO - for modern
	 * =ST - for standard
	 * =EDH - for commander
	 */
	private static final String COMPETITION = "=MO";

	//Keys from app.properties
	private static final String PROP_DATABASE_DRIVER = "db.driver";
	private static final String PROP_DATABASE_PASSWORD = "db.password";
	private static final String PROP_DATABASE_URL = "db.url";
	private static final String PROP_DATABASE_USERNAME = "db.username";

	private static final String SQL_STATEMENT = "INSERT INTO existed_decks (name, deck, event, rank, deck_url) VALUES (?, ?, ?, ?, ?);";

	//property file with jdbc configuration
	private static Properties appProperties;

	private static Queue<String> queue = getURLListFromURLsWithDecks("http://mtgtop8.com/format?f=MO&meta=44");

	//initialization property file instance
	static {
		appProperties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream input = loader.getResourceAsStream("app.properties")) {
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		System.out.println(queue.toString());
		System.out.println(queue.size());

		long l = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			executor.submit(() -> {
				String poll;
				while ((poll = queue.poll()) != null) {
					System.out.println(poll);
					String[] split = poll.split(" ");
					getDecksFromWebSite(split[0], Integer.valueOf(split[1]), split[2]);
				}
			});
		}
		executor.shutdown();
		System.out.println((System.currentTimeMillis() - l) / 1000);

	}


	/**
	 *
	 * @param url
	 * @return
	 */
	private static Queue<String> getURLListFromURLsWithDecks(String url) {
		Queue<String> queue = new ConcurrentLinkedQueue<>();
		try {
			Document document = Jsoup.connect(url).get();
			Elements stable = document.getElementsByClass("Stable");
			Element element = stable.get(0);
			Elements tableRow = element.getElementsByClass("hover_tr");
			for(Element row : tableRow){
				Elements tds = row.getElementsByTag("td");
				String urlRow = tds.get(0).getElementsByTag("a").attr("href");
				Integer amount = Integer.valueOf(tds.get(1).html());
				int pages = (int) Math.ceil(amount/20.0);
				queue.add(ROOT_PATH_OF_WEBSITE.concat(urlRow) + " " + pages + " " + COMPETITION);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queue;
	}


	private static void getDecksFromWebSite(String url, Integer pages, String typeOfMTGCompetition) {
		//Get web driver for selenium
		WebDriver driver = getWebDriver();
		driver.get(url);
		try (Connection connection = getJDBCConnection()) {
			for (int i = 1; i <= pages; i++) {
				//getting of page source of start page
				if (i == 1) {
					String pageSource = driver.getPageSource();
					parseDecks(pageSource, typeOfMTGCompetition, connection);
					continue;
				}

				//get element of with button to redirect to the next "i" page
				WebElement element = driver.findElement(By.xpath(String.format(NEXT_PAGE_BUTTON_XPATH_EXPRESSION, i)));
				element.click();

				//get source of loaded page
				String pageSource = driver.getPageSource();
				parseDecks(pageSource, typeOfMTGCompetition, connection);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		driver.close();

	}

	/**
	 * Initialization of selenium web driver
	 *
	 * @return instance of ChromeDriver
	 */
	private static WebDriver getWebDriver() {
		File file = new File(CHROMEDRIVER_PATHNAME);
		System.setProperty(CHROMEDRIVER_SYSTEM_PROPERTY, file.getAbsolutePath());
		return new ChromeDriver();
	}

	private static void parseDecks(String pageSource, String typeOfCompetition, Connection connection) {
		Document doc = Jsoup.parse(pageSource);
		try {
			Elements listOfDecks = getDeckTableWithCards(doc);

			System.out.println("Opened database successfully");

			for (Element element : listOfDecks) {
				Elements deckRow = element.getElementsByTag("td");

				String artifactName = deckRow.get(1).getElementsByTag("a").html();
				String playerName = deckRow.get(2).getElementsByTag("a").html();
				String name = artifactName.concat(" ").concat(playerName);
				System.out.println("Deck name is: " + name);
				String deckUrl = getDeckUrl(deckRow, typeOfCompetition);
				System.out.println("Deck url is: " + deckUrl);
				//get page source of deck page
				Document deckPage = Jsoup.connect(deckUrl).get();

				String competition = deckRow.get(3).getElementsByTag("a").html();
				String rank = deckRow.get(5).html();

				//replaceAll("'", "''"); uses for validation of insert in PostgreSQL
				Map<String, String> deckFromWebSite = getDeckFromWebSite(deckPage);
				String jsonDeckFromWebSite = (new ObjectMapper().writeValueAsString(deckFromWebSite)).replaceAll("'", "''");
				System.out.println("Deck is: " + jsonDeckFromWebSite);

				writeDataToDB(connection, name, jsonDeckFromWebSite, rank, competition, deckUrl);
			}
			connection.commit();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provide JDBC Connection
	 *
	 * @return DB connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static Connection getJDBCConnection() throws ClassNotFoundException, SQLException {
		Connection connection;
		Class.forName(appProperties.getProperty(PROP_DATABASE_DRIVER));
		connection = DriverManager
				.getConnection(appProperties.getProperty(PROP_DATABASE_URL),
						appProperties.getProperty(PROP_DATABASE_USERNAME),
						appProperties.getProperty(PROP_DATABASE_PASSWORD));
		connection.setAutoCommit(false);
		return connection;
	}

	/**
	 * Write deck to DB
	 *
	 * @param connection          - JDBC connection
	 * @param name                - name of deck
	 * @param jsonDeckFromWebSite - json string of deck cards
	 * @param rank
	 * @param competition
	 * @param deckUrl
	 */
	private static void writeDataToDB(Connection connection, String name, String jsonDeckFromWebSite,
									  String rank, String competition, String deckUrl) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_STATEMENT);) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, jsonDeckFromWebSite);
			preparedStatement.setString(3, competition);
			preparedStatement.setString(4, rank);
			preparedStatement.setString(5, deckUrl);
			preparedStatement.executeUpdate();
			System.out.println("Record created successfully");

		} catch (Exception e) {
			System.err.println("error for deck: " + name + " - " + e.getMessage());
		}
	}

	/**
	 * Looks for table with list of decks in the html page
	 *
	 * @param doc - page source
	 * @return html table with list of all decks in the page
	 */
	private static Elements getDeckTableWithCards(Document doc) {
		Elements tables = doc.getElementsByClass("Stable");
		Element decksTable = tables.get(1);
		return decksTable.getElementsByClass("hover_tr");
	}


	/**
	 * Create URL of deckPage
	 *
	 * @param deckRow
	 * @param typeOfCompetition - (Standard, Modern, Legacy)
	 * @return Deck URL
	 */
	private static String getDeckUrl(Elements deckRow, String typeOfCompetition) {
		String event = deckRow.get(1).getElementsByTag("a").attr("href").split("&")[0];
		return ROOT_PATH_OF_WEBSITE.concat(event).concat(typeOfCompetition);
	}

	/**
	 * Parse deck from html table to the HashMap
	 *
	 * @param deckPage - html of parsing deck
	 * @return - HashMap of parsed deck
	 */
	private static Map<String, String> getDeckFromWebSite(Document deckPage) {
		Map<String, String> decks = new HashMap<>();
		//get html table with cards
		Element cardTable = deckPage.getElementsByClass("Stable").get(1);
		//get all cards rows from html
		Elements cards = cardTable.getElementsByClass("G14");
		for (Element card : cards) {
			String cardName = card.getElementsByClass("L14").html();
			String amountString = card.getElementsByTag("div").html();
			String amount = (amountString.substring(0, amountString.indexOf("<")));
			decks.put(cardName, amount);
		}
		return decks;
	}

}
