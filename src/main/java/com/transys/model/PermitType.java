package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="permitType")
public class PermitType extends AbstractBaseModel {

	@Column(name="type")
	private String type;
	
	@Column(name="daysAllowed")
	private String daysAllowed;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDaysAllowed() {
		return daysAllowed;
	}

	public void setDaysAllowed(String daysAllowed) {
		this.daysAllowed = daysAllowed;
	}
	
}
