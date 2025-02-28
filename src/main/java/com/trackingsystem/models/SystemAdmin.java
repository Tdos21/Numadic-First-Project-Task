package com.trackingsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class SystemAdmin {
	
	
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
	
	String username;
	String password;
	
	
	//Default Constructor
	public SystemAdmin() {
		
	}
	
	
	//Paramterized Constructor
	public SystemAdmin(Integer id, String username, String password) {
		
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	
	//getters and setters
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Admin [id=" + id + ", username=" + username + ", password=" + password + "]";
	}
}

