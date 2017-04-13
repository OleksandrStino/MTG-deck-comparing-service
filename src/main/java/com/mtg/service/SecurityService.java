package com.mtg.service;

import org.springframework.stereotype.Service;

@Service
public interface SecurityService {

	String findLoggedInUsername();

	void autologin(String username, String password);
}