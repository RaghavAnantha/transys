package com.transys.model.invoice;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.transys.core.util.FormatUtil;

import com.transys.model.AbstractBaseModel;

@Entity
@Table(name="orderInvoiceDetails")
public class OrderInvoiceDetails extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="invoiceId") 
	private OrderInvoiceHeader invoiceHeader;
	
	@Column(name="orderId")
	private Long orderId;
	
	@Column(name="orderDate")
	private Date orderDate;
	
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
	
	@Column(name="deliveryAddressId")
	private Long deliveryAddressId;
	
	@Column(name="deliveryAddressLine1")
	private String deliveryAddressLine1;
	
	@Column(name="deliveryAddressLine2")
	private String deliveryAddressLine2;
	
	@Column(name="deliveryAddressCity")
	private String deliveryAddressCity;
	
	@Column(name="deliveryAddressState")
	private String deliveryAddressState;
	
	@Column(name="deliveryAddressZip")
	private String deliveryAddressZip;
	
	@Column(name="materialType") 
	private String materialType;
	
	@Column(name="materialCategory") 
	private String materialCategory;
	
	@Column(name="pickupDate")
	private Date pickupDate;
	
	@Column(name="pickupDriver") 
	private String pickupDriver;
	
	@Column(name="dropOffDriver") 
	private String dropOffDriver;
	
	@Column(name="locationType") 
	private String locationType;
	
	@Column(name="pickupOrderId")
	private Long pickupOrderId;
	
	@Column(name="grossWeight")
	private BigDecimal grossWeight;
	
	@Column(name="netWeightLb")
	private BigDecimal netWeightLb;
	
	@Column(name="netWeightTonnage")
	private BigDecimal netWeightTonnage;
	
	@Column(name="tare")
	private BigDecimal tare;
	
	@Column(name="scaleTicketNumber")
	private String scaleTicketNumber;
	
	@Column(name="orderStatus") 
	private String orderStatus;
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name = "dumpsterPrice")
	private BigDecimal dumpsterPrice;
	
	@Column(name = "cityFee")
	private BigDecimal cityFee;
	
	@Column(name = "cityFeeSuburbName")
	private String cityFeeSuburbName;
	
	@Column(name = "permitNum1")
	private String permitNum1;
	
	@Column(name = "permitNum2")
	private String permitNum2;
	  
	@Column(name = "permitNum3")
	private String permitNum3;
	
	@Column(name = "permitFee1")
	private BigDecimal permitFee1;
	
	@Column(name = "permitFee2")
	private BigDecimal permitFee2;
	
	@Column(name = "permitFee3")
	private BigDecimal permitFee3;
	
	@Column(name = "permitType1")
	private String permitType1;
	
	@Column(name = "permitType2")
	private String permitType2;
	
	@Column(name = "permitType3")
	private String permitType3;
	
	@Column(name = "permitClass1")
	private String permitClass1;
	
	@Column(name = "permitClass2")
	private String permitClass2;
	
	@Column(name = "permitClass3")
	private String permitClass3;
	
	@Column(name = "totalPermitFees")
	private BigDecimal totalPermitFees;

	@Column(name = "overweightFee")
	private BigDecimal overweightFee;

	@Column(name = "overweightTonLimit")
	private BigDecimal overweightTonLimit;

	@Column(name = "additionalFee1Desc")
	private String additionalFee1Desc;
	
	@Column(name = "additionalFee1")
	private BigDecimal additionalFee1;
	
	@Column(name = "additionalFee2Desc")
	private String additionalFee2Desc;
	
	@Column(name = "additionalFee2")
	private BigDecimal additionalFee2;
	
	@Column(name = "additionalFee3Desc")
	private String additionalFee3Desc;
	
	@Column(name = "additionalFee3")
	private BigDecimal additionalFee3;
	
	@Column(name = "tonnageFees")
	private BigDecimal tonnageFees;

	@Column(name = "totalAdditionalFees")
	private BigDecimal totalAdditionalFees;
	
	@Column(name = "discountAmount")
	private BigDecimal discountAmount;

	@Column(name = "totalFees")
	private BigDecimal totalFees;
	
	@Column(name = "totalAmountPaid")
	private BigDecimal totalAmountPaid;

	@Column(name = "balanceAmountDue")
	private BigDecimal balanceAmountDue;
	
	@Column(name = "paymentMethod1")
	private String paymentMethod1;
	
	@Column(name = "paymentDate1")
	private Date paymentDate1;
	
	@Column(name = "paymentAmount1")
	private BigDecimal paymentAmount1;
	
	@Column(name = "paymentCCRefNum1")
	private String paymentCCRefNum1;
	
	@Column(name = "paymentCheckNum1")
	private String paymentCheckNum1;
	
	@Column(name = "paymentMethod2")
	private String paymentMethod2;
	
	@Column(name = "paymentDate2")
	private Date paymentDate2;
	
	@Column(name = "paymentAmount2")
	private BigDecimal paymentAmount2;
	
	@Column(name = "paymentCCRefNum2")
	private String paymentCCRefNum2;
	
	@Column(name = "paymentCheckNum2")
	private String paymentCheckNum2;
	
	@Column(name = "paymentMethod3")
	private String paymentMethod3;
	
	@Column(name = "paymentDate3")
	private Date paymentDate3;

	@Column(name = "paymentAmount3")
	private BigDecimal paymentAmount3;
	
	@Column(name = "paymentCCRefNum3")
	private String paymentCCRefNum3;
	
	@Column(name = "paymentCheckNum3")
	private String paymentCheckNum3;
	   
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

	public Long getDeliveryAddressId() {
		return deliveryAddressId;
	}

	public void setDeliveryAddressId(Long deliveryAddressId) {
		this.deliveryAddressId = deliveryAddressId;
	}

	public String getDeliveryAddressLine1() {
		return deliveryAddressLine1;
	}

	public void setDeliveryAddressLine1(String deliveryAddressLine1) {
		this.deliveryAddressLine1 = deliveryAddressLine1;
	}

	public String getDeliveryAddressLine2() {
		return deliveryAddressLine2;
	}

	public void setDeliveryAddressLine2(String deliveryAddressLine2) {
		this.deliveryAddressLine2 = deliveryAddressLine2;
	}

	public String getDeliveryAddressCity() {
		return deliveryAddressCity;
	}

	public void setDeliveryAddressCity(String deliveryAddressCity) {
		this.deliveryAddressCity = deliveryAddressCity;
	}

	public String getDeliveryAddressState() {
		return deliveryAddressState;
	}

	public void setDeliveryAddressState(String deliveryAddressState) {
		this.deliveryAddressState = deliveryAddressState;
	}

	public String getDeliveryAddressZip() {
		return deliveryAddressZip;
	}

	public void setDeliveryAddressZip(String deliveryAddressZip) {
		this.deliveryAddressZip = deliveryAddressZip;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialCategory() {
		return materialCategory;
	}

	public void setMaterialCategory(String materialCategory) {
		this.materialCategory = materialCategory;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

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

	public BigDecimal getDumpsterPrice() {
		return dumpsterPrice;
	}

	public void setDumpsterPrice(BigDecimal dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}

	public String getPickupDriver() {
		return pickupDriver;
	}

	public void setPickupDriver(String pickupDriver) {
		this.pickupDriver = pickupDriver;
	}

	public String getDropOffDriver() {
		return dropOffDriver;
	}

	public void setDropOffDriver(String dropOffDriver) {
		this.dropOffDriver = dropOffDriver;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public Long getPickupOrderId() {
		return pickupOrderId;
	}

	public void setPickupOrderId(Long pickupOrderId) {
		this.pickupOrderId = pickupOrderId;
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

	public String getScaleTicketNumber() {
		return scaleTicketNumber;
	}

	public void setScaleTicketNumber(String scaleTicketNumber) {
		this.scaleTicketNumber = scaleTicketNumber;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}
	
	public OrderInvoiceHeader getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(OrderInvoiceHeader invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getCityFee() {
		return cityFee;
	}

	public void setCityFee(BigDecimal cityFee) {
		this.cityFee = cityFee;
	}

	public String getCityFeeSuburbName() {
		return cityFeeSuburbName;
	}

	public void setCityFeeSuburbName(String cityFeeSuburbName) {
		this.cityFeeSuburbName = cityFeeSuburbName;
	}

	public String getPermitNum1() {
		return permitNum1;
	}

	public void setPermitNum1(String permitNum1) {
		this.permitNum1 = permitNum1;
	}

	public String getPermitNum2() {
		return permitNum2;
	}

	public void setPermitNum2(String permitNum2) {
		this.permitNum2 = permitNum2;
	}

	public String getPermitNum3() {
		return permitNum3;
	}

	public void setPermitNum3(String permitNum3) {
		this.permitNum3 = permitNum3;
	}

	public BigDecimal getPermitFee1() {
		return permitFee1;
	}

	public void setPermitFee1(BigDecimal permitFee1) {
		this.permitFee1 = permitFee1;
	}

	public BigDecimal getPermitFee2() {
		return permitFee2;
	}

	public void setPermitFee2(BigDecimal permitFee2) {
		this.permitFee2 = permitFee2;
	}

	public BigDecimal getPermitFee3() {
		return permitFee3;
	}

	public void setPermitFee3(BigDecimal permitFee3) {
		this.permitFee3 = permitFee3;
	}
	
	public String getPermitType1() {
		return permitType1;
	}

	public void setPermitType1(String permitType1) {
		this.permitType1 = permitType1;
	}

	public String getPermitType2() {
		return permitType2;
	}

	public void setPermitType2(String permitType2) {
		this.permitType2 = permitType2;
	}

	public String getPermitType3() {
		return permitType3;
	}

	public void setPermitType3(String permitType3) {
		this.permitType3 = permitType3;
	}

	public String getPermitClass1() {
		return permitClass1;
	}

	public void setPermitClass1(String permitClass1) {
		this.permitClass1 = permitClass1;
	}

	public String getPermitClass2() {
		return permitClass2;
	}

	public void setPermitClass2(String permitClass2) {
		this.permitClass2 = permitClass2;
	}

	public String getPermitClass3() {
		return permitClass3;
	}

	public void setPermitClass3(String permitClass3) {
		this.permitClass3 = permitClass3;
	}

	public BigDecimal getTotalPermitFees() {
		return totalPermitFees;
	}

	public void setTotalPermitFees(BigDecimal totalPermitFees) {
		this.totalPermitFees = totalPermitFees;
	}

	public BigDecimal getOverweightFee() {
		return overweightFee;
	}

	public void setOverweightFee(BigDecimal overweightFee) {
		this.overweightFee = overweightFee;
	}

	public BigDecimal getOverweightTonLimit() {
		return overweightTonLimit;
	}

	public void setOverweightTonLimit(BigDecimal overweightTonLimit) {
		this.overweightTonLimit = overweightTonLimit;
	}

	public String getAdditionalFee1Desc() {
		return additionalFee1Desc;
	}

	public void setAdditionalFee1Desc(String additionalFee1Desc) {
		this.additionalFee1Desc = additionalFee1Desc;
	}

	public BigDecimal getAdditionalFee1() {
		return additionalFee1;
	}

	public void setAdditionalFee1(BigDecimal additionalFee1) {
		this.additionalFee1 = additionalFee1;
	}

	public String getAdditionalFee2Desc() {
		return additionalFee2Desc;
	}

	public void setAdditionalFee2Desc(String additionalFee2Desc) {
		this.additionalFee2Desc = additionalFee2Desc;
	}

	public BigDecimal getAdditionalFee2() {
		return additionalFee2;
	}

	public void setAdditionalFee2(BigDecimal additionalFee2) {
		this.additionalFee2 = additionalFee2;
	}

	public String getAdditionalFee3Desc() {
		return additionalFee3Desc;
	}

	public void setAdditionalFee3Desc(String additionalFee3Desc) {
		this.additionalFee3Desc = additionalFee3Desc;
	}

	public BigDecimal getAdditionalFee3() {
		return additionalFee3;
	}

	public void setAdditionalFee3(BigDecimal additionalFee3) {
		this.additionalFee3 = additionalFee3;
	}

	public BigDecimal getTotalAdditionalFees() {
		return totalAdditionalFees;
	}

	public void setTotalAdditionalFees(BigDecimal totalAdditionalFees) {
		this.totalAdditionalFees = totalAdditionalFees;
	}

	public BigDecimal getTonnageFees() {
		return tonnageFees;
	}

	public void setTonnageFees(BigDecimal tonnageFees) {
		this.tonnageFees = tonnageFees;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}

	public String getPaymentMethod1() {
		return paymentMethod1;
	}

	public void setPaymentMethod1(String paymentMethod1) {
		this.paymentMethod1 = paymentMethod1;
	}

	public Date getPaymentDate1() {
		return paymentDate1;
	}

	public void setPaymentDate1(Date paymentDate1) {
		this.paymentDate1 = paymentDate1;
	}

	public BigDecimal getPaymentAmount1() {
		return paymentAmount1;
	}

	public void setPaymentAmount1(BigDecimal paymentAmount1) {
		this.paymentAmount1 = paymentAmount1;
	}

	public String getPaymentCCRefNum1() {
		return paymentCCRefNum1;
	}

	public void setPaymentCCRefNum1(String paymentCCRefNum1) {
		this.paymentCCRefNum1 = paymentCCRefNum1;
	}

	public String getPaymentCheckNum1() {
		return paymentCheckNum1;
	}

	public void setPaymentCheckNum1(String paymentCheckNum1) {
		this.paymentCheckNum1 = paymentCheckNum1;
	}

	public String getPaymentMethod2() {
		return paymentMethod2;
	}

	public void setPaymentMethod2(String paymentMethod2) {
		this.paymentMethod2 = paymentMethod2;
	}

	public Date getPaymentDate2() {
		return paymentDate2;
	}

	public void setPaymentDate2(Date paymentDate2) {
		this.paymentDate2 = paymentDate2;
	}

	public BigDecimal getPaymentAmount2() {
		return paymentAmount2;
	}

	public void setPaymentAmount2(BigDecimal paymentAmount2) {
		this.paymentAmount2 = paymentAmount2;
	}

	public String getPaymentCCRefNum2() {
		return paymentCCRefNum2;
	}

	public void setPaymentCCRefNum2(String paymentCCRefNum2) {
		this.paymentCCRefNum2 = paymentCCRefNum2;
	}

	public String getPaymentCheckNum2() {
		return paymentCheckNum2;
	}

	public void setPaymentCheckNum2(String paymentCheckNum2) {
		this.paymentCheckNum2 = paymentCheckNum2;
	}

	public String getPaymentMethod3() {
		return paymentMethod3;
	}

	public void setPaymentMethod3(String paymentMethod3) {
		this.paymentMethod3 = paymentMethod3;
	}

	public Date getPaymentDate3() {
		return paymentDate3;
	}

	public void setPaymentDate3(Date paymentDate3) {
		this.paymentDate3 = paymentDate3;
	}

	public BigDecimal getPaymentAmount3() {
		return paymentAmount3;
	}

	public void setPaymentAmount3(BigDecimal paymentAmount3) {
		this.paymentAmount3 = paymentAmount3;
	}

	public String getPaymentCCRefNum3() {
		return paymentCCRefNum3;
	}

	public void setPaymentCCRefNum3(String paymentCCRefNum3) {
		this.paymentCCRefNum3 = paymentCCRefNum3;
	}

	public String getPaymentCheckNum3() {
		return paymentCheckNum3;
	}

	public void setPaymentCheckNum3(String paymentCheckNum3) {
		this.paymentCheckNum3 = paymentCheckNum3;
	}

	@Transient
	public String getFormattedDeliveryDate() {
		return FormatUtil.formatDate(getDeliveryDate());
	}
	
	@Transient
	public String getDeliveryDateTime() {
		return FormatUtil.formatDateTimeRange(getDeliveryDate(), getDeliveryHourFrom(), getDeliveryHourTo());
	}
	
	@Transient
	public String getFormattedPickupDate() {
		return FormatUtil.formatDate(getPickupDate());
	}
	
	@Transient
	public String getDeliveryAddressFullLine() {
		return FormatUtil.formatAddress(getDeliveryAddressLine1(), getDeliveryAddressLine2());
	}
}
