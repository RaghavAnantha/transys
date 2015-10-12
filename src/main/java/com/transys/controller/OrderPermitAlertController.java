package com.transys.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.transys.model.BaseModel;
import com.transys.model.Customer;
import com.transys.model.DeliveryAddress;
import com.transys.model.LocationType;
import com.transys.model.Order;
import com.transys.model.OrderPermits;
import com.transys.model.OrderStatus;
import com.transys.model.Permit;
import com.transys.model.PermitAddress;
import com.transys.model.PermitClass;
import com.transys.model.PermitNotes;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;
import com.transys.model.State;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/orderPermitAlert")
public class OrderPermitAlertController extends CRUDController<OrderPermits> {
	
	public OrderPermitAlertController(){
		setUrlContext("orderPermitAlert");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		// Search for permits corresponding to open orders and status=expired or expiring in the next 7 days
		List<OrderPermits> orderPermits = getToBeAlertedPermits(criteria);
		model.addAttribute("orderPermitList", orderPermits);
		
		model.addAttribute("activeTab", "orderPermitAlert");
		return urlContext + "/list";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		List<DeliveryAddress> addresses = genericDAO.findUniqueByCriteria(DeliveryAddress.class, criterias, "line1", false);
	   model.addAttribute("allDeliveryAddresses", addresses);
		model.addAttribute("customer", genericDAO.findByCriteria(Customer.class, criterias, "contactName", false));
		model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		model.addAttribute("order", genericDAO.findByCriteria(Order.class, criterias, "id", false));
		model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		model.addAttribute("permit", genericDAO.findByCriteria(Permit.class, criterias, "id", false));
		model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		model.addAttribute("state", genericDAO.findAll(State.class));

		List<OrderPermits> orderPermits = getToBeAlertedPermits(new SearchCriteria());
		model.addAttribute("orderPermitList", orderPermits);

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		System.out.println(criteria.getSearchMap());
		
		// Search for permits corresponding to open orders and status=expired or expiring in the next 7 days
		List<OrderPermits> orderPermits = getToBeAlertedPermits(criteria);
		System.out.println("Number of matching permits = " + orderPermits.size());
		model.addAttribute("orderPermitList", orderPermits);
		
		model.addAttribute("activeTab", "orderPermitAlert");
		return urlContext + "/list";
	}
	
	/**
	 * Return the list of permits associated with open orders, expired or expiring in the next 7 days
	 * 
	 * @param searchCriteria
	 * @return
	 */
	private List<OrderPermits> getToBeAlertedPermits(SearchCriteria searchCriteria) {
			// TODO: open orders --> include DroppedOff?
			Object existingSearchValue = setupOrderPermitSearchCriteria(searchCriteria);
			
			List<OrderPermits> orderPermits = genericDAO.search(OrderPermits.class, searchCriteria, "id", null, null);
			System.out.println("Order Permits Size = " + orderPermits.size());

			cleanOrderPermitSearchCriteria(searchCriteria, existingSearchValue);
	
			return orderPermits;
			
	}
	
	private Object setupOrderPermitSearchCriteria(SearchCriteria searchCriteria) {
		
		Object existingSearchValue = null;
		Map searchMap = searchCriteria.getSearchMap();
		
		if (searchMap.containsKey("order.orderStatus.status") && !StringUtils.isEmpty(searchMap.get("order.orderStatus.status").toString())) {
			existingSearchValue = searchMap.get("order.orderStatus.status");
			System.out.println("Existing Search Value = " + existingSearchValue);
			// append these to it, store the existing search to be reset after the search
			
		} else {
			searchMap.put("order.orderStatus.status", "Open");
			//searchMap.put("||order.orderStatus.status", "Dropped-off");
		}
		
		// permit EndDate <= Today + 7 days
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		searchMap.put("permit.endDate", "<=" + BaseController.dateFormat.format(cal.getTime()));
		
		searchCriteria.setSearchMap(searchMap);
		return existingSearchValue;
	}
	
	private void cleanOrderPermitSearchCriteria(SearchCriteria searchCriteria, Object existingSearchValue) {
		Map searchMap = searchCriteria.getSearchMap();
		if (existingSearchValue == null) {
			// remove the key
			searchMap.remove("existingSearchValue");
		} else {
			// reset the value
			searchMap.put("order.orderStatus.status", existingSearchValue);
		}
		
		// TODO: Filter on endDate already available in SearchCriteria
		searchMap.remove("permit.endDate");
		searchCriteria.setSearchMap(searchMap);
	}
}
