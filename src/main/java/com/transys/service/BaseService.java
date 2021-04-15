package com.transys.service;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.transys.core.dao.GenericDAO;
import com.transys.model.AbstractBaseModel;
import com.transys.model.User;

public abstract class BaseService {
	protected static Logger log = LogManager.getLogger("com.transys.service");
	
	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}
	
	protected User getUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute("userInfo");
	}
	
	protected Long getUserId(HttpServletRequest request) {
		User user = getUser(request);
		return user.getId();
	}
	
	protected void setModifier(HttpServletRequest request, AbstractBaseModel entity) {
		if (entity.getId() == null) {
			entity.setCreatedAt(Calendar.getInstance().getTime());
			if (entity.getCreatedBy() == null) {
				entity.setCreatedBy(getUserId(request));
			}
		} else {
			entity.setModifiedAt(Calendar.getInstance().getTime());
			if (entity.getModifiedBy() == null) {
				entity.setModifiedBy(getUserId(request));
			}
		}
	}
}
