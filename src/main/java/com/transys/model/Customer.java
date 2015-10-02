package com.transys.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
	@ManyToOne
	@JoinColumn(name="customerTypeId") 
	private CustomerType customerType;
	
	@ManyToOne
	@JoinColumn(name="customerStatusId") //Enum?
	private CustomerStatus customerStatus;
	
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
	
   @ManyToOne
	@JoinColumn(name="state")
	private State state;
	
	@NotNull 
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="email")
	private String email;
	
	/******** Notes Info ************/
	@OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<CustomerNotes> customerNotes;
	
	public List<CustomerNotes> getCustomerNotes() {
		return customerNotes;
	}

	public void setCustomerNotes(List<CustomerNotes> customerNotes) {
		this.customerNotes = customerNotes;
	}

	@OneToMany(mappedBy="customer", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<DeliveryAddress> deliveryAddress;
	
	public List<DeliveryAddress> getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(List<DeliveryAddress> deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String name) {
		this.companyName = name;
	}
	
	public String getContactName() {
		return contactName;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}
	
	public CustomerStatus getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.customerStatus = customerStatus;
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
	
	@Transient
	//TODO: Move to utils
	public String getBillingAddress() {
		StringBuffer addressBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(getBillingAddressLine1())) {
			addressBuff.append(getBillingAddressLine1());
		}
		if (StringUtils.isNotEmpty(getBillingAddressLine2())) {
			addressBuff.append(" " + getBillingAddressLine2());
		}
		if (StringUtils.isNotEmpty(getCity())) {
			addressBuff.append(", " + getCity());
		}
		if (StringUtils.isNotEmpty(getState().getName())) {
			addressBuff.append(", " + getState().getName());
		}
		if (StringUtils.isNotEmpty(getZipcode())) {
			addressBuff.append(", " + getZipcode());
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
}