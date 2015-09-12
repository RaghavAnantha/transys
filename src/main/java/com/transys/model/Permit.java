package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="permit")
public class Permit  extends AbstractBaseModel {

	@JoinColumn(name="type")
	private PermitType type;
	
	@Column(name="class")
	private String permitClass;
	
	@Column(name="number")
	private String number;
	
	@Column(name="fee")
	private double fee;
	
	@Column(name="startDate")
	private String startDate;
	
	@Column(name="endDate")
	private String endDate;
	
	@Column(name="address")
	private String address;
	
	@Column(name="street")
	private String street;
	
	@Column(name="locationType")
	private String locationType;
	
	@JoinColumn(name="status")
	private PermitStatus status;
	
	@Column(name="comments")
	private String comments;

	public PermitType getType() {
		return type;
	}

	public void setType(PermitType type) {
		this.type = type;
	}

	public String getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(String permitClass) {
		this.permitClass = permitClass;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public PermitStatus getStatus() {
		return status;
	}

	public void setStatus(PermitStatus status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
