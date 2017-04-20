package com.mtg.service.impl;

import com.mtg.entity.TopDecks;
import com.mtg.repository.TopDecksRepository;
import com.mtg.service.TopDecskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopDecksServiceImpl implements TopDecskService {

	@Autowired
	private TopDecksRepository topDecksRepository;

	@Override
	public TopDecks addDeck(TopDecks deck) {
		return topDecksRepository.saveAndFlush(deck);
	}

	@Override
	public List<TopDecks> findAll() {
		return topDecksRepository.findAll();
	}
}
