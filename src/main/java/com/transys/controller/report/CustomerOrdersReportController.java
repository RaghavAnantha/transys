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

import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;
import com.transys.core.util.ReportUtil;
import com.transys.model.Customer;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.CustomerReportVO;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.OrderReportVO;

@Controller
@RequestMapping("/reports/customer/customerOrdersReport")
public class CustomerOrdersReportController extends ReportController {
	public CustomerOrdersReportController(){	
		setUrlContext("reports/customer/customerOrdersReport");
		setMessageCtx("customerOrdersReport");
		
		setReportName("customerOrdersReportMaster");
		setSubReportName("customerOrdersReportSub");
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
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		String[][] strArrOfArr = ModelUtil.extractContactDetails(customerVOList);
		String[] phoneArr = (String[])strArrOfArr[0];
		String[] contactNameArr = (String[])strArrOfArr[1];
		
		model.addAttribute("phones", phoneArr);
		model.addAttribute("contactNames", contactNameArr);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
	}
	
	@Override
	protected List<CustomerReportVO> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria, Map<String, Object> params) {
		List<CustomerReportVO> customerReportVOList = retrieveCustomerOrdersReportData(criteria, params);
		if (customerReportVOList.isEmpty()) {
			return customerReportVOList;
		}
		
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
			if (!customerReportVOList.isEmpty() && customerReportVOList.get(0).getOrderList() != null
					&& !customerReportVOList.get(0).getOrderList().isEmpty()) {
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
	
	private List<CustomerReportVO> retrieveCustomerOrdersReportData(SearchCriteria criteria, Map<String, Object> params) {
		Map<String, Object> searchMap = (Map<String, Object>)criteria.getSearchMap();
		String reportTypeKey = "customerOrderReportType";
		String reportType = (String)searchMap.get(reportTypeKey);
		searchMap.remove(reportTypeKey);
		List<CustomerReportVO> customerReportVOList;
		if (StringUtils.equals("TOTALS", reportType)) {
			customerReportVOList = processCustomerOrderTotalsReport(criteria, params);
		} else {
			customerReportVOList = processCustomerOrderDetailReport(criteria, params);
		}
		searchMap.put(reportTypeKey, reportType);
		
		return customerReportVOList;	
	}
	
	private List<CustomerReportVO> processCustomerOrderDetailReport(SearchCriteria criteria, Map<String, Object> params) {
		params.put(ReportUtil.reportNameKey, "customerOrdersReportMaster");
		params.put(ReportUtil.subReportNameKey, "customerOrdersReportSub");
		params.put(ReportUtil.subReportIndicatorKey, true);
		
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
			CustomerReportVO aCustomerReportVO =  new CustomerReportVO();
			map(customerOrderMap.get(customerKey), aCustomerReportVO);

         customerReportVOList.add(aCustomerReportVO);
		}
      
      return customerReportVOList;
	}
	
	private List<CustomerReportVO> processCustomerOrderTotalsReport(SearchCriteria criteria, Map<String, Object> params) {
		params.put(ReportUtil.reportNameKey, "customerOrderTotalsReport");
		params.put(ReportUtil.subReportNameKey, StringUtils.EMPTY);
		params.put(ReportUtil.subReportIndicatorKey, false);
		
		Map<String, Object> searchMap = (Map<String, Object>)criteria.getSearchMap();
		String createdFrom = (String)searchMap.get("createdAtFrom");
		String createdTo = (String)searchMap.get("createdAtTo");
		String customer = (String)searchMap.get("customer.id");
		
		StringBuffer query = new StringBuffer("select obj.customer.companyName as companyName, count(*) as orderCount from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		whereClause.append(" and obj.orderStatus.id !=" + orderStatus.getId().longValue());
		
		if (StringUtils.isNotEmpty(createdFrom)){
        	whereClause.append(" and obj.createdAt >='"+FormatUtil.formatInputDateToDbDate(createdFrom)+"'");
		}
		if (StringUtils.isNotEmpty(createdTo)){
        	whereClause.append(" and obj.createdAt <='"+FormatUtil.formatInputDateToDbDate(createdTo)+"'");
		}
		if (StringUtils.isNotEmpty(customer)){
			whereClause.append(" and obj.customer.id=" + customer);
		}
      
      query.append(whereClause);

      query.append(" group by obj.customer.companyName");
      query.append(" order by orderCount desc, companyName asc");

		List<?> orderList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.getResultList();
		
		List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>();
		if (orderList.isEmpty()) {
			return customerReportVOList;
		}
		
		for (int i = 0; i < orderList.size(); i++) {
			Object[] objArr = (Object[])orderList.get(i);
			CustomerReportVO aCustomerReportVO =  new CustomerReportVO();
			aCustomerReportVO.setCompanyName((String)objArr[0]);
			aCustomerReportVO.setTotalOrders(((Long)objArr[1]).intValue());
         customerReportVOList.add(aCustomerReportVO);
		}
      
      return customerReportVOList;
	}
	
	private void map(List<Order> orderList, CustomerReportVO aCustomerReportVO) {
		Customer aCustomer = orderList.get(0).getCustomer();
		ModelUtil.map(aCustomerReportVO, aCustomer);
		
		BigDecimal totalOrderAmount = new BigDecimal("0.00");
		List<OrderReportVO> anOrderReportVOList = new ArrayList<OrderReportVO>();
		for (Order anOrder : orderList) {
			OrderReportVO anOrderReportVO = new OrderReportVO();
			ModelUtil.map(anOrderReportVO, anOrder);
			anOrderReportVOList.add(anOrderReportVO);
			
			totalOrderAmount = totalOrderAmount.add(anOrderReportVO.getTotalFees());
		}
		
		aCustomerReportVO.setTotalOrderAmount(totalOrderAmount);
      aCustomerReportVO.setTotalOrders(orderList.size());
		
		aCustomerReportVO.setOrderList(anOrderReportVOList);
	}
}
