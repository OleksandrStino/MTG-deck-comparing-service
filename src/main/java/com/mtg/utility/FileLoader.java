package com.mtg.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtg.entity.TopDecks;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileLoader {

	public List<TopDecks> getDecks() {

		List<TopDecks> decks = new ArrayList<>();
		String path = "C:/Users/owner/Desktop/DECKS/BURN";
		File folder = new File(path);

		String[] files = folder.list();

		for (String file : files) {
			Map<String, Integer> map = new HashMap<>();
			try {
				BufferedReader lineReader = new BufferedReader(new FileReader(path.concat("/").concat(file)));
				String lineText;
				while ((lineText = lineReader.readLine()) != null) {
					String[] strings = lineText.split(" ");
					String s = "";
					if (strings.length > 2){
						for (int i = 1; i <strings.length; i++) {
							s = s + " " + strings[i];
						}
						map.put(s.trim(), Integer.valueOf(strings[0]));
					}
					else {
						map.put(strings[1], Integer.valueOf(strings[0]));
					}
				}
				TopDecks topDecks = new TopDecks();
				String jsonMap = new ObjectMapper().writeValueAsString(map);
				topDecks.setDeck(jsonMap);
				topDecks.setName(file.replaceAll(".txt", ""));
				decks.add(topDecks);
				lineReader.close();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
		return decks;
	}
}
