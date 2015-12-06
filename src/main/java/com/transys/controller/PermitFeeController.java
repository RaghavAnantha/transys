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
import com.transys.model.AdditionalFee;
import com.transys.model.DumpsterPrice;
import com.transys.model.OverweightFee;
import com.transys.model.PermitClass;
import com.transys.model.PermitFee;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/permitFee")
public class PermitFeeController extends CRUDController<PermitFee> {
	public PermitFeeController() {
		setUrlContext("masterData/permitFee");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(PermitClass.class, new AbstractModelEditor(PermitClass.class));
		binder.registerCustomEditor(PermitType.class, new AbstractModelEditor(PermitType.class));
		
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(PermitFee.class, criteria, "permitClass", null, null));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(PermitFee.class, criteria, "permitClass", false));
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
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(PermitFee.class, criteria, "permitClass", false));
		
		return urlContext + "/list";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("permitFees", genericDAO.executeSimpleQuery("select DISTINCT(obj.fee) from PermitFee obj where obj.deleteFlag='1' order by obj.fee asc"));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") PermitFee entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		model.addAttribute("msgCtx", "managePermitFee");
		
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Permit Fee saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new PermitFee());
		}
				
		return urlContext + "/form";
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = "Error occured while saving permit fee";
		return errorMsg;
	}
}
