package com.mtg.service;

import com.mtg.entity.Deck;
import com.mtg.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeckService {
	Deck addDeck(Deck deck, String username);

	List<Deck> findByUser(User user);

	Deck findById(Long id);

	void delete(long id);

	Deck findByName(String name);

	Deck editDeck(Deck user);

	List<Deck> getAll();
}
