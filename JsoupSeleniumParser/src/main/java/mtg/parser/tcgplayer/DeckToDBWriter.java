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

public class DeckToDBWriter {

	final static int NUMBER_OF_THREAD = 10;

	final static String STANDARD_DECKS_URLS_FILENAME = "LIST_OF_STANDARD_DECKS_URLS.txt";
	final static String MODERN_DECKS_URLS_FILENAME = "LIST_OF_MODERN_DECKS_URLS.txt";

	private static Queue<String> deckUrls;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		deckUrls = readUrlsFromFile();
		ExecutorService service = Executors.newFixedThreadPool(10);

		for (int i = 0; i < NUMBER_OF_THREAD; i++) {
			service.submit(new ParsingThread());
		}

		service.shutdown();
		try {
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println("Executor interrupted");
		}

		System.out.println("Parsing finished in: " + (System.currentTimeMillis() - start));

	}

	public static Queue<String> readUrlsFromFile() {
		Queue<String> urls = new ConcurrentLinkedQueue<>();
		String url;
		try (InputStream fis = new FileInputStream(STANDARD_DECKS_URLS_FILENAME);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {

			while ((url = br.readLine()) != null) {
				urls.add(url);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls;
	}

	public static String getNextUrl() {
		return deckUrls.poll();
	}

	public static void addDeckUrl(String url) {
		DeckToDBWriter.deckUrls.add(url);
	}
}
