package com.transys.core.util;

import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class FormatUtil {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	public static String formatPhone(String phone) {
		if (StringUtils.isEmpty(phone) || phone.length() < 10 || StringUtils.contains(phone, "-")) {
			return phone;
		}
		
		return String.format("%s-%s-%s", phone.substring(0, 3), 
												   phone.substring(3, 6), 
												   phone.substring(6, 10));
	}
	
	public static String formatDate(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		
		return dateFormat.format(date);
	}
	
	public static String formatDateRange(String dateFrom, String dateTo) {
		String dateRange = StringUtils.isEmpty(dateFrom) ? StringUtils.EMPTY : dateFrom;
		dateRange += " - ";
		dateRange += StringUtils.isEmpty(dateTo) ? StringUtils.EMPTY : dateTo;
		return dateRange;
	}
	
	public static String formatDateRange(Date dateFrom, Date dateTo) {
		String dateFromStr = (dateFrom ==  null) ? StringUtils.EMPTY : dateFormat.format(dateFrom);
		String dateToStr = (dateTo ==  null) ? StringUtils.EMPTY : dateFormat.format(dateTo);
		return formatDateRange(dateFromStr, dateToStr);
	}
}
