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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.controller.CRUDController;
import com.transys.core.report.generator.ExcelReportGenerator;
import com.transys.core.report.generator.TransferStationIntakeReportGenerator;
import com.transys.core.util.MimeUtil;
import com.transys.core.util.ValidationUtil;
import com.transys.model.Customer;
import com.transys.model.CustomerNotes;
import com.transys.model.CustomerStatus;
import com.transys.model.CustomerType;
import com.transys.model.DeliveryAddress;
import com.transys.model.Dumpster;
import com.transys.model.DumpsterSize;
import com.transys.model.DumpsterStatus;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;
import com.transys.model.State;
import com.transys.model.User;
import com.transys.model.migration.OldCustomer;
import com.transys.model.migration.OldDeliveryAddress;
import com.transys.model.migration.OldDumpster;
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
	public void migrate(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "migrationDataType") String migrationDataType) {
		StringBuffer dataNotImportedBuff = new StringBuffer();
		if ("Customers".equals(migrationDataType)) {
			migrateCustomers(dataNotImportedBuff);
		} else if ("Dumpsters".equals(migrationDataType)) {
			migrateDumpsters(dataNotImportedBuff);
		}
		
		response.setHeader("Content-Disposition", "attachment;filename= " + "DataNotImported.txt");
		response.setContentType(MimeUtil.getContentType("txt"));
		
		try {
			ServletOutputStream out = response.getOutputStream();
			out.write(dataNotImportedBuff.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void migrateDumpsters(StringBuffer dataNotImportedBuff) {
		String oldDumpsterQuery = "select obj from OldDumpster obj order by obj.id asc";
		List<OldDumpster> oldDumpsterList = genericDAO.executeSimpleQuery(oldDumpsterQuery);
		
		String dumpsterSizeQuery = "select obj from DumpsterSize obj order by obj.id asc";
		List<DumpsterSize> dumpsterSizeList = genericDAO.executeSimpleQuery(dumpsterSizeQuery);
		Map<String, DumpsterSize> dumpsterSizeMap = new HashMap<String, DumpsterSize>();
		for (DumpsterSize aDumpsterSize : dumpsterSizeList) {
			dumpsterSizeMap.put(aDumpsterSize.getSize(), aDumpsterSize);
		}
		
		StringBuffer dumpsterDataNotImportedBuff = new StringBuffer("Old Dumpster Id|Dumpster Num|Error\n");
		for (OldDumpster anOldDumpster : oldDumpsterList) {
			Dumpster aNewDumpster = new Dumpster();
			map(anOldDumpster, aNewDumpster, dumpsterSizeMap);
			
			if (StringUtils.isEmpty(aNewDumpster.getDumpsterNum())) {
				dumpsterDataNotImportedBuff.append(anOldDumpster.getId())
				 						 			.append("|" + anOldDumpster.getDumpsterNum())
				 						 			.append("|" + "Data validation failed or inactive dumpster")
				 						 			.append("\n");
				continue;
			}
			
			try {
				genericDAO.save(aNewDumpster);
			} catch (PersistenceException e) {
				String errorMsg = extractDumpsterSaveErrorMsg(e);
				dumpsterDataNotImportedBuff.append(anOldDumpster.getId())
										 		   .append("|" + aNewDumpster.getDumpsterNum())
										 		   .append("|" + errorMsg)
										 		   .append("\n");
				
			}
		}
		
		dataNotImportedBuff.append(dumpsterDataNotImportedBuff);
	}

	private void map(OldDumpster anOldDumpster, Dumpster aNewDumpster, Map<String, DumpsterSize> dumpsterSizeMap) {
		if ("I".equals(anOldDumpster.getStatus())) {
			return;
		}
		
		if ("1".equals(anOldDumpster.getInRepair())) {
			return;
		}
		
		DumpsterSize dumpsterSize = dumpsterSizeMap.get(anOldDumpster.getDumpsterSize() + " yd");
		if (dumpsterSize == null) {
			return;
		}
		
		if (!ValidationUtil.validateDumpsterNum(anOldDumpster.getDumpsterNum(), 25)) {
			return;
		}
		
		aNewDumpster.setDumpsterSize(dumpsterSize);
		aNewDumpster.setDumpsterNum(anOldDumpster.getDumpsterNum());
		aNewDumpster.setCreatedBy(1l);
		
		DumpsterStatus status = new DumpsterStatus();
		status.setId(1l);
		aNewDumpster.setStatus(status);
		
		aNewDumpster.setComments(anOldDumpster.getComments());
	}
	
	private void migrateCustomers(StringBuffer dataNotImportedBuff) {
		String oldCustomerQuery = "select obj from OldCustomer obj order by obj.id asc";
		List<OldCustomer> oldCustomerList = genericDAO.executeSimpleQuery(oldCustomerQuery);
	
		String oldAddressQuery = "select obj from OldDeliveryAddress obj order by obj.id asc";
		List<OldDeliveryAddress> oldAddressList = genericDAO.executeSimpleQuery(oldAddressQuery);
		
		Map<Long, List<OldDeliveryAddress>> oldAddressMap = new HashMap<Long, List<OldDeliveryAddress>>(); 
		map(oldAddressList, oldAddressMap);
		
		StringBuffer customerDataNotImportedBuff = new StringBuffer("Old Customer Id|Company Name|Error\n");
		StringBuffer addressDataNotImportedBuff = new StringBuffer("Old Customer Id|Old Delivery Address Id|New customer Id|Address Line1|Error\n");
		for (OldCustomer anOldCustomer : oldCustomerList) {
			Customer aNewCustomer = new Customer();
			map(anOldCustomer, aNewCustomer);
			
			if (StringUtils.isEmpty(aNewCustomer.getCompanyName())) {
				customerDataNotImportedBuff.append(anOldCustomer.getId())
				 						 			.append("|" + anOldCustomer.getCompanyName())
				 						 			.append("|" + "Data validation failed")
				 						 			.append("\n");
				continue;
			}
			
			try {
				genericDAO.save(aNewCustomer);
				saveDeliveryAddress(aNewCustomer, anOldCustomer, oldAddressMap, addressDataNotImportedBuff);
				
				String customerAuditMsg = "Customer and delivery address uploaded";
				createAuditCustomerNotes(aNewCustomer, customerAuditMsg, aNewCustomer.getId());
			} catch (PersistenceException e) {
				String errorMsg = extractCustomerSaveErrorMsg(e);
				customerDataNotImportedBuff.append(anOldCustomer.getId())
										 		   .append("|" + aNewCustomer.getCompanyName())
										 		   .append("|" + errorMsg)
										 		   .append("\n");
				
			}
		}
		
		dataNotImportedBuff.append(customerDataNotImportedBuff)
								 .append("\n---------------------------------------------------------\n")
								 .append(addressDataNotImportedBuff);
	}
	
	private CustomerNotes createAuditCustomerNotes(Customer customer, String customerAuditMsg, Long createdBy) {
		CustomerNotes auditCustomerNotes = new CustomerNotes();
		auditCustomerNotes.setNotesType(CustomerNotes.NOTES_TYPE_AUDIT);
		auditCustomerNotes.setNotes("***AUDIT: " + customerAuditMsg + "***");
		
		Customer emptyCustomer = new Customer();
		emptyCustomer.setId(customer.getId());
		auditCustomerNotes.setCustomer(emptyCustomer);
		
		auditCustomerNotes.setCreatedAt(Calendar.getInstance().getTime());
		auditCustomerNotes.setCreatedBy(createdBy);
		updateEnteredBy(auditCustomerNotes);
		
		genericDAO.save(auditCustomerNotes);
		
		if (customer.getCustomerNotes() != null) {
			customer.getCustomerNotes().add(auditCustomerNotes);
		}
		
		return auditCustomerNotes;
	}
	
	private void updateEnteredBy(CustomerNotes entity) {
		User user = genericDAO.getById(User.class, entity.getCreatedBy());
		entity.setEnteredBy(user.getEmployee().getFullName());
	}
	
	private String extractCustomerSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "company")) {
			errorMsg = "Duplicate company name - company name already exists"; 
		} else {
			errorMsg = "Persistence exception while saving Customer";
		}
		
		return errorMsg;
	}
	
	private String extractDumpsterSaveErrorMsg(Exception e) {
		String errorMsg = StringUtils.EMPTY;
		if (isConstraintError(e, "dumpsterNum")) {
			errorMsg = "Duplicate dumpster num - company name already exists"; 
		} else {
			errorMsg = "Persistence exception while saving Dumpster";
		}
		
		return errorMsg;
	}
	
	private void saveDeliveryAddress(Customer aNewCustomer, OldCustomer anOldCustomer, 
			Map<Long, List<OldDeliveryAddress>> oldAddressMap, StringBuffer addressDataNotImportedBuff) {
		List<OldDeliveryAddress> oldAddressList = oldAddressMap.get(anOldCustomer.getId());
		if (oldAddressList == null || oldAddressList.isEmpty()) {
			return;
		}
		
		for (OldDeliveryAddress anOldDeliveryAddress : oldAddressList) {
			DeliveryAddress newAddress = new DeliveryAddress();
			map(anOldDeliveryAddress, newAddress);
			if(newAddress.getCreatedBy() == null) {
				addressDataNotImportedBuff.append(anOldCustomer.getId())
				  .append("|" + anOldDeliveryAddress.getId())
				  .append("|" + aNewCustomer.getId())
				  .append("|" + anOldDeliveryAddress.getLine1())
				  .append("|" +"Inactive status or Data Validation")
				  .append("\n");
				
				continue;
			}
			
			newAddress.setCustomer(aNewCustomer);
			try {
				genericDAO.save(newAddress);
			} catch (PersistenceException e) {
				addressDataNotImportedBuff.append(anOldCustomer.getId())
				 								  .append("|" + anOldDeliveryAddress.getId())
												  .append("|" + aNewCustomer.getId())
												  .append("|" + newAddress.getLine1())
												  .append("|" +"PersistenceException")
												  .append("\n");

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
		String oldCompanyName = oldCustomer.getCompanyName();
		if (StringUtils.isEmpty(oldCompanyName)) {
			return;
		}
		oldCompanyName = StringUtils.remove(oldCompanyName, '*');
		if (!ValidationUtil.validateCompanyName(oldCompanyName, 60)) {
			return;
		}
				
		String oldCustomerType = oldCustomer.getCustomerType();
		if (StringUtils.isEmpty(oldCustomerType) || "R".equals(oldCustomerType)) {
			return;
		}
		
		String oldCustomerStatus = oldCustomer.getCustomerStatus();
		if (StringUtils.isEmpty(oldCustomerStatus)) {
			return;
		}
		
		newCustomer.setCreatedBy(1l);
		newCustomer.setCompanyName(oldCompanyName);
		
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
		
		String oldContactName = oldCustomer.getContactName();
		if (!ValidationUtil.validateName(oldContactName, 100)) {
			oldContactName = StringUtils.EMPTY;
		}
		newCustomer.setContactName(oldContactName);
		
		String oldBillingAddressLine1 = oldCustomer.getBillingAddressLine1();
		if (!ValidationUtil.validateAddressLine(oldBillingAddressLine1, 50)) {
			oldBillingAddressLine1 = StringUtils.EMPTY;
		}
		newCustomer.setBillingAddressLine1(oldBillingAddressLine1);
		
		String oldBillingAddressLine2 = oldCustomer.getBillingAddressLine2();
		if (!ValidationUtil.validateAddressLine(oldBillingAddressLine2, 50)) {
			oldBillingAddressLine2 = StringUtils.EMPTY;
		}
		newCustomer.setBillingAddressLine2(oldBillingAddressLine2);
		
		String oldCity = oldCustomer.getCity();
		if (!ValidationUtil.validateName(oldCity, 50)) {
			oldCity = StringUtils.EMPTY;
		}
		newCustomer.setCity(oldCity);
		
		State newState = new State();
		newState.setId(1l);
		newCustomer.setState(newState);
		
		String oldZipcode = oldCustomer.getZipcode();
		if (!ValidationUtil.validateZipcode(oldZipcode)) {
			oldZipcode = StringUtils.EMPTY;
		}
		newCustomer.setZipcode(oldZipcode);
		
		String oldPhone = oldCustomer.getPhone();
		if (!ValidationUtil.validatePhone(oldPhone)) {
			oldPhone = StringUtils.EMPTY;
		}
		newCustomer.setPhone(oldPhone);
		
		String oldAltPhone1 = oldCustomer.getAltPhone1();
		if (!ValidationUtil.validatePhone(oldAltPhone1)) {
			oldAltPhone1 = StringUtils.EMPTY;
		}
		newCustomer.setAltPhone1(oldAltPhone1);
		
		String oldAltPhone2 = oldCustomer.getAltPhone2();
		if (!ValidationUtil.validatePhone(oldAltPhone2)) {
			oldAltPhone2 = StringUtils.EMPTY;
		}
		newCustomer.setAltPhone2(oldAltPhone2);
		
		String oldFax = oldCustomer.getFax();
		if (!ValidationUtil.validatePhone(oldFax)) {
			oldFax = StringUtils.EMPTY;
		}
		newCustomer.setFax(oldFax);
		
		String oldEmail = oldCustomer.getEmail();
		if (!ValidationUtil.validateEmail(oldEmail)) {
			oldEmail = StringUtils.EMPTY;
		}
		newCustomer.setEmail(oldEmail);
	}
	
	private void map(OldDeliveryAddress oldAddress, DeliveryAddress newAddress) {
		String oldStatus = oldAddress.getStatus();
		if (StringUtils.isEmpty(oldStatus) || "I".equals(oldStatus)) {
			return;
		}
		
		newAddress.setCreatedBy(1l);
		
		String oldLine1 = oldAddress.getLine1();
		if (!ValidationUtil.validateAddressLine(oldLine1, 50)) {
			oldLine1 = StringUtils.EMPTY;
		}
		newAddress.setLine1(oldLine1);
		
		String oldLine2 = oldAddress.getLine2();
		if (!ValidationUtil.validateAddressLine(oldLine2, 50)) {
			oldLine2 = StringUtils.EMPTY;
		}
		newAddress.setLine2(oldLine2);
		
		String oldCity = oldAddress.getCity();
		if (!ValidationUtil.validateName(oldCity, 50)) {
			oldCity = StringUtils.EMPTY;
		}
		newAddress.setCity(oldCity);
		
		State newState = new State();
		newState.setId(1l);
		newAddress.setState(newState);
		
		String oldZipcode = oldAddress.getZipcode();
		if (!ValidationUtil.validateZipcode(oldZipcode)) {
			oldZipcode = StringUtils.EMPTY;
		}
		newAddress.setZipcode(oldZipcode);
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		List<String> migrationDataTypeList = new ArrayList<String>();
		migrationDataTypeList.add("Customers");
		migrationDataTypeList.add("Dumpsters");
		model.addAttribute("migrationDataTypeList", migrationDataTypeList);
	}
}
