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

	@Column(name="dumpsterPrice")
	private String dumpsterPrice;
	
	@Column(name="maxWeight")
	private String maxWeight;

	@Column(name="overWeightPrice")
	private String overWeightPrice;
	
	public String getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}

	public String getDumpsterPrice() {
		return dumpsterPrice;
	}

	public void setDumpsterPrice(String dumpsterPrice) {
		this.dumpsterPrice = dumpsterPrice;
	}

	public String getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(String maxWeight) {
		this.maxWeight = maxWeight;
	}

	public String getOverWeightPrice() {
		return overWeightPrice;
	}

	public void setOverWeightPrice(String overWeightPrice) {
		this.overWeightPrice = overWeightPrice;
	}

}
