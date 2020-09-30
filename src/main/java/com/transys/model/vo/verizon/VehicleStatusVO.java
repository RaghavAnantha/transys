package com.transys.model.vo.verizon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "DeviceTimeZoneOffset", "DeviceTimeZoneUseDST", "DisplayState", "DriverNumber", "Speed",
		"UpdateUTC", "DriverName", "EngineMinutes", "CurrentOdometer", "IdleTime", "SensorValues" })
public class VehicleStatusVO {
	@JsonProperty("DeviceTimeZoneOffset")
	private Integer deviceTimeZoneOffset;
	@JsonProperty("DeviceTimeZoneUseDST")
	private Boolean deviceTimeZoneUseDST;
	@JsonProperty("DisplayState")
	private String displayState;
	@JsonProperty("DriverNumber")
	private Object driverNumber;
	@JsonProperty("Speed")
	private Integer speed;
	@JsonProperty("UpdateUTC")
	private String updateUTC;
	@JsonProperty("DriverName")
	private Object driverName;
	@JsonProperty("EngineMinutes")
	private Integer engineMinutes;
	@JsonProperty("CurrentOdometer")
	private Double currentOdometer;
	@JsonProperty("IdleTime")
	private Integer idleTime;
	@JsonProperty("SensorValues")
	private List<String> sensorValues = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("DriverNumber")
	public Object getDriverNumber() {
		return driverNumber;
	}

	@JsonProperty("DriverNumber")
	public void setDriverNumber(Object driverNumber) {
		this.driverNumber = driverNumber;
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
	}

	@JsonProperty("DriverName")
	public Object getDriverName() {
		return driverName;
	}

	@JsonProperty("DriverName")
	public void setDriverName(Object driverName) {
		this.driverName = driverName;
	}

	@JsonProperty("EngineMinutes")
	public Integer getEngineMinutes() {
		return engineMinutes;
	}

	@JsonProperty("EngineMinutes")
	public void setEngineMinutes(Integer engineMinutes) {
		this.engineMinutes = engineMinutes;
	}

	@JsonProperty("CurrentOdometer")
	public Double getCurrentOdometer() {
		return currentOdometer;
	}

	@JsonProperty("CurrentOdometer")
	public void setCurrentOdometer(Double currentOdometer) {
		this.currentOdometer = currentOdometer;
	}

	@JsonProperty("IdleTime")
	public Integer getIdleTime() {
		return idleTime;
	}

	@JsonProperty("IdleTime")
	public void setIdleTime(Integer idleTime) {
		this.idleTime = idleTime;
	}

	@JsonProperty("SensorValues")
	public List<String> getSensorValues() {
		return sensorValues;
	}

	@JsonProperty("SensorValues")
	public void setSensorValues(List<String> sensorValues) {
		this.sensorValues = sensorValues;
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
