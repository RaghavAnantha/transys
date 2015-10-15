package com.transys.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/materialIntakeDailyReport")
public class MaterialIntakeDailyReportController extends CRUDController<Order> {

	public MaterialIntakeDailyReportController(){	
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
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		//SELECT materialTypeId, Sum(netWeightTonnage) FROM transys.transysorder where pickupDate='' group by materialTypeId;
		Object inTakeDate = criteria.getSearchMap().get("intakeDate");
		if (inTakeDate != null) {
			try {
				inTakeDate = "" + new Timestamp(((Date) BaseController.dateFormat
						.parse(inTakeDate.toString().trim())).getTime());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		List<?> aggregationResults = genericDAO.executeSimpleQuery("select materialType.materialName, Sum(netWeightTonnage) from Order obj where obj.pickupDate='" + inTakeDate + "' group by obj.materialType.id");
		List<List<String>> listData = new ArrayList<>();
		
		List<String> columnHeaders = new ArrayList<String>();
		columnHeaders.add("\"Type Of Material\"");
		columnHeaders.add("\"Roll-off\"");
		columnHeaders.add("\"Public\"");
		columnHeaders.add("\"Total\"");
		
		listData.add(columnHeaders);
		
		ObjectMapper objectMapper = new ObjectMapper();
		int index = 0;
		for (Object o : aggregationResults) {
			List<String> rowData = new ArrayList<String>();
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(o);
	
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonResponse = jsonResponse.substring(1, jsonResponse.length()-1);
			
			String [] tokens = jsonResponse.split(",");
			
			//rowData.add(index, tokens[0].substring(1, tokens[0].length()-1));
			rowData.add(index, tokens[0]);
			rowData.add(tokens[1]);
			rowData.add("0");
			rowData.add(tokens[1]);
			
			System.out.println("Adding Row --> " + rowData);
			listData.add(rowData);
		}
		
		System.out.println("Adding Table --> " + listData);
		
		try {
			String json = objectMapper.writeValueAsString(listData);
			model.addAttribute("data", json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
