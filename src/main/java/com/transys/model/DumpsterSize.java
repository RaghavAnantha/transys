package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="dumpsterSize")
public class DumpsterSize extends AbstractBaseModel {

	@Column(name="size")
	private String size;
	
	@Column(name="comments")
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="permitClassId")
	private PermitClass permitClass;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PermitClass getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(PermitClass permitClass) {
		this.permitClass = permitClass;
	}
}
