package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="dumpsterInfo")
public class DumpsterInfo extends AbstractBaseModel {
	
	// TODO: Do we need table size restriction here?
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name="dumpsterNum")
	private String dumpsterNum;
	
	@ManyToOne
	@JoinColumn(name="status")
	private DumpsterStatus status;

	/*@Column(name="dumpsterPrice")
	private Double dumpsterPrice;*/
	
	/*@Column(name="maxWeight")
	private String maxWeight;*/

	/*@Column(name="overWeightPrice")
	private String overWeightPrice;*/
	
	public String getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	/*public String getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(String maxWeight) {
		this.maxWeight = maxWeight;
	}*/

	public String getDumpsterNum() {
		return dumpsterNum;
	}

	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}

	public DumpsterStatus getStatus() {
		return status;
	}

	public void setStatus(DumpsterStatus status) {
		this.status = status;
	}
}
