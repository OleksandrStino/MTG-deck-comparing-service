package com.mtg.utility;

import com.mtg.entity.dto.CardDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Service
public class JsonParser {

	public CardDTO getCardFromUrl(URL url) throws IOException {
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
			object = (JSONObject) card.get(0);
		}

		return createCard(object);
	}

	private CardDTO createCard(JSONObject jsonObject) {
		CardDTO card = new CardDTO();
		card.setMultiverseid((Integer) jsonObject.get("multiverseid"));
		card.setName((String) jsonObject.get("name"));
		card.setImageUrl((String) jsonObject.get("imageUrl"));
		card.setType((String) jsonObject.get("type"));
		card.setTypes((List<String>) jsonObject.get("types"));
		card.setRarity((String) jsonObject.get("rarity"));
		card.setSetName((String) jsonObject.get("setName"));
		card.setText((String) jsonObject.get("text"));

		return card;
	}

}