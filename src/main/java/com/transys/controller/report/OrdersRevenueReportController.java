package com.transys.controller.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderPayment;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/ordersRevenueReport")
public class OrdersRevenueReportController extends ReportController {
	public OrdersRevenueReportController() {	
		setUrlContext("reports/ordersRevenueReport");
		setReportName("ordersRevenueReport");
		setMessageCtx("ordersRevenueReport");
	}
	
	@Override
	protected String getReportFreezeRow() {
		return "10";
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 1500;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 2500;
	}

	@Override
	protected List<Map<String, Object>> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria,
			Map<String, Object> params) {
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		
		List<Order> orderList = genericDAO.search(Order.class, criteria, "id", null, null);
		if (orderList == null || orderList.isEmpty()) {
			return reportData;
		}
		
		for (Order anOrder : orderList) {
			DeliveryAddress deliveryAddress = anOrder.getDeliveryAddress();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", StringUtils.defaultIfEmpty(anOrder.getId().toString(),StringUtils.EMPTY));
			map.put("customer", StringUtils.defaultIfEmpty(anOrder.getCustomer().getCompanyName(),StringUtils.EMPTY));
			map.put("deliveryAddress", StringUtils.defaultIfEmpty(deliveryAddress.getFullLine(),StringUtils.EMPTY));
			map.put("city", StringUtils.defaultIfEmpty(deliveryAddress.getCity(),StringUtils.EMPTY));
			
			List<OrderPayment> orderPaymentList = anOrder.getOrderPayment();
			if (orderPaymentList != null && !orderPaymentList.isEmpty()) {
				OrderPayment anOrderPayment = orderPaymentList.get(0);
				map.put("paymentMethod", StringUtils.defaultIfEmpty(anOrderPayment.getPaymentMethod().getMethod(), StringUtils.EMPTY));
				map.put("checkNum", StringUtils.defaultIfEmpty(anOrderPayment.getCheckNum(),StringUtils.EMPTY));
				map.put("ccReferenceNum", StringUtils.defaultIfEmpty(anOrderPayment.getCcReferenceNum(),StringUtils.EMPTY));
			}
			
			OrderFees anOrderFees = anOrder.getOrderFees();
			if (anOrderFees != null) {
				map.put("dumpsterPrice", anOrderFees.getDumpsterPrice());
				map.put("cityFee", anOrderFees.getCityFee()); 
				map.put("totalPermitFees", anOrderFees.getTotalPermitFees());
				map.put("overweightFee", anOrderFees.getOverweightFee());
				map.put("totalAdditionalFee", anOrderFees.getTotalAdditionalFees());
				map.put("totalFees", anOrderFees.getTotalFees());
				map.put("totalPaid", anOrder.getTotalAmountPaid());
				map.put("feeBalance", anOrder.getBalanceAmountDue());
			}
			
			//System.out.println(new Gson().toJson(map));
			//String jsonResponse = JsonUtil.toJson(map);
			//log.info(jsonResponse);
			
			reportData.add(map);
		}
		
		params.put("orderDateFrom",(criteria.getSearchMap().get("createdAtFrom") == null ? StringUtils.EMPTY : criteria.getSearchMap().get("createdAtFrom")));
		params.put("orderDateTo", (criteria.getSearchMap().get("createdAtTo") == null ? StringUtils.EMPTY : criteria.getSearchMap().get("createdAtTo")));
		
		return reportData;
	}
}
