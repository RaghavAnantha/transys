package com.transys.controller;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		
		String intakeDate = extractIntakeSearchDate(criteria);
		
		String rollOffAggregationQuery = "select materialType.materialName, sum(netWeightTonnage) from Order obj where obj.pickupDate='" 
								+ intakeDate + "' group by obj.materialType.id";
		List<?> rollOffAggregationQueryResults = genericDAO.executeSimpleQuery(rollOffAggregationQuery);
		
		String publicIntakeAggregationQuery = "select materialType.materialName, sum(netWeightTonnage) from PublicMaterialIntake obj where obj.intakeDate='" 
				+ intakeDate + "' group by obj.materialType.id";
		List<?> publicIntakeAggregationQueryResults = genericDAO.executeSimpleQuery(publicIntakeAggregationQuery);
		
		List<MaterialIntakeReportVO> rollOffMaterialIntakeReportVOList = new ArrayList<MaterialIntakeReportVO>();
		populateRollOffData(intakeDate, rollOffAggregationQueryResults, rollOffMaterialIntakeReportVOList);
		
		List<MaterialIntakeReportVO> publicMaterialIntakeReportVOList = new ArrayList<MaterialIntakeReportVO>();
		populatePublicIntakeData(intakeDate, publicIntakeAggregationQueryResults, publicMaterialIntakeReportVOList);
		
		List<MaterialIntakeReportVO> unionMaterialIntakeReportVOList = unionMaterialIntakeData(
				rollOffMaterialIntakeReportVOList, publicMaterialIntakeReportVOList);
		model.addAttribute("list", unionMaterialIntakeReportVOList);
		
		return urlContext + "/list";
	}
	
	private void populateRollOffData(String intakeDate, List<?> rollOffAggregationQueryResults, 
			List<MaterialIntakeReportVO> rollOffMaterialIntakeReportVOList) {
		if (rollOffAggregationQueryResults == null) {
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
		if (publicIntakeAggregationQueryResults == null) {
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
		if (intakeDateObj == null) {
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
}
