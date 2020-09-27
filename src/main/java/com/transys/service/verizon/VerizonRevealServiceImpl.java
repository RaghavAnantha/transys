package com.transys.service.verizon;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.transys.core.util.HttpUtil;

public class VerizonRevealServiceImpl implements VerizonRevealService {
	protected static Logger log = LogManager.getLogger("com.transys.service.verizon");
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String token;
	private Date tokenAge;
	
	@Override
	public String getToken() {
		Date now = new Date();
		if (StringUtils.isEmpty(token) 
				|| tokenAge == null
				|| now.after(tokenAge)) {
			this.token = retrieveToken();
		}
		return this.token;
	}
	
	public String retrieveToken() {
		String token = StringUtils.EMPTY;
		//RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		try {
			HttpEntity<String> httpRequestEntity = HttpUtil.createHttpRequestEntity(VerizonConfigConstants.userName, 
					VerizonConfigConstants.password, null, MediaType.TEXT_PLAIN, StringUtils.EMPTY);
			ResponseEntity<String> response = restTemplate.exchange(VerizonConfigConstants.tokenUri, 
					HttpMethod.GET, httpRequestEntity, String.class);
			log.info("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody()
					+ ". Body: " + response.getBody());
			
			if (response.getStatusCode().is2xxSuccessful()) {
				setTokenAge();
				token = response.getBody();
			}
		} catch (Exception eek) {
			System.out.println("** Exception: " + eek.getMessage());
		}
		
		return token;
	}
	
	private void setTokenAge() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 20);
		this.tokenAge = now.getTime();
	}
	
	/*
	private String getToken() {
		String authString = StringUtils.EMPTY;
		String userName = "14af6733-0d19-4af7-bc61-41fd16edcc91.fleetmatics-p-us";
		String password = "!ntegration16";
		String tokenUri = "https://fim.api.us.fleetmatics.com:443/token";
		try {
			String basicAuth = buildBasicAuth(userName, password);
			
			URL url = new URL(tokenUri); 
			
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); 
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", basicAuth); 
			conn.setRequestProperty("Accept", "text/plain"); 
			conn.setDoOutput(false); // Do not send request body

			if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
				conn.getResponseMessage();
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			authString = br.readLine(); 

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		} 
		
		return authString;
	}
	*/
}
