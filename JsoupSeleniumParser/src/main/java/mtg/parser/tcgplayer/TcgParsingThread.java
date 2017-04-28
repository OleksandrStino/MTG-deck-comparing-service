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

public class TcgParsingThread implements Runnable {

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static String sql = "INSERT INTO existed_decks (name, deck, event, deck_url, rank) VALUES (?, ?, ?, ?, ?);";

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
			deckUrl = TcgDeckToDBWriter.deckUrls.poll();
			if (deckUrl == null) {
				try {
					System.out.println(Thread.currentThread().getName() + " waiting for some data");
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				deckUrl = TcgDeckToDBWriter.deckUrls.poll();
			}

			if (deckUrl == null) {
				break;
			}
			System.out.println(Thread.currentThread().getName() + " got URL: " + deckUrl);
			System.out.println(TcgDeckToDBWriter.deckUrls.size() + " left");
			String[] deckInfo = new String[4];
			String fullUrl = "http://decks.tcgplayer.com" + deckUrl;
			System.out.println("Connecting to url + " + deckUrl);
			Document deckPage;
			String jsonDeck = "";
			Map<String, String> deck = new HashMap<>();
			deckInfo[0] = fullUrl;
			try {
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
				// System.out.println(cardElements);
			} catch (IOException e) {
				System.out.println(Thread.currentThread().getName() + " Got exception return URL to Queue");
				e.printStackTrace();
				/*TcgDeckToDBWriter.deckUrls.add(deckUrl);*/
				continue;
			}

			try {
				System.out.println(Thread.currentThread().getName() + " calling write to DB method");
				writeDataToDB(deckInfo);
			} catch (ClassNotFoundException e) {
				System.out.println(Thread.currentThread().getName() + " exception in write to DB method");
				e.printStackTrace();
			}

			if (preparedStatement != null) {
				try {
					System.out.println(Thread.currentThread().getName() + " closing statement");
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					System.out.println(Thread.currentThread().getName() + " closing connection");
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		System.out.println(Thread.currentThread().getName() + " Thread finished!");

	}

/*	private void writeDeckToFile(String[] deckInfo) {

		try {
			FileWriter fw = new FileWriter("STANDARD_DECKS.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(deckInfo[0] + " " + deckInfo[1] + " " + deckInfo[2] + " " + deckInfo[3]);
			out.close();
		} catch (IOException e) {
			System.out.println("Problems with file");
		}

	}*/

	private static void writeDataToDB(String[] deckInfo) throws ClassNotFoundException {

		/*
		 * try { if (connection == null || preparedStatement == null ||
		 * connection.isClosed() || preparedStatement.isClosed()) {
		 * System.out.println(Thread.currentThread().getName() +
		 * " initializing new connection"); try { connection =
		 * getJDBCConnection(); connection.setAutoCommit(false);
		 * preparedStatement = connection.prepareStatement(sql); } catch
		 * (Exception e) { System.err.println("error for deck: " + deckInfo[1]);
		 * e.printStackTrace(); } } } catch (SQLException e1) {
		 * System.out.println(Thread.currentThread().getName() +
		 * " could not obtain connection"); }
		 */

		System.out.println(Thread.currentThread().getName() + " getting already created connection");
		try {
			/*
			 * connection.clearWarnings(); preparedStatement.clearParameters();
			 */

			connection = getJDBCConnection();
			preparedStatement = connection.prepareStatement(sql);

			System.out.println(Thread.currentThread().getName() + " Connection: " + connection + " Statement: "
					+ preparedStatement);
			preparedStatement.setString(1, deckInfo[1]);
			preparedStatement.setString(2, deckInfo[3]);
			preparedStatement.setString(3, deckInfo[2]);
			preparedStatement.setString(4, deckInfo[0]);
			preparedStatement.setString(5, " ");
			preparedStatement.executeUpdate();
			System.out.println("Record created successfully");
		
		} catch (SQLException e) {
			System.out.println(Thread.currentThread().getName() + " could not write to DB returning URL to Queue");
			/*TcgDeckToDBWriter.deckUrls.add(deckInfo[0]);*/
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
