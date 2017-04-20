package com.mtg.service;

import com.mtg.entity.TopDecks;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopDecskService {
	TopDecks addDeck(TopDecks deck);

	List<TopDecks> findAll();
}
