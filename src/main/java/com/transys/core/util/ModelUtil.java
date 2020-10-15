package com.transys.core.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.transys.core.dao.GenericDAO;
import com.transys.model.Customer;
import com.transys.model.DumpsterSize;
import com.transys.model.DumpsterStatus;
import com.transys.model.Order;
import com.transys.model.OrderNotes;
import com.transys.model.OrderStatus;
import com.transys.model.User;
import com.transys.model.vo.CustomerVO;
import com.transys.model.vo.DeliveryAddressVO;

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
	
	public static List<CustomerVO> retrieveOrderCustomers(GenericDAO genericDAO) {
		String customerQuery = "select distinct obj.customer.id, obj.customer.companyName from Order obj" 
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
	
	public static Object[] retrieveContactDetails(List<Customer> customerList) {
		if (customerList == null || customerList.isEmpty()) {
			return null;
		}
		
		SortedSet<String> phoneSet = new TreeSet<String>();
		SortedSet<String> contactNameSet = new TreeSet<String>();
		for (Customer aCustomer : customerList) {
			phoneSet.add(aCustomer.getPhone());
			contactNameSet.add(aCustomer.getContactName());
		}
		
		String[] phoneArr = phoneSet.toArray(new String[0]);
		String[] contactNameArr = contactNameSet.toArray(new String[0]);
		
		Object[] objArr = new Object[2];
		objArr[0] = phoneArr;
		objArr[1] = contactNameArr;
		return objArr;
	}
}
