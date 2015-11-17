package com.transys.model.migration;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.transys.model.AbstractBaseModel;
import com.transys.model.State;

@Entity
@Table(name="oldCustomers")
public class OldCustomer extends AbstractBaseModel {
	@Column(name="companyName")
	private String companyName;
	
	@Column(name="contactName")
	private String contactName;
	
	@Column(name="custtype")
	private String customerType;
	
	@Column(name="status")
	private String customerStatus;
	
	// Billing Info
	@Column(name="chargeCompany")
	private String chargeCompany;

	// Billing Details
	@Column(name="phone")
	private String phone;
	
	@Column(name="altPhone1")
	private String altPhone1;
	
	@Column(name="altPhone2")
	private String altPhone2;
	
	@NotEmpty
	@Column(name="Address1")
	private String billingAddressLine1;
	
	@Column(name="Address2")
	private String billingAddressLine2;
	
	@NotEmpty
	@Column(name="city")
	private String city;
	
   @Column(name="state")
	private String state;
	
	@NotNull 
	@Column(name="zip")
	private String zipcode;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="email")
	private String email;
	
	@Column(name="comments")
	private String notes;
	
	@Column(name="whocreated")
	protected Long whocreated;
	
	@Column(name="whoedited")
	protected Long whoedited;
	
	@Column(name="whencreated")
	protected Date whencreated = Calendar.getInstance().getTime();
	
	@Column(name="whenedited")
	protected Date whenedited;

	@Transient
	private List<OldDeliveryAddress> deliveryAddress;
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String name) {
		this.companyName = name;
	}
	
	public String getContactName() {
		return contactName;
	}

	public String getChargeCompany() {
		return chargeCompany;
	}

	public void setChargeCompany(String chargeCompany) {
		this.chargeCompany = chargeCompany;
	}

	public String getAltPhone1() {
		return altPhone1;
	}

	public void setAltPhone1(String altPhone1) {
		this.altPhone1 = altPhone1;
	}

	public String getAltPhone2() {
		return altPhone2;
	}

	public void setAltPhone2(String altPhone2) {
		this.altPhone2 = altPhone2;
	}

	public String getBillingAddressLine1() {
		return billingAddressLine1;
	}

	public void setBillingAddressLine1(String billingAddressLine1) {
		this.billingAddressLine1 = billingAddressLine1;
	}

	public String getBillingAddressLine2() {
		return billingAddressLine2;
	}

	public void setBillingAddressLine2(String billingAddressLine2) {
		this.billingAddressLine2 = billingAddressLine2;
	}

	public void setContactName(String name) {
		this.contactName = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	@Transient
	public List<OldDeliveryAddress> getDeliveryAddress() {
		return deliveryAddress;
	}

	@Transient
	public void setDeliveryAddress(List<OldDeliveryAddress> deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	
	@Transient
	//TODO: Move to utils
	public String getBillingAddress() {
		return getBillingAddress("<br>");
	}
	
	@Transient
	//TODO: Move to utils
	public String getBillingAddress(String lineSeparator) {
		StringBuffer addressBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getBillingAddressLine1())) {
			addressBuff.append(getBillingAddressLine1());
		}
		if (StringUtils.isNotEmpty(getBillingAddressLine2())) {
			addressBuff.append(" " + getBillingAddressLine2());
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
	
	@Transient
	//TODO: Move to utils
	public String getFormattedPhone() {
		return formatPhone(getPhone());
	}
	
	@Transient
	//TODO: Move to utils
	public String getFormattedFax() {
		return formatPhone(getFax());
	}
	
	@Transient
	//TODO: Move to utils
	public String formatPhone(String phone) {
		if (StringUtils.isEmpty(phone) || phone.length() < 10 || StringUtils.contains(phone, "-")) {
			return phone;
		}
		
		return String.format("%s-%s-%s", phone.substring(0, 3), 
												   phone.substring(3, 6), 
												   phone.substring(6, 10));
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return getCompanyName();
	}
}