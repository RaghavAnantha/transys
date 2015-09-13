package com.transys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="trans_order")
public class Order extends AbstractBaseModel {

	// TODO: Do we need table size restriction here?
	
	/******** Customer Info ************/
	/*@JoinColumn(name="custID") 
	private Customer customerID;*/
	
	/******** Delivery Info ************/
	@Column(name="deliverycontactname")
	private String deliveryContactName;
	
	@Column(name="deliverycontactphone1")
	private String deliveryContactPhone1;

	@Column(name="deliverycontactphone2")
	private String deliveryContactPhone2;
	
	@Column(name="deliverydate")
	private Date deliveryDate;
	
	@Column(name="deliveryTimeFrom")
	private Date deliveryTimeFrom;
	
	@Column(name="deliveryTimeTo")
	private Date deliveryTimeTo;
	
	/*@JoinColumn(name="deliveryAddress")
	private Address deliveryAddress;*/
	
	@Column(name="typeOfMaterial")
	private String typeOfMaterial;
	
	@Column(name="pickupDate")
	private Date pickupDate;
	
	/******** Permits Info ************/
	/*@JoinColumn(name="permits")
	private OrderPermits permits;*/

	/******** Dumpster Info ************/
	@Column(name="dumpsterLocation")
	private String dumpsterLocation;
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name="dumpsterNum")
	private String dumpsterNum;
	
	/******** Payment Info ************/
	/*@JoinColumn(name="paymentInfo")
	private OrderPaymentInfo paymentInfo;*/
	
	/******** Weight Info ************/
	/*@JoinColumn(name="weightInfo")
	private OrderWeightInfo weightInfo;*/
	
	/******** Driver Info ************/
	/*@JoinColumn(name="driverInfo")
	private OrderDriverInfo driverInfo;*/

	/******** Order Status ************/
	@ManyToOne
	@JoinColumn(name="orderStatus") //Enum?
	private OrderStatus orderStatus;
	
	/******** Order Notes ************/
	/*@JoinColumn(name="notes")
	private OrderNotes notes;*/

	/*public Customer getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Customer customerID) {
		this.customerID = customerID;
	}*/

	public String getDeliveryContactName() {
		return deliveryContactName;
	}

	public void setDeliveryContactName(String deliveryContactName) {
		this.deliveryContactName = deliveryContactName;
	}

	public String getDeliveryContactPhone1() {
		return deliveryContactPhone1;
	}

	public void setDeliveryContactPhone1(String deliveryContactPhone1) {
		this.deliveryContactPhone1 = deliveryContactPhone1;
	}

	public String getDeliveryContactPhone2() {
		return deliveryContactPhone2;
	}

	public void setDeliveryContactPhone2(String deliveryContactPhone2) {
		this.deliveryContactPhone2 = deliveryContactPhone2;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDeliveryTimeFrom() {
		return deliveryTimeFrom;
	}

	public void setDeliveryTimeFrom(Date deliveryTimeFrom) {
		this.deliveryTimeFrom = deliveryTimeFrom;
	}

	public Date getDeliveryTimeTo() {
		return deliveryTimeTo;
	}

	public void setDeliveryTimeTo(Date deliveryTimeTo) {
		this.deliveryTimeTo = deliveryTimeTo;
	}

	/*public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}*/

	public String getTypeOfMaterial() {
		return typeOfMaterial;
	}

	public void setTypeOfMaterial(String typeOfMaterial) {
		this.typeOfMaterial = typeOfMaterial;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	/*public OrderPermits getPermits() {
		return permits;
	}

	public void setPermits(OrderPermits permits) {
		this.permits = permits;
	}*/

	public String getDumpsterLocation() {
		return dumpsterLocation;
	}

	public void setDumpsterLocation(String dumpsterLocation) {
		this.dumpsterLocation = dumpsterLocation;
	}

	public String getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public String getDumpsterNum() {
		return dumpsterNum;
	}

	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}

	/*public OrderPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(OrderPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public OrderWeightInfo getWeightInfo() {
		return weightInfo;
	}

	public void setWeightInfo(OrderWeightInfo weightInfo) {
		this.weightInfo = weightInfo;
	}

	public OrderDriverInfo getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(OrderDriverInfo driverInfo) {
		this.driverInfo = driverInfo;
	}*/

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus status) {
		this.orderStatus = status;
	}

	/*public OrderNotes getNotes() {
		return notes;
	}

	public void setNotes(OrderNotes notes) {
		this.notes = notes;
	}*/
}
