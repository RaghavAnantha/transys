package com.transys.model.vo;

import java.math.BigDecimal;

public class PublicMaterialIntakeVO {
	
	private BigDecimal tonnage;
	private BigDecimal cubicYards;
	
	public BigDecimal getTonnage() {
		return tonnage;
	}
	public void setTonnage(BigDecimal tonnage) {
		this.tonnage = tonnage;
	}
	public BigDecimal getCubicYards() {
		return cubicYards;
	}
	public void setCubicYards(BigDecimal cubicYards) {
		this.cubicYards = cubicYards;
	}
	
}
