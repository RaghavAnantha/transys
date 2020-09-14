package com.transys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.ui.Model;

import org.springframework.validation.Validator;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

//import com.google.gson.Gson;

import com.transys.core.dao.GenericDAO;

import com.transys.core.util.FormatUtil;
import com.transys.core.util.MimeUtil;
import com.transys.core.util.ReportUtil;

import com.transys.model.Permit;
//import com.transys.model.Language;
import com.transys.model.SearchCriteria;
//import com.transys.model.StaticData;
import com.transys.model.User;

import net.sf.jasperreports.engine.JasperPrint;

//import com.transys.service.AuditService;

@SuppressWarnings("unchecked")
public class BaseController {
	//protected static DecimalFormat decimalFormat = new DecimalFormat("######.000");
	//public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	protected static Logger log = LogManager.getLogger("com.transys.controller");
	
	//@Autowired
	//private AuditService auditService;

	protected String urlContext;

	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}

	// Set up any custom editors, adds a custom one for java.sql.date by default
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(FormatUtil.inputDateFormat, false));
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	@Autowired
	protected GenericDAO genericDAO;

	public GenericDAO getGenericDAO() {
		return genericDAO;
	}

	@Autowired
	protected Validator validator;

	/**
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	protected User getUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute("userInfo");
	}

	/*protected List<StaticData> listStaticData(String staticDataType) {
		Map criteria = new HashMap();
		criteria.put("dataType", staticDataType);
		return genericDAO.findByCriteria(StaticData.class, criteria);
	}

	protected void setLanguageAttributes(HttpServletRequest request,
			Language language) {
		request.getSession().setAttribute("lang", language.getLocale());
		if (language.getRtl() == true) {
			request.getSession().setAttribute("dir", "rtl");
			request.getSession().setAttribute("right", "left");
			request.getSession().setAttribute("left", "right");
		} else {
			request.getSession().setAttribute("dir", "ltr");
			request.getSession().setAttribute("right", "right");
			request.getSession().setAttribute("left", "left");
		}
	}*/

	protected void populateSearchCriteria(HttpServletRequest request, Map<String, String[]> params) {
		populateSearchCriteria(request, params, "searchCriteria");
	}
	
	protected void populateSearchCriteria(HttpServletRequest request,
			Map<String, String[]> params, String searchCriteriaName) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute(searchCriteriaName);
		if (criteria == null) {
			criteria = new SearchCriteria();
			criteria.setPageSize(25);
		}
		
		if (StringUtils.isNotEmpty(request.getParameter("rst"))) {
			criteria.getSearchMap().clear();
		}
		
		criteria.setRequestParams(params);
		if (params != null && params.size()>0) {
			Map parameters = new HashMap();
			if (params.get("pageSize") != null) {
				criteria.setPageSize(Integer.parseInt(params.get("pageSize")[0]));
			}
			if (params.get("search") == null
					&& request.getParameter("p") != null) {
				criteria.setPage(Integer.parseInt(request.getParameter("p")));
			} else {
				if (params.get("queryString") != null) {
						parameters.put(params.get("searchBy")[0],
								params.get("queryString")[0]);
					} else {
						Object[] keys = params.keySet().toArray();
						for (int i = 0; i < keys.length; i++) {
							if (params.get(keys[i]) != null) {
								if (!"rst".equalsIgnoreCase(keys[i].toString()))
									parameters.put(keys[i],
											params.get(keys[i])[0]);
							}
						}
					}
				
				criteria.setPage(0);
				criteria.setSearchMap(parameters);
			}
		}
		
		request.getSession().setAttribute("searchCriteria", criteria);
		updateEffectiveDateInSearch(request);
	}

	protected String getCriteriaAsString(SearchCriteria criteria) {
		StringBuffer buffer = new StringBuffer("");
		if (criteria != null) {
			Iterator<String> it = criteria.getSearchMap().keySet().iterator();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = it.next();
				Object obj = criteria.getSearchMap().get(key);
				if (obj instanceof String) {
					value = (String) criteria.getSearchMap().get(key);
				}
				//value = (String) criteria.getSearchMap().get(key);
				if (!StringUtils.isEmpty(value)) {
					buffer.append(key + ":" + value).append(" ");
				}
			}
		}
		return buffer.toString();
	}
	
	protected String setReportRequestHeaders(HttpServletResponse response, String type, String reportName) {
		if (StringUtils.isEmpty(type)) {
			type = "xlsx";
		}
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename= " + reportName + "." + type);
		}
		
		response.setContentType(MimeUtil.getContentType(type));
		return type;
	}
	
	protected void updateEffectiveDateInSearch(HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		if (searchMap.containsKey("effectiveStartDate")) {
			searchMap.put("effectiveStartDate", ">=" + searchMap.get("effectiveStartDate"));
			System.out.println("Setting effective start date");
		}
		
		if (searchMap.containsKey("effectiveEndDate")) {
			searchMap.put("effectiveEndDate", "<=" + searchMap.get("effectiveEndDate"));
			System.out.println("Setting effective end date");
		}
		
		criteria.setSearchMap(searchMap);
	}
	
	protected void resetEffectiveDateInSearch(HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		if (searchMap.containsKey("effectiveStartDate")) {
			if (searchMap.get("effectiveStartDate").toString().startsWith(">=") || searchMap.get("effectiveStartDate").toString().startsWith("<=")) {
				searchMap.put("effectiveStartDate", searchMap.get("effectiveStartDate").toString().substring(2));
				System.out.println("Resetting effective start date");
			}
		}
		
		if (searchMap.containsKey("effectiveEndDate")) {
			if (searchMap.get("effectiveEndDate").toString().startsWith(">=") || searchMap.get("effectiveEndDate").toString().startsWith("<=")) {
				searchMap.put("effectiveEndDate", searchMap.get("effectiveEndDate").toString().substring(2));
				System.out.println("Resetting effective end date");
			}
		}
		
		criteria.setSearchMap(searchMap);
	}
	
	protected void resetEmptyPermitNumberInSearch(HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		if (searchMap.containsKey("number") && searchMap.get("number").toString().equalsIgnoreCase("null")) {
			searchMap.put("number", Permit.EMPTY_PERMIT_NUMBER);
			System.out.println("Resetting Permit Number");
		}
		
		criteria.setSearchMap(searchMap);
	}

	protected void setErrorMsg(HttpServletRequest request, String msg) {
		request.getSession().setAttribute("error", msg);
	}
	
	protected void setSuccessMsg(HttpServletRequest request, String msg) {
		request.getSession().setAttribute("msg", msg);
	}
	
	/*public void writeActivityLog(String activityType, String details) {
		auditService.writeActivityLog(urlContext, activityType, details);
	}*/
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/ajax.do")
	public @ResponseBody String ajaxRequest(HttpServletRequest request,
			@RequestParam(value = "action", required = true) String action, Model model) {
		return processAjaxRequest(request, action, model);
	}

	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) {
		if ("removeJasperPrint".equalsIgnoreCase(action)) {
			return removeJasperPrint(request);
		}
		
		return StringUtils.EMPTY;
	}
	
	protected String removeJasperPrint(HttpServletRequest request) {
		return ReportUtil.removeJasperPrint(request, getClass().getName());
	}
	
	protected void addJasperPrint(HttpServletRequest request, JasperPrint jp) {
		ReportUtil.addJasperPrint(request, jp, getClass().getName());
	}
	
	protected void addWaterMarkRendererReportParam(Map<String, Object> params, boolean display, String displayText) {
		ReportUtil.addWaterMarkRendererReportParam(params, display, displayText);
	}
}
