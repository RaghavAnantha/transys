package com.transys.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.transys.model.Order;

public class CustomerReportVO extends BaseVO {
	private Long id;
	
	private String companyName;
	
	private String contactName;
	private String phoneNumber;
	
	private Integer totalOrders;
	private BigDecimal totalOrderAmount;
	
	private String status;
	
	private List<OrderReportVO> orderList;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getTotalOrders() {
		return totalOrders;
	}
	public void setTotalOrders(Integer totalOrders) {
		this.totalOrders = totalOrders;
	}
	public BigDecimal getTotalOrderAmount() {
		return totalOrderAmount;
	}
	public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}
	public List<OrderReportVO> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderReportVO> orderList) {
		this.orderList = orderList;
	}
}
