package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.transys.core.util.FormatUtil;

@Entity
@Table(name="permitAddress")
public class PermitAddress extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="permitId")
	@JsonBackReference
	private Permit permit;
	
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

	public Permit getPermit() {
		return permit;
	}

	public void setPermit(Permit permit) {
		this.permit = permit;
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
	public String getFullLine() {
		return FormatUtil.formatAddress(getLine1(), getLine2());
	}
	
	@Transient
	public String getFullPermitAddress() {
		return getFullPermitAddress(", ");
	}
	
	@Transient
	public String getFullPermitAddress(String lineSeparator) {
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
		return getFullPermitAddress();
	}
}
