package com.transys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.core.util.DateUtil;
import com.transys.core.util.FormatUtil;
import com.transys.core.util.JsonUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.SearchCriteria;
import com.transys.model.Vehicle;
import com.transys.model.map.Geocode;

import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.verizon.MultipleVehicleLocationVO;
import com.transys.model.vo.verizon.VehicleLocationVO;
import com.transys.model.vo.verizon.VehicleVO;

import com.transys.service.map.MapService;
import com.transys.service.verizon.VerizonRevealService;

@Controller
@RequestMapping("/orderScheduler")
public class OrderScheduleController extends BaseController {
	protected static int MAX_DISPLAY_ORDERS = 40;
	protected static int MAX_DISPLAY_VEHICLES = 30;
	
	@Autowired
	private VerizonRevealService verizonRevealService;
	
	@Autowired
	private MapService mapService;
	
	public OrderScheduleController(){	
		setUrlContext("orderScheduler");
		setMessageCtx("orderSchedulerSearch");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		resetSearchCriteria(request);
		
		return performSearch(model, request);
	}
	
	protected void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		addCtx(model);
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return performSearch(model, request);
	}
	
	private String performSearch(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria searchCriteria = getSearchCriteria(request);
		
		List<DeliveryAddressVO> deliveryOrderAddressList = retrieveDeliveryOrderAddress(searchCriteria, request);
		String deliveryOrderAddressListJson = JsonUtil.toJson(deliveryOrderAddressList);
		model.addAttribute("deliveryOrderAddressList", deliveryOrderAddressListJson);
		
		List<DeliveryAddressVO> pickupOrderAddressList = retrievePickupOrderAddress(searchCriteria, request);
		String pickupOrderAddressListJson = JsonUtil.toJson(pickupOrderAddressList);
		model.addAttribute("pickupOrderAddressList", pickupOrderAddressListJson);
		
		List<VehicleLocationVO> vehicleLocationList = retrieveVehicleLocations(request);
		String vehicleLocationListJson = JsonUtil.toJson(vehicleLocationList);
		model.addAttribute("vehicleLocationList", vehicleLocationListJson);
		
		return urlContext + "/list";
	}
	
	private List<DeliveryAddressVO> retrieveDeliveryOrderAddress(SearchCriteria criteria, HttpServletRequest request) {
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
		
		List<DeliveryAddressVO> deliveryAddressVOList = retrieveOrderAddress(query, request);
		return deliveryAddressVOList;
	}
	
	private List<DeliveryAddressVO> retrievePickupOrderAddress(SearchCriteria criteria, HttpServletRequest request) {
		String fromDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(-5);
		String toDateStr = DateUtil.addDaysToTodayAndFormatToDbDate(1);
		String query = "select obj from Order obj where obj.deleteFlag='1'"
				+ " and obj.orderStatus.status = '" + OrderStatus.ORDER_STATUS_PICK_UP + "'"
				+ " and obj.modifiedAt >='" + fromDateStr + "' and obj.modifiedAt <'" + toDateStr + "'"
				+ " order by obj.modifiedAt desc";
		
		List<DeliveryAddressVO> deliveryAddressVOList = retrieveOrderAddress(query, request);
		return deliveryAddressVOList;
	}
	
	private List<DeliveryAddressVO> retrieveOrderAddress(String query, HttpServletRequest request) {
		List<DeliveryAddressVO> deliveryAddressVOList = new ArrayList<DeliveryAddressVO>();
		List<Order> orderList = genericDAO.executeSimpleQuery(query);
		if (orderList == null || orderList.isEmpty()) {
			return deliveryAddressVOList;
		}
		
		int maxRows = (orderList.size() <= MAX_DISPLAY_ORDERS ? orderList.size() : MAX_DISPLAY_ORDERS);
		for (Order order : orderList.subList(0, maxRows)) {
			if (order.getDeliveryAddress() == null) {
				continue;
			}
			
			DeliveryAddressVO aDeliveryAddressVO = new DeliveryAddressVO();
			map(aDeliveryAddressVO, order);
			
			Geocode geocode =  mapService.retrieveGeocode(aDeliveryAddressVO.getFullAddress(), request);
			aDeliveryAddressVO.setGeoCode(geocode.getLatLng());
			
			deliveryAddressVOList.add(aDeliveryAddressVO);
		}
		return deliveryAddressVOList;
	}
	
	private void map(Vehicle vehicle, VehicleVO vehicleVO) {
		vehicle.setNumber(vehicleVO.getVehicleNumber());
		vehicle.setName(vehicleVO.getName());
		vehicle.setVin(vehicleVO.getVIN());
		vehicle.setRegistrationNumber(vehicleVO.getRegistrationNumber());
		vehicle.setYear(vehicleVO.getYear());
		vehicle.setMake(vehicleVO.getMake());
		vehicle.setModel(vehicleVO.getModel());
		vehicle.setSize(vehicleVO.getVehicleSize());
	}
	
	private void updateVehicle(List<VehicleVO> vehicleVOList, HttpServletRequest request) {
		if (vehicleVOList == null || vehicleVOList.isEmpty()) {
			return;
		}
		
		List<String> vehicleNumberList = new ArrayList<String>();
		List<Vehicle> vehicleList = genericDAO.findAll(Vehicle.class);
		for (Vehicle aVehicle : vehicleList) {
			if (StringUtils.isNotEmpty(aVehicle.getNumber())) {
				vehicleNumberList.add(aVehicle.getNumber());
			}
		}
		
		for (VehicleVO aVehicleVO : vehicleVOList) {
			if (vehicleNumberList.contains(aVehicleVO.getVehicleNumber())) {
				continue;
			}
			
			Vehicle vehicle = new Vehicle();
			map(vehicle, aVehicleVO);
			setModifier(request, vehicle);
			genericDAO.save(vehicle);
		}
	}
	
	private List<VehicleLocationVO> retrieveVehicleLocations(HttpServletRequest request) {
		List<VehicleLocationVO> vehicleLocationList = new ArrayList<VehicleLocationVO>();
		List<VehicleVO> vehicleVOList = verizonRevealService.getAllVehicles();
		if (vehicleVOList == null || vehicleVOList.isEmpty()) {
			return vehicleLocationList;
		}
		
		updateVehicle(vehicleVOList, request);
		
		List<String> vehicleNumberList = new ArrayList<String>();
		int maxRows = (vehicleVOList.size() <= MAX_DISPLAY_VEHICLES ? vehicleVOList.size() : MAX_DISPLAY_VEHICLES);
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
}
