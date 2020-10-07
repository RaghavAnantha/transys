package com.transys.service.map;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;

import com.google.maps.errors.ApiException;

import com.google.maps.model.GeocodingResult;

import com.google.maps.model.LatLng;

public class MapServiceImpl implements MapService {
	protected static Logger log = LogManager.getLogger("com.transys.service.map");
	
	private GeoApiContext geoApiContext;
	
	public MapServiceImpl() {
		geoApiContext = new GeoApiContext.Builder().apiKey(MapConfigConstants.apiKey).build();
	}
	
	@Override
	public String getGeocode(String address) {
			GeocodingResult[] results;
			String latLngStr = StringUtils.EMPTY;
			try {
				results = GeocodingApi.geocode(geoApiContext, address).await();
				LatLng latLng = results[0].geometry.location;
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
