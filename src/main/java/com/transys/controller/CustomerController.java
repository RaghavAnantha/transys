package com.transys.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

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
import com.transys.model.AbstractBaseModel;
import com.transys.model.Address;
import com.transys.model.Customer;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.BaseModel;

@Controller
@RequestMapping("/customer")
public class CustomerController extends CRUDController<Customer> {
	
	public CustomerController(){
		setUrlContext("customer");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("customer",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id!=0 order by obj.companyName asc"));
		model.addAttribute("customerIds",genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id is not null order by obj.id asc"));
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		List<String> statusList = new ArrayList<String>();
		statusList.add("Active");
		model.addAttribute("statuses", statusList);
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "billing");
		//return urlContext + "/form";
		
		model.addAttribute("deliveryAddressModelObject", new Address());
				
		return urlContext + "/customer";
	}
	
	//@Override
	public String saveSuccess(ModelMap model, HttpServletRequest request, Customer entity) {
		setupCreate(model, request);
		model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "billing");
		model.addAttribute("mode", "ADD");
		
		Customer customer = new Customer();
		customer.setId(entity.getId());
		Address address = new Address();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from Address obj where obj.customer.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("deliveryAddressList", addressList);
		//return urlContext + "/form";
		return urlContext + "/customer";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"companyName",null,null));
		model.addAttribute("activeTab", "manageCustomer");
		//model.addAttribute("activeSubTab", "billing");
		model.addAttribute("mode", "MANAGE");
		//return urlContext + "/list";
		return urlContext + "/customer";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "billing");
		
		Customer customerToBeEdited = (Customer)model.get("modelObject");
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerToBeEdited.getId());
		Address address = new Address();
		address.setCustomer(emptyCustomer);
		model.addAttribute("deliveryAddressModelObject", address);
	
		
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from Address obj where obj.customer.id=" +  customerToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		//return urlContext + "/form";
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/customer";
	}
	/*@RequestMapping(method = RequestMethod.GET, value = "/address.do")
	public String displayDeliveryAddress(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "deliveryAddress");
		return urlContext + "/deliveryAddress";
	}*/
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "manageCustomer");
		return urlContext + "/customer";
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
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#save(javax.servlet.http.HttpServletRequest, com.primovision.lutransport.model.BaseModel, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)
	 */
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Customer entity,
			BindingResult bindingResult, ModelMap model) {
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
		//return super.save(request, entity, bindingResult, model);
		
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		//return "redirect:/" + urlContext + "/list.do";
		//model.addAttribute("activeTab", "manageCustomer");
		//return urlContext + "/list";
		
		/*SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO: Fix me 
		criteria.getSearchMap().remove("_csrf");*/
		
		/*setupList(model, request);
		
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"companyName",null,null));
		model.addAttribute("activeTab", "manageCustomer");
		//return urlContext + "/list";
		return urlContext + "/customer";*/
		//request.getSession().removeAttribute("searchCriteria");
		//request.getParameterMap().remove("_csrf");
		
		//return list(model, request);
		return saveSuccess(model, request, entity);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveDeliveryAddress.do")
	public String saveDeliveryAddress(HttpServletRequest request,
			@ModelAttribute("deliveryAddressModelObject") Address entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		//beforeSave(request, entity, model);
		if (entity instanceof AbstractBaseModel) {
			AbstractBaseModel baseModel = (AbstractBaseModel) entity;
			if (baseModel.getId() == null) {
				baseModel.setCreatedAt(Calendar.getInstance().getTime());
				if (baseModel.getCreatedBy()==null) {
					baseModel.setCreatedBy(getUser(request).getId());
				}
			} else {
				baseModel.setModifiedAt(Calendar.getInstance().getTime());
				if (baseModel.getModifiedBy()==null) {
					baseModel.setModifiedBy(getUser(request).getId());
				}
			}
		}
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		//return "redirect:/" + urlContext + "/list.do";
		//model.addAttribute("activeTab", "manageCustomer");
		//return urlContext + "/list";
		
		/*SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO: Fix me 
		criteria.getSearchMap().remove("_csrf");*/
		
		/*setupList(model, request);
		
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"companyName",null,null));
		model.addAttribute("activeTab", "manageCustomer");
		//return urlContext + "/list";
		return urlContext + "/customer";*/
		//request.getSession().removeAttribute("searchCriteria");
		//request.getParameterMap().remove("_csrf");
		
		//return list(model, request);
		//return saveSuccess(model, request, entity);
		setupCreate(model, request);
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "delivery");
		model.addAttribute("mode", "ADD");
		
		Long customerId = entity.getCustomer().getId();
		List<BaseModel> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId);
		model.addAttribute("modelObject", customerList.get(0));
		
		Customer customer = new Customer();
		customer.setId(entity.getCustomer().getId());
		Address address = new Address();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from Address obj where obj.customer.id=" +  customerId + " order by obj.id asc");
		model.addAttribute("deliveryAddressList", addressList);
		//return urlContext + "/form";
		return urlContext + "/customer";
	}
}
	
	
	
	

