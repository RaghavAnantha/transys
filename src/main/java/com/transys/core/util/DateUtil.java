package com.transys.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	public static SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
	public static SimpleDateFormat dbDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date parseInputDate(String inputDateStr) throws ParseException {
		return inputDateFormat.parse(inputDateStr);
	}
	
	public static String formatInputDate(Date date) {
		return inputDateFormat.format(date);
	}
	
	public static String formatDbDate(Date date) {
		return dbDateFormat.format(date);
	}
	
	public static String formatDbDate2(Date date) {
		return dbDateFormat2.format(date);
	}
	
	public static String formatTodayDbDate2() {
		return formatDbDate2(new Date());
	}
	
	public static String formatDbDate(String inputDateStr) throws ParseException {
		Date inputDate = parseInputDate(inputDateStr);
		return dbDateFormat.format(inputDate);
	}
	
	public static Date addDays(Date date, int days) {
		return DateUtils.addDays(date, days);
	}
	
	public static String addDaysAndFormatDbDate(String inputDateStr, int days) throws ParseException {
		Date inputDate = parseInputDate(inputDateStr);
		Date modifiedDate = DateUtils.addDays(inputDate, days);
		return formatDbDate(modifiedDate);
	}
}
