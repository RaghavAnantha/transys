package com.transys.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.transys.model.AbstractBaseModel;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.LocationType;
import com.transys.model.Order;
import com.transys.model.OrderPaymentInfo;
import com.transys.model.OrderPermits;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.PermitAddress;
import com.transys.model.PermitClass;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;
import com.transys.model.State;

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
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");

		List<Permit> listOfPermits = genericDAO.search(Permit.class, criteria, "id", null, null);
		
		for (Permit p : listOfPermits) {
			List<BaseModel> orders = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj.order from OrderPermits obj where obj.permit.id=" +  p.getId() + " order by obj.id desc");
			if (orders != null && orders.size() > 0) {
				BaseModel order = orders.get(0);
				p.setOrderId(order.getId());
			}
		}
		
		model.addAttribute("list",listOfPermits);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/permit";
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
			return urlContext + "/list";
		}
		
		List<Permit> listOfPermits = genericDAO.search(getEntityClass(), criteria);
		
		for (Permit p : listOfPermits) {
			List<BaseModel> orders = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj.order from OrderPermits obj where obj.permit.id=" +  p.getId() + " order by obj.id desc");
			if (orders != null && orders.size() > 0) {
				BaseModel order = orders.get(0);
				p.setOrderId(order.getId());
			}
		}
		
		model.addAttribute("list",listOfPermits);
		return urlContext + "/list";
	}

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
		return permitIDSearchCriteria.substring(0, permitIDSearchCriteria.lastIndexOf(",")).toString();
	}

	private List<OrderPermits> getOrderRelatedPermits(Map<String, Object> searchMap, Object[] param, int i) {
		SearchCriteria innerSearch = new SearchCriteria();
		Map innerSearchCriteria = new HashMap<String, Object>();
		innerSearch.setSearchMap(innerSearchCriteria);
		innerSearchCriteria.put("order", searchMap.get(param[i].toString()));
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
		String customerQuery = "select obj from Customer obj where obj.id=" + customerId;
		List<Customer> customerList = genericDAO.executeSimpleQuery(customerQuery);
		model.put("customer", customerList);
		
		String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.id=" + deliveryAddressId;
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		model.addAttribute("deliveryAddress", deliveryAddressList);
		
		String locationTypesQuery = "select obj from LocationType obj where obj.id=" + locationTypeId;
		List<LocationType> locationTypeList = genericDAO.executeSimpleQuery(locationTypesQuery);
		model.addAttribute("locationType", locationTypeList);
		
		String permitClassQuery = "select obj from PermitClass obj where obj.id=" + permitClassId;
		List<PermitClass> permitClassList = genericDAO.executeSimpleQuery(permitClassQuery);
		model.addAttribute("permitClass", permitClassList);
		
		String permitTypeQuery = "select obj from PermitType obj where obj.id=" + permitTypeId;
		List<PermitType> permitTypeList = genericDAO.executeSimpleQuery(permitTypeQuery);
		model.addAttribute("permitType", permitTypeList);
		
		Permit emptyPermit = new Permit();
		emptyPermit.setStartDate(deliveryDate);
		Date endDate = calculatePermitEndDate(permitTypeId, deliveryDate);
		emptyPermit.setEndDate(endDate);
		
		model.put("modelObject", emptyPermit);
		
		return urlContext + "/formForCustomerModal";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/permitCreateModal.do")
	public String permitCreateModal(ModelMap model, HttpServletRequest request, 
			@RequestParam(value = "id") Long orderPermitId)  {
	
		setupUpdate(model, request);
		System.out.println("OrderPermit Id being edited = " + orderPermitId);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("id", orderPermitId);
		OrderPermits orderPermitToBeEdited = genericDAO.findByCriteria(OrderPermits.class, criterias, "id", false).get(0);
		
		if (!validateMaxPermitsAllowable(model, orderPermitToBeEdited)) {
			return urlContext + "/formModal"; //TODO: Check
		}
		
		Permit permitToBeEdited = orderPermitToBeEdited.getPermit();
		permitToBeEdited.setNumber(StringUtils.EMPTY); // empty the permit number
		
		model.put("modelObject", permitToBeEdited);
		
		List<Customer> customers = new ArrayList<>();
		customers.add(permitToBeEdited.getCustomer());
		model.put("customer", customers);
		
		List<DeliveryAddress> deliveryAddress = new ArrayList<>();
		deliveryAddress.add(orderPermitToBeEdited.getOrder().getDeliveryAddress());
		model.addAttribute("deliveryAddress", deliveryAddress);
		
		List<LocationType> locationType = new ArrayList<>();
		locationType.add(permitToBeEdited.getLocationType());
		model.addAttribute("locationType", locationType);
		
		List<PermitClass> permitClass = new ArrayList<>();
		permitClass.add(permitToBeEdited.getPermitClass());
		model.addAttribute("permitClass", permitClass);
		
		criterias = new HashMap<String, Object>();
		criterias.put("permit", permitToBeEdited);
		model.addAttribute("permitAddress", genericDAO.findByCriteria(PermitAddress.class, criterias, "id", false));
		
		model.addAttribute("associatedOrderID", orderPermitToBeEdited);
		
		return urlContext + "/formModal";
	}

	private boolean validateMaxPermitsAllowable(ModelMap model, OrderPermits orderPermitToBeEdited) {
		// validate if this order has < 3 permits, else show alert
		List<BaseModel> orderPermitsForThisOrder = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.order.id=" +  orderPermitToBeEdited.getOrder().getId());
		if (orderPermitsForThisOrder != null && orderPermitsForThisOrder.size() >= MAX_NUMBER_OF_ASSOCIATED_PERMITS) {
			//do not create modal, show alert
			model.addAttribute("error", "There are already " + MAX_NUMBER_OF_ASSOCIATED_PERMITS + " for this order.");
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
		
		// get the delivery address
		model.addAttribute("editDeliveryAddress", permitToBeEdited.getCustomer().getDeliveryAddress());
		
		PermitNotes notes = new PermitNotes();
		notes.setPermit(permitToBeEdited); 
		model.addAttribute("notesModelObject", notes);
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
	
		PermitAddress permitAddress = new PermitAddress();
		permitAddress.setPermit(permitToBeEdited); 
		model.addAttribute("permitAddressModelObject", permitAddress);
		List<BaseModel> permitAddressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("permitAddressList", permitAddressList);

		// only in cases of Edit, an order ID can be associated with the permit
		List<BaseModel> orderPermits = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			BaseModel orderPermitObj = orderPermits.get(0);
			model.addAttribute("associatedOrderID", orderPermitObj);
		}
	
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
		Map criterias = new HashMap();
		
		List<DeliveryAddress> addresses = genericDAO.findUniqueByCriteria(DeliveryAddress.class, criterias, "line1", false);
	   model.addAttribute("allDeliveryAddresses", addresses);
		model.addAttribute("customer", genericDAO.findByCriteria(Customer.class, criterias, "contactName", false));
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("state", genericDAO.findAll(State.class));

	}
	
	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {
		
			boolean isNewPermitFromAlertScreen = isNewPermitFromAlertScreen(entity);
			String status = "Pending";
			OrderPermits associatedOrderPermitEntry = null;
			
			if (isNewPermitFromAlertScreen) { // Its a new permit, triggered from OrderPermit Screen
				status = "Assigned"; // TODO: Is this ok?
				associatedOrderPermitEntry = validatePermitEndDate(entity);
			} else {
				if (entity.getNumber() != null && entity.getNumber().length() > 0) {
					status = "Available";
				} 
			}
			
			PermitStatus permitStatus = (PermitStatus)genericDAO.executeSimpleQuery("select obj from PermitStatus obj where obj.status='" + status + "'").get(0);
			entity.setStatus(permitStatus);
			
			try {
				getValidator().validate(entity, bindingResult);
			} catch (ValidationException e) {
				e.printStackTrace();
				System.out.println("Error in validation " + e);
				log.warn("Error in validation :" + e);
			}
			
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
			criteria.getSearchMap().put("id!",0l);
			//TODO: Fix me 
			criteria.getSearchMap().remove("_csrf");
			
			// return to form if we had errors
			if (bindingResult.hasErrors()) {
				List<ObjectError> errors = bindingResult.getAllErrors();
				for(ObjectError e : errors) {
					System.out.println("Error: " + e.getDefaultMessage());
				}
				
				setupCreate(model, request);
				return urlContext + "/form";
			}
			
			System.out.println("Permits parking meter = " + entity.getParkingMeter());
			//TODO: REMOVE THIS HACK
			if (entity.getParkingMeter().startsWith("Yes")) {
				entity.setParkingMeter("Yes");
			} else {
				entity.setParkingMeter("No");
			}
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			
			// The delivery address entered will automatically be stored as one of the Permit Addresses. Users can add more.
			addDeliveryAddAsPermitAdd(request, entity);
			
			// If new permit initiated from OrderPermitAlert screen, the permit should be associated with the corresponding orderID
			if (isNewPermitFromAlertScreen) {
				
				associateToOrder(entity, associatedOrderPermitEntry, request);
				updatePermitAndTotalFeesInOrder(entity, associatedOrderPermitEntry);
			}
			
			cleanUp(request);
			
			return saveSuccess(model, request, entity);
	}

	private void updatePermitAndTotalFeesInOrder(Permit entity, OrderPermits associatedOrderPermitEntry) {
		// update permit fees in Order
		OrderPaymentInfo orderPaymentInfo = associatedOrderPermitEntry.getOrder().getOrderPaymentInfo();
		int numberOfPermits = associatedOrderPermitEntry.getOrder().getPermits().size();
		BigDecimal permitFees = entity.getFee();
		
		if (numberOfPermits == 1) { // minimum 1, as this step is after associating this permit to the order
			// first permit
			orderPaymentInfo.setPermitFee1(permitFees);
		} else if (numberOfPermits == 2) {
			// second permit
			orderPaymentInfo.setPermitFee2(permitFees);
		} else if (numberOfPermits == 3) {
			// thirdPermit
			orderPaymentInfo.setPermitFee3(permitFees);
		}
		
		orderPaymentInfo.setTotalPermitFees(orderPaymentInfo.getTotalPermitFees().add(permitFees));
		orderPaymentInfo.setTotalFees(orderPaymentInfo.getTotalFees().add(permitFees));
		
		genericDAO.saveOrUpdate(orderPaymentInfo);
		System.out.println("Permit Fees updated for Order with Id = " + associatedOrderPermitEntry.getOrder().getId());
	}

	private OrderPermits validatePermitEndDate(Permit entity) {
		OrderPermits associatedOrderPermitEntry = null;
		// check the dates, endDate >= Order's delivery date
		List<BaseModel> orderPermits = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.id=" +  entity.getOrderId()+ " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			associatedOrderPermitEntry = (OrderPermits) orderPermits.get(0);
			if (entity.getEndDate().before(associatedOrderPermitEntry.getOrder().getDeliveryDate())) {
				// validation error must be thrown
				System.out.println("Permit End Date does not match the Order's Delivery Date, please check.");
			}
		}
		return associatedOrderPermitEntry;
	}
	
	private boolean isNewPermitFromAlertScreen(Permit entity) {
		System.out.println("Entity = " + entity.getOrderId());
		if (entity.getOrderId() != null) { // Its
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/saveForCustomerModal.do")
	public @ResponseBody String saveForCustomerModal(HttpServletRequest request,
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
			return urlContext + "/form";
		}
		
		String status = "Pending";
		PermitStatus permitStatus = (PermitStatus)genericDAO.executeSimpleQuery("select obj from PermitStatus obj where obj.status='" + status + "'").get(0);
		entity.setStatus(permitStatus);
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		
		// The delivery address entered will automatically be stored as one of the Permit Addresses. Users can add more.
		addDeliveryAddAsPermitAdd(request, entity);
		
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
		DeliveryAddress deliveryAddress =  (DeliveryAddress)genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.id='" + entity.getDeliveryAddress().getId() + "'").get(0);
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
		// existing permit details, chk order association
		if (associatedOrderPermit != null) {
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.put("number", entity.getNumber());
			Permit newPermit = genericDAO.findByCriteria(Permit.class, criterias, "id", false).get(0);
			OrderPermits newOrderPermits = new OrderPermits();
			
			newOrderPermits.setOrder(associatedOrderPermit.getOrder());
			newOrderPermits.setPermit(newPermit);
			if (newOrderPermits instanceof AbstractBaseModel) {
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
			
			genericDAO.save(newOrderPermits);
			
		} else {
			System.out.println("For some reason associatedOrderPermit is null. Check Me!");
		}
	}
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Permit entity) {
		setupCreate(model, request);
		
		if (entity.getOrderId() != null) {
			model.addAttribute("activeTab", "orderPermitAlert");
		} else {
			model.addAttribute("activeTab", "managePermits");
			model.addAttribute("activeSubTab", "permitNotes"); // Permit Address?
			model.addAttribute("mode", "ADD");
		}
		
		PermitNotes notes = new PermitNotes();
		notes.setPermit(entity);
		model.addAttribute("notesModelObject", notes);
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from PermitNotes obj where obj.permit.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		PermitAddress permitAddress = new PermitAddress();
		permitAddress.setPermit(entity); 
		model.addAttribute("permitAddressModelObject", permitAddress);
		List<BaseModel> permitAddressList = genericDAO.executeSimpleQuery("select obj from PermitAddress obj where obj.permit.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("permitAddressList", permitAddressList);
		
		if (entity.getOrderId() != null) {
			return "orderPermitAlert/list";
		} else {
			return urlContext + "/permit";
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress")
	public @ResponseBody String displayCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("customerId");
		List<DeliveryAddress> addressList  = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.customer.id=" + customerId);
		
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/calculatePermitEndDate")
	public @ResponseBody String calculatePermitEndDate(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "permitTypeId") Long permitTypeId,
			@RequestParam(value = "startDate") Date startDate) {
		Date endDate = calculatePermitEndDate(permitTypeId, startDate);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(endDate);
	}

	private Date calculatePermitEndDate(Long permitTypeId, Date startDate) {
		if (startDate == null) {
			return null;
		}
		
		PermitType permitTypeObj = (PermitType) genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.id=" + permitTypeId).get(0);
		if (permitTypeObj.getPermitType() == null) {
			return null;
		}
		
		String tokens[] = permitTypeObj.getPermitType().split("\\s");
		int noOfDays = new Integer(tokens[0]).intValue();
		Date endDate = DateUtils.addDays(startDate, noOfDays);
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

		setupCreate(model, request);
		return urlContext + "/list";
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

		setupCreate(model, request);
		return urlContext + "/list";
	}
}
