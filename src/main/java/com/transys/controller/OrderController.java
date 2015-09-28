package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.core.util.MimeUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.AdditionalFee;
import com.transys.model.Address;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.DumpsterInfo;
import com.transys.model.DumpsterSize;
import com.transys.model.LocationType;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.OrderNotes;
import com.transys.model.OrderPaymentInfo;
import com.transys.model.OrderStatus;
import com.transys.model.PaymentMethodType;
import com.transys.model.Permit;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;

@Controller
@RequestMapping("/order")
public class OrderController extends CRUDController<Order> {
	public OrderController() {
		setUrlContext("order");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(Address.class, new AbstractModelEditor(Address.class));
		binder.registerCustomEditor(Permit.class, new AbstractModelEditor(Permit.class));
		binder.registerCustomEditor(LocationType.class, new AbstractModelEditor(LocationType.class));
		binder.registerCustomEditor(OrderPaymentInfo.class, new AbstractModelEditor(OrderPaymentInfo.class));
		binder.registerCustomEditor(OrderNotes.class, new AbstractModelEditor(OrderNotes.class));
		binder.registerCustomEditor(DumpsterInfo.class, new AbstractModelEditor(DumpsterInfo.class));
		binder.registerCustomEditor(User.class, new AbstractModelEditor(User.class));
		binder.registerCustomEditor(MaterialType.class, new AbstractModelEditor(MaterialType.class));
		binder.registerCustomEditor(AdditionalFee.class, new AbstractModelEditor(AdditionalFee.class));
		binder.registerCustomEditor(DumpsterSize.class, new AbstractModelEditor(DumpsterSize.class));
		
		super.initBinder(binder);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("order", genericDAO.executeSimpleQuery("select obj from Order obj where obj.id!=0 order by obj.id asc"));
		model.addAttribute("orderIds", genericDAO.executeSimpleQuery("select obj from Order obj where obj.id is not null order by obj.id asc"));
		
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("customers", genericDAO.findByCriteria(Customer.class, criterias, "companyName", false));
      //model.addAttribute("deliveryAddresses", genericDAO.findByCriteria(Address.class, criterias, "line1", false));
      
      //model.addAttribute("permits", genericDAO.executeSimpleQuery("select obj from Permit obj where obj.id!=0 order by obj.id asc"));
      //model.addAttribute("permits", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
      
      model.addAttribute("dumpsters", genericDAO.executeSimpleQuery("select obj from DumpsterInfo obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("dumpsterSizes", genericDAO.executeSimpleQuery("select obj from DumpsterSize obj where obj.id!=0 order by obj.size asc"));
      
      model.addAttribute("dusmpsterLocationTypes", genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("permitClasses", genericDAO.executeSimpleQuery("select obj from PermitClass obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("permitTypes", genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("additionalFeeTypes", genericDAO.executeSimpleQuery("select obj from AdditionalFee obj where obj.id!=0 order by obj.id asc"));
     
      model.addAttribute("materialTypes", genericDAO.executeSimpleQuery("select obj from MaterialType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethod obj where obj.id!=0 order by obj.id asc"));
      
      populateDeliveryTimeSettings(model);
      
      String driverRole = "DRIVER";
      List<BaseModel> driversList = genericDAO.executeSimpleQuery("select obj from User obj where obj.id!=0 and obj.role.name='" + driverRole + "' order by obj.id asc");
      model.addAttribute("drivers", driversList);
	}
	
	private void populateDeliveryTimeSettings(ModelMap model) {
		List<String> deliveryHours = new ArrayList<String>();
		deliveryHours.add("--");
      deliveryHours.add("12:00 AM");
      deliveryHours.add("1:00 AM");
      
      model.addAttribute("deliveryHours", deliveryHours);
      
      List<String> deliveryMinutes = new ArrayList<String>();
      deliveryMinutes.add("--");
      deliveryMinutes.add("00");
      deliveryMinutes.add("15");
      
      model.addAttribute("deliveryMinutes", deliveryMinutes);
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"id",null,null));
		model.addAttribute("activeTab", "manageOrders");
		//model.addAttribute("activeSubTab", "orderDetails");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/orderReport.do")
	public String orderReport(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		model.addAttribute("orderReportList",genericDAO.search(getEntityClass(), criteria,"id",null,null));
		model.addAttribute("activeTab", "orderReports");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/order";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		//return urlContext + "/form";
		
		model.addAttribute("notesModelObject", new OrderNotes());
		
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveOrderNotes.do")
	public String saveOrderNotes(HttpServletRequest request,
			@ModelAttribute("notesModelObject") OrderNotes entity,
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
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderNotesTab");
		
		Long orderId = entity.getOrder().getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.id=" + orderId);
		Order savedOrder = orderList.get(0);
		model.addAttribute("modelObject", savedOrder);
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from Address obj where obj.customer.id=" +  savedOrder.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		for(Permit aChosenPermit : savedOrder.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId =  aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId =  aChosenPermit.getPermitType().getId().toString();
				List<Permit> aPermitsOfChosenTypeList = retrievePermit(StringUtils.EMPTY, aChosenPermitClassId, aChosenPermitTypeId);
				
				allPermitsOfChosenTypesList.add(aPermitsOfChosenTypeList);
			}
		}
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveDropOffDriver.do")
	public String saveDropOffDriver(HttpServletRequest request,
			@ModelAttribute("modelObject") Order entity,
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
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "dropOffDriver");
		
		Long orderId = entity.getId();
		List<BaseModel> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.id=" + orderId);
		model.addAttribute("modelObject", orderList.get(0));
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from Address obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		for(Permit aChosenPermit : entity.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId =  aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId =  aChosenPermit.getPermitType().getId().toString();
				List<Permit> aPermitsOfChosenTypeList = retrievePermit(StringUtils.EMPTY, aChosenPermitClassId, aChosenPermitTypeId);
				
				allPermitsOfChosenTypesList.add(aPermitsOfChosenTypeList);
			}
		}
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/savePickupDriver.do")
	public String savePickupDriver(HttpServletRequest request,
			@ModelAttribute("modelObject") Order entity,
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
		
		//TODO: why is this reqd?
		entity.getOrderPaymentInfo().setOrder(entity);
		entity.getOrderPaymentInfo().setCreatedBy(entity.getCreatedBy());
		entity.getOrderPaymentInfo().setModifiedBy(entity.getModifiedBy());
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);

		setupCreate(model, request);
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "pickupDriver");
		
		Long orderId = entity.getId();
		List<BaseModel> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.id=" + orderId);
		model.addAttribute("modelObject", orderList.get(0));
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from Address obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		for(Permit aChosenPermit : entity.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId =  aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId =  aChosenPermit.getPermitType().getId().toString();
				List<Permit> aPermitsOfChosenTypeList = retrievePermit(StringUtils.EMPTY, aChosenPermitClassId, aChosenPermitTypeId);
				
				allPermitsOfChosenTypesList.add(aPermitsOfChosenTypeList);
			}
		}
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "id", null, null));
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "MANAGE");
		return urlContext + "/order";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "id", null, null));
		model.addAttribute("activeTab", "manageOrder");
		return urlContext + "/order";
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/generateOrderReport.do")
	public void generateOrderReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		
		try {
		if (StringUtils.isEmpty(type))
			type = "xlsx";
		if (!type.equals("html") && !(type.equals("print"))) {
			response.setHeader("Content-Disposition",
					"attachment;filename= ordersReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String, Object> params = new HashMap<String,Object>();
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<Order> list = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		List<Map<String, ?>> newList = new ArrayList<Map<String, ?>>();
		for (Order order : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", order.getId());
			map.put("company_name", order.getCustomer().getCompanyName());
			map.put("createdAt", order.getCreatedAt().toString());
			map.put("deliveryContactName", order.getDeliveryContactName());
			map.put("phone", order.getCustomer().getPhone());
			map.put("line1", order.getDeliveryAddress().getLine1());
			map.put("city", order.getDeliveryAddress().getCity());
			map.put("line1", order.getDeliveryAddress().getLine1());
			map.put("status", order.getOrderStatus().getStatus());
			map.put("deliveryDate", order.getDeliveryDate().toString());
			map.put("pickupDate", order.getPickupDate().toString());
			map.put("paymentMethod", order.getOrderPaymentInfo().getPaymentMethod());
			map.put("dumpsterPrice", order.getOrderPaymentInfo().getDumpsterPrice().toString());
			map.put("cityFee", order.getOrderPaymentInfo().getCityFee().toString());
			map.put("permitFees", order.getOrderPaymentInfo().getPermitFees().toString());
			map.put("overweightFee", order.getOrderPaymentInfo().getOverweightFee().toString());
			if (order.getOrderPaymentInfo().getTotalFees() != null ){
			map.put("totalFees", order.getOrderPaymentInfo().getTotalFees().toString());
			}
			
			newList.add(map);
		}
		
		if (!type.equals("print") && !type.equals("pdf")) {
			out = dynamicReportService.generateStaticReport("ordersReport",
					newList, params, type, request);
		}
		else if(type.equals("pdf")){
			out = dynamicReportService.generateStaticReport("ordersReportPdf",
					newList, params, type, request);
		}
		else {
			out = dynamicReportService.generateStaticReport("ordersReport"+"print",
					newList, params, type, request);
		}
	
		out.writeTo(response.getOutputStream());
		out.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		log.warn("Unable to create file :" + e);
		request.getSession().setAttribute("errors", e.getMessage());
		
	}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrievePermit.do")
	public @ResponseBody String retrievePermit(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "permitId", required = false) String permitId,
															 @RequestParam(value = "permitClassId", required = false) String permitClassId,
															 @RequestParam(value = "permitTypeId", required = false) String permitTypeId) {
		List<Permit> permitList = retrievePermit(permitId, permitClassId, permitTypeId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(permitList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	
	private List<Permit> retrievePermit(String permitId, String permitClassId, String permitTypeId) {
		String query = "select obj from Permit obj where ";
		if (StringUtils.isNotEmpty(permitId)) {
			query += "obj.id=" + permitId;
		} else {
			query += "obj.permitType.id=" + permitTypeId;
			query += " and obj.permitClass.id=" + permitClassId;
		}
		
		return genericDAO.executeSimpleQuery(query);
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		 
		Order orderToBeEdited = (Order)model.get("modelObject");
		
		String query = "select obj from Address obj where obj.customer.id=" +  orderToBeEdited.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
    	
		Order emptyOrder = new Order();
		emptyOrder.setId(orderToBeEdited.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		for(Permit aChosenPermit : orderToBeEdited.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId =  aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId =  aChosenPermit.getPermitType().getId().toString();
				List<Permit> aPermitsOfChosenTypeList = retrievePermit(StringUtils.EMPTY, aChosenPermitClassId, aChosenPermitTypeId);
				
				allPermitsOfChosenTypesList.add(aPermitsOfChosenTypeList);
			}
		}
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		//return urlContext + "/form";
		return urlContext + "/order";
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress.do")
	public @ResponseBody String retrieveCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("id");
		List<Address> addressList  = genericDAO.executeSimpleQuery("select obj from Address obj where obj.customer.id=" + customerId);
		
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerAddress.do")
	public @ResponseBody String retrieveCustomerAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("id");
		List<Customer> customerList  = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(customerList.get(0));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		//String json = (new Gson()).toJson(customerList.get(0));
		//return json;
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Order entity,
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
			return urlContext + "/order";
		}
		
		beforeSave(request, entity, model);
		
		StringBuffer permitIdsBuff = new StringBuffer();
		for (Permit aPermit : entity.getPermits()) {
			if (aPermit != null && aPermit.getId() != null) {
				permitIdsBuff.append(aPermit.getId().toString() + ", ");
			}
		}
		
		// TODO: why is permit updating even when not changed?
		String permitIds = permitIdsBuff.substring(0, (permitIdsBuff.length() - 2));
		List<Permit> permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.id in (" 
																					+ permitIds
																					+ ")");
		entity.setPermits(permitList);
		
		String initOrderStatus = "Open";
		OrderStatus orderStatus = (OrderStatus)genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.status='" + initOrderStatus + "'").get(0);
		entity.setOrderStatus(orderStatus);
		
		// TODO: Why both created by and modified by and why set if not changed?
		entity.getOrderPaymentInfo().setOrder(entity);
		entity.getOrderPaymentInfo().setCreatedBy(entity.getCreatedBy());
		entity.getOrderPaymentInfo().setModifiedBy(entity.getModifiedBy());
		
		// TODO: Why both created by and modified by and why set if not changed?
		entity.getOrderNotes().get(0).setOrder(entity);
		entity.getOrderNotes().get(0).setCreatedBy(entity.getCreatedBy());
		entity.getOrderNotes().get(0).setModifiedBy(entity.getModifiedBy());

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
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Order entity) {
		setupCreate(model, request);
		model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("activeSubTab", "orderDetails");
		model.addAttribute("mode", "ADD");
		
		Order emptyOrder = new Order();
		emptyOrder.setId(entity.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from Address obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		for(Permit aChosenPermit : entity.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId =  aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId =  aChosenPermit.getPermitType().getId().toString();
				List<Permit> aPermitsOfChosenTypeList = retrievePermit(StringUtils.EMPTY, aChosenPermitClassId, aChosenPermitTypeId);
				
				allPermitsOfChosenTypesList.add(aPermitsOfChosenTypeList);
			}
		}
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		Long customerId = entity.getCustomer().getId();
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId);
		Customer orderCustomer = customerList.get(0);
		
		/*//Or set hibernate fetch all?
		List<State> stateList = genericDAO.executeSimpleQuery("select obj from State obj where obj.id= " + orderCustomer.getState().getId());
		orderCustomer.getState().setName(stateList.get(0).getName());*/
		
		entity.setCustomer(orderCustomer);
		
		//return urlContext + "/form";
		return urlContext + "/order";

		//return super.save(request, entity, bindingResult, model);
	}
}