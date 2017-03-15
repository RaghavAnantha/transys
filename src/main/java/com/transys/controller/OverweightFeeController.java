package com.transys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.Dumpster;
import com.transys.model.DumpsterSize;
import com.transys.model.MaterialCategory;
import com.transys.model.OverweightFee;
import com.transys.model.PermitFee;
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
		model.addAttribute("list", genericDAO.search(OverweightFee.class, criteria, "materialCategory.category, effectiveStartDate desc", null, null));
		
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(OverweightFee.class, criteria,"materialCategory.category, effectiveStartDate desc", null, null));
		
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(OverweightFee.class, criteria, "materialCategory.category, effectiveStartDate desc", null, null));
		
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
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") OverweightFee entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		model.addAttribute("msgCtx", "manageOverweightFee");
		
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Overweight Fee saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new OverweightFee());
		}
				
		return urlContext + "/form";
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = "Error occured while saving overweight fee";
		return errorMsg;
	}
}
