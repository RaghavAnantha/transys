package com.transys.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class FormatUtil {
	public static SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat auditDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	public static String formatPhone(String phone) {
		if (StringUtils.isEmpty(phone) || phone.length() < 10 || StringUtils.contains(phone, "-")) {
			return phone;
		}
		
		return String.format("%s-%s-%s", phone.substring(0, 3), 
												   phone.substring(3, 6), 
												   phone.substring(6, 10));
	}
	
	public static String formatDate(Date date, SimpleDateFormat useDateFormat) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		
		return useDateFormat.format(date);
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, inputDateFormat);
	}
	
	public static String formatAuditDate(Date date) {
		return formatDate(date, auditDateFormat);
	}
	
	public static String convertInputDateToDbDate(String inputDateStr) {
		Date inputDate = parseInputDate(inputDateStr);
		if (inputDate == null) {
			return StringUtils.EMPTY;
		}
		
		return formatDbDate(inputDate);
	}
	
	public static Date parseDate(String dateStr, SimpleDateFormat useDateFormat) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		
		try {
			return useDateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date parseInputDate(String inputDateStr) {
		return parseDate(inputDateStr, inputDateFormat);
	}
	
	public static String formatDbDate(Date date) {
		return formatDate(date, dbDateFormat);
	}
	
	public static String formatDateRange(String dateFrom, String dateTo) {
		String dateRange = StringUtils.isEmpty(dateFrom) ? StringUtils.EMPTY : dateFrom;
		dateRange += " - ";
		dateRange += StringUtils.isEmpty(dateTo) ? StringUtils.EMPTY : dateTo;
		return dateRange;
	}
	
	public static String formatDateRange(Date dateFrom, Date dateTo) {
		String dateFromStr = (dateFrom ==  null) ? StringUtils.EMPTY : formatDate(dateFrom);
		String dateToStr = (dateTo ==  null) ? StringUtils.EMPTY : formatDate(dateTo);
		return formatDateRange(dateFromStr, dateToStr);
	}
}
