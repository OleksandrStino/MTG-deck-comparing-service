package com.mtg.entity;

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
