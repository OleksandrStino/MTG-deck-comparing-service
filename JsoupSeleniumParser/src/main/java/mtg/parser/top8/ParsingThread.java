package mtg.parser.top8;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ParsingThread implements Runnable {

	// selenium settings
	private static final String CHROMEDRIVER_PATHNAME = "C:/Users/owner/Downloads/chromedriver.exe";
	private static final String CHROMEDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
	private static final String NEXT_PAGE_BUTTON_XPATH_EXPRESSION = "//div[@onclick='PageSubmit(%d)']";
	private static final String ROOT_PATH_OF_WEBSITE = "http://mtgtop8.com/";

	// Keys from app.properties
	private static final String PROP_DATABASE_DRIVER = "db.driver";
	private static final String PROP_DATABASE_PASSWORD = "db.password";
	private static final String PROP_DATABASE_URL = "db.url";
	private static final String PROP_DATABASE_USERNAME = "db.username";
	

	private static final String SQL_STATEMENT = "INSERT INTO existing_decks (name, deck, event, rank, deck_url, format) VALUES (?, ?, ?, ?, ?, ?);";



	// property file with jdbc configuration
	private static Properties appProperties;

	// initialization property file instance
	static {
		appProperties = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream input = loader.getResourceAsStream("app.properties")) {
			appProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String poll;
		while ((poll = DeckToDBWriter.getNextUrl()) != null) {
			System.out.println(poll);
			String[] split = poll.split(" ");
			getDecksFromWebSite(split[0], Integer.valueOf(split[1]));
		}

	}

	private void getDecksFromWebSite(String url, Integer pages) {
		// Get web driver for selenium
		WebDriver driver = getWebDriver();
		driver.get(url);
		try (Connection connection = getJDBCConnection()) {
			for (int i = 1; i <= pages; i++) {
				// getting of page source of start page
				if (i == 1) {
					String pageSource = driver.getPageSource();
					parseDecks(pageSource, connection);
					continue;
				}

				// get element of with button to redirect to the next "i" page
				WebElement element = driver.findElement(By.xpath(String.format(NEXT_PAGE_BUTTON_XPATH_EXPRESSION, i)));
				element.click();

				// get source of loaded page
				String pageSource = driver.getPageSource();
				parseDecks(pageSource, connection);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		driver.close();
	}

	private void parseDecks(String pageSource, Connection connection) {
		Document doc = Jsoup.parse(pageSource);
		try {
			Elements listOfDecks = getDeckTableWithCards(doc);

			System.out.println("Opened database successfully");

			for (Element element : listOfDecks) {
				Elements deckRow = element.getElementsByTag("td");

				String artifactName = deckRow.get(1).getElementsByTag("a").html();
				String playerName = deckRow.get(2).getElementsByTag("a").html();
				String name = artifactName.concat(" ").concat(playerName);
				String deckUrl = getDeckUrl(deckRow);
				// get page source of deck page
				Document deckPage = Jsoup.connect(deckUrl).get();

				String competition = deckRow.get(3).getElementsByTag("a").html();
				String rank = deckRow.get(5).html();

				// replaceAll("'", "''"); uses for validation of insert in
				// PostgreSQL
				Map<String, String> deckFromWebSite = getDeckFromWebSite(deckPage);
				String jsonDeckFromWebSite = (new ObjectMapper().writeValueAsString(deckFromWebSite)).replaceAll("'",
						"''");
				writeDataToDB(connection, name, jsonDeckFromWebSite, rank, competition, deckUrl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write deck to DB
	 *
	 * @param connection
	 *            - JDBC connection
	 * @param name
	 *            - name of deck
	 * @param jsonDeckFromWebSite
	 *            - json string of deck cards
	 * @param rank
	 * @param competition
	 * @param deckUrl
	 */
	private void writeDataToDB(Connection connection, String name, String jsonDeckFromWebSite, String rank,
			String competition, String deckUrl) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_STATEMENT);) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, jsonDeckFromWebSite);
			preparedStatement.setString(3, competition);
			preparedStatement.setString(4, rank);
			preparedStatement.setString(5, deckUrl);
			preparedStatement.setString(6, "Modern");
			preparedStatement.executeUpdate();
			System.out.println("Record created successfully");

		} catch (Exception e) {
			System.err.println("error for deck: " + name + " - " + e.getMessage());
		}
	}

	/**
	 * Create URL of deckPage
	 *
	 * @param deckRow
	 * @param typeOfCompetition
	 *            - (Standard, Modern, Legacy)
	 * @return Deck URL
	 */
	private String getDeckUrl(Elements deckRow) {
		String event = deckRow.get(1).getElementsByTag("a").attr("href");
		return ROOT_PATH_OF_WEBSITE.concat(event);
	}

	/**
	 * Parse deck from html table to the HashMap
	 *
	 * @param deckPage
	 *            - html of parsing deck
	 * @return - HashMap of parsed deck
	 */
	private Map<String, String> getDeckFromWebSite(Document deckPage) {
		Map<String, String> decks = new HashMap<>();
		// get html table with cards
		Element cardTable = deckPage.getElementsByClass("Stable").get(1);
		// get all cards rows from html
		Elements cards = cardTable.getElementsByClass("G14");
		for (Element card : cards) {
			String cardName = card.getElementsByClass("L14").html();
			String amountString = card.getElementsByTag("div").html();
			String amount = (amountString.substring(0, amountString.indexOf("<")));
			decks.put(cardName, amount);
		}
		return decks;
	}

	/**
	 * Initialization of selenium web driver
	 *
	 * @return instance of ChromeDriver
	 */
	private WebDriver getWebDriver() {
		File file = new File(CHROMEDRIVER_PATHNAME);
		System.setProperty(CHROMEDRIVER_SYSTEM_PROPERTY, file.getAbsolutePath());
		return new ChromeDriver();
	}

	/**
	 * Provide JDBC Connection
	 *
	 * @return DB connection
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection getJDBCConnection() throws ClassNotFoundException, SQLException {
		Connection connection;
		Class.forName(appProperties.getProperty(PROP_DATABASE_DRIVER));
		connection = DriverManager.getConnection(appProperties.getProperty(PROP_DATABASE_URL),
				appProperties.getProperty(PROP_DATABASE_USERNAME), appProperties.getProperty(PROP_DATABASE_PASSWORD));
		connection.setAutoCommit(true);
		return connection;
	}

	/**
	 * Looks for table with list of decks in the html page
	 *
	 * @param doc
	 *            - page source
	 * @return html table with list of all decks in the page
	 */
	private Elements getDeckTableWithCards(Document doc) {
		Elements tables = doc.getElementsByClass("Stable");
		Element decksTable = tables.get(1);
		return decksTable.getElementsByClass("hover_tr");
	}

}
