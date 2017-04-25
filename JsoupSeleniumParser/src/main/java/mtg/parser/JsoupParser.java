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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JsoupParser {

	private static final String CHROMEDRIVER_PATHNAME = "C:/Users/owner/Downloads/chromedriver.exe";
	private static final String CHROMEDRIVER_SYSTEM_PROPERTY = "webdriver.chrome.driver";
	private static final String NEXT_PAGE_BUTTON_XPATH_EXPRESSION = "//div[@onclick='PageSubmit(%d)']";
	private static final int SELENIUM_TIMEOUT = 3000;
	private static final String ROOT_PATH_OF_WEBSITE = "http://mtgtop8.com/";

	//Keys from app.properties
	private static final String PROP_DATABASE_DRIVER = "db.driver";
	private static final String PROP_DATABASE_PASSWORD = "db.password";
	private static final String PROP_DATABASE_URL = "db.url";
	private static final String PROP_DATABASE_USERNAME = "db.username";

	//property file with jdbc configuration
	private static Properties appProperties;

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
		getDecksFromWebSite("http://mtgtop8.com/archetype?a=193&meta=118&f=MO", Integer.valueOf("2"), "=MO");
	}

	private static void getDecksFromWebSite(String url, Integer pages, String typeOfMTGCompetition) {
		//Get web driver for selenium
		WebDriver driver = getWebDriver();
		driver.get(url);
		for (int i = 1; i <= pages; i++) {
			//getting of page source of start page
			if (i == 1) {
				String pageSource = driver.getPageSource();
				parseDecks(pageSource, typeOfMTGCompetition);
				continue;
			}

			//get element of with button to redirect to the next "i" page
			WebElement element = driver.findElement(By.xpath(String.format(NEXT_PAGE_BUTTON_XPATH_EXPRESSION, i)));
			element.click();

			//wait tree seconds for full loading of new page
			try {
				Thread.sleep(SELENIUM_TIMEOUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//get source of load page
			String pageSource = driver.getPageSource();
			parseDecks(pageSource, typeOfMTGCompetition);
		}
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

	private static void parseDecks(String pageSource, String typeOfCompetition) {
		Document doc = Jsoup.parse(pageSource);
		try {
			Elements listOfDecks = getDeckTableWithCards(doc);
			Connection connection = getJDBCConnection();
			System.out.println("Opened database successfully");

			for (Element element : listOfDecks) {
				Elements deckRow = element.getElementsByTag("td");
				String name = getDeckName(deckRow);
				System.out.println("Deck name is: " + name);
				String deckUrl = getDeckUrl(deckRow, typeOfCompetition);
				System.out.println("Deck url is: " + deckUrl);

				//get page source of deck page
				Document deckPage = Jsoup.connect(deckUrl).get();


				//replaceAll("'", "''"); uses for validation of insert in PostgreSQL
				Map<String, String> deckFromWebSite = getDeckFromWebSite(deckPage);
				String jsonDeckFromWebSite = (new ObjectMapper().writeValueAsString(deckFromWebSite)).replaceAll("'", "''");
				System.out.println("Deck is: " + jsonDeckFromWebSite);

				writeDataToDB(connection, name, jsonDeckFromWebSite);
			}
			connection.commit();
			connection.close();
		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provide JDBC Connection
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
	 * @param connection - JDBC connection
	 * @param name - name of deck
	 * @param jsonDeckFromWebSite - json string of deck cards
	 */
	private static void writeDataToDB(Connection connection, String name, String jsonDeckFromWebSite) {
		Statement statement;
		try {
			statement = connection.createStatement();
			String sql = String.format("INSERT INTO existed_decks (name, deck) " +
					"VALUES ('%s', '%s');", name, jsonDeckFromWebSite);
			System.out.println(sql);
			statement.executeUpdate(sql);
			System.out.println("Record created successfully");
			statement.close();

		} catch (Exception e) {
			System.err.println("error for deck: " + name + " - " + e.getMessage());
		}
	}

	/**
	 * Loks for table with list of decks in the html page
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
		return ROOT_PATH_OF_WEBSITE .concat(event).concat(typeOfCompetition);
	}

	/**
	 * Get  deckName from html row
	 *
	 * @param deckRow - row with all deck information (link, name)
	 * @return deckName
	 */
	private static String getDeckName(Elements deckRow) {
		String artifactName = deckRow.get(1).getElementsByTag("a").html();
		String playerName = deckRow.get(2).getElementsByTag("a").html();
		String rank = deckRow.get(5).html();
		return (artifactName.concat(" ").concat(playerName).concat(" rank: ").concat(rank)).replaceAll("'", "''");
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
