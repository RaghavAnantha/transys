package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.vo.MaterialIntakeReportVO;
import com.transys.model.vo.RecycleReportVO;

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
		
	/*	SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<?> reportData =  retrieveReportData(criteria);
		model.addAttribute("list", reportData);*/
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<?> reportData =  retrieveReportData(criteria);
		model.addAttribute("list", reportData);
		
		return urlContext + "/list";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
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
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		model.addAttribute("materialCategories", genericDAO.findByCriteria(MaterialCategory.class, criterias, "id", false));
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Object materialCategoryObj = criteria.getSearchMap().get("materialCategory");
		if (materialCategoryObj != null) {
			String materialCategoryId = materialCategoryObj.toString();
			if (StringUtils.isNotEmpty(materialCategoryId)) {
				String query = "select obj from MaterialType obj where obj.materialCategory.id=" + materialCategoryId;
				List<MaterialType> materialTypeList = genericDAO.executeSimpleQuery(query);
				model.addAttribute("materialTypes", materialTypeList);
			}
		}
	}
	
	private List<RecycleReportVO> retrieveReportData(SearchCriteria criteria) {
		
		// Working Query in MySQL - select matCategory,materialName,SUM(tonnageSum),recycleLoc from (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' AND order0_.pickupDate<='2015-10-27' AND order0_.pickupDate>='2015-10-17' AND order0_.pickupDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by order0_.materialTypeId , recycleloc1_.id UNION (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' AND public0_.intakeDate<='2015-10-27' AND public0_.intakeDate>='2015-10-17' AND public0_.intakeDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by public0_.materialTypeId , recycleloc1_.id)) AS temp group by materialName, recycleLoc;
		String conditionClause = createSearchCriteria(criteria);
		String rollOffAggregationQuery = "select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' " + conditionClause + " group by order0_.materialTypeId , recycleloc1_.id";
		
		conditionClause = conditionClause.replaceAll("order0_.pickupDate","public0_.intakeDate");
		String publicAggregationQuery = "select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' " + conditionClause + " group by public0_.materialTypeId , recycleloc1_.id";
		String aggregationQuery = "select matCategory,materialName,SUM(tonnageSum),recycleLoc from (" + rollOffAggregationQuery + " UNION (" + publicAggregationQuery + ")) AS temp group by materialName, recycleLoc";
		System.out.println("Query => " + aggregationQuery);
		//String aggregationQuery = "select matCategory,materialName,SUM(tonnageSum),recycleLoc from (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' AND order0_.pickupDate<='2015-10-27' AND order0_.pickupDate>='2015-10-17' AND order0_.pickupDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by order0_.materialTypeId , recycleloc1_.id UNION (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' AND public0_.intakeDate<='2015-10-27' AND public0_.intakeDate>='2015-10-17' AND public0_.intakeDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by public0_.materialTypeId , recycleloc1_.id)) AS temp group by materialName, recycleLoc";		

		/**
		 * JPA Simple Query - UNION not supported
		 */
//		String rollOffAggregationQuery = "select obj.materialType.materialCategory.category as materialCategory, obj.materialType.materialName as materialName, SUM(obj.netWeightTonnage) as totalTonnage, r.location as recycleLocation from Order obj, RecycleLocation r where r.materialType.id = obj.materialType.id and r.status='Active' " + conditionClause + " group by obj.materialType.id, r.id";
//		String publicAggregationQuery = "select public.materialType.materialCategory.category as materialCategory, public.materialType.materialName as materialName, SUM(public.netWeightTonnage) as totalTonnage, r.location as recycleLocation from PublicMaterialIntake public, RecycleLocation r where r.materialType.id = public.materialType.id and r.status='Active' " + conditionClause + " group by public.materialType.id, r.id";
//		String aggregationQuery = "select materialCategory, materialName, SUM(totalTonnage),recycleLocation from (" + rollOffAggregationQuery + " UNION " + publicAggregationQuery + ") AS temp group by materialName, recycleLocation";
	
		List<?> aggregationQueryResults = genericDAO.executeNativeQuery(aggregationQuery);
		return populateAggregationResults(aggregationQueryResults);
	}
	
	private List<RecycleReportVO> populateAggregationResults(List<?> aggregationQueryResults) {
		
		List<RecycleReportVO> recycleReportVOList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for (Object aggregationObj : aggregationQueryResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(aggregationObj);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
			String [] tokens = jsonResponse.split(",");
			String materialCategory = StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY);
			String materialType = StringUtils.replace(tokens[1], "\"", StringUtils.EMPTY);
			BigDecimal rollOffTons = new BigDecimal(tokens[2]);
			String recycleLocation = StringUtils.replace(tokens[3], "\"", StringUtils.EMPTY);
			
			RecycleReportVO recycleReportVO = new RecycleReportVO();
			recycleReportVO.setMaterialCategory(materialCategory);
			recycleReportVO.setMaterialName(materialType);
			recycleReportVO.setTotalNetTonnage(rollOffTons);
			recycleReportVO.setRecycleLocation(recycleLocation);
			
			recycleReportVOList.add(recycleReportVO);
		}
		
		return recycleReportVOList;
	}

	private String createSearchCriteria(SearchCriteria criteria) {
		
		StringBuffer conditionClause = new StringBuffer();
		Map<String, Object> searchMap = criteria.getSearchMap();
		
		boolean isEmptySearchCriteria = true;
		
		for (String param : searchMap.keySet()) {
			String paramValue = searchMap.get(param).toString();
			if (param.equalsIgnoreCase("materialType") && !StringUtils.isEmpty(paramValue)) {
				isEmptySearchCriteria = false;
				conditionClause.append(" AND materialty2_.id = '" + searchMap.get(param).toString() + "'");
			} else if (param.equalsIgnoreCase("materialCategory") && !StringUtils.isEmpty(paramValue)) {
				isEmptySearchCriteria = false;
				conditionClause.append(" AND materialca3_.id = '" + searchMap.get(param).toString() + "'");
			} else if (param.equalsIgnoreCase("recycleDateFrom") && !StringUtils.isEmpty(paramValue)) {
				isEmptySearchCriteria = false;
				conditionClause.append(" AND order0_.pickupDate >= '" +  convertDateFormat(searchMap.get(param).toString(), "MM/dd/yyyy", "yyy-MM-dd") + "'");
			} else if (param.equalsIgnoreCase("recycleDateTo") && !StringUtils.isEmpty(paramValue)) {
				isEmptySearchCriteria = false;
				conditionClause.append(" AND order0_.pickupDate <= '" +  convertDateFormat(searchMap.get(param).toString(), "MM/dd/yyyy", "yyy-MM-dd") + "'");
			}
			
		}
		
		if (!isEmptySearchCriteria) {
			conditionClause.append(" AND order0_.pickupDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate");
		}

		return conditionClause.toString();
	}
	
	private String convertDateFormat(String inputDateStr, String inputDateFormatStr, String outputDateFormatStr) {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputDateFormatStr);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputDateFormatStr);
		
		String outputDateStr = StringUtils.EMPTY;
		try {
			Date inputDate = inputDateFormat.parse(inputDateStr);
			outputDateStr = outputDateFormat.format(inputDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputDateStr;
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateRecycleReport.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<Map<String,Object>> exportReportData = generateExportReportData(model, request);
			
			type = setRequestHeaders(response, type, "recycleReport");
			Map<String, Object> params = new HashMap<String, Object>();

			ByteArrayOutputStream out = dynamicReportService.generateStaticReport("recycleReport", exportReportData, params, type, request);
		
			out.writeTo(response.getOutputStream());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}
	private List<Map<String, Object>> generateExportReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		
		List<Map<String, Object>> exportReportData = new ArrayList<Map<String, Object>>();

		List<RecycleReportVO> recycleReportVOList =  retrieveReportData(criteria);
		if (recycleReportVOList == null || recycleReportVOList.isEmpty()) {
			return exportReportData;
		}
		
		for (RecycleReportVO reportVO : recycleReportVOList) {
			Map<String, Object> aReportRow = new HashMap<String, Object>();
			
			aReportRow.put("materialCategory", reportVO.getMaterialCategory());
			aReportRow.put("materialName", reportVO.getMaterialName());
			aReportRow.put("totalNetTonnage",reportVO.getTotalNetTonnage());
			aReportRow.put("recycleLocation",reportVO.getRecycleLocation());
			
			exportReportData.add(aReportRow);
		}
		
		return exportReportData;
	}
}
