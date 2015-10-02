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
public class OverweightFee extends AbstractBaseModel {
	@Column(name="tonLimit")
	private BigDecimal tonLimit;

	@Column(name="fee")
	private BigDecimal fee;
	
	@Column(name="comments")
	private String comments;

	@ManyToOne
	@JoinColumn(name="dumpsterSizeId") 
	private DumpsterSize dumpsterSize;
	
	@ManyToOne
	@JoinColumn(name="materialCategoryId") 
	private MaterialCategory materialCategory;
	
	@Column(name="effectiveDateFrom")
	private Date effectiveDateFrom;
	
	@Column(name="effectiveDateTo")
	private Date effectiveDateTo;

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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Date getEffectiveDateFrom() {
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}
}
