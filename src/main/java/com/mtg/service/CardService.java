package com.mtg.service;

import com.mtg.entity.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {
	Card addCard(Card card);

	void delete(long id);

	Card findByName(String name);

	Card editCard(Card card);

	List<Card> getAll();
}
