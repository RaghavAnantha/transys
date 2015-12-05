package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.model.CityFee;
import com.transys.model.DumpsterPrice;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/cityFee")
public class CityFeeController extends CRUDController<CityFee> {
	public CityFeeController() {
		setUrlContext("masterData/cityFee");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(CityFee.class, criteria, "suburbName", null, null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(CityFee.class, criteria, "suburbName", null, null));
		
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(CityFee.class, criteria, "suburbName", false));
		cleanUp(request);
		
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		return urlContext + "/form";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("cityFees", genericDAO.findByCriteria(CityFee.class, criterias, "suburbName", false));
		model.addAttribute("uniqueCityFees", genericDAO.executeSimpleQuery("select DISTINCT(obj.fee) from CityFee obj where obj.deleteFlag='1' order by obj.fee asc"));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") CityFee entity,
			BindingResult bindingResult, ModelMap model) {
		super.save(request, entity, bindingResult, model);
		
		model.addAttribute("msgCtx", "manageCityFee");
		model.addAttribute("msg", "City Fee saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new CityFee());
		}

		return urlContext + "/form";
	}
}