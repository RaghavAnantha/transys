package com.transys.controller.migration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
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
import com.transys.model.Customer;
import com.transys.model.CustomerStatus;
import com.transys.model.CustomerType;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.migration.OldCustomer;
import com.transys.model.migration.OldDeliveryAddress;
import com.transys.model.vo.MonthlyIntakeReportVO;
import com.transys.model.vo.PublicMaterialIntakeVO;
import com.transys.model.vo.RollOffBoxesPerYardVO;

@Controller
@RequestMapping("/migration")
public class MigrationController extends CRUDController<Order> {
	public MigrationController(){	
		setUrlContext("migration");
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

	@RequestMapping(method = RequestMethod.POST, value = "/migrate.do")
	public String migrate(ModelMap model, HttpServletRequest request) {
		String oldCustomerQuery = "select obj from OldCustomer obj order by obj.id asc";
		List<OldCustomer> oldCustomerList = genericDAO.executeSimpleQuery(oldCustomerQuery);
	
		String oldAddressQuery = "select obj from OldDeliveryAddress obj order by obj.id asc";
		List<OldDeliveryAddress> oldAddressList = genericDAO.executeSimpleQuery(oldAddressQuery);
		
		Map<Long, List<OldDeliveryAddress>> oldAddressMap = new HashMap<Long, List<OldDeliveryAddress>>(); 
		map(oldAddressList, oldAddressMap);
		
		for (OldCustomer anOldCustomer : oldCustomerList) {
			Customer aNewCustomer = new Customer();
			map(anOldCustomer, aNewCustomer);
			
			if (!StringUtils.isEmpty(aNewCustomer.getCompanyName())) {
				try {
					genericDAO.save(aNewCustomer);
					saveDeliveryAddress(aNewCustomer, anOldCustomer, oldAddressMap);
				} catch (PersistenceException e) {
					if (e.getCause() instanceof ConstraintViolationException) {
						ConstraintViolationException ce = (ConstraintViolationException) e.getCause();
						if (ce.getConstraintName().contains("company")) {
							continue;
						}
					} 
				}
			}
		}
		
		setupList(model, request);
		return urlContext + "/list";
	}
	
	private void saveDeliveryAddress(Customer aNewCustomer, OldCustomer anOldCustomer, Map<Long, List<OldDeliveryAddress>> oldAddressMap) {
		List<OldDeliveryAddress> oldAddressList = oldAddressMap.get(anOldCustomer.getId());
		if (oldAddressList == null || oldAddressList.isEmpty()) {
			return;
		}
		
		for (OldDeliveryAddress anOldDeliveryAddress : oldAddressList) {
			DeliveryAddress newAddress = new DeliveryAddress();
			map(anOldDeliveryAddress, newAddress);
			newAddress.setCustomer(aNewCustomer);
			
			try {
				genericDAO.save(newAddress);
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
		}
	}

	private void map(List<OldDeliveryAddress> oldAddressList, Map<Long, List<OldDeliveryAddress>> oldAddressMap) {
		for (OldDeliveryAddress anOldAddress : oldAddressList) {
			List<OldDeliveryAddress> custAddressList = oldAddressMap.get(anOldAddress.getCustID());
			if (custAddressList == null) {
				custAddressList = new ArrayList<OldDeliveryAddress>();
				oldAddressMap.put(anOldAddress.getCustID(), custAddressList);
			}
			
			custAddressList.add(anOldAddress);
		}
	}
	
	private void map(OldCustomer oldCustomer, Customer newCustomer) {
		String oldCustomerType = oldCustomer.getCustomerType();
		if (StringUtils.isEmpty(oldCustomerType) || "R".equals(oldCustomerType)) {
			return;
		}
		
		String oldCustomerStatus = oldCustomer.getCustomerStatus();
		if (StringUtils.isEmpty(oldCustomerStatus)) {
			return;
		}
		
		newCustomer.setCreatedBy(1l);
		
		newCustomer.setCompanyName(oldCustomer.getCompanyName());
		
		CustomerType newCustomerType = new CustomerType();
		newCustomerType.setId(1l);
		newCustomer.setCustomerType(newCustomerType);
		
		String newChargeCompany = StringUtils.EMPTY;
		String oldChargeCompany = oldCustomer.getChargeCompany();
		if (StringUtils.isEmpty(oldChargeCompany)) {
			newChargeCompany = "No";
		} else {
			if ("Y".equals(oldChargeCompany)) {
				newChargeCompany = "Yes";
			} else {
				newChargeCompany = "No";
			}
		}
		newCustomer.setChargeCompany(newChargeCompany);
		
		CustomerStatus newCustomerStatus = new CustomerStatus();
		if ("A".equals(oldCustomerStatus)) {
			newCustomerStatus.setId(1l);
		} else {
			newCustomerStatus.setId(2l);
		}
		newCustomer.setCustomerStatus(newCustomerStatus);
		
		newCustomer.setContactName(oldCustomer.getContactName());
		newCustomer.setBillingAddressLine1(oldCustomer.getBillingAddressLine1());
		newCustomer.setBillingAddressLine2(oldCustomer.getBillingAddressLine2());
		newCustomer.setCity(oldCustomer.getCity());
		
		State newState = new State();
		newState.setId(1l);
		newCustomer.setState(newState);
		
		newCustomer.setZipcode(oldCustomer.getZipcode());
		
		String oldPhone = oldCustomer.getPhone();
		if (!validatePhone(oldPhone)) {
			oldPhone = StringUtils.EMPTY;
		}
		newCustomer.setPhone(oldPhone);
		
		String oldAltPhone1 = oldCustomer.getAltPhone1();
		if (!validatePhone(oldAltPhone1)) {
			oldAltPhone1 = StringUtils.EMPTY;
		}
		newCustomer.setAltPhone1(oldAltPhone1);
		
		String oldAltPhone2 = oldCustomer.getAltPhone2();
		if (!validatePhone(oldAltPhone2)) {
			oldAltPhone2 = StringUtils.EMPTY;
		}
		newCustomer.setAltPhone2(oldAltPhone2);
		
		String oldFax = oldCustomer.getFax();
		if (!validatePhone(oldFax)) {
			oldFax = StringUtils.EMPTY;
		}
		newCustomer.setFax(oldFax);
		
		newCustomer.setEmail(oldCustomer.getEmail());
	}
	
	private void map(OldDeliveryAddress oldAddress, DeliveryAddress newAddress) {
		newAddress.setCreatedBy(1l);
		
		newAddress.setLine1(oldAddress.getLine1());
		newAddress.setLine2(oldAddress.getLine2());
		newAddress.setCity(oldAddress.getCity());
		
		State newState = new State();
		newState.setId(1l);
		newAddress.setState(newState);
		
		newAddress.setZipcode(oldAddress.getZipcode());
	}
	
	private boolean validatePhone(String phone) {
		//Regex
		return phone.matches("^[2-9]{1}\\d{2}(-)[2-9]{1}\\d{2}(-)\\d{4}$");
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
	}
}
