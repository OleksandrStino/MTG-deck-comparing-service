package com.mtg.repository;

import com.mtg.entity.Deck;
import com.mtg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long>{
	Deck findByName(String name);

	List<Deck> findByUser(User user);
}
