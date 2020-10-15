package com.transys.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import com.transys.controller.CRUDController;
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
		
//		List<?> reportData =  retrieveReportData(criteria);
	//	model.addAttribute("list", reportData);
		
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
		
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int year = now.get(Calendar.YEAR); 
		
		List<Integer> years = new LinkedList<>();
		for (int tmpYear = (year-1); tmpYear < (year + 20); tmpYear++) {
			years.add(tmpYear);
		}
		model.addAttribute("years", years); 
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
	
	private List<MonthlyIntakeReportVO> retrieveReportData(String month, String year) {

		List<MonthlyIntakeReportVO> monthlyIntakeReportVOList = new ArrayList<>();

		// System.out.println("Search critera = " + criteria.getSearchMap());

		// String month = criteria.getSearchMap().get("month").toString();
		// String year = criteria.getSearchMap().get("year").toString();

		try {
			
			String intakeDate = convertDateFormat(year + "-" + month + "-" + "01", "yyyy-MMMM-dd", "yyyy-MM-dd");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(intakeDate));
			int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			for (int co = 0; co < maxDay; co++) {

				MonthlyIntakeReportVO monthlyIntakeReportVO = new MonthlyIntakeReportVO();

				System.out.println("Executing query for date = " + intakeDate);

				monthlyIntakeReportVO.setIntakeDate(sdf.parse(intakeDate));

				// select SUM(count), SUM(cubicYards) from (select
				// dumpstersizeId,count(*) as count, (dumpstersizeId * count(*)) AS
				// cubicYards from `transys`.transysOrder where
				// deliverydate='2015-10-09' group by dumpstersizeId) AS A;
				// String rollOffBoxesPerYardQuery = "select dumpsterSize0_.size,
				// count(*) as count, (dumpsterSize0_.size * count(*)) AS cubicYards
				// from `transys`.transysOrder order0_, `transys`.dumpsterSize
				// dumpsterSize0_ where dumpsterSize0_.id = order0_.dumpsterSizeId
				// AND deliveryDate='2015-10-09' group by dumpstersizeId";
				// gets the number of dumpsters grouped by dumpsterSize for orders
				// on a particular delivery date
				String rollOffBoxesPerYardQuery = "select dumpsterSize0_.size as SIZE, ifnull(count(order0_.dumpsterSizeId),0) AS COUNT, (dumpsterSize0_.size * ifnull(count(order0_.dumpsterSizeId),0)) AS cubicYards FROM `transys`.dumpsterSize dumpsterSize0_ LEFT JOIN `transys`.transysOrder order0_ ON (dumpsterSize0_.id = order0_.dumpsterSizeId AND pickupDate='"
						+ intakeDate + "' AND dumpsterSize0_.delete_flag='1' AND order0_.delete_flag='1') group by dumpsterSize0_.size order by dumpsterSize0_.id";
				List<?> rollOffBoxesPerYardResults = genericDAO.executeNativeQuery(rollOffBoxesPerYardQuery);

				String publicIntakeTonnageQuery = "select ifnull(sum(obj.netWeightTonnage),0) AS tonnage, ifnull(((sum(obj.netWeightTonnage)) * 3.3),0) as cubicYards from `transys`.publicMaterialIntake obj where obj.delete_flag='1' and obj.intakeDate = '"
						+ intakeDate + "'";// ='2015-10-09'";
				List<?> publicIntakeTonnageResults = genericDAO.executeNativeQuery(publicIntakeTonnageQuery);

				if (rollOffBoxesPerYardResults != null && rollOffBoxesPerYardResults.size() > 0) {
					List<RollOffBoxesPerYardVO> boxesPerYardVOList = (List<RollOffBoxesPerYardVO>) (List<?>) populateAggregationResults(
							rollOffBoxesPerYardResults);

					monthlyIntakeReportVO.setRollOffBoxesPerYard(boxesPerYardVOList);

					Integer totalBoxes = 0;
					BigDecimal rollOffCubicYards = new BigDecimal(0);

					for (RollOffBoxesPerYardVO eachYardCount : boxesPerYardVOList) {
						totalBoxes += eachYardCount.getNumBoxes();
						rollOffCubicYards = rollOffCubicYards.add(eachYardCount.getCubicYards());
					}

					monthlyIntakeReportVO.setTotalBoxes(totalBoxes);
					monthlyIntakeReportVO.setRollOffCubicYards(rollOffCubicYards);

					String rollOffTonnageQuery = "select SUM(netWeightTonnage) from Order obj where obj.deleteFlag='1' and obj.pickupDate='"
							+ intakeDate + "'";// '2015-10-09'";
					List<?> rollOffTonnageResult = genericDAO.executeSimpleQuery(rollOffTonnageQuery);

					String rollOffTonnage = parseSingleElementFromQueryResult(rollOffTonnageResult);
					if (StringUtils.isEmpty(rollOffTonnage)) {
						monthlyIntakeReportVO.setRollOffTonnage(new BigDecimal(0));
					} else {
						monthlyIntakeReportVO.setRollOffTonnage(new BigDecimal(rollOffTonnage));
					}

				} else {
					monthlyIntakeReportVO.setTotalBoxes(0);
					monthlyIntakeReportVO.setTotalCubicYards(new BigDecimal(0));
					monthlyIntakeReportVO.setTotalTonnage(new BigDecimal(0));
				}

				List<PublicMaterialIntakeVO> publicIntakeVOList = (List<PublicMaterialIntakeVO>) (List<?>) populateAggregationResults(
						publicIntakeTonnageResults);

				if (publicIntakeVOList != null && publicIntakeVOList.size() > 0) {
					for (PublicMaterialIntakeVO eachPublicIntakeCount : publicIntakeVOList) {
						monthlyIntakeReportVO.setPublicIntakeCubicYards(eachPublicIntakeCount.getCubicYards());
						monthlyIntakeReportVO.setPublicIntakeTonnage(eachPublicIntakeCount.getTonnage());
					}
				} else {
					monthlyIntakeReportVO.setPublicIntakeCubicYards(new BigDecimal(0));
					monthlyIntakeReportVO.setPublicIntakeTonnage(new BigDecimal(0));
				}

				System.out.println("Setting data for date = " + monthlyIntakeReportVO.getIntakeDate());
				monthlyIntakeReportVOList.add(monthlyIntakeReportVO);

				c.add(Calendar.DATE, 1);
				intakeDate = sdf.format(c.getTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				
//				System.out.println("JSON Response = " + jsonResponse);
				
				if (jsonResponse.startsWith("\"")) {
					jsonResponse = jsonResponse.substring(1, jsonResponse.length() - 1);
				}
				
				String [] tokens = jsonResponse.split(",");
				if (tokens.length > 0) {
					String value = StringUtils.replace(tokens[0], "\"", StringUtils.EMPTY);
					if (value.equals("null")) {
						return StringUtils.EMPTY;
					} else {
						return value;
					}
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/generateExcelReport.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, Object objectDAO, Class clazz) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		String month = request.getParameter("month");
		String year =request.getParameter("year");
		
		ExcelReportGenerator excelReportGenerator = new TransferStationIntakeReportGenerator();
		
		List<MonthlyIntakeReportVO> reportDataList = retrieveReportData(month, year);
		Map<String, String> headerMap = new LinkedHashMap<>();
		
		headerMap.put("Date", "intakeDate");
		headerMap.put("Total Boxes", "totalBoxes");
		headerMap.put("Roll-off Container Sizes", "rollOffBoxesPerYard");
		headerMap.put("Roll Off Totals", "rollOffTotals");
		headerMap.put("Public Intake Totals", "publicIntakeTotals");
		headerMap.put("Waste Totals", "wasteTotals");
		
		ByteArrayOutputStream out = excelReportGenerator.exportReport("Monthly Transfer Station Intake Report", headerMap, reportDataList,
				true);
		
		setReportRequestHeaders(response, "xlsx", "monthlyTransferStationIntakeReport");
		
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
