package com.transys.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="trans_order")
public class Order extends AbstractBaseModel {

	// TODO: Do we need table size restriction here?
	
	/******** Customer Info ************/
	@ManyToOne
	@JoinColumn(name="custID") 
	private Customer customer;
	
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
	
	@ManyToOne
	@JoinColumn(name="deliveryAddressId")
	private Address deliveryAddress;
	
	/*@ManyToOne
	@JoinColumn(name="materialTypeId") 
	private MaterialType materialType;*/
	
	@Column(name="materialType")
	private String materialType;
	
	@Column(name="pickupDate")
	private Date pickupDate;
	
	/******** Permits Info ************/
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	        name = "orderPermits",
	        joinColumns = @JoinColumn(name = "orderID"),
	        inverseJoinColumns = @JoinColumn(name = "permitID"),
	        uniqueConstraints = @UniqueConstraint(columnNames = {"orderID", "permitID"})
	)
	private List<Permit> permits;
	 
	public List<Permit> getPermits() {
		return permits;
	}
	
	public void  setPermits(List<Permit> permits) {
	    this.permits = permits;
	}

	/******** Dumpster Info ************/
	@ManyToOne
	@JoinColumn(name="locationTypeId") 
	private LocationType dumpsterLocation;
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name="dumpsterPrice")
	private Double dumpsterPrice;
	
	@ManyToOne
	@JoinColumn(name="dumpsterId") 
	private DumpsterInfo dumpster;
	
	/******** Payment Info ************/
	/*@JoinColumn(name="paymentInfo")
	private OrderPaymentInfo paymentInfo;*/
	
	/******** Weight Info ************/
	/*@JoinColumn(name="weightInfo")
	private OrderWeightInfo weightInfo;*/
	
	@Column(name="grossWeight")
	private Double grossWeight;
	
	@Column(name="netWeightLb")
	private Double netWeightLb;
	
	@Column(name="netWeightTonnage")
	private Double netWeightTonnage;
	
	@Column(name="tare")
	private Double tare;
	
	/******** Driver Info ************/
	/*@JoinColumn(name="driverInfo")
	private OrderDriverInfo driverInfo;*/

	/******** Order Status ************/
	@ManyToOne
	@JoinColumn(name="orderStatusId") //Enum?
	private OrderStatus orderStatus;
	
	/******** Order Notes ************/
	/*@JoinColumn(name="notes")
	private OrderNotes notes;*/

	public Customer getCustomer() {
		return customer;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeightLb() {
		return netWeightLb;
	}

	public void setNetWeightLb(Double netWeightLb) {
		this.netWeightLb = netWeightLb;
	}

	public Double getNetWeightTonnage() {
		return netWeightTonnage;
	}

	public void setNetWeightTonnage(Double netWeightTonnage) {
		this.netWeightTonnage = netWeightTonnage;
	}

	public Double getTare() {
		return tare;
	}

	public void setTare(Double tare) {
		this.tare = tare;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

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

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
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

	public LocationType getDumpsterLocation() {
		return dumpsterLocation;
	}

	public void setDumpsterLocation(LocationType dumpsterLocation) {
		this.dumpsterLocation = dumpsterLocation;
	}

	public String getDumpsterSize() {
		return dumpsterSize;
	}
	
	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
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

	/*public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}*/

	public Double getDumpsterPrice() {
		return dumpsterPrice;
	}

	public void setDumpsterPrice(Double dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}

	public DumpsterInfo getDumpster() {
		return dumpster;
	}

	public void setDumpster(DumpsterInfo dumpster) {
		this.dumpster = dumpster;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	/*public OrderNotes getNotes() {
		return notes;
	}

	public void setNotes(OrderNotes notes) {
		this.notes = notes;
	}*/
}
