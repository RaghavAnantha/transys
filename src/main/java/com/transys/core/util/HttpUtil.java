package com.transys.core.util;

import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpUtil {
	public static String buildBasicAuth(String userName, String password) {
		String userCredentials = userName + ":" + password; 
		return "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes()); 
	}
	
	public static void addBasicAuthHeaders(HttpHeaders httpHeaders, String userName, String password) {
		String basicAuth = buildBasicAuth(userName, password);
		httpHeaders.add("Authorization", basicAuth);
	}
	
	public static HttpHeaders createHttpHeaders(String userName, String password, 
			MediaType contentType, MediaType acceptType) {
		HttpHeaders httpHeaders = new HttpHeaders();
		//httpHeaders.clear();
		
		if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
			HttpUtil.addBasicAuthHeaders(httpHeaders, userName, password);
		}
		
		setContentType(httpHeaders, contentType);
		setAcceptType(httpHeaders, acceptType);
		
		return httpHeaders;
	}
	
	public static HttpEntity<String> createHttpRequestEntity(String userName, String password, 
			MediaType contentType, MediaType acceptType, String requestBody) {
		HttpHeaders headers = createHttpHeaders(userName, password, contentType, acceptType);
		//HttpEntity<String> headersRequestEntity = new HttpEntity<String>("parameters", headers);
		HttpEntity<String> httpRequestEntity = new HttpEntity<String>(requestBody, headers);
		return httpRequestEntity;
	}
	
	public static void setContentType(HttpHeaders httpHeaders, MediaType contentType) {
		if (contentType != null) {
			httpHeaders.setContentType(contentType);
		}
	}
	
	public static void setAcceptType(HttpHeaders httpHeaders, MediaType acceptType) {
		if (acceptType != null) {
			httpHeaders.setAccept(Arrays.asList(new MediaType[]{ acceptType }));
		}
	}
}
