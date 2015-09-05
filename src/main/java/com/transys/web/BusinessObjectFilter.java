package com.transys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.transys.model.menu.MenuHelper;
import com.transys.model.menu.MenuTree;

//import com.transys.core.dao.GenericDAO;
import com.transys.model.BusinessObject;

public class BusinessObjectFilter implements Filter {
	private static Logger log = LogManager.getLogger(BusinessObjectFilter.class.getName());
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String url = ((HttpServletRequest)request).getServletPath();
		/*GenericDAO genericDAO = (GenericDAO)SpringAppContext.getBean("genericDAO");
		String query = "select obj from BusinessObject obj where url like '%"+url+"%'";
		List<BusinessObject> bos = genericDAO.executeSimpleQuery(query);
		if (bos!=null && bos.size()>0) {
			((HttpServletRequest)request).getSession().setAttribute("curObj", bos.get(0));
			log.debug("Current business object is:"+bos.get(0).getObjectHierarchy());
		}*/
		BusinessObject bo = mockBO();
		((HttpServletRequest)request).getSession().setAttribute("curObj", bo);
		
		MenuTree menuTree = mockMenuTree(bo);
		((HttpServletRequest)request).getSession().setAttribute("menuTree", menuTree);
		chain.doFilter(request, response);
	}
	
	private BusinessObject mockBO() {
		BusinessObject bo = new BusinessObject();
		bo.setAction("transys/customer");
		bo.setId(1l);
		bo.setUrl("transys/customer");
		return bo;
	}
	
	private MenuTree mockMenuTree(BusinessObject bo) {
		List<BusinessObject> businessObjects = new ArrayList<BusinessObject>();
		businessObjects.add(bo);
		MenuTree menuTree = MenuHelper.getMenuTree(businessObjects);
		return menuTree;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
