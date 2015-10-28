package com.transys.model.vo;

import java.math.BigDecimal;

public class RecycleReportVO implements BaseVO {
	
	private String reportDate;
	private String materialCategory;
	private String materialName;
	private BigDecimal totalNetTonnage;
	private String recycleLocation;
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getMaterialCategory() {
		return materialCategory;
	}
	public void setMaterialCategory(String materialCategory) {
		this.materialCategory = materialCategory;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public BigDecimal getTotalNetTonnage() {
		return totalNetTonnage;
	}
	public void setTotalNetTonnage(BigDecimal totalNetTonnage) {
		this.totalNetTonnage = totalNetTonnage;
	}
	public String getRecycleLocation() {
		return recycleLocation;
	}
	public void setRecycleLocation(String recycleLocation) {
		this.recycleLocation = recycleLocation;
	}
	
}
