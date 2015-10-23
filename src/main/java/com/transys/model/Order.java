package com.transys.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name="transysOrder")
public class Order extends AbstractBaseModel {

	// TODO: Do we need table size restriction here?
	
	/******** Customer Info ************/
	@ManyToOne
	@JoinColumn(name="customerId") 
	private Customer customer;
	
	/******** Delivery Info ************/
	@Column(name="deliveryContactName")
	private String deliveryContactName;
	
	@Column(name="deliveryContactPhone1")
	private String deliveryContactPhone1;

	@Column(name="deliveryContactPhone2")
	private String deliveryContactPhone2;
	
	@Column(name="deliveryDate")
	private Date deliveryDate;
	
	@Column(name="deliveryHourFrom")
	private String deliveryHourFrom;
	
	@Column(name="deliveryMinutesFrom")
	private String deliveryMinutesFrom;
	
	@Column(name="deliveryHourTo")
	private String deliveryHourTo;
	
	@Column(name="deliveryMinutesTo")
	private String deliveryMinutesTo;
	
	@ManyToOne
	@JoinColumn(name="deliveryAddressId")
	private DeliveryAddress deliveryAddress;
	
	@ManyToOne
	@JoinColumn(name="materialTypeId") 
	private MaterialType materialType;
	
	/*@ManyToOne
	@JoinColumn(name="materialCategoryId") 
	private MaterialCategory materialCategory;*/
	
	@Column(name="pickupDate")
	private Date pickupDate;
	
	/******** Permits Info ************/
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
     name = "orderPermits",
     joinColumns = @JoinColumn(name = "orderId"),
     inverseJoinColumns = @JoinColumn(name = "permitId")
     //uniqueConstraints = @UniqueConstraint(columnNames = {"orderId", "permitId"})
	)
	private List<Permit> permits;
	 
	public List<Permit> getPermits() {
		return permits;
	}
	
	public void  setPermits(List<Permit> permits) {
	    this.permits = permits;
	}
	
	/******** Notes Info ************/
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
	/*@JoinTable(
     name = "orderNotes",
     joinColumns = @JoinColumn(name = "orderId")
	)*/
	private List<OrderNotes> orderNotes;
	 
	public List<OrderNotes> getOrderNotes() {
		return orderNotes;
	}
	
	public void setOrderNotes(List<OrderNotes> orderNotes) {
		this.orderNotes = orderNotes;
	}
	
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
	/*@JoinTable(
     name = "OrderPayment",
     joinColumns = @JoinColumn(name = "orderId")
	)*/
	private List<OrderPayment> orderPayment;
	
	@Column(name="totalAmountPaid")
	private BigDecimal totalAmountPaid;
	
	@Column(name="balanceAmountDue")
	private BigDecimal balanceAmountDue;

	public BigDecimal getTotalAmountPaid() {
		return totalAmountPaid;
	}

	public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}
	
	public BigDecimal getBalanceAmountDue() {
		return balanceAmountDue;
	}

	public void setBalanceAmountDue(BigDecimal balanceAmountDue) {
		this.balanceAmountDue = balanceAmountDue;
	}

	@OneToOne(mappedBy="order", cascade = CascadeType.ALL)
	private OrderFees orderFees;

	public List<OrderPayment> getOrderPayment() {
		return orderPayment;
	}

	public void setOrderPayment(List<OrderPayment> orderPayment) {
		this.orderPayment = orderPayment;
	}

	public OrderFees getOrderFees() {
		return orderFees;
	}

	public void setOrderFees(OrderFees orderFees) {
		this.orderFees = orderFees;
	}

	@ManyToOne
	@JoinColumn(name="pickupDriverId") 
	private User pickupDriver;
	
	@ManyToOne
	@JoinColumn(name="dropOffDriverId") 
	private User dropOffDriver;
	
	public User getPickupDriver() {
		return pickupDriver;
	}

	public void setPickupDriver(User pickupDriver) {
		this.pickupDriver = pickupDriver;
	}

	public User getDropOffDriver() {
		return dropOffDriver;
	}

	public void setDropOffDriver(User dropOffDriver) {
		this.dropOffDriver = dropOffDriver;
	}

	/******** Dumpster Info ************/
	@ManyToOne
	@JoinColumn(name="locationTypeId") 
	private LocationType dumpsterLocation;
	
	@ManyToOne
	@JoinColumn(name="dumpsterId") 
	private Dumpster dumpster;
	
	@Column(name="pickupOrderId")
	private Long pickupOrderId;
	
	public Long getPickupOrderId() {
		return pickupOrderId;
	}

	public void setPickupOrderId(Long pickupOrderId) {
		this.pickupOrderId = pickupOrderId;
	}

	/******** Weight Info ************/
	/*@JoinColumn(name="weightInfo")
	private OrderWeightInfo weightInfo;*/
	
	@Column(name="grossWeight")
	private BigDecimal grossWeight;
	
	@Column(name="netWeightLb")
	private BigDecimal netWeightLb;
	
	@Column(name="netWeightTonnage")
	private BigDecimal netWeightTonnage;
	
	@Column(name="tare")
	private BigDecimal tare;
	
	/******** Driver Info ************/
	/*@JoinColumn(name="driverInfo")
	private OrderDriverInfo driverInfo;*/

	/******** Order Status ************/
	@ManyToOne
	@JoinColumn(name="orderStatusId") //Enum?
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name="dumpsterSizeId") //Enum?
	private DumpsterSize dumpsterSize;
	
	/******** Order Notes ************/
	/*@JoinColumn(name="notes")
	private OrderNotes notes;*/

	public Customer getCustomer() {
		return customer;
	}

	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}

	public BigDecimal getNetWeightLb() {
		return netWeightLb;
	}

	public void setNetWeightLb(BigDecimal netWeightLb) {
		this.netWeightLb = netWeightLb;
	}

	public BigDecimal getNetWeightTonnage() {
		return netWeightTonnage;
	}

	public void setNetWeightTonnage(BigDecimal netWeightTonnage) {
		this.netWeightTonnage = netWeightTonnage;
	}

	public BigDecimal getTare() {
		return tare;
	}

	public void setTare(BigDecimal tare) {
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
	
	@Transient
	public String getFormattedDeliveryDate() {
		if (deliveryDate == null) {
			return StringUtils.EMPTY;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(deliveryDate);
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getDeliveryHourFrom() {
		return deliveryHourFrom;
	}

	public void setDeliveryHourFrom(String deliveryHourFrom) {
		this.deliveryHourFrom = deliveryHourFrom;
	}

	public String getDeliveryMinutesFrom() {
		return deliveryMinutesFrom;
	}

	public void setDeliveryMinutesFrom(String deliveryMinutesFrom) {
		this.deliveryMinutesFrom = deliveryMinutesFrom;
	}

	public String getDeliveryHourTo() {
		return deliveryHourTo;
	}

	public void setDeliveryHourTo(String deliveryHourTo) {
		this.deliveryHourTo = deliveryHourTo;
	}

	public String getDeliveryMinutesTo() {
		return deliveryMinutesTo;
	}

	public void setDeliveryMinutesTo(String deliveryMinutesTo) {
		this.deliveryMinutesTo = deliveryMinutesTo;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
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
	
	/*
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

	public DumpsterSize getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(DumpsterSize dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus status) {
		this.orderStatus = status;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public Dumpster getDumpster() {
		return dumpster;
	}

	public void setDumpster(Dumpster dumpster) {
		this.dumpster = dumpster;
	}

	/*public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}*/

	/*public OrderNotes getNotes() {
		return notes;
	}

	public void setNotes(OrderNotes notes) {
		this.notes = notes;
	}*/
}
