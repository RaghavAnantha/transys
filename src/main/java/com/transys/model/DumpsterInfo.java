package com.transys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="dumpsterInfo")
public class DumpsterInfo extends AbstractBaseModel {
	
	// TODO: Do we need table size restriction here?
	
	@ManyToOne
	@JoinColumn(name="dumpsterSizeId") //Enum?
	private DumpsterSize dumpsterSize;
	
	@Column(name="dumpsterNum")
	private String dumpsterNum;
	
	@ManyToOne
	@JoinColumn(name="status")
	private DumpsterStatus status;
	
	@Transient
	private String deliveryAddress;
	
	@Transient
	private String deliveryDate;

	/*@Column(name="dumpsterPrice")
	private Double dumpsterPrice;*/
	
	/*@Column(name="maxWeight")
	private String maxWeight;*/

	/*@Column(name="overWeightPrice")
	private String overWeightPrice;*/
	
	/*public String getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(String maxWeight) {
		this.maxWeight = maxWeight;
	}*/

	public String getDumpsterNum() {
		return dumpsterNum;
	}

	public DumpsterSize getDumpsterSize() {
		return dumpsterSize;
	}

	public void setDumpsterSize(DumpsterSize dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
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

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
}
