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

import com.transys.core.util.CoreUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderStatus;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.CustomerVO;

@Controller
@RequestMapping("/reports/order/orderListReport")
public class OrderListReportController extends ReportController {
	public OrderListReportController(){	
		setUrlContext("reports/order/orderListReport");
		setMessageCtx("orderListReport");
		
		setReportName("orderListReport");
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 750;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 1000;
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		String[][] strArrOfArr = ModelUtil.retrieveOrderDeliveryContactDetails(genericDAO);
		String[] phoneArr = strArrOfArr[0];
		//String[] contactNameArr = strArrOfArr[1];
		
		model.addAttribute("deliveryContactPhones", phoneArr);
		//model.addAttribute("deliveryContactNames", contactNameArr);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveOrderCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
	}
	
	@Override
	protected List<Map<String, Object>> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria, Map<String, Object> params) {
		List<Map<String, Object>> reportDataList = new ArrayList<Map<String, Object>>();
		
		List<Order> orderList =  genericDAO.search(Order.class, criteria, "id", null, null);
		if (orderList == null || orderList.isEmpty()) {
			return reportDataList;
		}
		
		populateOrderReportData(orderList, reportDataList);
		
		Map<String, Object> criteriaMap = (Map<String, Object>)criteria.getSearchMap();
		String orderDateFrom = criteriaMap.getOrDefault("createdAtFrom", StringUtils.EMPTY).toString();
		String orderDateTo = criteriaMap.getOrDefault("createdAtTo", StringUtils.EMPTY).toString();
		orderDateFrom = StringUtils.defaultIfEmpty(orderDateFrom, orderList.get(0).getFormattedCreatedAt());
		orderDateTo = StringUtils.defaultIfEmpty(orderDateTo, orderList.get(orderList.size() - 1).getFormattedCreatedAt());
		
		params.put("orderDateFrom", orderDateFrom);
		params.put("orderDateTo", orderDateTo);
		
		return reportDataList;
	}
	
	private void populateOrderReportData(List<Order> orderList, List<Map<String, Object>> reportDataList) {
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		
		BigDecimal defaultFee = new BigDecimal("0.00");
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
			
			aReportRow.put("totalPaid", CoreUtil.defaultIfNull(order.getTotalAmountPaid(), defaultFee));
			aReportRow.put("balanceDue", CoreUtil.defaultIfNull(order.getBalanceAmountDue(), defaultFee));
			
			OrderFees orderFees = order.getOrderFees();
			populateFees(aReportRow, orderFees, defaultFee);
			
			/*List<OrderPayment> orderPaymentList = order.getOrderPayment();
			if (orderPaymentList != null && !orderPaymentList.isEmpty()) {
				OrderPayment anOrderPayment = orderPaymentList.get(0);
				aReportRow.put("paymentMethod", StringUtils.defaultIfEmpty(anOrderPayment.getPaymentMethod().getMethod(), StringUtils.EMPTY));
			}*/
			
			reportDataList.add(aReportRow);
		}
	}
	
	private void populateFees(Map<String, Object> aReportRow, OrderFees orderFees, BigDecimal defaultFee) {
		if (orderFees == null) {
			return;
		}
		
		aReportRow.put("dumpsterPrice", CoreUtil.defaultIfNull(orderFees.getDumpsterPrice(), defaultFee));
		aReportRow.put("cityFee", CoreUtil.defaultIfNull(orderFees.getCityFee(), defaultFee));
		aReportRow.put("permitFees", CoreUtil.defaultIfNull(orderFees.getTotalPermitFees(), defaultFee));
		aReportRow.put("overweightFee", CoreUtil.defaultIfNull(orderFees.getOverweightFee(), defaultFee));
		aReportRow.put("additionalFees", CoreUtil.defaultIfNull(orderFees.getTotalAdditionalFees(), defaultFee));
		aReportRow.put("totalFees", CoreUtil.defaultIfNull(orderFees.getTotalFees(), defaultFee));
	}
}
