package com.transys.model.migration;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.transys.model.AbstractBaseModel;
import com.transys.model.State;

@Entity
@Table(name="oldAddresses")
public class OldDeliveryAddress extends AbstractBaseModel {
	@Column(name="custID")
	private Long custID;
	
	@Column(name="address1")
	private String line1;
	
	@Column(name="address2")
	private String line2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zip")
	private String zipcode;

	@Column(name="status")
	private String status;
	
	@Column(name="whocreated")
	protected Long whocreated;
	
	@Column(name="whoedited")
	protected Long whoedited;
	
	@Column(name="whencreated")
	protected Date whencreated = Calendar.getInstance().getTime();
	
	@Column(name="whenedited")
	protected Date whenedited;

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}
	
	@Transient
	//TODO: Move to utils?
	public String getFullLine() {
		StringBuffer fullLineBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getLine1())) {
			fullLineBuff.append(getLine1());
		}
		if (StringUtils.isNotEmpty(getLine2())) {
			fullLineBuff.append(" " + getLine2());
		}
		
		return fullLineBuff.toString();
	}
	
	@Transient
	//TODO: Move to utils
	public String getFullDeliveryAddress(String lineSeparator) {
		StringBuffer addressBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getLine1())) {
			addressBuff.append(getLine1());
		}
		if (StringUtils.isNotEmpty(getLine2())) {
			addressBuff.append(" " + getLine2());
		}
		if (StringUtils.isNotEmpty(getCity())) {
			addressBuff.append(lineSeparator + getCity());
		}
		if (getState() != null) {
			if (StringUtils.isNotEmpty(getState())) {
				addressBuff.append(" " + getState());
			}
		}
		if (StringUtils.isNotEmpty(getZipcode())) {
			addressBuff.append(" " + getZipcode());
		}
		
		return addressBuff.toString();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getCustID() {
		return custID;
	}

	public void setCustID(Long custID) {
		this.custID = custID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getWhocreated() {
		return whocreated;
	}

	public void setWhocreated(Long whocreated) {
		this.whocreated = whocreated;
	}

	public Long getWhoedited() {
		return whoedited;
	}

	public void setWhoedited(Long whoedited) {
		this.whoedited = whoedited;
	}

	public Date getWhencreated() {
		return whencreated;
	}

	public void setWhencreated(Date whencreated) {
		this.whencreated = whencreated;
	}

	public Date getWhenedited() {
		return whenedited;
	}

	public void setWhenedited(Date whenedited) {
		this.whenedited = whenedited;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	@Override
	public String toString() {
		return getFullDeliveryAddress(", ");
	}
}
