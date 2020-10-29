package com.transys.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.transys.controller.BaseController;
import com.transys.core.util.ReportUtil;
import com.transys.model.SearchCriteria;

public abstract class ReportController extends BaseController {
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		
		processDisplayMain(model, request, searchCriteria);
		
		return urlContext + "/list";
	}
	
	protected void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		addCtx(model);
	}
	
	protected void processDisplayMain(ModelMap model, HttpServletRequest request, SearchCriteria searchCriteria) {
	}
	
	protected int getCriteriaPage() {
		return 0;
	}
	
	protected int getCriteriaSearchPageSize() {
		return 1500;
	}
	
	protected int getCriteriaExportPageSize() {
		return 2500;
	}
	
	protected String getReportFreezeRow() {
		return null;
	}
	
	protected Map<String, Object> generateReportData(ModelMap model, HttpServletRequest request, SearchCriteria criteria) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<?> data = performSearch(model, request, criteria, params);
		
		Map<String, Object> reportData = new HashMap<String, Object>();
		reportData.put(ReportUtil.dataKey, data);
		reportData.put(ReportUtil.paramsKey, params);
		
		return reportData;
	}
	
	protected abstract List<?> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria,
			Map<String, Object> params);
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(getCriteriaPage());
		criteria.setPageSize(getCriteriaSearchPageSize());
		
		String reportName = getReportName();
		String subReportName = getSubReportName();
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> reportData = generateReportData(model, request, criteria);
			
			if (StringUtils.isEmpty(reportName)) {
				setupList(model, request);
				return urlContext + "/list";
			}
			
			Map<String, Object> params = (Map<String, Object>)reportData.get(ReportUtil.paramsKey);
			List<?> data = (List<?>)reportData.get(ReportUtil.dataKey);
			
			String type = "html";
			setReportRequestHeaders(response, type, reportName);
			
			if (data.isEmpty()) {
				return "report.empty";
			}
			
			if (StringUtils.isNotEmpty(subReportName)) {
				out = dynamicReportService.generateStaticMasterSubReport(reportName, subReportName, data, params, 
						type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportName, data, params, type, request);
			}
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int originalPage = criteria.getPage();
		int originalPageSize = criteria.getPageSize();
		criteria.setPage(getCriteriaPage());
		criteria.setPageSize(getCriteriaExportPageSize());
		
		String reportName = getReportName();
		String subReportName = getSubReportName();
		ByteArrayOutputStream out = null;
		try {
			Map<String, Object> reportData = generateReportData(model, request, criteria);
			Map<String, Object> params = (Map<String, Object>)reportData.get(ReportUtil.paramsKey);
			List<?> data = (List<?>)reportData.get(ReportUtil.dataKey);
			
			type = setReportRequestHeaders(response, type, getReportName());
			String reportFreezeRow = getReportFreezeRow();
			if (StringUtils.isNotEmpty(reportFreezeRow)) {
				setReportFreezeRow(params, type, reportFreezeRow);
			}
			
			if (StringUtils.isNotEmpty(subReportName)) {
				out = dynamicReportService.generateStaticMasterSubReport(reportName, subReportName, data, params, 
						type, request);
			} else {
				out = dynamicReportService.generateStaticReport(reportName, data, params, type, request);
			}
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			log.warn("Unable to generate report: " + t);
			
			setErrorMsg(request, response, "Exception occured while generating report: " + t);
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
}
