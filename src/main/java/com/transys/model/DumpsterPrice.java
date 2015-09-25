package com.transys.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="dumpsterPrice")
public class DumpsterPrice extends AbstractBaseModel {
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@ManyToOne
	@JoinColumn(name="materialType")
	private MaterialType materialType;
	
	@Column(name="price")
	private BigDecimal price;

	public String getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
