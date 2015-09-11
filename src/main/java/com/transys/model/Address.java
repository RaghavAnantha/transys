package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="address")
public class Address extends AbstractBaseModel {

	@Column(name="custID")
	private String customerID;
	
	@Column(name="line1")
	private String line1;
	
	@Column(name="line2")
	private String line2;
	
	@Column(name="city")
	private String city;
	
	@JoinColumn(name="state")
	private State state;
	
	@Column(name="zip")
	private String zipcode;
	
}
