package com.mtg.service.impl;

import com.mtg.entity.Deck;
import com.mtg.repository.DeckRepository;
import com.mtg.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {
	@Autowired
	private DeckRepository deckRepository;

	@Override
	public Deck addDeck(Deck card) {
		return deckRepository.saveAndFlush(card);
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
	public Deck editDeck(Deck card) {
		return deckRepository.saveAndFlush(card);
	}

	@Override
	public List<Deck> getAll() {
		return deckRepository.findAll();
	}
}
