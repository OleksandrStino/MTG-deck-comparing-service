package com.mtg.controller;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.service.impl.CardServiceImpl;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.utility.JsonParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

	@GetMapping("/decks/{deckId}/bulkAdd")
	public String bulkAddRedirect(@PathVariable Long deckId, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("deckId", deckId);
		return "bulk_add_page";

	}

	@PostMapping("/decks/{deckId}/bulkAdd")
	public String bulkAdd(@RequestParam String cardRows, @PathVariable Long deckId,
			RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
		model.addAttribute("deckId", deckId);
		List<String> errorLines = new ArrayList<>();
		String[] lines = cardRows.split(System.getProperty("line.separator"));
		for (String currentLine : lines) {

			int amount = Integer.parseInt(currentLine.substring(0, 1));
			String cardName = currentLine.substring(1).trim();
			System.out.println(amount + " x " + cardName);
			String url = CARD_NAME_URL.concat(cardName.replaceAll(" ", "_"));
			try {
				Set<Card> cardList = jsonParser.getCardFromUrl(new URL(url));

				if (cardList.isEmpty()) {
					System.out.println("Not found card " + cardName);
					errorLines.add(currentLine);
				} else if (cardList.size() == 1) {
					System.out.println("Found 1 card" + cardName);
					Card newCard = (Card) cardList.toArray()[0];
					for (int i = amount; i > 0; i--) {
						cardService.addCard(newCard);
					}

				} else {

					boolean sameCards = true;
					Card randomCard = cardList.iterator().next();
					for (Card currentCard : cardList) {
						if (!currentCard.getName().equals(randomCard.getName())) {
							sameCards = false;
						}
					}

					System.out.println("Found many cards " + " all are the same?" + sameCards + cardName + " : "
							+ cardList.toString());

					if (sameCards) {

						Card newCard = (Card) cardList.toArray()[0];
						for (int i = amount; i > 0; i--) {
							cardService.addCard(newCard);
						}

					} else {
						errorLines.add(currentLine);
					}

				}

			} catch (IOException e) {
				logger.error(e.getMessage());
			}

		}
		if(errorLines.isEmpty()){
			return "redirect:" + request.getContextPath() + "/decks/" + deckId;
		}
		model.addAttribute("errorLines", errorLines);
		return "bulk_add_page";

	}

	@PostMapping("/decks/{deckId}/addCard")
	public String addCard(@RequestParam String cardName, @RequestParam Integer amount, @PathVariable Long deckId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName.replaceAll(" ", "_"));
		Deck deck = deckService.findById(deckId);

		try {
			// get Cards from URL
			Set<Card> cardList = jsonParser.getCardFromUrl(new URL(url));
			if (cardList.isEmpty()) {
				// TODO: return error
				System.out.println("EMPTY!!!!!!!!!!");
			}

			// if there is only one card in set we add it to the deck and card
			// db tables
			if (cardList.size() == 1) {
				Card newCard = (Card) cardList.toArray()[0];
				cardService.addCard(newCard);
				deckService.editDeck(deck, newCard, amount);
			} else {
				logger.info("added card list: " + cardList);
				// add list of cards with same name or with different sets
				redirectAttributes.addFlashAttribute("cardList", cardList);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return "redirect:" + request.getContextPath() + "/decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/addCardFromList")
	public String addCardFromList(@RequestParam String cardName, @RequestParam Integer amount, @RequestParam String set,
			@PathVariable Long deckId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName.replaceAll(" ", "_"));

		try {
			Set<Card> cardList = jsonParser.getCardFromUrl(new URL(url));
			cardList.forEach(choosenCard -> {
				// chose the card from list with requested setName than we add
				// it to the deck and card db tables
				if (choosenCard.getSetName().equals(set)) {
					cardService.addCard(choosenCard);
					deckService.editDeck(deckService.findById(deckId), choosenCard, amount);
				}
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return "redirect: /decks/" + deckId;
	}

	@PostMapping("/decks/{deckId}/{cardId}/removeCard")
	public String removeCardFromDeck(@PathVariable Long deckId, @PathVariable Long cardId,
			@RequestParam Integer amount) {
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

}
