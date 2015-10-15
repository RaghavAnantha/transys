package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
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
import com.transys.core.dao.GenericJpaDAO;
import com.transys.core.tags.IColumnTag;
import com.transys.core.util.MimeUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.AdditionalFee;
import com.transys.model.DeliveryAddress;
import com.transys.model.BaseModel;
import com.transys.model.CityFee;
import com.transys.model.Customer;
import com.transys.model.Dumpster;
import com.transys.model.DumpsterPrice;
import com.transys.model.DumpsterSize;
import com.transys.model.DumpsterStatus;
import com.transys.model.LocationType;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderNotes;
import com.transys.model.OrderPayment;
import com.transys.model.OrderPermits;
import com.transys.model.OrderStatus;
import com.transys.model.OverweightFee;
import com.transys.model.PaymentMethodType;
import com.transys.model.Permit;
import com.transys.model.PermitClass;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;
import com.transys.service.DynamicReportServiceImpl;

@Controller
@RequestMapping("/order")
public class OrderController extends CRUDController<Order> {
	public OrderController() {
		setUrlContext("order");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(DeliveryAddress.class, new AbstractModelEditor(DeliveryAddress.class));
		binder.registerCustomEditor(Permit.class, new AbstractModelEditor(Permit.class));
		binder.registerCustomEditor(LocationType.class, new AbstractModelEditor(LocationType.class));
		binder.registerCustomEditor(OrderPayment.class, new AbstractModelEditor(OrderPayment.class));
		binder.registerCustomEditor(OrderNotes.class, new AbstractModelEditor(OrderNotes.class));
		binder.registerCustomEditor(Dumpster.class, new AbstractModelEditor(Dumpster.class));
		binder.registerCustomEditor(User.class, new AbstractModelEditor(User.class));
		binder.registerCustomEditor(MaterialType.class, new AbstractModelEditor(MaterialType.class));
		binder.registerCustomEditor(MaterialCategory.class, new AbstractModelEditor(MaterialCategory.class));
		binder.registerCustomEditor(AdditionalFee.class, new AbstractModelEditor(AdditionalFee.class));
		binder.registerCustomEditor(DumpsterSize.class, new AbstractModelEditor(DumpsterSize.class));
		binder.registerCustomEditor(CityFee.class, new AbstractModelEditor(CityFee.class));
		binder.registerCustomEditor(PaymentMethodType.class, new AbstractModelEditor(PaymentMethodType.class));
		binder.registerCustomEditor(OrderFees.class, new AbstractModelEditor(OrderFees.class));
		
		super.initBinder(binder);
	}
	
	public void setupCreate(ModelMap model, HttpServletRequest request, Order order) {
		setupCreate(model, request);
		
		String dumpsterQuery = "select obj from Dumpster obj where obj.status.status='Available' order by obj.id asc";
		List<Dumpster> dumpsterInfoList = genericDAO.executeSimpleQuery(dumpsterQuery);
		
		if (order == null) {
			model.addAttribute("orderDumpsters", dumpsterInfoList);
			return;
		}
		
		if (order.getDumpster() != null && order.getDumpster().getId() != null) {
			List<Dumpster> assignedDumpsterList = genericDAO.executeSimpleQuery("select obj from Dumpster obj where obj.id=" + order.getDumpster().getId());
			dumpsterInfoList.add(assignedDumpsterList.get(0));
		}
		
		model.addAttribute("orderDumpsters", dumpsterInfoList);
			
		Long materialCategoryId = null;
		if (order.getMaterialType().getMaterialCategory() == null) {
			Long materialTypeId = order.getMaterialType().getId();
			String materialTypeQuery = "select obj from MaterialType obj where obj.id=" + materialTypeId;
			List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(materialTypeQuery);
			materialCategoryId = materialTypeList.get(0).getMaterialCategory().getId();
		} else {
			materialCategoryId = order.getMaterialType().getMaterialCategory().getId();
		}
		
		Long dumsterSizeId = order.getDumpsterSize().getId();
		
		List<MaterialCategory> materialCategoryList = retrieveMaterialCategories(dumsterSizeId);
		model.addAttribute("materialCategories", materialCategoryList);
		
		List<MaterialType> materialTypeList = retrieveMaterialTypes(dumsterSizeId, materialCategoryId);
		model.addAttribute("materialTypes", materialTypeList);
		
		/*BigDecimal totalAmountPaid = new BigDecimal(0.00);
		if (order.getOrderPayment() != null) {
			for (OrderPayment anOrderPayment : order.getOrderPayment()) {
				totalAmountPaid = totalAmountPaid.add(anOrderPayment.getAmountPaid());
			}
			
			order.setTotalAmountPaid(totalAmountPaid);
			
			BigDecimal balanceDue = order.getOrderFees().getTotalFees().subtract(totalAmountPaid);
			order.setBalanceAmountDue(balanceDue);
		}*/
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
      //model.addAttribute("deliveryAddresses", genericDAO.findByCriteria(DeliveryAddress.class, criterias, "line1", false));
      
      //model.addAttribute("permits", genericDAO.executeSimpleQuery("select obj from Permit obj where obj.id!=0 order by obj.id asc"));
      //model.addAttribute("permits", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
      
		model.addAttribute("dumpsters", genericDAO.executeSimpleQuery("select obj from Dumpster obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("dumpsterSizes", genericDAO.executeSimpleQuery("select obj from DumpsterSize obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("dusmpsterLocationTypes", genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("permitClasses", genericDAO.executeSimpleQuery("select obj from PermitClass obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("permitTypes", genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("additionalFeeTypes", genericDAO.executeSimpleQuery("select obj from AdditionalFee obj where obj.id!=0 order by obj.id asc"));
     
      //model.addAttribute("materialCategories", genericDAO.executeSimpleQuery("select obj from MaterialCategory obj where obj.id!=0 order by obj.id asc"));
      //model.addAttribute("materialTypes", genericDAO.executeSimpleQuery("select obj from MaterialType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("cityFeeDetails", genericDAO.executeSimpleQuery("select obj from CityFee obj where obj.id!=0 order by obj.id asc"));
      
      populateDeliveryTimeSettings(model);
      
      String driverRole = "DRIVER";
      List<BaseModel> driversList = genericDAO.executeSimpleQuery("select obj from User obj where obj.id!=0 and obj.accountStatus=1 and obj.role.name='" + driverRole + "' order by obj.id asc");
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
		
		if (criteria.getSearchMap().get("customer") != null) {
			String customerId = (String)criteria.getSearchMap().get("customer");
			if (StringUtils.isNotEmpty(customerId)) {
				String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.customer.id=" + customerId  + " order by obj.line1 asc";
				model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
			}
	   }
		
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
		setupCreate(model, request, null);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		//return urlContext + "/form";
		
		model.addAttribute("notesModelObject", new OrderNotes());
		
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/orderNotesCreateModal.do")
	public String orderNotesCreateModal(ModelMap model, HttpServletRequest request,
												   @RequestParam(value = "id") Long orderPermitId) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("id", orderPermitId);
		OrderPermits orderPermit = genericDAO.findByCriteria(OrderPermits.class, criterias, "id", false).get(0);
		Long orderId = orderPermit.getOrder().getId();
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		return urlContext + "/notesModal";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveOrderNotesModal.do")
	public @ResponseBody String saveOrderNotesModal(HttpServletRequest request,
			@ModelAttribute("notesModelObject") OrderNotes entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request, null);
			return urlContext + "/notesModal";
		}
		
		//beforeSave(request, entity, model);
		if (entity instanceof AbstractBaseModel) {
			AbstractBaseModel baseModel = (AbstractBaseModel) entity;
			if (baseModel.getId() == null) {
				baseModel.setCreatedAt(Calendar.getInstance().getTime());
				if (baseModel.getCreatedBy() == null) {
					baseModel.setCreatedBy(getUser(request).getId());
				}
			} else {
				baseModel.setModifiedAt(Calendar.getInstance().getTime());
				if (baseModel.getModifiedBy() == null) {
					baseModel.setModifiedBy(getUser(request).getId());
				}
			}
		}
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		return "Saved successfully";
		
		/*Long orderId = entity.getOrder().getId();
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		return urlContext + "/notesModal";*/
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
			setupCreate(model, request, null);
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
		
		Long orderId = entity.getOrder().getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.id=" + orderId);
		Order savedOrder = orderList.get(0);
		model.addAttribute("modelObject", savedOrder);

		setupCreate(model, request, savedOrder);
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderNotesTab");
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from DeliveryAddress obj where obj.customer.id=" +  savedOrder.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(savedOrder);
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}

	private List<List<Permit>> retrievePermitsOfChosenType(Order order) {
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		
		for(Permit aChosenPermit : order.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId = aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId = aChosenPermit.getPermitType().getId().toString();
				String aChosenPermitCustomerId = aChosenPermit.getCustomer().getId().toString();
				String aChosenPermitDeliveryAddressId = aChosenPermit.getDeliveryAddress().getId().toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String deliveryDateStr = formatter.format(order.getDeliveryDate());
				
				List<Permit> aPermitOfChosenTypeList = retrievePermit(aChosenPermitCustomerId, 
						aChosenPermitDeliveryAddressId, aChosenPermitClassId,  aChosenPermitTypeId, deliveryDateStr);
					
				aPermitOfChosenTypeList.add(aChosenPermit);
				
				allPermitsOfChosenTypesList.add(aPermitOfChosenTypeList);
			}
		}
		
		return allPermitsOfChosenTypesList;
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
			setupCreate(model, request, entity);
			return urlContext + "/form";
		}
		
		entity.setModifiedAt(Calendar.getInstance().getTime());
		entity.setModifiedBy(getUser(request).getId());
		
		//beforeSave(request, entity, model);
		
		OrderStatus orderStatus = retrieveOrderStatus("Dropped Off");
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		updateDumpsterStatus(entity.getDumpster(), "Dropped Off", getUser(request).getId());
		
		cleanUp(request);

		setupCreate(model, request, entity);
		
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
		
		String query = "select obj from DeliveryAddress obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}
	
	private void updatePermitStatus(List<Permit> permitList, String status, Long modifiedBy) {
		String permitStatusQuery = "select obj from PermitStatus obj where obj.status='" + status + "'";
		List<PermitStatus> permitStatusList = genericDAO.executeSimpleQuery(permitStatusQuery);
		
		for (Permit aPermit : permitList) {
			aPermit.setStatus(permitStatusList.get(0));
			aPermit.setModifiedAt(Calendar.getInstance().getTime());
			aPermit.setModifiedBy(modifiedBy);
			
			genericDAO.saveOrUpdate(aPermit);
		}
	}
	
	private void updateDumpsterStatus(Dumpster dumpsterInfoPassed, String status, Long modifiedBy) {
		String dumpsterStatusQuery = "select obj from DumpsterStatus obj where obj.status='" + status + "'";
		List<DumpsterStatus> dumpsterStatusList = genericDAO.executeSimpleQuery(dumpsterStatusQuery);
		
		String dumpsterQuery = "select obj from Dumpster obj where obj.id=" + dumpsterInfoPassed.getId();
		List<Dumpster> dumpsterInfoList = genericDAO.executeSimpleQuery(dumpsterQuery);
		Dumpster dumpsterInfo = dumpsterInfoList.get(0);
		
		dumpsterInfo.setStatus(dumpsterStatusList.get(0));
		dumpsterInfo.setModifiedAt(Calendar.getInstance().getTime());
		dumpsterInfo.setModifiedBy(modifiedBy);
		
		genericDAO.saveOrUpdate(dumpsterInfo);
	}
	
	private OrderStatus retrieveOrderStatus(String status) {
		OrderStatus orderStatus = (OrderStatus)genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.status='" + status + "'").get(0);
		return orderStatus;
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
			setupCreate(model, request, entity);
			return urlContext + "/form";
		}
		
		//beforeSave(request, entity, model);
		entity.setModifiedAt(Calendar.getInstance().getTime());
		entity.setModifiedBy(getUser(request).getId());
		
		//TODO: why is this reqd?
		//setupOrderFees(entity);
		
		OrderStatus orderStatus = retrieveOrderStatus("Picked Up");
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = getUser(request).getId();
		updateDumpsterStatus(entity.getDumpster(), "Available", modifiedBy);
		updatePermitStatus(entity.getPermits(), "Available", modifiedBy);
		
		cleanUp(request);

		setupCreate(model, request, entity);
		
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
		
		String query = "select obj from DeliveryAddress obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
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
		
		/*if (criteria.getSearchMap().get("customer") != null) {
			String customerId = (String)criteria.getSearchMap().get("customer");
			String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.customer.id=" + customerId  + " order by obj.line1 asc";
			model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
	   }*/
		
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
				response.setHeader("Content-Disposition", "attachment;filename= ordersReport." + type);
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
				map.put("line2", order.getDeliveryAddress().getLine2());
				map.put("status", order.getOrderStatus().getStatus());
				
				if (order.getDeliveryDate() != null) {
					map.put("deliveryDate", order.getDeliveryDate().toString());
				}
				
				if (order.getPickupDate() != null) {
					map.put("pickupDate", order.getPickupDate().toString());
				}
				
				List<OrderPayment> orderPaymentList = order.getOrderPayment();
				if (orderPaymentList != null && !orderPaymentList.isEmpty()) {
					OrderPayment anOrderPayment = orderPaymentList.get(0);
					map.put("paymentMethod", StringUtils.defaultIfEmpty(anOrderPayment.getPaymentMethod().getMethod(), StringUtils.EMPTY));
				}
				
				OrderFees orderFees = order.getOrderFees();
				if (orderFees != null) {
					map.put("dumpsterPrice", StringUtils.defaultIfEmpty(orderFees.getDumpsterPrice().toString(), "0.00"));
					map.put("cityFee", StringUtils.defaultIfEmpty(orderFees.getCityFee().toString(), "0.00"));
					map.put("permitFees", StringUtils.defaultIfEmpty(orderFees.getTotalPermitFees().toString(), "0.00"));
					map.put("overweightFee", StringUtils.defaultIfEmpty(orderFees.getOverweightFee().toString(), "0.00"));
					map.put("totalFees", StringUtils.defaultIfEmpty(orderFees.getTotalFees().toString(), "0.00"));
				}
				
				newList.add(map);
			}
			
			out = dynamicReportService.generateStaticReport("ordersReport", newList, params, type, request);
			
			/*if (!type.equals("print") && !type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("ordersReport", newList, params, type, request);
			}
			else if(type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("ordersReportPdf",
						newList, params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("ordersReport"+"print",
						newList, params, type, request);
			}*/
		
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
															 @RequestParam(value = "customerId", required = false) String customerId,
															 @RequestParam(value = "deliveryAddressId", required = false) String deliveryAddressId,
														    @RequestParam(value = "permitId", required = false) String permitId,
															 @RequestParam(value = "permitClassId", required = false) String permitClassId,
															 @RequestParam(value = "permitTypeId", required = false) String permitTypeId,
															 @RequestParam(value = "deliveryDate", required = false) String deliveryDate) {
		List<Permit> permitList = new ArrayList<Permit>();
		
		if (StringUtils.isNotEmpty(permitId)) {
			permitList = retrievePermit(permitId); 
		} else {
			permitList = retrievePermit(customerId, deliveryAddressId, permitClassId, permitTypeId, deliveryDate);
		}
		
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
	
	private List<Permit> retrievePermit(String permitId) {
		String permitsQuery = "select obj from Permit obj where ";
		permitsQuery += "obj.id=" + permitId;
		
		List<Permit> permits = genericDAO.executeSimpleQuery(permitsQuery);
		Permit aPermit = permits.get(0);
		if ("Available".equals(aPermit.getStatus().getStatus())) {
			aPermit.setFee(new BigDecimal(0.00));
		}
		return permits;
	}
	
	private List<Permit> retrievePermit(String customerId, String deliveryAddressId, String permitClassId, String permitTypeId, String deliveryDateStr) {
		String requiredEndDateStr = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(deliveryDateStr)) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try {
				String permitTypeQuery = "select obj from PermitType obj where obj.id="+ permitTypeId;
				List<PermitType> requestedPermitTypes = genericDAO.executeSimpleQuery(permitTypeQuery);
				
				String requestedPermitDaysTokens[] = requestedPermitTypes.get(0).getPermitType().split("\\s");
				int requestedPermitDays = new Integer(requestedPermitDaysTokens[0]);
				
				Date deliveryDate = formatter.parse(deliveryDateStr);
				Date requiredEndDate = DateUtils.addDays(deliveryDate, requestedPermitDays);
				
				//2015-10-03 00:00:00.0
				formatter.applyPattern("yyyy-MM-dd 00:00:00.0");
				requiredEndDateStr = formatter.format(requiredEndDate);
			} catch (ParseException pe) {
				//TODO: handle error
				pe.printStackTrace();
			}
		}
		
		String permitsQuery = "select obj from Permit obj where";
		permitsQuery += " obj.customer.id=" + customerId
						 +  " and obj.deliveryAddress.id=" + deliveryAddressId
				    	 +  " and obj.permitClass.id=" + permitClassId
				    	 +  " and obj.permitType.id=" + permitTypeId
				    	 +  " and obj.endDate >= '" + requiredEndDateStr + "'"
				    	 +  " and obj.status.status=";
		
		List<Permit> availablePermits = genericDAO.executeSimpleQuery(permitsQuery + "'Available'");
		//return availablePermits;
		
		List<Permit> pendingPermits = genericDAO.executeSimpleQuery(permitsQuery + "'Pending'");
		if (pendingPermits.isEmpty()) {
			return availablePermits;
		}
		
		StringBuffer pendingPermitIdsBuff = new StringBuffer();
		Map<Long, Permit> pendingPermitsMap = new HashMap<Long, Permit>();
		for (Permit aPendingPermit : pendingPermits) {
			pendingPermitIdsBuff.append(aPendingPermit.getId().toString() + " ,");
			pendingPermitsMap.put(aPendingPermit.getId(), aPendingPermit);
		}
		
		String pendingPermitIds = pendingPermitIdsBuff.substring(0, (pendingPermitIdsBuff.length() - 2));
		String orderPermitsQuery = "select obj from OrderPermits obj where obj.id in (" 
											+ pendingPermitIds
											+ ")";
		List<OrderPermits> orderPermitsList = genericDAO.executeSimpleQuery(orderPermitsQuery);
		for (OrderPermits aOrderPermit : orderPermitsList) {
			pendingPermitsMap.remove(aOrderPermit.getPermit().getId());
		}
		
		Collection<Permit> filteredPendingPermits = pendingPermitsMap.values();
		
		availablePermits.addAll(filteredPendingPermits);
		return availablePermits;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveDumpsterPrice.do")
	public @ResponseBody String retrieveDumpsterPrice(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") String dumpsterSizeId,
															 @RequestParam(value = "materialTypeId") String materialTypeId) {
		BigDecimal dumpsterPrice = retrieveDumpsterPrice(dumpsterSizeId, materialTypeId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(dumpsterPrice);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private BigDecimal retrieveDumpsterPrice(String dumpsterSizeId, String materialTypeId) {
		String dumpsterPriceQuery = "select obj from DumpsterPrice obj where";
		dumpsterPriceQuery += " obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 +  " and obj.materialType.id=" + materialTypeId;
		
		List<DumpsterPrice> dumsterPriceList = genericDAO.executeSimpleQuery(dumpsterPriceQuery);
		return dumsterPriceList.get(0).getPrice();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveMaterialCategories.do")
	public @ResponseBody String retrieveMaterialCategories(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") Long dumpsterSizeId) {
		List<MaterialCategory> materialCategories = retrieveMaterialCategories(dumpsterSizeId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(materialCategories);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private List<MaterialCategory> retrieveMaterialCategories(Long dumpsterSizeId) {
		String dumpsterPriceQuery = "select distinct obj.materialType.materialCategory from DumpsterPrice obj where";
		dumpsterPriceQuery += " obj.dumpsterSize.id=" + dumpsterSizeId;
		List<MaterialCategory> materialCategories = genericDAO.executeSimpleQuery(dumpsterPriceQuery);
		return materialCategories;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveMaterialTypes.do")
	public @ResponseBody String retrieveMaterialTypes(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") Long dumpsterSizeId,
															 @RequestParam(value = "materialCategoryId") Long materialCategoryId) {
		List<MaterialType> materialTypes = retrieveMaterialTypes(dumpsterSizeId, materialCategoryId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(materialTypes);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private List<MaterialType> retrieveMaterialTypes(Long dumpsterSizeId, Long materialCategoryId) {
		String dumpsterPriceQuery = "select obj.materialType from DumpsterPrice obj where";
		dumpsterPriceQuery += " obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 +  " and obj.materialType.materialCategory.id=" + materialCategoryId;
		List<MaterialType> materialTypes = genericDAO.executeSimpleQuery(dumpsterPriceQuery);
		return materialTypes;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveCityFee.do")
	public @ResponseBody String retrieveCityFee(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "cityFeeId") String cityFeeId) {
		BigDecimal cityFee = retrieveCityFee(cityFeeId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(cityFee);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrievePermitClass.do")
	public @ResponseBody String retrievePermitClass(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") String dumpsterSizeId) {
		List<PermitClass> permitClassList = retrievePermitClass(dumpsterSizeId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(permitClassList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private BigDecimal retrieveCityFee(String cityFeeId) {
		String cityFeeQuery = "select obj from CityFee obj where ";
		cityFeeQuery += "obj.id=" + cityFeeId;
		
		List<CityFee> cityFeeList = genericDAO.executeSimpleQuery(cityFeeQuery);
		return cityFeeList.get(0).getFee();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveOverweightFee.do")
	public @ResponseBody String retrieveOverweightFee(ModelMap model, HttpServletRequest request,
			 													     @RequestParam(value = "dumpsterSizeId") String dumpsterSizeId,
			 													     @RequestParam(value = "materialCategoryId") String materialCategoryId,
			 													     @RequestParam(value = "netWeightTonnage") String netWeightTonnage) {
		BigDecimal cityFee = retrieveOverweightFee(dumpsterSizeId, materialCategoryId, netWeightTonnage);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(cityFee);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private BigDecimal retrieveOverweightFee(String dumpsterSizeId, String materialCategoryId, String netWeightTonnage) {
		String overweightFeeQuery = "select obj from OverweightFee obj where ";
		overweightFeeQuery += "obj.dumpsterSize.id=" + dumpsterSizeId
								 +  " and obj.materialCategory.id=" + materialCategoryId
								 +  " and obj.tonLimit<" + netWeightTonnage;
		
		List<OverweightFee> overweightFeeList = genericDAO.executeSimpleQuery(overweightFeeQuery);
		if (overweightFeeList.isEmpty()) {
			return new BigDecimal(0.0);
		} else {
			OverweightFee overweightFee = overweightFeeList.get(0);
			
			BigDecimal requestedWeight = new BigDecimal(netWeightTonnage);
			BigDecimal differentialWeight = requestedWeight.subtract(overweightFee.getTonLimit());
			return differentialWeight.multiply(overweightFee.getFee());
		}
	}
	
	private List<PermitClass> retrievePermitClass(String dumpsterSizeId) {
		String permitClassQuery = "select obj from DumpsterSize obj where ";
		permitClassQuery += "obj.size.id=" + dumpsterSizeId;
		
		List<DumpsterSize> dumpsterSizeList = genericDAO.executeSimpleQuery(permitClassQuery);
		
		List<PermitClass> permitClassList = new ArrayList<PermitClass>();
		permitClassList.add(dumpsterSizeList.get(0).getPermitClass());
		
		return permitClassList;
	}
	
	public void setupUpdate(ModelMap model, HttpServletRequest request, Order order) {
		setupCreate(model, request, order);
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		Order orderToBeEdited = (Order)model.get("modelObject");
		
		setupUpdate(model, request, orderToBeEdited);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		
		String query = "select obj from DeliveryAddress obj where obj.customer.id=" +  orderToBeEdited.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
    	
		Order emptyOrder = new Order();
		emptyOrder.setId(orderToBeEdited.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(orderToBeEdited);
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
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveMatchingOrder.do")
	public @ResponseBody String retrieveMatchingOrder(ModelMap model, HttpServletRequest request,
																	  @RequestParam("customerId") String customerId,
																	  @RequestParam("deliveryAddressId") String deliveryAddressId) {
		String query = "select obj from Order obj where obj.customer.id=" + customerId
				+ " and obj.deliveryAddress.id=" + deliveryAddressId
				+ " and obj.dumpster.dumpsterNum is not null";
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		
		String existingDroppedOffOrderId = StringUtils.EMPTY;
		if (orderList.size() > 0) {
			existingDroppedOffOrderId = orderList.get(0).getId().toString();
		}
		
		return existingDroppedOffOrderId;
		
		//String json = (new Gson()).toJson(customerList.get(0));
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
	
		String isExchange = request.getParameter("isExchange");
		if (BooleanUtils.toBoolean(isExchange)) {
			String existingDroppedOffOrderId = request.getParameter("existingDroppedOffOrderId");
			entity.setPickupOrderId(new Long(existingDroppedOffOrderId));
		}
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request, entity);
			return urlContext + "/order";
		}
		
		beforeSave(request, entity, model);
		
		StringBuffer permitIdsBuff = new StringBuffer();
		for (Permit aPermit : entity.getPermits()) {
			if (aPermit != null && aPermit.getId() != null) {
				permitIdsBuff.append(aPermit.getId().toString() + ", ");
			}
		}
		
		// TODO: why is permit updating even when not changed? Changeto list of order permits instead?
		// TODO: create/modified date not updated
		String permitIds = permitIdsBuff.substring(0, (permitIdsBuff.length() - 2));
		List<Permit> permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.id in (" 
																					+ permitIds
																					+ ")");
		entity.setPermits(permitList);
		
		OrderStatus orderStatus = retrieveOrderStatus("Open");
		entity.setOrderStatus(orderStatus);
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupOrderFees(entity);
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupOrderNotes(entity);
		
		setupOrderPayment(entity);

		genericDAO.saveOrUpdate(entity);
		
		updatePermitStatus(permitList, "Assigned", getUser(request).getId());
		
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
	
	private void setupOrderPayment(Order order) {
		order.setTotalAmountPaid(new BigDecimal(0.00));
		order.setBalanceAmountDue(new BigDecimal(0.00));
		
		List<OrderPayment> orderPaymentList = order.getOrderPayment();
		if (orderPaymentList == null || orderPaymentList.isEmpty()) {
			return;
		}
		
		BigDecimal totalAmountPaid = new BigDecimal(0.00);
		List<OrderPayment> filteredOrderPaymentList = new ArrayList<OrderPayment>();
		for (OrderPayment anOrderPayment : orderPaymentList) {
			if (anOrderPayment.getPaymentMethod() != null) {
				anOrderPayment.setOrder(order);
				anOrderPayment.setCreatedAt(order.getCreatedAt());
				anOrderPayment.setCreatedBy(order.getCreatedBy());
				//anOrderPayment.setModifiedAt(order.getModifiedAt());
				//anOrderPayment.setModifiedBy(order.getModifiedBy());
				
				filteredOrderPaymentList.add(anOrderPayment);
				
				totalAmountPaid = totalAmountPaid.add(anOrderPayment.getAmountPaid());
			}
		}
		
		order.setOrderPayment(filteredOrderPaymentList);
		
		order.setTotalAmountPaid(totalAmountPaid);
		
		BigDecimal balanceDue = order.getOrderFees().getTotalFees().subtract(totalAmountPaid);
		order.setBalanceAmountDue(balanceDue);
	}
	
	private void setupOrderFees(Order order) {
		OrderFees orderFees = order.getOrderFees();
		
		orderFees.setOrder(order);
		orderFees.setCreatedBy(order.getCreatedBy());
		orderFees.setCreatedAt(order.getCreatedAt());
		//orderFees.setModifiedBy(order.getModifiedBy());
		//orderFees.setModifiedAt(order.getModifiedAt());
		
		/*if (orderFees.getAdditionalFee1() == null) {
			orderFees.setAdditionalFee1(new BigDecimal(0.00));
		}
		if (orderFees.getAdditionalFee2() == null) {
			orderFees.setAdditionalFee2(new BigDecimal(0.00));
		}
		if (orderFees.getAdditionalFee3() == null) {
			orderFees.setAdditionalFee3(new BigDecimal(0.00));
		}*/
		if (orderFees.getTotalAdditionalFees() == null) {
			orderFees.setTotalAdditionalFees(new BigDecimal(0.00));
		}
		if (orderFees.getCityFee() == null) {
			orderFees.setCityFee(new BigDecimal(0.00));
		}
		if (orderFees.getDiscountPercentage() == null) {
			orderFees.setDiscountPercentage(new BigDecimal(0.00));
		}
		if (orderFees.getDiscountAmount() == null) {
			orderFees.setDiscountAmount(new BigDecimal(0.00));
		}
		if (orderFees.getOverweightFee() == null) {
			orderFees.setOverweightFee(new BigDecimal(0.00));
		}
		/*if (orderFees.getPermitFee1() == null) {
			orderFees.setPermitFee1(new BigDecimal(0.00));
		}
		if (orderFees.getPermitFee2() == null) {
			orderFees.setPermitFee2(new BigDecimal(0.00));
		}
		if (orderFees.getPermitFee3() == null) {
			orderFees.setPermitFee3(new BigDecimal(0.00));
		}*/
		if (orderFees.getTotalPermitFees() == null) {
			orderFees.setTotalPermitFees(new BigDecimal(0.00));
		}
	}
	
	private void setupOrderNotes(Order order) {
		List<OrderNotes> orderNotesList = order.getOrderNotes();
		if (order.getId() != null) {
			// First notes should not be editable
			/*if (orderNotesList != null) {
				orderNotesList.clear();
			}*/
			return;
		}
		
		if (orderNotesList == null || orderNotesList.isEmpty()) {
			return;
		}
		
		OrderNotes anOrderNotes = orderNotesList.get(0);
		if (StringUtils.isEmpty(anOrderNotes.getNotes())) {
			orderNotesList.clear();
			return;
		}
		
		anOrderNotes.setOrder(order);
		anOrderNotes.setCreatedBy(order.getCreatedBy());
		anOrderNotes.setCreatedAt(order.getCreatedAt());
		//anOrderNotes.setModifiedBy(order.getModifiedBy());
		//anOrderNotes.setModifiedAt(order.getModifiedAt());
	}
	
	/*@Override
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		String dataQualifier = request.getParameter("dataQualifier");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html"))
			response.setHeader("Content-Disposition", "attachment;filename="
					+ urlContext + "Report." + type);
		try {
			criteria.setPageSize(100000);
			//String label = getCriteriaAsString(criteria);
			ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(),
					columnPropertyList, criteria, request);
			out.writeTo(response.getOutputStream());
			if (type.equals("html"))
				response.getOutputStream()
						.println(
								"<script language=\"javascript\">window.print()</script>");
			criteria.setPageSize(25);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		}
	}*/
	
	public String saveSuccess(ModelMap model, HttpServletRequest request, Order entity) {
		setupCreate(model, request, entity);
		
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
		
		String query = "select obj from DeliveryAddress obj where obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
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