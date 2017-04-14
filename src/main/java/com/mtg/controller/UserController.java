package com.mtg.controller;

import com.mtg.entity.Card;
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

@Controller
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private CardServiceImpl cardService;

	@Autowired
	private DeckServiceImpl deckService;

	private String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private JsonParser jsonParser;


	/*@GetMapping("/")
	public String initialMethod() {
		return "index";
	}*/

	@GetMapping("/register")
	public String registration() {
		return "register";
	}

	@PostMapping("/register")
	public String addUser(@RequestParam String username, @RequestParam String password, Model model) {
		if (null != username && null == userService.findByUsername(username)) {
			User user = new User(username);
			user.setPassword(password);
			user.setEnabled(true);
			userService.addUser(user);
			model.addAttribute("user", user);
		}
		if (null != username && null != userService.findByUsername(username)) {
			model.addAttribute("message", "user already exist");
		} else {
			model.addAttribute("message", "user name is empty");
		}

		return "register";
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

		return "index";
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
		return "index";
	}
}
