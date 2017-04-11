package com.mtg.service;

import com.mtg.entity.Deck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeckService {
	Deck addDeck(Deck deck);

	void delete(long id);

	Deck findByName(String name);

	Deck editDeck(Deck user);

	List<Deck> getAll();
}
