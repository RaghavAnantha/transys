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
import com.transys.model.DumpsterSize;
import com.transys.model.MaterialCategory;
import com.transys.model.OverweightFee;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/overweightFee")
public class OverweightFeeController extends CRUDController<OverweightFee> {
	public OverweightFeeController() {
		setUrlContext("masterData/overweightFee");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(OverweightFee.class, new AbstractModelEditor(OverweightFee.class));
		binder.registerCustomEditor(DumpsterSize.class, new AbstractModelEditor(DumpsterSize.class));
		binder.registerCustomEditor(MaterialCategory.class, new AbstractModelEditor(MaterialCategory.class));
		super.initBinder(binder);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(OverweightFee.class, criteria, "materialCategory.category", null, null));
		
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(OverweightFee.class, criteria, "materialCategory.category", false));
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
		model.addAttribute("dumpsterSizes", genericDAO.findUniqueByCriteria(DumpsterSize.class, criterias, "id", false));
		model.addAttribute("overweightFee", genericDAO.executeSimpleQuery("select DISTINCT(obj.fee) from OverweightFee obj where obj.deleteFlag='1' order by obj.fee asc"));
		model.addAttribute("materialCategories", genericDAO.findByCriteria(MaterialCategory.class, criterias, "category", false));
		model.addAttribute("tonLimits", genericDAO.executeSimpleQuery("select DISTINCT(obj.tonLimit) from OverweightFee obj where obj.deleteFlag='1' order by obj.tonLimit asc"));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") OverweightFee entity,
			BindingResult bindingResult, ModelMap model) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		super.save(request, entity, bindingResult, model);
		
		model.addAttribute("msgCtx", "manageOverweightFee");
		model.addAttribute("msg", "Overweight Fee saved successfully");
		model.addAttribute("modelObject", new OverweightFee());
		
		return urlContext + "/form";
	}
	
}
