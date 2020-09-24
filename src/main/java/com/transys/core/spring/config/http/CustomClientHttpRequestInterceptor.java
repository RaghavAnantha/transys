package com.transys.core.spring.config.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpRequest;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	protected static Logger log = LogManager.getLogger("com.transys.core.spring.config");

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution)
			throws IOException {
		// Log the http request
		log.info("URI: {}", request.getURI());
		log.info("HTTP Method: {}", request.getMethod());
		log.info("HTTP Headers: {}", request.getHeaders());

		return execution.execute(request, bytes);
	}
}
