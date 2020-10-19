package com.transys.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;

import com.transys.core.dao.GenericDAO;
import com.transys.model.Customer;
import com.transys.model.DumpsterSize;
import com.transys.model.DumpsterStatus;
import com.transys.model.Order;
import com.transys.model.OrderFees;
import com.transys.model.OrderNotes;
import com.transys.model.OrderStatus;
import com.transys.model.User;
import com.transys.model.vo.CustomerReportVO;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.DeliveryAddressVO;
import com.transys.model.vo.OrderReportVO;
import com.transys.model.vo.PermitAddressVO;

public class ModelUtil {
	public static OrderNotes createAuditOrderNotes(GenericDAO genericDAO, Order order, String orderAuditMsg,
					User createdByUser) {
		OrderNotes auditOrderNotes = createAuditOrderNotes(genericDAO, order.getId(), orderAuditMsg, createdByUser);
		order.getOrderNotes().add(auditOrderNotes);
		return auditOrderNotes;
	}
	
	public static void createAuditOrderNotes(GenericDAO genericDAO, String[] orderIdsArr, String orderAuditMsg,
			User createdByUser) {
		for (String orderIdStr : orderIdsArr) {
			Long orderId = new Long(orderIdStr);
			createAuditOrderNotes(genericDAO, orderId, orderAuditMsg, createdByUser);
		}
	}
	
	public static OrderNotes createAuditOrderNotes(GenericDAO genericDAO, Long orderId, String orderAuditMsg,
			User createdByUser) {
		OrderNotes auditOrderNotes = new OrderNotes();
		auditOrderNotes.setNotesType(OrderNotes.NOTES_TYPE_AUDIT);
		auditOrderNotes.setNotes(OrderNotes.AUDIT_MSG_PREFIX + orderAuditMsg + OrderNotes.AUDIT_MSG_SUFFIX);
		
		Order emptyOrder = new Order();
		emptyOrder.setId(orderId);
		auditOrderNotes.setOrder(emptyOrder);
		
		auditOrderNotes.setCreatedAt(Calendar.getInstance().getTime());
		auditOrderNotes.setCreatedBy(createdByUser.getId());
		auditOrderNotes.setEnteredBy(createdByUser.getEmployee().getFullName());
		
		genericDAO.save(auditOrderNotes);
		
		return auditOrderNotes;
	}
	
	public static OrderStatus retrieveOrderStatus(GenericDAO genericDAO, String status) {
		OrderStatus orderStatus = (OrderStatus)genericDAO.executeSimpleQuery("select obj from OrderStatus obj where obj.deleteFlag='1' and obj.status='" + status + "'").get(0);
		return orderStatus;
	}
	
	public static List<DeliveryAddressVO> mapToDeliveryAddressVO(List<?> objectList) {
		List<DeliveryAddressVO> deliveryAddressVOList = new ArrayList<DeliveryAddressVO>();
		if (objectList == null || objectList.isEmpty()) {
			return deliveryAddressVOList;
		}
		
		for (int i = 0; i < objectList.size(); i++) {
			Object anObject[] = (Object[])objectList.get(i);
			DeliveryAddressVO aDeliveryAddressVO = new DeliveryAddressVO();
			aDeliveryAddressVO.setId((Long)anObject[0]);
			if (anObject[1] != null && StringUtils.isNotEmpty(anObject[1].toString())) {
				aDeliveryAddressVO.setLine1(anObject[1].toString());
			}
			if (anObject[2] != null && StringUtils.isNotEmpty(anObject[2].toString())) {
				aDeliveryAddressVO.setLine2(anObject[2].toString());
			}
			deliveryAddressVOList.add(aDeliveryAddressVO);
		}
		return deliveryAddressVOList;
	}
	
	public static List<PermitAddressVO> mapToPermitAddressVO(List<?> objectList) {
		List<PermitAddressVO> permitAddressVOList = new ArrayList<PermitAddressVO>();
		if (objectList == null || objectList.isEmpty()) {
			return permitAddressVOList;
		}
		
		for (int i = 0; i < objectList.size(); i++) {
			Object anObject[] = (Object[])objectList.get(i);
			PermitAddressVO aPermitAddressVO = new PermitAddressVO();
			aPermitAddressVO.setId((Long)anObject[0]);
			if (anObject[1] != null && StringUtils.isNotEmpty(anObject[1].toString())) {
				aPermitAddressVO.setLine1(anObject[1].toString());
			}
			if (anObject[2] != null && StringUtils.isNotEmpty(anObject[2].toString())) {
				aPermitAddressVO.setLine2(anObject[2].toString());
			}
			permitAddressVOList.add(aPermitAddressVO);
		}
		return permitAddressVOList;
	}
	
	public static List<CustomerVO> retrieveCustomers(GenericDAO genericDAO) {
		List<Customer> customerList = genericDAO.executeSimpleQuery("select obj from Customer obj where obj.deleteFlag='1'"
				+ " order by obj.companyName asc");
		List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
		map(customerVOList, customerList);
		return customerVOList;
	}
	
	public static void map(List<CustomerVO> customerVOList, List<Customer> customerList) {
		if (customerList == null || customerList.isEmpty()) {
			return;
		}
		
		for (Customer aCustomer : customerList) {
			CustomerVO aCustomerVO = new CustomerVO();
			map(aCustomerVO, aCustomer);
			customerVOList.add(aCustomerVO);
		}
	}
	
	public static List<CustomerVO> retrieveOrderCustomers(GenericDAO genericDAO) {
		String customerQuery = "select distinct obj.customer.id, obj.customer.companyName from Order obj" 
				+ " where obj.deleteFlag='1' order by obj.customer.companyName asc";
		List<?> objectList = genericDAO.executeSimpleQuery(customerQuery);
		List<CustomerVO> customerVOList = mapToCustomerVO(objectList);
		return customerVOList;
	}
	
	public static List<CustomerVO> retrievePermitCustomers(GenericDAO genericDAO) {
		String customerQuery = "select distinct obj.customer.id, obj.customer.companyName from Permit obj"
				+ " where obj.deleteFlag='1' order by obj.customer.companyName asc";
		List<?> objectList = genericDAO.executeSimpleQuery(customerQuery);
		List<CustomerVO> customerVOList = mapToCustomerVO(objectList);
		return customerVOList;
	}
	
	public static List<DeliveryAddressVO> retrieveOrderDeliveryAddresses(GenericDAO genericDAO, String customerId) {
		String deliveryAddressQuery = "select distinct obj.deliveryAddress.id, obj.deliveryAddress.line1, obj.deliveryAddress.line2"
				+ " from Order obj where obj.deleteFlag='1'";
		if (!StringUtils.isEmpty(customerId)) {
			deliveryAddressQuery += (" and obj.customer.id=" + customerId);
		}
		deliveryAddressQuery	+= " order by obj.deliveryAddress.line1 asc";
		
		List<?> objectList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		List<DeliveryAddressVO> deliveryAddressVOList = mapToDeliveryAddressVO(objectList);
		return deliveryAddressVOList;
	}
	
	public static List<DeliveryAddressVO> retrieveOrderDeliveryAddresses(GenericDAO genericDAO) {
		return retrieveOrderDeliveryAddresses(genericDAO, StringUtils.EMPTY);
	}
	
	public static List<CustomerVO> mapToCustomerVO(List<?> objectList) {
		List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
		if (objectList == null || objectList.isEmpty()) {
			return customerVOList;
		}
		
		for (int i = 0; i < objectList.size(); i++) {
			Object anObject[] = (Object[])objectList.get(i);
			CustomerVO aCustomerVO = new CustomerVO();
			aCustomerVO.setId((Long)anObject[0]);
			if (anObject[1] != null && StringUtils.isNotEmpty(anObject[1].toString())) {
				aCustomerVO.setCompanyName(anObject[1].toString());
			}
			customerVOList.add(aCustomerVO);
		}
		return customerVOList;
	}
	
	public static String retrieveDumpsterSize(GenericDAO genericDAO, String id) {
		if (StringUtils.isEmpty(id)) {
			return StringUtils.EMPTY;
		}
		
		DumpsterSize dumpsterSize = genericDAO.getById(DumpsterSize.class, new Long(id));
		if (dumpsterSize == null) {
			return StringUtils.EMPTY;
		}
		
		return dumpsterSize.getSize();
	}
	
	public static String retrieveDumpsterStatus(GenericDAO genericDAO, String id) {
		if (StringUtils.isEmpty(id)) {
			return StringUtils.EMPTY;
		}
		
		DumpsterStatus dumpsterStatus = genericDAO.getById(DumpsterStatus.class, new Long(id));
		if (dumpsterStatus == null) {
			return StringUtils.EMPTY;
		}
		
		return dumpsterStatus.getStatus();
	}
	
	public static String[][] extractContactDetails(List<CustomerVO> customerVOList) {
		if (customerVOList == null || customerVOList.isEmpty()) {
			return null;
		}
		
		SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (CustomerVO aCustomerVO : customerVOList) {
			phoneSet.add(aCustomerVO.getPhoneNumber());
			contactNameSet.add(aCustomerVO.getContactName());
		}
		
		String[] phoneArr = phoneSet.toArray(new String[0]);
		String[] contactNameArr = contactNameSet.toArray(new String[0]);
		
		String[][] strArrOfArr = new String[2][];
		strArrOfArr[0] = phoneArr;
		strArrOfArr[1] = contactNameArr;
		return strArrOfArr;
	}
	
	public static String[][] retrieveOrderDeliveryContactDetails(GenericDAO genericDAO) {
		List<?> objectList = genericDAO.executeSimpleQuery("select obj.deliveryContactPhone1, obj.deliveryContactName from Order obj"
				+ " where obj.deleteFlag='1' order by obj.id asc");
		String[][] strArrOfArr = ModelUtil.extractContactDetailsGeneric((List<Object[]>)objectList);
		return strArrOfArr;
	}
	
	public static List<DeliveryAddressVO>  retrievePermitDeliveryAddresses(GenericDAO genericDAO) {
		String deliveryAddressQuery = "select distinct obj.deliveryAddress.id, obj.deliveryAddress.line1, obj.deliveryAddress.line2"
				+ " from Permit obj where obj.deleteFlag='1' order by obj.deliveryAddress.line1 asc";
		List<?> objectList = genericDAO.executeSimpleQuery(deliveryAddressQuery);
		List<DeliveryAddressVO> deliveryAddressVOList = ModelUtil.mapToDeliveryAddressVO(objectList);
		return deliveryAddressVOList;
	}
	
	public static List<PermitAddressVO> retrievePermitAddresses(GenericDAO genericDAO) {
		String permitAddressQuery = "select obj.line1, obj.line2 from PermitAddress obj where obj.deleteFlag='1' order by obj.line1 asc";
		List<?> objectList = genericDAO.executeSimpleQuery(permitAddressQuery);
		List<PermitAddressVO> permitAddressVOList = ModelUtil.mapToPermitAddressVO(objectList);
		return permitAddressVOList;
	}
	
	public static String[][] extractContactDetailsGeneric(List<Object[]> objectList) {
		if (objectList == null || objectList.isEmpty()) {
			return null;
		}
		
		SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (Object[] objArr : objectList) {
			phoneSet.add(objArr[0].toString());
			contactNameSet.add(objArr[1].toString());
		}
		
		String[] phoneArr = phoneSet.toArray(new String[0]);
		String[] contactNameArr = contactNameSet.toArray(new String[0]);
		
		String[][] strArrOfArr = new String[2][];
		strArrOfArr[0] = phoneArr;
		strArrOfArr[1] = contactNameArr;
		return strArrOfArr;
	}
	
	public static void sort(List<CustomerReportVO> customerReportVOList, String... sortBy) {
		if (customerReportVOList == null || customerReportVOList.isEmpty()) {
			return;
		}
		
		ComparatorChain chain = new ComparatorChain(); 
		
		String sortBy1 = "companyName";
		if (sortBy.length > 0) {
			sortBy1 = sortBy[0];
		}
		if (StringUtils.equals("companyName", sortBy1)) {
			chain.addComparator(companyNameComparator);
		}
		
		Collections.sort(customerReportVOList, chain);
	}
	
	public static Comparator<CustomerReportVO> companyNameComparator = new Comparator<CustomerReportVO>() {
		@Override
		public int compare(CustomerReportVO o1, CustomerReportVO o2) {
			if (StringUtils.isEmpty(o1.getCompanyName()) || StringUtils.isEmpty(o2.getCompanyName())) {
				return 0;
			}
			return o1.getCompanyName().compareTo(o2.getCompanyName());
		}
	};
	
	public static void map(CustomerReportVO aCustomerReportVO, Customer aCustomer) {
		aCustomerReportVO.setId(aCustomer.getId());
		aCustomerReportVO.setCompanyName(aCustomer.getCompanyName());
		aCustomerReportVO.setStatus(aCustomer.getCustomerStatus().getStatus());
		aCustomerReportVO.setContactName(aCustomer.getContactName());
		aCustomerReportVO.setPhoneNumber(aCustomer.getPhone());
	}
	
	public static void map(CustomerVO aCustomerVO, Customer aCustomer) {
		aCustomerVO.setId(aCustomer.getId());
		aCustomerVO.setCompanyName(aCustomer.getCompanyName());
		aCustomerVO.setStatus(aCustomer.getCustomerStatus().getStatus());
		aCustomerVO.setContactName(aCustomer.getContactName());
		aCustomerVO.setPhoneNumber(aCustomer.getPhone());
	}
	
	public static void map(OrderReportVO anOrderReportVO, Order anOrder) {
		anOrderReportVO.setId(anOrder.getId());
		anOrderReportVO.setOrderDate(anOrder.getFormattedCreatedAt());
		anOrderReportVO.setStatus(anOrder.getOrderStatus().getStatus());
		
		anOrderReportVO.setDeliveryContactName(anOrder.getDeliveryContactName());
		anOrderReportVO.setDeliveryContactPhone1(anOrder.getDeliveryContactPhone1());
		anOrderReportVO.setDeliveryAddressFullLine(anOrder.getDeliveryAddress().getFullLine());
		anOrderReportVO.setDeliveryCity(anOrder.getDeliveryAddress().getCity());
		
		anOrderReportVO.setDeliveryDate(anOrder.getFormattedDeliveryDate());
		anOrderReportVO.setPickupDate(anOrder.getFormattedPickupDate());
		
		OrderFees anOrderFees = anOrder.getOrderFees();
		anOrderReportVO.setDumpsterPrice(anOrderFees.getDumpsterPrice());
		
		anOrderReportVO.setCityFee(anOrderFees.getCityFee() == null ? (new BigDecimal("0.00")) : anOrderFees.getCityFee());
		
		anOrderReportVO.setPermitFees(anOrderFees.getTotalPermitFees());
		anOrderReportVO.setOverweightFee(anOrderFees.getOverweightFee());
		anOrderReportVO.setAdditionalFees(anOrderFees.getTotalAdditionalFees());
		anOrderReportVO.setTotalFees(anOrderFees.getTotalFees());
		
		anOrderReportVO.setTotalAmountPaid(anOrder.getTotalAmountPaid());
		anOrderReportVO.setBalanceAmountDue(anOrder.getBalanceAmountDue());
	}
}
