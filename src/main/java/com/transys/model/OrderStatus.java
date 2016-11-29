package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="orderStatus")
public class OrderStatus extends AbstractBaseModel {
	@Transient
	public static final String ORDER_STATUS_OPEN = "Open";
	@Transient
	public static final String ORDER_STATUS_DROPPED_OFF = "Dropped Off";
	@Transient
	public static final String ORDER_STATUS_PICK_UP = "Pick Up";
	@Transient
	public static final String ORDER_STATUS_CLOSED = "Closed";
	@Transient
	public static final String ORDER_STATUS_CANCELED = "Canceled";

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
