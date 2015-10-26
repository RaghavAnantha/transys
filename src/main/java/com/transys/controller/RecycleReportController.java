package com.transys.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.vo.MaterialIntakeReportVO;

@Controller
@RequestMapping("/reports/recycleReport")
public class RecycleReportController extends CRUDController<Order> {

	public RecycleReportController() {	
		setUrlContext("reports/recycleReport");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<?> reportData =  null; // retrieveReportData(criteria);
		model.addAttribute("list", reportData);
		
		return urlContext + "/list";
	}
	
	/*private List<?> retrieveReportData(SearchCriteria criteria) {
		// select SUM(NetTonnage), materialType from Orders group by materialType 
		// select SUM(NetTonnage), materialType from PublicMaterialIntake group by materialType
		
	}*/
}
