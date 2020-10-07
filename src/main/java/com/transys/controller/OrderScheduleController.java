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

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.transys.core.util.DateUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.JsonUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.DeliveryPickupReportVO;
import com.transys.model.vo.verizon.MultipleVehicleLocationVO;
import com.transys.model.vo.verizon.VehicleLocationVO;
import com.transys.model.vo.verizon.VehicleVO;

import com.transys.service.map.MapService;
import com.transys.service.verizon.VerizonRevealService;

@Controller
@RequestMapping("/orderScheduler")
public class OrderScheduleController extends BaseController {
	@Autowired
	private VerizonRevealService verizonRevealService;
	
	@Autowired
	private MapService mapService;
	
	public OrderScheduleController() {
		setUrlContext("orderScheduler");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		
		List<DeliveryAddressVO> deliveryOrderAddressList = retrieveDeliveryOrderAddress();
		String deliveryOrderAddressListJson = JsonUtil.toJson(deliveryOrderAddressList);
		model.addAttribute("deliveryOrderAddressList", deliveryOrderAddressListJson);
		
		List<DeliveryAddressVO> pickupOrderAddressList = retrievePickupOrderAddress();
		String pickupOrderAddressListJson = JsonUtil.toJson(pickupOrderAddressList);
		model.addAttribute("pickupOrderAddressList", pickupOrderAddressListJson);
		
		List<VehicleLocationVO> vehicleLocationList = retrieveVehicleLocations();
		String vehicleLocationListJson = JsonUtil.toJson(vehicleLocationList);
		model.addAttribute("vehicleLocationList", vehicleLocationListJson);
		
		return urlContext + "/list";
	}
	
	private List<DeliveryAddressVO> retrieveDeliveryOrderAddress() {
		String todayStr = DateUtil.formatTodayToDbDate();
		String query = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.deliveryDate = '" + todayStr + "'"
				+ " and obj.orderStatus.status = '" + OrderStatus.ORDER_STATUS_OPEN + "' order by id desc";
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		
		List<DeliveryAddressVO> deliveryAddressVOList = new ArrayList<DeliveryAddressVO>();
		String latLng = StringUtils.EMPTY;
		for (Order order : orderList) {
			if (order.getDeliveryAddress() == null) {
				continue;
			}
			
			DeliveryAddressVO aDeliveryAddressVO = new DeliveryAddressVO();
			map(aDeliveryAddressVO, order);
			
			latLng =  mapService.getGeocode(aDeliveryAddressVO.getFullAddress());
			aDeliveryAddressVO.setGeoCode(latLng);
			
			deliveryAddressVOList.add(aDeliveryAddressVO);
		}
		
		return deliveryAddressVOList;
	}
	
	private List<DeliveryAddressVO> retrievePickupOrderAddress() {
		String fromDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(-2);
		String toDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(1); 
		
		String query = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.orderStatus.status = '" + OrderStatus.ORDER_STATUS_PICK_UP + "'"
				+ " and obj.modifiedAt >='" + fromDateStr + "' and obj.modifiedAt <'" + toDateStr + "'"
				+ " order by obj.modifiedAt desc";
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		
		List<DeliveryAddressVO> deliveryAddressVOList = new ArrayList<DeliveryAddressVO>();
		String latLng = StringUtils.EMPTY;
		int maxRows = (orderList.size() <= 50 ? orderList.size() : 50);
		for (Order order : orderList.subList(0, maxRows)) {
			if (order.getDeliveryAddress() == null) {
				continue;
			}
			DeliveryAddressVO aDeliveryAddressVO = new DeliveryAddressVO();
			map(aDeliveryAddressVO, order);
			latLng =  mapService.getGeocode(aDeliveryAddressVO.getFullAddress());
			aDeliveryAddressVO.setGeoCode(latLng);
			deliveryAddressVOList.add(aDeliveryAddressVO);
		}
		return deliveryAddressVOList;
	}
	
	private List<VehicleLocationVO> retrieveVehicleLocations() {
		List<VehicleVO> vehicleVOList = verizonRevealService.getAllVehicles();
		List<String> vehicleNumberList = new ArrayList<String>();
		for (VehicleVO aVehicleVO : vehicleVOList) {
			if (StringUtils.isNotEmpty(aVehicleVO.getVehicleNumber())) {
				vehicleNumberList.add(aVehicleVO.getVehicleNumber());
			}
		}
		
		List<MultipleVehicleLocationVO> multipleVehicleLocationList = verizonRevealService.getVehicleLocation(vehicleNumberList);
		List<VehicleLocationVO> vehicleLocationList = new ArrayList<VehicleLocationVO>();
		for (MultipleVehicleLocationVO aMultipleVehicleLocationVO : multipleVehicleLocationList) {
			VehicleLocationVO vehicleLocationVO = new VehicleLocationVO();
			map(vehicleLocationVO, aMultipleVehicleLocationVO);
			vehicleLocationList.add(vehicleLocationVO);
		}
		return vehicleLocationList;
	}
	
	private void map(DeliveryAddressVO aDeliveryAddressVO, Order order) {
		DeliveryAddress aDeliveryAddress = order.getDeliveryAddress();
		aDeliveryAddressVO.setOrderId(order.getId());
		aDeliveryAddressVO.setDumpsterNum(order.getDumpsterNum());
		aDeliveryAddressVO.setDumpsterSize(order.getDumpsterSizeStr());
		aDeliveryAddressVO.setId(aDeliveryAddress.getId());
		aDeliveryAddressVO.setLine1(aDeliveryAddress.getLine1());
		aDeliveryAddressVO.setLine2(aDeliveryAddress.getLine2());
		aDeliveryAddressVO.setCity(aDeliveryAddress.getCity());
		aDeliveryAddressVO.setState(aDeliveryAddress.getStateStr());
		aDeliveryAddressVO.setZipcode(aDeliveryAddress.getZipcode());
		aDeliveryAddressVO.setFullLine(aDeliveryAddress.getFullLine());
		aDeliveryAddressVO.setFullAddress(aDeliveryAddress.getFullDeliveryAddress());
		aDeliveryAddressVO.setCustomerName(aDeliveryAddress.getCustomerName());
		aDeliveryAddressVO.setDeliveryDate(order.getDeliveryDate());
		aDeliveryAddressVO.setDeliveryDateTimeRange(order.getDeliveryDateTimeFullRange());
	}
	
	private void map(VehicleLocationVO vehicleLocationVO, MultipleVehicleLocationVO multipleVehicleLocationVO) {
		vehicleLocationVO.setVehicleNumber(multipleVehicleLocationVO.getVehicleNumber());
		vehicleLocationVO.setAddressStr(multipleVehicleLocationVO.getAddressStr());
		vehicleLocationVO.setLatitude(multipleVehicleLocationVO.getLatitude());
		vehicleLocationVO.setLongitude(multipleVehicleLocationVO.getLongitude());
		vehicleLocationVO.setDirection(multipleVehicleLocationVO.getDirection());
		vehicleLocationVO.setDisplayState(multipleVehicleLocationVO.getDisplayState());
		vehicleLocationVO.setHeading(multipleVehicleLocationVO.getHeading());
		vehicleLocationVO.setSpeed(multipleVehicleLocationVO.getSpeed());
		vehicleLocationVO.setUpdateUTC(multipleVehicleLocationVO.getUpdateUTC());
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		model.addAttribute("msgCtx", "schedulerSearch");
		model.addAttribute("errorCtx", "schedulerSearch");
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") DeliveryPickupReportVO input) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(250);
		
		DeliveryPickupReportVO inputToBeUsed = input;
		
		// Paging related
		String p = request.getParameter("p");
		if (StringUtils.isEmpty(p)) {
			request.getSession().setAttribute("input", input);
		} else {
			inputToBeUsed = (DeliveryPickupReportVO)request.getSession().getAttribute("input");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Map<String, Object>> reportData = generateReportData(model, criteria, inputToBeUsed, params);
			
			String reportName = "deliveryPickupReport";
			String type = "html";
			setReportRequestHeaders(response, type, reportName);
			//out = dynamicReportService.generateStaticReport(reportName, reportData, 
			//					params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;				
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to create file: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating report: " + t);
			return "report.error";
		} finally {
			criteria.setPage(originalPage);
			criteria.setPageSize(originalPageSize);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(2500);
		
		DeliveryPickupReportVO input = (DeliveryPickupReportVO)request.getSession().getAttribute("input");
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Map<String,Object>> reportData = generateReportData(model, criteria, input, params);
			
			String reportName = "deliveryPickupReport";
			type = setReportRequestHeaders(response, type, reportName);
			//out = dynamicReportService.generateStaticReport(reportName, reportData, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Delivery Pickup report: " + t);
			return "redirect:/" + getUrlContext() + "/main.do";
		} finally {
			criteria.setPage(originalPage);
			criteria.setPageSize(originalPageSize);
			
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

	private List<Order> performSearch(SearchCriteria criteria, DeliveryPickupReportVO input) {
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
	
	private String extractPermitAddress(Order anOrder) {
		List<Permit> permitList = anOrder.getPermits();
		if (permitList == null || permitList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return permitList.get(0).getFullLinePermitAddress1();
	}
	
	private List<Map<String, Object>> generateReportData(ModelMap model, SearchCriteria criteria, 
			DeliveryPickupReportVO input, Map<String, Object> params) {
		List<Order> orderList = performSearch(criteria, input);
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
		
		String deliveryDateFrom = input.getDeliveryDateFrom();
		params.put("deliveryDateFrom", StringUtils.isEmpty(deliveryDateFrom) ? StringUtils.EMPTY : deliveryDateFrom );
		
		String deliveryDateTo = input.getDeliveryDateTo();
		params.put("deliveryDateTo", StringUtils.isEmpty(deliveryDateTo) ? StringUtils.EMPTY : deliveryDateTo );
		
		String pickupDateFrom = input.getPickupDateFrom();
		params.put("pickupDateFrom", StringUtils.isEmpty(pickupDateFrom) ? StringUtils.EMPTY : pickupDateFrom );
		
		String pickupDateTo = input.getPickupDateTo();
		params.put("pickupDateTo", StringUtils.isEmpty(pickupDateTo) ? StringUtils.EMPTY : pickupDateTo );
		
		params.put("dumpsterSizeAggregation", dumpsterSizeAggregation);
		//params.put("noOfOrders", reportData.size());
		
		return reportData;
	}
	
	@ModelAttribute("modelObject")
	public DeliveryPickupReportVO setupModel(HttpServletRequest request) {
		return new DeliveryPickupReportVO();
	}
	
	/*
	private List<String> retrieveDeliveryOrderAddressGeocode() {
		Date today = new Date();
		String todayStr = DateUtil.formatToDbDate(today);
		String deliveryAddressQuery = "select obj.deliveryAddress from Order obj where obj.deleteFlag='1'"
				+ " and obj.deliveryDate = '" + todayStr + "'";
		List<DeliveryAddress> deliveryAddressList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		
		List<String> deliveryAddressGeocodeList = new ArrayList<String>();
		String latLng = StringUtils.EMPTY;
		for (DeliveryAddress aDeliveryAddress : deliveryAddressList) {
			latLng =  mapService.getGeocode(aDeliveryAddress.getFullDeliveryAddress());
			deliveryAddressGeocodeList.add(latLng);
		}
		return deliveryAddressGeocodeList;
	}
	
	private List<String> retrievePickupOrderAddressGeocode() {
		String deliveryAddressQuery = "select obj.deliveryAddress from Order obj where obj.deleteFlag='1'"
				+ " and obj.orderStatus.status = 'Pick Up'";
		List<DeliveryAddress> pickupAddressList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		
		List<String> pickupAddressGeocodeList = new ArrayList<String>();
		String latLng = StringUtils.EMPTY;
		for (DeliveryAddress aPickupAddress : pickupAddressList.subList(0, 10)) {
			latLng =  mapService.getGeocode(aPickupAddress.getFullDeliveryAddress());
			pickupAddressGeocodeList.add(latLng);
		}
		return pickupAddressGeocodeList;
	}
	
	private List<String> retrieveVehicleLocationsGeocode() { 
		List<MultipleVehicleLocationVO> vehicleLocationList = verizonRevealService.getVehicleLocation(Arrays.asList(new String[]{"2", "6", "11", "20", "21", "22", "23", "33", "34", "124"}));
		
		List<String> vehicleLocationGeocodeList = new ArrayList<String>();
		String latLng = StringUtils.EMPTY;
		for (MultipleVehicleLocationVO aMultipleVehicleLocationVO : vehicleLocationList) {
			latLng = aMultipleVehicleLocationVO.getGeocode();
			if (StringUtils.isNotEmpty(latLng)) {
				vehicleLocationGeocodeList.add(latLng);
			}
		}
		return vehicleLocationGeocodeList;
	}*/
}
