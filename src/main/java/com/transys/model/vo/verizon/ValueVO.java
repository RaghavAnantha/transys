package com.transys.model.vo.verizon;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Address", "DeltaDistance", "DeltaTime", "DeviceTimeZoneOffset", "DeviceTimeZoneUseDST",
		"DisplayState", "Direction", "Heading", "DriverNumber", "GeoFenceName", "Latitude", "Longitude", "Speed",
		"UpdateUTC", "IsPrivate" })
public class ValueVO {
	@JsonProperty("Address")
	private AddressVO address;
	@JsonProperty("DeltaDistance")
	private Object deltaDistance;
	@JsonProperty("DeltaTime")
	private Object deltaTime;
	@JsonProperty("DeviceTimeZoneOffset")
	private Object deviceTimeZoneOffset;
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
	private Object speed;
	@JsonProperty("UpdateUTC")
	private String updateUTC;
	@JsonProperty("IsPrivate")
	private Boolean isPrivate;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Address")
	public AddressVO getAddress() {
		return address;
	}

	@JsonProperty("Address")
	public void setAddress(AddressVO address) {
		this.address = address;
	}

	@JsonProperty("DeltaDistance")
	public Object getDeltaDistance() {
		return deltaDistance;
	}

	@JsonProperty("DeltaDistance")
	public void setDeltaDistance(Object deltaDistance) {
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
	public Object getDeviceTimeZoneOffset() {
		return deviceTimeZoneOffset;
	}

	@JsonProperty("DeviceTimeZoneOffset")
	public void setDeviceTimeZoneOffset(Object deviceTimeZoneOffset) {
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
	public Object getSpeed() {
		return speed;
	}

	@JsonProperty("Speed")
	public void setSpeed(Object speed) {
		this.speed = speed;
	}

	@JsonProperty("UpdateUTC")
	public String getUpdateUTC() {
		return updateUTC;
	}

	@JsonProperty("UpdateUTC")
	public void setUpdateUTC(String updateUTC) {
		this.updateUTC = updateUTC;
	}

	@JsonProperty("IsPrivate")
	public Boolean getIsPrivate() {
		return isPrivate;
	}

	@JsonProperty("IsPrivate")
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}