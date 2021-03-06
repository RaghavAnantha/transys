package com.transys.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import com.transys.core.util.ServletUtil;

import com.transys.model.AbstractBaseModel;
import com.transys.model.BaseModel;
import com.transys.model.Permit;
//import com.transys.model.Language;
import com.transys.model.SearchCriteria;
//import com.transys.model.StaticData;
import com.transys.model.User;

import com.transys.service.DynamicReportService;

import net.sf.jasperreports.engine.JasperPrint;

//import com.transys.service.AuditService;

@SuppressWarnings("unchecked")
public class BaseController {
	protected static Logger log = LogManager.getLogger("com.transys.controller");
	
	protected static final String MODEL_OBJECT_KEY = "modelObject";
	protected static final String NOTES_MODEL_OBJECT_KEY = "notesModelObject";
	
	protected static final String MSG_CTX_KEY = "msgCtx";
	protected static final String ERROR_CTX_KEY = "errorCtx";
	protected static final String URL_CTX_KEY = "urlContext";
	
	protected static final String MSG_KEY = "msg";
	protected static final String ERROR_KEY = "error";
	
	protected static final String ACTIVE_TAB_KEY = "activeTab";
	protected static final String ACTIVE_SUB_TAB_KEY = "activeSubTab";
	protected static final String MODE_KEY = "mode";
	
	protected static final String MODE_ADD = "ADD";
	protected static final String MODE_MANAGE = "MANAGE";
	
	protected static final String MANAGE_DOCS_TAB = "manageDocs";
	
	protected static final String DOCS_UPLOAD_DIR = "/transys/storage";
	
	protected static final String searchCriteriaKey = "searchCriteria";
	
	@Autowired
	protected DynamicReportService dynamicReportService;
	//@Autowired
	//protected AuditService auditService;

	protected String urlContext;
	protected String messageCtx;
	
	protected String reportName;
	protected String subReportName;
	
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getSubReportName() {
		return subReportName;
	}

	public void setSubReportName(String subReportName) {
		this.subReportName = subReportName;
	}

	public String getMessageCtx() {
		return messageCtx;
	}

	public void setMessageCtx(String messageCtx) {
		this.messageCtx = messageCtx;
	}
	
	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
	}
	
	public String getReportsUrlContext() {
		return "reports";
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

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	protected void addCtx(ModelMap model) {
		model.addAttribute(MSG_CTX_KEY, getMessageCtx());
		model.addAttribute(ERROR_CTX_KEY, getMessageCtx());
		model.addAttribute(URL_CTX_KEY, getUrlContext());
	}
	
	protected void addCtx(ModelMap model, String msgCtx, String urlCtx) {
		model.addAttribute(MSG_CTX_KEY, msgCtx);
		model.addAttribute(ERROR_CTX_KEY, msgCtx);
		model.addAttribute(URL_CTX_KEY, urlCtx);
	}
	
	protected void addMsgCtx(ModelMap model) {
		model.addAttribute(MSG_CTX_KEY, getMessageCtx());
		model.addAttribute(ERROR_CTX_KEY, getMessageCtx());
	}
	
	protected void addMsgCtx(ModelMap model, String msgCtx) {
		model.addAttribute(MSG_CTX_KEY, msgCtx);
		model.addAttribute(ERROR_CTX_KEY, msgCtx);
	}

	protected User getUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute("userInfo");
	}
	
	protected Long getUserId(HttpServletRequest request) {
		User user = getUser(request);
		return user.getId();
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
	
	
	protected void clearSearchCriteria(HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
	}

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
		if (params != null && params.size() > 0) {
			Map parameters = new HashMap();
			if (params.get("pageSize") != null) {
				criteria.setPageSize(Integer.parseInt(params.get("pageSize")[0]));
			}
			if (params.get("search") == null
					&& request.getParameter("p") != null) {
				criteria.setPage(Integer.parseInt(request.getParameter("p")));
			} else {
				if (params.get("queryString") != null) {
						parameters.put(params.get("searchBy")[0], params.get("queryString")[0]);
					} else {
						Object[] keys = params.keySet().toArray();
						for (int i = 0; i < keys.length; i++) {
							if (params.get(keys[i]) != null) {
								if (!"rst".equalsIgnoreCase(keys[i].toString()))
									parameters.put(keys[i], params.get(keys[i])[0]);
							}
						}
					}
				
				criteria.setPage(0);
				criteria.setSearchMap(parameters);
			}
		}
		
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		updateEffectiveDateInSearch(criteria);
		
		request.getSession().setAttribute("searchCriteria", criteria);
	}
	
	protected SearchCriteria getSearchCriteria(HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute(searchCriteriaKey);
		return criteria;
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
	
	protected void updateEffectiveDateInSearch(SearchCriteria criteria) {
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		if (searchMap.containsKey("effectiveStartDate")) {
			searchMap.put("effectiveStartDate", ">=" + searchMap.get("effectiveStartDate"));
			System.out.println("Setting effective start date");
		}
		
		if (searchMap.containsKey("effectiveEndDate")) {
			searchMap.put("effectiveEndDate", "<=" + searchMap.get("effectiveEndDate"));
			System.out.println("Setting effective end date");
		}
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
	}
	
	protected void resetEmptyPermitNumberInSearch(HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		if (searchMap.containsKey("number") && searchMap.get("number").toString().equalsIgnoreCase("null")) {
			searchMap.put("number", Permit.EMPTY_PERMIT_NUMBER);
			System.out.println("Resetting Permit Number");
		}
	}
	
	protected String processError(Throwable t, String event, HttpServletRequest request) {
		t.printStackTrace();
		log.warn("Exception while " + event + ":" + t);
		
		List<String> errors = new ArrayList<String>();
		errors.add(t.getMessage());
		request.getSession().setAttribute("error", errors);
		
		return "error";
	}

	protected void setErrorMsg(HttpServletRequest request, HttpServletResponse response, String msg) {
		/*if (response != null) {
			response.setContentType(MimeUtil.getContentType("html"));
		}*/
		request.getSession().setAttribute(ERROR_KEY, msg);
	}
	
	protected void setErrorMsg(HttpServletRequest request, String msg) {
		setErrorMsg(request, null, msg);
	}
	
	protected void setSuccessMsg(HttpServletRequest request, String msg) {
		request.getSession().setAttribute(MSG_KEY, msg);
	}
	
	protected void setReportFreezeRow(Map<String, Object> params, String type, String rowNo) {
		if (params == null || StringUtils.isEmpty(type) || StringUtils.isEmpty(rowNo)) {
			return;
		}
		
		if (StringUtils.equalsIgnoreCase("xlsx", type) || StringUtils.equalsIgnoreCase("xls", type)) {
			params.put(ReportUtil.FREEZE_ROW_NO_KEY, rowNo);
		}
	}
	
	protected void setExcludeCsvHeader(Map<String, Object> params, boolean exclude) {
		params.put(ReportUtil.EXCLUDE_CSV_HEADER, exclude);
	}
	
	/*public void writeActivityLog(String activityType, String details) {
		auditService.writeActivityLog(urlContext, activityType, details);
	}*/
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/ajax.do")
	public @ResponseBody String ajaxRequest(HttpServletRequest request,
			@RequestParam(value = "action", required = true) String action, Model model) {
		return processAjaxRequest(request, action, model);
	}

	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
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
	
	protected void addMsg(ModelMap model, String msgCtx, String msg) {
		model.addAttribute(MSG_CTX_KEY, msgCtx);
		model.addAttribute(MSG_KEY, msg);
	}
	
	protected void addError(ModelMap model, String errorCtx, String error) {
		model.addAttribute(ERROR_CTX_KEY, errorCtx);
		model.addAttribute(ERROR_KEY, error);
	}
	
	protected void addModelObject(ModelMap model, BaseModel modelObj) {
		model.addAttribute(MODEL_OBJECT_KEY, modelObj);
	}
	
	protected void addNotesModelObject(ModelMap model, BaseModel notesModelObj) {
		model.addAttribute(NOTES_MODEL_OBJECT_KEY, notesModelObj);
	}
	
	protected void addTabAttributes(ModelMap model, String activeTab, String mode, String activeSubTab) {
		model.addAttribute(ACTIVE_TAB_KEY, activeTab);
		
		String modeToBeUsed = StringUtils.defaultIfEmpty(mode, MODE_ADD);
		model.addAttribute(MODE_KEY, modeToBeUsed);
		
		if (StringUtils.isNotEmpty(activeSubTab)) {
			model.addAttribute(ACTIVE_SUB_TAB_KEY, activeSubTab);
		}
	}
	
	protected void setModifier(HttpServletRequest request, AbstractBaseModel entity) {
		long userId = getUserId(request);
		Date currentTime = Calendar.getInstance().getTime();
		
		if (entity.getId() == null) {
			entity.setCreatedAt(currentTime);
			if (entity.getCreatedBy() == null) {
				entity.setCreatedBy(userId);
			}
		} else {
			entity.setModifiedAt(currentTime);
			//if (entity.getModifiedBy() == null) {
				entity.setModifiedBy(userId);
			//}
		}
	}
	
	protected User getUser(Long id) {
		return genericDAO.getById(User.class, id);
	}
	
	protected void addRDSBillingInfo(Map<String, Object> params) {
		params.put("rdsName", "Ravenswood Disposal Services, Inc");
		
		String rdsBillingAddress = "6660 S Nashville"
				+ "\n" + "Bedford Park, IL 60638";
		params.put("rdsBillingAddress", rdsBillingAddress);
		params.put("rdsContact", "773-722-4444");
	}
	
	protected void addData(Map<String,Object> datas, Object dataObj, Map<String, Object> params) {
		datas.put("data", dataObj);
		datas.put("params", params);
	}
	
	protected void resetSearchCriteria(HttpServletRequest request) {
		SearchCriteria searchCriteria = getSearchCriteria(request);
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
	}

	protected void addLogoFilePath(HttpServletRequest request, Map<String, Object> params) {
		String logoFilePath = ServletUtil.getLogoFilePath(request);
		params.put("LOGO_FILE_PATH", logoFilePath);
	}
}
