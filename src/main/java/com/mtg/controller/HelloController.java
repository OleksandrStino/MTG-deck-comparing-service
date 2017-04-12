package com.mtg.controller;

import com.mtg.entity.Card;
import com.mtg.entity.Deck;
import com.mtg.entity.User;
import com.mtg.entity.dto.CardDTO;
import com.mtg.service.impl.CardServiceImpl;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.service.impl.UserServiceImpl;
import com.mtg.utility.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private CardServiceImpl cardService;

	@Autowired
	private DeckServiceImpl deckService;

	private String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private JsonParser jsonParser;


	@GetMapping("/")
	public String initialMethod() {
		return "hello";
	}

	@PostMapping("/")
	public String addUser(@RequestParam String name, Model model) {
		if (null != name && null == userService.findByName(name)) {
			User user = new User(name);
			userService.addUser(user);
			model.addAttribute("user", user);
		}
		if (null != name && null != userService.findByName(name)) {
			model.addAttribute("message", "user already exist");
		} else {
			model.addAttribute("message", "user name is empty");
		}

		return "hello";
	}

	@GetMapping("/addCart")
	public String addCardGet() {
		return "hello";
	}

	@PostMapping("/addCart")
	public String addCard(@RequestParam String cardName, @RequestParam Long id, Model model) {
		if (null != cardName) {
			Card card = new Card();
			card.setName(cardName);
			card.setMultiverseid(id);
			cardService.addCard(card);
			model.addAttribute("card", card);
		}

		return "hello";
	}

	@GetMapping("/addDeck")
	public String addDeckGet() {
		return "hello";
	}

	@PostMapping("/addDeck")
	public String addDeck(@RequestParam String name, Model model) {
		if (null != name) {
			Deck deck = new Deck();
			deck.setName(name);
			List<Card> list = new ArrayList<>();
			list.add(cardService.findByName("as"));
			deck.setCards(list);
			deck.setUser(userService.findByName("as"));
			deckService.addDeck(deck);
			model.addAttribute("deck", deck);
		}

		return "hello";
	}


	@GetMapping("/search")
	public String searchCard() {
		return "home";
	}

	@PostMapping("/search")
	public String searchCardPost(@RequestParam String searchedCardName, Model model) {
		String url = CARD_NAME_URL.concat(searchedCardName);

		CardDTO card = null;
		try {
			card = jsonParser.getCardFromUrl(new URL(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("card", card);
		return "home";
	}
}
