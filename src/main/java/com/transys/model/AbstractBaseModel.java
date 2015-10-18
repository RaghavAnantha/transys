package com.transys.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractBaseModel implements BaseModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	protected Long id;
	
	@Column(name="created_by")
	protected Long createdBy;
	
	@Column(name="modified_by")
	protected Long modifiedBy;
	
	@Column(name="delete_flag")
	protected Integer deleteFlag=1;
	
	@Column(name="created_at")
	protected Date createdAt = Calendar.getInstance().getTime();
	
	@Column(name="modified_at")
	protected Date modifiedAt;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractBaseModel other = (AbstractBaseModel) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
	public String getFormattedCreatedAt() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
		if (this.createdAt != null) {
			return dateFormat.format(this.createdAt);
		} else {
			return StringUtils.EMPTY;
		}
	}
}
