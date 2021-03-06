package com.transys.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.transys.controller.CRUDController;

import com.transys.controller.editor.AbstractModelEditor;

import com.transys.core.util.DateUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.BaseModel;
import com.transys.model.AbstractBaseModel;
import com.transys.model.DeliveryAddress;
import com.transys.model.Customer;
import com.transys.model.CustomerNotes;
import com.transys.model.CustomerStatus;
import com.transys.model.CustomerType;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;
import com.transys.model.vo.CustomerVO;

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
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		//model.addAttribute("customerIds", genericDAO.executeSimpleQuery("select obj.id from Customer obj where obj.deleteFlag='1' order by obj.id asc"));
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		String[][] strArrOfArr = ModelUtil.extractContactDetails(customerVOList);
		String[] phoneArr = strArrOfArr[0];
		String[] contactNameArr = strArrOfArr[1];
		
		model.addAttribute("phones", phoneArr);
		model.addAttribute("contactNames", contactNameArr);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
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
		
		model.addAttribute("notesModelObject", new CustomerNotes());
		
		model.addAttribute("deliveryAddressModelObject", new DeliveryAddress());
			
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createModal.do")
	public String createModal(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
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
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		DeliveryAddress emptyAddress = new DeliveryAddress();
		emptyAddress.setCustomer(emptyCustomer);
		
		model.addAttribute("deliveryAddressModelObject", emptyAddress);
		
		return urlContext + "/deliveryAddressModal";
	}
	
	private String saveSuccess(ModelMap model, HttpServletRequest request, Customer entity) {
		setupCreate(model, request);
		
		model.addAttribute("msgCtx", "manageCustomer");
		model.addAttribute("msg", "Customer saved successfully");
		
		model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "billing");
		model.addAttribute("mode", "ADD");
		
		Long customerId = entity.getId();
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from CustomerNotes obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		Customer customer = new Customer();
		customer.setId(customerId);
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		List<BaseModel> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.line1 asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		populateAggregartionValues(model, customerId);
		
		return urlContext + "/customer";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null,null));
		model.addAttribute("activeTab", "manageCustomer");
		
		model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/customer";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "billing");
		
		Customer customerToBeEdited = (Customer)model.get("modelObject");
		Long customerId = customerToBeEdited.getId();
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer);
		model.addAttribute("notesModelObject", notes);
	
		List<CustomerNotes> notesList = genericDAO.executeSimpleQuery("select obj from CustomerNotes obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(emptyCustomer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<DeliveryAddress> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.line1 asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		populateAggregartionValues(model, customerId);
		
		return urlContext + "/customer";
	}
	
	private void populateAggregartionValues(ModelMap model, Long customerId) {
		/*String orderQuery = "select obj from Order obj where"
								+ " obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id desc";
		List<Order> orderList = genericDAO.executeSimpleQuery(orderQuery);
		Integer totalOrders = orderList.size();
		
		String lastDeliveryDate = StringUtils.EMPTY;
		for (Order anOrder : orderList) {
			if (anOrder.getDeliveryDate() != null) {
				lastDeliveryDate = anOrder.getFormattedDeliveryDate();
				break;
			}
		}*/
		
		String aggregationQuery = "select count(obj), max(obj.deliveryDate) from Order obj where"
				+ " obj.customer.id=" +  customerId + " and obj.deleteFlag='1'";
		Object aggregationObjects[] = (Object[]) genericDAO.executeSingleResultQuery(aggregationQuery);
		Long totalOrders = (Long) aggregationObjects[0];
		String lastDeliveryDateStr = StringUtils.EMPTY;
		if (aggregationObjects[1] != null) {
			Date lastDeliveryDate = (Date) aggregationObjects[1];
			lastDeliveryDateStr = DateUtil.formatToInputDate(lastDeliveryDate);
		}
		
		model.addAttribute("totalOrders", totalOrders);
		model.addAttribute("lastDeliveryDate", lastDeliveryDateStr);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/customer";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/customer";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Customer entity,
			BindingResult bindingResult, ModelMap model) {
      /*if(entity.getState() == null)
			bindingResult.rejectValue("state", "error.select.option", null, null);*/
		/*if(entity.getCity() == null)
			bindingResult.rejectValue("city", "typeMismatch.java.lang.String", null, null);
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
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		beforeSave(request, entity, model);
		
		Long modifiedBy = getUser(request).getId();
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupCustomerNotes(entity, modifiedBy);
		
		String customerAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			customerAuditMsg = "Customer created";
		} else {
			customerAuditMsg = "Customer updated";
		}
		
		try {
			genericDAO.saveOrUpdate(entity);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("msgCtx", "manageCustomer");
			model.addAttribute("error", errorMsg);
			
			setupCreate(model, request);
			
			model.addAttribute("modelObject", entity);
			model.addAttribute("activeTab", "manageCustomer");
			model.addAttribute("activeSubTab", "billing");
			model.addAttribute("mode", "ADD");
			
			setupEmptyCreate(model);
			
			return urlContext + "/customer";
		}
		
		createAuditCustomerNotes(entity, customerAuditMsg, modifiedBy);
		
		cleanUp(request);
		
		return saveSuccess(model, request, entity);
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "company")) {
			errorMsg = "Duplicate company name - company name already exists"; 
		} else {
			errorMsg = "Error occured while saving Custome";
		}
		
		return errorMsg;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveCustomerNotes.do")
	public String saveCustomerNotes(HttpServletRequest request,
			@ModelAttribute("notesModelObject") CustomerNotes entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		if (entity.getCustomer().getId() == null) {
			setupEmptyCreate(model);
			
			model.addAttribute("activeTab", "manageCustomer");
			model.addAttribute("activeSubTab", "customerNotesTab");
			model.addAttribute("mode", "ADD");
			
			model.addAttribute("errorCtx", "manageCustomerNotes");
			model.addAttribute("error", "Please save valid Customer details first before saving Notes.");
			
			return urlContext + "/customer";
		}

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
		
		entity.setNotesType(CustomerNotes.NOTES_TYPE_USER);
		updateEnteredBy(entity);
		
		genericDAO.saveOrUpdate(entity);
		
		cleanUp(request);
		
		Long customerId = entity.getCustomer().getId();
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId + " and obj.deleteFlag='1'");
		Customer savedCustomer = customerList.get(0);
		model.addAttribute("modelObject", savedCustomer);

		setupCreate(model, request);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "customerNotesTab");
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer);
		model.addAttribute("notesModelObject", notes);
	
		List<CustomerNotes> notesList = genericDAO.executeSimpleQuery("select obj from CustomerNotes obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		Customer customer = new Customer();
		customer.setId(customerId);
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<DeliveryAddress> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.line1 asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		populateAggregartionValues(model, customerId);
		
		return urlContext + "/customer";
	}

	private void setupEmptyCreate(ModelMap model) {
		Customer emptyCustomer = new Customer();
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer);
		model.addAttribute("notesModelObject", notes);

		List<CustomerNotes> notesList = new ArrayList<>();
		model.addAttribute("notesList", notesList);
		
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(emptyCustomer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<DeliveryAddress> addressList = new ArrayList<>();
		model.addAttribute("deliveryAddressList", addressList);
	}
	
	private void updateEnteredBy(CustomerNotes entity) {
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getEmployee().getFullName());
	}
	
	private void setupCustomerNotes(Customer customer, Long modifiedBy) {
		List<CustomerNotes> customerNotesList = customer.getCustomerNotes();
		if (customerNotesList == null) {
			customerNotesList = new ArrayList<CustomerNotes>();
			customer.setCustomerNotes(customerNotesList);
		}
		
		if (!customerNotesList.isEmpty()) {
			CustomerNotes lastNotes = customerNotesList.get(customerNotesList.size() - 1);
			String notesStr = lastNotes.getNotes();
			if (StringUtils.isEmpty(notesStr)) {
				customerNotesList.remove(customerNotesList.size() - 1);
			} else if (lastNotes.getCreatedBy() == null) {
				lastNotes.setNotesType(CustomerNotes.NOTES_TYPE_USER);
				lastNotes.setCustomer(customer);
				lastNotes.setCreatedAt(Calendar.getInstance().getTime());
				lastNotes.setCreatedBy(modifiedBy);
				updateEnteredBy(lastNotes);
			}
		}
	}
	
	private CustomerNotes createAuditCustomerNotes(Customer customer, String customerAuditMsg, Long createdBy) {
		CustomerNotes auditCustomerNotes = new CustomerNotes();
		auditCustomerNotes.setNotesType(CustomerNotes.NOTES_TYPE_AUDIT);
		auditCustomerNotes.setNotes("***AUDIT: " + customerAuditMsg + "***");
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customer.getId());
		auditCustomerNotes.setCustomer(emptyCustomer);
		
		auditCustomerNotes.setCreatedAt(Calendar.getInstance().getTime());
		auditCustomerNotes.setCreatedBy(createdBy);
		updateEnteredBy(auditCustomerNotes);
		
		genericDAO.save(auditCustomerNotes);
		
		if (customer.getCustomerNotes() != null) {
			customer.getCustomerNotes().add(auditCustomerNotes);
		}
		
		return auditCustomerNotes;
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
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		if (entity.getCustomer().getId() == null) {
			setupEmptyCreate(model);
			
			model.addAttribute("activeTab", "manageCustomer");
			model.addAttribute("activeSubTab", "delivery");
			model.addAttribute("mode", "ADD");
			
			model.addAttribute("errorCtx", "manageCustomerDeliveryAddress");
			model.addAttribute("error", "Please save valid Customer details first before saving Delivery address.");
			
			return urlContext + "/customer";
		}
		
		Long customerId = entity.getCustomer().getId();
		
		if (isDuplicate(entity)) {
			model.addAttribute("errorCtx", "manageCustomerDeliveryAddress");
			model.addAttribute("error", "Duplicate Delivery address.");
			
			saveDeliveryAddressSuccess(model, request, customerId);
			model.addAttribute("deliveryAddressModelObject", entity);
			
			return urlContext + "/customer";
		}
		
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
		
		if (entity.getId() != null) {
			List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.id=" + entity.getId() + " and obj.deleteFlag='1'");
			DeliveryAddress existingDeliveryAddress = deliveryAddressList.get(0);
			entity.setCreatedAt(existingDeliveryAddress.getCreatedAt());
			entity.setCreatedBy(existingDeliveryAddress.getCreatedBy());
		}
		
		String customerAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			customerAuditMsg = "Delivery address created";
		} else {
			customerAuditMsg = "Delivery address updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = getUser(request).getId();
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		createAuditCustomerNotes(emptyCustomer, customerAuditMsg, modifiedBy);
		
		model.addAttribute("msgCtx", "manageCustomerDeliveryAddress");
		model.addAttribute("msg", "Delivery address saved successfully");
		
		saveDeliveryAddressSuccess(model, request, customerId);
		
		return urlContext + "/customer";
	}
	
	private void saveDeliveryAddressSuccess(ModelMap model, HttpServletRequest request, Long customerId) {
		cleanUp(request);
		
		setupCreate(model, request);
		
		Customer emptyCustomer2 = new Customer();
		emptyCustomer2.setId(customerId);
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer2);
		model.addAttribute("notesModelObject", notes);
	
		List<CustomerNotes> notesList = genericDAO.executeSimpleQuery("select obj from CustomerNotes obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		Customer customer = new Customer();
		customer.setId(customerId);
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId + " and obj.deleteFlag='1'");
		model.addAttribute("modelObject", customerList.get(0));
		
		List<DeliveryAddress> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.line1 asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		populateAggregartionValues(model, customerId);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "delivery");
		model.addAttribute("mode", "ADD");
	}
	
	private boolean isDuplicate(DeliveryAddress deliveryAddressToBeChecked) {
		String deliveryAddressToBeCheckedStr = deliveryAddressToBeChecked.getFullDeliveryAddress();
		
		Long customerId = deliveryAddressToBeChecked.getCustomer().getId();
		Customer customer = genericDAO.getById(Customer.class, customerId);
		List<DeliveryAddress> deliveryAddressList = customer.getDeliveryAddress();
		
		boolean dup = false;
		for (DeliveryAddress anExistingDeliveryAddress : deliveryAddressList) {
			if (StringUtils.contains(anExistingDeliveryAddress.getFullDeliveryAddress(), deliveryAddressToBeCheckedStr)) {
				if (deliveryAddressToBeChecked.getId() == null 
						|| deliveryAddressToBeChecked.getId().longValue() != anExistingDeliveryAddress.getId().longValue()) {
					dup = true;
					break;
				} 
			}
		}
		
		return dup;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteDeliveryAddress.do")
	public String deleteDeliveryAddress(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "deliveryAddressId") Long deliveryAddressId) {
		DeliveryAddress deliveryAddressEntity = genericDAO.getById(DeliveryAddress.class, deliveryAddressId);
		
		String orderQuery = "select obj from Order obj where obj.deliveryAddress.id=" 
					+ deliveryAddressEntity.getId();
		List<Order> orderList = genericDAO.executeSimpleQuery(orderQuery);
		if (!orderList.isEmpty()) {
			cleanUp(request);
			
			setupDeliveryAddress(model, request, deliveryAddressEntity);

			model.addAttribute("errorCtx", "manageCustomerDeliveryAddress");
			model.addAttribute("error", "Delivery address to be deleted is associated to Order(s)");
		
			return urlContext + "/customer";
		}
		
		Long customerId = deliveryAddressEntity.getCustomer().getId();
		Customer customer = genericDAO.getById(Customer.class, customerId);
		List<DeliveryAddress> deliveryAddressList = customer.getDeliveryAddress();
		int i = 0;
		for (DeliveryAddress aDeliveryAddress : deliveryAddressList) {
			if (aDeliveryAddress.getId() == deliveryAddressEntity.getId()) {
				break;
			}
			i++;
		}
		deliveryAddressList.remove(i);
		
		Long modifiedBy = getUser(request).getId();
		customer.setModifiedBy(modifiedBy);
		customer.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(customer);
		
		genericDAO.delete(deliveryAddressEntity);
		
		String customerAuditMsg = "Delivery address '" + deliveryAddressEntity.getFullLine() + "' deleted";
		createAuditCustomerNotes(customer, customerAuditMsg, modifiedBy);
		
		cleanUp(request);
		
		setupDeliveryAddress(model, request, deliveryAddressEntity);

		model.addAttribute("msgCtx", "manageCustomerDeliveryAddress");
		model.addAttribute("msg", "Delivery address deleted successfully");
		
		return urlContext + "/customer";
	}
	
	private void setupDeliveryAddress(ModelMap model, HttpServletRequest request,
			DeliveryAddress deliveryAddressEntity) {
		setupCreate(model, request);
		
		Long customerId = deliveryAddressEntity.getCustomer().getId();
		
		Customer emptyCustomer2 = new Customer();
		emptyCustomer2.setId(customerId);
		CustomerNotes notes = new CustomerNotes();
		notes.setCustomer(emptyCustomer2);
		model.addAttribute("notesModelObject", notes);
	
		List<CustomerNotes> notesList = genericDAO.executeSimpleQuery("select obj from CustomerNotes obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		Customer customer = new Customer();
		customer.setId(customerId);
		DeliveryAddress address = new DeliveryAddress();
		address.setCustomer(customer);
		model.addAttribute("deliveryAddressModelObject", address);
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId + " and obj.deleteFlag='1'");
		model.addAttribute("modelObject", customerList.get(0));
		
		List<DeliveryAddress> addressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" +  customerId + " and obj.deleteFlag='1' order by obj.line1 asc");
		model.addAttribute("deliveryAddressList", addressList);
		
		populateAggregartionValues(model, customerId);
		
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("activeSubTab", "delivery");
		model.addAttribute("mode", "ADD");
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
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		if (isDuplicate(entity)) {
			return "ErrorMsg: Duplicate Delivery address";
		}
		
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
		
		String customerAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			customerAuditMsg = "Delivery address created";
		} else {
			customerAuditMsg = "Delivery address updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = getUser(request).getId();
		Long customerId = entity.getCustomer().getId();
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		createAuditCustomerNotes(emptyCustomer, customerAuditMsg, modifiedBy);
		
		cleanUp(request);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
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
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
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
		entity.getDeliveryAddress().get(0).setCreatedAt(entity.getCreatedAt());
		
		Long modifiedBy = getUser(request).getId();
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupCustomerNotes(entity, modifiedBy);
		
		String customerAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			customerAuditMsg = "Customer created";
		} else {
			customerAuditMsg = "Customer updated";
		}
		
		try {
			genericDAO.saveOrUpdate(entity);
		} catch (PersistenceException e){
			String errorMsg = "ErrorMsg: " + extractSaveErrorMsg(e);
			return errorMsg;
		}
		
		createAuditCustomerNotes(entity, customerAuditMsg, modifiedBy);
		
		cleanUp(request);
		
		//Or retrieve cust again?
		List<State> stateList = genericDAO.executeSimpleQuery("select obj from State obj where obj.id= " + entity.getState().getId() + " and obj.deleteFlag='1' order by obj.name");
		entity.getState().setName(stateList.get(0).getName());
		
		return constructResponseJson(entity);
	}
	
	private String constructResponseJson(Customer entity) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
	}
}
