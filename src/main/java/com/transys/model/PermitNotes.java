package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="permitNotes")
public class PermitNotes extends AbstractBaseModel {

	@ManyToOne
	@JoinColumn(name="permitId")
	private Permit permit;
	
	@Column(name="notes")
	private String notes;

	public Permit getPermit() {
		return permit;
	}

	public void setPermit(Permit permit) {
		this.permit = permit;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
