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
@JsonPropertyOrder({ "Value", "StatusCode" })
public class ContentResourceVO {
	@JsonProperty("Value")
	private ValueVO value;
	@JsonProperty("StatusCode")
	private Integer statusCode;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Value")
	public ValueVO getValue() {
		return value;
	}

	@JsonProperty("Value")
	public void setValue(ValueVO value) {
		this.value = value;
	}

	@JsonProperty("StatusCode")
	public Integer getStatusCode() {
		return statusCode;
	}

	@JsonProperty("StatusCode")
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
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