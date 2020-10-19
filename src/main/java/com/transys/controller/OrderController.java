package com.transys.controller;

import java.io.ByteArrayOutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ValidationException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

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

import com.transys.core.util.DateUtil;
import com.transys.core.util.MimeUtil;
import com.transys.core.util.ModelUtil;

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
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.Role;
import com.transys.model.SearchCriteria;
import com.transys.model.User;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.OrderReportVO;

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
		
		SimpleDateFormat ccExpDateFormat = new SimpleDateFormat("MM/yyyy");
		ccExpDateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "orderPayment[0].ccExpDate", new CustomDateEditor(ccExpDateFormat, true));
		
		super.initBinder(binder);
	}
	
	public void setupCreate(ModelMap model, HttpServletRequest request, Order order) {
		setupCreate(model, request);
		
		setupCreateForCustomer(model, request);
		
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteOrderNotes.do")
	public String deleteOrderNotes(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "orderNotesId") Long orderNotesId) {
		OrderNotes orderNotesEntity = genericDAO.getById(OrderNotes.class, orderNotesId);
		
		String notes = orderNotesEntity.getNotes();
		if (StringUtils.contains(notes, OrderNotes.NOTES_TYPE_AUDIT)) {
			model.addAttribute("errorCtx", "manageOrderNotes");
			model.addAttribute("error", "Audit notes cannot be deleted");
		
			return orderNotesSaveSuccess(request, orderNotesEntity, model);
		}
		
		Long orderId = orderNotesEntity.getOrder().getId();
		Order order = genericDAO.getById(Order.class, orderId);
		List<OrderNotes> orderNotesList = order.getOrderNotes();
		int i = 0;
		for (OrderNotes anOrderNotes : orderNotesList) {
			if (anOrderNotes.getId() == orderNotesEntity.getId()) {
				break;
			}
			i++;
		}
		orderNotesList.remove(i);
		
		Long modifiedBy = getUser(request).getId();
		order.setModifiedBy(modifiedBy);
		order.setModifiedAt(Calendar.getInstance().getTime());
		genericDAO.saveOrUpdate(order);
		
		genericDAO.delete(orderNotesEntity);
		
		String orderNotesAuditMsg = "Order Notes deleted";
		createAuditOrderNotes(order, orderNotesAuditMsg, modifiedBy);
		
		cleanUp(request);
		
		model.addAttribute("msgCtx", "manageOrderNotes");
		model.addAttribute("msg", "Order Notes deleted successfully");
		
		return orderNotesSaveSuccess(request, orderNotesEntity, model);
	}
	
	private void setupCreateForCustomer(ModelMap model, HttpServletRequest request) {
		Object customerIdObj = request.getParameter("customerId");
		if (customerIdObj == null) {
			return;
		}
		
		String customerIdStr = (String) customerIdObj;
		Customer customer = genericDAO.getById(Customer.class, Long.valueOf(customerIdStr));
			
		Order modelOrder = (Order) model.get("modelObject");
		modelOrder.setCustomer(customer);
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" 
				+  customer.getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
	}
	
	private void setupCommon(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		
		model.addAttribute("dumpsters", genericDAO.executeSimpleQuery("select obj from Dumpster obj where obj.deleteFlag='1' and obj.id != 0 order by obj.dumpsterNum asc"));
      model.addAttribute("dumpsterSizes", genericDAO.executeSimpleQuery("select obj from DumpsterSize obj where obj.deleteFlag='1' and obj.id != 0 order by obj.id asc"));
   }
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
      model.addAttribute("dusmpsterLocationTypes", genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.locationType asc"));
      
      //model.addAttribute("permitClasses", genericDAO.executeSimpleQuery("select obj from PermitClass obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("permitTypes", genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.id asc"));
      
      model.addAttribute("additionalFeeTypes", genericDAO.executeSimpleQuery("select obj from AdditionalFee obj where obj.deleteFlag='1' and obj.id!=0 order by obj.description asc"));
     
      model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.method asc"));
      
      model.addAttribute("cityFeeDetails", genericDAO.executeSimpleQuery("select obj from CityFee obj where obj.deleteFlag='1' and obj.id!=0 order by obj.suburbName asc"));
      		
      populateDeliveryTimeSettings(model);
      
      String driverRole = Role.DRIVER_ROLE;
      String driverQuery = "select obj from User obj where obj.deleteFlag='1' and obj.id!=0"
      		+ " and obj.accountStatus=1 and obj.role.name='" + driverRole + "'"
      		+ " and obj.employee.status=1"
      		+ " order by obj.employee.firstName asc";
      List<User> driversList = genericDAO.executeSimpleQuery(driverQuery);
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
		criteria.setPageSize(25);
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
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "deliveryDate desc, orderStatus.status desc, id desc", null, null));
		
		model.addAttribute("activeTab", "manageOrders");
		//model.addAttribute("activeSubTab", "orderDetails");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
		return urlContext + "/order";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request, null);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		
		model.addAttribute("notesModelObject", new OrderNotes());
		
		return urlContext + "/order";
	}
	
	private void updateOrderOnExchange(HttpServletRequest request, Long orderId) {
		Order order = genericDAO.getById(Order.class, orderId);
		if (!StringUtils.equals(OrderStatus.ORDER_STATUS_DROPPED_OFF, order.getOrderStatus().getStatus())) {
			return;
		}
		
		String pickUpOrderStatusStr = OrderStatus.ORDER_STATUS_PICK_UP;;
		String pickUpOrderStatusAuditMsg = "Order being exchanged, so status changed to " + OrderStatus.ORDER_STATUS_PICK_UP;
		
		OrderStatus pickUpOrderStatus = retrieveOrderStatus(pickUpOrderStatusStr);
		order.setOrderStatus(pickUpOrderStatus);
		
		order.setModifiedAt(Calendar.getInstance().getTime());
		order.setModifiedBy(getUser(request).getId());
		
		genericDAO.saveOrUpdate(order);
		
		createAuditOrderNotes(order, pickUpOrderStatusAuditMsg, order.getModifiedBy()); 
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createExchange.do")
	public String createExchange(HttpServletRequest request, @ModelAttribute("modelObject") Order entity,
			BindingResult bindingResult, ModelMap model) {
		Order exchangeOrder = new Order();
		exchangeOrder.setPickupOrderId(entity.getId());
		
		exchangeOrder.setCustomer(entity.getCustomer());
		exchangeOrder.setDeliveryAddress(entity.getDeliveryAddress());
		exchangeOrder.setDeliveryContactName(entity.getDeliveryContactName());
		exchangeOrder.setDeliveryContactPhone1(entity.getDeliveryContactPhone1());
		exchangeOrder.setDeliveryContactPhone2(entity.getDeliveryContactPhone2());
		
		exchangeOrder.setDumpsterLocation(entity.getDumpsterLocation());
		exchangeOrder.setDumpsterSize(entity.getDumpsterSize());
		
		exchangeOrder.setMaterialType(entity.getMaterialType());
		
		OrderFees entityOrderFees = entity.getOrderFees();
		if (entityOrderFees != null) {
			OrderFees exchangeOrderFees = new OrderFees();
			BigDecimal exchangeTotalFees = new BigDecimal(0.00);
			
			/*if (entityOrderFees.getPermitFee1() != null) {
				exchangeOrderFees.setPermitFee1(entityOrderFees.getPermitFee1());
				exchangeTotalFees = exchangeTotalFees.add(entityOrderFees.getPermitFee1());
			}*/
			
			if (entityOrderFees.getDumpsterPrice() != null) {
				exchangeOrderFees.setDumpsterPrice(entityOrderFees.getDumpsterPrice());
				exchangeTotalFees = exchangeTotalFees.add(entityOrderFees.getDumpsterPrice());
			}
			
			if (entityOrderFees.getCityFee() != null) {
				exchangeOrderFees.setCityFeeType(entityOrderFees.getCityFeeType());
				exchangeOrderFees.setCityFee(entityOrderFees.getCityFee());
				exchangeTotalFees = exchangeTotalFees.add(entityOrderFees.getCityFee());
			}
			
			exchangeOrderFees.setTotalFees(exchangeTotalFees);
			exchangeOrder.setOrderFees(exchangeOrderFees);
			exchangeOrder.setTotalAmountPaid(new BigDecimal(0.00));
			exchangeOrder.setBalanceAmountDue(exchangeTotalFees);
		}
		
		String userNotes = extractUserNotes(entity);
		if (StringUtils.isNotEmpty(userNotes)) {
			OrderNotes notes = new OrderNotes();
			notes.setNotes(userNotes);
			notes.setNotesType(OrderNotes.NOTES_TYPE_USER);
			
			List<OrderNotes> exchangeNotesList = new ArrayList<OrderNotes>();
			exchangeNotesList.add(notes);
			
			exchangeOrder.setOrderNotes(exchangeNotesList);
		}
		
		List<Permit> entityPermitsList = entity.getPermits();
		if (entityPermitsList != null && !entityPermitsList.isEmpty()) {
			List<Permit> exchangePermitsList = new ArrayList<Permit>();
			exchangePermitsList.add(entityPermitsList.get(0));
			
			exchangeOrder.setPermits(exchangePermitsList);
			
			List<List<Permit>> allPermitsOfChosenTypesList = new ArrayList<List<Permit>>();
			allPermitsOfChosenTypesList.add(exchangePermitsList);
			model.addAttribute("allPermitsOfChosenTypesList", allPermitsOfChosenTypesList);
		}
		
		model.addAttribute("modelObject", exchangeOrder);
		
		setupCreate(model, request, exchangeOrder);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" +  exchangeOrder.getCustomer().getId() + " order by obj.line1 asc";
		model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(query));
    	
		model.addAttribute("notesModelObject", new OrderNotes());
		
		return urlContext + "/order";
	}
	
	private String extractUserNotes(Order entity) {
		String userNotes = StringUtils.EMPTY;
		List<OrderNotes> notesList = entity.getOrderNotes();
		if (notesList != null && !notesList.isEmpty()) {
			for (OrderNotes anOrderNotes : notesList) {
				if (OrderNotes.NOTES_TYPE_USER.equals(anOrderNotes.getNotesType())) {
					userNotes = anOrderNotes.getNotes();
					break;
				}
			}
		}
		
		return userNotes;
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
		
		return orderNotesSaveSuccess(request, entity, model);
	}
	
	private String orderNotesSaveSuccess(HttpServletRequest request, OrderNotes entity, ModelMap model) {
		cleanUp(request);
		
		Long orderId = entity.getOrder().getId();
		List<Order> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.deleteFlag='1' and obj.id=" + orderId);
		Order savedOrder = orderList.get(0);
		model.addAttribute("modelObject", savedOrder);

		setupCreate(model, request, savedOrder);
		
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
		Long userId = entity.getCreatedBy() != null ? entity.getCreatedBy() : entity.getModifiedBy();
		User user = genericDAO.getById(User.class, userId);
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
				
				String deliveryDateStr = DateUtil.formatToInputDate(order.getDeliveryDate());
				
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
		
		beforeSave(request, entity, model);
		
		Long modifiedBy = entity.getModifiedBy();
		String auditMsg = StringUtils.EMPTY;
		if (OrderStatus.ORDER_STATUS_OPEN.equals(entity.getOrderStatus().getStatus())) {
			auditMsg = "Order status changed to " + OrderStatus.ORDER_STATUS_DROPPED_OFF;
			
			OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_DROPPED_OFF);
			entity.setOrderStatus(orderStatus);
			
			updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_DROPPED_OFF, modifiedBy);
		} else {
			auditMsg = "Order Drop Off details updated";
			
			if (!currentlyAssignedDumpsterId.equals(entity.getDumpster().getId())) {
				if (!OrderStatus.ORDER_STATUS_CLOSED.equals(entity.getOrderStatus().getStatus())) {
					updateDumpsterStatus(currentlyAssignedDumpsterId, DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, modifiedBy);
					updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_DROPPED_OFF, modifiedBy);
				}
			}
		}
		
		genericDAO.saveOrUpdate(entity);
		
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, auditMsg, modifiedBy);
		entity.getOrderNotes().add(auditOrderNotes);
		
		return dropOffSaveSuccess(request, entity, model);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/revertDropOffToOpen.do")
	public String revertDropOffToOpen(HttpServletRequest request,
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
			return urlContext + "/order";
		}
		
		beforeSave(request, entity, model);
		
		Long associatedDumpsterId = entity.getDumpster().getId();
		
		entity.setDropOffDriver(null);
		entity.setDumpster(null);
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_OPEN);
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = entity.getModifiedBy();
		String auditMsg = "Order status reverted to " + OrderStatus.ORDER_STATUS_OPEN;
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, auditMsg, modifiedBy);
		entity.getOrderNotes().add(auditOrderNotes);
		
		updateDumpsterStatus(associatedDumpsterId, DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, modifiedBy);
		
		return dropOffSaveSuccess(request, entity, model);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/revertCancelToOpen.do")
	public String revertCancelToOpen(HttpServletRequest request,
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
			return urlContext + "/order";
		}
		
		beforeSave(request, entity, model);
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_OPEN);
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		Long modifiedBy = entity.getModifiedBy();
		
		String auditMsg = "Order status reverted to " + OrderStatus.ORDER_STATUS_OPEN;
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, auditMsg, modifiedBy);
		entity.getOrderNotes().add(auditOrderNotes);
		
		if (entity.getPermits() != null && !entity.getPermits().isEmpty()) {
			updatePermitStatus(entity.getPermits(), PermitStatus.PERMIT_STATUS_ASSIGNED, modifiedBy);
		}
		
		return saveSuccess(model, request, entity);
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
	
	private PermitStatus retrievePermitStatus(String status) {
		PermitStatus permitStatus = (PermitStatus) genericDAO.executeSimpleQuery("select obj from PermitStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'").get(0);
		return permitStatus;
	}
	
	private void updatePermitStatus(List<Permit> permitList, String status, Long modifiedBy) {
		if (permitList == null || permitList.isEmpty()) {
			return;
		}
		
		PermitStatus permitStatus = retrievePermitStatus(status);
		
		for (Permit aPermit : permitList) {
			if (status.equals(aPermit.getStatus().getStatus())) {
				continue;
			}
			
			aPermit.setStatus(permitStatus);
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
			updatePermitStatus(changedPermits, PermitStatus.PERMIT_STATUS_AVAILABLE, modifiedBy);
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
		
		beforeSave(request, entity, model);
		
		populateOrderFees(entity);
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_CLOSED);
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, "Order status changed to " + OrderStatus.ORDER_STATUS_CLOSED,
				entity.getModifiedBy());  
		entity.getOrderNotes().add(auditOrderNotes);
		
		Long modifiedBy = entity.getModifiedBy();
		updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, modifiedBy);
		
		String exchangeQuery = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.pickupOrderId=" + entity.getId()
				+ " and obj.orderStatus.status != '" + OrderStatus.ORDER_STATUS_CLOSED + "'";
		List<Order> exchangeOrderList = genericDAO.executeSimpleQuery(exchangeQuery);
		if (exchangeOrderList.isEmpty()) {
			updatePermitStatus(entity.getPermits(), PermitStatus.PERMIT_STATUS_AVAILABLE, modifiedBy);
		}
		
		return pickupSaveSuccess(request, entity, model);
	}
	
	private void populateOrderFees(Order entity) {
		OrderFees orderFees = entity.getOrderFees();
		
		if (orderFees.getOverweightFee() != null) {
			orderFees.setTotalFees(orderFees.getTotalFees().subtract(orderFees.getOverweightFee()));
		}
		if (orderFees.getTonnageFee() != null) {
			orderFees.setTotalFees(orderFees.getTotalFees().subtract(orderFees.getTonnageFee()));
		}
		
		BigDecimal netWeightTonnage = entity.getNetWeightTonnage();
		if (netWeightTonnage == null) {
			orderFees.setOverweightFee(new BigDecimal(0.00));
			orderFees.setTonnageFee(new BigDecimal(0.00));
			entity.setBalanceAmountDue(orderFees.getTotalFees().subtract(entity.getTotalAmountPaid()));
			return;
		}
		
		Long customerId = entity.getCustomer().getId();
		Long dumpsterSizeId = entity.getDumpsterSize().getId();
		Long materialTypeId = entity.getMaterialType().getId();
		Long materialCategoryId = entity.getMaterialType().getMaterialCategory().getId();
		BigDecimal overweightFee = calculateOverweightFee(entity.getCreatedAt(), dumpsterSizeId, materialCategoryId, netWeightTonnage);
		overweightFee = overweightFee.setScale(2, RoundingMode.HALF_UP);
		
		DumpsterPrice dumpsterPriceObj = retrieveDumpsterPrice(dumpsterSizeId, materialCategoryId,
								materialTypeId, customerId);
		BigDecimal tonnageFee = (dumpsterPriceObj == null || dumpsterPriceObj.getTonnageFee() == null) 
				? new BigDecimal(0.0) : dumpsterPriceObj.getTonnageFee();
		BigDecimal totalTonnageFee = netWeightTonnage.multiply(tonnageFee);
		totalTonnageFee = totalTonnageFee.setScale(2, RoundingMode.HALF_UP);
		
		orderFees.setOverweightFee(overweightFee);
		orderFees.setTonnageFee(totalTonnageFee);
		
		orderFees.setTotalFees(orderFees.getTotalFees().add(overweightFee).add(totalTonnageFee));
		entity.setBalanceAmountDue(orderFees.getTotalFees().subtract(entity.getTotalAmountPaid()));
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
			pickUpOrderStatusAuditMsg = "Order status changed to " + OrderStatus.ORDER_STATUS_PICK_UP;
		} else {
			pickUpOrderStatusStr = OrderStatus.ORDER_STATUS_DROPPED_OFF;
			pickUpOrderStatusAuditMsg = "Order status reverted to " + OrderStatus.ORDER_STATUS_DROPPED_OFF;
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
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "deliveryDate desc, orderStatus.status desc, id desc", null, null));
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/order";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		//criteria.getSearchMap().put("id!",0l);
		
		/*if (criteria.getSearchMap().get("customer") != null) {
			String customerId = (String)criteria.getSearchMap().get("customer");
			if (StringUtils.isNotEmpty(customerId)) {
				String deliveryAddressQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id=" + customerId  + " order by obj.line1 asc";
				model.addAttribute("deliveryAddresses", genericDAO.executeSimpleQuery(deliveryAddressQuery));
			}
	   }*/
		
		String orderBy = "deliveryDate desc, orderStatus.status desc, id desc"; 
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
			String fileName = "orderPrint_" + anOrder.getId() + ".pdf";
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
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
		anOrderReportVO.setPickupOrderDumpsterNum(pickupOrder.getDumpster().getDumpsterNum());
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
		
		DumpsterSize dumpsterSize = anOrder.getDumpsterSize();
		MaterialCategory materialCategory = anOrder.getMaterialType().getMaterialCategory();
		String tonLimitStr = StringUtils.EMPTY;
		if (!StringUtils.equals(MaterialCategory.MATERIAL_CATEGORY_STREET_SWEEPINGS, materialCategory.getCategory())) {
			BigDecimal tonLimit = retrieveTonLimit(dumpsterSize.getId(), materialCategory.getId());
			if (tonLimit != null) {
				tonLimitStr = " " + tonLimit + " ton limit";
			}
		}
		anOrderReportVO.setDumpsterSize(dumpsterSize.getSize() + tonLimitStr);
		
		if (anOrder.getDumpster() != null) {
			String dumpsterNum = StringUtils.defaultIfBlank(anOrder.getDumpster().getDumpsterNum(), StringUtils.EMPTY);
			anOrderReportVO.setDumpsterNum(dumpsterNum);
		}
		
		anOrderReportVO.setMaterialType(anOrder.getMaterialType().getMaterialName());
		
		anOrderReportVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anOrderReportVO.setDeliveryDateTime(anOrder.getDeliveryDateTimeRange());
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
		
		String userNotes = extractUserNotes(anOrder);
		if (StringUtils.isNotEmpty(userNotes) && userNotes.length() > 130) {
			userNotes = userNotes.substring(0, 135);
		}
		anOrderReportVO.setNotes(userNotes);
		
		String permitEndDate = StringUtils.EMPTY;
		String permitAddress = StringUtils.EMPTY;
		List<Permit> permitList = anOrder.getPermits();
		if (permitList != null && !permitList.isEmpty()) {
			Permit aPermit = permitList.get(0);
			permitEndDate = aPermit.getFormattedEndDate();
			
			if (!StringUtils.equals(anOrder.getDeliveryAddress().getFullLine(), aPermit.getFullLinePermitAddress1())) {
				permitAddress = "Permit Address: " + aPermit.getFullLinePermitAddress1();
			}
		}
		anOrderReportVO.setPermitEndDate(permitEndDate);
		anOrderReportVO.setPermitAddressFullLine(permitAddress);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrievePermit.do")
	public @ResponseBody String retrievePermit(ModelMap model, HttpServletRequest request,
															 @RequestParam(value = "customerId", required = false) String customerId,
															 @RequestParam(value = "deliveryAddressId", required = false) String deliveryAddressId,
														    @RequestParam(value = "permitId", required = false) String permitId,
															 @RequestParam(value = "permitClassId", required = false) String permitClassId,
															 @RequestParam(value = "permitTypeId", required = false) String permitTypeId,
															 @RequestParam(value = "deliveryDate", required = false) String deliveryDate,
															 @RequestParam(value = "locationTypeId", required = false) String locationTypeId,
															 @RequestParam(value = "orderId", required = false) String orderId) {
		List<Permit> permitList = new ArrayList<Permit>();
		if (StringUtils.isNotEmpty(permitId)) {
			permitList = retrievePermit(permitId); 
		} else {
			permitList = retrievePermit(customerId, deliveryAddressId, permitClassId, permitTypeId, 
					deliveryDate, locationTypeId);
		}
		
		if (StringUtils.isNotEmpty(orderId)) {
			List<OrderPermits> orderPermitsList = retrieveOrderPermits(orderId, customerId, deliveryAddressId, permitClassId, permitTypeId, 
					deliveryDate, locationTypeId);
			if (orderPermitsList != null && !orderPermitsList.isEmpty()) {
				Permit aPermit = orderPermitsList.get(0).getPermit();
				//if (!PermitStatus.PERMIT_STATUS_AVAILABLE.equals(aPermit.getStatus().getStatus())) {
					permitList.add(aPermit);
				//}
			}
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(permitList);
		} catch (JsonProcessingException e) {
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
		if (PermitStatus.PERMIT_STATUS_AVAILABLE.equals(aPermit.getStatus().getStatus())) {
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
		int requestedPermitDays = retrievePermitTypeDays(permitTypeId);
		
		String requiredEndDateStr = StringUtils.EMPTY;
		try {
			requiredEndDateStr = DateUtil.addDaysToInputDateAndFormatToDbDateTime2(deliveryDateStr, 
					(requestedPermitDays - 1));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		String permitsQuery = "select obj from Permit obj where obj.deleteFlag='1' and";
		permitsQuery += " obj.customer.id=" + customerId
						 +  " and obj.deliveryAddress.id=" + deliveryAddressId
				    	 +  " and obj.permitClass.id=" + permitClassId
				    	 +  " and obj.permitType.id=" + permitTypeId
				    	 +  " and obj.endDate >= '" + requiredEndDateStr + "'"
				    	 +  " and obj.locationType.id=" + locationTypeId
				    	 +  " and obj.status.status IN ('Available', 'Pending')"
				    	 +  " order by obj.number asc";
		
		List<Permit> availablePermits = genericDAO.executeSimpleQuery(permitsQuery);
		return availablePermits;
	}
	
	private List<OrderPermits> retrieveOrderPermits(String orderId, String customerId, String deliveryAddressId, String permitClassId, 
			String permitTypeId, String deliveryDateStr, String locationTypeId) {
		//int requestedPermitDays = retrievePermitTypeDays(permitTypeId);
		
		String requiredEndDateStr = StringUtils.EMPTY;
		try {
			//requiredEndDateStr = DateUtil.addDaysAndFormatToDbDate(deliveryDateStr, (requestedPermitDays - 1));
			requiredEndDateStr =  DateUtil.formatInputDateToDbDateTimeFormat2(deliveryDateStr);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		String orderPermitsQuery = "select obj from OrderPermits obj where obj.deleteFlag='1' and";
		orderPermitsQuery += " obj.permit.customer.id=" + customerId
						 		+  " and obj.permit.deliveryAddress.id=" + deliveryAddressId
						 		+  " and obj.permit.permitClass.id=" + permitClassId
						 		+  " and obj.permit.permitType.id=" + permitTypeId
						 		+  " and obj.permit.endDate >= '" + requiredEndDateStr + "'"
						 		+  " and obj.permit.locationType.id=" + locationTypeId
						 		+  " and obj.order.id=" + orderId
						 		+  " order by obj.permit.id desc";;
		
		List<OrderPermits> orderPermitsLiat = genericDAO.executeSimpleQuery(orderPermitsQuery);
		return orderPermitsLiat;
	}
	
	private int retrievePermitTypeDays(String permitTypeId) {
		PermitType permitType = retrievePermitType(permitTypeId);
		
		String requestedPermitDaysTokens[] = permitType.getPermitType().split("\\s");
		int requestedPermitDays = new Integer(requestedPermitDaysTokens[0]);
		return requestedPermitDays;
	}
	
	private PermitType retrievePermitType(String permitTypeId) {
		String permitTypeQuery = "select obj from PermitType obj where obj.deleteFlag='1' and obj.id="+ permitTypeId;
		List<PermitType> requestedPermitTypes = genericDAO.executeSimpleQuery(permitTypeQuery);
		return (requestedPermitTypes == null || requestedPermitTypes.isEmpty()) ? null : requestedPermitTypes.get(0);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveDumpsterPrice.do")
	public @ResponseBody String retrieveDumpsterPrice(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "dumpsterSizeId") Long dumpsterSizeId,
														    @RequestParam(value = "materialCategoryId") Long materialCategoryId,
															 @RequestParam(value = "materialTypeId") Long materialTypeId,
															 @RequestParam(value = "customerId") Long customerId) {
		DumpsterPrice dumpsterPriceObj = retrieveDumpsterPrice(dumpsterSizeId, materialCategoryId, 
							materialTypeId, customerId);
		BigDecimal dumpsterPrice = (dumpsterPriceObj == null || dumpsterPriceObj.getPrice() == null) ? new BigDecimal(0.0) : dumpsterPriceObj.getPrice();
		
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
	
	private DumpsterPrice retrieveDumpsterPrice(Long dumpsterSizeId, Long materialCategoryId,
					Long materialTypeId, Long customerId) {
		String todaysDateStr = DateUtil.formatTodayToDbDate();
		
		String baseDumpsterPriceQuery = "select obj from DumpsterPrice obj where obj.deleteFlag='1'";
		baseDumpsterPriceQuery += " and obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 	  +  " and '" + todaysDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		String materialCategoryCondn = StringUtils.EMPTY;
		if (materialCategoryId != null) {
			materialCategoryCondn = " and obj.materialCategory.id=" + materialCategoryId; 
		}
		String materialTypeCondn = StringUtils.EMPTY;
		if (materialTypeId != null) {
			materialTypeCondn = " and obj.materialType.id=" + materialTypeId; 
		}
		String customerCondn = StringUtils.EMPTY;
		if (customerId != null) {
			customerCondn = " and obj.customer.id=" + customerId; 
		}
		
		boolean applyCustomerGenericDiscount = false;
		List<DumpsterPrice> dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery
					+ materialTypeCondn + customerCondn);
		if (dumsterPriceList.isEmpty()) {
			if (StringUtils.isNotEmpty(customerCondn)) {
				dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery + materialCategoryCondn 
						+ customerCondn);
			}
			
			if (dumsterPriceList.isEmpty()) {
				String nonCustomerCondn = " and obj.customer.id is null";
				applyCustomerGenericDiscount = true;
				
				if (StringUtils.isNotEmpty(materialTypeCondn)) {
					dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery + materialTypeCondn 
							+ nonCustomerCondn);
				}
				
				if (dumsterPriceList.isEmpty()) {
					dumsterPriceList = genericDAO.executeSimpleQuery(baseDumpsterPriceQuery + materialCategoryCondn
							+ nonCustomerCondn);
				}
			}
		}
		
		if (dumsterPriceList.isEmpty()) {
			return null;
		} 
		
		DumpsterPrice retrievedDumpsterPriceObj = dumsterPriceList.get(0);
			
		DumpsterPrice returnDumpsterPriceObj = new DumpsterPrice();
		returnDumpsterPriceObj.setTonnageFee(retrievedDumpsterPriceObj.getTonnageFee());
		
		returnDumpsterPriceObj.setPrice(retrievedDumpsterPriceObj.getPrice());
		if (applyCustomerGenericDiscount) {
			BigDecimal customerGenericDumpsterDiscount = retrieveCustomerGenericDiscount(customerId);
			if (customerGenericDumpsterDiscount != null) {
				returnDumpsterPriceObj.setPrice(returnDumpsterPriceObj.getPrice().subtract(customerGenericDumpsterDiscount));
			}
		}
		
		return returnDumpsterPriceObj;
	}
	
	private BigDecimal retrieveCustomerGenericDiscount(Long customerId) {
		String customerQuery = "select obj from Customer obj where obj.deleteFlag='1'"
				+ " and obj.id=" + customerId;
		List<Customer> customerList = genericDAO.executeSimpleQuery(customerQuery);
		return customerList.get(0).getDumpsterDiscount();
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
		String dumpsterPriceQuery = "select distinct obj.materialCategory from DumpsterPrice obj where obj.deleteFlag='1'";
		dumpsterPriceQuery += " and obj.dumpsterSize.id=" + dumpsterSizeId;
		dumpsterPriceQuery += " order by obj.materialCategory.category asc";
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
		/*String dumpsterPriceQuery = "select distinct obj.materialType from DumpsterPrice obj where obj.deleteFlag='1'";
		dumpsterPriceQuery += " and obj.dumpsterSize.id=" + dumpsterSizeId
				    		  	 +  " and obj.materialCategory.id=" + materialCategoryId
				    		  	 +  " order by obj.materialType.materialName asc";*/
		String dumpsterPriceQuery = "select obj from MaterialType obj where obj.deleteFlag='1' and";
		dumpsterPriceQuery	+= " obj.materialCategory.id=" + materialCategoryId + " order by obj.materialName asc";
		List<MaterialType> materialTypes = genericDAO.executeSimpleQuery(dumpsterPriceQuery);
		return materialTypes;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveCityFee.do")
	public @ResponseBody String retrieveCityFee(ModelMap model, HttpServletRequest request,
														    @RequestParam(value = "cityFeeId") String cityFeeId) {
		BigDecimal cityFee = retrieveCityFee(cityFeeId);
		if (cityFee == null) {
			return "ErrorMsg: Valid city fee not found for selected City";
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(cityFee);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/isOrderExchangable.do")
	public @ResponseBody String isOrderExchangable(HttpServletRequest request, @ModelAttribute("modelObject") Order entity,
			BindingResult bindingResult, ModelMap model) {
		/*String errorMsg = String.format("No Dumpster assigned to Örder # %d to be exchanged", entity.getId());
		return (entity.getDumpster() == null) ? errorMsg : "true";*/
		return "true";
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
		String todaysDateStr = DateUtil.formatTodayToDbDate();
		
		String cityFeeQuery = "select obj from CityFee obj where obj.deleteFlag='1' and";
		cityFeeQuery += " obj.id=" + cityFeeId
						 +  " and '" + todaysDateStr + "' between obj.effectiveStartDate and obj.effectiveEndDate";
		
		List<CityFee> cityFeeList = genericDAO.executeSimpleQuery(cityFeeQuery);
		if (cityFeeList == null || cityFeeList.isEmpty()) {
			return null;
		} else {
			return cityFeeList.get(0).getFee();
		}
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
		Date searchDate = requiredDate == null ? (new Date()) : requiredDate;
		String searchDateStr = DateUtil.formatToDbDate(searchDate); 
		
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
	
	/*private BigDecimal calculateTonnageFee(Date requiredDate, Long dumpsterSizeId, Long materialCategoryId, BigDecimal netWeightTonnage) {
		Date searchDate = requiredDate == null ? (new Date()) : requiredDate;
		String searchDateStr = DateUtil.formatDbDate2(searchDate); 
		
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
	}*/
	
	private BigDecimal retrieveTonLimit(Long dumpsterSizeId, Long materialCategoryId) {
		String todaysDateStr = DateUtil.formatTodayToDbDate(); 
		
		String overweightFeeQuery = "select obj from OverweightFee obj where obj.deleteFlag='1'";
		overweightFeeQuery += " and obj.dumpsterSize.id=" + dumpsterSizeId
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
		setupCommon(model, request);
		
		//model.addAttribute("orderIds", genericDAO.executeSimpleQuery("select obj.id from Order obj where obj.deleteFlag='1' order by obj.id asc"));
		
		String[][] strArrOfArr = ModelUtil.retrieveOrderDeliveryContactDetails(genericDAO);
		String[] phoneArr = strArrOfArr[0];
		//String[] contactNameArr = strArrOfArr[1];
		
		model.addAttribute("deliveryContactPhones", phoneArr);
		//model.addAttribute("deliveryContactNames", contactNameArr);
		
		//String deliveryAddresseQuery = "select distinct obj.line2 from DeliveryAddress obj where obj.deleteFlag='1' and obj.line2 != '' order by obj.line2 asc";
		//model.addAttribute("deliveryAddressStreets", genericDAO.executeSimpleQuery(deliveryAddresseQuery));
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/cancel.do")
	public @ResponseBody String cancel(HttpServletRequest request, @ModelAttribute("modelObject") Order entity,
				BindingResult bindingResult, ModelMap model) {
		if (!StringUtils.equals(OrderStatus.ORDER_STATUS_OPEN, entity.getOrderStatus().getStatus())) {
			return String.format("Order # %d cannot be Cancelled as it is not in 'Open' status", entity.getId());
		}
		
		beforeSave(request, entity, model);
		
		OrderStatus orderStatus = retrieveOrderStatus(OrderStatus.ORDER_STATUS_CANCELED);
		entity.setOrderStatus(orderStatus);
		
		genericDAO.saveOrUpdate(entity);
		
		OrderNotes auditOrderNotes = createAuditOrderNotes(entity, 
				"Order status changed to " + OrderStatus.ORDER_STATUS_CANCELED, entity.getModifiedBy());  
		entity.getOrderNotes().add(auditOrderNotes);
		
		Long modifiedBy = entity.getModifiedBy();
		if (entity.getDumpster() != null && entity.getDumpster().getId() != null) {
			updateDumpsterStatus(entity.getDumpster().getId(), DumpsterStatus.DUMPSTER_STATUS_AVAILABLE, modifiedBy);
		}
		if (entity.getPermits() != null && !entity.getPermits().isEmpty()) {
			updatePermitStatus(entity.getPermits(), PermitStatus.PERMIT_STATUS_AVAILABLE, modifiedBy);
		}
		
		return String.format("Order # %d is cancelled successfully", entity.getId());
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Order entity,
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
			return urlContext + "/order";
		}
		
		String isExchange = request.getParameter("isExchange");
		if (BooleanUtils.toBoolean(isExchange)) {
			String existingDroppedOffOrderIdStr = request.getParameter("existingDroppedOffOrderId");
			Long existingDroppedOffOrderId = new Long(existingDroppedOffOrderIdStr);
			entity.setPickupOrderId(existingDroppedOffOrderId);
			
			updateOrderOnExchange(request, existingDroppedOffOrderId);
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
		
		if (!StringUtils.equals(OrderStatus.ORDER_STATUS_CLOSED, entity.getOrderStatus().getStatus())) {
			updatePermitStatus(permitList, PermitStatus.PERMIT_STATUS_ASSIGNED, modifiedBy);
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
		if (orderFees.getTonnageFee() == null) {
			orderFees.setTonnageFee(new BigDecimal(0.00));
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
		auditOrderNotes.setNotes(OrderNotes.AUDIT_MSG_PREFIX + orderAuditMsg + OrderNotes.AUDIT_MSG_SUFFIX);
		
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
		/*if (entity.getModifiedBy() == null) {
			setupCreate(model, request);
			
			Order emptyOrder = new Order();
			model.addAttribute("modelObject", emptyOrder);
			
			OrderNotes notes = new OrderNotes();
			notes.setOrder(emptyOrder);
			model.addAttribute("notesModelObject", notes);
		} else {*/
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
		//}
		
		//return urlContext + "/form";
		return urlContext + "/order";

		//return super.save(request, entity, bindingResult, model);
	}
}