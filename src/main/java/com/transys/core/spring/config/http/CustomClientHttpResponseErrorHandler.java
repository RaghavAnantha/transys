package com.transys.core.spring.config.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomClientHttpResponseErrorHandler implements ResponseErrorHandler {
	protected static Logger log = LogManager.getLogger("com.transys.core.spring.config");
	
	@Override
	public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
		return clientHttpResponse.getStatusCode().is4xxClientError();
	}

	@Override
	public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
		log.error("HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
	}
}
