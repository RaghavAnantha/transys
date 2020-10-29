package com.transys.controller.report;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import com.transys.core.util.ModelUtil;

import com.transys.model.Customer;
import com.transys.model.CustomerStatus;
import com.transys.model.SearchCriteria;

import com.transys.model.vo.CustomerReportVO;
import com.transys.model.vo.CustomerVO;

@Controller
@RequestMapping("/reports/customer/customerListReport")
public class CustomerListReportController extends ReportController {
	public CustomerListReportController(){	
		setUrlContext("reports/customer/customerListReport");
		setMessageCtx("customerListReport");
		
		setReportName("customerListReport");
	}
	
	@Override
	protected int getCriteriaSearchPageSize() {
		return 750;
	}
	
	@Override
	protected int getCriteriaExportPageSize() {
		return 3500;
	}
	
	@Override
	protected void setupList(ModelMap model, HttpServletRequest request) {
		super.setupList(model, request);
		
		List<CustomerVO> customerVOList = ModelUtil.retrieveCustomers(genericDAO);
		model.addAttribute("customers", customerVOList);
		
		String[][] strArrOfArr = ModelUtil.extractContactDetails(customerVOList);
		String[] phoneArr = strArrOfArr[0];
		String[] contactNameArr = strArrOfArr[1];
		
		model.addAttribute("phones", phoneArr);
		model.addAttribute("contactNames", contactNameArr);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		model.addAttribute("customerStatuses", genericDAO.findByCriteria(CustomerStatus.class, criterias, "status", false));
	}
	
	@Override
	protected List<CustomerReportVO> performSearch(ModelMap model, HttpServletRequest request, SearchCriteria criteria, Map<String, Object> params) {
		List<CustomerReportVO> customerReportVOList = retrieveCustomerListReportData(criteria);
		return customerReportVOList;
	}
	
	private List<CustomerReportVO> retrieveCustomerListReportData(SearchCriteria criteria) {
		List<CustomerReportVO> customerReportVOList = new ArrayList<CustomerReportVO>();
		
		List<Customer> customerList = genericDAO.search(Customer.class, criteria, "companyName", null, null);
		if (customerList.isEmpty()) {
			return customerReportVOList;
		}
		
		Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
		StringBuffer ids = new StringBuffer("(");
		Integer count = 0;
		for (Customer customer : customerList) {
			count++;
			ids.append(customer.getId());
			if (count < customerList.size()) {
				ids.append(",");
			} else {
				ids.append(")");
			}
			
			customerMap.put(customer.getId(), customer);
		}
        
      List<?> resultObjectList = genericDAO.executeSimpleQuery("select obj.order.customer.id, obj.totalFees from OrderFees obj where obj.deleteFlag='1'"
      		+ " and obj.order.customer.id IN " + ids.toString());
      
      for (Long key : customerMap.keySet()) {
      	BigDecimal sum = new BigDecimal("0.00");
			Integer orderCount = 0;
			for (Object[] resultObj : (List<Object[]>)resultObjectList) {
	          if (((Long)resultObj[0]).longValue() == key.longValue()) {
	               sum = sum.add((BigDecimal)resultObj[1]);
	               orderCount++;
	          }
	      }
			
			Customer aCustomer = customerMap.get(key);
			CustomerReportVO customerReportVO =  new CustomerReportVO();
			map(aCustomer, customerReportVO);
         customerReportVO.setTotalOrderAmount(sum);
         customerReportVO.setTotalOrders(orderCount);
         customerReportVOList.add(customerReportVO);
	   }
      
      ModelUtil.sort(customerReportVOList);
      return customerReportVOList;
	}
	
	private void map(Customer aCustomer, CustomerReportVO aCustomerReportVO) {
		aCustomerReportVO.setId(aCustomer.getId());
		aCustomerReportVO.setCompanyName(aCustomer.getCompanyName());
		aCustomerReportVO.setStatus(aCustomer.getCustomerStatus().getStatus());
		aCustomerReportVO.setContactName(aCustomer.getContactName());
		aCustomerReportVO.setPhoneNumber(aCustomer.getPhone());
	}
}
