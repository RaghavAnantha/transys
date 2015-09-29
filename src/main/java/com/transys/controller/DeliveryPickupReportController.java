package com.transys.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.model.Address;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/deliveryPickupReport")
public class DeliveryPickupReportController extends CRUDController<Order> {

	public DeliveryPickupReportController(){	
		setUrlContext("deliveryPickupReport");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<Order> orderList = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		model.addAttribute("ordersList",orderList);
		
		model.addAttribute("deliveryAddresses", genericDAO.findAll(Address.class));
		
		model.addAttribute("deliveryDateFrom", criteria.getSearchMap().get("deliveryDateFrom"));
		model.addAttribute("deliveryDateTo", criteria.getSearchMap().get("deliveryDateTo"));
		model.addAttribute("pickupDateFrom", criteria.getSearchMap().get("pickDateFrom"));
		model.addAttribute("pickDateTo", criteria.getSearchMap().get("pickDateTo"));	
	}
}
