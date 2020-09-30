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
@JsonPropertyOrder({ "AddressLine1", "AddressLine2", "Locality", "AdministrativeArea", "PostalCode", "Country" })
public class Address {
	@JsonProperty("AddressLine1")
	private String addressLine1;
	@JsonProperty("AddressLine2")
	private String addressLine2;
	@JsonProperty("Locality")
	private String locality;
	@JsonProperty("AdministrativeArea")
	private String administrativeArea;
	@JsonProperty("PostalCode")
	private String postalCode;
	@JsonProperty("Country")
	private String country;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("AddressLine1")
	public String getAddressLine1() {
		return addressLine1;
	}

	@JsonProperty("AddressLine1")
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	@JsonProperty("AddressLine2")
	public String getAddressLine2() {
		return addressLine2;
	}

	@JsonProperty("AddressLine2")
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	@JsonProperty("Locality")
	public String getLocality() {
		return locality;
	}

	@JsonProperty("Locality")
	public void setLocality(String locality) {
		this.locality = locality;
	}

	@JsonProperty("AdministrativeArea")
	public String getAdministrativeArea() {
		return administrativeArea;
	}

	@JsonProperty("AdministrativeArea")
	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	@JsonProperty("PostalCode")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("PostalCode")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@JsonProperty("Country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("Country")
	public void setCountry(String country) {
		this.country = country;
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
