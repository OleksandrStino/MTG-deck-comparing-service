package com.mtg.repository;

import com.mtg.entity.TopDecks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopDecksRepository extends JpaRepository<TopDecks, Long> {

}
