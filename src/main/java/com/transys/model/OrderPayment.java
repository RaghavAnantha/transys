package com.transys.model;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.transys.core.util.FormatUtil;

@Entity
@Table(name="orderPayment")
public class OrderPayment extends AbstractBaseModel {
		@ManyToOne
		@JoinColumn(name="orderId") 
		private Order order;
		
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
		
		@Column(name="invoiceId")
		private Long invoiceId;
		
		@Column(name="invoicePaymentId")
		private Long invoicePaymentId;

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
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

		public Long getInvoiceId() {
			return invoiceId;
		}

		public void setInvoiceId(Long invoiceId) {
			this.invoiceId = invoiceId;
		}

		public Long getInvoicePaymentId() {
			return invoicePaymentId;
		}

		public void setInvoicePaymentId(Long invoicePaymentId) {
			this.invoicePaymentId = invoicePaymentId;
		}

		@Transient
		public String getFormattedPaymentDate() {
			return FormatUtil.formatDate(this.paymentDate);
		}
		
		@Transient
		public String getFormattedCCExpDate() {
			return FormatUtil.formatDate(this.ccExpDate);
		}
}
