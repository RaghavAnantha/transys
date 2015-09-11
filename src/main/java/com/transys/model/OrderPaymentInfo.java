package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="orderPaymentInfo")
public class OrderPaymentInfo extends AbstractBaseModel {

		@Column(name="orderID")
		private String orderID;
		
		@Column(name="dumpsterPrice")
		private double dumpsterPrice;
		
		@Column(name="cityFee")
		private double cityFee;
		
		@Column(name="additionalFees")
		private double additionalFees;
		
		@Column(name="modeOfPayment")
		private String modeOfPayment;
		
		@Column(name="reference")
		private String reference;
		
		public String getOrderID() {
			return orderID;
		}

		public void setOrderID(String orderID) {
			this.orderID = orderID;
		}

		public double getDumpsterPrice() {
			return dumpsterPrice;
		}

		public void setDumpsterPrice(double dumpsterPrice) {
			this.dumpsterPrice = dumpsterPrice;
		}

		public double getCityFee() {
			return cityFee;
		}

		public void setCityFee(double cityFee) {
			this.cityFee = cityFee;
		}

		public double getAdditionalFees() {
			return additionalFees;
		}

		public void setAdditionalFees(double additionalFees) {
			this.additionalFees = additionalFees;
		}

		public String getModeOfPayment() {
			return modeOfPayment;
		}

		public void setModeOfPayment(String modeOfPayment) {
			this.modeOfPayment = modeOfPayment;
		}

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		
}
