package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="orderNotes")
public class OrderNotes extends AbstractBaseModel {
	@Transient
	public static final String NOTES_TYPE_USER = "USER";
	@Transient
	public static final String NOTES_TYPE_AUDIT = "AUDIT";
	
	@Transient
	public static final String AUDIT_MSG_PREFIX = "***AUDIT: ";
	@Transient
	public static final String AUDIT_MSG_SUFFIX = "***";
	
	@ManyToOne
	@JoinColumn(name="orderId") 
	private Order order;

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

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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
