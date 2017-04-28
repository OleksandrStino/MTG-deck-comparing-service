package com.mtg.controller;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.service.impl.CardServiceImpl;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.utility.JsonParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class CardController {

	private final Logger logger = Logger.getLogger(CardController.class);

	@Autowired
	private CardServiceImpl cardService;

	@Autowired
	private DeckServiceImpl deckService;

	private final String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private JsonParser jsonParser;

	//Map of cards and their amount in buffer before user update his deck
	private Map<Card, Integer> cards = new HashMap<>();

	@PostMapping("/decks/{deckId}/addCard")
	public String addCard(@RequestParam String cardName, @RequestParam Integer amount, @PathVariable Long deckId,
						  RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName.replaceAll(" ", "_"));

		try {
			//get Cards from URL
			Set<Card> cardList = jsonParser.getCardFromUrl(new URL(url));
			//if there is only one card in set we add it to the deck and card db tables
			if (cardList.size() == 1) {
				Card newCard = (Card) cardList.toArray()[0];
				cardService.addCard(newCard);
				cardService.comparingOfAddedAndExistedCardsInSession(cards, newCard, amount);

			} else {
				logger.info("added card list: " + cardList);
				//add list of cards with same name or with different sets
				redirectAttributes.addFlashAttribute("cardList", cardList);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		request.getSession().setAttribute("mapOfCards", cards);
		return "redirect:" + request.getContextPath() + "/decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/addCardFromList")
	public String addCardFromList(@RequestParam String cardName, @RequestParam Integer amount, @RequestParam String set, @PathVariable Long deckId,
						  RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName.replaceAll(" ", "_"));

		try {
			Set<Card> cardList = jsonParser.getCardFromUrl(new URL(url));
			cardList.forEach(choosenCard -> {
				//chose the card from list with requested setName than we add it to the deck and card db tables
				if(choosenCard.getSetName().equals(set)){
					cardService.addCard(choosenCard);
					cardService.comparingOfAddedAndExistedCardsInSession(cards, choosenCard, amount);
				}
			} );
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		request.getSession().setAttribute("mapOfCards", cards);
		return "redirect: /decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/updateDeck")
	public String updateDeck(@PathVariable Long deckId, HttpServletRequest request) {
		Deck deck = deckService.findById(deckId);
		Map<Card, Integer> mapOfCards = (Map<Card, Integer>) request.getSession().getAttribute("mapOfCards");
		logger.info("map Of cards: " + mapOfCards);
		if(null != mapOfCards){
			deckService.editDeck(deck, mapOfCards);
			mapOfCards.clear();
		}
		return "redirect: /decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/{cardId}/removeCard")
	public String removeCardFromDeck(@PathVariable Long deckId, @PathVariable Long cardId, @RequestParam Integer amount) {
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
