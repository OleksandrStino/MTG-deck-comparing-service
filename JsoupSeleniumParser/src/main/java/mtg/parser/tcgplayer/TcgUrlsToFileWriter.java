package mtg.parser.tcgplayer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TcgUrlsToFileWriter {

	private static final String MODERN_URL = "http://decks.tcgplayer.com/magic/deck/search?format=modern&page=";
	private static final int MODERN_PAGES_COUNT = 789;
	private static final String MODERN_DECKS_URLS_FILENAME = "LIST_OF_MODERN_DECKS_URLS.txt";

	/*
	 * private static final String STANDARD_URL =
	 * "http://decks.tcgplayer.com/magic/deck/search?format=standard&page=";
	 * private static final int STANDARD_PAGES_COUNT = 3442; private static
	 * final String STANDARD_DECKS_URLS_FILENAME =
	 * "LIST_OF_STANDARD_DECKS_URLS.txt";
	 */

	private static final String DECK_TABLE_ROW = "tr.gradeA";
	private static final String LINK_ELEMENT = "a";
	private static final String LINK_CONTAINER = "href";

	public static void main(String[] args) {
		String standardDecksUrl = MODERN_URL;
		Document decksPage;

		for (int page = 1; page < MODERN_PAGES_COUNT; page++) {
			try {
				System.out.println("Connecting to url: " + standardDecksUrl + page);
				decksPage = Jsoup.connect(standardDecksUrl + page).get();
				Elements deckRows = decksPage.select(DECK_TABLE_ROW);
				for (Element deckRow : deckRows) {
					Element deckLink = deckRow.select(LINK_ELEMENT).first();
					try {
						FileWriter fw = new FileWriter(MODERN_DECKS_URLS_FILENAME, true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter out = new PrintWriter(bw);
						out.println(deckLink.attr(LINK_CONTAINER));
						out.close();
					} catch (IOException e) {
						System.out.println("Problems with file");
					}

				}
			} catch (IOException e) {
				System.out.println("Exception while parsing page " + page + e);
			}
		}
	}

}
