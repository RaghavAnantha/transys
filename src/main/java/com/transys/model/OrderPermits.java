package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="orderPermits")
public class OrderPermits extends AbstractBaseModel {

	@JoinColumn(name="orderID")
	private Order orderID;
	
	@JoinColumn(name="permitID")
	private Permit permitID;
	
	
}
