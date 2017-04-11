package com.mtg.entity.dto;

import java.util.List;

public class CardDTO {

	private String name;
	private String imageUrl;
	private String type;
	private List<String> types;
	private String rarity;
	private String setName;
	private String text;

	public CardDTO() {
	}

	public CardDTO(String name, String imageUrl, String type, List<String> types, String rarity, String setName, String text) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.type = type;
		this.types = types;
		this.rarity = rarity;
		this.setName = setName;
		this.text = text;
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

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
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

	@Override
	public String toString() {
		return "CardDTO{" +
				"name='" + name + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", type='" + type + '\'' +
				", types=" + types +
				", rarity='" + rarity + '\'' +
				", setName='" + setName + '\'' +
				", text='" + text + '\'' +
				'}';
	}
}