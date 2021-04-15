package com.transys.model.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.transys.model.AbstractBaseModel;

@Entity
@Table(name="geocode")
public class Geocode extends AbstractBaseModel {
	@Transient
	public static final String geocodeAlreadyExistsMsg = "Geocode already exists";
	@Transient
	public static final String geocodeSavedSuccessfullyMsg = "Geocode saved successfully";
	
	@Column(name="address")
	private String address;
	
	@Column(name="latLng")
	private String latLng;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatLng() {
		return latLng;
	}

	public void setLatLng(String latLng) {
		this.latLng = latLng;
	}

	@Override
	public String toString() {
		return getLatLng();
	}
}
