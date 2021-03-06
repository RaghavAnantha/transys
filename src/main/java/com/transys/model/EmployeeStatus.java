package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="employeeStatus")
public class EmployeeStatus extends AbstractBaseModel {
	@Transient
	public static final String ACTIVE_STATUS = "Active";
	@Transient
	public static final String INACTIVE_STATUS = "Inactive";
	
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
