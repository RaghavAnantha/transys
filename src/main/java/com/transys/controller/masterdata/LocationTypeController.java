package com.transys.controller.masterdata;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.controller.CRUDController;
import com.transys.model.LocationType;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/locationType")
public class LocationTypeController extends CRUDController<LocationType> {

	public LocationTypeController() {
		setUrlContext("masterData/locationType");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(LocationType.class, criteria, "locationType", null, null));
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		model.addAttribute("list", genericDAO.search(LocationType.class, criteria, "locationType", false));
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
		model.addAttribute("locationTypes", genericDAO.findByCriteria(LocationType.class, criterias, "locationType", false));
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") LocationType entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		model.addAttribute("msgCtx", "manageLocationTypes");
		
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Location Type saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new LocationType());
		}
				
		return urlContext + "/form";
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "location")) {
			errorMsg = "Duplicate location type - location type already exists"; 
		} else {
			errorMsg = "Error occured while saving location type";
		}
		
		return errorMsg;
	}
}
