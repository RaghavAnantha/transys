package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.model.AdditionalFee;
import com.transys.model.SearchCriteria;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/additionalFee")
public class AdditionalFeeController extends CRUDController<AdditionalFee> {
	
	public AdditionalFeeController() {
		setUrlContext("additionalFee");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(AdditionalFee.class, criteria, "id", null, null));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(AdditionalFee.class, criteria));
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
		model.addAttribute("additionalFees", genericDAO.findByCriteria(AdditionalFee.class, criterias, "id", false));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") AdditionalFee entity,
			BindingResult bindingResult, ModelMap model) {

		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		super.save(request, entity, bindingResult, model);

		return urlContext + "/list";

	}

}
