package com.transys.controller;

import java.text.ParseException;
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
				p.setOrderID(order.getId());
			}
		}
		
		model.addAttribute("list",listOfPermits);
		
		model.addAttribute("activeTab", "managePermits");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listOrderPermit.do")
	public String displayOrderPermitsAlert(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		// Search for permits corresponding to open orders and status=expired or expiring in the next 7 days
		List<OrderPermits> orderPermits = getToBeAlertedPermits(criteria);
		model.addAttribute("orderPermitList", orderPermits);
		
		model.addAttribute("activeTab", "orderPermitAlert");
		return urlContext + "/permit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/notes.do")
	public String displayNotes(ModelMap model, HttpServletRequest request) {
		return urlContext + "/permit";
	}
	
	/**
	 * Return the list of permits associated with open orders, expired or expiring in the next 7 days
	 * 
	 * @param searchCriteria
	 * @return
	 */
	private List<OrderPermits> getToBeAlertedPermits(SearchCriteria searchCriteria) {
			// TODO: open orders --> include DroppedOff?
			Object existingSearchValue = setupOrderPermitSearchCriteria(searchCriteria);
			
			List<OrderPermits> orderPermits = genericDAO.search(OrderPermits.class, searchCriteria, "id", null, null);
			System.out.println("Order Permits Size = " + orderPermits.size());

			cleanOrderPermitSearchCriteria(searchCriteria, existingSearchValue);
	
			return orderPermits;
			
	}

	private Object setupOrderPermitSearchCriteria(SearchCriteria searchCriteria) {
		
		Object existingSearchValue = null;
		Map searchMap = searchCriteria.getSearchMap();
		
		if (searchMap.containsKey("order.orderStatus.status")) {
			existingSearchValue = searchMap.get("order.orderStatus.status");
			// append these to it, store the existing search to be reset after the search
			
		} else {
			searchMap.put("order.orderStatus.status", "Open");
		}
		
		// permit EndDate <= Today + 7 days
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		searchMap.put("permit.endDate", "<=" + BaseController.dateFormat.format(cal.getTime()));
		
		searchCriteria.setSearchMap(searchMap);
		return existingSearchValue;
	}

	private void cleanOrderPermitSearchCriteria(SearchCriteria searchCriteria, Object existingSearchValue) {
		Map searchMap = searchCriteria.getSearchMap();
		if (existingSearchValue == null) {
			// remove the key
			searchMap.remove("existingSearchValue");
		} else {
			// reset the value
			searchMap.put("order.orderStatus.status", existingSearchValue);
		}
		
		// TODO: Filter on endDate already available in SearchCriteria
		searchMap.remove("permit.endDate");
		searchCriteria.setSearchMap(searchMap);
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
				p.setOrderID(order.getId());
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/permitCreateModal.do")
	public String deliveryAddressCreateModal(ModelMap model, HttpServletRequest request, @RequestParam(value = "id") Long orderPermitId) {
	
		setupUpdate(model, request);
		System.out.println("OrderPermit Id being edited = " + orderPermitId);

		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("id", orderPermitId);
		OrderPermits orderPermitToBeEdited = genericDAO.findByCriteria(OrderPermits.class, criterias, "id", false).get(0);
		Permit permitToBeEdited = orderPermitToBeEdited.getPermit();
		permitToBeEdited.setNumber(StringUtils.EMPTY); // empty the permit number
		
	/*	PermitStatus permitStatus = new PermitStatus(); // will it automatically be done on permit save?
		permitStatus.setStatus("PENDING");
		permitToBeEdited.setStatus(permitStatus); */
		
		model.put("modelObject", permitToBeEdited);
		
		List<BaseModel> orderPermits = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.permit.id=" +  permitToBeEdited.getId() + " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			BaseModel orderPermitObj = orderPermits.get(0);
			model.addAttribute("associatedOrderID", orderPermitObj);
		}
		
		/*Permit permitToBeEdited = (Permit)model.get("modelObject");
		
		if (permitToBeEdited == null) { // OrderPermitAlert Screen - Add New Permit, should do a new add, not an update - clear out the id field?
			OrderPermits orderPermitToBeEdited = setupOrderPermitModel(request);
			permitToBeEdited = orderPermitToBeEdited.getPermit();
			System.out.println("Got the new permit number " + permitToBeEdited.getNumber());
			model.put("modelObject", permitToBeEdited);
		} 
		
		System.out.println("Permit to be edited = " + permitToBeEdited.getId());
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
	*/
		
		return urlContext + "/formModal";
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
	   model.addAttribute("deliveryAddress", addresses);
		model.addAttribute("customer", genericDAO.findByCriteria(Customer.class, criterias, "contactName", false));
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("state", genericDAO.findAll(State.class));

		List<OrderPermits> orderPermits = getToBeAlertedPermits(new SearchCriteria());
		model.addAttribute("orderPermitList", orderPermits);

	}
	
	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Permit entity,
			BindingResult bindingResult, ModelMap model) {

			String status = "Pending";
			if (entity.getNumber() != null && entity.getNumber().length() > 0) {
				status = "Available";
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
			
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			
			// If new permit initiated from OrderPermitAlert screen, the permit should be associated with the corresponding orderID
			associateToOrder(entity);
			
			cleanUp(request);
			
			return saveSuccess(model, request, entity);
	}

	private void associateToOrder(Permit entity) {
		if (entity.getOrderID() == null) { // Its a new permit, not triggered from OrderPermit Screen
			return;
		}
		
		// existing permit details, chk order association
		List<BaseModel> orderPermits = (List<BaseModel>)genericDAO.executeSimpleQuery("select obj from OrderPermits obj where obj.id=" +  entity.getOrderID()+ " order by obj.id desc");
		if (orderPermits != null && orderPermits.size() > 0) {
			OrderPermits associatedOrderPermit = (OrderPermits)orderPermits.get(0);
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.put("number", entity.getNumber());
			Permit newPermit = genericDAO.findByCriteria(Permit.class, criterias, "id", false).get(0);
			associatedOrderPermit.setPermit(newPermit);
			genericDAO.saveOrUpdate(associatedOrderPermit);
		}
	}
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Permit entity) {
		setupCreate(model, request);
		
		if (entity.getOrderID() != null) {
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
		
		return urlContext + "/permit";
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
	public @ResponseBody String calculatePermitEndDate(ModelMap model, HttpServletRequest request) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateInString = request.getParameter("startDate");
		Date startDateObj = null;
		try {
			startDateObj = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String permitType = request.getParameter("permitType");
		PermitType permitTypeObj = (PermitType) genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.id=" + permitType).get(0);
		if (startDateObj != null && permitTypeObj.getPermitType() != null) {
			String tokens[] = permitTypeObj.getPermitType().split("\\s");
			int noOfDays = new Integer(tokens[0]).intValue();
			Date endDate = DateUtils.addDays(startDateObj, noOfDays);
			System.out.println("End date = " + endDate);
			return formatter.format(endDate);
		} else {
			return null;
		}
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
