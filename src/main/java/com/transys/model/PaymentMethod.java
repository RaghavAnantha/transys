package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="paymentMethod")
public class PaymentMethod extends AbstractBaseModel {
	
	@Column(name="method")
	private String method;
	
	@Column(name="comments")
	private String comments;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
