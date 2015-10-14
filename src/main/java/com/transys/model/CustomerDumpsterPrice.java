package com.transys.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="customerDumpsterPrice")
public class CustomerDumpsterPrice extends AbstractFeeMasterData {
	@ManyToOne
	@JoinColumn(name="customerId")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="dumpsterSizeId") //Enum?
	private DumpsterSize dumpsterSize;
	
	@ManyToOne
	@JoinColumn(name="materialTypeId")
	private MaterialType materialType;
	
	public DumpsterSize getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(DumpsterSize dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}
	
	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
	
	@Column(name="price")
	private BigDecimal price;
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
