package com.transys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="orderPermits")
public class OrderPermits extends AbstractBaseModel {
	@ManyToOne
	@JoinColumn(name="orderId")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name="permitId")
	private Permit permit;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Permit getPermit() {
		return permit;
	}

	public void setPermit(Permit permit) {
		this.permit = permit;
	}
	
	
	
/*	private Set<Permit> permits = new HashSet<Permit>(
			0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "permit")
	public Set<Permit> getPermits() {
		return this.permits;
	}

	public void setPermits(Set<Permit> permit) {
		this.permits = permit;
	}


	public Long getOrder() {
		return orderID;
	}

	public void setOrder(Long orderID) {
		this.orderID = orderID;
	}*/

}
