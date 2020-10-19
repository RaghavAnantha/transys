package com.transys.service.verizon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.transys.core.util.HttpUtil;

import com.transys.model.vo.verizon.DriverVO;
import com.transys.model.vo.verizon.MultipleVehicleLocationVO;
import com.transys.model.vo.verizon.VehicleLocationVO;
import com.transys.model.vo.verizon.VehicleStatusVO;
import com.transys.model.vo.verizon.VehicleVO;
import com.transys.model.vo.verizon.WorkOrderStatusVO;
import com.transys.model.vo.verizon.WorkOrderTypeVO;
import com.transys.model.vo.verizon.WorkOrderVO;

public class VerizonRevealServiceImpl implements VerizonRevealService {
	protected static Logger log = LogManager.getLogger("com.transys.service.verizon");
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String getToken() {
		return TokenGenerator.getToken(restTemplate);
	}
	
	@Override
	public VehicleVO getVehicle(String vehicleNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		VehicleVO vehicleVO = null;
		ResponseEntity<VehicleVO> response = restTemplate.exchange(VerizonConfigConstants.getVehicleUri, 
				HttpMethod.GET, httpRequestEntity, VehicleVO.class, vehicleNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleVO = response.getBody();
		}
		
		return vehicleVO;
	}
	
	@Override
	public List<VehicleVO> getAllVehicles() {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		List<VehicleVO> vehicleList = Collections.emptyList();
		ResponseEntity<VehicleVO[]> response = restTemplate.exchange(VerizonConfigConstants.getAllVehiclesUri, 
				HttpMethod.GET, httpRequestEntity, VehicleVO[].class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleList = Arrays.asList(response.getBody());
		}
		return vehicleList;
	}
	
	@Override
	public void updateVehicle(VehicleVO vehicleVO) {
		HttpEntity<VehicleVO> httpRequestEntity = (HttpEntity<VehicleVO>)buildUpdateHttpRequestEntity(vehicleVO);
		
		ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.updateVehicleUri, 
				HttpMethod.PUT, httpRequestEntity, String.class, vehicleVO.VehicleNumber);
		//if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
		if (response.getStatusCode() == HttpStatus.OK) {
			
		}
	}
	
	@Override
	public VehicleLocationVO getVehicleLocation(String vehicleNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		VehicleLocationVO vehicleLocation = null;
		ResponseEntity<VehicleLocationVO> response = restTemplate.exchange(VerizonConfigConstants.getVehicleLocationUri, 
				HttpMethod.GET, httpRequestEntity, VehicleLocationVO.class, vehicleNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleLocation = response.getBody();
		}
		
		return vehicleLocation;
	}
	
	@Override
	public List<MultipleVehicleLocationVO> getVehicleLocation(List<String> vehicleNumList) {
		String[] vehicleNumArr = vehicleNumList.toArray(new String[vehicleNumList.size()]);
		HttpEntity<String[]> httpRequestEntity = (HttpEntity<String[]>)buildUpdateHttpRequestEntity(vehicleNumArr);
		
		List<MultipleVehicleLocationVO> vehicleLocationList = Collections.emptyList();;
		ResponseEntity<MultipleVehicleLocationVO[]> response = restTemplate.exchange(VerizonConfigConstants.getMultipleVehicleLocationsUri, 
				HttpMethod.POST, httpRequestEntity, MultipleVehicleLocationVO[].class);
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleLocationList = Arrays.asList(response.getBody());
		}
		
		return vehicleLocationList;
	}
	
	@Override
	public VehicleStatusVO getVehicleStatus(String vehicleNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		VehicleStatusVO vehicleStatus = null;
		ResponseEntity<VehicleStatusVO> response = restTemplate.exchange(VerizonConfigConstants.getVehicleStatusUri, 
				HttpMethod.GET, httpRequestEntity, VehicleStatusVO.class, vehicleNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleStatus = response.getBody();
		}
		
		return vehicleStatus;
	}
	
	@Override
	public List<VehicleStatusVO> getVehicleStatus(List<String> vehicleNumList) {
		String[] vehicleNumArr = vehicleNumList.toArray(new String[vehicleNumList.size()]);
		HttpEntity<String[]> httpRequestEntity = (HttpEntity<String[]>)buildUpdateHttpRequestEntity(vehicleNumArr);
		
		List<VehicleStatusVO> vehicleStatusList = Collections.emptyList();;
		ResponseEntity<VehicleStatusVO[]> response = restTemplate.exchange(VerizonConfigConstants.getMultipleVehicleStatusesUri, 
				HttpMethod.POST, httpRequestEntity, VehicleStatusVO[].class);
		if (response.getStatusCode().is2xxSuccessful()) {
			vehicleStatusList = Arrays.asList(response.getBody());
		}
		
		return vehicleStatusList;
	}
	
	@Override
	public DriverVO getDriver(String driverNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		DriverVO driverVO = null;
		ResponseEntity<DriverVO> response = restTemplate.exchange(VerizonConfigConstants.getDriverUri, 
				HttpMethod.GET, httpRequestEntity, DriverVO.class, driverNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			driverVO = response.getBody();
		}
		
		return driverVO;
	}
	
	@Override
	public List<DriverVO> getAllDrivers() {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		List<DriverVO> driverVOList = Collections.emptyList();
		ResponseEntity<DriverVO[]> response = restTemplate.exchange(VerizonConfigConstants.getAllDriversUri, 
				HttpMethod.GET, httpRequestEntity, DriverVO[].class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			driverVOList = Arrays.asList(response.getBody());
		}
		return driverVOList;
	}
	
	@Override
	public DriverVO createDriver(DriverVO driverVO) {
		HttpEntity<DriverVO> httpRequestEntity = (HttpEntity<DriverVO>)buildUpdateHttpRequestEntity(driverVO);
		
		DriverVO driverVOCreated = null;
		ResponseEntity<DriverVO> response = restTemplate.exchange(VerizonConfigConstants.createDriverUri, 
				HttpMethod.POST, httpRequestEntity, DriverVO.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			driverVOCreated = response.getBody();
		}
		
		return driverVOCreated;
	}
	
	@Override
	public void updateDriver(DriverVO driverVO) {
		HttpEntity<DriverVO> httpRequestEntity = (HttpEntity<DriverVO>) buildUpdateHttpRequestEntity(driverVO);
		
		ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.updateDriverUri, 
				HttpMethod.PUT, httpRequestEntity, String.class, driverVO.DriverNumber);
		//if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
		if (response.getStatusCode() == HttpStatus.OK) {
			
		}
	}
	
	@Override
	public void deleteDriver(String driverNum) {
		HttpEntity<String> httpRequestEntity = (HttpEntity<String>)buildUpdateHttpRequestEntity(StringUtils.EMPTY);
		
		ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.deleteDriverUri, 
				HttpMethod.DELETE, httpRequestEntity, String.class, driverNum);
		//if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
		if (response.getStatusCode() == HttpStatus.OK) {
			
		}
	}
	
	@Override
	public WorkOrderVO createWorkOrder(WorkOrderVO workOrderVO) {
		HttpEntity<WorkOrderVO> httpRequestEntity = (HttpEntity<WorkOrderVO>)buildUpdateHttpRequestEntity(workOrderVO);
		
		WorkOrderVO workOrderVOCreated = null;
		ResponseEntity<WorkOrderVO> response = restTemplate.exchange(VerizonConfigConstants.workOrderBaseUri, 
				HttpMethod.POST, httpRequestEntity, WorkOrderVO.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			workOrderVOCreated = response.getBody();
		}
		
		return workOrderVOCreated;
	}
	
	@Override
	public WorkOrderVO getWorkOrder(String workOrderNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		WorkOrderVO workOrderVO = null;
		ResponseEntity<WorkOrderVO> response = restTemplate.exchange(VerizonConfigConstants.getWorkOrderUri, 
				HttpMethod.GET, httpRequestEntity, WorkOrderVO.class, workOrderNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			workOrderVO = response.getBody();
		}
		
		return workOrderVO;
	}
	
	@Override
	public void updateWorkOrder(WorkOrderVO workOrderVO) {
		HttpEntity<WorkOrderVO> httpRequestEntity = (HttpEntity<WorkOrderVO>) buildUpdateHttpRequestEntity(workOrderVO);
		
		ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.updateWorkOrderUri, 
				HttpMethod.PUT, httpRequestEntity, String.class, workOrderVO.WorkOrderNumber);
		//if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
		if (response.getStatusCode() == HttpStatus.OK) {
		}
	}
	
	@Override
	public WorkOrderStatusVO getWorkOrderStatus(String workOrderNum) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		WorkOrderStatusVO workOrderStatusVO = null;
		ResponseEntity<WorkOrderStatusVO> response = restTemplate.exchange(VerizonConfigConstants.getWorkOrderStatusUri, 
				HttpMethod.GET, httpRequestEntity, WorkOrderStatusVO.class, workOrderNum);
		if (response.getStatusCode().is2xxSuccessful()) {
			workOrderStatusVO = response.getBody();
		}
		
		return workOrderStatusVO;
	}
	
	@Override
	public void updateWorkOrderStatus(String workOrderNum, WorkOrderStatusVO workOrderStatusVO) {
		HttpEntity<WorkOrderStatusVO> httpRequestEntity = (HttpEntity<WorkOrderStatusVO>)buildUpdateHttpRequestEntity (workOrderStatusVO);
		
		ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.updateWorkOrderStatusUri, 
				HttpMethod.POST, httpRequestEntity, String.class, workOrderNum);
		//if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
		if (response.getStatusCode() == HttpStatus.OK) {
		}
	}
	
	@Override
	public WorkOrderStatusVO createWorkOrderStatusType(WorkOrderStatusVO workOrderStatusVO) {
		HttpEntity<WorkOrderStatusVO> httpRequestEntity = (HttpEntity<WorkOrderStatusVO>) buildUpdateHttpRequestEntity(workOrderStatusVO);
		
		WorkOrderStatusVO workOrderStatusVOCreated = null;
		ResponseEntity<WorkOrderStatusVO> response = restTemplate.exchange(VerizonConfigConstants.createWorkOrderStatusTypeUri, 
				HttpMethod.POST, httpRequestEntity, WorkOrderStatusVO.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			workOrderStatusVOCreated = response.getBody();
		}
		
		return workOrderStatusVOCreated;
	}
	
	@Override
	public List<WorkOrderStatusVO> getAllWorkOrderStatusTypes() {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		List<WorkOrderStatusVO> workOrderStatusList = Collections.emptyList();;
		ResponseEntity<WorkOrderStatusVO[]> response = restTemplate.exchange(VerizonConfigConstants.getAllWorkOrderStatusTypesUri, 
				HttpMethod.GET, httpRequestEntity, WorkOrderStatusVO[].class);
		if (response.getStatusCode().is2xxSuccessful()) {
			workOrderStatusList = Arrays.asList(response.getBody());
		}
		
		return workOrderStatusList;
	}
	
	@Override
	public WorkOrderTypeVO createWorkOrderType(WorkOrderTypeVO workOrderTypeVO) {
		HttpEntity<WorkOrderStatusVO> httpRequestEntity = (HttpEntity<WorkOrderStatusVO>)buildUpdateHttpRequestEntity(workOrderTypeVO);
		
		WorkOrderTypeVO workOrderTypeVOCreated = null;
		ResponseEntity<WorkOrderTypeVO> response = restTemplate.exchange(VerizonConfigConstants.createWorkOrderTypeUri, 
				HttpMethod.POST, httpRequestEntity, WorkOrderTypeVO.class);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			workOrderTypeVOCreated = response.getBody();
		}
		
		return workOrderTypeVOCreated;
	}
	
	@Override
	public WorkOrderTypeVO getWorkOrderType(String code) {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		WorkOrderTypeVO workOrderTypeVO = null;
		ResponseEntity<WorkOrderTypeVO> response = restTemplate.exchange(VerizonConfigConstants.getWorkOrderTypeUri, 
				HttpMethod.GET, httpRequestEntity, WorkOrderTypeVO.class, code);
		if (response.getStatusCode().is2xxSuccessful()) {
			workOrderTypeVO = response.getBody();
		}
		
		return workOrderTypeVO;
	}
	
	@Override
	public List<WorkOrderTypeVO> getAllWorkOrderTypes() {
		HttpEntity<String> httpRequestEntity = buildGetHttpRequestEntity();
		
		List<WorkOrderTypeVO> workOrderTypeVOList = Collections.emptyList();;
		ResponseEntity<WorkOrderTypeVO[]> response = restTemplate.exchange(VerizonConfigConstants.getAllWorkOrderTypesUri, 
				HttpMethod.GET, httpRequestEntity, WorkOrderTypeVO[].class);
		if (response.getStatusCode().is2xxSuccessful()) {
			workOrderTypeVOList = Arrays.asList(response.getBody());
		}
		
		return workOrderTypeVOList;
	}
	
	private HttpEntity<String> buildGetHttpRequestEntity() {
		String token = getToken();
		HttpEntity<String> httpRequestEntity = (HttpEntity<String>) HttpUtil.buildHttpRequestEntity(
				VerizonConfigConstants.headerAppIdKey, VerizonConfigConstants.appId, token,
				null, MediaType.APPLICATION_JSON, StringUtils.EMPTY);
		return httpRequestEntity;
	}
	
	private HttpEntity<?> buildUpdateHttpRequestEntity(Object obj) {
		String token = getToken();
		HttpEntity<?> httpRequestEntity = (HttpEntity<?>) HttpUtil.buildHttpRequestEntity(
				VerizonConfigConstants.headerAppIdKey, VerizonConfigConstants.appId, token, 
				MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, obj);
		return httpRequestEntity;
	}
}
