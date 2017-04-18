package com.mtg.controller;

import com.mtg.entity.Card;
import com.mtg.entity.dto.CardDTO;
import com.mtg.service.impl.CardServiceImpl;
import com.mtg.utility.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	private String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private JsonParser jsonParser;

	private Map<Card, Integer> cards = new HashMap<>();

	@PostMapping("/decks/{deckId}/addCard")
	public String addCard(@RequestParam String cardName, @RequestParam Integer amount, @PathVariable Long deckId,
						  Model model, HttpServletRequest request) {
		String url = CARD_NAME_URL.concat(cardName);
		Card card = cardService.findByName(cardName);

		if (null != card) {
			cardService.comparingOfAddedAndExistedCardsInSession(cards, card, amount);
		} else {
			CardDTO cardDto;
			try {
				cardDto = jsonParser.getCardFromUrl(new URL(url));
				Card newCard = new Card(cardDto);
				cardService.addCard(newCard);
				cardService.comparingOfAddedAndExistedCardsInSession(cards, newCard, amount);
				model.addAttribute("card", cardDto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.getSession().setAttribute("mapOfCards", cards);
		return "redirect: /decks/" + deckId;
	}

}
