package com.mtg.service.impl;

import com.mtg.entity.User;
import com.mtg.repository.UserRepository;
import com.mtg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User addUser(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(long id) {
		userRepository.delete(id);
	}

	@Override
	public User findByName(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public User editBank(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}
}
