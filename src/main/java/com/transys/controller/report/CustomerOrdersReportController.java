package com.transys.controller.report;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import com.transys.core.util.ModelUtil;

import com.transys.model.Customer;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderStatus;
import com.transys.model.SearchCriteria;
import com.transys.model.vo.CustomerReportVO;
import com.transys.model.vo.OrderReportVO;

@Controller
@RequestMapping("/reports/customerOrdersReport")
public class CustomerOrdersReportController extends ReportController {
	public CustomerOrdersReportController(){	
		setUrlContext("reports/customerOrdersReport");
		setMessageCtx("customerOrdersReport");
		
		setReportName("customerOrdersReportMaster");
		setSubReportName("customerOrdersReportSub");
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 750;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 2500;
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1' order by obj.companyName asc");
		model.addAttribute("customer", customerList);
		
		Object[] objArr = ModelUtil.retrieveContactDetails(customerList);
		String[] phoneArr = (String[])objArr[0];
		String[] contactNameArr = (String[])objArr[1];
		
		model.addAttribute("phones", phoneArr);
		model.addAttribute("contactNames", contactNameArr);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
	}
	
	@Override
	protected List<CustomerReportVO> performSearch(HttpServletRequest request, SearchCriteria criteria, Map<String, Object> params) {
		List<CustomerReportVO> customerReportVOList = retrieveCustomerOrdersReportData(criteria);
		
		String[] orderDates = extractOrderDateRange(criteria, customerReportVOList);
		params.put("ORDER_DATE_FROM", orderDates[0]);
		params.put("ORDER_DATE_TO", orderDates[1]);
		
		return customerReportVOList;
	}
	
	private String[] extractOrderDateRange(SearchCriteria criteria, List<CustomerReportVO> customerReportVOList) {
		Map<String, Object> criteriaMap = (Map<String, Object>)criteria.getSearchMap();
		
		String orderDateFrom = criteriaMap.getOrDefault("createdAtFrom", StringUtils.EMPTY).toString();
		String orderDateTo = criteriaMap.getOrDefault("createdAtTo", StringUtils.EMPTY).toString();
		
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
}
