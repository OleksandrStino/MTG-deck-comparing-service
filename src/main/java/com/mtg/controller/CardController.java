package com.mtg.controller;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.service.impl.CardServiceImpl;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.utility.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CardController {

	@Autowired
	private CardServiceImpl cardService;

	@Autowired
	private DeckServiceImpl deckService;

	private String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private JsonParser jsonParser;

	private Map<Card, Integer> cards = new HashMap<>();

	@PostMapping("/decks/{deckId}/addCard")
	public String addCard(@RequestParam String cardName, @RequestParam Integer amount, @PathVariable Long deckId,
						  HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName);
		Card card = cardService.findByName(cardName);

		if (null != card) {
			cardService.comparingOfAddedAndExistedCardsInSession(cards, card, amount);
		} else {
			Card newCard;
			try {
				newCard = jsonParser.getCardFromUrl(new URL(url));
				cardService.addCard(newCard);
				cardService.comparingOfAddedAndExistedCardsInSession(cards, newCard, amount);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.getSession().setAttribute("mapOfCards", cards);
		return "redirect: /decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/{cardId}/removeCard")
	public String removeCard(@PathVariable Long deckId, @PathVariable Long cardId, @RequestParam Integer amount) {
		Deck deck = deckService.findById(deckId);
		Map<Card, Integer> currentDeckCards = deck.getCards();
		Card card = cardService.findByMultiverseid(cardId);
		Integer amountOfSameCards = currentDeckCards.get(card);
		if (amount >= amountOfSameCards) {
			currentDeckCards.remove(card);
		} else {
			currentDeckCards.put(card, amountOfSameCards - amount);
		}
		deckService.updateDeckCards(deck, currentDeckCards);
		return "redirect: /decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/{cardId}/removeCardFromBuffer")
	public String removeCardFromBuffer(@PathVariable Long deckId, @PathVariable Long cardId, @RequestParam Integer amount) {
		Card card = cardService.findByMultiverseid(cardId);
		Integer amountOfSameCards = cards.get(card);
		if (amount >= amountOfSameCards) {
			cards.remove(card);
		} else {
			cards.put(card, amountOfSameCards - amount);
		}
		return "redirect: /decks/" + deckId;
	}

}
