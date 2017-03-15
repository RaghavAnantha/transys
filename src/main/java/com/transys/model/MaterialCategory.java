package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="materialCategory")
public class MaterialCategory extends AbstractBaseModel {
	@Transient
	public static final String MATERIAL_CATEGORY_STREET_SWEEPINGS = "CCSS Street Sweepings";
	
	@Column(name="category")
	private String category;

	@Column(name="comments")
	private String comments;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
