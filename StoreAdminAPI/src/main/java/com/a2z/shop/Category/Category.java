package com.a2z.shop.Category;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category 
{
	@JsonProperty("categoryId")
	private String categoryId;
	
	@JsonProperty("categoryName")
  private String categoryName;
	
	@JsonProperty("categoryDescription")
  private String categoryDescription;
	
	@JsonProperty("vendorId")
	private int vendorId;
	
	@JsonProperty("categoryStatus")
	private String categoryStatus;
	
	@JsonProperty("create_ts")
	private Date create_ts;
	
	@JsonProperty("update_ts")
	private Date update_ts;
	
	public Category() {}

	public Category(String categoryId, String categoryName, String categoryDescription, int vendorId,
			String categoryStatus, Date create_ts, Date update_ts) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.categoryDescription = categoryDescription;
		this.vendorId = vendorId;
		this.categoryStatus = categoryStatus;
		this.create_ts = create_ts;
		this.update_ts = update_ts;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public String getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(String categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public Date getCreate_ts() {
		return create_ts;
	}

	public void setCreate_ts(Date create_ts) {
		this.create_ts = create_ts;
	}

	public Date getUpdate_ts() {
		return update_ts;
	}

	public void setUpdate_ts(Date update_ts) {
		this.update_ts = update_ts;
	}

	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", categoryDescription="
				+ categoryDescription + ", vendorId=" + vendorId + ", categoryStatus=" + categoryStatus + ", create_ts="
				+ create_ts + ", update_ts=" + update_ts + "]";
	}

			
	
}
