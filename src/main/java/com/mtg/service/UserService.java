package com.mtg.service;


import com.mtg.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
	User addUser(User bank);

	void delete(long id);

	User findByName(String name);

	User editUser(User user);

	List<User> getAll();
}

