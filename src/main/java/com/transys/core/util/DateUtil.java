package com.transys.core.util;

import java.text.ParseException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	public static Date parseInputDate(String inputDateStr) throws ParseException {
		return FormatUtil.inputDateFormat.parse(inputDateStr);
	}
	
	public static Date parseDbDateTimeFormat2(String dbDateTimeStr) throws ParseException {
		return FormatUtil.dbDateTimeFormat2.parse(dbDateTimeStr);
	}
	
	public static String formatToInputDate(Date date) {
		return FormatUtil.inputDateFormat.format(date);
	}
	
	public static String formatToDbDateTimeFormat(Date date) {
		return FormatUtil.dbDateTimeFormat.format(date);
	}
	
	public static String formatToDbDateTimeFormat2(Date date) {
		return FormatUtil.dbDateTimeFormat2.format(date);
	}
	
	public static String formatToDbDate(Date date) {
		return FormatUtil.dbDateFormat.format(date);
	}
	
	public static String formatTodayToDbDate() {
		return formatToDbDate(new Date());
	}
	
	public static String formatInputDateToDbDateTimeFormat2(String inputDateStr) throws ParseException {
		if (StringUtils.isEmpty(inputDateStr)) {
			return StringUtils.EMPTY;
		}
		
		Date inputDate = parseInputDate(inputDateStr);
		return FormatUtil.dbDateTimeFormat2.format(inputDate);
	}
	
	public static String formatDbDateTimeFormat2ToInputDate(String dbDateTimeStr) throws ParseException {
		if (StringUtils.isEmpty(dbDateTimeStr)) {
			return StringUtils.EMPTY;
		}
		
		Date dbDate = parseDbDateTimeFormat2(dbDateTimeStr);
		return FormatUtil.inputDateFormat.format(dbDate);
	}
	
	public static Date addDays(Date date, int days) {
		return DateUtils.addDays(date, days);
	}
	
	public static String addDaysToTodayAndFormatToDbDate(int days) {
		Date today = new Date();
		return addDaysAndFormatToDbDate(today, days);
	}
	
	public static String addDaysAndFormatToDbDate(Date date, int days) {
		Date modifiedDate = DateUtils.addDays(date, days);
		return formatToDbDate(modifiedDate);
	}
	
	public static String addDaysToInputDateAndFormatToDbDateTime2(String inputDateStr, int days) throws ParseException {
		Date inputDate = parseInputDate(inputDateStr);
		Date modifiedDate = DateUtils.addDays(inputDate, days);
		return formatToDbDateTimeFormat2(modifiedDate);
	}
	
	public static String convertUTC(String utcTime, String zoneId) {
		if (StringUtils.isEmpty(utcTime)) {
			return StringUtils.EMPTY;
		}
		
		if (!utcTime.endsWith("Z")) {
			utcTime += "Z";
		}
		
		String convertedDateTime = StringUtils.EMPTY;
		try {
			Instant utcTimestamp = Instant.parse(utcTime);
			ZonedDateTime zoneTimestamp = utcTimestamp.atZone(ZoneId.of(zoneId));
			convertedDateTime = zoneTimestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ");
		} catch (Throwable t) {
			 
		}
		return convertedDateTime;
	}
	
	public static String convertUTCToCT(String utcTime) {
		if (StringUtils.isEmpty(utcTime)) {
			return StringUtils.EMPTY;
		}
		
		return convertUTC(utcTime, "America/Chicago");
	}
}
