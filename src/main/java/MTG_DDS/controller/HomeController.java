package MTG_DDS.controller;


import MTG_DDS.services.MTG_Service.MTGService;
import MTG_DDS.services.RedisService.RedisService;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	private RedisService redisService;

	@Autowired
	private MTGService mtgService;

	@GetMapping("/")
	public String initialMethod(ModelMap model) {
		Card card = CardAPI.getCard(7169);
		String imageName = card.getImageName();
		String text = card.getText();
		List<String> allCards = mtgService.getAllCards();
		String imageUrl = allCards.get(0);
		redisService.setValue("key", "MTG");

		if(imageName==null){
			imageName="null";
		}
		model.addAttribute("imageUrl", imageUrl);
		model.addAttribute("cards", allCards);
		model.addAttribute("text", text);
		model.addAttribute("key", redisService.getValue("key"));
		model.addAttribute("image", imageName);
		model.addAttribute("card", card);

		return "home";
	}

}
