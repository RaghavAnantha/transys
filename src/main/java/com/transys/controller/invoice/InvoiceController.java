package com.transys.controller.invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.transys.controller.BaseController;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.service.DynamicReportService;

import com.transys.core.util.CoreUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;
import com.transys.core.util.ServletUtil;
import com.transys.model.AbstractBaseModel;
import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderNotes;
import com.transys.model.OrderPayment;
import com.transys.model.OrderStatus;
import com.transys.model.PaymentMethodType;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;
import com.transys.model.User;
import com.transys.model.invoice.OrderInvoiceDetails;
import com.transys.model.invoice.OrderInvoiceHeader;
import com.transys.model.invoice.OrderInvoicePayment;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.invoice.InvoiceVO;

@Controller
@RequestMapping("/invoice")
@Transactional(readOnly = false)
public class InvoiceController extends BaseController {
	private static final String INVOICE_TAB = "manageInvoice";
	private static final String INVOICE_PAYMENT_TAB = "invoicePayment";
	
	private static String ORDER_INVOICE_MASTER = "orderInvoiceMaster";
	private static String ORDER_INVOICE_SUB = "orderInvoiceSub";
	
	public InvoiceController() {
		setUrlContext("invoice");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		
		binder.registerCustomEditor(PaymentMethodType.class, new AbstractModelEditor(PaymentMethodType.class));
		binder.registerCustomEditor(OrderInvoiceHeader.class, new AbstractModelEditor(OrderInvoiceHeader.class));
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/createInvoiceMain.do")
	public String createInvoiceMain(ModelMap model, HttpServletRequest request) {
		//clearSearchCriteria(request);
		
		setupCreateInvoiceList(model, request);
		
		return getUrlContext() + "/invoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/manageInvoiceMain.do")
	public String manageInvoiceMain(ModelMap model, HttpServletRequest request) {
		clearSearchCriteria(request);
		return processManageInvoiceSearch(model, request);
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/invoicePaymentMain.do")
	public String invoicePaymentMain(ModelMap model, HttpServletRequest request) {
		//clearSearchCriteria(request);
		return processInvoicePaymentSearch(model, request);
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/createInvoicePayment.do")
	public String createInvoicePayment(ModelMap model, HttpServletRequest request) {
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.method asc"));
	   
		OrderInvoicePayment invoicePayment = new OrderInvoicePayment();
		model.addAttribute("invoicePaymentModelObject", invoicePayment);
		
		addTabAttributes(model, INVOICE_PAYMENT_TAB, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, "createInvoicePayment");
		
		return getUrlContext() + "/invoicePaymentForm";
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/editInvoicePayment.do")
	public String editInvoicePayment(ModelMap model, HttpServletRequest request,
			@RequestParam("id") Long invoicePaymentId) {
		OrderInvoicePayment invoicePayment = genericDAO.getById(OrderInvoicePayment.class, invoicePaymentId);
		model.addAttribute("invoicePaymentModelObject", invoicePayment);
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.method asc"));
	   
		addTabAttributes(model, INVOICE_PAYMENT_TAB, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, "createInvoicePayment");
		
		return getUrlContext() + "/invoicePaymentForm";
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceSearch.do")
	public String createInvoiceSearch(ModelMap model, HttpServletRequest request,
			@ModelAttribute("modelObject") InvoiceVO input,
			BindingResult bindingResult) {
		setupCreateInvoiceList(model, request);
		
		String returnUrl = getUrlContext() + "/invoice";
		
		SearchCriteria criteria = (SearchCriteria)request.getSession().getAttribute("searchCriteria");
		processSaveInvoiceReferrer(request, model, criteria, input);
		
		validateSearch(input, bindingResult);
		if(bindingResult.hasErrors()) {
        	return returnUrl;
      }
		
		request.getSession().setAttribute("input", input);
		
		List<Order> orderList = performCreateInvoiceSearch(criteria, input);
		model.addAttribute("list", orderList);
		
		return returnUrl;
	}
	
	private void processSaveInvoiceReferrer(HttpServletRequest request, ModelMap model,
			SearchCriteria criteria, InvoiceVO input) {
		String saveSuccessMsg = (String) request.getSession().getAttribute("msg");
		String referrer = request.getHeader("Referer");
		if (!StringUtils.contains(referrer, "previewInvoice.do")
				|| !StringUtils.contains(saveSuccessMsg, InvoiceVO.INV_SAVE_SUCCESS_MSG)) {
			return;
		}
		
		String customerId = (String) criteria.getSearchMap().get("customerId");
		String deliveryAddress = (String) criteria.getSearchMap().get("deliveryAddress");
		String orderId = (String) criteria.getSearchMap().get("orderId");
		String orderDateFrom = (String) criteria.getSearchMap().get("orderDateFrom");
		String orderDateTo = (String) criteria.getSearchMap().get("orderDateTo");
		
		input.setCustomerId(customerId);
		input.setDeliveryAddress(deliveryAddress);
		input.setOrderId(orderId);
		input.setOrderDateFrom(orderDateFrom);
		input.setOrderDateTo(orderDateTo);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceSearch.do")
	public String manageInvoiceSearch(ModelMap model, HttpServletRequest request) {
		return processManageInvoiceSearch(model, request);
	}
	
	private String processManageInvoiceSearch(ModelMap model, HttpServletRequest request) {
		setupManageInvoiceList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoiceHeader> orderInvoiceHeaderList = performManageInvoiceSearch(criteria);
		model.addAttribute("list", orderInvoiceHeaderList);
		
		return getUrlContext() + "/invoice";
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/invoicePaymentSearch.do")
	public String invoicePaymentSearch(ModelMap model, HttpServletRequest request) {
		return processInvoicePaymentSearch(model, request);
	}
	
	private String processInvoicePaymentSearch(ModelMap model, HttpServletRequest request) {
		setupInvoicePaymentList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoicePayment> orderInvoicePaymentList = performInvoicePaymentSearch(criteria);
		model.addAttribute("list", orderInvoicePaymentList);
		
		return getUrlContext() + "/invoicePaymentList";
	}
	
	private void setupCommon(ModelMap model, HttpServletRequest request,
					String customerIdKey, String orderInvoiced) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria searchCriteria = (SearchCriteria)request.getSession().getAttribute("searchCriteria");
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		String customerId = (String)searchCriteria.getSearchMap().get(customerIdKey);
		if (StringUtils.isNotEmpty(customerId)) {
			List<DeliveryAddressVO> deliveryAddressVOList = 
					ModelUtil.retrieveOrderDeliveryAddresses(genericDAO, customerId);
			model.addAttribute("deliveryAddresses", deliveryAddressVOList);
			model.addAttribute("orderIds", retrieveOrderIds(orderInvoiced, customerId));
		}
	}
	
	private void setupCreateInvoiceList(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request, "customerId", "N");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(150);
		
		addTabAttributes(model, MODE_ADD, StringUtils.EMPTY);

		addMsgCtx(model, "createInvoice");
	}
	
	private void setupManageInvoiceList(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request, "manageInvoiceCustomerId", "Y");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(150);
		
		/*String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1 order by obj.id asc";
		model.addAttribute("invoiceNos", genericDAO.executeSimpleQuery(query));*/
		
		addTabAttributes(model, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, "manageInvoice");
	}
	
	private void setupInvoicePaymentList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		/*String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1 order by obj.id asc";
		model.addAttribute("invoiceNos", genericDAO.executeSimpleQuery(query));*/
		
		addTabAttributes(model, INVOICE_PAYMENT_TAB, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, "manageInvoicePayment");
	}
	
	private List<Long> retrieveOrderIds(String invoiced, String customerId) {
		String query = "select obj.id from Order obj where obj.deleteFlag=1 "
				+ " and obj.invoiced='" + invoiced + "'";
		if (StringUtils.equals(invoiced, "N")) {
			OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
			query	+= " and obj.orderStatus.id !=" + orderStatus.getId().longValue();
			query	+= " and obj.balanceAmountDue > 0.0";
		}
		if (StringUtils.isNotEmpty(customerId)) {
			query	+= " and obj.customer.id = " + customerId;
		}
		query	+= " order by obj.id asc";
		
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		List<Long> orderIdList = ModelUtil.extractObjIds(orderList);
		return orderIdList;
	}
	
	private List<Long> retrievePayableInvoiceNos(String customerId) {
		String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1"
				+ " and obj.totalInvoiceBalanceDue > 0.0";
		if (StringUtils.isNotEmpty(customerId)) {
			query	+= " and obj.customerId = " + customerId;
		}
		query	+= " order by obj.id asc";
		
		List<OrderInvoiceHeader> invoiceList = genericDAO.executeSimpleQuery(query);
		List<Long> invoiceNoList = ModelUtil.extractObjIds(invoiceList);
		return invoiceNoList;
	}
	
	private List<Order> performCreateInvoiceSearch(SearchCriteria criteria, InvoiceVO input) {
		String customerId = input.getCustomerId();
		String deliveryAddress = input.getDeliveryAddress();
		String orderId = input.getOrderId();
		String orderDateFrom = input.getOrderDateFrom();
		String orderDateTo = input.getOrderDateTo();
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		whereClause.append(" and obj.balanceAmountDue > " + 0.0);
		whereClause.append(" and obj.invoiced='N'");
		
		OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		whereClause.append(" and obj.orderStatus.id !=" + orderStatus.getId().longValue());
		
		if (StringUtils.isNotEmpty(customerId)) {
			whereClause.append(" and obj.customer.id=" + customerId);
		}
		if (StringUtils.isNotEmpty(deliveryAddress)) {
			whereClause.append(" and obj.deliveryAddress.id=" + deliveryAddress);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			whereClause.append(" and obj.id=" + orderId);
		}
		if (StringUtils.isNotEmpty(orderDateFrom)){
        	whereClause.append(" and obj.createdAt >='"+FormatUtil.formatInputDateToDbDate(orderDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	whereClause.append(" and obj.createdAt <='"+FormatUtil.formatInputDateToDbDate(orderDateTo)+"'");
	   }
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Order> orderList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return orderList;
	}
	
	private List<OrderInvoiceHeader> performManageInvoiceSearch(SearchCriteria criteria) {
		Map<String, Object> criteriaMap = criteria.getSearchMap();
		String invoiceId = (String)criteriaMap.get("manageInvoiceInvoiceNo");
		String customerId = (String)criteriaMap.get("manageInvoiceCustomerId");
		String orderId = (String)criteriaMap.get("manageInvoiceOrderId");
		String deliveryAddress = (String)criteriaMap.get("manageInvoiceDeliveryAddress");
		String orderDateFrom = (String)criteriaMap.get("manageInvoiceOrderDateFrom");
		String orderDateTo = (String)criteriaMap.get("manageInvoiceOrderDateTo");
		String invoiceDateFrom = (String)criteriaMap.get("manageInvoiceInvoiceDateFrom");
		String invoiceDateTo = (String)criteriaMap.get("manageInvoiceInvoiceDateTo");
		
		StringBuffer query = new StringBuffer("select distinct obj from OrderInvoiceHeader obj join obj.orderInvoiceDetails oidet where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(distinct obj.id) from OrderInvoiceHeader obj join obj.orderInvoiceDetails oidet where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		if (StringUtils.isNotEmpty(invoiceId)) {
			whereClause.append(" and obj.id=" + invoiceId);
		}
		if (StringUtils.isNotEmpty(customerId)) {
			whereClause.append(" and obj.customerId=" + customerId);
		}
		if (StringUtils.isNotEmpty(deliveryAddress)) {
			whereClause.append(" and oidet.deliveryAddressId=" + deliveryAddress);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			whereClause.append(" and oidet.orderId=" + orderId);
		}
		if (StringUtils.isNotEmpty(orderDateFrom)){
        	whereClause.append(" and obj.orderDateFrom >='"+FormatUtil.formatInputDateToDbDate(orderDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	whereClause.append(" and obj.orderDateTo <='"+FormatUtil.formatInputDateToDbDate(orderDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(invoiceDateFrom)){
        	whereClause.append(" and obj.invoiceDate >='"+FormatUtil.formatInputDateToDbDate(invoiceDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(invoiceDateTo)){
	     	whereClause.append(" and obj.invoiceDate <='"+FormatUtil.formatInputDateToDbDate(invoiceDateTo)+"'");
	   }
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<OrderInvoiceHeader> orderInvoiceHeaderList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return orderInvoiceHeaderList;
	}
	
	private List<OrderInvoicePayment> performInvoicePaymentSearch(SearchCriteria criteria) {
		Map<String, Object> criteriaMap = criteria.getSearchMap();
		String invoiceId = (String)criteriaMap.get("invoicePaymentInvoiceNo");
		String customerId = (String)criteriaMap.get("invoicePaymentCustomerId");
		String paymentDateFrom = (String)criteriaMap.get("invoicePaymentDateFrom");
		String paymentDateTo = (String)criteriaMap.get("invoicePaymentDateTo");
		String orderDateFrom = (String)criteriaMap.get("invoicePaymentOrderDateFrom");
		String orderDateTo = (String)criteriaMap.get("invoicePaymentOrderDateTo");
		String invoiceDateFrom = (String)criteriaMap.get("invoicePaymentInvoiceDateFrom");
		String invoiceDateTo = (String)criteriaMap.get("invoicePaymentInvoiceDateTo");
		
		StringBuffer query = new StringBuffer("select obj from OrderInvoicePayment obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj.id) from OrderInvoicePayment obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		if (StringUtils.isNotEmpty(invoiceId)) {
			whereClause.append(" and obj.invoice=" + invoiceId);
		}
		if (StringUtils.isNotEmpty(customerId)) {
			whereClause.append(" and obj.invoice.customerId=" + customerId);
		}
		 if (StringUtils.isNotEmpty(paymentDateFrom)){
	        	whereClause.append(" and obj.invoice.paymentDate >='"+FormatUtil.formatInputDateToDbDate(paymentDateFrom)+"'");
			}
	      if (StringUtils.isNotEmpty(paymentDateTo)){
		     	whereClause.append(" and obj.invoice.paymentDate <='"+FormatUtil.formatInputDateToDbDate(paymentDateTo)+"'");
		   }
		if (StringUtils.isNotEmpty(orderDateFrom)){
        	whereClause.append(" and obj.invoice.orderDateFrom >='"+FormatUtil.formatInputDateToDbDate(orderDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	whereClause.append(" and obj.invoice.orderDateTo <='"+FormatUtil.formatInputDateToDbDate(orderDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(invoiceDateFrom)){
        	whereClause.append(" and obj.invoice.invoiceDate >='"+FormatUtil.formatInputDateToDbDate(invoiceDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(invoiceDateTo)){
	     	whereClause.append(" and obj.invoice.invoiceDate <='"+FormatUtil.formatInputDateToDbDate(invoiceDateTo)+"'");
	   }
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.paymentDate desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<OrderInvoicePayment> orderInvoicePaymentList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return orderInvoicePaymentList;
	}
	
	private void validateSearch(InvoiceVO input, BindingResult bindingResult) {
		if (StringUtils.isNotEmpty(input.getOrderId())) {
			return;
		}
		
		if (StringUtils.isEmpty(input.getCustomerId())) {
			bindingResult.rejectValue("customerId", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(input.getOrderDateFrom())) {
			bindingResult.rejectValue("orderDateFrom", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(input.getOrderDateTo())) {
			bindingResult.rejectValue("orderDateTo", "NotNull.java.lang.String", null, null);
		}
	}
	
	private List<Order> performPreviewInvoiceSearch(InvoiceVO input) {
		String[] orderIdsArr = input.getIds();
		if (orderIdsArr == null || orderIdsArr.length <= 0) {
			return null;
		}
		
		List<Order> orderList = retrieveOrder(orderIdsArr) ;
		return orderList;
	}
	
	private List<Order> retrieveOrder(String[] orderIdsArr) {
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		String orderIds = CoreUtil.toString(orderIdsArr);
		whereClause.append(" and obj.id in(" + orderIds + ")");
      
      query.append(whereClause);
      
      query.append(" order by obj.id desc");
      
      List<Order> orderList = genericDAO.executeSimpleQuery(query.toString());
      return orderList;
	}
	
	private void map(List<Order> orderList, List<InvoiceVO> invoiceVOList,
			List<InvoiceVO> invoicePaymentVOList) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		for (Order anOrder : orderList) {
			InvoiceVO anInvoiceVO = new InvoiceVO();
			map(anOrder, anInvoiceVO, invoicePaymentVOList);
			invoiceVOList.add(anInvoiceVO);
		}
	}
	
	private void map(OrderInvoiceDetails orderInvoiceDetails, InvoiceVO anInvoiceVO,
				List<InvoiceVO> invoicePaymentVOList) {
		anInvoiceVO.setId(orderInvoiceDetails.getId());
		anInvoiceVO.setOrderId(orderInvoiceDetails.getOrderId().toString());
		anInvoiceVO.setOrderDate(orderInvoiceDetails.getOrderDate());
		anInvoiceVO.setStatus(orderInvoiceDetails.getOrderStatus());
		
		anInvoiceVO.setDeliveryContactName(orderInvoiceDetails.getDeliveryContactName());
		anInvoiceVO.setDeliveryContactPhone1(orderInvoiceDetails.getDeliveryContactPhone1());
		anInvoiceVO.setDeliveryAddressFullLine(orderInvoiceDetails.getDeliveryAddressFullLine());
		anInvoiceVO.setDeliveryCity(orderInvoiceDetails.getDeliveryAddressCity());
		
		anInvoiceVO.setDeliveryDate(orderInvoiceDetails.getFormattedDeliveryDate());
		anInvoiceVO.setPickupDate(orderInvoiceDetails.getFormattedPickupDate());
		
		anInvoiceVO.setDumpsterPrice(orderInvoiceDetails.getDumpsterPrice());
		
		anInvoiceVO.setCityFee(orderInvoiceDetails.getCityFee() == null ? (new BigDecimal(0.00)) : orderInvoiceDetails.getCityFee());
		
		anInvoiceVO.setPermitFees(orderInvoiceDetails.getTotalPermitFees());
		anInvoiceVO.setOverweightFee(orderInvoiceDetails.getOverweightFee());
		anInvoiceVO.setAdditionalFees(orderInvoiceDetails.getTotalAdditionalFees());
		anInvoiceVO.setTonnageFees(orderInvoiceDetails.getTonnageFees());
		anInvoiceVO.setTotalFees(orderInvoiceDetails.getTotalFees());
		
		anInvoiceVO.setTotalAmountPaid(orderInvoiceDetails.getTotalAmountPaid());
		anInvoiceVO.setDiscount(orderInvoiceDetails.getDiscountAmount());
		anInvoiceVO.setBalanceAmountDue(orderInvoiceDetails.getBalanceAmountDue());
		
		InvoiceVO anInvoicePaymentVO = null;
		if (StringUtils.isNotEmpty(orderInvoiceDetails.getPaymentMethod1())) {
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(orderInvoiceDetails.getOrderId().toString());
			anInvoicePaymentVO.setPaymentMethod(orderInvoiceDetails.getPaymentMethod1());
			anInvoicePaymentVO.setPaymentDate(orderInvoiceDetails.getPaymentDate1());
			anInvoicePaymentVO.setPaymentAmount(orderInvoiceDetails.getPaymentAmount1());
			anInvoicePaymentVO.setCheckNum(orderInvoiceDetails.getPaymentCheckNum1());
			anInvoicePaymentVO.setReferenceNum(orderInvoiceDetails.getPaymentCCRefNum1());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
		if (StringUtils.isNotEmpty(orderInvoiceDetails.getPaymentMethod2())) {
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(orderInvoiceDetails.getOrderId().toString());
			anInvoicePaymentVO.setPaymentMethod(orderInvoiceDetails.getPaymentMethod2());
			anInvoicePaymentVO.setPaymentDate(orderInvoiceDetails.getPaymentDate2());
			anInvoicePaymentVO.setPaymentAmount(orderInvoiceDetails.getPaymentAmount2());
			anInvoicePaymentVO.setCheckNum(orderInvoiceDetails.getPaymentCheckNum2());
			anInvoicePaymentVO.setReferenceNum(orderInvoiceDetails.getPaymentCCRefNum2());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
		if (StringUtils.isNotEmpty(orderInvoiceDetails.getPaymentMethod3())) {
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(orderInvoiceDetails.getOrderId().toString());
			anInvoicePaymentVO.setPaymentMethod(orderInvoiceDetails.getPaymentMethod3());
			anInvoicePaymentVO.setPaymentDate(orderInvoiceDetails.getPaymentDate3());
			anInvoicePaymentVO.setPaymentAmount(orderInvoiceDetails.getPaymentAmount3());
			anInvoicePaymentVO.setCheckNum(orderInvoiceDetails.getPaymentCheckNum3());
			anInvoicePaymentVO.setReferenceNum(orderInvoiceDetails.getPaymentCCRefNum3());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
	}
	
	private void map(Order anOrder, InvoiceVO anInvoiceVO,
			List<InvoiceVO> invoicePaymentVOList) {
		anInvoiceVO.setOrderId(anOrder.getId().toString());
		anInvoiceVO.setOrderDate(anOrder.getCreatedAt());
		anInvoiceVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anInvoiceVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anInvoiceVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anInvoiceVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anInvoiceVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anInvoiceVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anInvoiceVO.setPickupDate(anOrder.getFormattedPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		anInvoiceVO.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		
		anInvoiceVO.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal(0.00)) : anOrderFees.getCityFee());
		
		anInvoiceVO.setPermitFees(anOrderFees.getTotalPermitFees());
		anInvoiceVO.setOverweightFee(anOrderFees.getOverweightFee());
		anInvoiceVO.setAdditionalFees(anOrderFees.getTotalAdditionalFees());
		anInvoiceVO.setTonnageFees(anOrderFees.getTonnageFee());
		anInvoiceVO.setTotalFees(anOrderFees.getTotalFees());
		anInvoiceVO.setDiscount(anOrderFees.getDiscountAmount());
		
		anInvoiceVO.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		anInvoiceVO.setBalanceAmountDue(anOrder.getBalanceAmountDue());
		
		List<OrderPayment> orderPaymentList = anOrder.getOrderPayment();
		if (orderPaymentList == null || orderPaymentList.isEmpty()) {
			return;
		}
		
		InvoiceVO anInvoicePaymentVO = null;
		OrderPayment anOrderPayment = null;
		if (orderPaymentList.size() > 0) {
			anOrderPayment = orderPaymentList.get(0);
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(anOrder.getId().toString());
			anInvoicePaymentVO.setPaymentMethod(anOrderPayment.getPaymentMethod().getMethod());
			anInvoicePaymentVO.setPaymentDate(anOrderPayment.getPaymentDate());
			anInvoicePaymentVO.setPaymentAmount(anOrderPayment.getAmountPaid());
			anInvoicePaymentVO.setCheckNum(anOrderPayment.getCheckNum());
			anInvoicePaymentVO.setReferenceNum(anOrderPayment.getCcReferenceNum());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
		if (orderPaymentList.size() > 1) {
			anOrderPayment = orderPaymentList.get(1);
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(anOrder.getId().toString());
			anInvoicePaymentVO.setPaymentMethod(anOrderPayment.getPaymentMethod().getMethod());
			anInvoicePaymentVO.setPaymentDate(anOrderPayment.getPaymentDate());
			anInvoicePaymentVO.setPaymentAmount(anOrderPayment.getAmountPaid());
			anInvoicePaymentVO.setCheckNum(anOrderPayment.getCheckNum());
			anInvoicePaymentVO.setReferenceNum(anOrderPayment.getCcReferenceNum());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
		if (orderPaymentList.size() > 2) {
			anOrderPayment = orderPaymentList.get(2);
			anInvoicePaymentVO = new InvoiceVO();
			anInvoicePaymentVO.setOrderId(anOrder.getId().toString());
			anInvoicePaymentVO.setPaymentMethod(anOrderPayment.getPaymentMethod().getMethod());
			anInvoicePaymentVO.setPaymentDate(anOrderPayment.getPaymentDate());
			anInvoicePaymentVO.setPaymentAmount(anOrderPayment.getAmountPaid());
			anInvoicePaymentVO.setCheckNum(anOrderPayment.getCheckNum());
			anInvoicePaymentVO.setReferenceNum(anOrderPayment.getCcReferenceNum());
			invoicePaymentVOList.add(anInvoicePaymentVO);
		}
	}
	
	private void map(List<Order> orderList, OrderInvoiceHeader orderInvoiceHeader, List<OrderInvoiceDetails> orderInvoiceDetailsList) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		BigDecimal totalFees = new BigDecimal(0.00);
		BigDecimal totalDiscount = new BigDecimal(0.00);
		BigDecimal totalAmountPaid = new BigDecimal(0.00);
		BigDecimal totalBalanceAmountDue = new BigDecimal(0.00);
		List<Date> orderDateList = new ArrayList<Date>();
		for (Order anOrder : orderList) {
			orderDateList.add(anOrder.getCreatedAt());
			
			OrderInvoiceDetails anOrderInvoiceDetails = new OrderInvoiceDetails();
			anOrderInvoiceDetails.setInvoiceHeader(orderInvoiceHeader);
			
			map(anOrder, anOrderInvoiceDetails);
			orderInvoiceDetailsList.add(anOrderInvoiceDetails);
			
			if (anOrderInvoiceDetails.getTotalFees() != null) {
				totalFees = totalFees.add(anOrderInvoiceDetails.getTotalFees());
			}
			if (anOrderInvoiceDetails.getTotalAmountPaid() != null) {
				totalAmountPaid = totalAmountPaid.add(anOrderInvoiceDetails.getTotalAmountPaid());
			}
			if (anOrderInvoiceDetails.getDiscountAmount() != null) {
				totalDiscount = totalDiscount.add(anOrderInvoiceDetails.getDiscountAmount());
			}
			if (anOrderInvoiceDetails.getBalanceAmountDue() != null) {
				totalBalanceAmountDue = totalBalanceAmountDue.add(anOrderInvoiceDetails.getBalanceAmountDue());
			}
		}
		
		Collections.sort(orderDateList);
		orderInvoiceHeader.setOrderDateFrom(orderDateList.get(0));
		orderInvoiceHeader.setOrderDateTo(orderDateList.get(orderDateList.size()-1));
		
		orderInvoiceHeader.setTotalFees(totalFees);
		orderInvoiceHeader.setTotalDiscount(totalDiscount);
		orderInvoiceHeader.setTotalAmountPaid(totalAmountPaid);
		orderInvoiceHeader.setTotalBalanceAmountDue(totalBalanceAmountDue);
		
		orderInvoiceHeader.setTotalInvoiceBalanceDue(totalBalanceAmountDue);
		orderInvoiceHeader.setTotalInvoicePaymentDone(new BigDecimal(0.00));
		orderInvoiceHeader.setTotalInvoiceBalanceAvailable(new BigDecimal(0.00));
	}
	
	private void map(InvoiceVO input, OrderInvoiceHeader orderInvoiceHeader) {
		orderInvoiceHeader.setInvoiceDate(input.getInvoiceDate());
		orderInvoiceHeader.setNotes(input.getNotes());
	}
	
	private void mapToInvoiceVOList(List<OrderInvoiceDetails> orderInvoiceDetailsList, 
			List<InvoiceVO> invoiceVOList, List<InvoiceVO> invoiceVOPaymentList) {
		if (orderInvoiceDetailsList == null || orderInvoiceDetailsList.isEmpty()) {
			return;
		}
		
		for (OrderInvoiceDetails orderInvoiceDetails : orderInvoiceDetailsList) {
			InvoiceVO anInvoiceVO = new InvoiceVO();
			map(orderInvoiceDetails, anInvoiceVO, invoiceVOPaymentList);
			invoiceVOList.add(anInvoiceVO);
		}
	}
	
	private void map(OrderInvoiceHeader orderInvoiceHeader, Map<String, Object> params) {
		params.put("invoiceNo", orderInvoiceHeader.getId().toString());
		params.put("invoiceDate", orderInvoiceHeader.getInvoiceDate());
		params.put("customer", orderInvoiceHeader.getCompanyName());
		params.put("billingAddress", orderInvoiceHeader.getBillingAddress("\n"));
		params.put("contact", orderInvoiceHeader.getContactDetails());
		
		String orderDateRange = FormatUtil.formatDateRange(orderInvoiceHeader.getOrderDateFrom(), orderInvoiceHeader.getOrderDateTo());
		params.put("orderDateRange", orderDateRange);
	}
	
	private void map(Order anOrder, OrderInvoiceHeader orderInvoiceHeader) {
		Customer customer = anOrder.getCustomer();
		orderInvoiceHeader.setCustomerId(customer.getId());
		orderInvoiceHeader.setCompanyName(customer.getCompanyName());
		orderInvoiceHeader.setBillingAddressLine1(customer.getBillingAddressLine1());
		orderInvoiceHeader.setBillingAddressLine2(customer.getBillingAddressLine2());
		orderInvoiceHeader.setCity(customer.getCity());
		orderInvoiceHeader.setState(customer.getState().getName());
		orderInvoiceHeader.setZip(customer.getZipcode());
		orderInvoiceHeader.setContactName(customer.getContactName());
		orderInvoiceHeader.setPhone1(customer.getPhone());
		orderInvoiceHeader.setPhone2(customer.getAltPhone1());
		orderInvoiceHeader.setFax(customer.getFax());
		orderInvoiceHeader.setEmail(customer.getEmail());
	}
	
	private void map(Order anOrder, OrderInvoiceDetails orderInvoiceDetails) {
		orderInvoiceDetails.setOrderId(anOrder.getId());
		orderInvoiceDetails.setOrderDate(anOrder.getCreatedAt());
		orderInvoiceDetails.setOrderStatus(anOrder.getOrderStatus().getStatus());
		orderInvoiceDetails.setPickupOrderId(anOrder.getPickupOrderId());
		
		orderInvoiceDetails.setDeliveryContactName(anOrder.getDeliveryContactName());
		orderInvoiceDetails.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		orderInvoiceDetails.setDeliveryContactPhone2(anOrder.getDeliveryContactPhone2());
		
		DeliveryAddress aDeliveryAddress = anOrder.getDeliveryAddress();
		orderInvoiceDetails.setDeliveryAddressId(aDeliveryAddress.getId());
		orderInvoiceDetails.setDeliveryAddressLine1(aDeliveryAddress.getLine1());
		orderInvoiceDetails.setDeliveryAddressLine2(aDeliveryAddress.getLine2());
		orderInvoiceDetails.setDeliveryAddressCity(aDeliveryAddress.getCity());
		orderInvoiceDetails.setDeliveryAddressState(aDeliveryAddress.getState().getCode());
		orderInvoiceDetails.setDeliveryAddressZip(aDeliveryAddress.getZipcode());
		
		orderInvoiceDetails.setDumpsterSize(anOrder.getDumpsterSize().getSize());
		if (anOrder.getDumpsterLocation() != null) {
			orderInvoiceDetails.setLocationType(anOrder.getDumpsterLocation().getLocationType());
		}
		
		orderInvoiceDetails.setMaterialCategory(anOrder.getMaterialType().getMaterialCategory().getCategory());
		orderInvoiceDetails.setMaterialType(anOrder.getMaterialType().getMaterialName());
		
		orderInvoiceDetails.setGrossWeight(anOrder.getGrossWeight());
		orderInvoiceDetails.setNetWeightLb(anOrder.getNetWeightLb());
		orderInvoiceDetails.setNetWeightTonnage(anOrder.getNetWeightTonnage());
		orderInvoiceDetails.setTare(anOrder.getTare());
		orderInvoiceDetails.setScaleTicketNumber(anOrder.getScaleTicketNumber());
		
		if (anOrder.getDropOffDriver() != null) {
			orderInvoiceDetails.setDropOffDriver(anOrder.getDropOffDriver().getEmployee().getFullName());
		}
		if (anOrder.getPickupDriver() != null) {
			orderInvoiceDetails.setPickupDriver(anOrder.getPickupDriver().getEmployee().getFullName());
		}
		
		orderInvoiceDetails.setGrossWeight(anOrder.getGrossWeight());
		orderInvoiceDetails.setNetWeightLb(anOrder.getNetWeightLb());
		orderInvoiceDetails.setNetWeightTonnage(anOrder.getNetWeightTonnage());
		orderInvoiceDetails.setTare(anOrder.getTare());
		orderInvoiceDetails.setScaleTicketNumber(anOrder.getScaleTicketNumber());
		
		orderInvoiceDetails.setDeliveryHourFrom(anOrder.getDeliveryHourFrom());
		orderInvoiceDetails.setDeliveryHourTo(anOrder.getDeliveryHourTo());
		orderInvoiceDetails.setDeliveryMinutesFrom(anOrder.getDeliveryMinutesFrom());
		orderInvoiceDetails.setDeliveryMinutesTo(anOrder.getDeliveryMinutesTo());
		
		orderInvoiceDetails.setDeliveryDate(anOrder.getDeliveryDate());
		orderInvoiceDetails.setPickupDate(anOrder.getPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		orderInvoiceDetails.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		
		orderInvoiceDetails.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal(0.00))
				: anOrderFees.getCityFee());
		orderInvoiceDetails.setCityFeeSuburbName(anOrderFees.getCityFeeType() == null ? 
					StringUtils.EMPTY : anOrderFees.getCityFeeType().getSuburbName());
		
		List<Permit> permitList = anOrder.getPermits();
		if (permitList != null && !permitList.isEmpty()) {
			Permit aPermit = permitList.get(0);
			orderInvoiceDetails.setPermitNum1(aPermit.getNumber());
			orderInvoiceDetails.setPermitFee1(aPermit.getFee());
			orderInvoiceDetails.setPermitType1(aPermit.getPermitType().getPermitType());
			orderInvoiceDetails.setPermitClass1(aPermit.getPermitClass().getPermitClass());
		
			if (permitList.size() > 1) {
				aPermit = permitList.get(1);
				orderInvoiceDetails.setPermitNum2(aPermit.getNumber());
				orderInvoiceDetails.setPermitFee2(aPermit.getFee());
				orderInvoiceDetails.setPermitType2(aPermit.getPermitType().getPermitType());
				orderInvoiceDetails.setPermitClass2(aPermit.getPermitClass().getPermitClass());
			}
			
			if (permitList.size() > 2) {
				aPermit = permitList.get(2);
				orderInvoiceDetails.setPermitNum3(aPermit.getNumber());
				orderInvoiceDetails.setPermitFee3(aPermit.getFee());
				orderInvoiceDetails.setPermitType3(aPermit.getPermitType().getPermitType());
				orderInvoiceDetails.setPermitClass3(aPermit.getPermitClass().getPermitClass());
			}
		}
		orderInvoiceDetails.setTotalPermitFees(anOrderFees.getTotalPermitFees());
		
		orderInvoiceDetails.setAdditionalFee1(anOrderFees.getAdditionalFee1());
		if (anOrderFees.getAdditionalFee1Type() != null) {
			orderInvoiceDetails.setAdditionalFee1Desc(anOrderFees.getAdditionalFee1Type().getDescription());
		}
		
		orderInvoiceDetails.setAdditionalFee2(anOrderFees.getAdditionalFee2());
		if (anOrderFees.getAdditionalFee2Type() != null) {
			orderInvoiceDetails.setAdditionalFee2Desc(anOrderFees.getAdditionalFee2Type().getDescription());
		}
		
		orderInvoiceDetails.setAdditionalFee3(anOrderFees.getAdditionalFee3());
		if (anOrderFees.getAdditionalFee3Type() != null) {
			orderInvoiceDetails.setAdditionalFee3Desc(anOrderFees.getAdditionalFee3Type().getDescription());
		}
		
		orderInvoiceDetails.setTotalAdditionalFees(anOrderFees.getTotalAdditionalFees());
		
		orderInvoiceDetails.setOverweightFee(anOrderFees.getOverweightFee());
		orderInvoiceDetails.setTonnageFees(anOrderFees.getTonnageFee());
		
		orderInvoiceDetails.setTotalFees(anOrderFees.getTotalFees());
		orderInvoiceDetails.setDiscountAmount(anOrderFees.getDiscountAmount());
		orderInvoiceDetails.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		orderInvoiceDetails.setBalanceAmountDue(anOrder.getBalanceAmountDue());
		
		List<OrderPayment> orderPaymentList = anOrder.getOrderPayment();
	   if (orderPaymentList != null && !orderPaymentList.isEmpty()) {
	   	OrderPayment orderPayment = orderPaymentList.get(0);
	   	orderInvoiceDetails.setPaymentAmount1(orderPayment.getAmountPaid());
   		if (orderPayment.getPaymentMethod() != null) {
   			orderInvoiceDetails.setPaymentMethod1(orderPayment.getPaymentMethod().getMethod());
   		}
   		orderInvoiceDetails.setPaymentCCRefNum1(orderPayment.getCcReferenceNum());
   		orderInvoiceDetails.setPaymentCheckNum1(orderPayment.getCheckNum());
   		orderInvoiceDetails.setPaymentDate1(orderPayment.getPaymentDate());
	  
	   	if (orderPaymentList.size() > 1) {
	   		orderPayment = orderPaymentList.get(1);
	   		orderInvoiceDetails.setPaymentAmount2(orderPayment.getAmountPaid());
	   		if (orderPayment.getPaymentMethod() != null) {
	   			orderInvoiceDetails.setPaymentMethod2(orderPayment.getPaymentMethod().getMethod());
	   		}
	   		orderInvoiceDetails.setPaymentCCRefNum2(orderPayment.getCcReferenceNum());
	   		orderInvoiceDetails.setPaymentCheckNum2(orderPayment.getCheckNum());
	   		orderInvoiceDetails.setPaymentDate2(orderPayment.getPaymentDate());
	   	}
	   	
	   	if (orderPaymentList.size() > 2) {
	   		orderPayment = orderPaymentList.get(2);
	   		orderInvoiceDetails.setPaymentAmount3(orderPayment.getAmountPaid());
	   		if (orderPayment.getPaymentMethod() != null) {
	   			orderInvoiceDetails.setPaymentMethod3(orderPayment.getPaymentMethod().getMethod());
	   		}
	   		orderInvoiceDetails.setPaymentCCRefNum3(orderPayment.getCcReferenceNum());
	   		orderInvoiceDetails.setPaymentCheckNum3(orderPayment.getCheckNum());
	   		orderInvoiceDetails.setPaymentDate3(orderPayment.getPaymentDate());
	   	}
	   }
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@RequestMapping(method = { RequestMethod.GET }, value = "/deleteInvoice.do")
	public String deleteInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") Long invoiceId) {
		User createdByUser = getUser(request);
		Long createdBy = createdByUser.getId();
		
		//String query = "Select obj.id, obj.orderId from OrderInvoiceDetails obj where obj.invoiceHeader=" + invoiceId;
		String query = "Select obj.orderId from OrderInvoiceDetails obj where obj.invoiceHeader=" + invoiceId;
		List<OrderInvoiceDetails> orderInvoiceDetailsList = genericDAO.executeSimpleQuery(query);
		
		//Long[] invoiceDetailsIdArr = new Long[orderInvoiceDetailsList.size()];
		String[] orderIdsArr = new String[orderInvoiceDetailsList.size()];
		int i = 0;
		for (Object obj : orderInvoiceDetailsList) {
			//Object[] resultObjArr = (Object[]) obj;
			//invoiceDetailsIdArr[i] = (Long) resultObjArr[0];
			//orderIdsArr[i] = resultObjArr[1].toString();
			orderIdsArr[i] = obj.toString();
			i++;
		}
		
		//String orderIds = CoreUtil.toString(orderIdsArr);
		//List<Order> orderList = genericDAO.findByCommaSeparatedIds(Order.class, orderIds);
		
		String deleteQuery = "delete OrderInvoiceDetails oid where oid.invoiceHeader = " + invoiceId;
		int noRowsDel = genericDAO.executeUpdate(deleteQuery);
		/*for (Long invoiceDetailsId : invoiceDetailsIdArr) {
			genericDAO.deleteById(OrderInvoiceDetails.class, invoiceDetailsId);
		}*/
		
		genericDAO.deleteById(OrderInvoiceHeader.class, invoiceId);
		
		updateOrder(orderIdsArr, null, null, "N", createdBy);
		  
		String successMsg = InvoiceVO.INV_DEL_SUCCESS_MSG + "  Invoice #: " + invoiceId;
		ModelUtil.createAuditOrderNotes(genericDAO, orderIdsArr, successMsg, createdByUser);
		
		setSuccessMsg(request, successMsg);
		return "redirect:/" + getUrlContext() + "/manageInvoiceSearch.do";
		//return getUrlContext() + "/manageInvoiceList";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deliveryAddressSearch.do")
	public @ResponseBody String deliveryAddressSearch(ModelMap model, HttpServletRequest request,
					@RequestParam("id") Long customerId) {
		String query = "select obj from DeliveryAddress obj where obj.deleteFlag='1' and obj.customer.id="
							+ customerId + " order by obj.line1 asc";
		List<DeliveryAddress> addressList  = genericDAO.executeSimpleQuery(query);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(addressList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoicableOrderIdsSearch.do")
	public @ResponseBody String invoicableOrderIdsSearch(ModelMap model, HttpServletRequest request,
					@RequestParam("id") Long customerId) {
		List<Long> orderIdList = retrieveOrderIds("N", String.valueOf(customerId));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(orderIdList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/payableInvoiceNosSearch.do")
	public @ResponseBody String payableInvoiceNosSearch(ModelMap model, HttpServletRequest request,
					@RequestParam("id") Long customerId) {
		List<Long> invoiceNoList = retrievePayableInvoiceNos(String.valueOf(customerId));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(invoiceNoList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@RequestMapping(method = { RequestMethod.POST }, value = "/saveInvoicePayment.do")
	public String saveInvoicePayment(HttpServletRequest request,
			@ModelAttribute("invoicePaymentModelObject") OrderInvoicePayment invoicePayment,
			BindingResult bindingResult, ModelMap model) {
		OrderInvoiceHeader invoiceHeader = invoicePayment.getInvoice();
		
		List<Order> orderList = retrieveOrders(invoiceHeader.getId());
		
		List<OrderPayment> newOrderPaymentList = new ArrayList<OrderPayment>();
		BigDecimal amountAvailable = updateOrderPayment(orderList, invoicePayment, newOrderPaymentList);
		invoicePayment.setAmountAvailable(amountAvailable);
		
		setModifier(request, invoicePayment);
		genericDAO.save(invoicePayment);
		
		saveInvoiceHeader(invoiceHeader, invoicePayment);
		
		saveOrderPayment(newOrderPaymentList, invoicePayment);
		
		setSuccessMsg(request, "Invoice payment saved successfully");
		return "redirect:/" + getUrlContext() + "/invoicePaymentSearch.do";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private String saveInvoiceHeader(OrderInvoiceHeader invoiceHeader, OrderInvoicePayment invoicePayment) {
		invoiceHeader.setTotalInvoicePaymentDone(invoiceHeader.getTotalInvoicePaymentDone()
				.add(invoicePayment.getAmountPaid()));
		invoiceHeader.setTotalInvoiceBalanceDue(invoiceHeader.getTotalInvoiceBalanceDue()
				.subtract(invoicePayment.getAmountPaid()));
		invoiceHeader.setTotalInvoiceBalanceAvailable(invoiceHeader.getTotalInvoiceBalanceAvailable()
				.add(invoicePayment.getAmountAvailable()));
		
		invoiceHeader.setModifiedBy(invoicePayment.getCreatedBy());
		invoiceHeader.setModifiedAt(invoicePayment.getCreatedAt());
		genericDAO.saveOrUpdate(invoiceHeader);
		
		return "Successfully saved invoice header";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private String saveOrderPayment(List<OrderPayment> orderPaymentList, OrderInvoicePayment invoicePayment) {
		User createdByUser = genericDAO.getById(User.class, invoicePayment.getCreatedBy());
		for (OrderPayment anOrderPayment : orderPaymentList) {
			anOrderPayment.setInvoicePaymentId(invoicePayment.getId());
			anOrderPayment.setCreatedAt(invoicePayment.getCreatedAt());
			anOrderPayment.setCreatedBy(invoicePayment.getCreatedBy());
			
			Order order = anOrderPayment.getOrder();
			order.setModifiedAt(invoicePayment.getCreatedAt());
			order.setModifiedBy(invoicePayment.getCreatedBy());
			genericDAO.saveOrUpdate(order);
			
			String auditMsg = String.format("Order payment updated.  Invoice#: {0}.  Invoice payment#: {1}",
					invoicePayment.getInvoice().getId(), invoicePayment.getId());
			ModelUtil.createAuditOrderNotes(genericDAO, order, auditMsg, createdByUser);
		}
		return "Successfully saved order payment and order";
	}
	
	private BigDecimal updateOrderPayment(List<Order> orderList, OrderInvoicePayment invoicePayment,
			List<OrderPayment> newOrderPaymentList) {
		BigDecimal amountAvailable = invoicePayment.getAmountPaid();
		for (Order anOrder : orderList) {
			BigDecimal balanceAmountDue = anOrder.getBalanceAmountDue();
			if (balanceAmountDue.doubleValue() == 0.0) {
				continue;
			}
			
			OrderPayment orderPayment = new OrderPayment();
			orderPayment.setOrder(anOrder);
			map(invoicePayment, orderPayment);
			
			BigDecimal payableAmount = balanceAmountDue;
			if (balanceAmountDue.doubleValue() >= amountAvailable.doubleValue()) {
				payableAmount = amountAvailable;
				amountAvailable = new BigDecimal(0.00);
			} else {
				amountAvailable = amountAvailable.subtract(balanceAmountDue);
			}
			orderPayment.setAmountPaid(payableAmount);
			
			List<OrderPayment> orderPaymentList = anOrder.getOrderPayment();
			orderPaymentList.add(orderPayment);
			newOrderPaymentList.add(orderPayment);
			
			anOrder.setTotalAmountPaid(anOrder.getTotalAmountPaid().add(orderPayment.getAmountPaid()));
			anOrder.setBalanceAmountDue(anOrder.getBalanceAmountDue().subtract(orderPayment.getAmountPaid()));
			
			if (amountAvailable.doubleValue() == 0.0) {
				break;
			}
		}
		return amountAvailable;
	}
	
	private void map(OrderInvoicePayment invoicePayment, OrderPayment orderPayment) {
		orderPayment.setPaymentMethod(invoicePayment.getPaymentMethod());
		orderPayment.setPaymentDate(invoicePayment.getPaymentDate());
		orderPayment.setCheckNum(invoicePayment.getCheckNum());
		orderPayment.setCcReferenceNum(invoicePayment.getCcReferenceNum());
		orderPayment.setCcName(invoicePayment.getCcName());
		orderPayment.setCcNumber(invoicePayment.getCcNumber());
		orderPayment.setCcExpDate(invoicePayment.getCcExpDate());
		orderPayment.setInvoiceId(invoicePayment.getInvoice().getId());
	}
	
	private List<Order> retrieveOrders(Long invoiceId) {
		List<Long> orderIdList = retrieveInvoiceOrderIds(invoiceId);
		List<Order> orderList = retrieveOrders(orderIdList);
		return orderList;
	}
	
	private List<Order> retrieveOrders(List<Long> orderIdList) {
		String orderIdsStr = CoreUtil.toStringFromLong(orderIdList);
		List<Order> orderList = genericDAO.findByCommaSeparatedIds(Order.class, orderIdsStr);
		ModelUtil.sortOrder(orderList, "deliveryDate");
		return orderList;
	}
	
	private List<Long> retrieveInvoiceOrderIds(Long invoiceId) {
		String query = "select obj.orderId from OrderInvoiceDetails obj where obj.deleteFlag=1"
					+ " and obj.invoiceHeader=" + invoiceId;
		List<OrderInvoiceDetails> invoiceDetailsList = genericDAO.executeSimpleQuery(query);
		List<Long> orderIdList = ModelUtil.extractObjIds(invoiceDetailsList);
		return orderIdList;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/saveInvoice.do")
	public String saveInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		
		String[] orderIdsArr = input.getIds();
		if (orderIdsArr == null || orderIdsArr.length <= 0) {
			//setupPreviewInvoice(request, model);
			//input.setHistoryCount(-2);
			
			setErrorMsg(request, response, "Please select at least one order");
			return "redirect:/" + getUrlContext() + "/createInvoiceMain.do";
			//return getUrlContext() + "/previewInvoice";
		}
		
		User createdByUser = getUser(request);
		Long createdBy = createdByUser.getId();
		
		List<Order> orderList = retrieveOrder(orderIdsArr);
		
		OrderInvoiceHeader orderInvoiceHeader = new OrderInvoiceHeader();
		orderInvoiceHeader.setCreatedAt(Calendar.getInstance().getTime());
		orderInvoiceHeader.setCreatedBy(createdBy);
		
		orderInvoiceHeader.setOrderCount(orderList.size());
		
		map(input, orderInvoiceHeader);
		map(orderList.get(0), orderInvoiceHeader);
		
		List<OrderInvoiceDetails> orderInvoiceDetailsList = new ArrayList<OrderInvoiceDetails>();
		map(orderList, orderInvoiceHeader, orderInvoiceDetailsList);
		
		genericDAO.save(orderInvoiceHeader);
		
		for (OrderInvoiceDetails anOrderInvoiceDetails : orderInvoiceDetailsList) {
			anOrderInvoiceDetails.setCreatedAt(Calendar.getInstance().getTime());
			anOrderInvoiceDetails.setCreatedBy(createdBy);
			genericDAO.save(anOrderInvoiceDetails);
		}
		
		updateOrder(orderIdsArr, orderInvoiceHeader.getId(), orderInvoiceHeader.getInvoiceDate(), "Y", createdBy);
		
		String successMsg = InvoiceVO.INV_SAVE_SUCCESS_MSG + "  Invoice #: " + orderInvoiceHeader.getId();
		ModelUtil.createAuditOrderNotes(genericDAO, orderIdsArr, successMsg, createdByUser);
		
		setSuccessMsg(request, successMsg);
		return "redirect:/" + getUrlContext() + "/createInvoiceSearch.do";
		//return processPreviewInvoiceCommon(request, response, input);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private int updateOrder(String[] orderIdsArr, Long invoiceId, Date invoiceDate, String invoiced, Long createdBy) {
		Date currentTime = Calendar.getInstance().getTime();
		String orderIds = CoreUtil.toString(orderIdsArr);
		
		String invoiceIdStr = (invoiceId == null) ? "null" : "'"+invoiceId.toString()+"'";
		String invoiceDateStr = (invoiceDate == null) ? "null" : 
			"'" + FormatUtil.dbDateTimeFormat.format(invoiceDate) + "'";
		
		String orderUpdateQuery = "update Order o set o.invoiced = '" + invoiced + "',"
			+ " o.invoiceIds = " + invoiceIdStr + ","
			+ " o.invoiceDates = " + invoiceDateStr + ","
			+ " o.modifiedBy = " + createdBy + ","
			+ " o.modifiedAt = '" + FormatUtil.dbDateTimeFormat.format(currentTime) + "'"
			+ " where o.id in (" + orderIds + ")";
		return genericDAO.executeUpdate(orderUpdateQuery);
	}
	
	@RequestMapping(method = {RequestMethod.POST}, value = "/previewInvoice.do")
	public String previewInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
					@RequestParam("id") String[] ids,
					@RequestParam("invoiceDate") Date invoiceDate) {
		setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		input.setIds(ids);
		input.setInvoiceDate(invoiceDate);
		//input.setHistoryCount(-1);
		
		return processPreviewInvoiceCommon(request, response, input);
	}
	
	private String processPreviewInvoiceCommon(HttpServletRequest request, HttpServletResponse response,
			InvoiceVO input) {
		Map<String, Object> datas = generateInvoiceData(request, input);
		List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		addWaterMarkRendererReportParam(params, true, "Preview");
		
		String type = "html";
		//setReportRequestHeaders(response, type, ORDER_INVOICE_MASTER);
		try {
			/*JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
						invoiceVOList, params, type, request);*/
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(ORDER_INVOICE_MASTER, 
					ORDER_INVOICE_SUB, invoiceVOList, params, type, request);
			if (jasperPrint == null) {
				setErrorMsg(request, response, "Error occured while processing invoice preview");
			} else {
				//request.setAttribute("japserPrint", jasperPrint);
				addJasperPrint(request, jasperPrint);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			setErrorMsg(request, response, t.getMessage());
		}
		
		return getUrlContext() + "/previewInvoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/previewInvoiceExport.do")
	public String previewInvoiceExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_MASTER);
		
		ByteArrayOutputStream out = null;
		try {
			InvoiceVO input = (InvoiceVO)request.getSession().getAttribute("input");
			Map<String, Object> datas = generateInvoiceData(request, input);
			
			List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get("data");
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			addWaterMarkRendererReportParam(params, true, "Preview");
			
			/*out = dynamicReportService.generateStaticReport(reportName,
						invoiceVOList, params, type, request);*/
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER, ORDER_INVOICE_SUB,
					invoiceVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate static report: " + t);
			
			setErrorMsg(request, response, "Exception occured while exporting invoice preview: " + t);
			return "redirect:/" + getUrlContext() + "/previewInvoice.do";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/createInvoiceExport.do")
	public String createInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		String reportName = "ordersBalanceDueReport";
		type = setReportRequestHeaders(response, type, reportName);
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		
		//Map<String, Object> params = new HashMap<String, Object>();
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			List<Order> orderList = performCreateInvoiceSearch(searchCriteria, input);
			
			out = dynamicReportService.exportReport(reportName, type, Order.class, orderList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					orderList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating create invoice report: " + t);
			return "redirect:/" + getUrlContext() + "/createInvoiceMain.do";
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/manageInvoiceExport.do")
	public String manageInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		String reportName = "invoiceReport";
		type = setReportRequestHeaders(response, type, reportName);
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			List<OrderInvoiceHeader> orderInvoiceHeaderList = performManageInvoiceSearch(searchCriteria);
			
			out = dynamicReportService.exportReport(reportName, type, OrderInvoiceHeader.class, orderInvoiceHeaderList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					invoiceList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating manage invoice report: " + t);
			return "redirect:/" + getUrlContext() + "/manageInvoiceMain.do";
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/downloadInvoice.do")
	public String downloadInvoice(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "id") Long invoiceId,
			@RequestParam(required = true, value = "type") String type) {
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_MASTER);
		
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateInvoiceData(request, invoiceId);
			List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get("data");
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			
			/*out = dynamicReportService.generateStaticReport(reportName,
						invoiceVOList, params, type, request);*/
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER, ORDER_INVOICE_SUB,
					invoiceVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate static report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating invoice: " + t);
			return "redirect:/" + getUrlContext() + "/manageInvoiceMain.do";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private OrderInvoiceHeader retrieveOrderInvoiceHeader(Long invoiceId) {
		OrderInvoiceHeader orderInvoiceHeader = genericDAO.getById(OrderInvoiceHeader.class, invoiceId);
		return orderInvoiceHeader;
	}
	
	private List<OrderInvoiceDetails> retrieveOrderInvoiceDetails(Long invoiceId) {
		String query = "select obj from OrderInvoiceDetails obj where obj.invoiceHeader.id=" + invoiceId
					+ " order by obj.orderId asc";
		List<OrderInvoiceDetails> orderInvoiceDetailsList = genericDAO.executeSimpleQuery(query);
		return orderInvoiceDetailsList;
	}
	
	private void setupPreviewInvoice(HttpServletRequest request, ModelMap model) {
		/*Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);*/
		
		model.addAttribute("msgCtx", "invoicePreview");
		model.addAttribute("errorCtx", "invoicePreview");
		model.addAttribute("activeTab", "createInvoice");
	}
	
	private Map<String, Object> generateInvoiceData(HttpServletRequest request, InvoiceVO input) {
		List<Order> orderList = performPreviewInvoiceSearch(input);
		
		List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
		List<InvoiceVO> invoicePaymentVOList = new ArrayList<InvoiceVO>();
		map(orderList, invoiceVOList, invoicePaymentVOList);
		
		Map<String, Object> params = new HashMap<String, Object>();
		map(input, invoiceVOList, invoicePaymentVOList, params);
		
		addLogoFilePath(request, params);
		
		Map<String,Object> datas = new HashMap<String,Object>();
		addData(invoiceVOList, params, datas);
		     
		return datas;
	}
	
	private void addData(List<InvoiceVO> invoiceVOList, Map<String, Object> params, Map<String,Object> datas) {
		datas.put("data", invoiceVOList);
		datas.put("params", params);
	}
	
	private void addLogoFilePath(HttpServletRequest request, Map<String, Object> params) {
		String logoFilePath = ServletUtil.getLogoFilePath(request);
		params.put("LOGO_FILE_PATH", logoFilePath);
	}
	
	private Map<String, Object> generateInvoiceData(HttpServletRequest request, Long invoiceId) {
		OrderInvoiceHeader orderInvoiceHeader = retrieveOrderInvoiceHeader(invoiceId);
		Map<String, Object> params = new HashMap<String, Object>();
		map(orderInvoiceHeader, params);
		
		List<OrderInvoiceDetails> orderInvoiceDetailsList = retrieveOrderInvoiceDetails(invoiceId);
		List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
		List<InvoiceVO> invoiceVOPaymentList = new ArrayList<InvoiceVO>();
		mapToInvoiceVOList(orderInvoiceDetailsList, invoiceVOList, invoiceVOPaymentList);
		params.put("orderPaymentList", invoiceVOPaymentList);
		
		addLogoFilePath(request, params);
		
		Map<String,Object> datas = new HashMap<String,Object>();
		addData(invoiceVOList, params, datas);
		     
		return datas;
	}
	
	private void map(InvoiceVO invoiceVO, List<InvoiceVO> invoiceVOList, List<InvoiceVO> invoicePaymentVOList, Map<String, Object> params) {
		params.put("invoiceNo", "TBD - Preview");
		params.put("invoiceDate", invoiceVO.getInvoiceDate());
		
		Customer customer = retrieveCustomer(invoiceVO.getCustomerId(), invoiceVO.getOrderId());
		params.put("customer", customer.getCompanyName());
		params.put("billingAddress", customer.getBillingAddress("\n"));
		params.put("contact", customer.getContactDetails());
		
		List<Date> orderDateList = new ArrayList<Date>();
		for (InvoiceVO anInvoiceVO : invoiceVOList) {
			orderDateList.add(anInvoiceVO.getOrderDate());
		}
		Collections.sort(orderDateList);
		
		String orderDateRange = FormatUtil.formatDateRange(orderDateList.get(0), orderDateList.get(orderDateList.size()-1));
		params.put("orderDateRange", orderDateRange);
		
		params.put("orderPaymentList", invoicePaymentVOList);
	}
	
	private Customer retrieveCustomer(String customerId, String orderId) {
		Customer customer = null;
		if (StringUtils.isNotEmpty(customerId)) {
			customer = genericDAO.getById(Customer.class, Long.valueOf(customerId));
		} else if (StringUtils.isNotEmpty(orderId)) {
			String query = "Select obj.customer from Order obj where obj.id=" + orderId;
			List<Customer> customerList = genericDAO.executeSimpleQuery(query);
			customer = customerList.get(0);
		}
		
		return customer;
	}
	
	protected void addTabAttributes(ModelMap model, String mode, String activeSubTab) {
		addTabAttributes(model, INVOICE_TAB, mode, activeSubTab);
	}
	
	@ModelAttribute("modelObject")
	public InvoiceVO setupModel(HttpServletRequest request) {
		return new InvoiceVO();
	}
}
