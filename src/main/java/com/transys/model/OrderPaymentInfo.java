package com.transys.model;

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
		private Double dumpsterPrice;
		
		@Column(name="permitFees")
		private Double permitFees;
		  
		@Column(name="overweightFee")
		private Double overweightFee;
		  
		@Column(name="cityFee")
		private Double cityFee;
		
		@Column(name="additionalFee")
		private Double additionalFee;
		
		@Column(name="totalFees")
		private Double totalFees;
		
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

		public Double getDumpsterPrice() {
			return dumpsterPrice;
		}

		public void setDumpsterPrice(Double dumpsterPrice) {
			this.dumpsterPrice = dumpsterPrice;
		}

		public Double getPermitFees() {
			return permitFees;
		}

		public void setPermitFees(Double permitFees) {
			this.permitFees = permitFees;
		}

		public Double getOverweightFee() {
			return overweightFee;
		}

		public void setOverweightFee(Double overweightFee) {
			this.overweightFee = overweightFee;
		}

		public Double getCityFee() {
			return cityFee;
		}

		public void setCityFee(Double cityFee) {
			this.cityFee = cityFee;
		}

		public Double getAdditionalFee() {
			return additionalFee;
		}

		public void setAdditionalFee(Double additionalFee) {
			this.additionalFee = additionalFee;
		}

		public Double getTotalFees() {
			return totalFees;
		}

		public void setTotalFees(Double totalFees) {
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
