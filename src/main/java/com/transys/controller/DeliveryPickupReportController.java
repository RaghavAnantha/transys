package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.core.util.MimeUtil;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/deliveryPickupReport")
public class DeliveryPickupReportController extends CRUDController<Order> {
	public DeliveryPickupReportController(){	
		setUrlContext("reports/deliveryPickupReport");
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
		
		List<Order> orderList = performSearch(criteria);
		model.addAttribute("ordersList", orderList);
		model.addAttribute("dumpsterSizeAggregation", setDumpsterSizeAggregation(model, orderList));
		
		String addrssQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' order by obj.line1 asc";
		List<DeliveryAddress> addresses = genericDAO.executeSimpleQuery(addrssQuery);
		model.addAttribute("deliveryAddresses", addresses);
		model.addAttribute("deliveryDateFrom", criteria.getSearchMap().get("deliveryDateFrom"));
		model.addAttribute("deliveryDateTo", criteria.getSearchMap().get("deliveryDateTo"));
		model.addAttribute("pickupDateFrom", criteria.getSearchMap().get("pickDateFrom"));
		model.addAttribute("pickDateTo", criteria.getSearchMap().get("pickDateTo"));	
	}
	
	private String setDumpsterSizeAggregation(ModelMap model, List<Order> orderList) {
		StringBuffer selectedOrderIds = new StringBuffer();
		if (orderList == null || orderList.size() == 0) {
			// throw Exception??
			System.out.println("No orders matching criteria, report will be empty.");
		}
		
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
				// TODO Auto-generated catch block
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateDeliveryPickupReport.do")
	public void export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type, Object objectDAO, Class clazz) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(100000);
		//criteria.setPage(0);
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Map<String,Object>> reportData = prepareReportData(model, request);
			params.put("noOfOrders", reportData.size());

			type = setRequestHeaders(response, type, "DeliveryPickupReport");
			out = dynamicReportService.generateStaticReport("deliveryPickupReport", reportData, params, type, request);
			out.writeTo(response.getOutputStream());
			
			criteria.setPageSize(25);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private List<Order> performSearch(SearchCriteria criteria) {
		criteria.getSearchMap().put("orderStatus.id", "!=5");
		List<Order> orderList = genericDAO.search(getEntityClass(), criteria, "id", null, null);
		criteria.getSearchMap().remove("orderStatus.id");
		
		return orderList;
	}
	
	private String extractPermitAddress(Order anOrder) {
		List<Permit> permitList = anOrder.getPermits();
		if (permitList == null || permitList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return permitList.get(0).getFullLinePermitAddress1();
	}
	
	private List<Map<String, Object>> prepareReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		
		List<Order> orderList = performSearch(criteria);
		String dumpsterSizeAggregation = setDumpsterSizeAggregation(model, orderList);
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
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
			
			Object deliveryDateFrom = criteria.getSearchMap().get("deliveryDateFrom");
			map.put("deliveryDateFrom", deliveryDateFrom == null ? StringUtils.EMPTY : deliveryDateFrom );
			
			Object deliveryDateTo = criteria.getSearchMap().get("deliveryDateTo");
			map.put("deliveryDateTo", deliveryDateTo == null ? StringUtils.EMPTY : deliveryDateTo );
			
			Object pickupDateFrom = criteria.getSearchMap().get("pickupDateFrom");
			map.put("pickupDateFrom", pickupDateFrom == null ? StringUtils.EMPTY : pickupDateFrom );
			
			Object pickupDateTo = criteria.getSearchMap().get("pickupDateTo");
			map.put("pickupDateTo", pickupDateTo == null ? StringUtils.EMPTY : pickupDateTo );
			
			map.put("dumpsterSizeAggregation", dumpsterSizeAggregation);
			
			/*ObjectMapper objectMapper = new ObjectMapper();
			String jSonResponse = StringUtils.EMPTY;
			try {
				jSonResponse = objectMapper.writeValueAsString(map);
				System.out.println(jSonResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			reportData.add(map);
		}
		
		return reportData;
	}
	
}
