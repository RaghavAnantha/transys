package com.transys.model.invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.transys.core.util.FormatUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.OrderNotes;

@Entity
@Table(name="orderInvoiceHeader")
public class OrderInvoiceHeader extends AbstractBaseModel {
	@Column(name="invoiceDate")
	private Date invoiceDate;
	
	@Column(name="customerId")
	private Long customerId;
		   
	@Column(name="companyName")
	private String companyName;
	
	@Column(name="contactName")
	private String contactName;
	
	@Column(name="phone1")
	private String phone1;
	
	@Column(name="phone2")
	private String phone2;
	
	@Column(name="billingAddressLine1")
	private String billingAddressLine1;
	
	@Column(name="billingAddressLine2")
	private String billingAddressLine2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zip")
	private String zip;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="email")
	private String email;
	
	@Column(name="orderDateFrom")
 	private Date orderDateFrom;
	 
	@Column(name="orderDateTo")
	private Date orderDateTo;
	
	@Column(name = "orderCount")
	private Integer orderCount;
	
   @Column(name = "totalFees")
	private BigDecimal totalFees;
	
   @Column(name = "totalAmountPaid")
 	private BigDecimal totalAmountPaid;
   
   @Column(name = "totalDiscount")
 	private BigDecimal totalDiscount;
   
   @Column(name = "totalBalanceAmountDue")
 	private BigDecimal totalBalanceAmountDue;
   
   @OneToMany(mappedBy="invoiceHeader", cascade = CascadeType.ALL)
	private List<OrderInvoiceDetails> orderInvoiceDetails;
	
	public List<OrderInvoiceDetails> getOrderInvoiceDetails() {
		return orderInvoiceDetails;
	}

	public void setOrderInvoiceDetails(List<OrderInvoiceDetails> orderInvoiceDetails) {
		this.orderInvoiceDetails = orderInvoiceDetails;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
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

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public BigDecimal getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}

	public BigDecimal getTotalAmountPaid() {
		return totalAmountPaid;
	}

	public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
	}

	public Date getOrderDateFrom() {
		return orderDateFrom;
	}

	public void setOrderDateFrom(Date orderDateFrom) {
		this.orderDateFrom = orderDateFrom;
	}

	public Date getOrderDateTo() {
		return orderDateTo;
	}

	public void setOrderDateTo(Date orderDateTo) {
		this.orderDateTo = orderDateTo;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getTotalBalanceAmountDue() {
		return totalBalanceAmountDue;
	}

	public void setTotalBalanceAmountDue(BigDecimal totalBalanceAmountDue) {
		this.totalBalanceAmountDue = totalBalanceAmountDue;
	}

	@Transient
	// TODO: Move to utils?
	public String getBillingAddress() {
		return getBillingAddress("<br>");
	}
	
	@Transient
	// TODO: Move to utils?
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
		if (StringUtils.isNotEmpty(getState())) {
			addressBuff.append(" " + getState());
		}
		if (StringUtils.isNotEmpty(getZip())) {
			addressBuff.append(" " + getZip());
		}
		
		return addressBuff.toString();
	}
	
	@Transient
	public String getContactDetails() {
		String contactDetails = StringUtils.isEmpty(getContactName()) ? StringUtils.EMPTY : getContactName();
		if (StringUtils.isNotEmpty(getPhone1())) {
			contactDetails += " " + getFormattedPhone1();
		}
		return contactDetails;
	}
	
	@Transient
	public String getFormattedPhone1() {
		return FormatUtil.formatPhone(getPhone1());
	}
	
	@Transient
	public String getFormattedFax() {
		return FormatUtil.formatPhone(getFax());
	}
	
	@Override
	public String toString() {
		return getCompanyName();
	}
}