package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.core.report.generator.ExcelReportGenerator;
import com.transys.core.report.generator.TransferStationIntakeReportGenerator;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.vo.MonthlyIntakeReportVO;
import com.transys.model.vo.PublicMaterialIntakeVO;
import com.transys.model.vo.RollOffBoxesPerYardVO;

@Controller
@RequestMapping("/reports/monthlyTransferStationIntakeReport")
public class MonthlyTransferStationIntakeReportController extends CRUDController<Order> {
	
	public MonthlyTransferStationIntakeReportController(){	
		setUrlContext("reports/monthlyTransferStationIntakeReport");
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
		
		Object monthObj = criteria.getSearchMap().get("month");
		if (monthObj == null || StringUtils.isEmpty(monthObj.toString())) {
			model.addAttribute("msgCtx", "monthlyTransferStationIntakeReport");
			model.addAttribute("error", "Please select a month for report generation.");
			return "/list";
		}
		
		Object yearObj = criteria.getSearchMap().get("year");
		if (yearObj == null || StringUtils.isEmpty(yearObj.toString())) {
			model.addAttribute("msgCtx", "monthlyTransferStationIntakeReport");
			model.addAttribute("error", "Please select a year for report generation.");
			return "/list";
		}
		
		List<?> reportData =  retrieveReportData(criteria);
		model.addAttribute("list", reportData);
		
		return urlContext + "/list";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		
		//Calendar cal = Calendar.getInstance();
		//Map<String, Integer> months = cal.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();
		model.addAttribute("months", months); 
	}
	
	private List<MonthlyIntakeReportVO> retrieveReportData(SearchCriteria criteria) {
		
		List<MonthlyIntakeReportVO> monthlyIntakeReportVOList = new ArrayList<>();
		
		MonthlyIntakeReportVO monthlyIntakeReportVO = new MonthlyIntakeReportVO();
		// select SUM(count), SUM(cubicYards) from (select dumpstersizeId,count(*) as count, (dumpstersizeId * count(*)) AS cubicYards from `transys`.transysOrder where deliverydate='2015-10-09' group by dumpstersizeId) AS A;
		//String rollOffBoxesPerYardQuery = "select dumpsterSize0_.size, count(*) as count, (dumpsterSize0_.size * count(*)) AS cubicYards from `transys`.transysOrder order0_, `transys`.dumpsterSize dumpsterSize0_ where dumpsterSize0_.id = order0_.dumpsterSizeId AND deliveryDate='2015-10-09' group by dumpstersizeId";
		String rollOffBoxesPerYardQuery = "select dumpsterSize0_.size as SIZE, ifnull(count(order0_.dumpsterSizeId),0) AS COUNT, (dumpsterSize0_.size * ifnull(count(order0_.dumpsterSizeId),0)) AS cubicYards FROM `transys`.dumpsterSize dumpsterSize0_ LEFT JOIN `transys`.transysOrder order0_ ON (dumpsterSize0_.id = order0_.dumpsterSizeId AND deliveryDate='2015-10-09') group by dumpsterSize0_.size order by dumpsterSize0_.id";
		List<?> rollOffBoxesPerYardResults = genericDAO.executeNativeQuery(rollOffBoxesPerYardQuery);

		String publicIntakeTonnageQuery = "select ifnull(sum(obj.netWeightTonnage),0) AS tonnage, ifnull(((sum(obj.netWeightTonnage)) * 3.3),0) as cubicYards from `transys`.publicMaterialIntake obj where obj.intakeDate='2015-10-09'";
		List<?> publicIntakeTonnageResults = genericDAO.executeNativeQuery(publicIntakeTonnageQuery);
		
		if (rollOffBoxesPerYardResults != null && rollOffBoxesPerYardResults.size() > 0) {
			List<RollOffBoxesPerYardVO> boxesPerYardVOList = (List<RollOffBoxesPerYardVO>)(List<?>) populateAggregationResults(rollOffBoxesPerYardResults);
			List<PublicMaterialIntakeVO>publicIntakeVOList = (List<PublicMaterialIntakeVO>)(List<?>) populateAggregationResults(publicIntakeTonnageResults);
			
			monthlyIntakeReportVO.setRollOffBoxesPerYard(boxesPerYardVOList);
			
			Integer totalBoxes = 0;
			BigDecimal rollOffCubicYards = new BigDecimal(0);
			
			for (RollOffBoxesPerYardVO eachYardCount : boxesPerYardVOList) {
				totalBoxes += eachYardCount.getNumBoxes();
				rollOffCubicYards = rollOffCubicYards.add(eachYardCount.getCubicYards());
			}
		
			monthlyIntakeReportVO.setIntakeDate(Calendar.getInstance().getTime());
			monthlyIntakeReportVO.setTotalBoxes(totalBoxes);
			monthlyIntakeReportVO.setRollOffCubicYards(rollOffCubicYards);
			
			for (PublicMaterialIntakeVO eachPublicIntakeCount : publicIntakeVOList) {
				monthlyIntakeReportVO.setPublicIntakeCubicYards(eachPublicIntakeCount.getCubicYards());
				monthlyIntakeReportVO.setPublicIntakeTonnage(eachPublicIntakeCount.getTonnage());
			}
			
			String rollOffTonnageQuery = "select SUM(netWeightTonnage) from Order obj where obj.deliveryDate='2015-10-09'";
			List<?> rollOffTonnageResult = genericDAO.executeSimpleQuery(rollOffTonnageQuery);
			monthlyIntakeReportVO.setRollOffTonnage(new BigDecimal(parseSingleElementFromQueryResult(rollOffTonnageResult)));
			
			monthlyIntakeReportVOList.add(monthlyIntakeReportVO);
		}
		
		return monthlyIntakeReportVOList;
	}
	
	private String parseSingleElementFromQueryResult(List<?> queryResults) {
		
		if (queryResults != null && queryResults.size() > 0) {
			ObjectMapper objectMapper = new ObjectMapper();
				String jsonResponse = StringUtils.EMPTY;
				try {
					jsonResponse = objectMapper.writeValueAsString(queryResults.get(0));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (jsonResponse.startsWith("\"")) {
					jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
				}
				
				String [] tokens = jsonResponse.split(",");
				if (tokens.length > 0) {
					return StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY);
				} else {
					return StringUtils.EMPTY;
				}
		} else {
			return StringUtils.EMPTY;
		}
			
	}
	
	private List<Object> populateAggregationResults(List<?> aggregationQueryResults) {
		List<Object> resultList = new ArrayList<>();
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
			
			System.out.println("Json response = " + jsonResponse);
			
			String [] tokens = jsonResponse.split(",");
			if (tokens.length == 3) {
				String yardSize = StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY);
				Integer numBoxes = Integer.parseInt(tokens[1]);
				BigDecimal cubicYards = new BigDecimal(tokens[2]);
				
				RollOffBoxesPerYardVO boxesPerYardData = new RollOffBoxesPerYardVO();
				boxesPerYardData.setYardSize(yardSize);
				boxesPerYardData.setNumBoxes(numBoxes);
				boxesPerYardData.setCubicYards(cubicYards);
				
				resultList.add(boxesPerYardData);
			} else if (tokens.length == 2) {
				// public intake
				BigDecimal tonnage = new BigDecimal(tokens[0]);
				BigDecimal cubicYards = new BigDecimal(tokens[1]);
				PublicMaterialIntakeVO publicMaterialIntakeVO = new PublicMaterialIntakeVO();
				publicMaterialIntakeVO.setTonnage(tonnage);
				publicMaterialIntakeVO.setCubicYards(cubicYards);
				
				resultList.add(publicMaterialIntakeVO);
			}
		}
		
		return resultList;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateExcelReport.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, Object objectDAO, Class clazz) {
		
		ExcelReportGenerator excelReportGenerator = new TransferStationIntakeReportGenerator();
		
		List<MonthlyIntakeReportVO> reportDataList = retrieveReportData(null);
		Map<String, String> headerMap = new LinkedHashMap<>();
		
		headerMap.put("Date", "intakeDate");
		headerMap.put("Total Boxes", "totalBoxes");
		headerMap.put("Roll-off Container Sizes", "rollOffBoxesPerYard");
		headerMap.put("Roll Off Cubic Yards", "rollOffCubicYards");
		headerMap.put("Roll Off Actual Tonnage", "rollOffTonnage");
		headerMap.put("Public Intake Tonnage", "publicIntakeTonnage");
		headerMap.put("Public Intake Cubic Yards", "publicIntakeCubicYards");
		
		ByteArrayOutputStream out = excelReportGenerator.exportReport("Monthly Transfer Station Intake Report", headerMap, reportDataList);
		
		setRequestHeaders(response, "xls", "MonthlyTransferTest");
		
		try {
			//FileOutputStream fout = new FileOutputStream("MonthlyTransferTest.xlsx");
			//out.writeTo(fout);
			out.writeTo(response.getOutputStream());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
