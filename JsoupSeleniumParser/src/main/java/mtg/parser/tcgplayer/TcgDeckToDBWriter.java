package mtg.parser.tcgplayer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TcgDeckToDBWriter {

	final static String STANDARD_DECKS_URLS_FILENAME = "LIST_OF_STANDARD_DECKS_URLS.txt";
	final static String MODERN_DECKS_URLS_FILENAME = "LIST_OF_MODERN_DECKS_URLS.txt";


	public static Queue<String> deckUrls;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		deckUrls = readUrlsFromFile();
		ExecutorService service = Executors.newFixedThreadPool(10);

		/*
		 * while (!deckUrls.isEmpty()) { service.submit(new TcgParsingThread());
		 * }
		 */

		for (int i = 0; i < 10; i++) {
			service.submit(new TcgParsingThread());
		}

		service.shutdown();
		try {
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println("EXECUTOR INTERRUPTED");
		}

		System.out.println(System.currentTimeMillis() - start);

	}

	public static Queue<String> readUrlsFromFile() {
		Queue<String> urls = new ConcurrentLinkedQueue<>();
		String url;
		try (InputStream fis = new FileInputStream(MODERN_DECKS_URLS_FILENAME);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((url = br.readLine()) != null) {
				urls.add(url);
			}
			
		/*	for(int i = 0; i< 10000; i++){
				url = br.readLine();
				urls.add(url);
			}*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls;

	}

	/*
	 * public static String[] parseUrlToJson(String deckUrl) { String[] deckInfo
	 * = new String[4]; String fullUrl = "http://decks.tcgplayer.com" + deckUrl;
	 * Document deckPage; String jsonDeck = ""; Map<String, String> deck = new
	 * HashMap<>(); deckInfo[0] = fullUrl; try { deckPage =
	 * Jsoup.connect(fullUrl).get(); String deckName =
	 * deckPage.select("h1").text() + " " +
	 * deckPage.select("h3").first().text(); deckInfo[1] = deckName; String
	 * event = deckPage.select("div.bottomBuffer > a").text(); deckInfo[2] =
	 * event; Elements cardElements = deckPage.select("div[id*='Product']"); for
	 * (Element cardElement : cardElements) { Element card =
	 * cardElement.select("div > div").first(); String cardAmount =
	 * (card.text().substring(0, card.text().indexOf(" "))); String cardName =
	 * (card.text().substring(card.text().indexOf(" "))); deck.put(cardName,
	 * cardAmount); }
	 * 
	 * jsonDeck = (new ObjectMapper().writeValueAsString(deck)).replaceAll("'",
	 * "''"); deckInfo[3] = jsonDeck; // System.out.println(cardElements); }
	 * catch (IOException e) { e.printStackTrace(); } return deckInfo;
	 * 
	 * }
	 */

}
