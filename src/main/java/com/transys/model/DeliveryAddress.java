package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.transys.core.util.FormatUtil;

@Entity
@Table(name="deliveryAddress")
public class DeliveryAddress extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="customerId")
	@JsonBackReference
	private Customer customer;
	
	@Column(name="line1")
	private String line1;
	
	@Column(name="line2")
	private String line2;
	
	@Column(name="city")
	private String city;
	
	@ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@Column(name="zip")
	private String zipcode;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	@Transient
	public String getCustomerName() {
		return getCustomer() == null ? StringUtils.EMPTY : getCustomer().getCompanyName();
	}
	
	@Transient
	public String getStateStr() {
		return getState() == null ? StringUtils.EMPTY : getState().getName();
	}
	
	@Transient
	public String getFullLine() {
		return FormatUtil.formatAddress(getLine1(), getLine2());
	}
	
	@Transient
	public String getFullDeliveryAddress() {
		return getFullDeliveryAddress(", "); 
	}
	
	@Transient
	public String getFullDeliveryAddress(String lineSeparator) {
		return FormatUtil.formatAddress(getLine1(), getLine2(), getCity(), getState(), getZipcode(),
				lineSeparator);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	@Override
	public String toString() {
		return getFullDeliveryAddress();
	}
}
