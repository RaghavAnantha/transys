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

}
