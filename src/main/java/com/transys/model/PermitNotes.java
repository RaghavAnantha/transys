package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="permitNotes")
public class PermitNotes extends AbstractBaseModel {
	@Transient
	public static final String NOTES_TYPE_USER = "USER";
	@Transient
	public static final String NOTES_TYPE_AUDIT = "AUDIT";
	
	@ManyToOne
	@JoinColumn(name="permitId")
	@JsonBackReference
	private Permit permit;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="notesType")
	private String notesType;
	
	@Column(name="entered_by")
	private String enteredBy;
	
	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

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

	public String getNotesType() {
		return notesType;
	}

	public void setNotesType(String notesType) {
		this.notesType = notesType;
	}
}
