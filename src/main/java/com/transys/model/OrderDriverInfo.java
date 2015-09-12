package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="orderDriverInfo")
public class OrderDriverInfo extends AbstractBaseModel {

	@Column(name="orderID")
	private String orderID;
	
	@JoinColumn(name="pickUpDriver")
	private Employee pickUpDriver;
	
	@JoinColumn(name="dropOffDriver")
	private Employee dropoffDriver;

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public Employee getPickUpDriver() {
		return pickUpDriver;
	}

	public void setPickUpDriver(Employee pickUpDriver) {
		this.pickUpDriver = pickUpDriver;
	}

	public Employee getDropoffDriver() {
		return dropoffDriver;
	}

	public void setDropoffDriver(Employee dropoffDriver) {
		this.dropoffDriver = dropoffDriver;
	}
	
}
