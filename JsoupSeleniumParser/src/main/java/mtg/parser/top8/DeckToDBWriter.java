package mtg.parser.top8;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DeckToDBWriter {

	final static String STANDARD_URL_PREFIX="http://mtgtop8.com/archetype?a=292&f=ST&meta=58";
	final static String MODERN_URL_PREFIX="http://mtgtop8.com/format?f=MO&meta=44";
	private static Queue<String> queue = getURLListFromURLsWithDecks(MODERN_URL_PREFIX);

	private static final int NUMBER_OF_THREADS = 10;
	private static final String ROOT_PATH_OF_WEBSITE = "http://mtgtop8.com/";
	/**
	 * according to "http://mtgtop8.com/" the value of COMPETITION has next
	 * values: =VI - for vintage =LE - for legacy =MO - for modern =ST - for
	 * standard =EDH - for commander
	 */

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			executor.submit(new ParsingThread());
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println("Executor interrupted");
		}
		
		System.out.println("Parsing took: " + (System.currentTimeMillis() - startTime) / 1000);

	}


	/**
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
			for (Element row : tableRow) {
				Elements tds = row.getElementsByTag("td");
				String urlRow = tds.get(0).getElementsByTag("a").attr("href");
				Integer amount = Integer.valueOf(tds.get(1).html());
				int pages = (int) Math.ceil(amount / 20.0);
				queue.add(ROOT_PATH_OF_WEBSITE.concat(urlRow) + " " + pages);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queue;
	}

	public static String getNextUrl() {
		return queue.poll();
	}

}
