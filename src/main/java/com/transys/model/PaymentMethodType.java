package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name="paymentMethodType")
public class PaymentMethodType extends AbstractBaseModel {

	//@Column(name="paymentMethodType")
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
