package com.servlet.model;

import java.util.UUID;

public class Item {
	private UUID id;
	private String data;
	
	public Item(UUID id, String data) {
		this.id = id;
		this.data = data;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public String getData() { 
		return this.data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
