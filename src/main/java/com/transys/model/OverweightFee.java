package com.transys.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="overweightFee")
public class OverweightFee extends AbstractFeeMasterData {
	@Column(name="tonLimit")
	private BigDecimal tonLimit;

	@Column(name="fee")
	private BigDecimal fee;

	@ManyToOne
	@JoinColumn(name="dumpsterSizeId") 
	private DumpsterSize dumpsterSize;
	
	@ManyToOne
	@JoinColumn(name="materialCategoryId") 
	private MaterialCategory materialCategory;
	
	public BigDecimal getTonLimit() {
		return tonLimit;
	}

	public void setTonLimit(BigDecimal tonLimit) {
		this.tonLimit = tonLimit;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	public DumpsterSize getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(DumpsterSize dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public MaterialCategory getMaterialCategory() {
		return materialCategory;
	}

	public void setMaterialCategory(MaterialCategory materialCategory) {
		this.materialCategory = materialCategory;
	}
}
