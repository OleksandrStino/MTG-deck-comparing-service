package com.mtg.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
public class Card {

	@Id
	private long multiverseid;

	@Column
	private String name;

	@ManyToMany(mappedBy = "cards")
	private List<Deck> decks = new ArrayList<>();

	public Card() {
	}

	public List<Deck> getDecks() {
		return decks;
	}

	public void setDecks(List<Deck> decks) {
		this.decks = decks;
	}

	public long getMultiverseid() {
		return multiverseid;
	}

	public void setMultiverseid(long multiverseid) {
		this.multiverseid = multiverseid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
