package com.transys.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
		
		@Column(name="checkNum")
		private String checkNum;
		
		@Column(name="amountPaid")
		private BigDecimal amountPaid;

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
}
