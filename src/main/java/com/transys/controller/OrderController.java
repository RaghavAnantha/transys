package com.transys.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.AbstractBaseModel;
import com.transys.model.Address;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.DumpsterInfo;
import com.transys.model.LocationType;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.OrderNotes;
import com.transys.model.OrderPaymentInfo;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
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
      model.addAttribute("permits", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
      
      model.addAttribute("dumpsters", genericDAO.executeSimpleQuery("select obj from DumpsterInfo obj where obj.id!=0 order by obj.id asc"));
      model.addAttribute("dusmpsterLocationTypes", genericDAO.executeSimpleQuery("select obj from LocationType obj where obj.id!=0 order by obj.id asc"));
      
      String driverRole = "DRIVER";
      List<BaseModel> driversList = genericDAO.executeSimpleQuery("select obj from User obj where obj.id!=0 and obj.role.name='" + driverRole + "' order by obj.id asc");
      model.addAttribute("drivers", driversList);
      
      MaterialType aMaterialType = new MaterialType();
      aMaterialType.setId(1l);
      aMaterialType.setType("Drywall/Wood");
      
      List<MaterialType> materialTypeList = new ArrayList<MaterialType>();
      materialTypeList.add(aMaterialType);
      
      model.addAttribute("materialTypes", materialTypeList);
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
		List<BaseModel> orderList = genericDAO.executeSimpleQuery("select obj from Order obj where obj.id=" + orderId);
		model.addAttribute("modelObject", orderList.get(0));
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderId + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
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
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		model.addAttribute("activeTab", "manageOrders");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "orderDetails");
		
		Order orderToBeEdited = (Order)model.get("modelObject");
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderToBeEdited.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(emptyOrder);
		model.addAttribute("notesModelObject", notes);
	
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  orderToBeEdited.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
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
		String json = (new Gson()).toJson(addressList);
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerAddress.do")
	public @ResponseBody String retrieveCustomerAddress(ModelMap model, HttpServletRequest request) {
		String customerId = request.getParameter("id");
		List<Customer> customerList  = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId);
		String json = (new Gson()).toJson(customerList.get(0));
		return json;
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
		List<Permit> permitList = genericDAO.executeSimpleQuery("select obj from Permit obj where obj.id in (" 
																					+ entity.getPermits().get(0).getId()
																					+ ")");
		entity.setPermits(permitList);
		
		String initOrderStatus = "Open";
		OrderStatus orderStatus = (OrderStatus)genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.status='" + initOrderStatus + "'").get(0);
		entity.setOrderStatus(orderStatus);
		
		entity.getOrderPaymentInfo().setOrder(entity);
		
		entity.getOrderNotes().get(0).setOrder(entity);

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
		
		Order order = new Order();
		order.setId(entity.getId());
		OrderNotes notes = new OrderNotes();
		notes.setOrder(order);
		model.addAttribute("notesModelObject", notes);
		List<BaseModel> notesList = genericDAO.executeSimpleQuery("select obj from OrderNotes obj where obj.order.id=" +  entity.getId() + " order by obj.id asc");
		model.addAttribute("notesList", notesList);
		
		//return urlContext + "/form";
		return urlContext + "/order";

		// create permit relationship
		/*	Map criterias = new HashMap();
		List<Permit> permits = genericDAO.findByCriteria(Permit.class, criterias, "id", false);
		entity.setPermits(permits.parallelStream().distinct().collect(Collectors.toSet()));*/
		
		//return super.save(request, entity, bindingResult, model);
	}
}