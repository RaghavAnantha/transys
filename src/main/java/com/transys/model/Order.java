package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="order")
public class Order extends AbstractBaseModel {

	// TODO: Do we need table size restriction here?
	
	/******** Customer Info ************/
	@Column(name="customerID") // TODO: Establish FK relationship
	private String customerID;
	
	/******** Delivery Info ************/
	@Column(name="deliverycontactname")
	private String deliveryContactName;
	
	@Column(name="deliverycontactphone1")
	private String deliveryContactPhone1;

	@Column(name="deliverycontactphone2")
	private String deliveryContactPhone2;
	
	@Column(name="deliverydate")
	private String deliveryDate;
	
	@Column(name="deliveryHour1")
	private Integer deliveryHour1;
	
	@Column(name="deliveryAMPM1")
	private String deliveryAMPM1;
	
	@Column(name="deliveryMin1")
	private Integer deliveryMin1;
	
	@Column(name="deliveryHour2")
	private Integer deliveryHour2;
	
	@Column(name="deliveryAMPM2")
	private String deliveryAMPM2;
	
	@Column(name="deliveryMin2")
	private Integer deliveryMin2;
	
	@JoinColumn(name="address")
	private Integer deliveryAddress;

	/******** Dumpster Info ************/
	
	@Column(name="dumpsterLocation")
	private String dumpsterLocation;
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name="typeOfMaterial")
	private String typeOfMaterial;
	
	/******** Pricing Info ************/
	
	@Column(name="dumpsterPrice")
	private Double dumpsterPrice;
	
	@Column(name="cityFee")
	private Double cityFee;
	
	@Column(name="additionalFees")
	private Double additionalFees;
	
	/******** Payment Info ************/

	@Column(name="methodOfPayment")
	private String methodOfPayment;
	
	@Column(name="referenceNum")
	private String referenceNum;
	
	/******** Order Notes ************/

	@Column(name="notes")
	private Integer notesID;
}
