package mtg.parser.tcgplayer;

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

import com.fasterxml.jackson.databind.ObjectMapper;

public class ParsingThread implements Runnable {

	private static String sql = "INSERT INTO existing_decks (name, deck, event, deck_url, rank, format) VALUES (?, ?, ?, ?, ?, ?);";

	private static final String URL_PREFIX = "http://decks.tcgplayer.com";

	// Keys from app.properties
	private static final String PROP_DATABASE_DRIVER = "db.driver";
	private static final String PROP_DATABASE_PASSWORD = "db.password";
	private static final String PROP_DATABASE_URL = "db.url";
	private static final String PROP_DATABASE_USERNAME = "db.username";

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
		System.out.println(Thread.currentThread().getName() + " starting run methor");
		String deckUrl;
		while (true) {
			deckUrl = DeckToDBWriter.getNextUrl();
			if (deckUrl == null) {
				try {
					System.out.println(Thread.currentThread().getName() + " waiting for some data");
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				deckUrl = DeckToDBWriter.getNextUrl();
			}

			if (deckUrl == null) {
				break;
			}
			System.out.println(Thread.currentThread().getName() + " got URL: " + deckUrl);
			String[] deckInfo = new String[4];
			String fullUrl = URL_PREFIX + deckUrl;
			deckInfo[0] = fullUrl;

			Document deckPage;
			String jsonDeck = "";
			Map<String, String> deck = new HashMap<>();
			try {
				System.out.println("Connecting to url + " + fullUrl);
				deckPage = Jsoup.connect(fullUrl).get();
				String deckName = deckPage.select("h1").text() + " " + deckPage.select("h3").first().text();
				deckInfo[1] = deckName;
				String event = deckPage.select("div.bottomBuffer > a").text();
				deckInfo[2] = event;
				Elements cardElements = deckPage.select("div[id*='Product']");
				for (Element cardElement : cardElements) {
					Element card = cardElement.select("div > div").first();
					String cardAmount = (card.text().substring(0, card.text().indexOf(" ")));
					String cardName = (card.text().substring(card.text().indexOf(" ")));
					deck.put(cardName, cardAmount);
				}

				jsonDeck = (new ObjectMapper().writeValueAsString(deck)).replaceAll("'", "''");
				deckInfo[3] = jsonDeck;
			} catch (IOException e) {
				System.out.println(Thread.currentThread().getName() + " Got exception return URL to Queue");
				DeckToDBWriter.addDeckUrl(deckUrl); 
				e.printStackTrace();
				continue;
			}

			try {
				System.out.println(Thread.currentThread().getName() + " calling write to DB method");
				writeDataToDB(deckInfo);
			} catch (ClassNotFoundException e) {
				System.out.println(Thread.currentThread().getName() + " exception in write to DB method");
				e.printStackTrace();
			}

		
		}
		System.out.println(Thread.currentThread().getName() + " Thread finished!");

	}

	private static void writeDataToDB(String[] deckInfo) throws ClassNotFoundException {

		System.out.println(Thread.currentThread().getName() + " getting connection");
		try (Connection connection = getJDBCConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);){
			

			System.out.println(Thread.currentThread().getName() + " Connection: " + connection + " Statement: "
					+ preparedStatement);
			preparedStatement.setString(1, deckInfo[1]);
			preparedStatement.setString(2, deckInfo[3]);
			preparedStatement.setString(3, deckInfo[2]);
			preparedStatement.setString(4, deckInfo[0]);
			preparedStatement.setString(5, " ");
			preparedStatement.setString(6, "Standard");
			preparedStatement.executeUpdate();
			System.out.println("Record created successfully");

		} catch (SQLException e) {
			System.out.println(Thread.currentThread().getName() + " could not write to DB returning URL to Queue");
			DeckToDBWriter.addDeckUrl(deckInfo[0].substring(26)); 
			e.printStackTrace();
		}

	}

	private static Connection getJDBCConnection() throws ClassNotFoundException, SQLException {
		Connection connection;
		Class.forName(appProperties.getProperty(PROP_DATABASE_DRIVER));
		connection = DriverManager.getConnection(appProperties.getProperty(PROP_DATABASE_URL),
				appProperties.getProperty(PROP_DATABASE_USERNAME), appProperties.getProperty(PROP_DATABASE_PASSWORD));
		connection.setAutoCommit(true);
		return connection;
	}
}
