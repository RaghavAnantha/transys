package com.transys.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="permit")
public class Permit  extends AbstractBaseModel {

	@ManyToOne
	@JoinColumn(name="customerID")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="type")
	private PermitType type;
	
	@ManyToOne
	@JoinColumn(name="class")
	private PermitClass permitClass;
	
	@Column(name="number")
	private String number;
	
	@Column(name="fee")
	private double fee;
	
	@Column(name="startDate")
	private Date startDate;
	
//	private String endDate;
	
	@Column(name="permitAddress")
	private String permitAddress;
	
	@ManyToOne
	@JoinColumn(name="deliveryAddress")
	private Address deliveryAddress;
	
	@ManyToOne
	@JoinColumn(name="locationType")
	private LocationType locationType;
	
	@ManyToOne
	@JoinColumn(name="status")
	private PermitStatus status;
	
	@Column(name="comments")
	private String comments;

	@Column(name="parkingMeter")
	private String parkingMeter;

	/*public void setEndDate(String endDate) {
		
		if (this.startDate != null) {
			// TODO: start date + permit type # days
			this.endDate = this.startDate;
		}
		
	}
	
	public String getEndDate() {
		return this.endDate;
	}*/

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public PermitType getType() {
		return type;
	}

	public void setType(PermitType type) {
		this.type = type;
	}

	public PermitClass getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(PermitClass permitClass) {
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPermitAddress() {
		return permitAddress;
	}

	public void setPermitAddres(String permitAddres) {
		this.permitAddress = permitAddres;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
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

	public String getParkingMeter() {
		return parkingMeter;
	}

	public void setParkingMeter(String parkingMeter) {
		this.parkingMeter = parkingMeter;
	}
	
}
