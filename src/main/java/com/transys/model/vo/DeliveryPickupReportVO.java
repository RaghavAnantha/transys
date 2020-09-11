package com.transys.model.vo;

import java.math.BigDecimal;

public class DeliveryPickupReportVO extends BaseVO {
	private String deliveryDateFrom;
	private String deliveryDateTo;
	private String pickupDateFrom;
	private String pickupDateTo;
	
	private String deliveryAddress;

	public String getDeliveryDateFrom() {
		return deliveryDateFrom;
	}

	public void setDeliveryDateFrom(String deliveryDateFrom) {
		this.deliveryDateFrom = deliveryDateFrom;
	}

	public String getDeliveryDateTo() {
		return deliveryDateTo;
	}

	public void setDeliveryDateTo(String deliveryDateTo) {
		this.deliveryDateTo = deliveryDateTo;
	}

	public String getPickupDateFrom() {
		return pickupDateFrom;
	}

	public void setPickupDateFrom(String pickupDateFrom) {
		this.pickupDateFrom = pickupDateFrom;
	}

	public String getPickupDateTo() {
		return pickupDateTo;
	}

	public void setPickupDateTo(String pickupDateTo) {
		this.pickupDateTo = pickupDateTo;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
}
