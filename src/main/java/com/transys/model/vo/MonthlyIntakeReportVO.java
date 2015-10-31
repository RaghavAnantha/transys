package com.transys.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MonthlyIntakeReportVO {
	
	private Date intakeDate;
	
	private Integer totalBoxes;
	
	private List<RollOffBoxesPerYardVO> rollOffBoxesPerYard;
	
	private BigDecimal rollOffCubicYards;
	private BigDecimal rollOffTonnage;
	
	private BigDecimal publicIntakeCubicYards;
	private BigDecimal publicIntakeTonnage;
	
	private BigDecimal totalCubicYards;
	private BigDecimal totalTonnage;
	
	public Date getIntakeDate() {
		return intakeDate;
	}
	
	public void setIntakeDate(Date intakeDate) {
		this.intakeDate = intakeDate;
	}
	
	public Integer getTotalBoxes() {
		return totalBoxes;
	}
	
	public void setTotalBoxes(Integer totalBoxes) {
		this.totalBoxes = totalBoxes;
	}
	
	public List<RollOffBoxesPerYardVO> getRollOffBoxesPerYard() {
		return rollOffBoxesPerYard;
	}
	
	public void setRollOffBoxesPerYard(List<RollOffBoxesPerYardVO> rollOffBoxesPerYard) {
		this.rollOffBoxesPerYard = rollOffBoxesPerYard;
	}
	
	public BigDecimal getRollOffCubicYards() {
		return rollOffCubicYards;
	}
	
	public void setRollOffCubicYards(BigDecimal rollOffCubicYards) {
		this.rollOffCubicYards = rollOffCubicYards;
	}
	
	public BigDecimal getRollOffTonnage() {
		return rollOffTonnage;
	}
	
	public void setRollOffTonnage(BigDecimal rollOffTonnage) {
		this.rollOffTonnage = rollOffTonnage;
	}
	
	public BigDecimal getPublicIntakeCubicYards() {
		return publicIntakeCubicYards;
	}
	
	public void setPublicIntakeCubicYards(BigDecimal publicIntakeCubicYards) {
		this.publicIntakeCubicYards = publicIntakeCubicYards;
	}
	
	public BigDecimal getPublicIntakeTonnage() {
		return publicIntakeTonnage;
	}
	
	public void setPublicIntakeTonnage(BigDecimal publicIntakeTonnage) {
		this.publicIntakeTonnage = publicIntakeTonnage;
	}
	
	public BigDecimal getTotalCubicYards() {
		return totalCubicYards;
	}
	
	public void setTotalCubicYards(BigDecimal totalCubicYards) {
		this.totalCubicYards = totalCubicYards;
	}
	
	public BigDecimal getTotalTonnage() {
		return totalTonnage;
	}
	
	public void setTotalTonnage(BigDecimal totalTonnage) {
		this.totalTonnage = totalTonnage;
	}
}
