package com.mtg.controller;

import com.mtg.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	@Autowired
	private UserServiceImpl userService;

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@PostMapping("/login")
	public String loginPagePost(){
		return "index";
	}
}