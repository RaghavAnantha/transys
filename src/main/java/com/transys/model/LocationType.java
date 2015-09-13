package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="locationType")
public class LocationType extends AbstractBaseModel {

	@Column(name="locationType")
	private String locationType;

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	
}
