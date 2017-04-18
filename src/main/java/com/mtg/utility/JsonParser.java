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

@Service
public class JsonParser {

	private Logger logger = Logger.getLogger(JsonParser.class);

	public Card getCardFromUrl(URL url) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		System.setProperty("http.agent", "Chrome");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				stringBuilder.append(inputLine);
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		JSONArray card = null;
		try {
			JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(stringBuilder.toString());
			card = (JSONArray) genreJsonObject.get("cards");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JSONObject object = null;
		if (card != null) {
			object = (JSONObject) card.get(card.size()-1);
		}

		logger.info("object is: " + object);

		return createCard(object);
	}

	private Card createCard(JSONObject jsonObject) {
		Card card = new Card();
		card.setMultiverseid((Long) jsonObject.get("multiverseid"));
		card.setName((String) jsonObject.get("name"));
		card.setImageUrl((String) jsonObject.get("imageUrl"));
		card.setType((String) jsonObject.get("type"));
		card.setRarity((String) jsonObject.get("rarity"));
		card.setSetName((String) jsonObject.get("setName"));
		card.setText((String) jsonObject.get("text"));

		return card;
	}

}