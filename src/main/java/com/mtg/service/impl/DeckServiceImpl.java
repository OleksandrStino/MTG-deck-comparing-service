package com.mtg.service.impl;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.entity.User;
import com.mtg.repository.DeckRepository;
import com.mtg.service.DeckService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeckServiceImpl implements DeckService {

	private Logger logger = Logger.getLogger(DeckServiceImpl.class);

	@Autowired
	private DeckRepository deckRepository;

	@Autowired
	private UserServiceImpl userService;

	@Override
	public Deck addDeck(Deck deck, String username) {
		deck.setUser(userService.findByUsername(username));
		return deckRepository.saveAndFlush(deck);
	}

	@Override
	public List<Deck> findByUser(User user) {
		return deckRepository.findByUser(user);
	}

	@Override
	public Deck findById(Long id) {
		return deckRepository.findOne(id);
	}


	@Override
	public void delete(long id) {
		deckRepository.delete(id);
	}

	@Override
	public Deck findByName(String name) {
		return deckRepository.findByName(name);
	}

	@Override
	public Deck editDeck(Deck deck, Card card, Integer amount) {
		Map<Card, Integer> deckCards = deck.getCards();

		if (deckCards.isEmpty()) {
			Map<Card, Integer> newDeck = new HashMap<>();
			newDeck.put(card, amount);
			deck.setCards(newDeck);
		} else {
			Map<Card, Integer> resultMap = new HashMap<>();
			boolean isAdded = false;
			for (Map.Entry<Card, Integer> entry : deckCards.entrySet()) {
				if (card.equals(entry.getKey())) {
					resultMap.put(entry.getKey(), amount + entry.getValue());
					isAdded = true;
				} else {
					resultMap.put(entry.getKey(), entry.getValue());
				}
			}
			if(!isAdded){
				resultMap.put(card, amount);
			}
			logger.info("RESULT MAO IS: " + resultMap);
			deck.setCards(resultMap);
		}

		return deckRepository.saveAndFlush(deck);
	}

	@Override
	public List<Deck> getAll() {
		return deckRepository.findAll();
	}

	@Override
	public Deck updateDeckCards(Deck deck, Map<Card, Integer> cards) {
		deck.setCards(cards);
		return deckRepository.saveAndFlush(deck);
	}
}
