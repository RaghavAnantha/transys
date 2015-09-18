package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="dumpsterInfo")
public class DumpsterInfo extends AbstractBaseModel {
	
	// TODO: Do we need table size restriction here?
	
	@Column(name="dumpsterSize")
	private String dumpsterSize;
	
	@Column(name="dumpsterNum")
	private String dumpsterNum;

	@Column(name="dumpsterPrice")
	private Double dumpsterPrice;
	
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

	public Double getDumpsterPrice() {
		return dumpsterPrice;
	}

	public void setDumpsterPrice(Double dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}

	public String getDumpsterNum() {
		return dumpsterNum;
	}

	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}
}
