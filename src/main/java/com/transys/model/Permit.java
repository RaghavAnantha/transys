package com.transys.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="permit")
public class Permit  extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="customerId")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="permitType")
	private PermitType permitType;
	
	@ManyToOne
	@JoinColumn(name="permitClass")
	private PermitClass permitClass;
	
	@OneToMany(mappedBy="permit", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PermitAddress> permitAddress;
	
	@Column(name="number")
	private String number;
	
	@Column(name="fee")
	private BigDecimal fee;
	
	@Column(name="startDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date startDate;
	
	@Column(name="endDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date endDate;
	
	@ManyToOne
	@JoinColumn(name="deliveryAddressId")
	private DeliveryAddress deliveryAddress;
	
	@ManyToOne
	@JoinColumn(name="locationType")
	private LocationType locationType;
	
	@ManyToOne
	@JoinColumn(name="status")
	private PermitStatus status;
	
	@Column(name="parkingMeter")
	private String parkingMeter;
	
	@Column(name="parkingMeterFee")
	private BigDecimal parkingMeterFee;
	
	@Transient
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public PermitType getPermitType() {
		return permitType;
	}

	public void setPermitType(PermitType permitType) {
		this.permitType = permitType;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}

	public List<PermitAddress> getPermitAddress() {
		return permitAddress;
	}

	public void setPermitAddress(List<PermitAddress> permitAddress) {
		this.permitAddress = permitAddress;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
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

	public String getParkingMeter() {
		return parkingMeter;
	}

	public void setParkingMeter(String parkingMeter) {
		this.parkingMeter = parkingMeter;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getParkingMeterFee() {
		return parkingMeterFee;
	}

	public void setParkingMeterFee(BigDecimal parkingMeterFee) {
		this.parkingMeterFee = parkingMeterFee;
	}
}
