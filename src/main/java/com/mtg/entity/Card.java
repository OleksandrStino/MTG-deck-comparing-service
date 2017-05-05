package com.mtg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {

	@Id
	private long multiverseid;

	@Column
	private String name;

	@Column(name = "image")
	private String imageUrl;

	@Column
	private String type;

	@Column
	private String rarity;

	@Column(name = "set_name")
	private String setName;

	@Column
	private String set;

	@Column
	private String legality;

	@Column
	private String text;

	@Column(name = "mana_cost")
	private String manaCost;


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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getLegality() {
		return legality;
	}

	public void setLegality(String legality) {
		this.legality = legality;
	}

	public String getManaCost() {
		return manaCost;
	}

	public void setManaCost(String manaCost) {
		this.manaCost = manaCost;
	}

	@Override
	public String toString() {
		return "Card{" +
				"multiverseid=" + multiverseid +
				", name='" + name + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", type='" + type + '\'' +
				", rarity='" + rarity + '\'' +
				", setName='" + setName + '\'' +
				", set='" + set + '\'' +
				", legality='" + legality + '\'' +
				", text='" + text + '\'' +
				", manaCost='" + manaCost + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Card)) return false;

		Card card = (Card) o;

		if (name != null ? !name.equals(card.name) : card.name != null) return false;
		return setName != null ? setName.equals(card.setName) : card.setName == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (setName != null ? setName.hashCode() : 0);
		return result;
	}
}
