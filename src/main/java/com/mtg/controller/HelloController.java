package com.mtg.controller;

import com.mtg.entity.User;
import com.mtg.entity.dto.CardDTO;
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
public class HelloController {

	@Autowired
	private UserServiceImpl userService;

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

	@GetMapping("/")
	public String searchCard() {
		return "home";
	}

	@PostMapping
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
