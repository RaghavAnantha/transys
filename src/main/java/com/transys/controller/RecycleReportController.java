package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.transys.core.report.generator.RecycleReportGenerator;
import com.transys.model.MaterialCategory;
import com.transys.model.MaterialType;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
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
		
		/*SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
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
		criteria.setRecordCount(reportData.size());
		model.addAttribute("list", reportData);
		
		String recycleDateFrom = criteria.getSearchMap().getOrDefault("recycleDateFrom", StringUtils.EMPTY).toString();
		String recycleDateTo = criteria.getSearchMap().getOrDefault("recycleDateTo", StringUtils.EMPTY).toString();
		model.addAttribute("recycleDateFrom", recycleDateFrom);
		model.addAttribute("recycleDateTo", recycleDateTo);
		
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
		String query = "select obj from MaterialType obj where obj.deleteFlag='1' and";
		query	+= " obj.materialCategory.id=" + materialCategoryId;
		query	+= " order by obj.materialName";
		List<MaterialType> materialTypes = genericDAO.executeSimpleQuery(query);
		return materialTypes;
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		model.addAttribute("materialCategories", genericDAO.findByCriteria(MaterialCategory.class, criterias, "category", false));
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Object materialCategoryObj = criteria.getSearchMap().get("materialCategory");
		if (materialCategoryObj != null) {
			String materialCategoryId = materialCategoryObj.toString();
			if (StringUtils.isNotEmpty(materialCategoryId)) {
				List<MaterialType> materialTypeList = retrieveMaterialTypes(Long.valueOf(materialCategoryId));
				model.addAttribute("materialTypes", materialTypeList);
			}
		}
	}
	
	private List<RecycleReportVO> retrieveReportData(SearchCriteria criteria) {
		// Working Query in MySQL - select matCategory,materialName,SUM(tonnageSum),recycleLoc from (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' AND order0_.pickupDate<='2015-10-27' AND order0_.pickupDate>='2015-10-17' AND order0_.pickupDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by order0_.materialTypeId , recycleloc1_.id UNION (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' AND public0_.intakeDate<='2015-10-27' AND public0_.intakeDate>='2015-10-17' AND public0_.intakeDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by public0_.materialTypeId , recycleloc1_.id)) AS temp group by materialName, recycleLoc;
		String conditionClause = createSearchCriteria(criteria);
		String rollOffAggregationQuery = "select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.delete_flag='1' and materialty2_.delete_flag='1' and materialca3_.delete_flag='1' and recycleloc1_.delete_flag='1' and order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' " + conditionClause + " group by order0_.materialTypeId , recycleloc1_.id";
		
		conditionClause = conditionClause.replaceAll("order0_.pickupDate","public0_.intakeDate");
		String publicAggregationQuery = "select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.delete_flag='1' and materialty2_.delete_flag='1' and materialca3_.delete_flag='1' and recycleloc1_.delete_flag='1' and public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' " + conditionClause + " group by public0_.materialTypeId , recycleloc1_.id";
		String aggregationQuery = "select matCategory,materialName,SUM(tonnageSum),recycleLoc from (" + rollOffAggregationQuery + " UNION (" + publicAggregationQuery + ")) AS temp group by materialName, recycleLoc";
		System.out.println("Query => " + aggregationQuery);
		//String aggregationQuery = "select matCategory,materialName,SUM(tonnageSum),recycleLoc from (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(order0_.netWeightTonnage) as tonnageSum,  order0_.pickupDate as pickupDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.transysOrder order0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where order0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=order0_.materialTypeId and recycleloc1_.status='Active' AND order0_.pickupDate<='2015-10-27' AND order0_.pickupDate>='2015-10-17' AND order0_.pickupDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by order0_.materialTypeId , recycleloc1_.id UNION (select materialca3_.category as matCategory, materialty2_.materialName as materialName, sum(public0_.netWeightTonnage) as tonnageSum,  public0_.intakeDate as intakeDate, recycleloc1_.location as recycleLoc, recycleloc1_.effectiveStartDate, recycleloc1_.effectiveEndDate from `transys`.publicMaterialIntake public0_, `transys`.materialType materialty2_, `transys`.materialCategory materialca3_ cross join `transys`.recycleLocation recycleloc1_ where public0_.materialTypeId=materialty2_.id and materialty2_.materialCategoryId=materialca3_.id and recycleloc1_.materialTypeId=public0_.materialTypeId and recycleloc1_.status='Active' AND public0_.intakeDate<='2015-10-27' AND public0_.intakeDate>='2015-10-17' AND public0_.intakeDate BETWEEN recycleloc1_.effectiveStartDate AND recycleloc1_.effectiveEndDate group by public0_.materialTypeId , recycleloc1_.id)) AS temp group by materialName, recycleLoc";		

		/**
		 * JPA Simple Query - UNION not supported
		 */
		//	String rollOffAggregationQuery = "select obj.materialType.materialCategory.category as materialCategory, obj.materialType.materialName as materialName, SUM(obj.netWeightTonnage) as totalTonnage, r.location as recycleLocation from Order obj, RecycleLocation r where r.materialType.id = obj.materialType.id and r.status='Active' " + conditionClause + " group by obj.materialType.id, r.id";
		//	String publicAggregationQuery = "select public.materialType.materialCategory.category as materialCategory, public.materialType.materialName as materialName, SUM(public.netWeightTonnage) as totalTonnage, r.location as recycleLocation from PublicMaterialIntake public, RecycleLocation r where r.materialType.id = public.materialType.id and r.status='Active' " + conditionClause + " group by public.materialType.id, r.id";
		//	String aggregationQuery = "select materialCategory, materialName, SUM(totalTonnage),recycleLocation from (" + rollOffAggregationQuery + " UNION " + publicAggregationQuery + ") AS temp group by materialName, recycleLocation";
	
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
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
			
			criteria.getSearchMap().remove("_csrf");
			
			List<RecycleReportVO> exportReportData1 =  retrieveReportData(criteria);

			/*if (!StringUtils.isEmpty(type) && type.equals("xls")) {
				type = "xlsx";
			}*/
			type = setRequestHeaders(response, type, "recycleReport");
			
			String recycleDateFrom = criteria.getSearchMap().getOrDefault("recycleDateFrom", StringUtils.EMPTY).toString();
			String recycleDateTo = criteria.getSearchMap().getOrDefault("recycleDateTo", StringUtils.EMPTY).toString();
			
			/*Map<String, Object> params = new HashMap<String, Object>();
			params.put("RECYCLE_DATE_FROM", recycleDateFrom);
			params.put("RECYCLE_DATE_TO", recycleDateTo);*/

			//ByteArrayOutputStream out = dynamicReportService.generateStaticReport("recycleReport", exportReportData, params, type, request);
			Map<String, String> headers = new LinkedHashMap<>();
			headers.put("Material Category", "materialCategory");
			headers.put("Material Type", "materialName");
			headers.put("Tons", "totalNetTonnage");
			headers.put("Location", "recycleLocation");
			
			RecycleReportGenerator reportGenerator = new RecycleReportGenerator();
			reportGenerator.setAggregationHeader("Date Range:	" + recycleDateFrom	+ "  To  " + recycleDateTo);
			ByteArrayOutputStream out = reportGenerator.exportReport("Recycle Report", headers, exportReportData1, true);
			
			out.writeTo(response.getOutputStream());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}
}
