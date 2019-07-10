package com.transys.core.util;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import com.transys.core.dao.GenericDAO;

import com.transys.model.Order;
import com.transys.model.OrderNotes;
import com.transys.model.OrderStatus;
import com.transys.model.User;

public class ModelUtil {
	public static SimpleDateFormat mysqlDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
}
