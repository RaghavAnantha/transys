package com.transys.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.transys.core.util.FormatUtil;
import com.transys.core.util.ModelUtil;
import com.transys.model.Customer;
import com.transys.model.LocationType;
import com.transys.model.Order;
import com.transys.model.OrderPermits;
import com.transys.model.Permit;
import com.transys.model.PermitClass;
import com.transys.model.PermitStatus;
import com.transys.model.PermitType;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.vo.DeliveryAddressVO;

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
		criteria.setPageSize(50);
		
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
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		/*
		String deliveryAddressQuery = "select distinct obj.line1 from DeliveryAddress obj where obj.deleteFlag='1' and obj.line1 != '' order by obj.line1 asc";
		model.addAttribute("deliveryAddressesLine1", genericDAO.executeSimpleQuery(deliveryAddresseQuery));
		
	   deliveryAddressQuery = "select distinct obj.line2 from DeliveryAddress obj where obj.deleteFlag='1' and obj.line2 != '' order by obj.line2 asc";
		model.addAttribute("deliveryAddressesLine2", genericDAO.executeSimpleQuery(deliveryAddresseQuery));
		*/
		
		String deliveryAddressQuery = "select distinct obj.deliveryAddress.id, obj.deliveryAddress.line1, obj.deliveryAddress.line2"
				+ " from Order obj where obj.deleteFlag='1' order by obj.deliveryAddress.line1 asc";
		List<?> objectList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.mapToDeliveryAddressVO(objectList);
		model.addAttribute("deliveryAddresses", deliveryAddressVOList);
	
		//model.addAttribute("order", genericDAO.executeSimpleQuery("select obj.id from Order obj where obj.deleteFlag='1' order by obj.id asc"));
		
		model.addAttribute("orderStatuses", genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.deleteFlag='1' and obj.status != 'Closed'"));
		//model.addAttribute("orderStatuses", genericDAO.findByCriteria(OrderStatus.class, criterias, "status", false));
		
		//model.addAttribute("locationType", genericDAO.findByCriteria(LocationType.class, criterias, "id", false));
		//model.addAttribute("permitClass", genericDAO.findByCriteria(PermitClass.class, criterias, "permitClass", false));
		//model.addAttribute("permitType", genericDAO.findByCriteria(PermitType.class, criterias, "permitType", false));
		//model.addAttribute("state", genericDAO.findAll(State.class, true));
		
		model.addAttribute("permitStatus", genericDAO.findByCriteria(PermitStatus.class, criterias, "status", false));
		
		String permitQuery = "select obj.number from Permit obj where obj.deleteFlag='1' order by obj.number asc";
		model.addAttribute("permit", genericDAO.executeSimpleQuery(permitQuery));
		
		//List<Customer> customerList = genericDAO.findByCriteria(Customer.class, criterias, "companyName", false);
		//model.addAttribute("customer", customerList);
		
		/*SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (Customer aCustomer : customerList) {
			phoneSet.add(aCustomer.getPhone());
			contactNameSet.add(aCustomer.getContactName());
		}
		
		model.addAttribute("phone", phoneSet);	
		model.addAttribute("contactName", contactNameSet);*/
		
		//List<OrderPermits> orderPermits = getToBeAlertedPermits(new SearchCriteria());
		//model.addAttribute("orderPermitList", orderPermits);
	}	
	
	@RequestMapping(method = RequestMethod.GET, value = "/list.do")
	public String list(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(50);
		
		//injectPendingPaymentPermitSearch(criteria);
		
		// Search for permits corresponding to open orders and status=expired or expiring in the next 7 days
		List<OrderPermits> orderPermits = getToBeAlertedPermits(criteria);
		System.out.println("Number of matching permits = " + orderPermits.size());
		model.addAttribute("orderPermitList", orderPermits);
		
		model.addAttribute("activeTab", "orderPermitAlert");
		return urlContext + "/list";
	}
	
	private void injectPendingPaymentPermitSearch(SearchCriteria criteria) {
		if (criteria != null && criteria.getSearchMap() != null) {
			Map<String, Object> searchMap = criteria.getSearchMap();
			Object[] param = searchMap.keySet().toArray();
			for (int i = 0; i < param.length; i++) {
				String key = param[i].toString();
				if(key.toUpperCase().contains("NUMBER") && searchMap.get(key).toString().equalsIgnoreCase(Permit.EMPTY_PERMIT_NUMBER) ) {
					searchMap.put("permit.number", "null");
				}
			}
			criteria.setSearchMap(searchMap);
		}
	}
	
	/**
	 * Return the list of permits associated with open orders, expired or expiring in the next 7 days
	 * 
	 * @param searchCriteria
	 * @return
	 */
	private List<OrderPermits> getToBeAlertedPermits(SearchCriteria searchCriteria) {
			Object existingSearchValue = setupOrderPermitSearchCriteria(searchCriteria);
			
			List<OrderPermits> orderPermits = genericDAO.search(OrderPermits.class, searchCriteria, "id", null, null);
			System.out.println("Order Permits Size = " + orderPermits.size());

			/**
			 *  for each orderPermit,
			 *  a) get orderID
			 *  b) get # of permits associated with this orderID, if count == 3, AlertFlag
			 *  c) for each of the permits associated, if at least 1 valid permit available, do not add to list
			 */
			
			Map<Long, List<OrderPermits>> orderPermitsSortedOnOrderId = new HashMap<>();
			List<OrderPermits> expiredOrderPermitEntriesForOrder = null;
			for (OrderPermits op : orderPermits) {
				System.out.println("Order ID = " + op.getOrder().getId());
				if (!orderPermitsSortedOnOrderId.containsKey(op.getOrder().getId())) {
					expiredOrderPermitEntriesForOrder = new ArrayList<>();
					expiredOrderPermitEntriesForOrder.add(op);
				} else {
					expiredOrderPermitEntriesForOrder = orderPermitsSortedOnOrderId.get(op.getOrder().getId());
					expiredOrderPermitEntriesForOrder.add(op);
					System.out.println("Added");
				}
				orderPermitsSortedOnOrderId.put(op.getOrder().getId(), expiredOrderPermitEntriesForOrder);
			}
			
			List<OrderPermits> expiredOrderPermits = new ArrayList<>();
			// for each orderPermitsSortedOnOrderId, ensure that the corresponding permits for this order has atleast 1 unexpired permit
			for (Long orderId : orderPermitsSortedOnOrderId.keySet()) {
				List<OrderPermits> orderPermitsForThisOrder = orderPermitsSortedOnOrderId.get(orderId);
				
				SearchCriteria criteria = new SearchCriteria();
				criteria.getSearchMap().put("order.id", orderId);
				setupOrderPermitSearchCriteria(criteria);
				List<OrderPermits> actualPermitsForOrder = genericDAO.search(OrderPermits.class, criteria, "id", null, null);
				
				if (unExpiredPermitAvailable(orderId, actualPermitsForOrder)) {
					continue;
				}
				
				expiredOrderPermits.addAll(orderPermitsForThisOrder);
			}
			
			System.out.println("Order Permits Size (after expiry logic) = " + expiredOrderPermits.size());
			
			cleanOrderPermitSearchCriteria(searchCriteria, existingSearchValue);
			return expiredOrderPermits;
	}
	
	private boolean unExpiredPermitAvailable(Long orderId, List<OrderPermits> orderPermits) {
		if (orderPermits.size() == 1) {
			// this is the only permit available for this order and it is expired
			return false;
		}
		
		Order currentOrder = orderPermits.get(0).getOrder();
		if (currentOrder.getPermits().size() == orderPermits.size()) {
			// all permits of the current order are expired
			return false;
		} else if (currentOrder.getPermits().size() > orderPermits.size()) {
			// order has atleast 1 permit not available in expired order list, so it is a valid permit
			return true;
		}
		
		boolean isExpiredPermit = false;
		for (Permit p : currentOrder.getPermits()) { // for every permit of the order
			for (OrderPermits expiredP : orderPermits) { // check if the permit is in the expired list
				if (p.getId() == expiredP.getId()) {
					isExpiredPermit = true;
				}
			}
			
			if (!isExpiredPermit) {
				// atleast 1 non-expired permit is available, return
				return true;
			}
		}
			
		return false;
	}

	private Object setupOrderPermitSearchCriteria(SearchCriteria searchCriteria) {
		Object existingSearchValue = null;
		Map searchMap = searchCriteria.getSearchMap();
		
		if (searchMap.containsKey("order.orderStatus.status") && !StringUtils.isEmpty(searchMap.get("order.orderStatus.status").toString())) {
			existingSearchValue = searchMap.get("order.orderStatus.status");
			System.out.println("Existing Search Value = " + existingSearchValue);
			// append these to it, store the existing search to be reset after the search
			
		} else {
			searchMap.put("||1order.orderStatus.status", "Open");
			searchMap.put("||2order.orderStatus.status", "Dropped Off");
		}
		
		// permit EndDate <= Today + 7 days
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		searchMap.put("permit.endDate", "<=" + FormatUtil.inputDateFormat.format(cal.getTime()));
		
		searchCriteria.setSearchMap(searchMap);
		return existingSearchValue;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/calculatePermitEndDate")
	public @ResponseBody String calculatePermitEndDate(ModelMap model, HttpServletRequest request) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateInString = request.getParameter("startDate");
		Date startDateObj = null;
		try {
			startDateObj = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String permitType = request.getParameter("permitType");
		PermitType permitTypeObj = (PermitType) genericDAO.executeSimpleQuery("select obj from PermitType obj where obj.deleteFlag='1' and obj.id=" + permitType).get(0);
		if (startDateObj != null && permitTypeObj.getPermitType() != null) {
			String tokens[] = permitTypeObj.getPermitType().split("\\s");
			int noOfDays = new Integer(tokens[0]).intValue();
			Date endDate = DateUtils.addDays(startDateObj, noOfDays);
			System.out.println("End date = " + endDate);
			return formatter.format(endDate);
		} else {
			return null;
		}
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
