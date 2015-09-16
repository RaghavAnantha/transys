package com.transys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.Address;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.LocationType;
import com.transys.model.Order;
import com.transys.model.Permit;
import com.transys.model.PermitClass;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;
import com.transys.model.State;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/permit")
public class PermitController extends CRUDController<Permit> {
	
	public PermitController(){
		setUrlContext("permit");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(LocationType.class, new AbstractModelEditor(LocationType.class));
		binder.registerCustomEditor(PermitClass.class, new AbstractModelEditor(PermitClass.class));
		binder.registerCustomEditor(PermitType.class, new AbstractModelEditor(PermitType.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress")
	public @ResponseBody String displayCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customer = request.getParameter("customer");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().put("id", Long.parseLong(customer));
		BaseModel customerObj = genericDAO.search(Customer.class, criteria, "id", null, null).get(0);
		String json = (new Gson()).toJson(customerObj);
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
//		criteria.getSearchMap().put("id!",0l);
//		model.addAttribute("list", createViewObjects(genericDAO.search(getEntityClass(), criteria, "number", null, null)));
		model.addAttribute("list", genericDAO.search(Permit.class, criteria, "id", null, null));
		// add order# corresponding to this permit in the attribute
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/permit";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "permitDetails");
		//return urlContext + "/form";
		
		//model.addAttribute("deliveryAddressModelObject", new Address());
				
		return urlContext + "/permit";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		List<Address> addresses = genericDAO.findUniqueByCriteria(Address.class, criterias, "line1", false);
		System.out.println("List of addresses = " + addresses.size());
	   model.addAttribute("deliveryAddress", addresses);
		model.addAttribute("customer", genericDAO.findByCriteria(Customer.class, criterias, "contactName", false));
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "type", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
		
		// TODO: Remove
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
	}
	
}
