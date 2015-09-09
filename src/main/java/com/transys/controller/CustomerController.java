package com.transys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.CRUDController;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.Customer;
//import com.primovision.lutransport.model.FuelVendor;
//import com.primovision.lutransport.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;

@Controller
public class CustomerController extends CRUDController<Customer> {
	
	public CustomerController(){
		setUrlContext("transysapp/customer");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	@RequestMapping(value = "/customer/main", method = RequestMethod.GET)
	public String main(HttpServletRequest request, ModelMap model) {
		setupList(model, request);
		return "customer/customer";
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public String search(HttpServletRequest request, ModelMap model) {
		if (request.getParameterMap().size() == 0) {
			return displaySearch(request, model);
		} else {
			return executeSearch(request, model);
		}
	}
	
	public String displaySearch(HttpServletRequest request, ModelMap model) {
		/*List<Customer> customerList = new ArrayList<Customer>();
		model.addAttribute("customer", customerList);
		model.addAttribute("customerIds", customerList);*/
		setupList(model, request);
		
		//populateSearchCriteria(request, request.getParameterMap());
		
		/*setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().put("id!",0l);*/
		
		//return "customer/customer";
		return "customer/list";
	}
	
	public String executeSearch(HttpServletRequest request, ModelMap model) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().put("id!",0l);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria,"company_name",null,null));
		
		return "customer/customer";
	}
	
	/*@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		List<Customer> customerList = mockCustomerList();
		model.addAttribute("customer", customerList);
		model.addAttribute("customerIds", customerList);
		//model.addAttribute("customer",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id!=0 order by obj.name asc"));
		//model.addAttribute("customerIds",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.customerNameID is not null order by obj.customerNameID asc"));
      //model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
	}*/
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		//List<Customer> customerList = mockCustomerList();
		//List<State> stateList = mockStateList();
		Map criterias = new HashMap();
		model.addAttribute("customer",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id!=0 order by obj.companyName asc"));
		model.addAttribute("customerIds",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id is not null order by obj.id asc"));
      model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		model.addAttribute("modelObject", new Customer());
	}
	
	@Override
	public void setupCreate2(ModelMap model, HttpServletRequest request) {
		//List<Customer> customerList = mockCustomerList();
		//List<State> stateList = mockStateList();
		Map criterias = new HashMap();
		model.addAttribute("customer",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id!=0 order by obj.companyName asc"));
		model.addAttribute("customerIds",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id is not null order by obj.id asc"));
      model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		//model.addAttribute("modelObject", new Customer());
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		//model.addAttribute("list", mockCustomerList());
		setupCreate(model, request);
	}
	
	private List<Customer> mockCustomerList() {
		List<Customer> customerList = new ArrayList<Customer>();
		Customer customer = new Customer();
		customer.setId(0l);
		customer.setCompanyName("Aberdeen construction");
		
		State state = new State();
		state.setCode("IL");
		state.setName("Illinois");
		customer.setState(state);
		//customer.setState("IL");
		
		customerList.add(customer);
		
		return customerList;
	}
	
	private List<State> mockStateList() {
		List<State> stateList = new ArrayList<State>();
		
		State state = new State();
		state.setId(1l);
		state.setCode("IL");
		state.setName("Illinois");
		
		
		stateList.add(state);
		
		return stateList;
	}
	
	@RequestMapping(value = "/customer/add", method = RequestMethod.GET)
	public String displayAdd(HttpServletRequest request, ModelMap model) {
		model.addAttribute("modelObject", new Customer());
		//List<State> stateList = mockStateList();
		Map criterias = new HashMap();
		
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		//model.addAttribute("state", stateList);
		return "customer/form";
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.POST)
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Customer entity,
			BindingResult bindingResult, ModelMap model) {
		if(entity.getState() == null)
			bindingResult.rejectValue("state", "error.select.option", null, null);
		if(entity.getCity() == null)
			bindingResult.rejectValue("city", "typeMismatch.java.lang.String", null, null);
		//server side verification
		if(entity.getZipcode() != null){
			if(entity.getZipcode().toString().length() < 5)
			bindingResult.rejectValue("zipcode", "typeMismatch.java.lang.Integer", null, null);
		}
		if(!StringUtils.isEmpty(entity.getPhone())){
			if(entity.getPhone().length() < 12)
				bindingResult.rejectValue("phone", "typeMismatch.java.lang.String", null, null);
		}
		if(!StringUtils.isEmpty(entity.getFax())){
			if(entity.getFax().length() < 12)
				bindingResult.rejectValue("fax", "typeMismatch.java.lang.String", null, null);
		}
		return super.save(request, entity, bindingResult, model);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	/*@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) 
	{
		Map criterias = new HashMap();
		model.addAttribute("customer",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id!=0 order by obj.name asc"));
		model.addAttribute("customerIds",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.customerNameID is not null order by obj.customerNameID asc"));
        model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
	}*/
	
	
	/*@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		 criteria.getSearchMap().put("id!",0l);
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		return urlContext + "/list";
	}*/
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	/*@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}*/
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#save(javax.servlet.http.HttpServletRequest, com.primovision.lutransport.model.BaseModel, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)
	 */
	/*@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Customer entity,
			BindingResult bindingResult, ModelMap model)
	{*/
         /*if(entity.getState() == null)
			bindingResult.rejectValue("state", "error.select.option", null, null);*/
		/*if(entity.getCity() == null)
			bindingResult.rejectValue("city", "typeMismatch.java.lang.String", null, null);
		//server side verification
		if(entity.getZipcode() != null){
			if(entity.getZipcode().toString().length() < 5)
			bindingResult.rejectValue("zipcode", "typeMismatch.java.lang.Integer", null, null);
		}
		if(!StringUtils.isEmpty(entity.getPhone())){
			if(entity.getPhone().length() < 12)
				bindingResult.rejectValue("phone", "typeMismatch.java.lang.String", null, null);
		}
		if(!StringUtils.isEmpty(entity.getFax())){
			if(entity.getFax().length() < 12)
				bindingResult.rejectValue("fax", "typeMismatch.java.lang.String", null, null);
		}*/
	/*return super.save(request, entity, bindingResult, model);
	}*/
}
	
	
	
	

