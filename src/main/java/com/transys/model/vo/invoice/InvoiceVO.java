package com.transys.model.vo.invoice;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.transys.model.vo.BaseVO;

public class InvoiceVO implements BaseVO {
	private Long id;
	private String orderDate;
	
	private String companyName;
	
	private String billingAddress;
	
	private String deliveryContactName;
	private String deliveryContactPhone1;
	private String deliveryContactPhone2;
	
	private String deliveryAddress;
	private String deliveryAddressFullLine;
	private String deliveryCity;
	
	private String dumpsterLocation;
	
	private String status;
	
	private String deliveryDate;
	private String deliveryDateTime;
	private String pickupDate;
	
	private String dumpsterSize;
	private String dumpsterNum = StringUtils.EMPTY;;
	
	private String materialType;
	
	private BigDecimal dumpsterPrice;
	private BigDecimal cityFee;
	private BigDecimal permitFees;
	private BigDecimal tonnageFees;
	private BigDecimal overweightFee;
	private BigDecimal additionalFees;
	private BigDecimal totalFees;
	private BigDecimal totalAmountPaid;
	private BigDecimal balanceAmountDue;
	
	private String paymentMethod;
	private String referenceNum;
	private String checkNum;
	private String paymentDate;
	
	private String notes;
	
	private Long pickupOrderId;
	private String pickupOrderDeliveryDate = StringUtils.EMPTY;
	private String pickupOrderDumpsterSize = StringUtils.EMPTY;
	private String pickupOrderDumpsterNum = StringUtils.EMPTY;
	private String pickupOrderDeliveryAddress = StringUtils.EMPTY;
	private String pickupOrderDeliveryCity = StringUtils.EMPTY;
	
	private String permitEndDate;
	
	private String permitAddressFullLine = StringUtils.EMPTY;
	
	private String customerId;
	private String orderDateFrom;
	private String orderDateTo;
	
	private String[] ids;
	private String invoiceNo;
	private Date invoiceDate;
	
	private int historyCount = -1;
	
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getDeliveryAddressFullLine() {
		return deliveryAddressFullLine;
	}
	public void setDeliveryAddressFullLine(String deliveryAddressFullLine) {
		this.deliveryAddressFullLine = deliveryAddressFullLine;
	}
	public String getDeliveryCity() {
		return deliveryCity;
	}
	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}
	public BigDecimal getDumpsterPrice() {
		return dumpsterPrice;
	}
	public void setDumpsterPrice(BigDecimal dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}
	public BigDecimal getCityFee() {
		return cityFee;
	}
	public void setCityFee(BigDecimal cityFee) {
		this.cityFee = cityFee;
	}
	public BigDecimal getPermitFees() {
		return permitFees;
	}
	public void setPermitFees(BigDecimal permitFees) {
		this.permitFees = permitFees;
	}
	public BigDecimal getOverweightFee() {
		return overweightFee;
	}
	public void setOverweightFee(BigDecimal overweightFee) {
		this.overweightFee = overweightFee;
	}
	public BigDecimal getAdditionalFees() {
		return additionalFees;
	}
	public void setAdditionalFees(BigDecimal additionalFees) {
		this.additionalFees = additionalFees;
	}
	public BigDecimal getTotalFees() {
		return totalFees;
	}
	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
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
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getDeliveryContactPhone2() {
		return deliveryContactPhone2;
	}
	public void setDeliveryContactPhone2(String deliveryContactPhone2) {
		this.deliveryContactPhone2 = deliveryContactPhone2;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getDumpsterLocation() {
		return dumpsterLocation;
	}
	public void setDumpsterLocation(String dumpsterLocation) {
		this.dumpsterLocation = dumpsterLocation;
	}
	public String getDeliveryDateTime() {
		return deliveryDateTime;
	}
	public void setDeliveryDateTime(String deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}
	public String getDumpsterSize() {
		return dumpsterSize;
	}
	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getReferenceNum() {
		return referenceNum;
	}
	public void setReferenceNum(String referenceNum) {
		this.referenceNum = referenceNum;
	}
	public Long getPickupOrderId() {
		return pickupOrderId;
	}
	public void setPickupOrderId(Long pickupOrderId) {
		this.pickupOrderId = pickupOrderId;
	}
	public String getPickupOrderDeliveryDate() {
		return pickupOrderDeliveryDate;
	}
	public void setPickupOrderDeliveryDate(String pickupOrderDeliveryDate) {
		this.pickupOrderDeliveryDate = pickupOrderDeliveryDate;
	}
	public String getPickupOrderDumpsterSize() {
		return pickupOrderDumpsterSize;
	}
	public void setPickupOrderDumpsterSize(String pickupOrderDumpsterSize) {
		this.pickupOrderDumpsterSize = pickupOrderDumpsterSize;
	}
	public String getPickupOrderDeliveryAddress() {
		return pickupOrderDeliveryAddress;
	}
	public void setPickupOrderDeliveryAddress(String pickupOrderDeliveryAddress) {
		this.pickupOrderDeliveryAddress = pickupOrderDeliveryAddress;
	}
	public String getPickupOrderDeliveryCity() {
		return pickupOrderDeliveryCity;
	}
	public void setPickupOrderDeliveryCity(String pickupOrderDeliveryCity) {
		this.pickupOrderDeliveryCity = pickupOrderDeliveryCity;
	}
	public String getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPermitEndDate() {
		return permitEndDate;
	}
	public void setPermitEndDate(String permitEndDate) {
		this.permitEndDate = permitEndDate;
	}
	public String getDumpsterNum() {
		return dumpsterNum;
	}
	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}
	public String getPermitAddressFullLine() {
		return permitAddressFullLine;
	}
	public void setPermitAddressFullLine(String permitAddressFullLine) {
		this.permitAddressFullLine = permitAddressFullLine;
	}
	public String getPickupOrderDumpsterNum() {
		return pickupOrderDumpsterNum;
	}
	public void setPickupOrderDumpsterNum(String pickupOrderDumpsterNum) {
		this.pickupOrderDumpsterNum = pickupOrderDumpsterNum;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrderDateFrom() {
		return orderDateFrom;
	}
	public void setOrderDateFrom(String orderDateFrom) {
		this.orderDateFrom = orderDateFrom;
	}
	public String getOrderDateTo() {
		return orderDateTo;
	}
	public void setOrderDateTo(String orderDateTo) {
		this.orderDateTo = orderDateTo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public int getHistoryCount() {
		return historyCount;
	}
	public void setHistoryCount(int historyCount) {
		this.historyCount = historyCount;
	}
	public BigDecimal getTonnageFees() {
		return tonnageFees;
	}
	public void setTonnageFees(BigDecimal tonnageFees) {
		this.tonnageFees = tonnageFees;
	}
}
