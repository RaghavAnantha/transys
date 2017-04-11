package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;

import com.transys.controller.editor.AbstractModelEditor;

import com.transys.core.report.generator.ExcelReportGenerator;

import com.transys.model.AbstractBaseModel;
import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.LocationType;
import com.transys.model.OrderFees;
import com.transys.model.OrderPermits;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.PermitAddress;
import com.transys.model.PermitClass;
import com.transys.model.PermitFee;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/permit")
public class PermitController extends CRUDController<Permit> {
	public static final int MAX_NUMBER_OF_ASSOCIATED_PERMITS = 3;
	
	public PermitController(){
		setUrlContext("permit");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(LocationType.class, new AbstractModelEditor(LocationType.class));
		binder.registerCustomEditor(PermitClass.class, new AbstractModelEditor(PermitClass.class));
		binder.registerCustomEditor(PermitType.class, new AbstractModelEditor(PermitType.class));
		binder.registerCustomEditor(DeliveryAddress.class, new AbstractModelEditor(DeliveryAddress.class));
		binder.registerCustomEditor(PermitNotes.class, new AbstractModelEditor(PermitNotes.class));
		binder.registerCustomEditor(PermitAddress.class, new AbstractModelEditor(PermitAddress.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		return list(model, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/notes.do")
	public String displayNotes(ModelMap model, HttpServletRequest request) {
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		//TODO: Fix me 
		criteria.getSearchMap().remove("_csrf");
		
		if (!injectOrderSearchCriteria(criteria)) {
			// search yielded no results
			model.addAttribute("list", new ArrayList<Permit>());
			
			model.addAttribute("activeTab", "managePermits");
			model.addAttribute("mode", "MANAGE");
			
			return urlContext + "/permit";
		}
		
		List<Permit> listOfPermits = performSearch(criteria);
		
		model.addAttribute("list", listOfPermits);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "MANAGE");
		
		cleanUp(request);
		
		return urlContext + "/permit";
	}
	
	private List<Permit> performSearch(SearchCriteria criteria) {
		List<Permit> listOfPermits = genericDAO.search(getEntityClass(), criteria, "id", true, null);
		
		for (Permit p : listOfPermits) {
			List<?> objectList = genericDAO.executeSimpleQuery("select obj.order.id from OrderPermits obj where obj.deleteFlag='1' and obj.permit.id=" +  p.getId() + " order by obj.id desc");
			if (!objectList.isEmpty()) {
				Long orderId = (Long)objectList.get(0);
				p.setOrderId(orderId);
				
				String associatedOrderIds = StringUtils.EMPTY;
				for (int i = 0; i < objectList.size(); i++) {
					associatedOrderIds += ((Long)objectList.get(i) + ", ");
				}
				associatedOrderIds = associatedOrderIds.substring(0, associatedOrderIds.length()-2);
				p.setAssociatedOrderIds(associatedOrderIds);
			}
		}
		
		return listOfPermits;
	}

	/*private void injectPendingPaymentPermitSearch(SearchCriteria criteria) {
		if (criteria != null && criteria.getSearchMap() != null) {
			Map<String, Object> searchMap = criteria.getSearchMap();
			Object[] param = searchMap.keySet().toArray();
			for (int i = 0; i < param.length; i++) {
				String key = param[i].toString();
				if(key.toUpperCase().contains("NUMBER") && searchMap.get(key).toString().equalsIgnoreCase(Permit.EMPTY_PERMIT_NUMBER) ) {
					System.out.println("Key = " + key + " : " + searchMap.get(key).toString());
					searchMap.put("number", "null");
				}
			}
			criteria.setSearchMap(searchMap);
		}
	}*/

	private boolean injectOrderSearchCriteria(SearchCriteria criteria) {
		if (criteria != null && criteria.getSearchMap() != null) {
			Map<String, Object> searchMap = criteria.getSearchMap();
			Object[] param = searchMap.keySet().toArray();
			
			for (int i = 0; i < param.length; i++) {
				String key = param[i].toString();
				if(key.toUpperCase().contains("EXCLUDE.ORDER.ID") && searchMap.get(key).toString().length() > 0 ) {
					// execute statement and get related permits
					List<OrderPermits> orderPermits = getOrderRelatedPermits(searchMap, param, i);
					String permitIDSearchCriteria = constructPermitSearchCriteria(orderPermits, searchMap, param, i);
					
					if (permitIDSearchCriteria == null) {
						System.out.println("Permits for searched Order is not available.");
						return false;
					}
					searchMap.put("id", permitIDSearchCriteria);
				}
			}
			criteria.setSearchMap(searchMap);
		}
		
		return true;
	}

	private String constructPermitSearchCriteria(List<OrderPermits> orderPermits, Map<String, Object> searchMap,
			Object[] param, int i) {

		if (orderPermits == null || orderPermits.size() == 0) {
			return null;
		}
		
		StringBuffer permitIDSearchCriteria = new StringBuffer();
		for(OrderPermits o : orderPermits) {
			permitIDSearchCriteria.append(o.getPermit().getId() + ",");
		}
		
		System.out.println("Found permits for order = " + permitIDSearchCriteria);
		//searchMap.remove(param[i].toString());
		return "$INCLUDE_COMMA$," + permitIDSearchCriteria.substring(0, permitIDSearchCriteria.lastIndexOf(",")).toString();
	}

	private List<OrderPermits> getOrderRelatedPermits(Map<String, Object> searchMap, Object[] param, int i) {
		SearchCriteria innerSearch = new SearchCriteria();
		Map<String, Object> innerSearchCriteria = new HashMap<String, Object>();
		innerSearchCriteria.put("order", searchMap.get(param[i].toString()));
		innerSearch.setSearchMap(innerSearchCriteria);
		List<OrderPermits> orderPermits = genericDAO.search(OrderPermits.class, innerSearch, "id", null, null);
		return orderPermits;
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "permitDetails");
		
		model.addAttribute("notesModelObject", new PermitNotes());
		model.addAttribute("permitAddressModelObject", new PermitAddress());
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createForCustomerModal.do")
	public String createForCustomerModal(ModelMap model, HttpServletRequest request, 
			@RequestParam(value = "customerId") Long customerId,
			@RequestParam(value = "deliveryAddressId") Long deliveryAddressId,
			@RequestParam(value = "locationTypeId") Long locationTypeId,
			@RequestParam(value = "permitClassId") Long permitClassId,
			@RequestParam(value = "permitTypeId") Long permitTypeId,
			@RequestParam(value = "deliveryDate") Date deliveryDate) {
		String customerQuery = "select obj from Customer obj where obj.deleteFlag='1' and obj.id=" + customerId;
		List<Customer> customerList = genericDAO.executeSimpleQuery(customerQuery);
		model.put("customer", customerList);
		
		String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id=" + deliveryAddressId + " order by line1 asc";
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		model.addAttribute("deliveryAddress", deliveryAddressList);

		// For new permit, first permit address will be the delivery address
		//model.addAttribute("permitAddress", deliveryAddressList);
		
		String locationTypesQuery = "select obj from LocationType obj where obj.deleteFlag='1' and obj.id=" + locationTypeId + "order by locationType asc";
		List<LocationType> locationTypeList = genericDAO.executeSimpleQuery(locationTypesQuery);
		model.addAttribute("locationType", locationTypeList);
		
		String permitClassQuery = "select obj from PermitClass obj where obj.deleteFlag='1' and obj.id=" + permitClassId;
		List<PermitClass> permitClassList = genericDAO.executeSimpleQuery(permitClassQuery);
		model.addAttribute("permitClass", permitClassList);
		
		String permitTypeQuery = "select obj from PermitType obj where obj.deleteFlag='1' and obj.id=" + permitTypeId;
		List<PermitType> permitTypeList = genericDAO.executeSimpleQuery(permitTypeQuery);
		model.addAttribute("permitType", permitTypeList);
		
		Permit emptyPermit = new Permit();
		emptyPermit.setStartDate(deliveryDate);
		Date endDate = calculatePermitEndDate(permitTypeId, deliveryDate);
		emptyPermit.setEndDate(endDate);
		
		String permitFeeStr = getPermitFee(model, request, permitTypeId, permitClassId, deliveryDate);
		if (!StringUtils.isEmpty(permitFeeStr)) {
			emptyPermit.setFee(new BigDecimal(permitFeeStr));
		}
		
		DeliveryAddress aDeliveryAddress = deliveryAddressList.get(0);
		PermitAddress aPermitAddress = new PermitAddress();
		aPermitAddress.setPermit(emptyPermit);
		map(aPermitAddress, aDeliveryAddress);
		
		List<PermitAddress> permitAddressList = new ArrayList<PermitAddress>();
		permitAddressList.add(aPermitAddress);
		emptyPermit.setPermitAddress(permitAddressList);
		
		model.put("modelObject", emptyPermit);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		return urlContext + "/formForCustomerModal";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/editForCustomerModal.do")
	public String editForCustomerModal(ModelMap model, HttpServletRequest request, 
			@RequestParam(value = "permitId") Long permitId) {
		Permit permit = genericDAO.getById(Permit.class, permitId);
		
		String customerQuery = "select obj from Customer obj where obj.deleteFlag='1' and obj.id=" + permit.getCustomer().getId();
		List<Customer> customerList = genericDAO.executeSimpleQuery(customerQuery);
		model.put("customer", customerList);
		
		String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id=" + permit.getDeliveryAddress().getId();
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		model.addAttribute("deliveryAddress", deliveryAddressList);

		String locationTypesQuery = "select obj from LocationType obj where obj.deleteFlag='1' and obj.id=" + permit.getLocationType().getId();
		List<LocationType> locationTypeList = genericDAO.executeSimpleQuery(locationTypesQuery);
		model.addAttribute("locationType", locationTypeList);
		
		String permitClassQuery = "select obj from PermitClass obj where obj.deleteFlag='1' and obj.id=" + permit.getPermitClass().getId();
		List<PermitClass> permitClassList = genericDAO.executeSimpleQuery(permitClassQuery);
		model.addAttribute("permitClass", permitClassList);
		
		String permitTypeQuery = "select obj from PermitType obj where obj.deleteFlag='1' and obj.id=" + permit.getPermitType().getId();
		List<PermitType> permitTypeList = genericDAO.executeSimpleQuery(permitTypeQuery);
		model.addAttribute("permitType", permitTypeList);
	
		model.put("modelObject", permit);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		return urlContext + "/formForCustomerModal";
	}
	
	private void map(PermitAddress aPermitAddress, DeliveryAddress aDeliveryAddress) {
		aPermitAddress.setLine1(aDeliveryAddress.getLine1());
		aPermitAddress.setLine2(aDeliveryAddress.getLine2());
		aPermitAddress.setCity(aDeliveryAddress.getCity());
		aPermitAddress.setState(aDeliveryAddress.getState());
		aPermitAddress.setZipcode(aDeliveryAddress.getZipcode());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createModal.do")
	public String createModal(ModelMap model, HttpServletRequest request, 
			@RequestParam(value = "id") Long orderPermitId)  {
		setupUpdate(model, request);
		System.out.println("OrderPermit Id being edited = " + orderPermitId);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.put("id", orderPermitId);
		OrderPermits orderPermitToBeEdited = genericDAO.findByCriteria(OrderPermits.class, criterias, "id", false).get(0);
		
		Permit permitToBeEdited = orderPermitToBeEdited.getPermit();
		
		criterias.clear();
		criterias.put("permit", permitToBeEdited.getId());
		model.addAttribute("permitAddress", genericDAO.findByCriteria(PermitAddress.class, criterias, "id", false));
		
		permitToBeEdited.setNumber(StringUtils.EMPTY); // Empty the permit number
		permitToBeEdited.getPermitNotes().clear();
		
		model.put("modelObject", permitToBeEdited);
		
		// dont do validation here
		/*if (!validateMaxPermitsAllowable(orderPermitToBeEdited)) {
			model.addAttribute("error", "There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " permits for this order.");
		}*/
		
		List<Customer> customers = new ArrayList<>();
		customers.add(permitToBeEdited.getCustomer());
		model.put("customer", customers);
		
		List<DeliveryAddress> deliveryAddress = new ArrayList<>();
		deliveryAddress.add(permitToBeEdited.getDeliveryAddress());
		model.addAttribute("deliveryAddress", deliveryAddress);
		
		List<LocationType> locationType = new ArrayList<>();
		locationType.add(permitToBeEdited.getLocationType());
		model.addAttribute("locationType", locationType);
		
		List<PermitClass> permitClass = new ArrayList<>();
		permitClass.add(permitToBeEdited.getPermitClass());
		model.addAttribute("permitClass", permitClass);

		/*List<PermitType> permitType = new ArrayList<>();
		permitType.add(permitToBeEdited.getPermitType());*/
		List<PermitType> permitType = genericDAO.findAll(PermitType.class, true);
		model.addAttribute("permitType", permitType);
		
		model.addAttribute("associatedOrderID", orderPermitToBeEdited);
		
		return urlContext + "/formModal";
	}

	private boolean validateMaxPermitsAllowable(OrderPermits orderPermit) {
		// Validate if this order has < 3 permits, else show alert
		List<OrderPermits> orderPermitsForThisOrder = genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.deleteFlag='1' and obj.order.id=" +  orderPermit.getOrder().getId());
		if (orderPermitsForThisOrder != null && orderPermitsForThisOrder.size() >= MAX_NUMBER_OF_ASSOCIATED_PERMITS) {
			// Do not create modal, show alert
			return false;
		}
		
		return true;
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "permitDetails");

		Permit permitToBeEdited = (Permit)model.get("modelObject");
		
		if (permitToBeEdited == null) { // OrderPermitAlert Screen - Add New Permit, should do a new add, not an update - clear out the id field?
			OrderPermits orderPermitToBeEdited = setupOrderPermitModel(request);
			permitToBeEdited = orderPermitToBeEdited.getPermit();
			System.out.println("Got the new permit number " + permitToBeEdited.getNumber());
			model.put("modelObject", permitToBeEdited);
		} 
		
		List<OrderPermits> orderPermits = genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.deleteFlag='1' and obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			OrderPermits orderPermitObj = orderPermits.get(0);
			model.addAttribute("associatedOrderID", orderPermitObj);
			
			// TODO: all fields should become non-editable except unrelated fields like parking meter fee / permit number
			List<Customer> customers = new ArrayList<>();
			customers.add(permitToBeEdited.getCustomer());
			model.put("customer", customers);
			
			List<DeliveryAddress> deliveryAddress = new ArrayList<>();
			deliveryAddress.add(permitToBeEdited.getDeliveryAddress());
			model.addAttribute("editDeliveryAddress", deliveryAddress);
			System.out.println("Delivery address = " + deliveryAddress.get(0).getFullDeliveryAddress(","));
			
			List<LocationType> locationType = new ArrayList<>();
			locationType.add(permitToBeEdited.getLocationType());
			model.addAttribute("locationType", locationType);
			
			List<PermitClass> permitClass = new ArrayList<>();
			permitClass.add(permitToBeEdited.getPermitClass());
			model.addAttribute("permitClass", permitClass);

			List<PermitType> permitType = new ArrayList<>();
			permitType.add(permitToBeEdited.getPermitType());
			model.addAttribute("permitType", permitType);
			
			/*Map filter = new HashMap<String, Object>();
			filter.put("permit", permitToBeEdited);
			model.addAttribute("permitAddressList", genericDAO.findByCriteria(PermitAddress.class, filter, "id", false));*/
		} else {
			// get the delivery address
			model.addAttribute("editDeliveryAddress", permitToBeEdited.getCustomer().getDeliveryAddress());
		}
		
		PermitNotes notes = new PermitNotes();
		notes.setPermit(permitToBeEdited); 
		model.addAttribute("notesModelObject", notes);
		List<PermitNotes> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.deleteFlag='1' and obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
	
		PermitAddress permitAddress = new PermitAddress();
		permitAddress.setPermit(permitToBeEdited); 
		model.addAttribute("permitAddressModelObject", permitAddress);
		List<PermitAddress> permitAddressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.deleteFlag='1' and obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("permitAddressList", permitAddressList);

		return urlContext + "/permit";
	}
	
	public OrderPermits setupOrderPermitModel(HttpServletRequest request) {
		String pk = request.getParameter(getPkParam());
		if (pk == null || StringUtils.isEmpty(pk)) {
			return new OrderPermits();
		} else {
			return genericDAO.getById(OrderPermits.class, Long.parseLong(pk));
		}
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		String deliveryAddresseQuery = "select distinct obj.line1 from DeliveryAddress obj where obj.deleteFlag='1' and obj.line1 != '' order by obj.line1 asc";
		model.addAttribute("deliveryAddressesLine1", genericDAO.executeSimpleQuery(deliveryAddresseQuery));
		
	   deliveryAddresseQuery = "select distinct obj.line2 from DeliveryAddress obj where obj.deleteFlag='1' and obj.line2 != '' order by obj.line2 asc";
		model.addAttribute("deliveryAddressesLine2", genericDAO.executeSimpleQuery(deliveryAddresseQuery));
		 
	   List<?> objectList = genericDAO.executeSimpleQuery("select obj.line1, obj.line2 from PermitAddress obj where obj.deleteFlag='1' order by obj.line1 asc");
		
		SortedSet<String> permitAddressLine1Set = new TreeSet<String>();
		SortedSet<String> permitAddressLine2Set = new TreeSet<String>();
		for (int i = 0; i < objectList.size(); i++) {
			Object anObject[] = (Object[])objectList.get(i);
			
			if (anObject[0] != null && StringUtils.isNotEmpty(anObject[0].toString())) {
				permitAddressLine1Set.add(anObject[0].toString());
			}
			if (anObject[1] != null && StringUtils.isNotEmpty(anObject[1].toString())) {
				permitAddressLine2Set.add(anObject[1].toString());
			}
		}
		
		String[] permitAddressLine1Arr = permitAddressLine1Set.toArray(new String[0]);
		String[] permitAddressLine2Arr = permitAddressLine2Set.toArray(new String[0]);
		
		model.addAttribute("allPermitAddressesLine1", permitAddressLine1Arr);
		model.addAttribute("allPermitAddressesLine2", permitAddressLine2Arr);
	   
	   List<Customer> customerList = genericDAO.findByCriteria(Customer.class, criterias, "companyName", false);
		model.addAttribute("customer", customerList);
		
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "locationType", false));
		
		//model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.executeSimpleQuery("select obj.id from Order obj where obj.deleteFlag='1' order by obj.id asc"));
		
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "number", false));
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (Customer aCustomer : customerList) {
			phoneSet.add(aCustomer.getPhone());
			contactNameSet.add(aCustomer.getContactName());
		}
		
		model.addAttribute("phone", phoneSet);	
		model.addAttribute("contactName", contactNameSet);
	}
	
	private boolean shouldPermitStatusBeUpdated(Permit entity, String newStatus) {
		if (entity.getId() == null || entity.getStatus() == null) {
			return true;
		}
		
		if (StringUtils.equals(newStatus, entity.getStatus().getStatus())) {
			return false;
		}
		
		return !StringUtils.equals(PermitStatus.PERMIT_STATUS_ASSIGNED, entity.getStatus().getStatus());
	}
	
	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			System.out.println("Error in validation " + e);
			log.warn("Error in validation :" + e);
		}
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError e : errors) {
				System.out.println("Error: " + e.getDefaultMessage());
			}
			
			setupCreate(model, request);
			return urlContext + "/permit";
		}
			
		if (entity.getFee() == null) {
			return showPermitCreatePageWithError(request, entity, model, "Permit Fee for the required class/type for the given date range = " + entity.getFormattedStartDate() + " to " + entity.getFormattedEndDate() + " is not available.");
		} 
		
		String status = PermitStatus.PERMIT_STATUS_PENDING;
		if (isEmptyPermitNumber(entity)) {
			entity.setNumber(Permit.EMPTY_PERMIT_NUMBER);
		} else {
			if (!isPermitNumberUnique(entity.getId(), entity.getNumber())) { 
				return showPermitCreatePageWithError(request, entity, model, "Permit Number " + entity.getNumber() + " already exists.");
			}
			status = PermitStatus.PERMIT_STATUS_AVAILABLE;
		}
		
		if (shouldPermitStatusBeUpdated(entity, status)) {
			PermitStatus permitStatus = retrievePermitStatus(status);
			entity.setStatus(permitStatus);
		}
		
		beforeSave(request, entity, model);
		
		Long modifiedBy = getUser(request).getId();
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupPermitNotes(entity, modifiedBy);
		
		String permitAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			permitAuditMsg = "Permit created";
		} else {
			permitAuditMsg = "Permit updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		createAuditPermitNotes(entity, permitAuditMsg, modifiedBy);
		
		// The delivery address entered will automatically be stored as one of the Permit Addresses. Users can add more.
		addDeliveryAddAsPermitAdd(request, entity);
		
		cleanUp(request);
		
		return saveSuccess(model, request, entity);
	}
	
	private PermitNotes createAuditPermitNotes(Permit permit, String permitAuditMsg, Long createdBy) {
		PermitNotes auditPermitNotes = new PermitNotes();
		auditPermitNotes.setNotesType(PermitNotes.NOTES_TYPE_AUDIT);
		auditPermitNotes.setNotes("***AUDIT: " + permitAuditMsg + "***");
		
		Permit emptyPermit = new Permit();
		emptyPermit.setId(permit.getId());
		auditPermitNotes.setPermit(emptyPermit);
		
		auditPermitNotes.setCreatedAt(Calendar.getInstance().getTime());
		auditPermitNotes.setCreatedBy(createdBy);
		setEnteredBy(auditPermitNotes);
		
		genericDAO.save(auditPermitNotes);
		
		if (permit.getPermitNotes() != null) {
			permit.getPermitNotes().add(auditPermitNotes);
		}
		
		return auditPermitNotes;
	}

	private String showPermitCreatePageWithError(HttpServletRequest request, Permit entity, ModelMap model, String errorMessage) {
		setupCreate(model, request);
		
		model.addAttribute("msgCtx", "managePermit");
		model.addAttribute("error", errorMessage);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitDetails"); // Permit Address?
		model.addAttribute("mode", "ADD");
		
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id=" + entity.getDeliveryAddress().getId());
		System.out.println("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id=" + entity.getDeliveryAddress().getId());
		model.addAttribute("editDeliveryAddress", deliveryAddressList);
		
		List<LocationType> locationTypeList = genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.deleteFlag='1' and obj.id=" + entity.getLocationType().getId());
		System.out.println("select obj from LocationType obj where obj.deleteFlag='1' and obj.id=" + entity.getLocationType().getId());
		model.addAttribute("locationType", locationTypeList);
		
		PermitNotes notes = new PermitNotes();
		notes.setPermit(entity);
		model.addAttribute("notesModelObject", notes);
		List<PermitNotes> notesList = new ArrayList<>(); 
		model.addAttribute("notesList", notesList);
		
		PermitAddress permitAddress = new PermitAddress();
		permitAddress.setPermit(entity);
		model.addAttribute("permitAddressModelObject", permitAddress);
		List<PermitAddress> permitAddressList =  new ArrayList<>();
		model.addAttribute("permitAddressList", permitAddressList);
		
		return urlContext + "/permit";
	}

	private boolean isPermitNumberUnique(Long id, String number) {
		String query = "select obj from Permit obj where "
				+ " obj.number='" + number + "'";
		if (id != null) {
			query += " and obj.id !=" + id;
		}
 		List<Permit> listOfPermits = genericDAO.executeSimpleQuery(query);
 		return listOfPermits.isEmpty();
	}

	/*private void setupPermitNotes(Permit permit) {
		List<PermitNotes> permitNotesList = permit.getPermitNotes();
		if (permitNotesList == null || permitNotesList.isEmpty()) {
			return;
		}
		
		PermitNotes aPermitNotes = permitNotesList.get(0);
		if (StringUtils.isEmpty(aPermitNotes.getNotes())) {
			permitNotesList.clear();
			return;
		}
		
		aPermitNotes.setPermit(permit);
		
		Long createdBy = null;
		if (permit.getId() == null) {
			createdBy = permit.getCreatedBy();
		} else {
			createdBy = permit.getModifiedBy();
		}
		
		aPermitNotes.setCreatedBy(createdBy);
		aPermitNotes.setCreatedAt(Calendar.getInstance().getTime());
		
		updateEnteredBy(aPermitNotes);
		
		//aPermitNotes.setModifiedBy(order.getModifiedBy());
		//aPermitNotes.setModifiedAt(order.getModifiedAt());
	}*/
	
	private void setupPermitNotes(Permit permit, Long modifiedBy) {
		List<PermitNotes> permitNotesList = permit.getPermitNotes();
		if (permitNotesList == null) {
			permitNotesList = new ArrayList<PermitNotes>();
			permit.setPermitNotes(permitNotesList);
		}
		
		if (!permitNotesList.isEmpty()) {
			PermitNotes lastNotes = permitNotesList.get(permitNotesList.size() - 1);
			String notesStr = lastNotes.getNotes();
			if (StringUtils.isEmpty(notesStr)) {
				permitNotesList.remove(permitNotesList.size() - 1);
			} else if (lastNotes.getCreatedBy() == null) {
				lastNotes.setNotesType(PermitNotes.NOTES_TYPE_USER);
				lastNotes.setPermit(permit);
				lastNotes.setCreatedAt(Calendar.getInstance().getTime());
				lastNotes.setCreatedBy(modifiedBy);
				setEnteredBy(lastNotes);
			}
		}
	}
	
	private void setEnteredBy(PermitNotes entity) {
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getEmployee().getFullName());
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePermitFromAlert.do")
	public @ResponseBody String savePermitFromAlert(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {
		if (!validateParkingMeterFee(entity)) {
			System.out.println("Please correct following invalid data: Parking Meter Fee");
			return "ErrorMsg: Please correct following invalid data: Parking Meter Fee";
		}
			
		OrderPermits associatedOrderPermitEntry = null;
		
		/*try {
			associatedOrderPermitEntry = validatePermitEndDate(entity);
		} catch (Exception ex) {
			System.out.println("Exception while validating permit end date = " + ex.getMessage());
			return ex.getMessage();
		}*/
		
		List<OrderPermits> orderPermits = genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.deleteFlag='1' and obj.id=" +  entity.getOrderId()+ " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			associatedOrderPermitEntry = (OrderPermits) orderPermits.get(0);
		}
		
		if (!validateMaxPermitsAllowable(associatedOrderPermitEntry)) {
			System.out.println("There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " permits for this order.");
			return "ErrorMsg: There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " permits for this order.";
		}
		
		// Unique permit number check
		if(isEmptyPermitNumber(entity)) {
			entity.setNumber(Permit.EMPTY_PERMIT_NUMBER);
		} else {
			if (!isPermitNumberUnique(entity.getId(), entity.getNumber())) {  // Always a new permit, so this validation mandatory
				return "ErrorMsg: Permit Number " + entity.getNumber() + " already exists.";
			}
		}

		String status = PermitStatus.PERMIT_STATUS_ASSIGNED;
		PermitStatus permitStatus = retrievePermitStatus(status);
		entity.setStatus(permitStatus);
		
		System.out.println("Permits parking meter = " + entity.getParkingMeter());
		//TODO: REMOVE THIS HACK
		if (entity.getParkingMeter().startsWith("Yes")) {
			entity.setParkingMeter("Yes");
		} else {
			entity.setParkingMeter("No");
		}
		
		beforeSave(request, entity, model);
		
		Long modifiedBy = getUser(request).getId();
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupPermitNotes(entity, modifiedBy);
		
		String permitAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			permitAuditMsg = "Permit created";
		} else {
			permitAuditMsg = "Permit updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		createAuditPermitNotes(entity, permitAuditMsg, modifiedBy);
		
		// The delivery address entered will automatically be stored as one of the Permit Addresses. Users can add more.
		addDeliveryAddAsPermitAdd(request, entity);
		
		// If new permit initiated from OrderPermitAlert screen, the permit should be associated with the corresponding orderID
		associateToOrder(entity, associatedOrderPermitEntry, request);
		updatePermitAndTotalFeesInOrder(request, entity, associatedOrderPermitEntry);
		
		cleanUp(request);
		
		return "Permit saved successfully";
	}

	private boolean isEmptyPermitNumber(Permit entity) {
		return (StringUtils.isEmpty(entity.getNumber()) 
				|| StringUtils.equals(Permit.EMPTY_PERMIT_NUMBER, entity.getNumber()));
	}

	private boolean validateParkingMeterFee(Permit entity) {
		
		if (entity.getParkingMeter().equalsIgnoreCase("No")) {
			return true;
		}

		System.out.println("Validating parking meter fee " + entity.getParkingMeterFee());
		try {
			BigDecimal parkingMeterFee = entity.getParkingMeterFee();
			if (parkingMeterFee == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		
	}

	private void updatePermitAndTotalFeesInOrder(HttpServletRequest request, Permit entity, OrderPermits associatedOrderPermitEntry) {
		// update permit fees in Order
		OrderFees orderFees = associatedOrderPermitEntry.getOrder().getOrderFees();
		int numberOfPermits = associatedOrderPermitEntry.getOrder().getPermits().size();
		BigDecimal permitFees = entity.getFee();
		
		if (numberOfPermits == 1) { // minimum 1, as this step is after associating this permit to the order
			// first permit
			orderFees.setPermitFee1(permitFees);
		} else if (numberOfPermits == 2) {
			// second permit
			orderFees.setPermitFee2(permitFees);
		} else if (numberOfPermits == 3) {
			// thirdPermit
			orderFees.setPermitFee3(permitFees);
		}
		
		orderFees.setTotalPermitFees(orderFees.getTotalPermitFees().add(permitFees));
		orderFees.setTotalFees(orderFees.getTotalFees().add(permitFees));
		associatedOrderPermitEntry.getOrder().setBalanceAmountDue(orderFees.getTotalFees().subtract(associatedOrderPermitEntry.getOrder().getTotalAmountPaid()));
		
		updateBaseProperties(request, orderFees);
		genericDAO.saveOrUpdate(orderFees);
		System.out.println("Permit Fees updated for Order with Id = " + associatedOrderPermitEntry.getOrder().getId());
	}

	/*private OrderPermits validatePermitEndDate(Permit entity) throws Exception {
		OrderPermits associatedOrderPermitEntry = null;
		List<OrderPermits> orderPermits = genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.deleteFlag='1' and obj.id=" +  entity.getOrderId()+ " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			associatedOrderPermitEntry = (OrderPermits) orderPermits.get(0);
			if (entity.getEndDate().before(associatedOrderPermitEntry.getOrder().getDeliveryDate())) {
				// Validation error must be thrown
				System.out.println("Permit End Date does not match the Order's Delivery Date, please check.");
				if (!validateMaxPermitsAllowable(associatedOrderPermitEntry)) {
					throw new Exception("ErrorMsg: There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " permits for this order.");
				} else {
					throw new Exception("ErrorMsg: Permit End Date (" + entity.getFormattedEndDate() + ") does not match the Order's Delivery Date (" + associatedOrderPermitEntry.getOrder().getFormattedDeliveryDate() + "), please check.");
				}
			}
		}
		return associatedOrderPermitEntry;
	}*/
	
	private PermitStatus retrievePermitStatus(String status) {
		PermitStatus permitStatus = (PermitStatus) genericDAO.executeSimpleQuery("select obj from PermitStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'").get(0);
		return permitStatus;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveForCustomerModal.do")
	public @ResponseBody String saveForCustomerModal(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {
		if (!validateParkingMeterFee(entity)) {
			System.out.println("Please correct following invalid data: Parking Meter Fee");
			return "ErrorMsg: Please correct following invalid data: Parking Meter Fee";
		}	
		
		String status = PermitStatus.PERMIT_STATUS_PENDING;
		if (isEmptyPermitNumber(entity)) {
			entity.setNumber(Permit.EMPTY_PERMIT_NUMBER);
		} else {
			if (!isPermitNumberUnique(entity.getId(), entity.getNumber())) { 
				return "ErrorMsg: Permit Number " + entity.getNumber() + " already exists.";
			}
			status = PermitStatus.PERMIT_STATUS_AVAILABLE;
		}
				
		if (shouldPermitStatusBeUpdated(entity, status)) {
			PermitStatus permitStatus = retrievePermitStatus(status);
			entity.setStatus(permitStatus);
		}
		
		beforeSave(request, entity, model);
		
		Long modifiedBy = getUser(request).getId();
		
		PermitAddress aPermitAddress = entity.getPermitAddress().get(0);
		aPermitAddress.setPermit(entity);
		if (aPermitAddress.getId() == null) {
			aPermitAddress.setCreatedBy(modifiedBy);
			aPermitAddress.setCreatedAt(entity.getCreatedAt());
		} else {
			aPermitAddress.setModifiedBy(modifiedBy);
			aPermitAddress.setModifiedAt(entity.getModifiedAt());
		}
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupPermitNotes(entity, modifiedBy);
		
		String permitAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			permitAuditMsg = "Permit created";
		} else {
			permitAuditMsg = "Permit updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		createAuditPermitNotes(entity, permitAuditMsg, modifiedBy);
		
		// The delivery address entered will automatically be stored as one of the Permit Addresses. Users can add more.
		//addDeliveryAddAsPermitAdd(request, entity);
		
		cleanUp(request);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}

	private void addDeliveryAddAsPermitAdd(HttpServletRequest request, Permit entity) {
		DeliveryAddress deliveryAddress =  (DeliveryAddress)genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id='" + entity.getDeliveryAddress().getId() + "'").get(0);
		PermitAddress permitAddress = new PermitAddress();
		permitAddress.setLine1(deliveryAddress.getLine1());
		permitAddress.setLine2(deliveryAddress.getLine2());
		permitAddress.setCity(deliveryAddress.getCity());
		permitAddress.setState(deliveryAddress.getState());
		permitAddress.setZipcode(deliveryAddress.getZipcode());
		permitAddress.setPermit(entity);
		
		permitAddress.setCreatedAt(Calendar.getInstance().getTime());
		if (permitAddress.getCreatedBy()==null) {
			permitAddress.setCreatedBy(getUser(request).getId());
		}
		
		genericDAO.save(permitAddress);
		
		List<PermitAddress> permitAddressList = new ArrayList<PermitAddress>();
		permitAddressList.add(permitAddress);
		entity.setPermitAddress(permitAddressList);
	}

	private void associateToOrder(Permit entity, OrderPermits associatedOrderPermit, HttpServletRequest request) {
		// Existing permit details, checkk order association
		if (associatedOrderPermit != null) {
			System.out.println("Associated Permit number = " + entity.getId());
			OrderPermits newOrderPermits = new OrderPermits();
			
			newOrderPermits.setOrder(associatedOrderPermit.getOrder());
			newOrderPermits.setPermit(entity);
			if (newOrderPermits instanceof AbstractBaseModel) {
				if (newOrderPermits.getId() == null) {
					newOrderPermits.setCreatedAt(Calendar.getInstance().getTime());
					if (newOrderPermits.getCreatedBy()==null) {
						newOrderPermits.setCreatedBy(getUser(request).getId());
					}
				} else {
					newOrderPermits.setModifiedAt(Calendar.getInstance().getTime());
					if (newOrderPermits.getModifiedBy()==null) {
						newOrderPermits.setModifiedBy(getUser(request).getId());
					}
				}
			}
			
			genericDAO.save(newOrderPermits);
			
		} else {
			System.out.println("For some reason associatedOrderPermit is null. Check Me!");
		}
	}
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Permit entity) {
		setupCreate(model, request);
		
		model.addAttribute("msgCtx", "managePermit");
		model.addAttribute("msg", "Permit saved successfully");
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitDetails"); // Permit Address?
		model.addAttribute("mode", "ADD");
		
		Permit emptyPermit = new Permit();
		emptyPermit.setId(entity.getId());
		
		PermitNotes notes = new PermitNotes();
		//notes.setPermit(entity);
		notes.setPermit(emptyPermit);
		model.addAttribute("notesModelObject", notes);
		List<PermitNotes> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.deleteFlag='1' and obj.permit.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		PermitAddress permitAddress = new PermitAddress();
		//permitAddress.setPermit(entity);
		permitAddress.setPermit(emptyPermit);
		model.addAttribute("permitAddressModelObject", permitAddress);
		List<PermitAddress> permitAddressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.deleteFlag='1' and obj.permit.id=" +  entity.getId() + " order by obj.line1 asc");
		model.addAttribute("permitAddressList", permitAddressList);
		
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress")
	public @ResponseBody String displayCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("customerId");
		List<DeliveryAddress> addressList  = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" + customerId  + " and obj.city like 'Chicago'" + " order by line1");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(addressList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(addressList);
		//return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/validatePermitCanBeAdded")
	public @ResponseBody String validatePermitCanBeAdded(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "orderId", required = false) Long orderId,
			@RequestParam(value = "deliveryAddressId") Long deliveryAddressId) {
		String validationErrorMsg = StringUtils.EMPTY;
		
		if (orderId != null) {
			List<OrderPermits> orderPermitList = genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.deleteFlag='1' and obj.order.id=" + orderId);
			if (orderPermitList != null && orderPermitList.size() == 3) {
				validationErrorMsg = "There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " permits for this order.";
			}
		}
		
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.id=" + deliveryAddressId);
		DeliveryAddress deliveryAddress = deliveryAddressList.get(0);
		String city = deliveryAddress.getCity();
		
		if (!StringUtils.equalsIgnoreCase("Chicago", city)) {
			if (StringUtils.isNotEmpty(validationErrorMsg)) {
				validationErrorMsg += "\n";
			}
			validationErrorMsg += "Permits only required for Chicago delivery address.";
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(validationErrorMsg);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/calculatePermitEndDate")
	public @ResponseBody String calculatePermitEndDate(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "permitTypeId") Long permitTypeId,
			@RequestParam(value = "startDate") Date startDate) {
		Date endDate = calculatePermitEndDate(permitTypeId, startDate);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(endDate).trim();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getPermitFee")
	public @ResponseBody String getPermitFee(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "permitTypeId") Long permitTypeId,
			@RequestParam(value = "permitClassId") Long permitClassId,
			@RequestParam(value = "startDate") Date startDate) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = formatter.format(startDate);
		List<PermitFee> permitFee = genericDAO.executeSimpleQuery("select obj from PermitFee obj where obj.deleteFlag='1' and obj.permitType=" + permitTypeId + "and obj.permitClass=" + permitClassId + " and '" + startDateStr + "' BETWEEN obj.effectiveStartDate and obj.effectiveEndDate");
		if (permitFee.isEmpty()) {
			return StringUtils.EMPTY;
		} else {
			return permitFee.get(0).getFee().toPlainString();
		}
	}

	private Date calculatePermitEndDate(Long permitTypeId, Date startDate) {
		if (startDate == null) {
			return null;
		}
		
		PermitType permitTypeObj = (PermitType) genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.deleteFlag='1' and obj.id=" + permitTypeId).get(0);
		if (permitTypeObj.getPermitType() == null) {
			return null;
		}
		
		String tokens[] = permitTypeObj.getPermitType().split("\\s");
		int noOfDays = new Integer(tokens[0]).intValue();
		Date endDate = DateUtils.addDays(startDate, (noOfDays-1));
		return endDate;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePermitNotes.do")
	public String savePermitNotes(HttpServletRequest request,
			@ModelAttribute("notesModelObject") PermitNotes entity,
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
		
		updateBaseProperties(request, entity);
		
		entity.setNotesType(PermitNotes.NOTES_TYPE_USER);
		setEnteredBy(entity);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);

		setupCreate(model, request);
		
		Long permitId = entity.getPermit().getId();
		
		Permit permit = new Permit();
		permit.setId(permitId);
		PermitNotes notes = new PermitNotes();
		notes.setPermit(permit);
		model.addAttribute("notesModelObject", notes);
		
		List<PermitNotes> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.deleteFlag='1' and obj.permit.id=" +  permitId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		List<Permit> permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.deleteFlag='1' and obj.id=" + permitId);
		model.addAttribute("modelObject", permitList.get(0));
		
		model.addAttribute("editDeliveryAddress", ((Permit)permitList.get(0)).getCustomer().getDeliveryAddress());
		
		List<PermitAddress> addressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.deleteFlag='1' and obj.permit.id=" +  permitId + " order by obj.line1 asc");
		model.addAttribute("permitAddressList", addressList);
		
		PermitAddress emptyPermitAddress = new PermitAddress();
		emptyPermitAddress.setPermit(permit);
		model.addAttribute("permitAddressModelObject", emptyPermitAddress);
		
		model.addAttribute("msgCtx", "savePermitNotes");
		model.addAttribute("msg", "Permit notes saved successfully");
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitNotes");
		model.addAttribute("mode", "ADD");		
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePermitAddress.do")
	public String savePermitAddress(HttpServletRequest request,
			@ModelAttribute("permitAddressModelObject") PermitAddress entity,
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

		updateBaseProperties(request, entity);
		if (entity.getId() != null) {
			List<PermitAddress> permitAddressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.deleteFlag='1' and obj.id=" + entity.getId());
			PermitAddress existingPermitAddress = permitAddressList.get(0);
			entity.setCreatedAt(existingPermitAddress.getCreatedAt());
			entity.setCreatedBy(existingPermitAddress.getCreatedBy());
		}
		
		String permitAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			permitAuditMsg = "Permit address created";
		} else {
			permitAuditMsg = "Permit address updated";
		}
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = getUser(request).getId();
		Permit emptyPermit = new Permit();
		emptyPermit.setId(entity.getPermit().getId());
		createAuditPermitNotes(emptyPermit, permitAuditMsg, modifiedBy);
		
		cleanUp(request);

		setupCreate(model, request);
		
		PermitAddress address = new PermitAddress();
		address.setPermit(emptyPermit);
		model.addAttribute("permitAddressModelObject", address);
		
		Long permitId = entity.getPermit().getId();
		List<Permit> permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.deleteFlag='1' and obj.id=" + permitId);
		model.addAttribute("modelObject", permitList.get(0));
		
		model.addAttribute("editDeliveryAddress", permitList.get(0).getCustomer().getDeliveryAddress());
		
		List<PermitAddress> addressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.deleteFlag='1' and obj.permit.id=" +  permitId + " order by obj.id asc");
		System.out.println("=======" + (addressList.get(addressList.size()-1)).getState());
		model.addAttribute("permitAddressList", addressList);
		
		List<PermitNotes> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.deleteFlag='1' and obj.permit.id=" +  permitId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		PermitNotes emptyPermitNotes = new PermitNotes();
		emptyPermitNotes.setPermit(emptyPermit);
		model.addAttribute("notesModelObject", emptyPermitNotes);
		
		model.addAttribute("msgCtx", "savePermitAddress");
		model.addAttribute("msg", "Permit Address saved successfully");
	
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("activeSubTab", "permitAddress");
		model.addAttribute("mode", "ADD");
		
		return urlContext + "/permit";
	}
	
	private void updateBaseProperties(HttpServletRequest request,
			AbstractBaseModel entity) {
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
	}
	
	@Override
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(100000);
		int page = criteria.getPage();
		criteria.setPage(0);
		
		ByteArrayOutputStream out = null;
		try {
			List<Permit> exportReportData =  retrieveReportData(criteria);
			
			if (StringUtils.equals("xls", type)) {
				type = "xlsx";
			}
			type = setRequestHeaders(response, type, "permitReport");
			
			Map<String, String> headers = new LinkedHashMap<>();
			headers.put("Delivery Address", "deliveryAddress");
			/*headers.put("Delivery Address", "deliveryAddress.fullLine");
			headers.put("Permit Address1", "fullLinePermitAddress1");
			headers.put("Permit Address2", "fullLinePermitAddress2");*/
			headers.put("Location Type", "locationType");
			headers.put("Permit Type", "permitType");
			headers.put("Permit Class", "permitClass");
			headers.put("Start Date", "startDate");
			headers.put("End Date", "endDate");
			headers.put("Customer Name", "customer");
			headers.put("Permit Number", "number");
			headers.put("Permit Fee", "fee");
			headers.put("Order #", "associatedOrderIds");
			headers.put("Status", "status");
			
			ExcelReportGenerator reportGenerator = new ExcelReportGenerator();
			reportGenerator.setTitleMergeCellRange("$A$1:$L$1");
			out = reportGenerator.exportReport("Permit Report", headers, exportReportData);
			
			out.writeTo(response.getOutputStream());
			
			criteria.setPageSize(25);
			criteria.setPage(page);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<Permit> retrieveReportData(SearchCriteria criteria) {
		if (!injectOrderSearchCriteria(criteria)) {
			// search yielded no results
			return new ArrayList<Permit>();
		}
		
		List<Permit> listOfPermits = performSearch(criteria);
		return listOfPermits;
	}
}
