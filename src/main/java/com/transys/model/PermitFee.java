package com.transys.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="permitFee")
public class PermitFee extends AbstractBaseModel {

	@ManyToOne
	@JoinColumn(name="permitClass")
	private PermitClass permitClass;
	
	@ManyToOne
	@JoinColumn(name="permitType")
	private PermitType permitType;
	
	@Column(name="fee")
	private BigDecimal fee;

	public PermitClass getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(PermitClass permitClass) {
		this.permitClass = permitClass;
	}

	public PermitType getPermitType() {
		return permitType;
	}

	public void setPermitType(PermitType permitType) {
		this.permitType = permitType;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
}
