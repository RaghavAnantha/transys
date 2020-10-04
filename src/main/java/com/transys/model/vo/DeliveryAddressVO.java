package com.transys.model.vo;

import org.apache.commons.lang3.StringUtils;

import com.transys.core.util.FormatUtil;

public class DeliveryAddressVO extends BaseVO {
	private Long id;
	private String line1 = StringUtils.EMPTY;
	private String line2 = StringUtils.EMPTY;
	private String city = StringUtils.EMPTY;
	private String state = StringUtils.EMPTY;
	private String zipcode = StringUtils.EMPTY;
	
	private Long orderId;
	private String customerName = StringUtils.EMPTY;
	
	private String geoCode = StringUtils.EMPTY;
	private String fullLine = StringUtils.EMPTY;
	private String fullAddress = StringUtils.EMPTY;
	private Double latitude;
	private Double longitude;
	
	private String dumpsterNum;
	private String dumpsterSize;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getGeoCode() {
		return geoCode;
	}
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
		
		String[] geoCodeArr = geoCode.split(",");
		setLatitude(new Double(geoCodeArr[0]));
		setLongitude(new Double(geoCodeArr[1]));
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	public void setFullLine(String fullLine) {
		this.fullLine = fullLine;
	}
	public String getFullLine() {
		return StringUtils.isNotEmpty(fullLine) ? fullLine : FormatUtil.formatAddress(getLine1(), getLine2());
	}
	public String getFullAddress() {
		return StringUtils.isNotEmpty(fullAddress) ? fullAddress : getFullAddress(", ");
	}
	public String getFullAddress(String lineSeparator) {
		if (StringUtils.isNotEmpty(fullAddress)) {
			return fullAddress;
		}
		return FormatUtil.formatAddress(getLine1(), getLine2(), getCity(), getState(), getZipcode(),
				lineSeparator);
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getDumpsterNum() {
		return dumpsterNum;
	}
	public void setDumpsterNum(String dumpsterNum) {
		this.dumpsterNum = dumpsterNum;
	}
	public String getDumpsterSize() {
		return dumpsterSize;
	}
	public void setDumpsterSize(String dumpsterSize) {
		this.dumpsterSize = dumpsterSize;
	}
}
