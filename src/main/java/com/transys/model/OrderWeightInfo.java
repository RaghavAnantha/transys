package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

	@Entity
	@Table(name="orderWeightInfo")
	public class OrderWeightInfo extends AbstractBaseModel {

			@Column(name="orderID")
			private String orderID;
			
			@Column(name="grossWeight")
			private double grossWeight;
			
			@Column(name="tare")
			private double tare;
			
			@Column(name="netWeight")
			private double netWeight;

			public String getOrderID() {
				return orderID;
			}

			public void setOrderID(String orderID) {
				this.orderID = orderID;
			}

			public double getGrossWeight() {
				return grossWeight;
			}

			public void setGrossWeight(double grossWeight) {
				this.grossWeight = grossWeight;
			}

			public double getTare() {
				return tare;
			}

			public void setTare(double tare) {
				this.tare = tare;
			}

			public double getNetWeight() {
				return netWeight;
			}

			public void setNetWeight(double netWeight) {
				this.netWeight = netWeight;
			}
			
			// netTonnage???
			
}
