package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.transys.core.util.MimeUtil;
import com.transys.model.DumpsterInfo;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/report")
public class ReportController extends CRUDController<DumpsterInfo> {
	
	public ReportController(){	
		setUrlContext("report");
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("dumpsterInfo", genericDAO.executeSimpleQuery("select obj from DumpsterInfo obj where obj.id!=0 order by obj.id asc"));
		
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"id",null,null));
		
		return urlContext + "/report";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "id", null, null));
		return urlContext + "/report";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		
		try {
		
		StringBuffer query= new StringBuffer("select obj from DumpsterInfo obj where obj.id!=0 order by obj.id asc");
		
		if (StringUtils.isEmpty(type))
			type = "xlsx";
		if (!type.equals("html") && !(type.equals("print"))) {
			response.setHeader("Content-Disposition",
					"attachment;filename= dumpsterOnsiteReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("dumpsterSize", "dumpsterSize");
		params.put("dumpsterNum", "dumpsterNum");
		params.put("status", "status");
		
		System.out.println("******* The query is "+query);
		List<DumpsterInfo> list = genericDAO.executeSimpleQuery(query.toString());
		
		if (!type.equals("print") && !type.equals("pdf")) {
			out = dynamicReportService.generateStaticReport("dumpsterOnsiteReport",
					list, params, type, request);
		}
		else if(type.equals("pdf")){
			out = dynamicReportService.generateStaticReport("dumpsterOnsiteReportPdf",
					list, params, type, request);
		}
		else {
			out = dynamicReportService.generateStaticReport("dumpsterOnsiteReport"+"print",
					list, params, type, request);
		}
	
		out.writeTo(response.getOutputStream());
		out.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		log.warn("Unable to create file :" + e);
		request.getSession().setAttribute("errors", e.getMessage());
		
	}
	}
	

}
