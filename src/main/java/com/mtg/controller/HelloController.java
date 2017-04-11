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
public class HelloController {

	@Autowired
	private UserServiceImpl userService;

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

}
