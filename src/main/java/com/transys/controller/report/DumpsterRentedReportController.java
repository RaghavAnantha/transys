package com.transys.controller.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import com.transys.core.util.ModelUtil;

import com.transys.model.Dumpster;
import com.transys.model.DumpsterSize;

import com.transys.model.Order;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/dumpsterRentedReport")
public class DumpsterRentedReportController extends ReportController {
	public DumpsterRentedReportController() {	
		setUrlContext("reports/dumpsterRentedReport");
		setReportName("dumpsterRentedReport");
		setMessageCtx("dumpsterRentedReport");
	}
	
	@Override
	protected String getReportFreezeRow() {
		return "7";
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 750;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 2500;
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		String query = "select obj from DumpsterStatus obj where obj.status = 'Dropped Off' and obj.deleteFlag='1'";
		model.addAttribute("dumpsterStatus", genericDAO.executeSimpleQuery(query));
		model.addAttribute("dumpsterSizes", genericDAO.findAll(DumpsterSize.class, true));
	}
	
	@Override
	protected List<Dumpster> performSearch(HttpServletRequest request, SearchCriteria criteria,
			Map<String, Object> params) {
		Map<String, Object> criteriaMap = (Map<String, Object>)criteria.getSearchMap();
		
		String status = (String)criteriaMap.get("status");
		if (StringUtils.isEmpty(status)) {
			criteriaMap.put("status.status", "Dropped Off");
		}
		List<Dumpster> dumpsterList = genericDAO.search(Dumpster.class, criteria, "dumpsterSize.size asc, dumpsterNum asc", null, null);
		criteriaMap.remove("status.status", "Dropped Off");
		
		populateDeliveryDetailsForDumpster(dumpsterList);
		
		String dumpsterSize = (String)criteriaMap.get("dumpsterSize");
		params.put("dumpsterSize", ModelUtil.retrieveDumpsterSize(genericDAO, dumpsterSize));
		params.put("status", ModelUtil.retrieveDumpsterStatus(genericDAO, status));
	
		return dumpsterList;
	}
	
	private void populateDeliveryDetailsForDumpster(List<Dumpster> dumpsterInfoList) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("orderStatus.status", "Dropped Off");
		for (Dumpster aDumpster : dumpsterInfoList) {
			// Get the latest delivery address & delivery date for the corresponding dumpster# from transysOrder table
			criterias.put("dumpster.id", aDumpster.getId());
			List<Order> ordersForDumpster = genericDAO.findByCriteria(Order.class, criterias, "id", true);
			log.info("List of orders for this dumpster = " + ordersForDumpster.size());
			if (ordersForDumpster.isEmpty()) {
				continue;
			}
			
			Order anOrderForDumpster = ordersForDumpster.get(0);
			aDumpster.setOrderId(anOrderForDumpster.getId());
			aDumpster.setCustomer(anOrderForDumpster.getCustomerName());
			aDumpster.setDeliveryAddress(anOrderForDumpster.getDeliveryAddressFullLine());
			aDumpster.setDeliveryDate(anOrderForDumpster.getFormattedDeliveryDate());
		}
	}
	
	/*
	@RequestMapping(method = RequestMethod.GET, value = "/export.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<Dumpster> reportData = prepareReportData(model, request);
			type = setReportRequestHeaders(response, type, "dumpsterRentedReport");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("xls")) {
				Map<String, String> headers = new LinkedHashMap<>();
				headers.put("Dumpster Size", "dumpsterSize");
				headers.put("Dumpster#", "dumpsterNum");
				headers.put("Delivery Address", "deliveryAddress");
				headers.put("Delivery Date", "deliveryDate");
				headers.put("Status", "status");
				
				ExcelReportGenerator reportGenerator = new ExcelReportGenerator();
				reportGenerator.setTitleMergeCellRange("$A$1:$F$1");
				out = reportGenerator.exportReport("Dumpster Rented Report", headers, reportData, true);
				
			} else if (type.equals("pdf")) {
				List<Map<String, Object>> reportDataCollection = getReportDataAsCollection(reportData);
				
				out = new ByteArrayOutputStream();
				Map<String, Object> params = new HashMap<String, Object>();

				out = dynamicReportService.generateStaticReport("dumpsterRentedReport", reportDataCollection, params, type, request);
			}
			
			out.writeTo(response.getOutputStream());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}
	
	private List<Dumpster> prepareReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		List<Dumpster> dumpsterInfoList = genericDAO.search(Dumpster.class, criteria, "id", null, null);
		setDeliveryDetailsForDumpster(dumpsterInfoList);
		
		return dumpsterInfoList;
	}
	
	private List<Map<String, Object>> getReportDataAsCollection(List<Dumpster> dumpsterInfoList) {
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Dumpster aDumpster : dumpsterInfoList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dumpsterSize", aDumpster.getDumpsterSize().getSize());
			map.put("dumpsterNum", aDumpster.getDumpsterNum());
			map.put("status", aDumpster.getStatus().getStatus());
			map.put("deliveryAddress", StringUtils.defaultIfEmpty(aDumpster.getDeliveryAddress(), StringUtils.EMPTY));
			map.put("deliveryDate", StringUtils.defaultIfEmpty(aDumpster.getDeliveryDate(), StringUtils.EMPTY));
			
			//String jsonResponse = JsonUtil.toJson(map);
			//log.info(map);
			
			reportData.add(map);
		}
		return reportData;
	}*/
}
