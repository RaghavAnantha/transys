package com.transys.controller.invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;

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

import org.springframework.beans.propertyeditors.CustomDateEditor;

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

import com.transys.core.util.CoreUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;
import com.transys.core.util.ReportUtil;

import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderFees;
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
import com.transys.model.vo.invoice.InvoiceReportVO;
import com.transys.model.vo.invoice.InvoiceVO;

@Controller
@RequestMapping("/invoice")
@Transactional(readOnly = false)
public class InvoiceController extends BaseController {
	private static final String INVOICE_TAB = "manageInvoice";
	private static final String INVOICE_PAYMENT_TAB = "invoicePayment";
	private static final String INVOICE_REPORTS_TAB = "invoiceReports";
	
	private static final String INVOICE_PAYMENT_MODEL_OBJECT_KEY = "invoicePaymentModelObject";
	
	private static final String createInvoiceMsgCtx = "createInvoice";
	private static final String manageInvoiceMsgCtx = "manageInvoice";
	private static final String previewInvoiceMsgCtx = "invoicePreview";
	private static final String createInvoicePaymentMsgCtx = "createInvoicePayment";
	private static final String manageInvoicePaymentMsgCtx = "manageInvoicePayment";
	private static final String invoiceListReportMsgCtx = "invoiceListReport";
	
	private static String ORDER_INVOICE_REPORT = "orderInvoice";
	private static String ORDER_INVOICE_PAYMENT_MASTER_REPORT = "orderInvoicePaymentMaster";
	private static String ORDER_INVOICE_PAYMENT_SUB_REPORT = "orderInvoicePaymentSub";
	
	public InvoiceController() {
		setUrlContext("invoice");
	}
	
	@Override
	public String getReportsUrlContext() {
		return super.getReportsUrlContext() + "/invoice";
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		
		binder.registerCustomEditor(PaymentMethodType.class, new AbstractModelEditor(PaymentMethodType.class));
		binder.registerCustomEditor(OrderInvoiceHeader.class, new AbstractModelEditor(OrderInvoiceHeader.class));
		
		SimpleDateFormat ccExpDateFormat = new SimpleDateFormat("MM/yyyy");
		ccExpDateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "ccExpDate", new CustomDateEditor(ccExpDateFormat, true));
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
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/reports/invoiceReportsMain.do")
	public String invoiceReportsMain(ModelMap model, HttpServletRequest request) {
		//clearSearchCriteria(request);
		setupInvoiceListReport(model, request);
		return getReportsUrlContext() + "/invoiceListReport/list";
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/invoicePaymentMain.do")
	public String invoicePaymentMain(ModelMap model, HttpServletRequest request) {
		//clearSearchCriteria(request);
		return processInvoicePaymentSearch(model, request);
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/createInvoicePayment.do")
	public String createInvoicePayment(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "invoiceId", required = false) Long invoiceId) {
		OrderInvoicePayment invoicePayment = new OrderInvoicePayment();
		OrderInvoiceHeader invoiceHeader = null;
		if (invoiceId != null) {
			invoiceHeader = genericDAO.getById(OrderInvoiceHeader.class, invoiceId);
			if (invoiceHeader.getTotalInvoiceBalanceDue().doubleValue() == 0.0) {
				setErrorMsg(request, "Invoice is already paid in full");
				return "redirect:/" + getUrlContext() + "/invoicePaymentSearch.do";
			}
			
			invoicePayment.setInvoice(invoiceHeader);
		}
		
		setupCreateInvoicePayment(model, request, invoiceHeader, invoicePayment);
		
		return getUrlContext() + "/invoicePaymentForm";
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/editInvoicePayment.do")
	public String editInvoicePayment(ModelMap model, HttpServletRequest request,
			@RequestParam("id") Long invoicePaymentId) {
		OrderInvoicePayment invoicePayment = genericDAO.getById(OrderInvoicePayment.class, invoicePaymentId);
		OrderInvoiceHeader invoiceHeader = invoicePayment.getInvoice();
		setupCreateInvoicePayment(model, request, invoiceHeader, invoicePayment);
		
		return getUrlContext() + "/invoicePaymentForm";
	}
	
	private void setupCreateInvoicePayment(ModelMap model, HttpServletRequest request, 
			OrderInvoiceHeader invoiceHeader, OrderInvoicePayment invoicePayment) {
		model.addAttribute(INVOICE_PAYMENT_MODEL_OBJECT_KEY, invoicePayment);
		
		List<CustomerVO> customerVOList = null;
		if (invoiceHeader != null) {
			customerVOList = new ArrayList<CustomerVO>();
			CustomerVO customerVO = new CustomerVO();
			customerVO.setId(invoiceHeader.getCustomerId());
			customerVO.setCompanyName(invoiceHeader.getCompanyName());
			customerVOList.add(customerVO);
			
			List<String> invoiceNos = new ArrayList<String>();
			invoiceNos.add(String.valueOf(invoiceHeader.getId()));
			model.addAttribute("invoiceNos", invoiceNos);
		} else {
			customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		}
		model.addAttribute("customers", customerVOList);
		
		model.addAttribute("paymentMethods", genericDAO.executeSimpleQuery("select obj from PaymentMethodType obj where obj.deleteFlag='1' and obj.id!=0 order by obj.method asc"));
	   
		addTabAttributes(model, INVOICE_PAYMENT_TAB, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, createInvoicePaymentMsgCtx);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceSearch.do")
	public String createInvoiceSearch(ModelMap model, HttpServletRequest request,
			@ModelAttribute("modelObject") InvoiceVO input,
			BindingResult bindingResult) {
		setupCreateInvoiceList(model, request);
		
		String returnUrl = getUrlContext() + "/invoice";
		
		SearchCriteria criteria = (SearchCriteria)request.getSession().getAttribute("searchCriteria");
		processSaveInvoiceReferrer(request, model, criteria, input);
		
		validateCreateInvoiceSearch(input, bindingResult);
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
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/reports/invoiceListReportSearch.do")
	public String invoiceListReportSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		populateSearchCriteria(request, request.getParameterMap());
		
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateInvoiceListReportData(model, request);
			
			Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
			List<InvoiceReportVO> data = (List<InvoiceReportVO>) datas.get(ReportUtil.dataKey);
			
			String reportName = "orderInvoiceReportMaster";
			String subReportName = "orderInvoiceOrderSub";
			
			String type = "html";
			setReportRequestHeaders(response, type, reportName);
			
			if (data.isEmpty()) {
				return "report.empty";
			}
			
			out = dynamicReportService.generateStaticMasterSubReport(reportName, subReportName, data, params, 
						type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Exception occured while generating Invoice List report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Invoice List report: " + t);
			return "report.error";
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
	
	private Map<String, Object> generateInvoiceListReportData(ModelMap model, HttpServletRequest request) {
		List<InvoiceReportVO> invoiceReportVOList = new ArrayList<InvoiceReportVO>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> datas = new HashMap<String, Object>();
		addData(datas, invoiceReportVOList, params);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoiceHeader> orderInvoiceHeaderList = performInvoiceListReportSearch(criteria);
		if (orderInvoiceHeaderList == null || orderInvoiceHeaderList.isEmpty()) {
			return datas;
		}
		
		mapForInvoiceReport(orderInvoiceHeaderList.get(0), criteria, params);
		
		for (OrderInvoiceHeader anInvoiceHeader : orderInvoiceHeaderList) {
			InvoiceReportVO anInvoiceReportVO = new InvoiceReportVO();
			map(anInvoiceHeader, anInvoiceReportVO);
			
			List<OrderInvoiceDetails> orderInvoiceDetailsList = anInvoiceHeader.getOrderInvoiceDetails();
			List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
			List<InvoiceVO> invoiceVOPaymentList = new ArrayList<InvoiceVO>();
			mapToInvoiceVOList(orderInvoiceDetailsList, invoiceVOList, invoiceVOPaymentList);
			
			anInvoiceReportVO.setInvoiceVOList(invoiceVOList);
			
			String query = "select obj from OrderInvoicePayment obj where obj.deleteFlag=1";
			query += (" and obj.invoice.id=" + anInvoiceHeader.getId());
			query += " order by id desc";
			List<OrderInvoicePayment> invoicePaymentList = genericDAO.executeSimpleQuery(query);
			for (OrderInvoicePayment orderInvoicePayment : invoicePaymentList) {
				List<OrderPayment> orderPaymentList = retrieveInvoiceOrderPaymentDetails(orderInvoicePayment.getId());
				orderInvoicePayment.setOrderPaymentList(orderPaymentList);
			}
			anInvoiceReportVO.setInvoicePaymentList(invoicePaymentList);
			invoiceReportVOList.add(anInvoiceReportVO);
		}
		
		addRDSBillingInfo(params);
		addLogoFilePath(request, params);
		     
		return datas;
	}
	
	private String processManageInvoiceSearch(ModelMap model, HttpServletRequest request) {
		setupManageInvoiceList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoiceHeader> orderInvoiceHeaderList = performManageInvoiceSearch(criteria);
		model.addAttribute("list", orderInvoiceHeaderList);
		
		return getUrlContext() + "/invoice";
	}
	
	private List<OrderInvoiceHeader> performInvoiceListReportSearch(SearchCriteria criteria) {
		Map<String, Object> criteriaMap = criteria.getSearchMap();
		String invoiceId = (String)criteriaMap.get("invoiceListReportInvoiceNo");
		String customerId = (String)criteriaMap.get("invoiceListReportCustomer");
		String invoiceDateFrom = (String)criteriaMap.get("invoiceListReportInvoiceDateFrom");
		String invoiceDateTo = (String)criteriaMap.get("invoiceListReportInvoiceDateTo");
		
		StringBuffer query = new StringBuffer("select obj from OrderInvoiceHeader obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		if (StringUtils.isNotEmpty(invoiceId)) {
			whereClause.append(" and obj.id=" + invoiceId);
		}
		if (StringUtils.isNotEmpty(customerId)) {
			whereClause.append(" and obj.customerId=" + customerId);
		}
      if (StringUtils.isNotEmpty(invoiceDateFrom)){
        	whereClause.append(" and obj.invoiceDate >='"+FormatUtil.formatInputDateToDbDate(invoiceDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(invoiceDateTo)){
	     	whereClause.append(" and obj.invoiceDate <='"+FormatUtil.formatInputDateToDbDate(invoiceDateTo)+"'");
	   }
      
      query.append(whereClause);
      query.append(" order by obj.id desc");
     
		List<OrderInvoiceHeader> orderInvoiceHeaderList = genericDAO.executeSimpleQuery(query.toString());
		return orderInvoiceHeaderList;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/invoicePaymentSearch.do")
	public String invoicePaymentSearch(ModelMap model, HttpServletRequest request) {
		return processInvoicePaymentSearch(model, request);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/invoiceOrderPaymentDetails.do")
	public String invoiceOrderPaymentDetails(ModelMap model, HttpServletRequest request,
			@RequestParam("invoicePaymentId") Long invoicePaymentId) {
		List<OrderPayment> orderPaymentList = retrieveInvoiceOrderPaymentDetails(invoicePaymentId);
		model.addAttribute("list", orderPaymentList);
		return getUrlContext() + "/invoiceOrderPaymentList";
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/invoiceOrderDetails.do")
	public String invoiceOrderDetails(ModelMap model, HttpServletRequest request,
			@RequestParam("invoiceId") Long invoiceId) {
		List<OrderInvoiceDetails> orderInvoiceDetailsList = retrieveOrderInvoiceDetails(invoiceId);
		model.addAttribute("list", orderInvoiceDetailsList);
		return getUrlContext() + "/invoiceOrderList";
	}
	
	private List<OrderPayment> retrieveInvoiceOrderPaymentDetails(Long invoicePaymentId) {
		String query = "select obj from OrderPayment obj where obj.deleteFlag=1"
				+ " and invoicePaymentId=" + invoicePaymentId
				+ " order by obj.order.id asc, obj.id asc";
		List<OrderPayment> orderPaymentList = genericDAO.executeSimpleQuery(query);
		return orderPaymentList;
	}
	
	private String processInvoicePaymentSearch(ModelMap model, HttpServletRequest request) {
		setupInvoicePaymentList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoicePayment> orderInvoicePaymentList = performInvoicePaymentSearch(criteria);
		model.addAttribute("list", orderInvoicePaymentList);
		
		return getUrlContext() + "/invoicePaymentList";
	}
	
	private void setupManageInvoiceCommon(ModelMap model, HttpServletRequest request,
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
			if (StringUtils.equals("N", orderInvoiced)) {
				model.addAttribute("orderIds", retrieveInvoiceableOrderIds(customerId));
			}
		}
	}
	
	private void setupCreateInvoiceList(ModelMap model, HttpServletRequest request) {
		setupManageInvoiceCommon(model, request, "customerId", "N");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(150);
		
		addTabAttributes(model, MODE_ADD, StringUtils.EMPTY);

		addMsgCtx(model, createInvoiceMsgCtx);
	}
	
	private void setupManageInvoiceList(ModelMap model, HttpServletRequest request) {
		setupManageInvoiceCommon(model, request, "manageInvoiceCustomerId", "Y");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(150);
		
		/*String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1 order by obj.id asc";
		model.addAttribute("invoiceNos", genericDAO.executeSimpleQuery(query));*/
		
		addTabAttributes(model, MODE_MANAGE, StringUtils.EMPTY);
		addMsgCtx(model, manageInvoiceMsgCtx);
	}
	
	private void setupInvoicePaymentList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		/*String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1 order by obj.id asc";
		model.addAttribute("invoiceNos", genericDAO.executeSimpleQuery(query));*/
		
		addTabAttributes(model, INVOICE_PAYMENT_TAB, StringUtils.EMPTY, StringUtils.EMPTY);
		addMsgCtx(model, manageInvoicePaymentMsgCtx);
	}
	
	private void setupInvoiceListReport(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		addTabAttributes(model, INVOICE_REPORTS_TAB, StringUtils.EMPTY, StringUtils.EMPTY);
		addMsgCtx(model, invoiceListReportMsgCtx);
	}
	
	private List<Long> retrieveInvoiceableOrderIds(String customerId) {
		String query = "select obj from Order obj where obj.deleteFlag=1 ";
		query	+= constructInvoicableOrderCriteria();
		
		if (StringUtils.isNotEmpty(customerId)) {
			query	+= " and obj.customer.id = " + customerId;
		}
		query	+= " order by obj.id asc";
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		
		List<Order> invoicableOrderList = filterInvoicableOrders(orderList);
		if (invoicableOrderList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		List<Long> invoicableOrderIdList = ModelUtil.extractIds(invoicableOrderList);
		return invoicableOrderIdList;
	}
	
	private BigDecimal deduceAmountToBeInvoiced(Order anOrder, Map<Long, BigDecimal> orderInvoicedAmountMap,
			Map<Long, BigDecimal> nonInvoicedOrderPaymentAmountMap) {
		BigDecimal amountToBeInvoiced;
		BigDecimal amountAlreadyInvoiced = orderInvoicedAmountMap.get(anOrder.getId());
		BigDecimal nonInvoicedOrderPaymentAmount = nonInvoicedOrderPaymentAmountMap.get(anOrder.getId());
		if (amountAlreadyInvoiced == null || amountAlreadyInvoiced.doubleValue() == 0.0) {
			amountToBeInvoiced = anOrder.getBalanceAmountDue();
		} else {
			amountToBeInvoiced = anOrder.getOrderFees().getTotalFees().subtract(amountAlreadyInvoiced);
			if (nonInvoicedOrderPaymentAmount != null) {
				amountToBeInvoiced = amountToBeInvoiced.subtract(nonInvoicedOrderPaymentAmount);
			}
		}
		
		return amountToBeInvoiced;
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
	
	private String constructInvoicableOrderCriteria() {
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(" and obj.balanceAmountDue > " + 0.0);
		//whereClause.append(" and obj.invoiced='N'");
		
		OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		whereClause.append(" and obj.orderStatus.id !=" + orderStatus.getId().longValue());
		
		return whereClause.toString();
	}
	
	private List<Order> filterInvoicableOrders(List<Order> orderList) {
		if (orderList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		List<Long> orderIdList = ModelUtil.extractIds(orderList);
		Map<Long, BigDecimal> orderInvoicedAmountMap = retrieveOrderInvoicedAmountMap(orderIdList);
		Map<Long, BigDecimal> nonInvoicedOrderPaymentAmountMap = retrieveNonInvoicedOrderPaymentAmountMap(orderIdList);
		OrderStatus cancelOrderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		List<Order> invoicableOrders = new ArrayList<Order>();
		for (Order anOrder : orderList) {
			if (anOrder.getBalanceAmountDue().doubleValue() == 0.0) {
				continue;
			}
			
			if (anOrder.getOrderStatus().getId().longValue() == cancelOrderStatus.getId().longValue()) {
				continue;
			}
			
			BigDecimal amountToBeInvoiced = deduceAmountToBeInvoiced(anOrder, orderInvoicedAmountMap, nonInvoicedOrderPaymentAmountMap);
			if (amountToBeInvoiced.doubleValue() == 0.0) {
				continue;
			}
			
			anOrder.setAmountToBeInvoiced(amountToBeInvoiced);
			invoicableOrders.add(anOrder);
		}
		return invoicableOrders;
	}
	
	private List<Order> performCreateInvoiceSearch(SearchCriteria criteria, InvoiceVO input) {
		String customerId = input.getCustomerId();
		String deliveryAddress = input.getDeliveryAddress();
		String orderId = input.getOrderId();
		String orderDateFrom = input.getOrderDateFrom();
		String orderDateTo = input.getOrderDateTo();
		String deliveryDateFrom = input.getDeliveryDateFrom();
		String deliveryDateTo = input.getDeliveryDateTo();
		String pickupDateFrom = input.getPickupDateFrom();
		String pickupDateTo = input.getPickupDateTo();
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		whereClause.append(constructInvoicableOrderCriteria());
		
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
      if (StringUtils.isNotEmpty(deliveryDateFrom)){
        	whereClause.append(" and obj.deliveryDate >='"+FormatUtil.formatInputDateToDbDate(deliveryDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(deliveryDateTo)){
	     	whereClause.append(" and obj.deliveryDate <='"+FormatUtil.formatInputDateToDbDate(deliveryDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(pickupDateFrom)){
        	whereClause.append(" and obj.pickupDate >='"+FormatUtil.formatInputDateToDbDate(pickupDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(pickupDateTo)){
	     	whereClause.append(" and obj.pickupDate <='"+FormatUtil.formatInputDateToDbDate(pickupDateTo)+"'");
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
		
		List<Order> invoicableOrderList = filterInvoicableOrders(orderList);
		return invoicableOrderList;
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
	        	whereClause.append(" and obj.paymentDate >='"+FormatUtil.formatInputDateToDbDate(paymentDateFrom)+"'");
			}
	      if (StringUtils.isNotEmpty(paymentDateTo)){
		     	whereClause.append(" and obj.paymentDate <='"+FormatUtil.formatInputDateToDbDate(paymentDateTo)+"'");
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
      
      query.append(" order by obj.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<OrderInvoicePayment> orderInvoicePaymentList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return orderInvoicePaymentList;
	}
	
	private boolean isInvoicableOrder(Long orderId) {
		Order anOrder = genericDAO.getById(Order.class, orderId);
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(anOrder);
		List<Order> invoiceableOrderList = filterInvoicableOrders(orderList);
		return !invoiceableOrderList.isEmpty();
	}
	
	private void validateCreateInvoiceSearch(InvoiceVO input, BindingResult bindingResult) {
		String orderId = input.getOrderId();
		if (StringUtils.isNotEmpty(orderId)) {
			if (!isInvoicableOrder(Long.valueOf(orderId))) {
				bindingResult.rejectValue("orderId", null, null, "Order# " + orderId + " already invoiced for all charges");
			}
			return;
		}
		
		if (StringUtils.isEmpty(input.getCustomerId())) {
			bindingResult.rejectValue("customerId", "NotNull.java.lang.String", null, null);
		}
		/*if (StringUtils.isEmpty(input.getOrderDateFrom())) {
			bindingResult.rejectValue("orderDateFrom", "NotNull.java.lang.String", null, null);
		}
		if (StringUtils.isEmpty(input.getOrderDateTo())) {
			bindingResult.rejectValue("orderDateTo", "NotNull.java.lang.String", null, null);
		}*/
	}
	
	private List<Order> performPreviewInvoiceSearch(InvoiceVO input) {
		String[] orderIdsArr = input.getIds();
		if (orderIdsArr == null || orderIdsArr.length <= 0) {
			return null;
		}
		
		List<Order> orderList = retrieveOrder(orderIdsArr);
		List<Order> invoiceableOrderList = filterInvoicableOrders(orderList);
		return invoiceableOrderList;
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
	
	private void map(List<Order> orderList, List<InvoiceVO> invoiceVOList, List<InvoiceVO> invoicePaymentVOList) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		for (Order anOrder : orderList) {
			InvoiceVO anInvoiceVO = new InvoiceVO();
			map(anOrder, anInvoiceVO, invoicePaymentVOList);
			invoiceVOList.add(anInvoiceVO);
		}
	}
	
	private void map(OrderInvoiceHeader orderInvoiceHeader, InvoiceReportVO anInvoiceReportVO) {
		anInvoiceReportVO.setInvoiceNo(orderInvoiceHeader.getId());
		anInvoiceReportVO.setInvoiceDate(orderInvoiceHeader.getInvoiceDate());
		anInvoiceReportVO.setTotalBalanceAmountDue(orderInvoiceHeader.getTotalBalanceAmountDue());
		anInvoiceReportVO.setTotalInvoicedAmount(orderInvoiceHeader.getTotalInvoicedAmount());
		anInvoiceReportVO.setTotalInvoicePaymentDone(orderInvoiceHeader.getTotalInvoicePaymentDone());
		anInvoiceReportVO.setTotalInvoiceBalanceDue(orderInvoiceHeader.getTotalInvoiceBalanceDue());
	}
	
	private void map(OrderInvoiceDetails orderInvoiceDetails, InvoiceVO anInvoiceVO,
				List<InvoiceVO> invoicePaymentVOList) {
		anInvoiceVO.setId(orderInvoiceDetails.getId());
		anInvoiceVO.setOrderId(orderInvoiceDetails.getOrderId().toString());
		anInvoiceVO.setOrderDate(orderInvoiceDetails.getOrderDate());
		anInvoiceVO.setStatus(orderInvoiceDetails.getOrderStatus());
		
		anInvoiceVO.setDumpsterSize(orderInvoiceDetails.getDumpsterSize());
		
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
		anInvoiceVO.setInvoicedAmount(orderInvoiceDetails.getInvoicedAmount());
		
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
	
	private void map(Order anOrder, InvoiceVO anInvoiceVO, List<InvoiceVO> invoicePaymentVOList) {
		anInvoiceVO.setOrderId(anOrder.getId().toString());
		anInvoiceVO.setOrderDate(anOrder.getCreatedAt());
		anInvoiceVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anInvoiceVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anInvoiceVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anInvoiceVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anInvoiceVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anInvoiceVO.setDumpsterSize(anOrder.getDumpsterSize().getSize());
		
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
		anInvoiceVO.setInvoicedAmount(anOrder.getAmountToBeInvoiced());
		
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
	
	private void map(List<Order> invoicableOrderList, OrderInvoiceHeader orderInvoiceHeader, List<OrderInvoiceDetails> orderInvoiceDetailsList) {
		if (invoicableOrderList == null || invoicableOrderList.isEmpty()) {
			return;
		}
		
		BigDecimal totalFees = new BigDecimal(0.00);
		BigDecimal totalDiscount = new BigDecimal(0.00);
		BigDecimal totalAmountPaid = new BigDecimal(0.00);
		BigDecimal totalBalanceAmountDue = new BigDecimal(0.00);
		BigDecimal totalInvoicedAmount = new BigDecimal(0.00);
		List<Date> orderDateList = new ArrayList<Date>();
		for (Order anInvoicableOrder : invoicableOrderList) {
			orderDateList.add(anInvoicableOrder.getCreatedAt());
			
			OrderInvoiceDetails anOrderInvoiceDetails = new OrderInvoiceDetails();
			anOrderInvoiceDetails.setInvoiceHeader(orderInvoiceHeader);
			
			map(anInvoicableOrder, anOrderInvoiceDetails);
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
			if (anOrderInvoiceDetails.getInvoicedAmount() != null) {
				totalInvoicedAmount = totalInvoicedAmount.add(anOrderInvoiceDetails.getInvoicedAmount());
			}
		}
		
		Collections.sort(orderDateList);
		orderInvoiceHeader.setOrderDateFrom(orderDateList.get(0));
		orderInvoiceHeader.setOrderDateTo(orderDateList.get(orderDateList.size()-1));
		
		orderInvoiceHeader.setTotalFees(totalFees);
		orderInvoiceHeader.setTotalDiscount(totalDiscount);
		orderInvoiceHeader.setTotalAmountPaid(totalAmountPaid);
		orderInvoiceHeader.setTotalBalanceAmountDue(totalBalanceAmountDue);
		
		orderInvoiceHeader.setTotalInvoicedAmount(totalInvoicedAmount);
		orderInvoiceHeader.setTotalInvoiceBalanceDue(totalInvoicedAmount);
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
	
	private void map(OrderInvoicePayment orderInvoicePayment, Map<String, Object> params) {
		OrderInvoiceHeader orderInvoiceHeader = orderInvoicePayment.getInvoice();
		map(orderInvoiceHeader, params);
	}
	
	private void mapForInvoiceReport(OrderInvoiceHeader orderInvoiceHeader, SearchCriteria criteria, Map<String, Object> params) {
		Map<String, Object> criteriaMap = criteria.getSearchMap();
		String invoiceDateFrom = (String)criteriaMap.get("invoiceListReportInvoiceDateFrom");
		String invoiceDateTo = (String)criteriaMap.get("invoiceListReportInvoiceDateTo");
		String dateRange = FormatUtil.formatDateRange(invoiceDateFrom, invoiceDateTo);
		params.put("dateRange", dateRange);
		
		addCustomerData(orderInvoiceHeader, params);
	}
	
	private void map(OrderInvoiceHeader orderInvoiceHeader, Map<String, Object> params) {
		addInvoiceDetails(params, orderInvoiceHeader.getId().toString(), orderInvoiceHeader.getFormattedInvoiceDate());
		
		params.put("invoicedAmount", FormatUtil.formatFee(orderInvoiceHeader.getTotalInvoicedAmount(), true, true));
		params.put("invoicePaymentDone", FormatUtil.formatFee(orderInvoiceHeader.getTotalInvoicePaymentDone(), true, true));
		params.put("invoiceBalanceDue", FormatUtil.formatFee(orderInvoiceHeader.getTotalInvoiceBalanceDue(), true, true));
		
		addCustomerData(orderInvoiceHeader, params);
		
		/*String orderDateRange = FormatUtil.formatDateRange(orderInvoiceHeader.getOrderDateFrom(), orderInvoiceHeader.getOrderDateTo());
		params.put("orderDateRange", orderDateRange);*/
	}
	
	private void addCustomerData(OrderInvoiceHeader orderInvoiceHeader, Map<String, Object> params) {
		addCustomerData(params, orderInvoiceHeader.getCompanyName(), orderInvoiceHeader.getBillingAddress("\n"),
				orderInvoiceHeader.getContactDetails());
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
		orderInvoiceDetails.setInvoicedAmount(anOrder.getAmountToBeInvoiced());
		orderInvoiceDetails.setInvoicePaymentDone(new BigDecimal(0.0));
		orderInvoiceDetails.setInvoiceBalanceDue(orderInvoiceDetails.getInvoicedAmount());
		
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
		
		String query = "select obj.orderId from OrderInvoiceDetails obj where obj.invoiceHeader=" + invoiceId;
		List<OrderInvoiceDetails> orderInvoiceDetailsList = genericDAO.executeSimpleQuery(query);
		List<Long> orderIdList = ModelUtil.extractObjIds(orderInvoiceDetailsList);
		String[] orderIdsArr = CoreUtil.toStringArrFromLong(orderIdList);
		
		String deleteQuery = "delete OrderInvoiceDetails oid where oid.invoiceHeader = " + invoiceId;
		genericDAO.executeUpdate(deleteQuery);
		
		genericDAO.deleteById(OrderInvoiceHeader.class, invoiceId);
		
		String successMsg = InvoiceVO.INV_DEL_SUCCESS_MSG + "  Invoice #: " + invoiceId;
		updateOrder(orderIdsArr, null, null, "N", createdByUser, successMsg);
		
		setSuccessMsg(request, successMsg);
		return "redirect:/" + getUrlContext() + "/manageInvoiceSearch.do";
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
		List<Long> orderIdList = retrieveInvoiceableOrderIds(String.valueOf(customerId));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(orderIdList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/isInvoicableDeletable.do")
	public @ResponseBody String isInvoicableDeletable(ModelMap model, HttpServletRequest request,
					@RequestParam("id") Long invoiceId) {
		String query = "select obj OrderInvoicePayment obj where obj.invoice=" + invoiceId;
		List<OrderInvoicePayment> invoicePaymentList = genericDAO.executeSimpleQuery(query);
		return (invoicePaymentList == null || invoicePaymentList.isEmpty()) ? "true" : "false";
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveInvoiceBalanceDue.do")
	public @ResponseBody String retrieveInvoiceBalanceDue(ModelMap model, HttpServletRequest request,
					@RequestParam("invoiceNo") Long invoiceNo) {
		OrderInvoiceHeader invoiceHeader = genericDAO.getById(OrderInvoiceHeader.class, invoiceNo);
		return String.valueOf(invoiceHeader.getTotalInvoiceBalanceDue());
	}
	
	private boolean validateInvoicePayment(OrderInvoicePayment invoicePayment, StringBuffer errorMsgBuff) {
		if (invoicePayment.getAmountPaid().doubleValue() <= 0.0) {
			errorMsgBuff.append("Invoice payment amount not valid, ");
		}
		
		OrderInvoiceHeader invoice = invoicePayment.getInvoice();
		if (invoice.getTotalInvoiceBalanceDue().doubleValue() == 0.0
				|| invoicePayment.getAmountPaid().compareTo(invoice.getTotalInvoiceBalanceDue()) == 1) {
			errorMsgBuff.append("Invoice payment amount is greater than balance due, ");
		}
		
		int len = errorMsgBuff.length();
		if (len > 0) {
			errorMsgBuff.delete(errorMsgBuff.length()-2, errorMsgBuff.length());
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@RequestMapping(method = { RequestMethod.POST }, value = "/saveInvoicePayment.do")
	public String saveInvoicePayment(HttpServletRequest request,
			@ModelAttribute(INVOICE_PAYMENT_MODEL_OBJECT_KEY) OrderInvoicePayment invoicePayment,
			BindingResult bindingResult, ModelMap model) {
		OrderInvoiceHeader invoice  = genericDAO.getById(OrderInvoiceHeader.class, invoicePayment.getInvoice().getId());
		invoicePayment.setInvoice(invoice);
		
		StringBuffer errorMsgBuff = new StringBuffer();
		boolean valid = validateInvoicePayment(invoicePayment, errorMsgBuff);
		if (!valid) {
			setErrorMsg(request, errorMsgBuff.toString());
			return "redirect:/" + getUrlContext() + "/createInvoicePayment.do?invoiceId="+invoicePayment.getInvoice().getId();
		}
		
		setModifier(request, invoicePayment);
		
		Long invoiceId = invoicePayment.getInvoice().getId();
		List<Order> orderList = retrieveInvoicedOrders(invoiceId);
		List<OrderPayment> newOrderPaymentList = new ArrayList<OrderPayment>();
		List<OrderInvoiceDetails> orderInvoiceDetailsList = new ArrayList<OrderInvoiceDetails>();
		BigDecimal amountAvailable = updateOrderPayment(orderList, invoicePayment, newOrderPaymentList,
				orderInvoiceDetailsList);
		invoicePayment.setAmountAvailable(amountAvailable);
		
		genericDAO.saveOrUpdate(invoicePayment);
		
		OrderInvoiceHeader invoiceHeader = genericDAO.getById(OrderInvoiceHeader.class, invoiceId);
		saveInvoiceHeader(invoiceHeader, invoicePayment);
		
		saveOrderPayment(newOrderPaymentList, invoicePayment);
		
		saveOrderInvoiceDetails(orderInvoiceDetailsList, invoicePayment);
		
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
	private String saveOrderInvoiceDetails(List<OrderInvoiceDetails> orderInvoiceDetailsList, OrderInvoicePayment invoicePayment) {
		if (orderInvoiceDetailsList.isEmpty()) {
			return "Successfull - nothing to process";
		}
		
		for (OrderInvoiceDetails anOrderInvoiceDetails : orderInvoiceDetailsList) {
			anOrderInvoiceDetails.setModifiedAt(invoicePayment.getCreatedAt());
			anOrderInvoiceDetails.setModifiedBy(invoicePayment.getCreatedBy());
			genericDAO.saveOrUpdate(anOrderInvoiceDetails);
		}
		return "Successfully saved order invoice details";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private String saveOrderPayment(List<OrderPayment> orderPaymentList, OrderInvoicePayment invoicePayment) {
		if (orderPaymentList.isEmpty()) {
			return "Successfull - nothing to process";
		}
		
		User createdByUser = getUser(invoicePayment.getCreatedBy());
		for (OrderPayment anOrderPayment : orderPaymentList) {
			anOrderPayment.setInvoicePaymentId(invoicePayment.getId());
			
			Order order = anOrderPayment.getOrder();
			order.setModifiedAt(invoicePayment.getCreatedAt());
			order.setModifiedBy(createdByUser.getId());
			genericDAO.saveOrUpdate(order);
			
			String auditMsg = String.format("Order payment updated.  Invoice#: %s.  Invoice payment#: %s.  Amount paid: %.02f",
					invoicePayment.getInvoice().getId(), invoicePayment.getId(), anOrderPayment.getAmountPaid().doubleValue());
			ModelUtil.createAuditOrderNotes(genericDAO, order, auditMsg, createdByUser);
		}
		return "Successfully saved order payment and order";
	}
	
	private BigDecimal updateOrderPayment(List<Order> orderList, OrderInvoicePayment invoicePayment,
			List<OrderPayment> newOrderPaymentList, List<OrderInvoiceDetails> orderInvoiceDetailsList) {
		if (invoicePayment.getAmountPaid().doubleValue() == 0.0) {
			return invoicePayment.getAmountPaid();
		}
		
		Long invoiceId = invoicePayment.getInvoice().getId();
		Map<Long, OrderInvoiceDetails> invoiceDetailsMap = retrieveInvoiceDetailsMap(invoiceId);
		Map<Long, BigDecimal> existingInvoiceOrderPaymentMap = retrieveInvoicedOrderPaymentAmountMap(invoiceId);
		
		OrderStatus cancelOrderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		BigDecimal amountAvailable = invoicePayment.getAmountPaid();
		for (Order anOrder : orderList) {
			BigDecimal balanceAmountDue = anOrder.getBalanceAmountDue();
			if (balanceAmountDue.doubleValue() == 0.0) {
				continue;
			}
			
			if (anOrder.getOrderStatus().getId().longValue() == cancelOrderStatus.getId().longValue()) {
				continue;
			}
			
			Long orderId = anOrder.getId();
			OrderInvoiceDetails anOrderInvoiceDetails = invoiceDetailsMap.get(orderId);
			BigDecimal orderInvoicedAmount = anOrderInvoiceDetails.getInvoicedAmount();
			BigDecimal payableAmount = orderInvoicedAmount;
			BigDecimal totalInvoiceOrderPaymentMade = existingInvoiceOrderPaymentMap.get(orderId);
			if (totalInvoiceOrderPaymentMade != null) {
				payableAmount = orderInvoicedAmount.subtract(totalInvoiceOrderPaymentMade);
			}
			if (payableAmount.doubleValue() == 0.0) {
				continue;
			}
			
			if (payableAmount.doubleValue() >= amountAvailable.doubleValue()) {
				payableAmount = amountAvailable;
				amountAvailable = new BigDecimal(0.00);
			} else {
				amountAvailable = amountAvailable.subtract(payableAmount);
			}
			
			OrderPayment newOrderPayment = new OrderPayment();
			newOrderPayment.setOrder(anOrder);
			map(invoicePayment, newOrderPayment);
			newOrderPayment.setAmountPaid(payableAmount);
			
			List<OrderPayment> orderPaymentList = anOrder.getOrderPayment();
			orderPaymentList.add(newOrderPayment);
			newOrderPaymentList.add(newOrderPayment);
			
			anOrder.setTotalAmountPaid(anOrder.getTotalAmountPaid().add(payableAmount));
			anOrder.setBalanceAmountDue(anOrder.getBalanceAmountDue().subtract(payableAmount));
			
			if (anOrderInvoiceDetails.getInvoicePaymentDone() == null) {
				anOrderInvoiceDetails.setInvoicePaymentDone(payableAmount);
			} else {
				anOrderInvoiceDetails.setInvoicePaymentDone(anOrderInvoiceDetails.getInvoicePaymentDone().add(payableAmount));
			}
			anOrderInvoiceDetails.setInvoiceBalanceDue(
					anOrderInvoiceDetails.getInvoicedAmount().subtract(anOrderInvoiceDetails.getInvoicePaymentDone()));
			orderInvoiceDetailsList.add(anOrderInvoiceDetails);
			
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
		orderPayment.setInvoiceDate(invoicePayment.getInvoice().getInvoiceDate());
		orderPayment.setCreatedAt(invoicePayment.getCreatedAt());
		orderPayment.setCreatedBy(invoicePayment.getCreatedBy());
	}
	
	private List<Order> retrieveInvoicedOrders(Long invoiceId) {
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
			setErrorMsg(request, response, "Please select at least one order");
			return "redirect:/" + getUrlContext() + "/createInvoiceMain.do";
		}
		
		List<Order> orderList = retrieveOrder(orderIdsArr);
		List<Order> invoicableOrderList = filterInvoicableOrders(orderList);
		if (invoicableOrderList.isEmpty()) {
			setErrorMsg(request, response, "Order(s) selected are not invoicable");
			return "redirect:/" + getUrlContext() + "/createInvoiceMain.do";
		}
		
		User createdByUser = getUser(request);
		Long createdBy = createdByUser.getId();
		
		OrderInvoiceHeader orderInvoiceHeader = new OrderInvoiceHeader();
		orderInvoiceHeader.setCreatedAt(Calendar.getInstance().getTime());
		orderInvoiceHeader.setCreatedBy(createdBy);
		
		orderInvoiceHeader.setOrderCount(invoicableOrderList.size());
		
		map(input, orderInvoiceHeader);
		map(invoicableOrderList.get(0), orderInvoiceHeader);
		
		List<OrderInvoiceDetails> orderInvoiceDetailsList = new ArrayList<OrderInvoiceDetails>();
		map(invoicableOrderList, orderInvoiceHeader, orderInvoiceDetailsList);
		
		genericDAO.save(orderInvoiceHeader);
		
		for (OrderInvoiceDetails anOrderInvoiceDetails : orderInvoiceDetailsList) {
			anOrderInvoiceDetails.setCreatedAt(Calendar.getInstance().getTime());
			anOrderInvoiceDetails.setCreatedBy(createdBy);
			genericDAO.save(anOrderInvoiceDetails);
		}
		
		Long invoiceId = orderInvoiceHeader.getId();
		String successMsg = InvoiceVO.INV_SAVE_SUCCESS_MSG + "  Invoice #: " + invoiceId;
		updateOrderList(invoicableOrderList, invoiceId, orderInvoiceHeader.getInvoiceDate(), createdByUser, successMsg);
		
		setSuccessMsg(request, successMsg);
		return "redirect:/" + getUrlContext() + "/manageInvoiceMain.do";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private void updateOrderList(List<Order> invoicableOrderList, Long invoiceId, Date invoiceDate, 
			User createdByUser, String msg) {
		for (Order anInvoicableOrder : invoicableOrderList) {
			String invoiceIdsToBeUpdated = invoiceId.toString();
			if (StringUtils.isNotEmpty(anInvoicableOrder.getInvoiceIds())) {
				invoiceIdsToBeUpdated = anInvoicableOrder.getInvoiceIds() + "," + invoiceIdsToBeUpdated;
			}
			String invoiceDatesToBeUpdated = FormatUtil.dbDateFormat.format(invoiceDate);
			if (StringUtils.isNotEmpty(anInvoicableOrder.getInvoiceDates())) {
				invoiceDatesToBeUpdated = anInvoicableOrder.getInvoiceDates() + "," + invoiceDatesToBeUpdated;
			}
			BigDecimal invoicedAmount = anInvoicableOrder.getAmountToBeInvoiced();
			if (anInvoicableOrder.getInvoicedAmount() != null) {
				invoicedAmount = anInvoicableOrder.getInvoicedAmount().add(invoicedAmount);
			}
			
			anInvoicableOrder.setInvoiced("Y");
			anInvoicableOrder.setInvoiceIds(invoiceIdsToBeUpdated);
			anInvoicableOrder.setInvoiceDates(invoiceDatesToBeUpdated);
			anInvoicableOrder.setInvoicedAmount(invoicedAmount);
			
			anInvoicableOrder.setModifiedAt(Calendar.getInstance().getTime());
			anInvoicableOrder.setModifiedBy(createdByUser.getId());
			genericDAO.save(anInvoicableOrder);
		}
		
		ModelUtil.createAuditOrderNotes(genericDAO, invoicableOrderList, msg, createdByUser);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private int updateOrder(String[] orderIdsArr, Long invoiceId, Date invoiceDate, String invoiced, 
			User createdByUser, String msg) {
		Long createdBy = createdByUser.getId();
		Date currentTime = Calendar.getInstance().getTime();
		String orderIds = CoreUtil.toString(orderIdsArr);
		
		String invoiceIdsToBeUpdated = StringUtils.EMPTY;
		if (invoiceId == null) {
			invoiceIdsToBeUpdated = "null";
		} else {
			invoiceIdsToBeUpdated = "concat(IFNULL(invoiceIds, ''), " + invoiceId.toString() + ",)";
		}
		
		String invoiceDatesToBeUpdated = StringUtils.EMPTY;
		if (invoiceDate == null) {
			invoiceDatesToBeUpdated = "null";
		} else {
			invoiceDatesToBeUpdated = "concat(IFNULL(invoiceDate, ''), " 
						+ FormatUtil.dbDateTimeFormat.format(invoiceDate) + ",)";
		}
		
		String orderUpdateQuery = "update Order o set o.invoiced = '" + invoiced + "',"
			+ " o.invoiceIds = " + invoiceIdsToBeUpdated + ","
			+ " o.invoiceDates = " + invoiceDatesToBeUpdated + ","
			// invoicedAmount
			+ " o.modifiedBy = " + createdBy + ","
			+ " o.modifiedAt = '" + FormatUtil.dbDateTimeFormat.format(currentTime) + "'"
			+ " where o.id in (" + orderIds + ")";
		int noOrdersUpdated =  genericDAO.executeUpdate(orderUpdateQuery);
		
		ModelUtil.createAuditOrderNotes(genericDAO, orderIdsArr, msg, createdByUser);
		
		return noOrdersUpdated;
	}
	
	@RequestMapping(method = {RequestMethod.POST}, value = "/previewInvoice.do")
	public String previewInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
					@RequestParam("id") String[] ids,
					@RequestParam("invoiceDate") Date invoiceDate,
					@RequestParam("invoiceNotes") String invoiceNotes) {
		setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute(ReportUtil.inputKey);
		input.setIds(ids);
		input.setInvoiceDate(invoiceDate);
		input.setNotes(invoiceNotes);
		//input.setHistoryCount(-1);
		
		return processPreviewInvoice(request, response, input);
	}
	
	private String processPreviewInvoice(HttpServletRequest request, HttpServletResponse response,
			InvoiceVO input) {
		Map<String, Object> datas = generateInvoiceData(request, input);
		List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get(ReportUtil.dataKey);
		Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
		
		addWaterMarkRendererReportParam(params, true, "Preview");
		
		String type = "html";
		//setReportRequestHeaders(response, type, ORDER_INVOICE_MASTER_REPORT);
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(ORDER_INVOICE_REPORT,
						invoiceVOList, params, type, request);
			/*JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(ORDER_INVOICE_MASTER_REPORT, 
					ORDER_INVOICE_SUB_REPORT, invoiceVOList, params, type, request);*/
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
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_REPORT);
		
		ByteArrayOutputStream out = null;
		try {
			InvoiceVO input = (InvoiceVO)request.getSession().getAttribute("input");
			Map<String, Object> datas = generateInvoiceData(request, input);
			
			List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get(ReportUtil.dataKey);
			Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
			addWaterMarkRendererReportParam(params, true, "Preview");
			
			out = dynamicReportService.generateStaticReport(ORDER_INVOICE_REPORT,
						invoiceVOList, params, type, request);
			/*out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER_REPORT, 
					ORDER_INVOICE_SUB_REPORT, invoiceVOList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Exception occured while exporting invoice preview: " + t);
			
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
			log.warn("Exception occured while generating create invoice report: " + t);
			
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
			log.warn("Exception occured while generating manage invoice report: " + t);
			
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
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/invoicePaymentExport.do")
	public String invoicePaymentExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		String reportName = "invoicePaymentReport";
		type = setReportRequestHeaders(response, type, reportName);
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			List<OrderInvoicePayment> orderInvoicePaymentList = performInvoicePaymentSearch(searchCriteria);
			
			out = dynamicReportService.exportReport(reportName, type, OrderInvoicePayment.class, orderInvoicePaymentList,
						columnPropertyList, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating invoice payment report: " + t);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/reports/invoiceListReportExport.do")
	public String invoiceListReportExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type) {
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> reportData = generateInvoiceListReportData(model, request);
			Map<String, Object> params = (Map<String, Object>) reportData.get(ReportUtil.paramsKey);
			List<InvoiceReportVO> data = (List<InvoiceReportVO>) reportData.get(ReportUtil.dataKey);
			
			if (data.isEmpty()) {
				return null;
			}
			
			String reportName = "orderInvoiceReportMaster";
			String subReportName = "orderInvoiceOrderSub";
			
			type = setReportRequestHeaders(response, type, reportName);
			
			out = dynamicReportService.generateStaticMasterSubReport(reportName, subReportName, data, params, 
					type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate Invoice List report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Invoice List report: " + t);
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
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_REPORT);
		
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateInvoiceData(request, invoiceId);
			List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get(ReportUtil.dataKey);
			Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
			
			out = dynamicReportService.generateStaticReport(ORDER_INVOICE_REPORT, invoiceVOList, params, type, request);
			/*out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER_REPORT, ORDER_INVOICE_SUB_REPORT,
					invoiceVOList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Exception occured while generating invoice: " + t);
			
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
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/downloadInvoicePaymentAll.do")
	public String downloadInvoicePaymentAll(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "invoiceId") Long invoiceId,
			@RequestParam(required = false, value = "invoicePaymentId") Long invoicePaymentId,
			@RequestParam(required = true, value = "type") String type) {
		if (invoicePaymentId != null) {
			OrderInvoicePayment invoicePayment = genericDAO.getById(OrderInvoicePayment.class, invoicePaymentId);
			invoiceId = invoicePayment.getInvoice().getId();
		}
		
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_PAYMENT_MASTER_REPORT);
		
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateInvoicePaymentAllData(request, invoiceId);
			List<OrderInvoicePayment> invoicePaymentList = (List<OrderInvoicePayment>) datas.get(ReportUtil.dataKey);
			Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
			
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_PAYMENT_MASTER_REPORT, ORDER_INVOICE_PAYMENT_SUB_REPORT,
					invoicePaymentList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Exception occured while generating invoice payment report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating invoice payment report: " + t);
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
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/downloadInvoicePayment.do")
	public String downloadInvoicePayment(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "invoicePaymentId") Long invoicePaymentId,
			@RequestParam(required = true, value = "type") String type) {
		type = setReportRequestHeaders(response, type, ORDER_INVOICE_PAYMENT_MASTER_REPORT);
		
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> datas = generateInvoicePaymentData(request, invoicePaymentId);
			List<OrderInvoicePayment> invoicePaymentList = (List<OrderInvoicePayment>) datas.get(ReportUtil.dataKey);
			Map<String, Object> params = (Map<String, Object>) datas.get(ReportUtil.paramsKey);
			
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_PAYMENT_MASTER_REPORT, ORDER_INVOICE_PAYMENT_SUB_REPORT,
					invoicePaymentList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate static report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating invoice payment report: " + t);
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
	
	private Map<Long, OrderInvoiceDetails> retrieveInvoiceDetailsMap(Long invoiceId) {
		Map<Long, OrderInvoiceDetails> invoiceDetailsMap = new HashMap<Long, OrderInvoiceDetails>();
		List<OrderInvoiceDetails> invoiceDetailsList = retrieveOrderInvoiceDetails(invoiceId);
		if (invoiceDetailsList == null || invoiceDetailsList.isEmpty()) {
			return invoiceDetailsMap;
		}
		
		for (OrderInvoiceDetails anInvoiceDetails : invoiceDetailsList) {
			invoiceDetailsMap.put(anInvoiceDetails.getOrderId(), anInvoiceDetails);
		}
		return invoiceDetailsMap;
	}
	
	private List<OrderInvoiceDetails> retrieveOrderInvoiceDetails(Long invoiceId) {
		String query = "select obj from OrderInvoiceDetails obj where obj.invoiceHeader.id=" + invoiceId
					+ " order by obj.orderId asc";
		List<OrderInvoiceDetails> orderInvoiceDetailsList = genericDAO.executeSimpleQuery(query);
		return orderInvoiceDetailsList;
	}
	
	private Map<Long, BigDecimal> retrieveOrderInvoicedAmountMap(List<Long> orderIdList) {
		Map<Long, BigDecimal> orderInvoicedAmountMap = new HashMap<Long, BigDecimal>();
		if (orderIdList.isEmpty()) {
			return orderInvoicedAmountMap;
		}
		
		String orderIds = CoreUtil.toStringFromLong(orderIdList);
		String query = "select obj.orderId, sum(invoicedAmount) from OrderInvoiceDetails obj where obj.deleteFlag=1"
				+ " and orderId in(" + orderIds + ")"
				+ " group by obj.orderId"
				+ " order by obj.orderId asc";
		List<?> resultObjectList = genericDAO.executeSimpleQuery(query);
		if (resultObjectList == null || resultObjectList.isEmpty()) {
			return orderInvoicedAmountMap;
		}
		
		Long orderId;
		BigDecimal totalInvoicedAmount;
		for (Object[] resultObj : (List<Object[]>)resultObjectList) {
			orderId = ((Long)resultObj[0]);
			totalInvoicedAmount = ((BigDecimal)resultObj[1]);
			orderInvoicedAmountMap.put(orderId, totalInvoicedAmount);
		}   
		
		return orderInvoicedAmountMap;
	}
	
	private Map<Long, BigDecimal> retrieveNonInvoicedOrderPaymentAmountMap(List<Long> orderIdList) {
		Map<Long, BigDecimal> nonInvoicedOrderPaymentAmountMap = new HashMap<Long, BigDecimal>();
		if (orderIdList.isEmpty()) {
			return nonInvoicedOrderPaymentAmountMap;
		}
		
		String orderIds = CoreUtil.toStringFromLong(orderIdList);
		String query = "select obj.order.id, sum(amountPaid) from OrderPayment obj where obj.deleteFlag=1"
				+ " and obj.order.id in(" + orderIds + ")"
				+ " and invoiceId is null"
				+ " group by obj.order.id"
				+ " order by obj.order.id asc";
		List<?> resultObjectList = genericDAO.executeSimpleQuery(query);
		if (resultObjectList == null || resultObjectList.isEmpty()) {
			return nonInvoicedOrderPaymentAmountMap;
		}
		
		Long orderId;
		BigDecimal totalAmountPaid;
		for (Object[] resultObj : (List<Object[]>)resultObjectList) {
			orderId = ((Long)resultObj[0]);
			totalAmountPaid = ((BigDecimal)resultObj[1]);
			nonInvoicedOrderPaymentAmountMap.put(orderId, totalAmountPaid);
		}   
		
		return nonInvoicedOrderPaymentAmountMap;
	}
	
	private Map<Long, BigDecimal> retrieveInvoicedOrderPaymentAmountMap(Long invoiceId) {
		Map<Long, BigDecimal> invoiceOrderPaymentAmountMap = new HashMap<Long, BigDecimal>();
		String query = "select obj.order.id, sum(amountPaid) from OrderPayment obj where obj.deleteFlag=1"
				+ " and invoiceId=" + invoiceId
				+ " group by obj.order.id"
				+ " order by obj.order.id asc";
		List<?> resultObjectList = genericDAO.executeSimpleQuery(query);
		if (resultObjectList == null || resultObjectList.isEmpty()) {
			return invoiceOrderPaymentAmountMap;
		}
		
		Long orderId;
		BigDecimal totalAmountPaid;
		for (Object[] resultObj : (List<Object[]>)resultObjectList) {
			orderId = ((Long)resultObj[0]);
			totalAmountPaid = ((BigDecimal)resultObj[1]);
			invoiceOrderPaymentAmountMap.put(orderId, totalAmountPaid);
		}   
		
		return invoiceOrderPaymentAmountMap;
	}
	
	private void setupPreviewInvoice(HttpServletRequest request, ModelMap model) {
		addMsgCtx(model, previewInvoiceMsgCtx);
	}
	
	private Map<String, Object> generateInvoiceData(HttpServletRequest request, InvoiceVO input) {
		List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> datas = new HashMap<String, Object>();
		addData(datas, invoiceVOList, params);
		
		List<Order> invoiceableOrderList = performPreviewInvoiceSearch(input);
		if (invoiceableOrderList == null || invoiceableOrderList.isEmpty()) {
			return datas;
		}
		
		List<InvoiceVO> invoicePaymentVOList = new ArrayList<InvoiceVO>();
		map(invoiceableOrderList, invoiceVOList, invoicePaymentVOList);
		
		map(input, invoiceVOList, invoicePaymentVOList, params);
		
		addRDSBillingInfo(params);
		addLogoFilePath(request, params);
		
		return datas;
	}
	
	private Map<String, Object> generateInvoicePaymentData(HttpServletRequest request, Long invoicePaymentId) {
		OrderInvoicePayment invoicePayment = genericDAO.getById(OrderInvoicePayment.class, invoicePaymentId);
		List<OrderPayment> orderPaymentList = retrieveInvoiceOrderPaymentDetails(invoicePaymentId);
		invoicePayment.setOrderPaymentList(orderPaymentList);
		
		List<OrderInvoicePayment> invoicePaymentList = new ArrayList<OrderInvoicePayment>();
		invoicePaymentList.add(invoicePayment);
		
		Map<String, Object> params = new HashMap<String, Object>();
		map(invoicePayment, params);
		addRDSBillingInfo(params);
		addLogoFilePath(request, params);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		addData(datas, invoicePaymentList, params);
		
		return datas;
	}
	
	private Map<String, Object> generateInvoicePaymentAllData(HttpServletRequest request, Long invoiceId) {
		OrderInvoiceHeader invoiceHeader = genericDAO.getById(OrderInvoiceHeader.class, invoiceId);
		
		String query = "select obj from OrderInvoicePayment obj where obj.deleteFlag=1";
		query += (" and obj.invoice.id=" + invoiceId);
		query += " order by id desc";
		List<OrderInvoicePayment> invoicePaymentList = genericDAO.executeSimpleQuery(query);
		
		for (OrderInvoicePayment orderInvoicePayment : invoicePaymentList) {
			List<OrderPayment> orderPaymentList = retrieveInvoiceOrderPaymentDetails(orderInvoicePayment.getId());
			orderInvoicePayment.setOrderPaymentList(orderPaymentList);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		map(invoiceHeader, params);
		addRDSBillingInfo(params);
		addLogoFilePath(request, params);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		addData(datas, invoicePaymentList, params);
		
		return datas;
	}
	
	private Map<String, Object> generateInvoiceData(HttpServletRequest request, Long invoiceId) {
		OrderInvoiceHeader orderInvoiceHeader = retrieveOrderInvoiceHeader(invoiceId);
		Map<String, Object> params = new HashMap<String, Object>();
		map(orderInvoiceHeader, params);
		
		//List<OrderInvoiceDetails> orderInvoiceDetailsList = retrieveOrderInvoiceDetails(invoiceId);
		List<OrderInvoiceDetails> orderInvoiceDetailsList = orderInvoiceHeader.getOrderInvoiceDetails();
		List<InvoiceVO> invoiceVOList = new ArrayList<InvoiceVO>();
		List<InvoiceVO> invoiceVOPaymentList = new ArrayList<InvoiceVO>();
		mapToInvoiceVOList(orderInvoiceDetailsList, invoiceVOList, invoiceVOPaymentList);
		//params.put("orderPaymentList", invoiceVOPaymentList);
		
		addRDSBillingInfo(params);
		addLogoFilePath(request, params);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		addData(datas, invoiceVOList, params);
		     
		return datas;
	}
	
	private void map(InvoiceVO invoiceVO, List<InvoiceVO> invoiceVOList, List<InvoiceVO> invoicePaymentVOList,
			Map<String, Object> params) {
		addInvoiceDetails(params, "TBD - Preview", invoiceVO.getFormattedInvoiceDate());
		
		Customer customer = retrieveCustomer(invoiceVO.getCustomerId(), invoiceVO.getOrderId());
		addCustomerData(customer, params);
		
		/*List<Date> orderDateList = new ArrayList<Date>();
		for (InvoiceVO anInvoiceVO : invoiceVOList) {
			orderDateList.add(anInvoiceVO.getOrderDate());
		}
		Collections.sort(orderDateList);
		
		String orderDateRange = FormatUtil.formatDateRange(orderDateList.get(0), orderDateList.get(orderDateList.size()-1));
		params.put("orderDateRange", orderDateRange);*/
		
		//params.put("orderPaymentList", invoicePaymentVOList);
	}
	
	private void addInvoiceDetails(Map<String, Object> params, String invoiceNo, String invoiceDate) {
		params.put("invoiceNo", invoiceNo);
		params.put("invoiceDate", invoiceDate);
	}
	
	private void addCustomerData(Map<String, Object> params, String companyName, String billingAddress, 
			String contactDetails) {
		params.put("customer", companyName);
		params.put("billingAddress", billingAddress);
		params.put("contact", contactDetails);
	}
	
	private void addCustomerData(Customer customer, Map<String, Object> params) {
		addCustomerData(params, customer.getCompanyName(), customer.getBillingAddress("\n"),
				customer.getContactDetails());
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
