package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="permitType")
public class PermitType extends AbstractBaseModel {

	@Column(name="permitType")
	private String permitType;

	public String getPermitType() {
		return permitType;
	}

	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}
	
	
}
