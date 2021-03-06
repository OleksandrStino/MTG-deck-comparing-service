package com.mtg.controller;

import com.mtg.entity.Deck;
import com.mtg.entity.TopDecks;
import com.mtg.entity.User;
import com.mtg.service.impl.DeckServiceImpl;
import com.mtg.service.impl.TopDecksServiceImpl;
import com.mtg.service.impl.UserServiceImpl;
import com.mtg.utility.DeckComparator;
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
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class DeckController {

	private final Logger logger = Logger.getLogger(DeckController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private DeckServiceImpl deckService;

	@Autowired
	private TopDecksServiceImpl existedDecksService;

	@Autowired
	private DeckComparator deckComparator;

	@GetMapping("/")
	public String addDeckGet(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("decks", deckService.findByUser(userService.findByUsername(username)));
		return "index";
	}

	@PostMapping("/decks")
	public String addDeck(@RequestParam String name, Model model, Principal principal) {
		Deck deck;
		String username = principal.getName();
		User user = userService.findByUsername(username);
		if (!name.isEmpty() && user != null) {
			deck = deckService.addDeck(new Deck(name), username);
			model.addAttribute("decks", deckService.findByUser(user));
			return "redirect:/decks/" + deck.getId();
		}

		return "index";
	}

	@GetMapping("/decks/{deckId}")
	public String deckDetailPage(@PathVariable Long deckId, Model model) {
		model.addAttribute("deck", deckService.findById(deckId));
		return "deck_detail_page";
	}

	@GetMapping("/decks/{deckId}/remove")
	public String deleteProject(@PathVariable Long deckId) {
		deckService.delete(deckId);
		return "redirect:/";
	}

	@GetMapping("/decks/{deckId}/compareDeck")
	public String compareDeck(@PathVariable Long deckId, Model model, HttpServletRequest request) {
		Deck deck = deckService.findById(deckId);
		List<TopDecks> topDecks = existedDecksService.findAll();
		Map<TopDecks, Integer> mapOfComparingResult = deckComparator.getCardMatches(deck.getCards(), topDecks);
		model.addAttribute("mapOfComparingResult", mapOfComparingResult);
		return "compare";
	}

}
