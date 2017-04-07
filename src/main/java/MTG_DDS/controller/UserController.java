package MTG_DDS.controller;

import MTG_DDS.entities.UserDTO;
import MTG_DDS.repositories.implementation.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.apache.log4j.Logger;

@Controller
public class UserController {

	private Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserRepositoryImpl userRepository;

	@GetMapping("/registration")
	public String registration(Model model) {
		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userName") String userName, @ModelAttribute("password") String password,
							   Model model) {

		logger.info("!!!!!!!!! User name is: " + userName);
		logger.info("!!!!!!!!! User password is: " + password);
		logger.info("!!!!!!!!! User is: " + userRepository.findUserByName(userName));
		logger.info("!!!!!!!!! Condition is: " + (userRepository.findUserByName(userName) == null));
		if(userRepository.findUserByName(userName) == null){
			UserDTO user = new UserDTO(userName, password);
			logger.info("!!!!!!!!! user is: " + user);
			userRepository.saveUser(user);
			logger.info("!!!!!!!!! user added !!!!!!!!!!!!!!!!!!!");
			return "search";
		}else {
			logger.info("!!!!!!!!! user not added !!!!!!!!!!!!!!!!!!!");
			return "registration";
		}
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}
}