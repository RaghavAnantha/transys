package com.transys.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;

import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.DeliveryPickupReportVO;

import com.transys.service.DynamicReportService;

import org.springframework.context.annotation.Bean;

@Controller
@RequestMapping("/reports/deliveryPickupReport")
public class DeliveryPickupReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public DeliveryPickupReportController() {
		setUrlContext("reports/deliveryPickupReport");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		
		//RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		ResponseEntity<String> result;
		try {
			System.out.println(log.getLevel());
			System.out.println(System.getProperty("jetty.base"));
			System.out.println(System.getProperty("jetty.home"));
			System.out.println(System.getProperty("JETTY_HOME"));
			System.out.println(System.getProperty("JETTY_BASE"));
			result = restTemplate.getForEntity(new URI("http://www.google.com"), String.class);
			String body = result.getBody();
			log.info("Length: " + body.length());
			log.info("Body: " + body);
			
			getToken2();
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return urlContext + "/list";
	}
	
	private String getToken() {
		String authString = StringUtils.EMPTY;
		String userName = "14af6733-0d19-4af7-bc61-41fd16edcc91.fleetmatics-p-us";
		String password = "!ntegration16";
		String tokenUri = "https://fim.api.us.fleetmatics.com:443/token";
		ResponseEntity<String> result;
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

			// Reads text from a character-input stream
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			authString = br.readLine(); 

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RestClientException e) {
			e.printStackTrace();
		} /*catch (URISyntaxException e) {
			e.printStackTrace();
		}*/
		
		return authString;
	}
	
	private HttpHeaders createHttpHeaders(String user, String password) {
		String notEncoded = user + ":" + password;
		String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
		HttpHeaders headers = new HttpHeaders();
		headers.clear();
		//headers.setContentType(MediaType.TEXT_PLAIN);
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.TEXT_PLAIN }));
		headers.add("Authorization", encodedAuth);
		return headers;
	}

	private void getToken2() {
		String authString = StringUtils.EMPTY;
		String userName = "14af6733-0d19-4af7-bc61-41fd16edcc91.fleetmatics-p-us";
		String password = "!ntegration16";
		String tokenUri = "https://fim.api.us.fleetmatics.com:443/token";
		try {
			HttpHeaders headers = createHttpHeaders(userName, password);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.GET, entity, String.class);
			System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
		} catch (Exception eek) {
			System.out.println("** Exception: " + eek.getMessage());
		}
	}
	
	private String getToken1() {
		String authString = "";
		String userName = "14af6733-0d19-4af7-bc61-41fd16edcc91.fleetmatics-p-us";
		String password = "!ntegration16";
		String tokenUri = "https://fim.api.us.fleetmatics.com:443/token";
		try {
			String basicAuth = buildBasicAuth(userName, password);

			URL url = new URL(tokenUri); 
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(); 
			conn.setRequestMethod("GET"); // Set request type to GET
			conn.setRequestProperty("Authorization", basicAuth); 
			conn.setRequestProperty("Accept", "text/plain"); 
			conn.setDoOutput(false); // Do not send request body

			if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
				conn.getResponseMessage();
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			// Reads text from a character-input stream
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			authString = br.readLine(); 

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return authString;
	}

	private static String buildBasicAuth(String userName, String password) {
		String userCredentials = userName + ":" + password; 
		return "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes()); 
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		model.addAttribute("msgCtx", "deliveryPickupReport");
		model.addAttribute("errorCtx", "deliveryPickupReport");
		
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.retrieveOrderDeliveryAddresses(genericDAO);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") DeliveryPickupReportVO input) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(250);
		
		DeliveryPickupReportVO inputToBeUsed = input;
		
		// Paging related
		String p = request.getParameter("p");
		if (StringUtils.isEmpty(p)) {
			request.getSession().setAttribute("input", input);
		} else {
			inputToBeUsed = (DeliveryPickupReportVO)request.getSession().getAttribute("input");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Map<String, Object>> reportData = generateReportData(model, criteria, inputToBeUsed, params);
			
			String reportName = "deliveryPickupReport";
			String type = "html";
			setReportRequestHeaders(response, type, reportName);
			out = dynamicReportService.generateStaticReport(reportName, reportData, 
								params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;				
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to create file: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating report: " + t);
			return "report.error";
		} finally {
			criteria.setPage(originalPage);
			criteria.setPageSize(originalPageSize);
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String setDumpsterSizeAggregation(ModelMap model, List<Order> orderList) {
		StringBuffer selectedOrderIds = new StringBuffer();
		if (orderList == null || orderList.size() == 0) {
			// throw Exception??
			System.out.println("No orders matching criteria, report will be empty.");
		}
		
		for(Order o : orderList) {
			selectedOrderIds.append(o.getId() + ",");
		}
		List<?> aggregationResults = genericDAO.executeSimpleQuery("select dumpsterSize.size, count(*) from Order p where p.deleteFlag='1' and p.id IN (" + selectedOrderIds.substring(0,selectedOrderIds.lastIndexOf(",")) + ") group by p.dumpsterSize.size");
		List<String> dumpsterSizes = new ArrayList<String>();
		List<String> count = new ArrayList<String>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		int index = 0;
		for (Object o : aggregationResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			jsonResponse = jsonResponse.substring(1, jsonResponse.length()-1);
			
			String [] tokens = jsonResponse.split(",");
			dumpsterSizes.add(index, tokens[0].substring(1, tokens[0].length()-1)); // eliminate quotes
			count.add(index++, tokens[1]);
		}

		StringBuffer aggregationResult = new StringBuffer();
		for (int i = 0; i < dumpsterSizes.size(); i++) {
			aggregationResult.append(dumpsterSizes.get(i) + " : " + count.get(i) + "   ");
		}
		
		return aggregationResult.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(2500);
		
		DeliveryPickupReportVO input = (DeliveryPickupReportVO)request.getSession().getAttribute("input");
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Map<String,Object>> reportData = generateReportData(model, criteria, input, params);
			
			String reportName = "deliveryPickupReport";
			type = setReportRequestHeaders(response, type, reportName);
			out = dynamicReportService.generateStaticReport(reportName, reportData, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Delivery Pickup report: " + t);
			return "redirect:/" + getUrlContext() + "/main.do";
		} finally {
			criteria.setPage(originalPage);
			criteria.setPageSize(originalPageSize);
			
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<Order> performSearch(SearchCriteria criteria, DeliveryPickupReportVO input) {
		String deliveryAddressId = input.getDeliveryAddress();
		String deliveryDateFrom = input.getDeliveryDateFrom();
		String deliveryDateTo = input.getDeliveryDateTo();
		String pickupDateFrom = input.getPickupDateFrom();
		String pickupDateTo = input.getPickupDateTo();
		
		StringBuffer query = new StringBuffer("select obj from Order obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Order obj where 1=1");
		StringBuffer whereClause = new StringBuffer(" and obj.deleteFlag=1");
		
		OrderStatus orderStatus = ModelUtil.retrieveOrderStatus(genericDAO, OrderStatus.ORDER_STATUS_CANCELED);
		whereClause.append(" and obj.orderStatus.id !=" + orderStatus.getId().longValue());
		
		if (StringUtils.isNotEmpty(deliveryAddressId)) {
			whereClause.append(" and obj.deliveryAddress.id=" + deliveryAddressId);
		}
		if (StringUtils.isNotEmpty(deliveryDateFrom)){
        	whereClause.append(" and obj.deliveryDate >='"+FormatUtil.convertInputDateToDbDate(deliveryDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(deliveryDateTo)){
	     	whereClause.append(" and obj.deliveryDate <='"+FormatUtil.convertInputDateToDbDate(deliveryDateTo)+"'");
	   }
      if (StringUtils.isNotEmpty(pickupDateFrom)){
        	whereClause.append(" and obj.pickupDate >='"+FormatUtil.convertInputDateToDbDate(pickupDateFrom)+"'");
		}
      if (StringUtils.isNotEmpty(pickupDateTo)){
	     	whereClause.append(" and obj.pickupDate <='"+FormatUtil.convertInputDateToDbDate(pickupDateTo)+"'");
	   }
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Order> orderList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		return orderList;
	}
	
	private String extractPermitAddress(Order anOrder) {
		List<Permit> permitList = anOrder.getPermits();
		if (permitList == null || permitList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		return permitList.get(0).getFullLinePermitAddress1();
	}
	
	private List<Map<String, Object>> generateReportData(ModelMap model, SearchCriteria criteria, 
			DeliveryPickupReportVO input, Map<String, Object> params) {
		List<Order> orderList = performSearch(criteria, input);
		String dumpsterSizeAggregation = setDumpsterSizeAggregation(model, orderList);
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Order anOrder : orderList) {
			DeliveryAddress deliveryAddress = anOrder.getDeliveryAddress();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", StringUtils.EMPTY + anOrder.getId().toString());
			map.put("customer", StringUtils.EMPTY + anOrder.getCustomer().getCompanyName());
			map.put("deliveryAddress", StringUtils.EMPTY + deliveryAddress.getFullLine());
			map.put("city", StringUtils.EMPTY + deliveryAddress.getCity());
			map.put("permitAddress", extractPermitAddress(anOrder));
			map.put("dumpsterSize", StringUtils.EMPTY + anOrder.getDumpsterSize().getSize());
			map.put("dumpsterNum", (anOrder.getDumpster() == null ? StringUtils.EMPTY : anOrder.getDumpster().getDumpsterNum()));
			map.put("deliveryDate", anOrder.getFormattedDeliveryDate());
			map.put("pickupDate", anOrder.getFormattedPickupDate());
			map.put("isExchange", (anOrder.isExchangeOrder() ? "Exch" : StringUtils.EMPTY));
			map.put("tonnage", (anOrder.getNetWeightTonnage() != null ? anOrder.getNetWeightTonnage().toPlainString() 
					: StringUtils.EMPTY));
			map.put("dropOffDriverName", (anOrder.getDropOffDriver() != null ? anOrder.getDropOffDriver().getName() 
					: StringUtils.EMPTY));
			map.put("dumpsterLocation", (anOrder.getDumpsterLocation() != null ? anOrder.getDumpsterLocation().getLocationType() 
					: StringUtils.EMPTY));
			
			List<Permit> permits = anOrder.getPermits();
			String permitStr = StringUtils.EMPTY;
			for (Permit aPermit : permits) {
				if (StringUtils.isNotEmpty(aPermit.getNumber()) 
						&& !StringUtils.equals(Permit.EMPTY_PERMIT_NUMBER, aPermit.getNumber())) {
					permitStr += (aPermit.getNumber() + ", ");
				}
			}
			if (StringUtils.isNotEmpty(permitStr)) {
				permitStr = permitStr.substring(0, permitStr.length() - 2);
			}
			map.put("permit", permitStr);
			
			/*ObjectMapper objectMapper = new ObjectMapper();
			String jSonResponse = StringUtils.EMPTY;
			try {
				jSonResponse = objectMapper.writeValueAsString(map);
				System.out.println(jSonResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			reportData.add(map);
		}
		
		String deliveryDateFrom = input.getDeliveryDateFrom();
		params.put("deliveryDateFrom", StringUtils.isEmpty(deliveryDateFrom) ? StringUtils.EMPTY : deliveryDateFrom );
		
		String deliveryDateTo = input.getDeliveryDateTo();
		params.put("deliveryDateTo", StringUtils.isEmpty(deliveryDateTo) ? StringUtils.EMPTY : deliveryDateTo );
		
		String pickupDateFrom = input.getPickupDateFrom();
		params.put("pickupDateFrom", StringUtils.isEmpty(pickupDateFrom) ? StringUtils.EMPTY : pickupDateFrom );
		
		String pickupDateTo = input.getPickupDateTo();
		params.put("pickupDateTo", StringUtils.isEmpty(pickupDateTo) ? StringUtils.EMPTY : pickupDateTo );
		
		params.put("dumpsterSizeAggregation", dumpsterSizeAggregation);
		//params.put("noOfOrders", reportData.size());
		
		return reportData;
	}
	
	@ModelAttribute("modelObject")
	public DeliveryPickupReportVO setupModel(HttpServletRequest request) {
		return new DeliveryPickupReportVO();
	}
}
