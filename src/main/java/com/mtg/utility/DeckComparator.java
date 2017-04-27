package com.mtg.utility;

import com.mtg.entity.Card;
import com.mtg.entity.TopDecks;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeckComparator {

	private static final Logger logger = Logger.getLogger(DeckComparator.class);

	private int count;

	public Map<TopDecks, Integer> getCardMatches(Map<Card, Integer> userDeck, List<TopDecks> decksList) {
		Map<TopDecks, Integer> result = new HashMap<>();
		for (TopDecks deck : decksList) {
			Map<String, Integer> existedDeck = convertToMap(deck);
			for (Map.Entry<String, Integer> comparedCard : existedDeck.entrySet()) {
				for (Map.Entry<Card, Integer> currentCard : userDeck.entrySet()) {
					if (comparedCard.getKey().equals(currentCard.getKey().getName())) {
						count++;
					}
				}
			}
			if (count == 0) {
				continue;
			}
			result.put(deck, count);
			count = 0;
		}

		return sortByValue(result);
	}

	private Map<String, Integer> convertToMap(TopDecks deck) {
		JSONObject genreJsonObject = null;
		try {
			genreJsonObject = (JSONObject) JSONValue.parseWithException(deck.getDeck());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new HashMap<>(genreJsonObject);
	}

	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {
		// Convert Map to List of Map
		List<Map.Entry<K, V>> list = new LinkedList<>(unsortMap.entrySet());

		// Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		list.sort((o1, o2) -> -(o1.getValue()).compareTo(o2.getValue()));

		// Loop the sorted list and put it into a new insertion order Map LinkedHashMap
		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

}
