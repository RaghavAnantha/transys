package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
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
import com.transys.core.util.DateUtil;
import com.transys.core.util.MimeUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.DeliveryAddress;
import com.transys.model.Customer;
import com.transys.model.CustomerNotes;
import com.transys.model.CustomerStatus;
import com.transys.model.CustomerType;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderNotes;
import com.transys.model.OrderPayment;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.PermitNotes;
//import com.transys.model.FuelVendor;
//import com.transys.model.Location;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;
import com.transys.model.vo.CustomerReportVO;
import com.transys.model.vo.OrderReportVO;
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
		model.addAttribute("customerIds", genericDAO.executeSimpleQuery("select obj.id from Customer obj where obj.deleteFlag='1' order by obj.id asc"));
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' order by obj.companyName asc");
		model.addAttribute("customer", customerList);
		
		SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (Customer aCustomer : customerList) {
			phoneSet.add(aCustomer.getPhone());
			contactNameSet.add(aCustomer.getContactName());
		}
		
		String[] phoneArr = phoneSet.toArray(new String[0]);
		String[] contactNameArr = contactNameSet.toArray(new String[0]);
		
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
		
		//TODO:  This is for order report - think of moving the report to order
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "ADD");
		model.addAttribute("activeSubTab", "billing");
		
		model.addAttribute("notesModelObject", new CustomerNotes());
		
		model.addAttribute("deliveryAddressModelObject", new DeliveryAddress());
			
		//return urlContext + "/form";
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
		//setupCreate(model, request);
		//model.addAttribute("activeTab", "manageCustomer");
		//model.addAttribute("mode", "ADD");
		//model.addAttribute("activeSubTab", "billing");
		//return urlContext + "/form";
		
		Map<String, Object> criterias = new HashMap<String, Object>();
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
		
		//return urlContext + "/form";
		return urlContext + "/customer";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria,"companyName",null,null));
		model.addAttribute("activeTab", "manageCustomer");
		
		//model.addAttribute("activeSubTab", "billing");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
		return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerListReportMain.do")
	public String customerListReportMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		
		List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>();
		
		//model.addAttribute("customerListReportList", customerReportVOList);
		request.getSession().setAttribute("customerListReportList", customerReportVOList);
		
		//model.addAttribute("activeTab", "customerReports");
		//model.addAttribute("activeSubTab", "customerListReport");
		//model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/customerListReport";
		//return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerListReport.do")
	public String customerListReport(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		
		List<CustomerReportVO> customerReportVOList = retrieveCustomerListReportData(criteria);
		
		//model.addAttribute("customerListReportList", customerReportVOList);
		request.getSession().setAttribute("customerListReportList", customerReportVOList);
		
		//model.addAttribute("activeTab", "customerReports");
		//model.addAttribute("activeSubTab", "customerListReport");
		//model.addAttribute("mode", "MANAGE");
		
		return urlContext + "/customerListReport";
		//return urlContext + "/customer";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/customerOrdersReport.do")
	public String customerOrdersReport(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		
		//List<CustomerReportVO> customerReportVOList = retrieveCustomerListReportData(criteria);
		List<CustomerReportVO> customerReportVOList = retrieveCustomerOrdersReportData(criteria);
		//request.getSession().setAttribute("customerOrdersReportList", customerReportVOList);
		
		String[] orderDates = extractOrderDateRange(criteria, customerReportVOList);
		
		model.addAttribute("customerOrdersReportList", customerReportVOList);
		model.addAttribute("customerOrdersReportOrderDateFrom", orderDates[0]);
		model.addAttribute("customerOrdersReportOrderDateTo", orderDates[1]);
		
		if (!customerReportVOList.isEmpty()) {
			model.addAttribute("customerOrdersReportCompanyName", customerReportVOList.get(0).getCompanyName());
			model.addAttribute("customerOrdersReportTotalOrders", customerReportVOList.get(0).getTotalOrders());
			model.addAttribute("customerOrdersReportTotalOrderAmount", customerReportVOList.get(0).getTotalOrderAmount());
		}
		
		model.addAttribute("activeTab", "customerReports");
		model.addAttribute("activeSubTab", "customerOrdersReport");
		model.addAttribute("mode", "MANAGE");
		
		//return urlContext + "/list";
		return urlContext + "/customer";
	}
	
	private String[] extractOrderDateRange(SearchCriteria criteria, List<CustomerReportVO> customerReportVOList) {
		String orderDateFrom = criteria.getSearchMap().getOrDefault("createdAtFrom", StringUtils.EMPTY).toString();
		String orderDateTo = criteria.getSearchMap().getOrDefault("createdAtTo", StringUtils.EMPTY).toString();
		
		if (StringUtils.isEmpty(orderDateFrom)) {
			if (!customerReportVOList.isEmpty() && !customerReportVOList.get(0).getOrderList().isEmpty()) {
				List<OrderReportVO> orderList = customerReportVOList.get(0).getOrderList();
				orderDateFrom = orderList.get(0).getOrderDate();
				orderDateTo = orderList.get(orderList.size() - 1).getOrderDate();
			}
		}
		
		String orderDates[] = new String[2];
		orderDates[0] = orderDateFrom;
		orderDates[1] = orderDateTo;
		return orderDates;
	}
	
	private List<CustomerReportVO> retrieveCustomerListReportData(SearchCriteria criteria) {
		List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>();
		
		List<Customer> customerList = genericDAO.search(getEntityClass(), criteria, "companyName", null, null);
		if (customerList.isEmpty()) {
			return customerReportVOList;
		}
		
		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		
		StringBuffer ids = new StringBuffer("(");
		Integer count = 0;
		for (Customer customer : customerList) {
			count++;
			ids.append(customer.getId());
			if (count < customerList.size()) {
				ids.append(",");
			} else {
				ids.append(")");
			}
			
			customerMap.put(customer.getId(), customer);
		}
        
      List<OrderFees> orderFeesList = genericDAO.executeSimpleQuery("select obj from OrderFees obj where obj.deleteFlag='1' and obj.order.customer.id IN " + ids.toString());
      
      for (Long key : customerMap.keySet()) {
      	CustomerReportVO customerReportVO =  new CustomerReportVO();
			BigDecimal sum = new BigDecimal("0.00");
			Integer ordercount = 0;
			for (OrderFees anOrderFees: orderFeesList ) {
	          if (anOrderFees.getOrder().getCustomer().getId() == key) {
	               sum = sum.add(anOrderFees.getTotalFees());
	               ordercount++;
	          }
	      }
			
			Customer aCustomer = customerMap.get(key);
			customerReportVO.setId(aCustomer.getId());
	      customerReportVO.setCompanyName(aCustomer.getCompanyName());
	      customerReportVO.setStatus(aCustomer.getCustomerStatus().getStatus());
         customerReportVO.setContactName(aCustomer.getContactName());
         customerReportVO.setPhoneNumber(aCustomer.getPhone());
         customerReportVO.setTotalOrderAmount(sum);
         customerReportVO.setTotalOrders(ordercount);
         

         customerReportVOList.add(customerReportVO);
	   }
      
      return customerReportVOList;
	}
	
	private List<CustomerReportVO> retrieveCustomerOrdersReportData(SearchCriteria criteria) {
		List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>();
		
		List<Order> orderList = genericDAO.search(Order.class, criteria, "customer.companyName", null, null);
		if (orderList.isEmpty()) {
			return customerReportVOList;
		}
		
		Map<Long, List<Order>> customerOrderMap = new HashMap<Long, List<Order>>();
		for (Order anOrder : orderList) {
			Long customerId = anOrder.getCustomer().getId();
			List<Order> customerOrderList = customerOrderMap.get(customerId);
			if (customerOrderList == null) {
				customerOrderList = new ArrayList<Order>();
				customerOrderMap.put(customerId, customerOrderList);
			}
			
			customerOrderList.add(anOrder);
		}
		
		for (Long customerKey : customerOrderMap.keySet()) {
			CustomerReportVO aCcustomerReportVO =  new CustomerReportVO();
			map(customerOrderMap.get(customerKey), aCcustomerReportVO);

         customerReportVOList.add(aCcustomerReportVO);
		}
      
      return customerReportVOList;
	}
	
	private void map(Customer aCustomer, CustomerReportVO aCustomerReportVO) {
		aCustomerReportVO.setId(aCustomer.getId());
		aCustomerReportVO.setCompanyName(aCustomer.getCompanyName());
		aCustomerReportVO.setStatus(aCustomer.getCustomerStatus().getStatus());
		aCustomerReportVO.setContactName(aCustomer.getContactName());
		aCustomerReportVO.setPhoneNumber(aCustomer.getPhone());
	}
	
	private void map(List<Order> orderList, CustomerReportVO aCustomerReportVO) {
		Customer aCustomer = orderList.get(0).getCustomer();
		map(aCustomer, aCustomerReportVO);
		
		BigDecimal totalOrderAmount = new BigDecimal("0.00");
		List<OrderReportVO> anOrderReportVOList = new ArrayList<OrderReportVO>();
		for (Order anOrder : orderList) {
			OrderReportVO anOrderReportVO = new OrderReportVO();
			map(anOrder, anOrderReportVO);
			anOrderReportVOList.add(anOrderReportVO);
			
			totalOrderAmount = totalOrderAmount.add(anOrderReportVO.getTotalFees());
		}
		
		aCustomerReportVO.setTotalOrderAmount(totalOrderAmount);
      aCustomerReportVO.setTotalOrders(orderList.size());
		
		aCustomerReportVO.setOrderList(anOrderReportVOList);
	}
	
	private void map(Order anOrder, OrderReportVO anOrderReportVO) {
		anOrderReportVO.setId(anOrder.getId());
		anOrderReportVO.setOrderDate(anOrder.getFormattedCreatedAt());
		anOrderReportVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anOrderReportVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anOrderReportVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anOrderReportVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anOrderReportVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anOrderReportVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anOrderReportVO.setPickupDate(anOrder.getFormattedPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		anOrderReportVO.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		
		anOrderReportVO.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal("0.00")) : anOrderFees.getCityFee());
		
		anOrderReportVO.setPermitFees(anOrderFees.getTotalPermitFees());
		anOrderReportVO.setOverweightFee(anOrderFees.getOverweightFee());
		anOrderReportVO.setAdditionalFees(anOrderFees.getTotalAdditionalFees());
		anOrderReportVO.setTotalFees(anOrderFees.getTotalFees());
		
		anOrderReportVO.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		anOrderReportVO.setBalanceAmountDue(anOrder.getBalanceAmountDue());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateCustomerListReport.do")
	public void generateCustomerListReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<CustomerReportVO> customerReportVOList = (List<CustomerReportVO>) request.getSession().getAttribute("customerListReportList");
			
			List<Map<String, ?>> reportData = new ArrayList<Map<String, ?>>();
			for (CustomerReportVO aCustomerReportVO : customerReportVOList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", aCustomerReportVO.getId().toString());
				map.put("companyName", aCustomerReportVO.getCompanyName());
				map.put("contactName", aCustomerReportVO.getContactName());
				map.put("phoneNumber", aCustomerReportVO.getPhoneNumber());
				map.put("status", aCustomerReportVO.getStatus());
				map.put("totalOrders", aCustomerReportVO.getTotalOrders());
				map.put("totalOrderAmount", aCustomerReportVO.getTotalOrderAmount());
				
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
			
			out = dynamicReportService.generateStaticReport("customerListReport", reportData, params, type, request);
			
			/*if (!type.equals("print") && !type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("customerListReport", reportData, params, type, request);
			} else if (type.equals("pdf")){
				out = dynamicReportService.generateStaticReport("customerListReportPdf",
						reportData, params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport("customerListReport"+"print",
						reportData, params, type, request);
			}*/
		
			out.writeTo(response.getOutputStream());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateCustomerOrdersReport.do")
	public void generateCustomerOrdersReport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		criteria.getSearchMap().remove("_csrf");
		
		List<CustomerReportVO> customerReportVOList = retrieveCustomerOrdersReportData(criteria);
		
		String[] orderDates = extractOrderDateRange(criteria, customerReportVOList);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ORDER_DATE_FROM", orderDates[0]);
		params.put("ORDER_DATE_TO", orderDates[1]);
		
		try {
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
					"attachment;filename= customerOrdersReport." + type);
			}
			
			response.setContentType(MimeUtil.getContentType(type));
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out = dynamicReportService.generateStaticMasterSubReport("customerOrdersReportMaster", "customerOrdersReportSub",
					customerReportVOList, params, type, request);
		
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
		
		//return urlContext + "/form";
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
		//criteria.getSearchMap().put("id!",0l);
		//criteria.setPageSize(25);
		
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
		
		//criteria.getSearchMap().put("id!",0l);
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "companyName", null, null));
		model.addAttribute("activeTab", "manageCustomer");
		model.addAttribute("mode", "MANAGE");
		
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
			
			//TODO: is this reqd?
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
		// return to form if we had errors
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
		
		entity.setNotesType(CustomerNotes.NOTES_TYPE_USER);
		updateEnteredBy(entity);
		
		genericDAO.saveOrUpdate(entity);
		
		cleanUp(request);
		
		Long customerId = entity.getCustomer().getId();
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.id=" + customerId + " and obj.deleteFlag='1'");
		Customer savedCustomer = customerList.get(0);
		model.addAttribute("modelObject", savedCustomer);

		setupCreate(model, request);
		
		//model.addAttribute("modelObject", entity);
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
		// return to form if we had errors
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
		Long customerId = entity.getCustomer().getId();
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customerId);
		createAuditCustomerNotes(emptyCustomer, customerAuditMsg, modifiedBy);
		
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
		
		model.addAttribute("msgCtx", "manageCustomerDeliveryAddress");
		model.addAttribute("msg", "Delivery address saved successfully");
		
		//return urlContext + "/form";
		return urlContext + "/customer";
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
		List<State> stateList = genericDAO.executeSimpleQuery("select obj from State obj where obj.id= " + entity.getState().getId() + " and obj.deleteFlag='1' order by obj.name");
		entity.getState().setName(stateList.get(0).getName());
		
		return constructResponseJson(entity);
				
		/*String json = (new Gson()).toJson(entity);
		return json;*/
	}
	
	private String constructResponseJson(Customer entity) {
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
}
