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

import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.DumpsterPrice;
import com.transys.model.DumpsterSize;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/dumpsterPrice")
public class DumpsterPriceController extends CRUDController<DumpsterPrice> {

	public DumpsterPriceController() {
		setUrlContext("dumpsterPrice");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(DumpsterPrice.class, new AbstractModelEditor(DumpsterPrice.class));
		binder.registerCustomEditor(DumpsterSize.class, new AbstractModelEditor(DumpsterSize.class));
		binder.registerCustomEditor(MaterialType.class, new AbstractModelEditor(MaterialType.class));
		super.initBinder(binder);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(DumpsterPrice.class, criteria, "id", null, null));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(DumpsterPrice.class, criteria));
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
		model.addAttribute("dumpsterSizes", genericDAO.findUniqueByCriteria(DumpsterSize.class, criterias, "size", false));
		model.addAttribute("materialTypes", genericDAO.findByCriteria(MaterialType.class, criterias, "id", false));
		model.addAttribute("dumpsterPrices", genericDAO.findUniqueByCriteria(DumpsterPrice.class, criterias, "id", false));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") DumpsterPrice entity,
			BindingResult bindingResult, ModelMap model) {

		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		super.save(request, entity, bindingResult, model);

		return urlContext + "/list";

	}

}
