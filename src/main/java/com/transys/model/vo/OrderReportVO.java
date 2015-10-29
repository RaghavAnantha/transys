package com.transys.model.vo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.transys.model.Order;

public class OrderReportVO implements BaseVO {
	private Long id;
	
	private String companyName;
	
	private String deliveryContactName;
	private String deliveryContactPhone1;
	private String deliveryAddressFullLine;
	private String deliveryCity;
	
	private String status;
	
	private String deliveryDate;
	private String pickupDate;
	
	private BigDecimal dumpsterPrice;
	private BigDecimal cityFee;
	private BigDecimal permitFees;
	private BigDecimal overweightFee;
	private BigDecimal additionalFees;
	private BigDecimal totalFees;
	private BigDecimal totalAmountPaid;
	private BigDecimal balanceAmountDue;
	
	private String createdAt;
	
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
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
