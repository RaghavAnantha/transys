package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="customerDumpsterPrice")
public class CustomerDumpsterPrice extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="customer")
	private Customer customer;
	
	@Column(name="dumpsterPrice")
	private String dumpsterPrice;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDumpsterPrice() {
		return dumpsterPrice;
	}

	public void setDumpsterPrice(String dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}
	
}
