package com.mtg.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "deck")
public class Deck {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	@Column
	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "card_deck",
			joinColumns = @JoinColumn(name = "deck_id"))
	@MapKeyJoinColumn(name = "card_id")
	@Column(name = "count")
	private Map<Card, Integer> cards;


	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Deck() {
	}

	public Deck(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Card, Integer> getCards() {
		return cards;
	}

	public void setCards(Map<Card, Integer> cards) {
		this.cards = cards;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Deck{" +
				"id=" + id +
				", name='" + name + '\'' +
				", cards=" + cards +
				", user=" + user +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Deck)) return false;

		Deck deck = (Deck) o;

		if (id != deck.id) return false;
		return name != null ? name.equals(deck.name) : deck.name == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
