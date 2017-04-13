package com.mtg.service.impl;

import com.mtg.entity.User;
import com.mtg.repository.UserRepository;
import com.mtg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	private void passwordEncoder(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}

	@Override
	public User addUser(User user) {
		passwordEncoder(user);
		user.setRole("ROLE_USER");
		return userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(long id) {
		userRepository.delete(id);
	}

	@Override
	public User findByUsername(String name) {
		return userRepository.findByUsername(name);
	}

	@Override
	public User editUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}
}
