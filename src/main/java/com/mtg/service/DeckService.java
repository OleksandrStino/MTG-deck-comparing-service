package com.mtg.service;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DeckService {
	Deck addDeck(Deck deck, String username);

	List<Deck> findByUser(User user);

	Deck findById(Long id);

	void delete(long id);

	Deck findByName(String name);

	Deck addCard(Deck deck, Card card, Integer amount);

	Deck addCollectionOfCards(Deck deck, Map<Card, Integer> cards);

	List<Deck> getAll();

	Deck updateDeckCards(Deck deck, Map<Card, Integer> cards);
}
