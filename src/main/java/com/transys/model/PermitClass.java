package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="permitClass")
public class PermitClass extends AbstractBaseModel {

	@Column(name="permitClass")
	private String permitClass;

	public String getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(String permitClass) {
		this.permitClass = permitClass;
	}
	
	
}
