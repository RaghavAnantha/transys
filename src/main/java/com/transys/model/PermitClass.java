package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="permitClass")
public class PermitClass extends AbstractBaseModel {
	@Transient
	public static final String PERMIT_CLASS_A = "CLASS A";
	@Transient
	public static final String PERMIT_CLASS_B = "CLASS B";
	@Transient
	public static final String PERMIT_CLASS_D = "CLASS D";

	@Column(name="permitClass")
	private String permitClass;
	
	@Column(name="comments")
	private String comments;

	public String getPermitClass() {
		return permitClass;
	}

	public void setPermitClass(String permitClass) {
		this.permitClass = permitClass;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		return getPermitClass();
	}
}
