package com.transys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import com.transys.controller.report.ReportController;

import com.transys.core.util.DateUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.JsonUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.verizon.MultipleVehicleLocationVO;
import com.transys.model.vo.verizon.VehicleLocationVO;
import com.transys.model.vo.verizon.VehicleVO;

import com.transys.service.map.MapService;
import com.transys.service.verizon.VerizonRevealService;

@Controller
@RequestMapping("/orderScheduler")
public class OrderScheduleController extends ReportController {
	protected static int MAX_DELIVERY_ORDERS = 3;
	protected static int MAX_PICKUP_ORDERS = 3;
	protected static int MAX_VEHICLES = 5;
	
	@Autowired
	private VerizonRevealService verizonRevealService;
	
	@Autowired
	private MapService mapService;
	
	public OrderScheduleController(){	
		setUrlContext("orderScheduler");
		setReportName(StringUtils.EMPTY);
		setMessageCtx("orderSchedulerSearch");
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	}
	
	@Override
	protected void processDisplayMain(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {
		performSearch(model, request, null);
	}
	
	@Override
	protected List<?> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria, Map<String, Object> params) {
		performSearch(model, request, criteria);
		return null;
	}
	
	private void performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria) {
		List<DeliveryAddressVO> deliveryOrderAddressList = retrieveDeliveryOrderAddress(criteria);
		String deliveryOrderAddressListJson = JsonUtil.toJson(deliveryOrderAddressList);
		model.addAttribute("deliveryOrderAddressList", deliveryOrderAddressListJson);
		
		List<DeliveryAddressVO> pickupOrderAddressList = retrievePickupOrderAddress(criteria);
		String pickupOrderAddressListJson = JsonUtil.toJson(pickupOrderAddressList);
		model.addAttribute("pickupOrderAddressList", pickupOrderAddressListJson);
		
		List<VehicleLocationVO> vehicleLocationList = retrieveVehicleLocations();
		String vehicleLocationListJson = JsonUtil.toJson(vehicleLocationList);
		model.addAttribute("vehicleLocationList", vehicleLocationListJson);
	}
	
	private List<DeliveryAddressVO> retrieveDeliveryOrderAddress(SearchCriteria criteria) {
		String deliveryDateFrom = StringUtils.EMPTY;
		String deliveryDateTo = StringUtils.EMPTY;
		String deliveryAddress = StringUtils.EMPTY;
		if (criteria != null) {
			Map<String, Object> criteriaMap = criteria.getSearchMap();
			deliveryDateFrom = (String) criteriaMap.get("deliveryDateFrom");
			deliveryDateTo = (String) criteriaMap.get("deliveryDateTo");
			deliveryAddress = (String) criteriaMap.get("deliveryAddress");
		}
		
		String query = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.orderStatus.status = '" + OrderStatus.ORDER_STATUS_OPEN + "'";
		
		if (StringUtils.isNotEmpty(deliveryDateFrom) && StringUtils.isNotEmpty(deliveryDateTo)) {
			//query +=	(" and obj.deliveryDate >='" + FormatUtil.formatInputDateToDbDate(deliveryDateFrom) + "' and obj.deliveryDate <='" + FormatUtil.formatInputDateToDbDate(deliveryDateTo) + "'");
			query +=	(" and obj.deliveryDate between '" + FormatUtil.formatInputDateToDbDate(deliveryDateFrom) + "' and '" + FormatUtil.formatInputDateToDbDate(deliveryDateTo) + "'");
		} else {
			String todayStr = DateUtil.formatTodayToDbDate();
			query +=	(" and obj.deliveryDate = '" + todayStr + "'");
		}
		
		if (StringUtils.isNotEmpty(deliveryAddress)) {
			query +=	(" and obj.deliveryAddress = " + deliveryAddress);
		}
		
		query +=	" order by id desc";
		
		List<DeliveryAddressVO> deliveryAddressVOList = retrieveOrderAddress(query);
		return deliveryAddressVOList;
	}
	
	private List<DeliveryAddressVO> retrievePickupOrderAddress(SearchCriteria criteria) {
		//String fromDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(-2);
		//String toDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(1);
		//+ " and obj.modifiedAt >='" + fromDateStr + "' and obj.modifiedAt <'" + toDateStr + "'"
		
		String query = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.orderStatus.status = '" + OrderStatus.ORDER_STATUS_PICK_UP + "'"
				+ " order by obj.modifiedAt desc";
		
		List<DeliveryAddressVO> deliveryAddressVOList = retrieveOrderAddress(query);
		return deliveryAddressVOList;
	}
	
	private List<DeliveryAddressVO> retrieveOrderAddress(String query) {
		List<DeliveryAddressVO> deliveryAddressVOList = new ArrayList<DeliveryAddressVO>();
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		if (orderList == null || orderList.isEmpty()) {
			return deliveryAddressVOList;
		}
		
		String latLng = StringUtils.EMPTY;
		int maxRows = (orderList.size() <= MAX_PICKUP_ORDERS ? orderList.size() : MAX_PICKUP_ORDERS);
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
		List<VehicleLocationVO> vehicleLocationList = new ArrayList<VehicleLocationVO>();
		List<VehicleVO> vehicleVOList = verizonRevealService.getAllVehicles();
		if (vehicleVOList == null || vehicleVOList.isEmpty()) {
			return vehicleLocationList;
		}
		
		List<String> vehicleNumberList = new ArrayList<String>();
		int maxRows = (vehicleVOList.size() <= MAX_VEHICLES ? vehicleVOList.size() : MAX_VEHICLES);
		for (VehicleVO aVehicleVO : vehicleVOList.subList(0, maxRows)) {
			if (StringUtils.isNotEmpty(aVehicleVO.getVehicleNumber())) {
				vehicleNumberList.add(aVehicleVO.getVehicleNumber());
			}
		}
		
		List<MultipleVehicleLocationVO> multipleVehicleLocationList = verizonRevealService.getVehicleLocation(vehicleNumberList);
		if (multipleVehicleLocationList == null || multipleVehicleLocationList.isEmpty()) {
			return vehicleLocationList;
		}
		
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
