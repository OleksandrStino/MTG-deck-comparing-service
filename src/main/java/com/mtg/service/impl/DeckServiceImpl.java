package com.mtg.service.impl;

import com.mtg.entity.Deck;
import com.mtg.entity.User;
import com.mtg.repository.DeckRepository;
import com.mtg.service.DeckService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService {

	private Logger logger = Logger.getLogger(DeckServiceImpl.class);

	@Autowired
	private DeckRepository deckRepository;

	@Autowired
	private UserServiceImpl userService;

	private Principal principal;

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	@Override
	public Deck addDeck(Deck deck, String username) {
		logger.info("Principal is: " + username);
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
	public Deck editDeck(Deck card) {
		return deckRepository.saveAndFlush(card);
	}

	@Override
	public List<Deck> getAll() {
		return deckRepository.findAll();
	}
}
