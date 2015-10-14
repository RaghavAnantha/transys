package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
import com.transys.controller.CRUDController;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.core.util.MimeUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.DeliveryAddress;
import com.transys.model.Customer;
import com.transys.model.CustomerStatus;
import com.transys.model.CustomerType;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderPayment;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.vo.CustomerReportVO;
import com.transys.model.BaseModel;

@Controller
@RequestMapping("/customer")
public class CustomerController extends CRUDController<Customer> {
	public CustomerController() {
		setUrlContext("customer");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(DeliveryAddress.class, new AbstractModelEditor(DeliveryAddress.class));
		binder.registerCustomEditor(CustomerType.class, new AbstractModelEditor(CustomerType.class));
		binder.registerCustomEditor(CustomerStatus.class, new AbstractModelEditor(CustomerStatus.class));
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
		
		model.addAttribute("customerTypes", genericDAO.findByCriteria(CustomerType.class, criterias, "customerType", false));
		
		List<String> chargeCompanyOptions = new ArrayList<String>();
		chargeCompanyOptions.add("Yes");
		chargeCompanyOptions.add("No");
		model.addAttribute("chargeCompanyOptions", chargeCompanyOptions);
		
		model.addAttribute("customerStatuses", genericDAO.findByCriteria(CustomerStatus.class, criterias, "status", false));
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "billing");
		//return urlContext + "/form";
		
		model.addAttribute("deliveryAddressModelObject", new DeliveryAddress());
				
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createModal.do")
	public String createModal(ModelMap model, HttpServletRequest request) {
		//setupCreate(model, request);
		//model.addAttribute("activeTab", "manageCustomer");
		//model.addAttribute("mode", "ADD");
		//model.addAttribute("activeSubTab", "billing");
		//return urlContext + "/form";
		
		//model.addAttribute("deliveryAddressModelObject", new Address());
		
		Map criterias = new HashMap();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		model.addAttribute("customerTypes", genericDAO.findByCriteria(CustomerType.class, criterias, "customerType", false));
		
		List<String> chargeCompanyOptions = new ArrayList<String>();
		chargeCompanyOptions.add("Yes");
		chargeCompanyOptions.add("No");
		model.addAttribute("chargeCompanyOptions", chargeCompanyOptions);
		
		model.addAttribute("customerStatuses", genericDAO.findByCriteria(CustomerStatus.class, criterias, "status", false));
				
		return urlContext + "/formModal";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deliveryAddressCreateModal.do")
	public String deliveryAddressCreateModal(ModelMap model, HttpServletRequest request,
			 										@RequestParam(value = "customerId") Long customerId) {
		//setupCreate(model, request);
		//model.addAttribute("activeTab", "manageCustomer");
		//model.addAttribute("mode", "ADD");
		//model.addAttribute("activeSubTab", "billing");
		//return urlContext + "/form";
		
		Map criterias = new HashMap();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		DeliveryAddress emptyAddress = new DeliveryAddress();
		emptyAddress.setCustomer(emptyCustomer);
		
		model.addAttribute("deliveryAddressModelObject", emptyAddress);
		
		return urlContext + "/deliveryAddressModal";
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
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  entity.getId() + " order by obj.id asc");
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerListReport.do")
	public String customerListReport(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		
		List<Customer> customerList =  genericDAO.search(getEntityClass(), criteria, "companyName", null, null);
		
		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		
		StringBuffer Ids = new StringBuffer("(");
		Integer count = 0;
		for (Customer customer : customerList) {
			count++;
			Ids.append(customer.getId());
			if (count < customerList.size()) {
				Ids.append(",");
			}
			else {
				Ids.append(")");
			}
			customerMap.put(customer.getId(), customer);
		}
        
      List<OrderFees> orderFeesList = genericDAO.executeSimpleQuery("select obj from OrderFees obj where obj.order.customer.id IN " + Ids.toString());
      List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>() ;
      for (Long key : customerMap.keySet()) {
      	CustomerReportVO customerReportVO =  new CustomerReportVO();
			BigDecimal sum = new BigDecimal("0.0");
			Integer Ordercount = 0;
			for (OrderFees anOrderFees: orderFeesList ) {
	          if (anOrderFees.getOrder().getCustomer().getId() == key) {
	               sum = sum.add(anOrderFees.getTotalFees());
	               Ordercount++;
	          }
	      }
	      customerReportVO.setCompanyName(customerMap.get(key).getCompanyName());
         customerReportVO.setContactName(customerMap.get(key).getContactName());
         customerReportVO.setPhoneNumber(customerMap.get(key).getPhone());
         customerReportVO.setTotalAmount(sum);
         customerReportVO.setTotalOrders(Ordercount);
         customerReportVO.setId(customerMap.get(key).getId());
         customerReportVO.setStatus(customerMap.get(key).getCustomerStatus().getStatus());

         customerReportVOList.add(customerReportVO);
	   }

		
		//model.addAttribute("customerReportVOList",customerReportVOList);
		request.getSession().setAttribute("customerReportVOList", customerReportVOList);
		
		model.addAttribute("activeTab", "customerReports");
		model.addAttribute("activeSubTab", "customerListReport");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateCustomerListReport.do")
	public void generateCustomerListReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		
		try {
			List<CustomerReportVO> customerReportVOList = (List<CustomerReportVO>) request.getSession().getAttribute("customerReportVOList");
			
			List<Map<String, ?>> reportData = new ArrayList<Map<String, ?>>();
			for (CustomerReportVO aCustomerReportVO : customerReportVOList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", aCustomerReportVO.getId().toString());
				map.put("companyName", aCustomerReportVO.getCompanyName());
				map.put("contactName", aCustomerReportVO.getContactName());
				map.put("phoneNumber", aCustomerReportVO.getPhoneNumber());
				map.put("status", aCustomerReportVO.getStatus());
				map.put("totalOrders", aCustomerReportVO.getTotalOrders().toString());
				map.put("totalAmount", aCustomerReportVO.getTotalAmount().toString());
				
				reportData.add(map);
			}
			
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
					"attachment;filename= customerListReport." + type);
			}
			
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = new HashMap<String,Object>();
			
			if (!type.equals("print") && !type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("customerListReport",
						reportData, params, type, request);
			} else if (type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("customerListReportPdf",
						reportData, params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport("customerListReport"+"print",
						reportData, params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
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
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(emptyCustomer);
		model.addAttribute("deliveryAddressModelObject", address);
	
		
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerToBeEdited.getId() + " order by obj.id asc");
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
		
		entity.getCustomerNotes().get(0).setCustomer(entity);
		entity.getCustomerNotes().get(0).setCreatedBy(entity.getCreatedBy());
		entity.getCustomerNotes().get(0).setModifiedBy(entity.getModifiedBy());
		
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
			@ModelAttribute("deliveryAddressModelObject") DeliveryAddress entity,
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
		
		Customer customer = new Customer();
		customer.setId(entity.getCustomer().getId());
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		Long customerId = entity.getCustomer().getId();
		List<BaseModel> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId);
		model.addAttribute("modelObject", customerList.get(0));
		
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " order by obj.id asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "delivery");
		model.addAttribute("mode", "ADD");
		
		//return urlContext + "/form";
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveDeliveryAddressModal.do")
	public @ResponseBody String saveDeliveryAddressModal(HttpServletRequest request,
			@ModelAttribute("deliveryAddressModelObject") DeliveryAddress entity,
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
		//setupCreate(model, request);
		//model.addAttribute("modelObject", entity);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
		
		//String json = (new Gson()).toJson(entity);
		//return json;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveModal.do")
	public @ResponseBody String saveModal(HttpServletRequest request,
			@ModelAttribute("modelObject") Customer entity,
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
		
		entity.getDeliveryAddress().get(0).setCustomer(entity);
		entity.getDeliveryAddress().get(0).setCreatedBy(entity.getCreatedBy());
		entity.getDeliveryAddress().get(0).setModifiedBy(entity.getModifiedBy());
		
		entity.getCustomerNotes().get(0).setCustomer(entity);
		entity.getCustomerNotes().get(0).setCreatedBy(entity.getCreatedBy());
		entity.getCustomerNotes().get(0).setModifiedBy(entity.getModifiedBy());
		
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
		//setupCreate(model, request);
		//model.addAttribute("modelObject", entity);
		
		//Or retrieve cust again?
		List<State> stateList = genericDAO.executeSimpleQuery("select obj from State obj where obj.id= " + entity.getState().getId());
		entity.getState().setName(stateList.get(0).getName());
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
				
		/*String json = (new Gson()).toJson(entity);
		return json;*/
	}
}
