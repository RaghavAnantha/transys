package com.transys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.controller.editor.AbstractModelEditor;
import com.transys.model.PublicMaterialIntake;
import com.transys.model.BaseModel;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.OrderNotes;
import com.transys.model.Permit;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/publicMaterialIntake")
public class PublicMaterialIntakeController extends CRUDController<PublicMaterialIntake> {

	public PublicMaterialIntakeController(){	
		setUrlContext("publicMaterialIntake");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(MaterialCategory.class, new AbstractModelEditor(MaterialCategory.class));
		binder.registerCustomEditor(MaterialType.class, new AbstractModelEditor(MaterialType.class));
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(PublicMaterialIntake.class, criteria, "id", null, null));
		
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(PublicMaterialIntake.class, criteria, "id", false));
		
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
		
		model.addAttribute("materialCategories", genericDAO.findByCriteria(MaterialCategory.class, criterias, "id", false));
		
		//model.addAttribute("materialTypes", genericDAO.findByCriteria(MaterialType.class, criterias, "id", false));
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Object materialCategoryObj = criteria.getSearchMap().get("materialType.materialCategory");
		if (materialCategoryObj != null) {
			String materialCategoryId = materialCategoryObj.toString();
			if (StringUtils.isNotEmpty(materialCategoryId)) {
				String query = "select obj from MaterialType obj where obj.materialCategory.id=" + materialCategoryId;
				List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(query);
				model.addAttribute("materialTypes", materialTypeList);
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/save.do")
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") PublicMaterialIntake entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			System.out.println("Error in validation " + e);
			log.warn("Error in validation :" + e);
		}
		
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError e : errors) {
				System.out.println("Error: " + e.getDefaultMessage());
			}
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		setupCreate(model, request);
		
		String query = "select obj from MaterialType obj where obj.id=" + entity.getMaterialType().getId();
		List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(query);
		entity.setMaterialType(materialTypeList.get(0));
		
		query = "select obj from MaterialType obj where obj.materialCategory.id=" + entity.getMaterialType().getMaterialCategory().getId();
		List<MaterialType> materialTypesForSelMatCat = genericDAO.executeSimpleQuery(query);
		model.addAttribute("materialTypes", materialTypesForSelMatCat);
		
		model.addAttribute("msgCtx", "managePublicMaterialIntake");
		model.addAttribute("msg", "Public material intake saved successfully");

		return urlContext + "/form";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		PublicMaterialIntake publicMaterialIntakeToBeEdited = (PublicMaterialIntake)model.get("modelObject");
		
		String query = "select obj from MaterialType obj where obj.materialCategory.id=" + publicMaterialIntakeToBeEdited.getMaterialType().getMaterialCategory().getId();
		List<MaterialType> materialTypesForSelMatCat = genericDAO.executeSimpleQuery(query);
		model.addAttribute("materialTypes", materialTypesForSelMatCat);
		
		return urlContext + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/retrieveMaterialTypes.do")
	public @ResponseBody String retrieveMaterialTypes(ModelMap model, HttpServletRequest request,
															 @RequestParam(value = "materialCategoryId") Long materialCategoryId) {
		List<MaterialType> materialTypes = retrieveMaterialTypes(materialCategoryId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = StringUtils.EMPTY;
		try {
			json = objectMapper.writeValueAsString(materialTypes);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		//String json = (new Gson()).toJson(permitList);
		//return json;
	}
	
	private List<MaterialType> retrieveMaterialTypes(Long materialCategoryId) {
		String query = "select obj from MaterialType obj where";
		query	+= " obj.materialCategory.id=" + materialCategoryId;
		List<MaterialType> materialTypes = genericDAO.executeSimpleQuery(query);
		return materialTypes;
	}
}
