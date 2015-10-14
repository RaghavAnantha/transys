package com.transys.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="cityFee")
public class CityFee extends AbstractFeeMasterData {
	@Column(name="suburbName")
	private String suburbName;
	
	@Column(name="fee")
	private BigDecimal fee;
	
	public String getSuburbName() {
		return suburbName;
	}

	public void setSuburbName(String suburbName) {
		this.suburbName = suburbName;
	}
	
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
