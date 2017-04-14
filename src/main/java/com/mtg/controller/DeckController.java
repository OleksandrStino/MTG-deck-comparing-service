package com.mtg.controller;

import com.mtg.entity.Deck;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class DeckController {

	private final Logger logger = Logger.getLogger(DeckController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private DeckServiceImpl deckService;

	@GetMapping("/")
	public String addDeckGet(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("decks", deckService.findByUser(userService.findByUsername(username)));
		return "index";
	}

	@PostMapping("/decks")
	public String addDeck(@RequestParam String name, Model model, Principal principal) {
		Deck deck;
		if (!name.isEmpty()) {
			String username = principal.getName();
			deck = deckService.addDeck(new Deck(name), username);
			model.addAttribute("decks", deckService.findByUser(userService.findByUsername(username)));
			return "redirect:/decks/" + deck.getId();
		}

		return "index";
	}

	@GetMapping("/decks/{deckId}")
	public String deckDetailPage(@PathVariable Long deckId, Model model){
		model.addAttribute("deck", deckService.findById(deckId));
		return "deck_detail_page";
	}

	@GetMapping("/decks/{deckId}/remove")
	public String deleteProject(@PathVariable Long deckId) {
		deckService.delete(deckId);
		return "redirect:/";
	}
}
