package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.transys.core.tags.IColumnTag;
import com.transys.core.util.MimeUtil;
import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.DumpsterPrice;
import com.transys.model.DumpsterSize;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.RecycleLocation;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/masterData/dumpsterPrice")
public class DumpsterPriceController extends CRUDController<DumpsterPrice> {
	public DumpsterPriceController() {
		setUrlContext("masterData/dumpsterPrice");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Customer.class, new AbstractModelEditor(Customer.class));
		binder.registerCustomEditor(DumpsterSize.class, new AbstractModelEditor(DumpsterSize.class));
		binder.registerCustomEditor(MaterialType.class, new AbstractModelEditor(MaterialType.class));
		binder.registerCustomEditor(MaterialCategory.class, new AbstractModelEditor(MaterialCategory.class));
		super.initBinder(binder);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<DumpsterPrice> dumpsterPriceList = retrieveDusmpsterPrice(criteria);
		model.addAttribute("list", dumpsterPriceList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<DumpsterPrice> dumpsterPriceList = retrieveDusmpsterPrice(criteria);
		model.addAttribute("list", dumpsterPriceList);
		
		return urlContext + "/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		// TODO:
		criteria.getSearchMap().remove("_csrf");
		criteria.setPageSize(25);
		
		List<DumpsterPrice> dumpsterPriceList = retrieveDusmpsterPrice(criteria);
		model.addAttribute("list", dumpsterPriceList);
		cleanUp(request);
		
		return urlContext + "/list";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		
		DumpsterPrice dumpsterPriceToBeEdited = (DumpsterPrice)model.get("modelObject");
		
		String query = "select obj from MaterialType obj where obj.deleteFlag='1' and obj.materialCategory.id=" 
						 + dumpsterPriceToBeEdited.getMaterialType().getMaterialCategory().getId()
						 + " order by obj.materialName asc";
		List<MaterialType> materialTypesForSelMatCat = genericDAO.executeSimpleQuery(query);
		model.addAttribute("materialTypes", materialTypesForSelMatCat);
		
		return urlContext + "/form";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("materialTypes", genericDAO.findByCriteria(MaterialType.class, criterias, "materialName", false));
	}

	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		return urlContext + "/form";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("customers", genericDAO.findByCriteria(Customer.class, criterias, "companyName", false));
		model.addAttribute("dumpsterSizes", genericDAO.findUniqueByCriteria(DumpsterSize.class, criterias, "id", false));
		//model.addAttribute("materialTypes", genericDAO.findByCriteria(MaterialType.class, criterias, "materialName", false));
		model.addAttribute("dumpsterPrices", genericDAO.executeSimpleQuery("select DISTINCT(obj.price) from DumpsterPrice obj where obj.deleteFlag='1' order by obj.price asc"));
		model.addAttribute("materialCategories", genericDAO.findByCriteria(MaterialCategory.class, criterias, "category", false));
	}

	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") DumpsterPrice entity,
			BindingResult bindingResult, ModelMap model) {
		setupCreate(model, request);
		
		model.addAttribute("msgCtx", "manageDumpsterPrice");
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			System.out.println("Error in validation " + e);
			log.warn("Error in validation: " + e);
		}
		
		// Return to form if we had errors
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			for(ObjectError e : errors) {
				System.out.println("Error: " + e.getDefaultMessage());
			}
			
			return urlContext + "/form";
		}
		
		try {
			beforeSave(request, entity, model);
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
		} catch (PersistenceException e){
			String errorMsg = extractSaveErrorMsg(e);
			model.addAttribute("error", errorMsg);
			
			return urlContext + "/form";
		}
		
		model.addAttribute("msg", "Dumpster Price saved successfully");
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new DumpsterPrice());
		} else {
			String materialTypeQuery = StringUtils.EMPTY;
			if (entity.getMaterialType() != null) {
				materialTypeQuery = "select obj from MaterialType obj where obj.deleteFlag='1' and obj.id=" + entity.getMaterialType().getId();
				List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(materialTypeQuery);
				entity.setMaterialType(materialTypeList.get(0));
			}
			
			materialTypeQuery = "select obj from MaterialType obj where obj.deleteFlag='1' and obj.materialCategory.id=" 
					+ entity.getMaterialCategory().getId()
					+ " order by obj.materialName asc";
			List<MaterialType> materialTypesForSelMatCat = genericDAO.executeSimpleQuery(materialTypeQuery);
			model.addAttribute("materialTypes", materialTypesForSelMatCat);
		}
		
		return urlContext + "/form";
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			@RequestParam("dataQualifier") String dataQualifier,
			Object objectDAO, Class clazz) {
		List columnPropertyList = (List) request.getSession().getAttribute(dataQualifier + "ColumnPropertyList");
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");

		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html"))
			response.setHeader("Content-Disposition", "attachment;filename="
					+ urlContext + "Report." + type);
		try {
			criteria.setPageSize(100000);
			String label = getCriteriaAsString(criteria);
			System.out.println("Criteria: " + label);
			
			/*ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(),
					columnPropertyList, criteria, request);*/
			
			List<DumpsterPrice> dumpsterPriceList = retrieveDusmpsterPrice(criteria);
			ByteArrayOutputStream out = dynamicReportService.exportReport(
					urlContext + "Report", type, getEntityClass(), dumpsterPriceList,
					columnPropertyList, request);
			
			out.writeTo(response.getOutputStream());
			if (type.equals("html"))
				response.getOutputStream()
						.println(
								"<script language=\"javascript\">window.print()</script>");
			criteria.setPageSize(25);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
		}
	}
	
	private List<DumpsterPrice> retrieveDusmpsterPrice(SearchCriteria criteria) {
		String orderBy = "materialCategory.category, dumpsterSize.id, effectiveStartDate desc";
		List<DumpsterPrice> dumpsterPriceList = genericDAO.search(DumpsterPrice.class, criteria, orderBy, null, null);
		return dumpsterPriceList;
	}
	
	private String extractSaveErrorMsg(Exception e) {
		String errorMsg = "Error occured while saving Dumpster Price";
		return errorMsg;
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
		String query = "select obj from MaterialType obj where obj.deleteFlag='1' and ";
		query	+= " obj.materialCategory.id=" + materialCategoryId + " order by obj.materialName asc";
		List<MaterialType> materialTypes = genericDAO.executeSimpleQuery(query);
		return materialTypes;
	}
}
