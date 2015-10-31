package com.transys.model.vo;

import java.math.BigDecimal;

public class RollOffBoxesPerYardVO {
	
	private String yardSize;
	private Integer numBoxes;
	private BigDecimal cubicYards;
	
	public String getYardSize() {
		return yardSize;
	}
	public void setYardSize(String yardSize) {
		this.yardSize = yardSize;
	}
	public Integer getNumBoxes() {
		return numBoxes;
	}
	public void setNumBoxes(Integer numBoxes) {
		this.numBoxes = numBoxes;
	}
	public BigDecimal getCubicYards() {
		return cubicYards;
	}
	public void setCubicYards(BigDecimal cubicYards) {
		this.cubicYards = cubicYards;
	}

}
