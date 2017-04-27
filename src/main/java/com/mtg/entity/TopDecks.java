package com.mtg.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "existed_decks")
public class TopDecks {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	@Column
	private String name;

	@Column
	private String deck;

	@Column
	private String event;

	@Column
	private String rank;

	@Column(name = "deck_url")
	private String deckUrl;

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

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getDeckUrl() {
		return deckUrl;
	}

	public void setDeckUrl(String deckUrl) {
		this.deckUrl = deckUrl;
	}

	@Override
	public String toString() {
		return "TopDecks{" +
				"id=" + id +
				", name='" + name + '\'' +
				", deck='" + deck + '\'' +
				", event='" + event + '\'' +
				", rank='" + rank + '\'' +
				", deckUrl='" + deckUrl + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TopDecks)) return false;

		TopDecks topDecks = (TopDecks) o;

		if (id != topDecks.id) return false;
		if (name != null ? !name.equals(topDecks.name) : topDecks.name != null) return false;
		return event != null ? event.equals(topDecks.event) : topDecks.event == null;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (event != null ? event.hashCode() : 0);
		return result;
	}
}
