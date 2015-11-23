package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.model.Dumpster;
import com.transys.model.DumpsterSize;
import com.transys.model.DumpsterStatus;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/dumpsterRentedReport")
public class DumpsterRentedReportController extends CRUDController<Dumpster> {
	public DumpsterRentedReportController() {	
		setUrlContext("reports/dumpsterRentedReport");
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		String query = "select obj from DumpsterStatus obj where obj.status = 'Dropped Off' and delete_flag=1";
		model.addAttribute("dumpsterStatus", genericDAO.executeSimpleQuery(query));
		model.addAttribute("dumpsterSizes", genericDAO.findAll(DumpsterSize.class));
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		criteria.getSearchMap().put("status.status", "Dropped Off");
		List<Dumpster> dumpsterInfoList = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		
		setDeliveryDetailsForDumpster(dumpsterInfoList);
		model.addAttribute("dumpsterInfoList", dumpsterInfoList);
		
		return urlContext + "/list";
	}


	private void setDeliveryDetailsForDumpster(List<Dumpster> dumpsterInfoList) {
		for (Dumpster aDumpster : dumpsterInfoList) {
			// get the latest delivery address & delivery date for the corresponding dumpster# from transysOrder table
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.put("dumpster.id", aDumpster.getId());
			criterias.put("orderStatus.status", "Dropped Off");
			List<Order> ordersForDumpster = genericDAO.findByCriteria(Order.class, criterias, "id", true);
			System.out.println("List of orders for this dumpster = " + ordersForDumpster.size());
			
			//List<?> deliveryDetailsForDumpster = genericDAO.executeSimpleQuery("select obj from Order p where p.dumpster.id = " + aDumpster.getId() + " order by p.id desc");
			if (ordersForDumpster.isEmpty()) {
				// do nothing
				continue;
			}
			
			Order orderForDumpster = ordersForDumpster.get(0);

			// set 2 transient fields for deliveryAddress and deliveryDate in DumpsterInfo
			aDumpster.setDeliveryAddress(orderForDumpster.getDeliveryAddress().getFullLine());
			System.out.println("Setting delivery address = " + orderForDumpster.getDeliveryAddress().getFullLine());
			aDumpster.setDeliveryDate(orderForDumpster.getFormattedDeliveryDate());
			System.out.println("Setting delivery date = " + orderForDumpster.getFormattedDeliveryDate());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//criteria.getSearchMap().put("id!",0l);
		//model.addAttribute("dumpsterInfoList", genericDAO.search(getEntityClass(), criteria, "id", null, null));
		return urlContext + "/list";
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateDumpsterRentedReport.do")
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		try {
			List<Map<String,Object>> reportData = prepareReportData(model, request);
			type = setRequestHeaders(response, type, "dumpsterRentedReport");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = new HashMap<String, Object>();

			out = dynamicReportService.generateStaticReport("dumpsterRentedReport", reportData, params, type, request);
			/*} else {
				out = dynamicReportService.generateStaticReport("dumpsterRentedReport" + "print", reportData, params, type,
						request);
			}*/

			out.writeTo(response.getOutputStream());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());

		}
	}
	
	private List<Map<String, Object>> prepareReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
	
		List<Dumpster> dumpsterInfoList = genericDAO.search(getEntityClass(), criteria, "id", null, null);
		setDeliveryDetailsForDumpster(dumpsterInfoList);
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Dumpster aDumpster : dumpsterInfoList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dumpsterSize", aDumpster.getDumpsterSize().getSize());
			map.put("dumpsterNum", aDumpster.getDumpsterNum());
			map.put("status", aDumpster.getStatus().getStatus());
			map.put("deliveryAddress", StringUtils.defaultIfEmpty(aDumpster.getDeliveryAddress(), StringUtils.EMPTY));
			map.put("deliveryDate", StringUtils.defaultIfEmpty(aDumpster.getDeliveryDate(), StringUtils.EMPTY));
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jSonResponse = StringUtils.EMPTY;
			try {
				jSonResponse = objectMapper.writeValueAsString(map);
				System.out.println(jSonResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			reportData.add(map);
		}
		return reportData;
	}

}
