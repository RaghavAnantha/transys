package com.transys.service.verizon;

public interface VerizonConfigConstants {
	String userName = "REST_Driwotech@rav.com";
	String password = "BJKgY25RzQrN";
	String appId = "fleetmatics-p-us-uyogQ2Limmwd3exGPTkiwzz00luYXA1m0mj28OVa";
	String headerAppIdKey = "Atmosphere atmosphere_app_id";
	//Atmosphere realm=http://atmosphere,atmosphere_app_id=<Integration Manager App ID>
	
	String baseUri = "https://fim.api.us.fleetmatics.com:443/";
	
	String tokenUri = baseUri + "token";
	
	String cmdBaseUri = baseUri + "cmd/v1/";
	
	String vehicleBaseUri = cmdBaseUri + "vehicles";
	String getAllVehiclesUri = vehicleBaseUri;
	String getVehicleUri = vehicleBaseUri + "?={vehicleNum}";
	String updateVehicleUri = vehicleBaseUri + "/{vehicleNum}";
	
	String driverBaseUri = cmdBaseUri + "drivers";
	String getAllDriversUri = driverBaseUri;
	String getDriverUri = driverBaseUri + "?={driverNum}";
	String createDriverUri = driverBaseUri;
	String updateDriverUri = driverBaseUri;
	String deleteDriverUri = driverBaseUri + "/{driverNum}";
	
	String radBaseUri = baseUri + "rad/v1/vehicles";
	String getVehicleLocationUri = radBaseUri + "/{vehicleNum}/location";
	String getMultipleVehicleLocationsUri = radBaseUri + "/locations";
	String getVehicleStatusUri = radBaseUri + "/{vehicleNum}/status";
	String getMultipleVehicleStatusesUri = radBaseUri + "/statuses";
	
	String daBaseUri = baseUri + "da/v1/driverassignments/";
	String driverAssignmentDriverUri = daBaseUri + "drivers/{vehicleNum}/currentassignment";
	String driverAssignmentVehicleUri = daBaseUri + "vehicles/{driverNum}/currentassignment";
	
	String pasBaseUri = baseUri + "pas/v1/";
	String workOrderBaseUri = pasBaseUri + "workorders";
	String getWorkOrderUri = workOrderBaseUri + "/{workOrderNumber}";
	String updateWorkOrderUri = getWorkOrderUri;
	String getWorkOrderStatusUri = workOrderBaseUri + "/{workOrderNumber}/status";
	String updateWorkOrderStatusUri = getWorkOrderStatusUri;
	
	String workOrderStatusTypeBaseUri = pasBaseUri + "workorderstatuses";
	String getAllWorkOrderStatusTypesUri = workOrderStatusTypeBaseUri;
	String getWorkOrderStatusTypeUri = workOrderStatusTypeBaseUri + "/{code}";
	String createWorkOrderStatusTypeUri = workOrderStatusTypeBaseUri;
	
	String workOrderTypeBaseUri = pasBaseUri + "workordertypes";
	String getAllWorkOrderTypesUri = workOrderTypeBaseUri;
	String getWorkOrderTypeUri = workOrderTypeBaseUri + "/{code}";
	String createWorkOrderTypeUri = workOrderTypeBaseUri;
}
	
			
