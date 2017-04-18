package com.mtg.service.impl;

import com.mtg.entity.Card;
import com.mtg.repository.CardRepository;
import com.mtg.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService{

	@Autowired
	private CardRepository cardRepository;

	@Override
	public Card addCard(Card card) {
		return cardRepository.saveAndFlush(card);
	}

	@Override
	public Card findByMultiverseid(Long id) {
		return cardRepository.findOne(id);
	}

	@Override
	public void delete(long id) {
		cardRepository.delete(id);
	}

	@Override
	public Card findByName(String name) {
		return cardRepository.findByName(name);
	}

	@Override
	public Card editCard(Card card) {
		return cardRepository.saveAndFlush(card);
	}

	@Override
	public List<Card> getAll() {
		return cardRepository.findAll();
	}

	public void comparingOfAddedAndExistedCardsInSession(Map<Card, Integer> cards, Card card, Integer amount) {
		cards.forEach((k, v) -> {
			if (k.equals(card)) {
				cards.put(card, amount + v);
			}
		});
		if (!cards.containsKey(card)) {
			cards.put(card, amount);
		}
	}
}
