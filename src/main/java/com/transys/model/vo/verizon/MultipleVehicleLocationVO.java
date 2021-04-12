package com.transys.model.vo.verizon;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "VehicleNumber", "StatusCode", "ContentResource" })
public class MultipleVehicleLocationVO {
	@JsonProperty("VehicleNumber")
	private String vehicleNumber;
	@JsonProperty("StatusCode")
	private Integer statusCode;
	@JsonProperty("ContentResource")
	private ContentResourceVO contentResource;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("VehicleNumber")
	public String getVehicleNumber() {
		return vehicleNumber;
	}

	@JsonProperty("VehicleNumber")
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	@JsonProperty("StatusCode")
	public Integer getStatusCode() {
		return statusCode;
	}

	@JsonProperty("StatusCode")
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@JsonProperty("ContentResource")
	public ContentResourceVO getContentResource() {
		return contentResource;
	}

	@JsonProperty("ContentResource")
	public void setContentResource(ContentResourceVO contentResource) {
		this.contentResource = contentResource;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	@JsonIgnore
	public String getGeocode() {
		String geocode = StringUtils.EMPTY;
		Double latitude = getLatitude();
		Double longitude = getLongitude();
		if (latitude != null && longitude != null) {
			geocode = latitude + "," + longitude;
		}
		return geocode;
	}
	
	@JsonIgnore
	public Double getLatitude() {
		Double latitude = null;
		ValueVO valueVO = getValueVO();
		if (valueVO != null && valueVO.getLatitude() != null) {
			latitude = valueVO.getLatitude();
		}
		return latitude;
	}
	
	@JsonIgnore
	public Double getLongitude() {
		Double longitude = null;
		ValueVO valueVO = getValueVO();
		if (valueVO != null && valueVO.getLongitude() != null) {
			longitude = valueVO.getLongitude();
		}
		return longitude;
	}
	
	@JsonIgnore
	public Integer getDirection() {
		Integer direction = null;
		ValueVO valueVO = getValueVO();
		if (valueVO != null) {
			if (valueVO.getDirection() != null) {
				direction = valueVO.getDirection();
			}
		}
		return direction;
	}
	
	@JsonIgnore
	public String getDisplayState() {
		String displayState = StringUtils.EMPTY;
		ValueVO valueVO = getValueVO();
		if (valueVO != null) {
			if (valueVO.getDisplayState() != null) {
				displayState = valueVO.getDisplayState();
			}
		}
		return displayState;
	}
	
	@JsonIgnore
	public String getAddressStr() {
		String addressStr = StringUtils.EMPTY;
		ValueVO valueVO = getValueVO();
		if (valueVO != null) {
			if (valueVO.getAddress() != null) {
				addressStr = valueVO.getAddress().getFullAddress();
			}
		}
		return addressStr;
	}
	
	@JsonIgnore
	public String getHeading() {
		String heading = StringUtils.EMPTY;
		ValueVO valueVO = getValueVO();
		if (valueVO != null && StringUtils.isNotEmpty(valueVO.getHeading())) {
			heading = valueVO.getHeading();
		}
		return heading;
	}
	
	@JsonIgnore
	public Integer getSpeed() {
		Integer speed = null;
		ValueVO valueVO = getValueVO();
		if (valueVO != null && valueVO.getSpeed() != null) {
			speed = ((Double) valueVO.getSpeed()).intValue();
		}
		return speed;
	}
	
	@JsonIgnore
	public String getUpdateUTC() {
		String updateUTC = StringUtils.EMPTY;
		ValueVO valueVO = getValueVO();
		if (valueVO != null && StringUtils.isNotEmpty(valueVO.getUpdateUTC())) {
			updateUTC = valueVO.getUpdateUTC();
		}
		return updateUTC;
	}
	
	@JsonIgnore
	public ValueVO getValueVO() {
		ValueVO valueVO = null;
		ContentResourceVO contentResourceVO = getContentResource();
		if (contentResourceVO != null) {
			valueVO = contentResourceVO.getValue();
		}
		return valueVO;
	}
}