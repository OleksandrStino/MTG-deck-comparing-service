package com.mtg.entity;

import com.mtg.entity.dto.CardDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "card")
public class Card {

	@Id
	private long multiverseid;

	@Column
	private String name;

	public Card() {
	}

	public Card(CardDTO cardDto) {
		this.multiverseid = cardDto.getMultiverseid();
		this.name = cardDto.getName();
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

	@Override
	public String toString() {
		return "Card{" +
				"multiverseid=" + multiverseid +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Card card = (Card) o;

		if (multiverseid != card.multiverseid) return false;
		return name.equals(card.name);
	}

	@Override
	public int hashCode() {
		int result = (int) (multiverseid ^ (multiverseid >>> 32));
		result = 31 * result + name.hashCode();
		return result;
	}
}
