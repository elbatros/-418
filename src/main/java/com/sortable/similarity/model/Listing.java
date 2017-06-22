package com.sortable.similarity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Listing {

	
	private String title;
	private String manufacturer;
	private String currency;
	private String price;
	
	public String getTitle() {
		return title.toLowerCase();
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getManufacturer() {
		return manufacturer.toLowerCase();
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	
	public String toString(){
		return title;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
