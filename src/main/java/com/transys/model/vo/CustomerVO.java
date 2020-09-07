package com.transys.model.vo;

import org.apache.commons.lang3.StringUtils;

public class CustomerVO implements BaseVO {
	private Long id;
	private String companyName = StringUtils.EMPTY;
	
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
}
