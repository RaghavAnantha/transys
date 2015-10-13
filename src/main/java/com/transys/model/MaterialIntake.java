package com.transys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="materialIntake")
public class MaterialIntake extends AbstractBaseModel {
	
	@ManyToOne
	@JoinColumn(name="materialTypeId")
	private MaterialType materialType;
	
	@Column(name="netWeightTonnage")
	private String netWeightTonnage;
	
	@Column(name="intakeDate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date intakeDate;

	@Column(name="comments")
	private String comments;

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public String getNetWeightTonnage() {
		return netWeightTonnage;
	}

	public void setNetWeightTonnage(String netWeightTonnage) {
		this.netWeightTonnage = netWeightTonnage;
	}

	public Date getIntakeDate() {
		return intakeDate;
	}

	public void setIntakeDate(Date intakeDate) {
		this.intakeDate = intakeDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
