package com.mtg.utility;

import com.mtg.entity.Card;
import com.mtg.entity.TopDecks;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DeckComparator {

	private Logger logger = Logger.getLogger(DeckComparator.class);

	private int count;

	public Map<String, Integer> getCardMatches(Map<Card, Integer> userDeck, List<TopDecks> decksList){
		Map<String, Integer> result = new TreeMap<>();
		for(TopDecks deck : decksList){
			Map<String, Integer> existedDeck = convertToMap(deck);
			for(Map.Entry<String, Integer> comparedCard : existedDeck.entrySet()){
				for(Map.Entry<Card, Integer> currentCard : userDeck.entrySet()){
					if(comparedCard.getKey().equals(currentCard.getKey().getName())){
						count++;
					}
				}
			}
			result.put(deck.getName(), count);
			count=0;
		}

		return result;
	}

	private Map<String, Integer> convertToMap(TopDecks list) {
		JSONObject genreJsonObject = null;
		try {
			genreJsonObject = (JSONObject) JSONValue.parseWithException(list.getDeck());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new HashMap<>(genreJsonObject);
	}
}
