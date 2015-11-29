package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.model.Dumpster;
import com.transys.model.Order;
import com.transys.model.RecycleLocation;
import com.transys.model.SearchCriteria;
import com.transys.model.vo.MaterialIntakeReportVO;

@Controller
@RequestMapping("/reports/materialIntakeDailyReport")
public class MaterialIntakeDailyReportController extends CRUDController<Order> {
	public MaterialIntakeDailyReportController() {	
		setUrlContext("reports/materialIntakeDailyReport");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<MaterialIntakeReportVO> reportData =  retrieveReportData(criteria);
		model.addAttribute("list", reportData);
		
		return urlContext + "/list";
	}
	
	private List<MaterialIntakeReportVO> retrieveReportData(SearchCriteria criteria) {
		String intakeDate = extractIntakeSearchDate(criteria);
		if (StringUtils.isEmpty(intakeDate)) {
			return (new ArrayList<MaterialIntakeReportVO>());
		}
		
		String rollOffAggregationQuery = "select materialType.materialName, sum(netWeightTonnage) from Order obj where obj.deleteFlag='1' and obj.pickupDate='" 
								+ intakeDate + "' group by obj.materialType.id";
		List<?> rollOffAggregationQueryResults = genericDAO.executeSimpleQuery(rollOffAggregationQuery);
		
		String publicIntakeAggregationQuery = "select materialType.materialName, sum(netWeightTonnage) from PublicMaterialIntake obj where obj.deleteFlag='1' and obj.intakeDate='" 
				+ intakeDate + "' group by obj.materialType.id";
		List<?> publicIntakeAggregationQueryResults = genericDAO.executeSimpleQuery(publicIntakeAggregationQuery);
		
		List<MaterialIntakeReportVO> rollOffMaterialIntakeReportVOList = new ArrayList<MaterialIntakeReportVO>();
		populateRollOffData(intakeDate, rollOffAggregationQueryResults, rollOffMaterialIntakeReportVOList);
		
		List<MaterialIntakeReportVO> publicMaterialIntakeReportVOList = new ArrayList<MaterialIntakeReportVO>();
		populatePublicIntakeData(intakeDate, publicIntakeAggregationQueryResults, publicMaterialIntakeReportVOList);
		
		List<MaterialIntakeReportVO> unionMaterialIntakeReportVOList = unionMaterialIntakeData(rollOffMaterialIntakeReportVOList, 
				publicMaterialIntakeReportVOList);
		
		return unionMaterialIntakeReportVOList;
	}
	
	private void populateRollOffData(String intakeDate, List<?> rollOffAggregationQueryResults, 
			List<MaterialIntakeReportVO> rollOffMaterialIntakeReportVOList) {
		if (rollOffAggregationQueryResults == null || rollOffAggregationQueryResults.isEmpty()) {
			return;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		for (Object aggregationObj : rollOffAggregationQueryResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(aggregationObj);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
			String [] tokens = jsonResponse.split(",");
			
			MaterialIntakeReportVO aMaterialIntakeReportVO = new MaterialIntakeReportVO();
			
			String reportDate = convertDateFormat(intakeDate, "yyy-MM-dd", "MM/dd/yyyy");
			aMaterialIntakeReportVO.setReportDate(reportDate);
			
			aMaterialIntakeReportVO.setMaterialName(StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY));
			
			BigDecimal rollOffTons = new BigDecimal(tokens[1]);
			BigDecimal publicIntakeTons = new BigDecimal(0.00);
			BigDecimal totalTons = rollOffTons.add(publicIntakeTons);
			
			aMaterialIntakeReportVO.setRollOffTons(rollOffTons);
			aMaterialIntakeReportVO.setPublicIntakeTons(publicIntakeTons);
			aMaterialIntakeReportVO.setTotalTons(totalTons);
			
			rollOffMaterialIntakeReportVOList.add(aMaterialIntakeReportVO);
		}
	}
	
	private void populatePublicIntakeData(String intakeDate, List<?> publicIntakeAggregationQueryResults, 
			List<MaterialIntakeReportVO> publicMaterialIntakeReportVOList) {
		if (publicIntakeAggregationQueryResults == null || publicIntakeAggregationQueryResults.isEmpty()) {
			return;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		for (Object aggregationObj : publicIntakeAggregationQueryResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(aggregationObj);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
			String [] tokens = jsonResponse.split(",");
			
			MaterialIntakeReportVO aMaterialIntakeReportVO = new MaterialIntakeReportVO();
			
			String reportDate = convertDateFormat(intakeDate, "yyy-MM-dd", "MM/dd/yyyy");
			aMaterialIntakeReportVO.setReportDate(reportDate);
			
			aMaterialIntakeReportVO.setMaterialName(StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY));
			
			BigDecimal rollOffTons = new BigDecimal(0.00);
			BigDecimal publicIntakeTons = new BigDecimal(tokens[1]);
			BigDecimal totalTons = rollOffTons.add(publicIntakeTons);
			
			aMaterialIntakeReportVO.setRollOffTons(rollOffTons);
			aMaterialIntakeReportVO.setPublicIntakeTons(publicIntakeTons);
			aMaterialIntakeReportVO.setTotalTons(totalTons);
			
			publicMaterialIntakeReportVOList.add(aMaterialIntakeReportVO);
		}
	}
	
	private List<MaterialIntakeReportVO> unionMaterialIntakeData(List<MaterialIntakeReportVO> rollOffMaterialIntakeReportVOList,
			List<MaterialIntakeReportVO> publicMaterialIntakeReportVOList) {
		Map<String, MaterialIntakeReportVO> mappedPublicMaterialIntake = new HashMap<String, MaterialIntakeReportVO>();
		for (MaterialIntakeReportVO aPublicMaterialIntakeReportVO : publicMaterialIntakeReportVOList) {
			mappedPublicMaterialIntake.put(aPublicMaterialIntakeReportVO.getMaterialName(), aPublicMaterialIntakeReportVO);
		}
		
		List<MaterialIntakeReportVO> unionMaterialIntakeReportVOList = new ArrayList<MaterialIntakeReportVO>();
		for (MaterialIntakeReportVO aRollOffMaterialIntakeReportVO : rollOffMaterialIntakeReportVOList) {
			MaterialIntakeReportVO aPublicMaterialIntakeReportVO = mappedPublicMaterialIntake.get(aRollOffMaterialIntakeReportVO.getMaterialName());
			if (aPublicMaterialIntakeReportVO != null) {
				BigDecimal rollOffTons = aRollOffMaterialIntakeReportVO.getRollOffTons();
				BigDecimal publicIntakeTons = aPublicMaterialIntakeReportVO.getPublicIntakeTons();
				BigDecimal totalTons = rollOffTons.add(publicIntakeTons);
				
				aRollOffMaterialIntakeReportVO.setPublicIntakeTons(publicIntakeTons);
				aRollOffMaterialIntakeReportVO.setTotalTons(totalTons);
				
				mappedPublicMaterialIntake.remove(aRollOffMaterialIntakeReportVO.getMaterialName());
			}
			
			unionMaterialIntakeReportVOList.add(aRollOffMaterialIntakeReportVO);
		}
		
		unionMaterialIntakeReportVOList.addAll(mappedPublicMaterialIntake.values());
		
		return unionMaterialIntakeReportVOList;
	}
	
	private String extractIntakeSearchDate(SearchCriteria criteria) {
		String intakeDateStr = StringUtils.EMPTY;
		
		Object intakeDateObj = criteria.getSearchMap().get("intakeDate");
		if (intakeDateObj == null || StringUtils.isEmpty(intakeDateObj.toString())) {
			return intakeDateStr;
		}
		
		intakeDateStr = intakeDateObj.toString();
		return convertDateFormat(intakeDateStr, "MM/dd/yyyy", "yyy-MM-dd");
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateMaterialIntakeDailyReport.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<Map<String,Object>> exportReportData = generateExportReportData(model, request);
			
			type = setRequestHeaders(response, type, "materialIntakeDailyReport");
			Map<String, Object> params = new HashMap<String, Object>();

			ByteArrayOutputStream out = dynamicReportService.generateStaticReport("materialIntakeDailyReport", exportReportData, params, type, request);
		
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

		List<MaterialIntakeReportVO> materialIntakeReportVOList =  retrieveReportData(criteria);
		if (materialIntakeReportVOList == null || materialIntakeReportVOList.isEmpty()) {
			return exportReportData;
		}
		
		for (MaterialIntakeReportVO aMaterialIntakeReportVO : materialIntakeReportVOList) {
			Map<String, Object> aReportRow = new HashMap<String, Object>();
			
			aReportRow.put("reportDate", aMaterialIntakeReportVO.getReportDate());
			aReportRow.put("materialName", aMaterialIntakeReportVO.getMaterialName());
			aReportRow.put("rollOffTons",aMaterialIntakeReportVO.getRollOffTons());
			aReportRow.put("publicIntakeTons",aMaterialIntakeReportVO.getPublicIntakeTons());
			aReportRow.put("totalTons",aMaterialIntakeReportVO.getTotalTons());
			
			exportReportData.add(aReportRow);
		}
		
		return exportReportData;
	}
}
