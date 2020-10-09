package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.transys.core.util.ModelUtil;
import com.transys.model.Dumpster;
import com.transys.model.DumpsterSize;

import com.transys.model.Order;
import com.transys.model.SearchCriteria;

import com.transys.service.DynamicReportService;

@Controller
@RequestMapping("/reports/dumpsterRentedReport")
public class DumpsterRentedReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;
	
	public DumpsterRentedReportController() {	
		setUrlContext("reports/dumpsterRentedReport");
		setReportName("dumpsterRentedReport");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		
		return urlContext + "/list";
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		model.addAttribute("msgCtx", "dumpsterRentedReport");
		model.addAttribute("errorCtx", "dumpsterRentedReport");
		
		String query = "select obj from DumpsterStatus obj where obj.status = 'Dropped Off' and obj.deleteFlag='1'";
		model.addAttribute("dumpsterStatus", genericDAO.executeSimpleQuery(query));
		model.addAttribute("dumpsterSizes", genericDAO.findAll(DumpsterSize.class, true));
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(750);
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Dumpster> dumpsterList = performSearch(model, request, criteria, params);
			
			String type = "html";
			setReportRequestHeaders(response, type, reportName);
			out = dynamicReportService.generateStaticReport(getReportName(), dumpsterList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to create file: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Dumpster Rented report: " + t);
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(0);
		criteria.setPageSize(2500);
		
		Map<String, Object> params = new HashMap<String, Object>();
		ByteArrayOutputStream out = null;
		try {
			List<Dumpster> dumpsterList = performSearch(model, request, criteria, params);
			
			type = setReportRequestHeaders(response, type, getReportName());
			setReportFreezeRow(params, type, "7");
			out = dynamicReportService.generateStaticReport(getReportName(), dumpsterList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating Dumpster Rented report: " + t);
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
	
	private List<Dumpster> performSearch(ModelMap model, HttpServletRequest request, 
			SearchCriteria criteria, Map<String, Object> params) {
		Map<String, Object> criteriaMap = (Map<String, Object>)criteria.getSearchMap();
		
		String status = (String)criteriaMap.get("status");
		if (StringUtils.isEmpty(status)) {
			criteriaMap.put("status.status", "Dropped Off");
		}
		List<Dumpster> dumpsterList = genericDAO.search(Dumpster.class, criteria, "dumpsterSize.size asc, dumpsterNum asc", null, null);
		criteriaMap.remove("status.status", "Dropped Off");
		
		populateDeliveryDetailsForDumpster(dumpsterList);
		
		String dumpsterSize = (String)criteriaMap.get("dumpsterSize");
		params.put("dumpsterSize", ModelUtil.retrieveDumpsterSize(genericDAO, dumpsterSize));
		params.put("status", ModelUtil.retrieveDumpsterStatus(genericDAO, status));
	
		return dumpsterList;
	}
	
	private void populateDeliveryDetailsForDumpster(List<Dumpster> dumpsterInfoList) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("orderStatus.status", "Dropped Off");
		for (Dumpster aDumpster : dumpsterInfoList) {
			// Get the latest delivery address & delivery date for the corresponding dumpster# from transysOrder table
			criterias.put("dumpster.id", aDumpster.getId());
			List<Order> ordersForDumpster = genericDAO.findByCriteria(Order.class, criterias, "id", true);
			log.info("List of orders for this dumpster = " + ordersForDumpster.size());
			if (ordersForDumpster.isEmpty()) {
				continue;
			}
			
			Order anOrderForDumpster = ordersForDumpster.get(0);
			aDumpster.setOrderId(anOrderForDumpster.getId());
			aDumpster.setDeliveryAddress(anOrderForDumpster.getDeliveryAddress().getFullLine());
			aDumpster.setDeliveryDate(anOrderForDumpster.getFormattedDeliveryDate());
		}
	}
	
	/*
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<Dumpster> reportData = prepareReportData(model, request);
			type = setReportRequestHeaders(response, type, "dumpsterRentedReport");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("xls")) {
				Map<String, String> headers = new LinkedHashMap<>();
				headers.put("Dumpster Size", "dumpsterSize");
				headers.put("Dumpster#", "dumpsterNum");
				headers.put("Delivery Address", "deliveryAddress");
				headers.put("Delivery Date", "deliveryDate");
				headers.put("Status", "status");
				
				ExcelReportGenerator reportGenerator = new ExcelReportGenerator();
				reportGenerator.setTitleMergeCellRange("$A$1:$F$1");
				out = reportGenerator.exportReport("Dumpster Rented Report", headers, reportData, true);
				
			} else if (type.equals("pdf")) {
				List<Map<String, Object>> reportDataCollection = getReportDataAsCollection(reportData);
				
				out = new ByteArrayOutputStream();
				Map<String, Object> params = new HashMap<String, Object>();

				out = dynamicReportService.generateStaticReport("dumpsterRentedReport", reportDataCollection, params, type, request);
			}
			
			out.writeTo(response.getOutputStream());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}
	
	private List<Dumpster> prepareReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<Dumpster> dumpsterInfoList = genericDAO.search(Dumpster.class, criteria, "id", null, null);
		setDeliveryDetailsForDumpster(dumpsterInfoList);
		
		return dumpsterInfoList;
	}
	
	private List<Map<String, Object>> getReportDataAsCollection(List<Dumpster> dumpsterInfoList) {
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Dumpster aDumpster : dumpsterInfoList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dumpsterSize", aDumpster.getDumpsterSize().getSize());
			map.put("dumpsterNum", aDumpster.getDumpsterNum());
			map.put("status", aDumpster.getStatus().getStatus());
			map.put("deliveryAddress", StringUtils.defaultIfEmpty(aDumpster.getDeliveryAddress(), StringUtils.EMPTY));
			map.put("deliveryDate", StringUtils.defaultIfEmpty(aDumpster.getDeliveryDate(), StringUtils.EMPTY));
			
			//String jsonResponse = JsonUtil.toJson(map);
			//log.info(map);
			
			reportData.add(map);
		}
		return reportData;
	}*/
}
