package com.transys.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="additionalFee")
public class AdditionalFee extends AbstractFeeMasterData {
	@Column(name="description")
	private String description;

	@Column(name="fee")
	private BigDecimal fee;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
