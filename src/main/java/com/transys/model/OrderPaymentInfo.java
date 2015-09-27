package com.transys.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="orderPaymentInfo")
public class OrderPaymentInfo extends AbstractBaseModel {
		@OneToOne
		@JoinColumn(name="orderId") 
		private Order order;
		
		@Column(name="dumpsterPrice")
		private BigDecimal dumpsterPrice;
		
		@Column(name="permitFees")
		private BigDecimal permitFees;
		  
		@Column(name="overweightFee")
		private BigDecimal overweightFee;
		  
		@Column(name="cityFee")
		private BigDecimal cityFee;
		
		@ManyToOne
		@JoinColumn(name="additionalFee1Id") 
		private AdditionalFee additionalFee1Type;
		
		@Column(name="additionalFee1")
		private BigDecimal additionalFee1;
		
		@ManyToOne
		@JoinColumn(name="additionalFee2Id") 
		private AdditionalFee additionalFee2Type;
		
		@Column(name="additionalFee2")
		private BigDecimal additionalFee2;
		
		@ManyToOne
		@JoinColumn(name="additionalFee3Id") 
		private AdditionalFee additionalFee3Type;
		
		@Column(name="additionalFee3")
		private BigDecimal additionalFee3;
		
		@Column(name="totalFees")
		private BigDecimal totalFees;
		
		@Column(name="paymentMethod")
		private String paymentMethod;
		
		@Column(name="ccReferenceNum")
		private String ccReferenceNum;
		
		@Column(name="checkNum")
		private String checkNum;

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}

		public BigDecimal getDumpsterPrice() {
			return dumpsterPrice;
		}

		public void setDumpsterPrice(BigDecimal dumpsterPrice) {
			this.dumpsterPrice = dumpsterPrice;
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

		public BigDecimal getCityFee() {
			return cityFee;
		}

		public void setCityFee(BigDecimal cityFee) {
			this.cityFee = cityFee;
		}
		public AdditionalFee getAdditionalFee1Type() {
			return additionalFee1Type;
		}

		public void setAdditionalFee1Type(AdditionalFee additionalFee1Type) {
			this.additionalFee1Type = additionalFee1Type;
		}

		public BigDecimal getAdditionalFee1() {
			return additionalFee1;
		}

		public void setAdditionalFee1(BigDecimal additionalFee1) {
			this.additionalFee1 = additionalFee1;
		}

		public AdditionalFee getAdditionalFee2Type() {
			return additionalFee2Type;
		}

		public void setAdditionalFee2Type(AdditionalFee additionalFee2Type) {
			this.additionalFee2Type = additionalFee2Type;
		}

		public BigDecimal getAdditionalFee2() {
			return additionalFee2;
		}

		public void setAdditionalFee2(BigDecimal additionalFee2) {
			this.additionalFee2 = additionalFee2;
		}

		public AdditionalFee getAdditionalFee3Type() {
			return additionalFee3Type;
		}

		public void setAdditionalFee3Type(AdditionalFee additionalFee3Type) {
			this.additionalFee3Type = additionalFee3Type;
		}

		public BigDecimal getAdditionalFee3() {
			return additionalFee3;
		}

		public void setAdditionalFee3(BigDecimal additionalFee3) {
			this.additionalFee3 = additionalFee3;
		}

		public BigDecimal getTotalFees() {
			return totalFees;
		}

		public void setTotalFees(BigDecimal totalFees) {
			this.totalFees = totalFees;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getCcReferenceNum() {
			return ccReferenceNum;
		}

		public void setCcReferenceNum(String ccReferenceNum) {
			this.ccReferenceNum = ccReferenceNum;
		}

		public String getCheckNum() {
			return checkNum;
		}

		public void setCheckNum(String checkNum) {
			this.checkNum = checkNum;
		}
}
