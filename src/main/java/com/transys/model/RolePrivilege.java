package com.transys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rolePrivilege")
@SuppressWarnings("serial")
public class RolePrivilege extends AbstractBaseModel {

	/**
	 * Attribute businessObject
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "businessObjectId")
	private BusinessObject businessObject;

	/**
	 * Attribute userRole
	 */
	@ManyToOne
	@JoinColumn(name = "roleId")
	private Role role;

	@Column(name = "permissionType")
	private Integer permissionType;

	/**
	 * get businessObject
	 */

	public BusinessObject getBusinessObject() {
		return this.businessObject;
	}

	/**
	 * set businessObject
	 */
	public void setBusinessObject(BusinessObject businessObject) {
		this.businessObject = businessObject;
	}
	
	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}
	
	
}