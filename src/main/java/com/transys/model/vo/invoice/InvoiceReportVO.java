package com.transys.model.vo.invoice;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import com.transys.core.util.FormatUtil;

import com.transys.model.invoice.OrderInvoicePayment;
import com.transys.model.vo.BaseVO;

public class InvoiceReportVO extends BaseVO {
	private Long invoiceNo;
	private Date invoiceDate;
	
	private String companyName;
	private String billingAddress;
	
	private String orderDateFrom;
	private String orderDateTo;
	
	private String notes;
	
	private BigDecimal totalFees;
	private BigDecimal totalAmountPaid;
	private BigDecimal totalDiscount;
	private BigDecimal totalBalanceAmountDue;
	private BigDecimal totalInvoicedAmount;
	
	private BigDecimal totalInvoicePaymentDone;
	private BigDecimal totalInvoiceBalanceDue;
	private BigDecimal totalInvoiceBalanceAvailable;
	
	private List<InvoiceVO> invoiceVOList;
	private List<OrderInvoicePayment> invoicePaymentList;
	
	public Long getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(Long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getOrderDateFrom() {
		return orderDateFrom;
	}
	public void setOrderDateFrom(String orderDateFrom) {
		this.orderDateFrom = orderDateFrom;
	}
	public String getOrderDateTo() {
		return orderDateTo;
	}
	public void setOrderDateTo(String orderDateTo) {
		this.orderDateTo = orderDateTo;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public BigDecimal getTotalFees() {
		return totalFees;
	}
	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}
	public BigDecimal getTotalInvoicedAmount() {
		return totalInvoicedAmount;
	}
	public void setTotalInvoicedAmount(BigDecimal totalInvoicedAmount) {
		this.totalInvoicedAmount = totalInvoicedAmount;
	}
	public BigDecimal getTotalAmountPaid() {
		return totalAmountPaid;
	}
	public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
		this.totalAmountPaid = totalAmountPaid;
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
	public BigDecimal getTotalInvoicePaymentDone() {
		return totalInvoicePaymentDone;
	}
	public void setTotalInvoicePaymentDone(BigDecimal totalInvoicePaymentDone) {
		this.totalInvoicePaymentDone = totalInvoicePaymentDone;
	}
	public BigDecimal getTotalInvoiceBalanceDue() {
		return totalInvoiceBalanceDue;
	}
	public void setTotalInvoiceBalanceDue(BigDecimal totalInvoiceBalanceDue) {
		this.totalInvoiceBalanceDue = totalInvoiceBalanceDue;
	}
	public BigDecimal getTotalInvoiceBalanceAvailable() {
		return totalInvoiceBalanceAvailable;
	}
	public void setTotalInvoiceBalanceAvailable(BigDecimal totalInvoiceBalanceAvailable) {
		this.totalInvoiceBalanceAvailable = totalInvoiceBalanceAvailable;
	}
	public List<InvoiceVO> getInvoiceVOList() {
		return invoiceVOList;
	}
	public void setInvoiceVOList(List<InvoiceVO> invoiceVOList) {
		this.invoiceVOList = invoiceVOList;
	}
	public List<OrderInvoicePayment> getInvoicePaymentList() {
		return invoicePaymentList;
	}
	public void setInvoicePaymentList(List<OrderInvoicePayment> invoicePaymentList) {
		this.invoicePaymentList = invoicePaymentList;
	}
	public String getFormattedInvoiceDate() {
		return FormatUtil.formatDate(this.invoiceDate);
	}
}
