package com.transys.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MaterialIntakeReportVO extends BaseVO {
	private String reportDate;
	
	private String materialName;
	
	private BigDecimal rollOffTons;
	private BigDecimal publicIntakeTons;
	private BigDecimal totalTons;
	
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public BigDecimal getRollOffTons() {
		return rollOffTons;
	}
	public void setRollOffTons(BigDecimal rollOffTons) {
		this.rollOffTons = rollOffTons;
	}
	public BigDecimal getPublicIntakeTons() {
		return publicIntakeTons;
	}
	public void setPublicIntakeTons(BigDecimal publicIntakeTons) {
		this.publicIntakeTons = publicIntakeTons;
	}
	public BigDecimal getTotalTons() {
		return totalTons;
	}
	public void setTotalTons(BigDecimal totalTons) {
		this.totalTons = totalTons;
	}
}
