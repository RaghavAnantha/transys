package com.transys.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	public static SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
	public static SimpleDateFormat dbDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date parseInputDate(String inputDateStr) throws ParseException {
		return inputDateFormat.parse(inputDateStr);
	}
	
	public static Date parseDbDate(String dbDateStr) throws ParseException {
		return dbDateFormat.parse(dbDateStr);
	}
	
	public static String formatToInputDate(Date date) {
		return inputDateFormat.format(date);
	}
	
	public static String formatToDbDate(Date date) {
		return dbDateFormat.format(date);
	}
	
	public static String formatToDbDate2(Date date) {
		return dbDateFormat2.format(date);
	}
	
	public static String formatTodayToDbDate2() {
		return formatToDbDate2(new Date());
	}
	
	public static String formatInputDateToDbDate(String inputDateStr) throws ParseException {
		if (StringUtils.isEmpty(inputDateStr)) {
			return StringUtils.EMPTY;
		}
		
		Date inputDate = parseInputDate(inputDateStr);
		return dbDateFormat.format(inputDate);
	}
	
	public static String formatDbDateToInputDate(String dbDateStr) throws ParseException {
		if (StringUtils.isEmpty(dbDateStr)) {
			return StringUtils.EMPTY;
		}
		
		Date dbDate = parseDbDate(dbDateStr);
		return inputDateFormat.format(dbDate);
	}
	
	public static Date addDays(Date date, int days) {
		return DateUtils.addDays(date, days);
	}
	
	public static String addDaysAndFormatToDbDate(String inputDateStr, int days) throws ParseException {
		Date inputDate = parseInputDate(inputDateStr);
		Date modifiedDate = DateUtils.addDays(inputDate, days);
		return formatToDbDate(modifiedDate);
	}
	
	public static String getTodayDbDateStr() {
		Date today = new Date();
		String todayStr = DateUtil.formatToDbDate2(today);
		return todayStr;
	}
}
