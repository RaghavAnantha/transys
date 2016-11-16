package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import com.transys.model.vo.OrderReportVO;
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
		
		String dumpsterQuery = "select obj from Dumpster obj where obj.deleteFlag='1' and obj.status.status='Available'";
		Dumpster assignedDumpster = null;
		Long currentlyAssignedDumpsterId = -1l;
		if (order != null) {
			dumpsterQuery += " and obj.dumpsterSize.id=" + order.getDumpsterSize().getId();
		
			if (order.getDumpster() != null && order.getDumpster().getId() != null) {
				currentlyAssignedDumpsterId = order.getDumpster().getId();
				
				List<Dumpster> assignedDumpsterList = genericDAO.executeSimpleQuery("select obj from Dumpster obj where obj.deleteFlag='1' and obj.id=" + order.getDumpster().getId());
				assignedDumpster = assignedDumpsterList.get(0);
			}
		}
		dumpsterQuery += " order by obj.id asc";
				
		List<Dumpster> dumpsterInfoList = genericDAO.executeSimpleQuery(dumpsterQuery);
		
		if (assignedDumpster != null) {
			dumpsterInfoList.add(assignedDumpster);
		}
		
		model.addAttribute("orderDumpsters", dumpsterInfoList);
		
		model.addAttribute("currentlyAssignedDumpsterId", currentlyAssignedDumpsterId);
		
		if (order == null) {
			return;
		}
			
		Long materialCategoryId = null;
		if (order.getMaterialType().getMaterialCategory() == null) {
			Long materialTypeId = order.getMaterialType().getId();
			String materialTypeQuery = "select obj from MaterialType obj where obj.deleteFlag='1' and obj.id=" + materialTypeId;

			List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(materialTypeQuery);
			materialCategoryId = materialTypeList.get(0).getMaterialCategory().getId();
			order.setMaterialType(materialTypeList.get(0));
		} else {
			materialCategoryId = order.getMaterialType().getMaterialCategory().getId();
		}
		
		Long dumpsterSizeId = order.getDumpsterSize().getId();
		
		List<MaterialCategory> materialCategoryList = retrieveMaterialCategories(dumpsterSizeId);
		model.addAttribute("materialCategories", materialCategoryList);
		
		List<MaterialType> materialTypeList = retrieveMaterialTypes(dumpsterSizeId, materialCategoryId);
		model.addAttribute("materialTypes", materialTypeList);
	
		List<PermitClass> permitClassList = new ArrayList<PermitClass>();
		List<Permit> permits = order.getPermits();
		if (permits != null && !permits.isEmpty()) {
			permitClassList.add(permits.get(0).getPermitClass());
		} else {
			permitClassList = retrievePermitClass(dumpsterSizeId);
		}
		model.addAttribute("permitClasses", permitClassList);
   }
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		model.addAttribute("orderIds", genericDAO.executeSimpleQuery("select obj.id from Order obj where obj.deleteFlag='1' order by obj.id asc"));
		
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.deleteFlag='1' order by obj.id asc");
		//model.addAttribute("order", orderList);
		
		SortedSet<String> contactNameSet = new TreeSet<String>();
		SortedSet<String> phoneSet = new TreeSet<String>();
		for (Order anOrder : orderList) {
			phoneSet.add(anOrder.getDeliveryContactPhone1());
			contactNameSet.add(anOrder.getDeliveryContactName());
		}
		
		String[] phoneArr = phoneSet.toArray(new String[0]);
		String[] contactNameArr = contactNameSet.toArray(new String[0]);
		
		model.addAttribute("deliveryContactPhones", phoneArr);
		model.addAttribute("deliveryContactNames", contactNameArr);
		
		Map criterias = new HashMap();
		
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("customers", genericDAO.findByCriteria(Customer.class, criterias, "companyName", false));
      
		model.addAttribute("dumpsters", genericDAO.executeSimpleQuery("select obj from Dumpster obj where obj.deleteFlag='1' and obj.id!=0 order by obj.id asc"));
      model.addAttribute("dumpsterSizes", genericDAO.executeSimpleQuery("select obj from DumpsterSize obj where obj.deleteFlag='1' and obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("dusmpsterLocationTypes", genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.locationType asc"));
      
      //model.addAttribute("permitClasses", genericDAO.executeSimpleQuery("select obj from PermitClass obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("permitTypes", genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("additionalFeeTypes", genericDAO.executeSimpleQuery("select obj from AdditionalFee obj where obj.deleteFlag='1' and obj.id!=0 order by obj.description asc"));
     
      model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.method asc"));
      
      model.addAttribute("cityFeeDetails", genericDAO.executeSimpleQuery("select obj from CityFee obj where obj.deleteFlag='1' and obj.id!=0 order by obj.suburbName asc"));
      		
      populateDeliveryTimeSettings(model);
      
      String driverRole = "DRIVER";
      List<User> driversList = genericDAO.executeSimpleQuery("select obj from User obj where obj.deleteFlag='1' and obj.id!=0 and obj.accountStatus=1 and obj.role.name='" + driverRole + "' order by obj.employee.firstName asc");
      model.addAttribute("drivers", driversList);
	}
	
	private void populateDeliveryTimeSettings(ModelMap model) {
		List<String> deliveryHours = new ArrayList<String>();
		deliveryHours.add("--");
		
		for (int i = 1; i <= 12; i++) {
			deliveryHours.add(i + " AM");
		}
		for (int i = 1; i <= 12; i++) {
			deliveryHours.add(i + " PM");
		}
      
      model.addAttribute("deliveryHours", deliveryHours);
      
      /*List<String> deliveryMinutes = new ArrayList<String>();
      deliveryMinutes.add("--");
      deliveryMinutes.add("00");
      deliveryMinutes.add("15");
      
      model.addAttribute("deliveryMinutes", deliveryMinutes);*/
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		/*if (criteria.getSearchMap().get("customer") != null) {
			String customerId = (String)criteria.getSearchMap().get("customer");
			if (StringUtils.isNotEmpty(customerId)) {
				String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" + customerId  + " order by obj.line1 asc";
				model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
			}
	   }*/
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "deliveryDate desc, orderStatus.status desc", null, null));
		
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
		
		List<Order> orderList =  genericDAO.search(getEntityClass(), criteria,"id", null, null);
		
		model.addAttribute("orderReportList", orderList);
		model.addAttribute("activeTab", "orderReports");
		model.addAttribute("mode", "MANAGE");
		
		String orderDateFrom = criteria.getSearchMap().get("createdAtFrom").toString();
		String orderDateTo = criteria.getSearchMap().get("createdAtTo").toString();
		
		if (orderList != null && !orderList.isEmpty()) {
			orderDateFrom = StringUtils.defaultIfEmpty(orderDateFrom, orderList.get(0).getFormattedCreatedAt());
			orderDateTo = StringUtils.defaultIfEmpty(orderDateTo, orderList.get(orderList.size() - 1).getFormattedCreatedAt());
		}
		
		model.addAttribute("orderDateFrom", orderDateFrom);
		model.addAttribute("orderDateTo", orderDateTo);
		
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
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  orderId + " order by obj.id asc");
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
		
		entity.setNotesType(OrderNotes.NOTES_TYPE_USER);
		updateEnteredBy(entity);
		
		genericDAO.saveOrUpdate(entity);
		
		cleanUp(request);
		
		return "Order notes saved successfully";
		
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
		
		entity.setNotesType(OrderNotes.NOTES_TYPE_USER);
		updateEnteredBy(entity);
		
		genericDAO.saveOrUpdate(entity);
		
		cleanUp(request);
		
		Long orderId = entity.getOrder().getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId);
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
	
		List<OrderNotes> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  savedOrder.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(savedOrder);
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}

	private void updateEnteredBy(OrderNotes entity) {
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getEmployee().getFullName());
	}
	
	private List<List<Permit>> retrievePermitsOfChosenType(Order order) {
		List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
		if (order.getPermits() == null || order.getPermits().isEmpty()) {
			return allPermitsOfChosenTypesList;
		}
		
		for(Permit aChosenPermit : order.getPermits()) {
			if (aChosenPermit != null && aChosenPermit.getId() != null) {
				String aChosenPermitClassId = aChosenPermit.getPermitClass().getId().toString();
				String aChosenPermitTypeId = aChosenPermit.getPermitType().getId().toString();
				String aChosenPermitCustomerId = aChosenPermit.getCustomer().getId().toString();
				String aChosenPermitDeliveryAddressId = aChosenPermit.getDeliveryAddress().getId().toString();
				String aChosenPermitLocationTypeId = aChosenPermit.getLocationType().getId().toString();
				
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				String deliveryDateStr = formatter.format(order.getDeliveryDate());
				
				List<Permit> aPermitOfChosenTypeList = retrievePermit(aChosenPermitCustomerId, 
						aChosenPermitDeliveryAddressId, aChosenPermitClassId,  aChosenPermitTypeId, deliveryDateStr, aChosenPermitLocationTypeId);
					
				aPermitOfChosenTypeList.add(aChosenPermit);
				
				allPermitsOfChosenTypesList.add(aPermitOfChosenTypeList);
			}
		}
		
		return allPermitsOfChosenTypesList;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveDropOffDriver.do")
	public String saveDropOffDriver(HttpServletRequest request,
			@RequestParam(value = "currentlyAssignedDumpsterId") Long currentlyAssignedDumpsterId,
			@ModelAttribute("modelObject") Order entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request, entity);
			return urlContext + "/form";
		}
		
		entity.setModifiedAt(Calendar.getInstance().getTime());
		entity.setModifiedBy(getUser(request).getId());
		
		//beforeSave(request, entity, model);
		
		String auditMsg = StringUtils.EMPTY;
		if (OrderStatus.ORDER_STATUS_OPEN.equals(entity.getOrderStatus().getStatus())) {
			auditMsg = "Order status changed to Drop Off";
			
			OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_DROPPED_OFF);
			entity.setOrderStatus(orderStatus);
			
			updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_DROPPED_OFF, getUser(request).getId());
		} else {
			auditMsg = "Order Drop Off details updated";
			
			if (!currentlyAssignedDumpsterId.equals(entity.getDumpster().getId())) {
				if (!OrderStatus.ORDER_STATUS_CLOSED.equals(entity.getOrderStatus().getStatus())) {
					updateDumpsterStatus(currentlyAssignedDumpsterId, DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, getUser(request).getId());
					updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_DROPPED_OFF, getUser(request).getId());
				}
			}
		}
		
		genericDAO.saveOrUpdate(entity);
		
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, auditMsg, entity.getModifiedBy());
		entity.getOrderNotes().add(auditOrderNotes);
		
		return dropOffSaveSuccess(request, entity, model);
	}
	
	private String dropOffSaveSuccess(HttpServletRequest request, Order entity, ModelMap model) {
		cleanUp(request);

		setupCreate(model, request, entity);
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "dropOffDriver");
		
		model.addAttribute("msgCtx", "manageDropOffDriver");
		model.addAttribute("msg", "Drop off data saved successfully");
		
		Long orderId = entity.getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId);
		model.addAttribute("modelObject", orderList.get(0));
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<OrderNotes> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}
	
	private void updatePermitStatus(List<Permit> permitList, String status, Long modifiedBy) {
		String permitStatusQuery = "select obj from PermitStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'";
		List<PermitStatus> permitStatusList = genericDAO.executeSimpleQuery(permitStatusQuery);
		
		for (Permit aPermit : permitList) {
			if (status.equals(aPermit.getStatus().getStatus())) {
				continue;
			}
			
			aPermit.setStatus(permitStatusList.get(0));
			aPermit.setModifiedAt(Calendar.getInstance().getTime());
			aPermit.setModifiedBy(modifiedBy);
			
			genericDAO.saveOrUpdate(aPermit);
		}
	}
	
	private void updateIfPermitsChanged(List<Permit> originallyAssignedPermits, List<Permit> currentlyAssignedPermits, Long modifiedBy) {
		if (originallyAssignedPermits == null || originallyAssignedPermits.isEmpty()) {
			return;
		}
		
		List<Permit> changedPermits = new ArrayList<Permit>();
		for (Permit anOriginallyAssignedPermit : originallyAssignedPermits) {
			boolean permitChanged = true;
			for (Permit aCurrentlyAssignedPermit : currentlyAssignedPermits) {
				if (anOriginallyAssignedPermit.getId() == aCurrentlyAssignedPermit.getId()) {
					permitChanged = false;
					break;
				}
			}
			
			if (permitChanged) { 
				changedPermits.add(anOriginallyAssignedPermit);
			}
		}
		
		if (!changedPermits.isEmpty()) {
			updatePermitStatus(changedPermits, "Available", modifiedBy);
		}
	}
	
	private void updateDumpsterStatus(Long dumpsterId, String status, Long modifiedBy) {
		String dumpsterStatusQuery = "select obj from DumpsterStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'";
		List<DumpsterStatus> dumpsterStatusList = genericDAO.executeSimpleQuery(dumpsterStatusQuery);
		
		String dumpsterQuery = "select obj from Dumpster obj where obj.deleteFlag='1' and obj.id=" + dumpsterId;
		List<Dumpster> dumpsterInfoList = genericDAO.executeSimpleQuery(dumpsterQuery);
		Dumpster dumpsterInfo = dumpsterInfoList.get(0);
		
		dumpsterInfo.setStatus(dumpsterStatusList.get(0));
		dumpsterInfo.setModifiedAt(Calendar.getInstance().getTime());
		dumpsterInfo.setModifiedBy(modifiedBy);
		
		genericDAO.saveOrUpdate(dumpsterInfo);
	}
	
	private OrderStatus retrieveOrderStatus(String status) {
		OrderStatus orderStatus = (OrderStatus)genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'").get(0);
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
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request, entity);
			return urlContext + "/form";
		}
		
		//beforeSave(request, entity, model);
		entity.setModifiedAt(Calendar.getInstance().getTime());
		entity.setModifiedBy(getUser(request).getId());
		
		//TODO: Why is this reqd?
		//setupOrderFees(entity);
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_CLOSED);
		entity.setOrderStatus(orderStatus);
		
		Long dumpsterSizeId = entity.getDumpsterSize().getId();
		Long materialCategoryId = entity.getMaterialType().getMaterialCategory().getId();
		BigDecimal netWeightTonnage = entity.getNetWeightTonnage();
		BigDecimal overweightFee = calculateOverweightFee(entity.getCreatedAt(), dumpsterSizeId, materialCategoryId, netWeightTonnage);
		overweightFee = overweightFee.setScale(2, RoundingMode.UP);
		
		OrderFees orderFees = entity.getOrderFees();
		orderFees.setOverweightFee(overweightFee);
		
		orderFees.setTotalFees(orderFees.getTotalFees().add(overweightFee));
		entity.setBalanceAmountDue(orderFees.getTotalFees().subtract(entity.getTotalAmountPaid()));
		
		genericDAO.saveOrUpdate(entity);
		
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, "Order status changed to Closed", entity.getModifiedBy());  
		entity.getOrderNotes().add(auditOrderNotes);
		
		Long modifiedBy = entity.getModifiedBy();
		updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, modifiedBy);
		updatePermitStatus(entity.getPermits(), PermitStatus.PERMIT_STATUS_AVAILABLE, modifiedBy);
		
		return pickupSaveSuccess(request, entity, model);
	}
	
	private String pickupSaveSuccess(HttpServletRequest request, Order entity, ModelMap model) {
		cleanUp(request);

		setupCreate(model, request, entity);
		
		//model.addAttribute("modelObject", entity);
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "pickupDriver");
		
		model.addAttribute("msgCtx", "managePickupDriver");
		model.addAttribute("msg", "Pickup data saved successfully");
		
		Long orderId = entity.getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId);
		model.addAttribute("modelObject", orderList.get(0));
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<OrderNotes> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
		
		List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
		model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		
		return urlContext + "/order";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/processOrderReadyForPickup.do")
	public @ResponseBody String processOrderReadyForPickup(ModelMap model, HttpServletRequest request,
															 @RequestParam(value = "orderId") String orderId,
															 @RequestParam(value = "readyForPickup") boolean readyForPickup) {
		String query = "select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId;
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		if (orderList.isEmpty()) {
			return "Specified Order not found";
		}
		
		Order order = orderList.get(0);
		OrderStatus currentOrderStatus = order.getOrderStatus();
		if (readyForPickup &&
				StringUtils.equals(OrderStatus.ORDER_STATUS_PICK_UP, currentOrderStatus.getStatus())) {
			return "Order already in Pick Up status";
		}
		if (!readyForPickup &&
				StringUtils.equals(OrderStatus.ORDER_STATUS_DROPPED_OFF, currentOrderStatus.getStatus())) {
			return "Order already not in Pick Up status";
		}
		
		String pickUpOrderStatusStr = StringUtils.EMPTY;
		String pickUpOrderStatusAuditMsg = StringUtils.EMPTY;
		if (readyForPickup) {
			pickUpOrderStatusStr = OrderStatus.ORDER_STATUS_PICK_UP;
			pickUpOrderStatusAuditMsg = "Order status changed to Pick Up";
		} else {
			pickUpOrderStatusStr = OrderStatus.ORDER_STATUS_DROPPED_OFF;
			pickUpOrderStatusAuditMsg = "Order status reverted to Dropped Off";
		}
		
		order.setModifiedAt(Calendar.getInstance().getTime());
		order.setModifiedBy(getUser(request).getId());
		
		OrderStatus pickUpOrderStatus = retrieveOrderStatus(pickUpOrderStatusStr);
		order.setOrderStatus(pickUpOrderStatus);
		
		genericDAO.saveOrUpdate(order);
		
		createAuditOrderNotes(order, pickUpOrderStatusAuditMsg, order.getModifiedBy()); 
		
		return "Order status changed successfully";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/processOrderReopen.do")
	public @ResponseBody String processOrderReopen(ModelMap model, HttpServletRequest request,
															 @RequestParam(value = "orderId") String orderId) {
		String query = "select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId;
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		if (orderList.isEmpty()) {
			return "Specified Order not found";
		}
		
		Order order = orderList.get(0);
		OrderStatus currentOrderStatus = order.getOrderStatus();
		if (!StringUtils.equals(OrderStatus.ORDER_STATUS_CLOSED, currentOrderStatus.getStatus())) {
			return "Order not in Closed status";
		}
		
		order.setModifiedAt(Calendar.getInstance().getTime());
		order.setModifiedBy(getUser(request).getId());
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_PICK_UP);
		order.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(order);
		
		createAuditOrderNotes(order, "Order re-opened; status reverted to Pick Up", order.getModifiedBy()); 
		
		return "Order status changed successfully";
	}


	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "deliveryDate desc, orderStatus.status desc", null, null));
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
			if (StringUtils.isNotEmpty(customerId)) {
				String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" + customerId  + " order by obj.line1 asc";
				model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
			}
	   }*/
		
		String orderBy = "deliveryDate desc, orderStatus.status desc"; 
		/*String deliveryDateFrom = (String)criteria.getSearchMap().get("deliveryDateFrom");
		String deliveryDateTo = (String)criteria.getSearchMap().get("deliveryDateTo");
		String pickupDateFrom = (String)criteria.getSearchMap().get("pickupDateFrom");
		String pickupDateTo = (String)criteria.getSearchMap().get("pickupDateTo");
		if ( (StringUtils.isNotEmpty(deliveryDateFrom) && StringUtils.isNotEmpty(deliveryDateTo)) ||
				(StringUtils.isNotEmpty(pickupDateFrom) && StringUtils.isNotEmpty(pickupDateTo)) ) {
			orderBy = "deliveryAddress.line1";
		}*/
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, orderBy, null, null));
		
		model.addAttribute("activeTab", "manageOrders");
		//model.addAttribute("activeSubTab", "orderDetails");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
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
			
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
			List<Order> orderList = genericDAO.search(getEntityClass(), criteria,"id",false,null);
			
			Map<String, Object> reportParams = new HashMap<String,Object>();
			List<Map<String, Object>> reportList = new ArrayList<Map<String, Object>>();
			populateOrderReportData(criteria, orderList, reportList, reportParams);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out = dynamicReportService.generateStaticReport("ordersReport", reportList, reportParams, type, request);
		
			out.writeTo(response.getOutputStream());
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			
		}
	}
	
	@RequestMapping(method = {RequestMethod.GET}, value = "/printOrder.do")
	public void printOrder(ModelMap model, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("orderId") Long orderId) {
		String query = "select obj from Order obj where obj.deleteFlag='1' and obj.id= ";
		
		List<Order> orderList = genericDAO.executeSimpleQuery(query + orderId);
		Order anOrder = orderList.get(0);
		OrderReportVO anOrderReportVO = new OrderReportVO();
		map(anOrder, anOrderReportVO);
		
		if (anOrder.getPickupOrderId() != null) {
			List<Order> pickupOrderList = genericDAO.executeSimpleQuery(query + anOrder.getPickupOrderId());
			Order pickupOrder = pickupOrderList.get(0);
			mapPickupOrder(pickupOrder, anOrderReportVO);
		}
		
		List<OrderReportVO> orderReportVOList = new ArrayList<OrderReportVO>();
		orderReportVOList.add(anOrderReportVO);
		try {
			response.setHeader("Content-Disposition", "attachment;filename= orderPrint." + "pdf");
			response.setContentType(MimeUtil.getContentType("pdf"));
			
			String logoFilePath = request.getSession().getServletContext().getRealPath("/images/" + "rds_logo.png");
			
			Map<String, Object> reportParams = new HashMap<String,Object>();
			reportParams.put("LOGO_FILE_PATH", logoFilePath);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out = dynamicReportService.generateStaticReport("orderPrint", orderReportVOList, reportParams, "pdf", request);
		
			out.writeTo(response.getOutputStream());
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			
		}
	}
	
	private void mapPickupOrder(Order pickupOrder, OrderReportVO anOrderReportVO) {
		anOrderReportVO.setPickupOrderId(pickupOrder.getId());
		anOrderReportVO.setPickupOrderDeliveryDate(pickupOrder.getFormattedDeliveryDate());
		anOrderReportVO.setPickupOrderDeliveryAddress(pickupOrder.getDeliveryAddress().getFullDeliveryAddress("\n"));
		anOrderReportVO.setPickupOrderDeliveryCity(pickupOrder.getDeliveryAddress().getCity());
		anOrderReportVO.setPickupOrderDumpsterSize(pickupOrder.getDumpsterSize().getSize());
	}
	
	private void map(Order anOrder, OrderReportVO anOrderReportVO) {
		anOrderReportVO.setId(anOrder.getId());
		anOrderReportVO.setOrderDate(anOrder.getFormattedCreatedAt());
		anOrderReportVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anOrderReportVO.setCompanyName(anOrder.getCustomer().getCompanyName());
		anOrderReportVO.setBillingAddress(anOrder.getCustomer().getBillingAddress("\n"));
		
		anOrderReportVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anOrderReportVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anOrderReportVO.setDeliveryContactPhone2(anOrder.getDeliveryContactPhone2());
		anOrderReportVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anOrderReportVO.setDeliveryAddress(anOrder.getDeliveryAddress().getFullDeliveryAddress("\n"));
		anOrderReportVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anOrderReportVO.setDumpsterLocation(anOrder.getDumpsterLocation().getLocationType());
		
		String dumpsterSize = anOrder.getDumpsterSize().getSize();
		BigDecimal tonLimit = retrieveTonLimit(anOrder.getDumpsterSize().getId(), 
				anOrder.getMaterialType().getMaterialCategory().getId());
		if (tonLimit != null) {
			dumpsterSize += " " + tonLimit + " ton limit";
		}
		anOrderReportVO.setDumpsterSize(dumpsterSize);
		
		if (anOrder.getDumpster() != null) {
			String dumpsterNum = StringUtils.defaultIfBlank(anOrder.getDumpster().getDumpsterNum(), StringUtils.EMPTY);
			anOrderReportVO.setDumpsterNum(dumpsterNum);
		}
		
		anOrderReportVO.setMaterialType(anOrder.getMaterialType().getMaterialName());
		
		anOrderReportVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anOrderReportVO.setDeliveryDateTime(anOrder.getDeliveryDateTime());
		anOrderReportVO.setPickupDate(anOrder.getFormattedPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		anOrderReportVO.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		anOrderReportVO.setCityFee(anOrderFees.getCityFee());
		anOrderReportVO.setPermitFees(anOrderFees.getTotalPermitFees());
		anOrderReportVO.setOverweightFee(anOrderFees.getOverweightFee());
		anOrderReportVO.setAdditionalFees(anOrderFees.getTotalAdditionalFees());
		anOrderReportVO.setTotalFees(anOrderFees.getTotalFees());
		
		List<OrderPayment> payments = anOrder.getOrderPayment();
		if (payments != null && !payments.isEmpty()) {
			OrderPayment anOrderPayment = payments.get(0);
			String paymentMethod = anOrderPayment.getPaymentMethod() == null ? StringUtils.EMPTY
												: anOrderPayment.getPaymentMethod().getMethod();
			anOrderReportVO.setPaymentMethod(paymentMethod);
			anOrderReportVO.setReferenceNum(StringUtils.defaultIfEmpty(anOrderPayment.getCcReferenceNum(), StringUtils.EMPTY));
			anOrderReportVO.setCheckNum(StringUtils.defaultIfEmpty(anOrderPayment.getCheckNum(), StringUtils.EMPTY));
			anOrderReportVO.setPaymentDate(anOrderPayment.getFormattedPaymentDate());
		} else {
			anOrderReportVO.setPaymentMethod(StringUtils.EMPTY);
			anOrderReportVO.setReferenceNum(StringUtils.EMPTY);
			anOrderReportVO.setCheckNum(StringUtils.EMPTY);
			anOrderReportVO.setPaymentDate(StringUtils.EMPTY);
		}
		
		anOrderReportVO.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		anOrderReportVO.setBalanceAmountDue(anOrder.getBalanceAmountDue());
		
		String userNotes = StringUtils.EMPTY;
		List<OrderNotes> notesList = anOrder.getOrderNotes();
		if (notesList != null && !notesList.isEmpty()) {
			for (OrderNotes anOrderNotes : notesList) {
				if (OrderNotes.NOTES_TYPE_USER.equals(anOrderNotes.getNotesType())) {
					userNotes = anOrderNotes.getNotes();
					break;
				}
			}
		}
		anOrderReportVO.setNotes(userNotes);
		
		String permitEndDate = StringUtils.EMPTY;
		List<Permit> permitList = anOrder.getPermits();
		if (permitList != null && !permitList.isEmpty()) {
			permitEndDate = permitList.get(0).getFormattedEndDate();
		}
		anOrderReportVO.setPermitEndDate(permitEndDate);
	}
	
	private void populateOrderReportData(SearchCriteria criteria, 
			List<Order> orderList, List<Map<String, Object>> reportList, Map<String, Object> reportParams) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		for (Order order : orderList) {
			Map<String, Object> aReportRow = new HashMap<String, Object>();
			
			aReportRow.put("id", order.getId());
			aReportRow.put("customer", order.getCustomer().getCompanyName());
			aReportRow.put("deliveryContactName", order.getDeliveryContactName());
			aReportRow.put("phone", order.getDeliveryContactPhone1());
			aReportRow.put("deliveryAddressFullLine", order.getDeliveryAddress().getFullLine());
			aReportRow.put("city", order.getDeliveryAddress().getCity());
			aReportRow.put("status", order.getOrderStatus().getStatus());
			
			aReportRow.put("deliveryDate", order.getFormattedDeliveryDate());
			aReportRow.put("pickupDate", order.getFormattedPickupDate());
			
			/*List<OrderPayment> orderPaymentList = order.getOrderPayment();
			if (orderPaymentList != null && !orderPaymentList.isEmpty()) {
				OrderPayment anOrderPayment = orderPaymentList.get(0);
				aReportRow.put("paymentMethod", StringUtils.defaultIfEmpty(anOrderPayment.getPaymentMethod().getMethod(), StringUtils.EMPTY));
			}*/
			
			OrderFees orderFees = order.getOrderFees();
			if (orderFees != null) {
				aReportRow.put("dumpsterPrice", StringUtils.defaultIfEmpty(orderFees.getDumpsterPrice().toString(), "0.00"));
				
				String cityFee = "0.00";
				if (orderFees.getCityFee() != null) {
					cityFee = orderFees.getCityFee().toString();
				}
				aReportRow.put("cityFee", cityFee);
				
				aReportRow.put("permitFees", StringUtils.defaultIfEmpty(orderFees.getTotalPermitFees().toString(), "0.00"));
				aReportRow.put("overweightFee", StringUtils.defaultIfEmpty(orderFees.getOverweightFee().toString(), "0.00"));
				aReportRow.put("additionalFees", StringUtils.defaultIfEmpty(orderFees.getTotalAdditionalFees().toString(), "0.00"));
				aReportRow.put("totalFees", StringUtils.defaultIfEmpty(orderFees.getTotalFees().toString(), "0.00"));
				aReportRow.put("totalPaid", StringUtils.defaultIfEmpty(order.getTotalAmountPaid().toString(), "0.00"));
				aReportRow.put("balanceDue", StringUtils.defaultIfEmpty(order.getBalanceAmountDue().toString(), "0.00"));
			}
			
			reportList.add(aReportRow);
		}
		
		String orderDateFrom = StringUtils.EMPTY;
		if (criteria.getSearchMap().get("createdAtFrom") != null) {
			orderDateFrom = criteria.getSearchMap().get("createdAtFrom").toString();
		}
		
		String orderDateTo = StringUtils.EMPTY;
		if (criteria.getSearchMap().get("createdAtTo") != null) {
			orderDateTo = criteria.getSearchMap().get("createdAtTo").toString();
		}
		
		orderDateFrom = StringUtils.defaultIfEmpty(orderDateFrom, orderList.get(0).getFormattedCreatedAt());
		orderDateTo = StringUtils.defaultIfEmpty(orderDateTo, orderList.get(orderList.size() - 1).getFormattedCreatedAt());
		
		reportList.get(0).put("orderDateFrom", orderDateFrom);
		reportList.get(0).put("orderDateTo", orderDateTo);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrievePermit.do")
	public @ResponseBody String retrievePermit(ModelMap model, HttpServletRequest request,
															 @RequestParam(value = "customerId", required = false) String customerId,
															 @RequestParam(value = "deliveryAddressId", required = false) String deliveryAddressId,
														    @RequestParam(value = "permitId", required = false) String permitId,
															 @RequestParam(value = "permitClassId", required = false) String permitClassId,
															 @RequestParam(value = "permitTypeId", required = false) String permitTypeId,
															 @RequestParam(value = "deliveryDate", required = false) String deliveryDate,
															 @RequestParam(value = "locationTypeId", required = false) String locationTypeId) {
		List<Permit> permitList = new ArrayList<Permit>();
		
		if (StringUtils.isNotEmpty(permitId)) {
			permitList = retrievePermit(permitId); 
		} else {
			permitList = retrievePermit(customerId, deliveryAddressId, permitClassId, permitTypeId, 
					deliveryDate, locationTypeId);
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
		String permitsQuery = "select obj from Permit obj where obj.deleteFlag='1' and ";
		permitsQuery += "obj.id=" + permitId;
		
		List<Permit> permits = genericDAO.executeSimpleQuery(permitsQuery);
		Permit aPermit = permits.get(0);
		if ("Available".equals(aPermit.getStatus().getStatus())) {
			String orderPermitsQuery = "select obj from OrderPermits obj where obj.deleteFlag='1' and obj.permit.id=" + permitId;
			List<OrderPermits> orderPermitsList = genericDAO.executeSimpleQuery(orderPermitsQuery);
			if (!orderPermitsList.isEmpty()) {
				aPermit.setFee(new BigDecimal(0.00));
			}
		}
		
		return permits;
	}
	
	private List<Permit> retrievePermit(String customerId, String deliveryAddressId, String permitClassId, 
			String permitTypeId, String deliveryDateStr, String locationTypeId) {
		String permitTypeQuery = "select obj from PermitType obj where obj.deleteFlag='1' and obj.id="+ permitTypeId;
		List<PermitType> requestedPermitTypes = genericDAO.executeSimpleQuery(permitTypeQuery);
		
		String requestedPermitDaysTokens[] = requestedPermitTypes.get(0).getPermitType().split("\\s");
		int requestedPermitDays = new Integer(requestedPermitDaysTokens[0]);
		
		String requiredEndDateStr = StringUtils.EMPTY;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date deliveryDate = formatter.parse(deliveryDateStr);
			Date requiredEndDate = DateUtils.addDays(deliveryDate, (requestedPermitDays - 1));
			
			//2015-10-03 00:00:00.0
			formatter.applyPattern("yyyy-MM-dd 00:00:00.0");
			requiredEndDateStr = formatter.format(requiredEndDate);
		} catch (ParseException pe) {
			//TODO: handle error
			pe.printStackTrace();
		}
		
		String permitsQuery = "select obj from Permit obj where obj.deleteFlag='1' and";
		permitsQuery += " obj.customer.id=" + customerId
						 +  " and obj.deliveryAddress.id=" + deliveryAddressId
				    	 +  " and obj.permitClass.id=" + permitClassId
				    	 +  " and obj.permitType.id=" + permitTypeId
				    	 +  " and obj.endDate >= '" + requiredEndDateStr + "'"
				    	 +  " and obj.locationType.id=" + locationTypeId
				    	 +  " and obj.status.status IN ('Available', 'Pending')";
		
		List<Permit> availablePermits = genericDAO.executeSimpleQuery(permitsQuery);
		return availablePermits;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveDumpsterPrice.do")
	public @ResponseBody String retrieveDumpsterPrice(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") String dumpsterSizeId,
															 @RequestParam(value = "materialTypeId") String materialTypeId,
															 @RequestParam(value = "customerId") String customerId) {
		BigDecimal dumpsterPrice = retrieveDumpsterPrice(dumpsterSizeId, materialTypeId, customerId);
		
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
	
	private BigDecimal retrieveDumpsterPrice(String dumpsterSizeId, String materialTypeId, String customerId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String todaysDateStr = dateFormat.format(new Date());
		
		String baseDumpsterPriceQuery = "select obj from DumpsterPrice obj where obj.deleteFlag='1'";
		baseDumpsterPriceQuery += " and obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 	  +  " and obj.materialType.id=" + materialTypeId
				    		  	 	  +  " and '" + todaysDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		String customerCondn = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(customerId)) {
			customerCondn = " and obj.customer.id=" + customerId; 
		}
		
		List<DumpsterPrice> dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery + customerCondn);
		if (dumsterPriceList.isEmpty()) { 
			dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery);
		}
		
		
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
		String dumpsterPriceQuery = "select distinct obj.materialType.materialCategory from DumpsterPrice obj where obj.deleteFlag='1' and";
		dumpsterPriceQuery += " obj.dumpsterSize.id=" + dumpsterSizeId;
		dumpsterPriceQuery += " order by obj.materialType.materialCategory.category asc";
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
		String dumpsterPriceQuery = "select distinct obj.materialType from DumpsterPrice obj where obj.deleteFlag='1' and";
		dumpsterPriceQuery += " obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 +  " and obj.materialType.materialCategory.id=" + materialCategoryId
				    		  	 +  " order by obj.materialType.materialName asc";
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
														    @RequestParam(value = "dumpsterSizeId") Long dumpsterSizeId) {
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String todaysDateStr = dateFormat.format(new Date());
		
		String cityFeeQuery = "select obj from CityFee obj where obj.deleteFlag='1' and";
		cityFeeQuery += " obj.id=" + cityFeeId
						 +  " and '" + todaysDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		
		List<CityFee> cityFeeList = genericDAO.executeSimpleQuery(cityFeeQuery);
		return cityFeeList.get(0).getFee();
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/calculateOverweightFee.do")
	public @ResponseBody String calculateOverweightFee(ModelMap model, HttpServletRequest request,
			 													     @RequestParam(value = "dumpsterSizeId") Long dumpsterSizeId,
			 													     @RequestParam(value = "materialCategoryId") Long materialCategoryId,
			 													     @RequestParam(value = "netWeightTonnage") BigDecimal netWeightTonnage) {
		BigDecimal overweightFee = calculateOverweightFee(dumpsterSizeId, materialCategoryId, netWeightTonnage);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(overweightFee);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}*/
	
	private BigDecimal calculateOverweightFee(Date requiredDate, Long dumpsterSizeId, Long materialCategoryId, BigDecimal netWeightTonnage) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		
		Date searchDate = requiredDate == null ? (new Date()) : requiredDate;
		String searchDateStr = dateFormat.format(searchDate);
		
		String overweightFeeQuery = "select obj from OverweightFee obj where obj.deleteFlag='1' and ";
		overweightFeeQuery += "obj.dumpsterSize.id=" + dumpsterSizeId
								 +  " and obj.materialCategory.id=" + materialCategoryId
								 +  " and obj.tonLimit<" + netWeightTonnage
								 +  " and '" + searchDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		
		List<OverweightFee> overweightFeeList = genericDAO.executeSimpleQuery(overweightFeeQuery);
		if (overweightFeeList.isEmpty()) {
			return new BigDecimal(0.00);
		} else {
			OverweightFee overweightFee = overweightFeeList.get(0);
			
			BigDecimal differentialWeight = netWeightTonnage.subtract(overweightFee.getTonLimit());
			return differentialWeight.multiply(overweightFee.getFee());
		}
	}
	
	private BigDecimal retrieveTonLimit(Long dumpsterSizeId, Long materialCategoryId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		String todaysDateStr = dateFormat.format(new Date());
		
		String overweightFeeQuery = "select obj from OverweightFee obj where obj.deleteFlag='1' and ";
		overweightFeeQuery += "obj.dumpsterSize.id=" + dumpsterSizeId
								 +  " and obj.materialCategory.id=" + materialCategoryId
								 +  " and '" + todaysDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		
		List<OverweightFee> overweightFeeList = genericDAO.executeSimpleQuery(overweightFeeQuery);
		if (overweightFeeList.isEmpty()) {
			return null;
		} else {
			OverweightFee overweightFee = overweightFeeList.get(0);
			return overweightFee.getTonLimit();
		}
	}
	
	private List<PermitClass> retrievePermitClass(Long dumpsterSizeId) {
		String permitClassQuery = "select obj from DumpsterSize obj where obj.deleteFlag='1' and ";
		permitClassQuery += "obj.size.id=" + dumpsterSizeId;
		
		List<DumpsterSize> dumpsterSizeList = genericDAO.executeSimpleQuery(permitClassQuery);
		
		List<PermitClass> permitClassList = new ArrayList<PermitClass>();
		permitClassList.add(dumpsterSizeList.get(0).getPermitClass());
		
		PermitClass permitClassD = retrievePermitClass(PermitClass.PERMIT_CLASS_D);
		if (permitClassD != null) {
			permitClassList.add(permitClassD);
		}
		
		return permitClassList;
	}
	
	private PermitClass retrievePermitClass(String permitClass) {
		String permitClassQuery = "select obj from PermitClass obj where obj.deleteFlag='1' and ";
		permitClassQuery += "obj.permitClass='" + permitClass + "'";
		
		List<PermitClass> permitClassList = genericDAO.executeSimpleQuery(permitClassQuery);
		if (permitClassList.isEmpty()) {
			return null;
		}
		return permitClassList.get(0);
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
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  orderToBeEdited.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
    	
		Order emptyOrder = new Order();
		emptyOrder.setId(orderToBeEdited.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  orderToBeEdited.getId() + " order by obj.id asc");
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
		
		String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerDeliveryAddress.do")
	public @ResponseBody String retrieveCustomerDeliveryAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("id");
		List<DeliveryAddress> addressList  = genericDAO.executeSimpleQuery("select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" + customerId + " order by line1 asc");
		
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
		String query = "select obj from Order obj where obj.deleteFlag='1' and obj.customer.id=" + customerId
				+ " and obj.deliveryAddress.id=" + deliveryAddressId
				+ " and obj.dumpster.dumpsterNum is not null"
				+ " and obj.orderStatus.status = 'Dropped Off'";
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
		List<Customer> customerList  = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' and obj.id=" + customerId);
		
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
		
		List<Permit> originallyAssignedPermits = null;
		if (entity.getId() != null) {
			originallyAssignedPermits = genericDAO.executeSimpleQuery("select obj.permits from Order obj where obj.deleteFlag='1' and obj.id=" + entity.getId());
		}
		
		if (entity.getPermits() == null || entity.getPermits().isEmpty() || entity.getPermits().get(0) == null) {
			entity.setPermits(new ArrayList<Permit>());
		}
		
		List<Permit> permitList = null;
		if (!entity.getPermits().isEmpty()) {
			StringBuffer permitIdsBuff = new StringBuffer();
			for (Permit aPermit : entity.getPermits()) {
				if (aPermit != null && aPermit.getId() != null) {
					permitIdsBuff.append(aPermit.getId().toString() + ", ");
				}
			}
			
			// TODO: Why is permit/order permit updating even when not changed? Change to list of order permits instead?
			// TODO: Create/modified date not updated
			String permitIds = permitIdsBuff.substring(0, (permitIdsBuff.length() - 2));
			permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.deleteFlag='1' and obj.id in (" 
																						+ permitIds
																						+ ")");
			entity.setPermits(permitList);
		}
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupOrderFees(entity);
		
		Long modifiedBy = getUser(request).getId();
		
		// TODO: Why both created by and modified by and why set if not changed?
		setupOrderNotes(entity, modifiedBy);
		
		setupOrderPayment(entity, modifiedBy);
		
		String orderAuditMsg = StringUtils.EMPTY;
		if (entity.getId() == null) {
			orderAuditMsg = "Order created";
			
			OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_OPEN);
			entity.setOrderStatus(orderStatus);
		} else {
			orderAuditMsg = "Order details updated";
		}

		genericDAO.saveOrUpdate(entity);
		
		createAuditOrderNotes(entity, orderAuditMsg, modifiedBy);
		
		if (permitList != null && !permitList.isEmpty()) {
			updatePermitStatus(permitList, "Assigned", modifiedBy);
		}
		
		updateIfPermitsChanged(originallyAssignedPermits, permitList, modifiedBy);
		
		cleanUp(request);
		
		//return list(model, request);
		return saveSuccess(model, request, entity);
	}
	
	private void setupOrderPayment(Order order, Long modifiedBy) {
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
				
				if (anOrderPayment.getId() == null) {
					anOrderPayment.setCreatedAt(Calendar.getInstance().getTime());
					anOrderPayment.setCreatedBy(modifiedBy);
				} else {
					//anOrderPayment.setModifiedAt(order.getModifiedAt());
					//anOrderPayment.setModifiedBy(order.getModifiedBy());
				}
				
				if (anOrderPayment.getAmountPaid() == null) {
					anOrderPayment.setAmountPaid(new BigDecimal(0.00));
				}
				
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
		
		if (orderFees.getId() == null) {
			orderFees.setCreatedBy(order.getCreatedBy());
			orderFees.setCreatedAt(order.getCreatedAt());
		} else {
			//orderFees.setModifiedBy(order.getModifiedBy());
			//orderFees.setModifiedAt(order.getModifiedAt());
		}
		
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
		/*if (orderFees.getCityFee() == null) {
			orderFees.setCityFee(new BigDecimal(0.00));
		}*/
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
	
	private void setupOrderNotes(Order order, Long modifiedBy) {
		List<OrderNotes> orderNotesList = order.getOrderNotes();
		if (orderNotesList == null) {
			orderNotesList = new ArrayList<OrderNotes>();
			order.setOrderNotes(orderNotesList);
		}
		
		if (!orderNotesList.isEmpty()) {
			OrderNotes lastNotes = orderNotesList.get(orderNotesList.size() - 1);
			String notesStr = lastNotes.getNotes();
			if (StringUtils.isEmpty(notesStr)) {
				orderNotesList.remove(orderNotesList.size() - 1);
			} else if (lastNotes.getCreatedBy() == null) {
				lastNotes.setNotesType(OrderNotes.NOTES_TYPE_USER);
				lastNotes.setOrder(order);
				lastNotes.setCreatedAt(Calendar.getInstance().getTime());
				lastNotes.setCreatedBy(modifiedBy);
				updateEnteredBy(lastNotes);
			}
		}
	}
	
	private OrderNotes createAuditOrderNotes(Order order, String orderAuditMsg, Long createdBy) {
		OrderNotes auditOrderNotes = new OrderNotes();
		auditOrderNotes.setNotesType(OrderNotes.NOTES_TYPE_AUDIT);
		auditOrderNotes.setNotes("***AUDIT: " + orderAuditMsg + "***");
		
		Order emptyOrder = new Order();
		emptyOrder.setId(order.getId());
		auditOrderNotes.setOrder(emptyOrder);
		
		auditOrderNotes.setCreatedAt(Calendar.getInstance().getTime());
		auditOrderNotes.setCreatedBy(createdBy);
		updateEnteredBy(auditOrderNotes);
		
		genericDAO.save(auditOrderNotes);
		order.getOrderNotes().add(auditOrderNotes);
		
		return auditOrderNotes;
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
		model.addAttribute("msgCtx", "manageOrder");
		model.addAttribute("msg", "Order saved successfully");
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("activeSubTab", "orderDetails");
		model.addAttribute("mode", "ADD");
		
		// EMPTY_ORDER after save change
		if (entity.getModifiedBy() == null) {
			setupCreate(model, request);
			
			Order emptyOrder = new Order();
			model.addAttribute("modelObject", emptyOrder);
			
			OrderNotes notes = new OrderNotes();
			notes.setOrder(emptyOrder);
			model.addAttribute("notesModelObject", notes);
		} else {
			setupCreate(model, request, entity);
			
			model.addAttribute("modelObject", entity);
			
			Order emptyOrder = new Order();
			emptyOrder.setId(entity.getId());
			OrderNotes notes = new OrderNotes();
			notes.setOrder(emptyOrder);
			model.addAttribute("notesModelObject", notes);
			
			List<OrderNotes> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.deleteFlag='1' and obj.order.id=" +  entity.getId() + " order by obj.id asc");
			model.addAttribute("notesList", notesList);
			
			String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  entity.getCustomer().getId() + " order by obj.line1 asc";
			model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
			
			List<List<Permit>> allPermitsOfChosenTypesList = retrievePermitsOfChosenType(entity);
			model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
			
			Long customerId = entity.getCustomer().getId();
			List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' and obj.id=" + customerId);
			Customer orderCustomer = customerList.get(0);
			
			entity.setCustomer(orderCustomer);
		}
		
		//return urlContext + "/form";
		return urlContext + "/order";

		//return super.save(request, entity, bindingResult, model);
	}
}