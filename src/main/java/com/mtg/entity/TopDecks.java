package com.mtg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "existed_decks")
public class TopDecks {

	@Id
	private String name;

	@Column
	private String deck;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeck() {
		return deck;
	}

	public void setDeck(String deck) {
		this.deck = deck;
	}

	@Override
	public String toString() {
		return "TopDecks{" +
				"name='" + name + '\'' +
				", deck='" + deck + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TopDecks)) return false;

		TopDecks topDecks = (TopDecks) o;

		return name.equals(topDecks.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
