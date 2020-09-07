package com.transys.model.vo;

import org.apache.commons.lang3.StringUtils;

import com.transys.core.util.CoreUtil;

public class DeliveryAddressVO implements BaseVO {
	private Long id;
	private String line1 = StringUtils.EMPTY;
	private String line2 = StringUtils.EMPTY;
	
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
	public String getFullLine() {
		return CoreUtil.concatenate(getLine1(), getLine2());
	}
}
