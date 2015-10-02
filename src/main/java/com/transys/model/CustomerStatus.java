package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customerStatus")
public class CustomerStatus extends AbstractBaseModel {

	// TODO: Make an ENUM?
	@Column(name="status")
	private String status;
	
	@Column(name="comments")
	private String comments;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
