package com.transys.service.map;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;

import com.google.maps.errors.ApiException;

import com.google.maps.model.GeocodingResult;

import com.google.maps.model.LatLng;

import com.transys.model.map.Geocode;

import com.transys.service.BaseService;

public class MapServiceImpl extends BaseService implements MapService {
	private GeoApiContext geoApiContext;
	
	public MapServiceImpl() {
		geoApiContext = new GeoApiContext.Builder().apiKey(MapConfigConstants.apiKey).build();
	}
	
	@Override
	public Geocode retrieveGeocode(String address, HttpServletRequest request) {
		if (StringUtils.isEmpty(address)) {
			return null;
		}
		
		log.info("Now retrieving Geocode");
		return saveGeocode(address, request);
	}
	
	@Override
	public Geocode saveGeocode(String address, HttpServletRequest request) {
		if (StringUtils.isEmpty(address)) {
			return null;
		}
		
		String query = "select obj from Geocode obj where obj.deleteFlag='1'"
				+ " and obj.address='" + address + "'";
		List<Geocode> geocodeList = genericDAO.executeSimpleQuery(query);
		if (geocodeList != null && !geocodeList.isEmpty()) {
			return geocodeList.get(0);
		}
		
		String latLng = findLatLng(address);
		if (StringUtils.isEmpty(latLng)) {
			return null;
		}
		
		Geocode geocode = new Geocode();
		geocode.setAddress(address);
		geocode.setLatLng(latLng);
		setModifier(request, geocode);
		
		log.info("Now saving Geocode");
		genericDAO.save(geocode);
		return geocode;
	}
	
	private String findLatLng(String address) {
		if (StringUtils.isEmpty(address)) {
			return StringUtils.EMPTY;
		}
		
		GeocodingResult[] results;
		String latLngStr = StringUtils.EMPTY;
		try {
			results = GeocodingApi.geocode(geoApiContext, address).await();
			if (results == null || results.length <= 0) {
				return latLngStr;
			}
			GeocodingResult aResult = results[0];
			if (aResult.geometry == null || aResult.geometry.location == null) {
				return latLngStr;
			}
			
			LatLng latLng = aResult.geometry.location;
			log.info("Latitude: {}, Longitude: {}", latLng.lat, latLng.lng);
			latLngStr = String.format("%s,%s", latLng.lat, latLng.lng);
			
			/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
			log.info(gson.toJson(results[0].addressComponents));*/
		} catch (ApiException | InterruptedException | IOException e) {
			log.error(e);
		}
		
		return latLngStr;
	}
}
