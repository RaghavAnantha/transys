package com.transys.model.vo.verizon;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.transys.core.util.DateUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Address", "DeltaDistance", "DeltaTime", "DeviceTimeZoneOffset", "DeviceTimeZoneUseDST",
		"DisplayState", "Direction", "Heading", "DriverNumber", "GeoFenceName", "Latitude", "Longitude", "Speed",
		"UpdateUTC" })
public class VehicleLocationVO {
	@JsonProperty("Address")
	private AddressVO address;
	@JsonProperty("DeltaDistance")
	private Integer deltaDistance;
	@JsonProperty("DeltaTime")
	private Object deltaTime;
	@JsonProperty("DeviceTimeZoneOffset")
	private Integer deviceTimeZoneOffset;
	@JsonProperty("DeviceTimeZoneUseDST")
	private Boolean deviceTimeZoneUseDST;
	@JsonProperty("DisplayState")
	private String displayState;
	@JsonProperty("Direction")
	private Integer direction;
	@JsonProperty("Heading")
	private String heading;
	@JsonProperty("DriverNumber")
	private Object driverNumber;
	@JsonProperty("GeoFenceName")
	private Object geoFenceName;
	@JsonProperty("Latitude")
	private Double latitude;
	@JsonProperty("Longitude")
	private Double longitude;
	@JsonProperty("Speed")
	private Integer speed;
	@JsonProperty("UpdateUTC")
	private String updateUTC;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("VehicleNumber")
	private String vehicleNumber;
	@JsonProperty("addressStr")
	private String addressStr;
	@JsonProperty("updateDateTime")
	private String updateDateTime;
	
	@JsonProperty("Address")
	public AddressVO getAddress() {
		return address;
	}

	@JsonProperty("Address")
	public void setAddress(AddressVO address) {
		this.address = address;
	}

	@JsonProperty("DeltaDistance")
	public Integer getDeltaDistance() {
		return deltaDistance;
	}

	@JsonProperty("DeltaDistance")
	public void setDeltaDistance(Integer deltaDistance) {
		this.deltaDistance = deltaDistance;
	}

	@JsonProperty("DeltaTime")
	public Object getDeltaTime() {
		return deltaTime;
	}

	@JsonProperty("DeltaTime")
	public void setDeltaTime(Object deltaTime) {
		this.deltaTime = deltaTime;
	}

	@JsonProperty("DeviceTimeZoneOffset")
	public Integer getDeviceTimeZoneOffset() {
		return deviceTimeZoneOffset;
	}

	@JsonProperty("DeviceTimeZoneOffset")
	public void setDeviceTimeZoneOffset(Integer deviceTimeZoneOffset) {
		this.deviceTimeZoneOffset = deviceTimeZoneOffset;
	}

	@JsonProperty("DeviceTimeZoneUseDST")
	public Boolean getDeviceTimeZoneUseDST() {
		return deviceTimeZoneUseDST;
	}

	@JsonProperty("DeviceTimeZoneUseDST")
	public void setDeviceTimeZoneUseDST(Boolean deviceTimeZoneUseDST) {
		this.deviceTimeZoneUseDST = deviceTimeZoneUseDST;
	}

	@JsonProperty("DisplayState")
	public String getDisplayState() {
		return displayState;
	}

	@JsonProperty("DisplayState")
	public void setDisplayState(String displayState) {
		this.displayState = displayState;
	}

	@JsonProperty("Direction")
	public Integer getDirection() {
		return direction;
	}

	@JsonProperty("Direction")
	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	@JsonProperty("Heading")
	public String getHeading() {
		return heading;
	}

	@JsonProperty("Heading")
	public void setHeading(String heading) {
		this.heading = heading;
	}

	@JsonProperty("DriverNumber")
	public Object getDriverNumber() {
		return driverNumber;
	}

	@JsonProperty("DriverNumber")
	public void setDriverNumber(Object driverNumber) {
		this.driverNumber = driverNumber;
	}

	@JsonProperty("GeoFenceName")
	public Object getGeoFenceName() {
		return geoFenceName;
	}

	@JsonProperty("GeoFenceName")
	public void setGeoFenceName(Object geoFenceName) {
		this.geoFenceName = geoFenceName;
	}

	@JsonProperty("Latitude")
	public Double getLatitude() {
		return latitude;
	}

	@JsonProperty("Latitude")
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("Longitude")
	public Double getLongitude() {
		return longitude;
	}

	@JsonProperty("Longitude")
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("Speed")
	public Integer getSpeed() {
		return speed;
	}

	@JsonProperty("Speed")
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	@JsonProperty("UpdateUTC")
	public String getUpdateUTC() {
		return updateUTC;
	}

	@JsonProperty("UpdateUTC")
	public void setUpdateUTC(String updateUTC) {
		this.updateUTC = updateUTC;
		
		String ctDateTime = DateUtil.convertUTCToCT(updateUTC);
		setUpdateDateTime(ctDateTime);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonProperty("vehicleNumber")
	public String getVehicleNumber() {
		return vehicleNumber;
	}

	@JsonProperty("vehicleNumber")
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	@JsonProperty("addressStr")
	public String getAddressStr() {
		if (StringUtils.isNotEmpty(addressStr)) {
			return addressStr;
		}
		return getAddress() == null ? StringUtils.EMPTY : getAddress().toString();
	}

	@JsonProperty("addressStr")
	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}

	@JsonProperty("updateDateTime")
	public String getUpdateDateTime() {
		return updateDateTime;
	}

	@JsonProperty("updateDateTime")
	public void setUpdateDateTime(String updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
}