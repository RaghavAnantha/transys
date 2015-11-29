package com.transys.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@Entity
@Table(name="orderFees")
public class OrderFees extends AbstractBaseModel {
		@OneToOne
		@JoinColumn(name="orderId") 
		private Order order;
		
		@Column(name="dumpsterPrice")
		private BigDecimal dumpsterPrice;
		
		@Column(name="permitFee1")
		private BigDecimal permitFee1;
		
		@Column(name="permitFee2")
		private BigDecimal permitFee2;
		
		@Column(name="permitFee3")
		private BigDecimal permitFee3;
		  
		@Column(name="overweightFee")
		private BigDecimal overweightFee;
		  
		@ManyToOne
		@JoinColumn(name="cityFeeId") 
		private CityFee cityFeeType;
		
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
		
		@Column(name="discountAmount")
		private BigDecimal discountAmount;
		
		@Column(name="totalPermitFees")
		private BigDecimal totalPermitFees;
		
		@Column(name="totalAdditionalFees")
		private BigDecimal totalAdditionalFees;
		
		@Column(name="totalFees")
		private BigDecimal totalFees;

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
		
		public BigDecimal getDiscountAmount() {
			return discountAmount;
		}

		public void setDiscountAmount(BigDecimal discountAmount) {
			this.discountAmount = discountAmount;
		}

		public void setTotalFees(BigDecimal totalFees) {
			this.totalFees = totalFees;
		}
		
		public CityFee getCityFeeType() {
			return cityFeeType;
		}

		public void setCityFeeType(CityFee cityFeeType) {
			this.cityFeeType = cityFeeType;
		}

		public BigDecimal getTotalPermitFees() {
			return totalPermitFees;
		}

		public void setTotalPermitFees(BigDecimal totalPermitFees) {
			this.totalPermitFees = totalPermitFees;
		}

		public BigDecimal getTotalAdditionalFees() {
			return totalAdditionalFees;
		}

		public void setTotalAdditionalFees(BigDecimal totalAdditionalFees) {
			this.totalAdditionalFees = totalAdditionalFees;
		}
}
