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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.DeliveryPickupReportVO;

@Controller
@RequestMapping("/reports/deliveryPickupReport")
public class DeliveryPickupReportController extends ModelReportController<DeliveryPickupReportVO> {
	public DeliveryPickupReportController() {
		setUrlContext("reports/deliveryPickupReport");
		setReportName("deliveryPickupReport");
		setMessageCtx("deliveryPickupReport");
	}
	
	@Override
	protected String getReportFreezeRow() {
		return "7";
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 250;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 2500;
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	}
	
	@Override
	protected List<Map<String, Object>> performSearch(HttpServletRequest request, SearchCriteria criteria, DeliveryPickupReportVO input,
			Map<String, Object> params) {
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		
		List<Order> orderList = performOrderSearch(criteria, input);
		if (orderList == null || orderList.isEmpty()) {
			log.info("No orders matching criteria, report will be empty.");
			return reportData;
		}
		
		for (Order anOrder : orderList) {
			DeliveryAddress deliveryAddress = anOrder.getDeliveryAddress();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", StringUtils.EMPTY + anOrder.getId().toString());
			map.put("customer", StringUtils.EMPTY + anOrder.getCustomer().getCompanyName());
			map.put("deliveryAddress", StringUtils.EMPTY + deliveryAddress.getFullLine());
			map.put("city", StringUtils.EMPTY + deliveryAddress.getCity());
			map.put("permitAddress", extractPermitAddress(anOrder));
			map.put("dumpsterSize", StringUtils.EMPTY + anOrder.getDumpsterSize().getSize());
			map.put("dumpsterNum", (anOrder.getDumpster() == null ? StringUtils.EMPTY : anOrder.getDumpster().getDumpsterNum()));
			map.put("deliveryDate", anOrder.getFormattedDeliveryDate());
			map.put("pickupDate", anOrder.getFormattedPickupDate());
			map.put("isExchange", (anOrder.isExchangeOrder() ? "Exch" : StringUtils.EMPTY));
			map.put("tonnage", (anOrder.getNetWeightTonnage() != null ? anOrder.getNetWeightTonnage().toPlainString() 
					: StringUtils.EMPTY));
			map.put("dropOffDriverName", (anOrder.getDropOffDriver() != null ? anOrder.getDropOffDriver().getName() 
					: StringUtils.EMPTY));
			map.put("dumpsterLocation", (anOrder.getDumpsterLocation() != null ? anOrder.getDumpsterLocation().getLocationType() 
					: StringUtils.EMPTY));
			
			List<Permit> permits = anOrder.getPermits();
			String permitStr = StringUtils.EMPTY;
			for (Permit aPermit : permits) {
				if (StringUtils.isNotEmpty(aPermit.getNumber()) 
						&& !StringUtils.equals(Permit.EMPTY_PERMIT_NUMBER, aPermit.getNumber())) {
					permitStr += (aPermit.getNumber() + ", ");
				}
			}
			if (StringUtils.isNotEmpty(permitStr)) {
				permitStr = permitStr.substring(0, permitStr.length() - 2);
			}
			map.put("permit", permitStr);
			
			//String jsonResponse = JsonUtil.toJson(map);
			//log.info(jsonResponse);
			
			reportData.add(map);
		}
		
		String dumpsterSizeAggregation = deriveDumpsterSizeAggregation(orderList);
		params.put("dumpsterSizeAggregation", dumpsterSizeAggregation);
		
		String deliveryDateFrom = input.getDeliveryDateFrom();
		params.put("deliveryDateFrom", StringUtils.isEmpty(deliveryDateFrom) ? StringUtils.EMPTY : deliveryDateFrom );
		
		String deliveryDateTo = input.getDeliveryDateTo();
		params.put("deliveryDateTo", StringUtils.isEmpty(deliveryDateTo) ? StringUtils.EMPTY : deliveryDateTo );
		
		String pickupDateFrom = input.getPickupDateFrom();
		params.put("pickupDateFrom", StringUtils.isEmpty(pickupDateFrom) ? StringUtils.EMPTY : pickupDateFrom );
		
		String pickupDateTo = input.getPickupDateTo();
		params.put("pickupDateTo", StringUtils.isEmpty(pickupDateTo) ? StringUtils.EMPTY : pickupDateTo );
		
		//params.put("noOfOrders", reportData.size());
		
		return reportData;
	}
	
	private List<Order> performOrderSearch(SearchCriteria criteria, DeliveryPickupReportVO input) {
		String deliveryAddressId = input.getDeliveryAddress();
		String deliveryDateFrom = input.getDeliveryDateFrom();
		String deliveryDateTo = input.getDeliveryDateTo();
		String pickupDateFrom = input.getPickupDateFrom();
		String pickupDateTo = input.getPickupDateTo();
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		whereClause.append(" and obj.orderStatus.id !=" + orderStatus.getId().longValue());
		
		if (StringUtils.isNotEmpty(deliveryAddressId)) {
			whereClause.append(" and obj.deliveryAddress.id=" + deliveryAddressId);
		}
		if (StringUtils.isNotEmpty(deliveryDateFrom)){
        	whereClause.append(" and obj.deliveryDate >='"+FormatUtil.convertInputDateToDbDate(deliveryDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(deliveryDateTo)){
	     	whereClause.append(" and obj.deliveryDate <='"+FormatUtil.convertInputDateToDbDate(deliveryDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(pickupDateFrom)){
        	whereClause.append(" and obj.pickupDate >='"+FormatUtil.convertInputDateToDbDate(pickupDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(pickupDateTo)){
	     	whereClause.append(" and obj.pickupDate <='"+FormatUtil.convertInputDateToDbDate(pickupDateTo)+"'");
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
	
	private String deriveDumpsterSizeAggregation(List<Order> orderList) {
		if (orderList == null || orderList.isEmpty()) {
			log.info("No orders matching criteria, report will be empty.");
			return StringUtils.EMPTY;
		}
		
		StringBuffer selectedOrderIds = new StringBuffer();
		for(Order o : orderList) {
			selectedOrderIds.append(o.getId() + ",");
		}
		List<?> aggregationResults = genericDAO.executeSimpleQuery("select dumpsterSize.size, count(*) from Order p where p.deleteFlag='1' and p.id IN (" + selectedOrderIds.substring(0,selectedOrderIds.lastIndexOf(",")) + ") group by p.dumpsterSize.size");
		List<String> dumpsterSizes = new ArrayList<String>();
		List<String> count = new ArrayList<String>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		int index = 0;
		for (Object o : aggregationResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			jsonResponse = jsonResponse.substring(1, jsonResponse.length()-1);
			
			String [] tokens = jsonResponse.split(",");
			dumpsterSizes.add(index, tokens[0].substring(1, tokens[0].length()-1)); // eliminate quotes
			count.add(index++, tokens[1]);
		}

		StringBuffer aggregationResult = new StringBuffer();
		for (int i = 0; i < dumpsterSizes.size(); i++) {
			aggregationResult.append(dumpsterSizes.get(i) + " : " + count.get(i) + "   ");
		}
		
		return aggregationResult.toString();
	}
	
	private String extractPermitAddress(Order anOrder) {
		List<Permit> permitList = anOrder.getPermits();
		if (permitList == null || permitList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return permitList.get(0).getFullLinePermitAddress1();
	}
}
