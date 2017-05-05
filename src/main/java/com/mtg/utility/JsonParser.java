package com.mtg.utility;

import com.mtg.entity.Card;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class JsonParser {

	private Logger logger = Logger.getLogger(JsonParser.class);

	public Set<Card> getCardFromUrl(URL url) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		System.setProperty("http.agent", "Chrome");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				stringBuilder.append(inputLine);
			}
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
		}

		JSONArray cards = getJsonObject(stringBuilder);
		logger.info("number of cards is: " + cards.size());

		return createCards(cards);
	}

	private JSONArray getJsonObject(StringBuilder stringBuilder) {
		JSONArray cards = null;
		try {
			JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(stringBuilder.toString());
			cards = (JSONArray) genreJsonObject.get("cards");
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

		return cards;
	}

	private Set<Card> createCards(JSONArray cards) {
		Set<Card> cardsList = new LinkedHashSet<>();
		for (Object cardJsonObject : cards) {
			JSONObject cardJson = (JSONObject) cardJsonObject;
			logger.info("card Json:" + cardJsonObject);
			Long multiverseid = (Long) cardJson.get("multiverseid");
			logger.info("multiverseid: " + multiverseid);
			if (null == multiverseid) {
				continue;
			}
			Card card = new Card();
			card.setMultiverseid(multiverseid);
			card.setName((String) cardJson.get("name"));
			card.setImageUrl((String) cardJson.get("imageUrl"));
			card.setType(((String) cardJson.get("type")).replaceAll("â€”", "-"));
			card.setRarity((String) cardJson.get("rarity"));
			card.setSetName((String) cardJson.get("setName"));
			card.setText((String) cardJson.get("text"));
			card.setSet((String) cardJson.get("set"));
			String manaCost = (String) cardJson.get("manaCost");
			if (null != manaCost) {
				card.setManaCost(manaCost.replaceAll("[{, }]", " ").trim());
			}
//			card.setLegality((String) cardJson.get("legalities"));
			cardsList.add(card);
			logger.info("Parsed card :" + card);
		}

		return cardsList;
	}

}