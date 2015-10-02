package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="permitClass")
public class PermitClass extends AbstractBaseModel {

	@Column(name="permitClass")
	private String permitClass;
	
	@Column(name="comments")
	private String comments;

	public String getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(String permitClass) {
		this.permitClass = permitClass;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
