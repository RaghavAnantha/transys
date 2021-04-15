package com.transys.service.map;

import javax.servlet.http.HttpServletRequest;

import com.transys.model.map.Geocode;

public interface MapService {
	public Geocode retrieveGeocode(String address, HttpServletRequest request);
	public Geocode saveGeocode(String address, HttpServletRequest request);
}
