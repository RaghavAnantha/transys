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

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.transys.controller.BaseController;

import com.transys.service.DynamicReportService;

import com.transys.core.util.CoreUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.MimeUtil;
import com.transys.core.util.ModelUtil;
import com.transys.core.util.ServletUtil;

import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderPayment;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;
import com.transys.model.User;
import com.transys.model.invoice.OrderInvoiceDetails;
import com.transys.model.invoice.OrderInvoiceHeader;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.invoice.InvoiceVO;

@Controller
@RequestMapping("/invoice")
@Transactional(readOnly = false)
public class InvoiceController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;
	
	private static String ORDER_INVOICE_MASTER = "orderInvoiceMaster";
	private static String ORDER_INVOICE_SUB = "orderInvoiceSub";
	
	public InvoiceController() {
		setUrlContext("invoice");
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceMain.do")
	public String createInvoiceMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupCreateInvoiceList(model, request);
		
		return getUrlContext() + "/invoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceMain.do")
	public String manageInvoiceMain(ModelMap model, HttpServletRequest request) {
		/*SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}*/
		
		setupManageInvoiceList(model, request);
		
		return getUrlContext() + "/manageInvoiceList";
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceSearch.do")
	public String createInvoiceSearch(ModelMap model, HttpServletRequest request,
			@ModelAttribute("modelObject") InvoiceVO input,
			BindingResult bindingResult) {
		setupCreateInvoiceList(model, request);
		
		String returnUrl = getUrlContext() + "/invoice";
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
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
		setupManageInvoiceList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoiceHeader> orderInvoiceHeaderList = performManageInvoiceSearch(criteria);
		model.addAttribute("list", orderInvoiceHeaderList);
		
		return getUrlContext() + "/manageInvoiceList";
	}
	
	private void setupCommon(ModelMap model, HttpServletRequest request,
					String customerIdKey, String orderInvoiced) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria searchCriteria = (SearchCriteria)request.getSession().getAttribute("searchCriteria");
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		String customerId = (String)searchCriteria.getSearchMap().get(customerIdKey);
		if (StringUtils.isNotEmpty(customerId)) {
			List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
			model.addAttribute("deliveryAddresses", deliveryAddressVOList);
		}
		
		model.addAttribute("orderIds", retrieveOrderIds(orderInvoiced));
	}
	
	private void setupCreateInvoiceList(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request, "customerId", "N");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(150);

		model.addAttribute("msgCtx", "createInvoice");
		model.addAttribute("errorCtx", "createInvoice");
		model.addAttribute("activeTab", "createInvoice");
	}
	
	private void setupManageInvoiceList(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request, "manageInvoiceCustomerId", "Y");
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		searchCriteria.setPage(0);
		searchCriteria.setPageSize(100);
		
		String query = "select obj.id from OrderInvoiceHeader obj where obj.deleteFlag=1 order by obj.id asc";
		/*List<OrderInvoiceHeader> orderInvoiceHeaderList = genericDAO.executeSimpleQuery(query);
		List<String> invoiceNoList = new ArrayList<String>();
		for (Object obj : orderInvoiceHeaderList) {
			invoiceNoList.add(obj.toString());
		}
		model.addAttribute("invoiceNos", invoiceNoList);*/
		model.addAttribute("invoiceNos", genericDAO.executeSimpleQuery(query));
		
		model.addAttribute("msgCtx", "manageInvoice");
		model.addAttribute("errorCtx", "manageInvoice");
		model.addAttribute("activeTab", "manageInvoice");
	}
	
	private List<Order> retrieveOrderIds(String invoiced) {
		String query = "select obj.id from Order obj where obj.deleteFlag=1 "
				+ " and obj.invoiced='" + invoiced + "'";
		if (StringUtils.equals(invoiced, "N")) {
			OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
			query	+= " and obj.orderStatus.id !=" + orderStatus.getId().longValue();
			query	+= " and obj.balanceAmountDue > 0.0";
		}
		query	+= " order by obj.id asc";
		
		return genericDAO.executeSimpleQuery(query);
		
		/*List<Order> orderList = genericDAO.executeSimpleQuery(query);
		List<String> orderIdList = new ArrayList<String>();
		for (Object obj : orderList) {
			orderIdList.add(obj.toString());
		}
		return orderIdList;*/
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
        	whereClause.append(" and obj.createdAt >='"+FormatUtil.convertInputDateToDbDate(orderDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	whereClause.append(" and obj.createdAt <='"+FormatUtil.convertInputDateToDbDate(orderDateTo)+"'");
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
		String invoiceId = (String) criteria.getSearchMap().get("manageInvoiceInvoiceNo");
		String customerId = (String) criteria.getSearchMap().get("manageInvoiceCustomerId");
		String orderId = (String) criteria.getSearchMap().get("manageInvoiceOrderId");
		String deliveryAddress = (String) criteria.getSearchMap().get("manageInvoiceDeliveryAddress");
		String orderDateFrom = (String) criteria.getSearchMap().get("manageInvoiceOrderDateFrom");
		String orderDateTo = (String) criteria.getSearchMap().get("manageInvoiceOrderDateTo");
		String invoiceDateFrom = (String) criteria.getSearchMap().get("manageInvoiceInvoiceDateFrom");
		String invoiceDateTo = (String) criteria.getSearchMap().get("manageInvoiceInvoiceDateTo");
		
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
        	whereClause.append(" and obj.orderDateFrom >='"+FormatUtil.convertInputDateToDbDate(orderDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(orderDateTo)){
	     	whereClause.append(" and obj.orderDateTo <='"+FormatUtil.convertInputDateToDbDate(orderDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(invoiceDateFrom)){
        	whereClause.append(" and obj.invoiceDate >='"+FormatUtil.convertInputDateToDbDate(invoiceDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(invoiceDateTo)){
	     	whereClause.append(" and obj.invoiceDate <='"+FormatUtil.convertInputDateToDbDate(invoiceDateTo)+"'");
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
		
		anInvoiceVO.setCityFee(orderInvoiceDetails.getCityFee() == null ? (new BigDecimal("0.00")) : orderInvoiceDetails.getCityFee());
		
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
		
		anInvoiceVO.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal("0.00")) : anOrderFees.getCityFee());
		
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
	}
	
	private void map(InvoiceVO input, OrderInvoiceHeader orderInvoiceHeader) {
		orderInvoiceHeader.setInvoiceDate(input.getInvoiceDate());
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
		
		orderInvoiceDetails.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal("0.00"))
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
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/deleteInvoice.do")
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
		
		//String json = (new Gson()).toJson(addressList);
		//return json;
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
	
	private int updateOrder(String[] orderIdsArr, Long invoiceId, Date invoiceDate, String invoiced, Long createdBy) {
		Date currentTime = Calendar.getInstance().getTime();
		String orderIds = CoreUtil.toString(orderIdsArr);
		
		String invoiceIdStr = (invoiceId == null) ? "null" : invoiceId.toString();
		String invoiceDateStr = (invoiceDate == null) ? "null" : 
			"'" + ModelUtil.mysqlDf.format(invoiceDate) + "'";
		
		String orderUpdateQuery = "update Order o set o.invoiced = '" + invoiced + "',"
			+ " o.invoiceId = " + invoiceIdStr + ","
			+ " o.invoiceDate = " + invoiceDateStr + ","
			+ " o.modifiedBy = " + createdBy + ","
			+ " o.modifiedAt = '" + ModelUtil.mysqlDf.format(currentTime) + "'"
			+ " where o.id in (" + orderIds + ")";
		return genericDAO.executeUpdate(orderUpdateQuery);
	}
	
	@RequestMapping(method = {RequestMethod.POST }, value = "/previewInvoice.do")
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
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			/*JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
						invoiceVOList, params, request);*/
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(ORDER_INVOICE_MASTER, 
					ORDER_INVOICE_SUB, invoiceVOList, params, request);
			if (jasperPrint == null) {
				setErrorMsg(request, response, "Error occured while processing invoice preview");
			} else {
				//request.setAttribute("japserPrint", jasperPrint);
				request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			}
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMsg(request, response, e.getMessage());
		}
		
		return getUrlContext() + "/previewInvoice";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/previewInvoiceExport.do")
	public String previewInvoiceExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		//setupPreviewInvoice(request, model);
		
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateInvoiceData(request, input);
		
		List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		addWaterMarkRendererReportParam(params, true, "Preview");
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+ORDER_INVOICE_MASTER+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.generateStaticReport(reportName,
						invoiceVOList, params, type, request);*/
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER, ORDER_INVOICE_SUB,
					invoiceVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate static report:" + e);
			
			setupPreviewInvoice(request, model);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/previewInvoice";
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/createInvoiceExport.do")
	public String createInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "ordersBalanceDueReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
		List<Order> orderList = performCreateInvoiceSearch(searchCriteria, input);
		Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, Order.class, orderList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					orderList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate report:" + e);
			
			setupCreateInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/invoice";
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/manageInvoiceExport.do")
	public String manageInvoiceExport(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "invoiceReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<OrderInvoiceHeader> orderInvoiceHeaderList = performManageInvoiceSearch(searchCriteria);
		//Map<String, Object> params = new HashMap<String, Object>();
		
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.exportReport(reportName, type, OrderInvoiceHeader.class, orderInvoiceHeaderList,
						columnPropertyList, request);
			/*out = dynamicReportService.generateStaticReport(reportName,
					invoiceList, params, type, request);*/
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate report:" + e);
			
			setupManageInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/manageInvoiceList";
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
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/downloadInvoice.do")
	public String downloadInvoice(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "id") Long invoiceId,
			@RequestParam(required = true, value = "type") String type) {
		setupManageInvoiceList(model, request);
		
		Map<String, Object> datas = generateInvoiceData(request, invoiceId);
		List<InvoiceVO> invoiceVOList = (List<InvoiceVO>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename="+ORDER_INVOICE_MASTER+"." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.generateStaticReport(reportName,
						invoiceVOList, params, type, request);*/
			out = dynamicReportService.generateStaticMasterSubReport(ORDER_INVOICE_MASTER, ORDER_INVOICE_SUB,
					invoiceVOList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to generate static report:" + e);
			
			setupManageInvoiceList(model, request);
			setErrorMsg(request, response, e.getMessage());
			return getUrlContext() + "/manageInvoiceList";
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
	
	@ModelAttribute("modelObject")
	public InvoiceVO setupModel(HttpServletRequest request) {
		return new InvoiceVO();
	}
}
