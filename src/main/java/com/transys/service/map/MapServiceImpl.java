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
		geoApiContext = new GeoApiContext.Builder().apiKey(MapConfigConstants.googleMapsAPIKey).build();
	}
	
	@Override
	public String getGeocode() {
			GeocodingResult[] results;
			String latLngStr = StringUtils.EMPTY;
			try {
				results = GeocodingApi.geocode(geoApiContext,
				   "4249 N Hermitage Ave Chicago, IL 60613").await();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				System.out.println(gson.toJson(results[0].addressComponents));
				LatLng latLng = results[0].geometry.location;
				log.info("Latitude: {}, Longitude: {}", latLng.lat, latLng.lng);
				latLngStr = String.format("%s,%s", latLng.lat, latLng.lng);
			} catch (ApiException | InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
			return latLngStr;
	}
}
