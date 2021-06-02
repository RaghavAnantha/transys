package com.transys.model.invoice;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.transys.core.util.FormatUtil;

import com.transys.model.AbstractBaseModel;
import com.transys.model.OrderPayment;
import com.transys.model.PaymentMethodType;

@Entity
@Table(name="orderInvoicePayment")
public class OrderInvoicePayment extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="invoiceId") 
	private OrderInvoiceHeader invoice;
	
	@ManyToOne
	@JoinColumn(name="paymentMethodId") 
	private PaymentMethodType paymentMethod;
	
	@Column(name="ccReferenceNum")
	private String ccReferenceNum;
	
	@Column(name="ccName")
	private String ccName;
	
	@Column(name="ccNumber")
	private String ccNumber;
	
	@Column(name="ccExpDate")
	private Date ccExpDate;
	
	@Column(name="checkNum")
	private String checkNum;
	
	@Column(name="amountPaid")
	private BigDecimal amountPaid;
	
	@Column(name="paymentDate")
	private Date paymentDate;
	
	@Column(name="amountAvailable")
	private BigDecimal amountAvailable;
	
	@Column(name="notes")
	private String notes;
	
	@Transient
	private List<OrderPayment> orderPaymentList;

	public OrderInvoiceHeader getInvoice() {
		return invoice;
	}

	public void setInvoice(OrderInvoiceHeader invoice) {
		this.invoice = invoice;
	}

	public PaymentMethodType getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodType paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCcReferenceNum() {
		return ccReferenceNum;
	}

	public void setCcReferenceNum(String ccReferenceNum) {
		this.ccReferenceNum = ccReferenceNum;
	}

	public String getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public Date getCcExpDate() {
		return ccExpDate;
	}

	public void setCcExpDate(Date ccExpDate) {
		this.ccExpDate = ccExpDate;
	}

	public BigDecimal getAmountAvailable() {
		return amountAvailable;
	}

	public void setAmountAvailable(BigDecimal amountAvailable) {
		this.amountAvailable = amountAvailable;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Transient
	public String getFormattedPaymentDate() {
		return FormatUtil.formatDate(this.paymentDate);
	}
	
	@Transient
	public String getFormattedCCExpDate() {
		return FormatUtil.formatExpiryDate(this.ccExpDate);
	}
	
	@Transient
	public BigDecimal getInvoiceBalanceDue() {
		OrderInvoiceHeader invoice = getInvoice();
		return ((invoice == null || invoice.getTotalInvoiceBalanceDue() == null) ? null : invoice.getTotalInvoiceBalanceDue());
	}
	
	@Transient
	public String getInvoiceBalanceDueStr() {
		BigDecimal invoiceBalanceDue = getInvoiceBalanceDue();
		return (invoiceBalanceDue == null ? StringUtils.EMPTY : String.valueOf(invoiceBalanceDue));
	}

	@Transient
	public List<OrderPayment> getOrderPaymentList() {
		return orderPaymentList;
	}

	@Transient
	public void setOrderPaymentList(List<OrderPayment> orderPaymentList) {
		this.orderPaymentList = orderPaymentList;
	}
	
	@Transient
	public String getFormattedAmountPaid() {
		return FormatUtil.formatFee(amountPaid);
	}
	
	@Transient
	public String getFormattedAmountPaidWithCurrency() {
		return FormatUtil.formatFee(amountPaid, true);
	}
}
