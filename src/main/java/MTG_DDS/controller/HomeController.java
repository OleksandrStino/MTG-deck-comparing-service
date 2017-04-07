package MTG_DDS.controller;


import MTG_DDS.entities.CardDTO;
import MTG_DDS.service.JsonParser;
import MTG_DDS.services.RedisService.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URL;

@Controller
public class HomeController {


	private String CARD_NAME_URL = "https://api.magicthegathering.io/v1/cards?name=";

	@Autowired
	private RedisService redisService;

	@Autowired
	private JsonParser jsonParser;

	@GetMapping("/")
	public String initialMethod() {
		return "home";
	}

	@PostMapping
	public String projectSearchByTitle(@RequestParam String searchedCardName, Model model) {
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
