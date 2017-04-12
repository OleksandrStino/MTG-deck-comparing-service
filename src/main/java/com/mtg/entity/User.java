package com.mtg.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name= "increment", strategy= "increment")
	private long id;

	@Column
	private String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Deck> decks = new ArrayList<>();

	/*@Column
	private String password;

	@Transient
	private String confirmPassword;
*/
	public User() {
	}

	public User(String name) {
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

	/*public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}*/
}
