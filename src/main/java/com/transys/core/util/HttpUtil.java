package com.transys.core.util;

import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.transys.service.verizon.VerizonConfigConstants;

public class HttpUtil {
	public static String buildBasicAuth(String userName, String password) {
		String userCredentials = userName + ":" + password; 
		return "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes()); 
	}
	
	public static void addAuthHeaders(HttpHeaders httpHeaders, String authHeader) {
		httpHeaders.add("Authorization", authHeader);
	}
	
	public static void addBasicAuthHeaders(HttpHeaders httpHeaders, String userName, String password) {
		String basicAuth = buildBasicAuth(userName, password);
		addAuthHeaders(httpHeaders, basicAuth);
	}
	
	public static HttpHeaders buildHttpHeaders(String userName, String password, 
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
	
	public static HttpHeaders buildHttpHeaders(String appIdHeaderKey, String appId, String token,
			MediaType contentType, MediaType acceptType) {
		HttpHeaders httpHeaders = new HttpHeaders();
		//httpHeaders.clear();
		
		addAppAuthHeader(httpHeaders, appIdHeaderKey, appId, token);
		
		setContentType(httpHeaders, contentType);
		setAcceptType(httpHeaders, acceptType);
		
		return httpHeaders;
	}
	
	public static HttpEntity<String> buildHttpRequestEntity(String userName, String password, 
			MediaType contentType, MediaType acceptType, String requestBody) {
		HttpHeaders headers = buildHttpHeaders(userName, password, contentType, acceptType);
		//HttpEntity<String> headersRequestEntity = new HttpEntity<String>("parameters", headers);
		HttpEntity<String> httpRequestEntity = new HttpEntity<String>(requestBody, headers);
		return httpRequestEntity;
	}
	
	public static HttpEntity<?> buildHttpRequestEntity(String headerAppIdKey, String appId, 
			String token, MediaType contentType, MediaType acceptType, Object requestBody) {
		HttpHeaders headers = HttpUtil.buildHttpHeaders(headerAppIdKey, appId, token, contentType, acceptType);
		HttpEntity<?> httpRequestEntity = new HttpEntity<>(requestBody, headers);
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
	
	public static void addAppAuthHeader(HttpHeaders httpHeaders, String appIdHeaderKey, String appId, String token) {
		String appAuthHeader = String.format(appIdHeaderKey += "=%s, Bearer %s", appId, token);
		addAuthHeaders(httpHeaders, appAuthHeader);
	}
}
