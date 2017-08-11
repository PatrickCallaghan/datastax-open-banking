package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "user")
public class User {

	@PartitionKey
	@Column(name="user_name")
	private String userName;
	
	private String email;
	private String password;
	
	@Column(name="display_name")
	private String displayName;
	
	public User(){}

	public User(String userName, String email, String password, String displayName) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.displayName = displayName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", email=" + email + ", password=" + password + ", displayName="
				+ displayName + "]";
	}
}
