package com.transys.model;

import java.math.BigDecimal;

public class OrdersRevenue extends AbstractBaseModel {

	BigDecimal totalDumpsterPrice;
	BigDecimal totalPermitFees;
	BigDecimal totalCityFees;
	BigDecimal totalOverweightFees;
	BigDecimal totalFees;
	public BigDecimal getTotalDumpsterPrice() {
		return totalDumpsterPrice;
	}
	public void setTotalDumpsterPrice(BigDecimal totalDumpsterPrice) {
		this.totalDumpsterPrice = totalDumpsterPrice;
	}
	public BigDecimal getTotalPermitFees() {
		return totalPermitFees;
	}
	public void setTotalPermitFees(BigDecimal totalPermitFees) {
		this.totalPermitFees = totalPermitFees;
	}
	public BigDecimal getTotalCityFees() {
		return totalCityFees;
	}
	public void setTotalCityFees(BigDecimal totalCityFees) {
		this.totalCityFees = totalCityFees;
	}
	public BigDecimal getTotalOverweightFees() {
		return totalOverweightFees;
	}
	public void setTotalOverweightFees(BigDecimal totalOverweightFees) {
		this.totalOverweightFees = totalOverweightFees;
	}
	public BigDecimal getTotalFees() {
		return totalFees;
	}
	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}
	
}
