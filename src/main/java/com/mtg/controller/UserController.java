package com.mtg.controller;

import com.mtg.entity.User;
import com.mtg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@Autowired
	private UserServiceImpl userService;

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
			return "login";
		}
		if (null != username && null != userService.findByUsername(username)) {
			model.addAttribute("message", "user already exist");
		} else {
			model.addAttribute("message", "user name is empty");
		}
		return "index";
	}
}
