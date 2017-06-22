package com.sortable.similarity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
	
	@JsonProperty("product_name")
	private String productName;
	private String manufacturer;
	private String model;
	private String family;
	@JsonProperty("announced-date")
	private Date announcedDate;
	public String getProductName() {
		return productName.toLowerCase();
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getManufacturer() {
		return manufacturer.toLowerCase();
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public Date getAnnouncedDate() {
		return announcedDate;
	}
	public void setAnnouncedDate(Date announcedDate) {
		this.announcedDate = announcedDate;
	}
	
	public String toString(){
		return productName;
	}
}
