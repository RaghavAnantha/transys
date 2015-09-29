package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
import com.transys.core.util.MimeUtil;
import com.transys.model.Address;
import com.transys.model.Order;
import com.transys.model.OrderPaymentInfo;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/ordersRevenueReport")
public class OrdersRevenueReportController extends CRUDController<Order> {

	public OrdersRevenueReportController(){	
		setUrlContext("ordersRevenueReport");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<Order> orderList = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		model.addAttribute("ordersList",orderList);
		
		String[] aggregationValues = getOrdersRevenueData(orderList);
		
		model.addAttribute("orderDateFrom", criteria.getSearchMap().get("createdAtFrom"));
		model.addAttribute("orderDateTo", criteria.getSearchMap().get("createdAtTo"));

		model.addAttribute("totalDumpsterPrice", "$" + aggregationValues[0]);
		model.addAttribute("totalPermitFees", "$" + aggregationValues[1]);
		model.addAttribute("totalCityFees", "$" + aggregationValues[2]);
		model.addAttribute("totalOverweightFees", "$" + aggregationValues[3]);
		model.addAttribute("aggregatedTotalFees", "$" + aggregationValues[4]);
	}

	private String[] getOrdersRevenueData(List<Order> orderList) {
		StringBuffer selectedOrderIDs = new StringBuffer();
		if (orderList == null && orderList.size() == 0) {
			// throw Exception??
			System.out.println("No orders matching criteria, report will be empty.");
		}
		
		for(Order o : orderList) {
			selectedOrderIDs.append(o.getId() + ",");
		}
		List<?> aggregationResults = genericDAO.executeSimpleQuery("select SUM(p.dumpsterPrice) as totalDumpsterPrice, SUM(p.permitFees) as totalPermitFees, SUM(p.cityFee) as totalCityFees, SUM(p.overweightFee) as totalOverweightFees, SUM(p.totalFees) as totalFees from OrderPaymentInfo p where p.order IN (" + selectedOrderIDs.substring(0,selectedOrderIDs.lastIndexOf(",")) + ")");
		//String jSonResponse =new Gson().toJson(aggregationResults.get(0));
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = StringUtils.EMPTY;
		try {
			jsonResponse = objectMapper.writeValueAsString(aggregationResults.get(0));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonResponse = jsonResponse.substring(1, jsonResponse.length()-1);
		String[] aggregationValues = jsonResponse.split(",");

		return aggregationValues;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateOrdersRevenueReport.do")
	public void export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type, Object objectDAO, Class clazz) {

		try {

			List<Map<String,Object>> reportData = prepareReportData(request);

			type = setRequestHeaders(response, type);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = new HashMap<String, Object>();

			if (!type.equals("print") && !type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("ordersRevenueReport", reportData, params, type, request);
			} else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("ordersRevenueReport", reportData, params, type, request);
			} else {
				out = dynamicReportService.generateStaticReport("ordersRevenueReport" + "print", reportData, params, type,
						request);
			}

			out.writeTo(response.getOutputStream());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());

		}
	}

	private String setRequestHeaders(HttpServletResponse response, String type) {
		if (StringUtils.isEmpty(type))
			type = "xlsx";
		if (!type.equals("html") && !(type.equals("print"))) {
			response.setHeader("Content-Disposition", "attachment;filename= dumpsterOnsiteReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		return type;
	}

	private List<Map<String, Object>> prepareReportData(HttpServletRequest request) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");

		List<Order> orderList = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		String[] aggregationValues = getOrdersRevenueData(orderList);
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Order anOrder : orderList) {
			
			Address deliveryAddress = anOrder.getDeliveryAddress();
			OrderPaymentInfo orderPaymentInfo = anOrder.getOrderPaymentInfo();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", StringUtils.EMPTY + anOrder.getId().toString());
			map.put("customer", StringUtils.EMPTY + anOrder.getCustomer().getCompanyName());
			map.put("deliveryAddress", StringUtils.EMPTY + deliveryAddress.getFullLine());
			map.put("city", StringUtils.EMPTY + deliveryAddress.getCity());
			
			if (orderPaymentInfo != null) {
				map.put("paymentMethod", StringUtils.EMPTY + orderPaymentInfo.getPaymentMethod());
				map.put("checkNum", StringUtils.EMPTY + orderPaymentInfo.getCheckNum());
				map.put("ccReferenceNum", StringUtils.EMPTY + orderPaymentInfo.getCcReferenceNum());
				map.put("dumpsterPrice", StringUtils.EMPTY + orderPaymentInfo.getDumpsterPrice());
				map.put("cityFee", StringUtils.EMPTY + orderPaymentInfo.getCityFee());
				map.put("permitFees", StringUtils.EMPTY + orderPaymentInfo.getPermitFees());
				map.put("overweightFee", StringUtils.EMPTY + orderPaymentInfo.getOverweightFee());
				map.put("additionalFee", StringUtils.EMPTY + orderPaymentInfo.getAdditionalFee1());
				map.put("totalFees", StringUtils.EMPTY + orderPaymentInfo.getTotalFees());
			}
			
			map.put("orderDateFrom", "" + criteria.getSearchMap().get("createdAtFrom"));
			map.put("orderDateTo", "" + criteria.getSearchMap().get("createdAtTo"));
			
			map.put("totalDumpsterPrice", "$" + aggregationValues[0]);
			map.put("totalPermitFees", "$" + aggregationValues[1]);
			map.put("totalCityFees", "$" + aggregationValues[2]);
			map.put("totalOverweightFees", "$" + aggregationValues[3]);
			map.put("aggregateTotalFees", "$" + aggregationValues[4]);
			
			//System.out.println(new Gson().toJson(map));
			ObjectMapper objectMapper = new ObjectMapper();
			String jSonResponse = StringUtils.EMPTY;
			try {
				jSonResponse = objectMapper.writeValueAsString(map);
				System.out.println(jSonResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			reportData.add(map);
		}
		
		return reportData;
	}
}
