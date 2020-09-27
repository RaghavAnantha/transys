package com.transys.core.spring.config.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Autowired
	private CloseableHttpClient httpClient;
	
	/*
	private final CloseableHttpClient httpClient;
	
	@Autowired
	public RestTemplateConfig(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	@Autowired
	public RestTemplateConfig() {
		this.httpClient = null;
	}*/
	
	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient);
		return clientHttpRequestFactory;
	}

	@Bean
	public RestTemplate restTemplate() {
		/*return new RestTemplateBuilder().requestFactory(this::clientHttpRequestFactory)
				.errorHandler(new CustomClientErrorHandler()).interceptors(new CustomClientHttpRequestInterceptor())
				.build();*/
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory());
		restTemplate.setErrorHandler(new CustomClientHttpResponseErrorHandler());
		
		List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<ClientHttpRequestInterceptor>(); 
		interceptorList.add(new CustomClientHttpRequestInterceptor());
		restTemplate.setInterceptors(interceptorList);
		
		return restTemplate;
	}
}
