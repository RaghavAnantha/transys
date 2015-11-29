package com.transys.model.migration;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.transys.model.AbstractBaseModel;

@Entity
@Table(name="oldDumpsters")
public class OldDumpster extends AbstractBaseModel {
	@Column(name="dumpsterNum")
	private String dumpsterNum;
	
	@Column(name="dumpsterSize")
	private Integer dumpsterSize;
	
	@Column(name="status")
	private String status;
	
	@Column(name="inRepair")
	private String inRepair;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="whoedited")
	protected Long whoedited;
	
	@Column(name="whenedited")
	protected Date whenedited;

	public String getDumpsterNum() {
		return dumpsterNum;
	}

	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}

	public Integer getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(Integer dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInRepair() {
		return inRepair;
	}

	public void setInRepair(String inRepair) {
		this.inRepair = inRepair;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getWhoedited() {
		return whoedited;
	}

	public void setWhoedited(Long whoedited) {
		this.whoedited = whoedited;
	}

	public Date getWhenedited() {
		return whenedited;
	}

	public void setWhenedited(Date whenedited) {
		this.whenedited = whenedited;
	}
}
