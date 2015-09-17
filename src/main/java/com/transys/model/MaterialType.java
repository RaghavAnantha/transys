package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name="locationType")
public class MaterialType extends AbstractBaseModel {

	//@Column(name="locationType")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
