package com.sortable.similarity.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
	@JsonProperty("product_name")
	String productName;
	List<Listing> listings;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Listing> getListings() {
		return listings;
	}

	public void setListings(List<Listing> listings) {
		this.listings = listings;
	}
}
