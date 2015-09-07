package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="customer")
public class Customer extends AbstractBaseModel {
	
	@NotEmpty
	/*private String name;*/
	@Size(max=40)
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="contact_name")
	private String contactName;
	
	@Column(name="type")
	private String type;
	
	@Column(name="status")
	private String status;
	
	@Column(name="notes")
	private String notes;

	// Billing Info
	@Column(name="chargeCompany")
	private String chargeCompany;

	// Billing Details
	@Column(name="phone")
	private String phone;
	
	@Column(name="alt_phone_1")
	private String altPhone1;
	
	@Column(name="alt_phone_2")
	private String altPhone2;
	
	@NotEmpty
	@Column(name="billing_address_line1")
	private String billingAddressLine1;
	
	@Column(name="billing_address_line2")
	private String billingAddressLine2;
	
	@NotEmpty
	@Column(name="city")
	private String city;
	
//    @ManyToOne
//	@JoinColumn(name="state")
	 private State state;
	
	@NotNull 
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="fax")
	private String fax;

	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String name) {
		this.companyName = name;
	}
	
	public String getContactName() {
		return contactName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getZipcode() {
		return zipcode;
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

}
