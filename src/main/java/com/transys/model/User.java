package com.transys.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "userInfo")
@NamedQueries({@NamedQuery(name = "user.getByName", query = "select obj from User obj where obj.username=:name")})
public class User extends AbstractBaseModel implements Comparable, Auditable {
	private static final long serialVersionUID = 1807241954265797561L;
	
	@OneToOne
	@JoinColumn(name="employeeId")
	private Employee employee;
	
	@Column(name = "username")
	@NotEmpty(message = "User name is required.")
	private String username;
	
	@Transient
	private String name;
	
	@Column(name = "password")
	@NotEmpty(message = "Password is required.")
	@Size(min = 5, message = "Password should be of minimum 5 characters")
//	@Pattern(regexp = "[a-zA-Z]*[0-9]*", message = "Password should be alpanumeric.")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@Column(name = "lastLoginDate")
	private Date lastLoginDate;
	
	@Column(name = "loginAttempts")
	private Integer loginAttempts = 0;
	
	@ManyToOne
	@JoinColumn(name = "accountStatusId")
	private EmployeeStatus accountStatus;
	
	@Column(name="comments")
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="roleId")
	private Role role;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return this.employee.getFullName();
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public Integer getLoginAttempts() {
		return loginAttempts;
	}
	
	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	
	public EmployeeStatus getAccountStatus() {
		return accountStatus;
	}
	
	public void setAccountStatus(EmployeeStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Transient
	public boolean isOmniAdmin() {
		if ("ADMIN".equalsIgnoreCase(role.getName())) {
			return true;
		}
		return false;
	}
	
	//@Override
	public int compareTo(Object otherUser) {
		if (otherUser != null)
			return this.username.compareToIgnoreCase(((User) otherUser)
					.getUsername());
		return 0;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	//@Override
	public List getAuditableFields() {
		List props = new ArrayList();
		props.add("username");
		props.add("lastLoginDate");
		props.add("accountStatus");
		return props;
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	//@Override
	public String getAuditMessage() {
		return null;
	}
	
	//@Override
	public String getPrimaryField() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean skipAudit() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Transient
	public String getFormattedLastLoginDate() {
		if (lastLoginDate == null) {
			return StringUtils.EMPTY;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(lastLoginDate);
	}
}
