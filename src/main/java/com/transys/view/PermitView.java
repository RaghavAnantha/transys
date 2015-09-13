package com.transys.view;

import com.transys.model.BaseModel;
import com.transys.model.Permit;

public class PermitView extends Permit {
	
	private String customerName;
	
	public <T extends BaseModel> T construct() {
		this.customerName = "setFromOrderTable";
		return (T) this;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
	
}
