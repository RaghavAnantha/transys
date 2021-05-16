package com.transys.core.util;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.transys.model.State;

public class FormatUtil {
	public static SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static SimpleDateFormat auditDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dbDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dbDateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00.0");
	public static SimpleDateFormat expiryDateFormat = new SimpleDateFormat("MM/yyyy");
	
	public static DecimalFormat decimalFormat = new DecimalFormat("#####0.00");
	public static DecimalFormat currencyFormat = new DecimalFormat("########0.00");
	public static DecimalFormat currencyFormatWithSep = new DecimalFormat("###,###,##0.00");
	
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
	
	public static String formatExpiryDate(Date date) {
		return formatDate(date, expiryDateFormat);
	}
	
	public static String formatAuditDate(Date date) {
		return formatDate(date, auditDateFormat);
	}
	
	public static String formatDateTime(Date date) {
		return formatAuditDate(date);
	}
	
	public static String formatInputDateToDbDate(String inputDateStr) {
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
	
	public static String formatDateTimeRange(Date date, String timeFrom, String timeTo) {
		String dateStr = formatDate(date);
		if (StringUtils.isEmpty(dateStr)) {
			return StringUtils.EMPTY;
		}
		
		if (StringUtils.isNotEmpty(timeFrom)) {
			dateStr += "  ";
			dateStr += timeFrom;
		}
		
		if (StringUtils.isNotEmpty(timeTo) 
				&& !StringUtils.equals(timeFrom, timeTo)) {
			dateStr += " - ";
			dateStr += timeTo;
		}
		
		return dateStr;
	}
	
	public static String formatAddress(String line1, String line2) {
		return CoreUtil.concatenate(line1, line2);
	}
	
	public static String formatAddress(String line1, String line2, String sep) {
		return CoreUtil.concatenate(line1, line2, sep);
	}
	
	public static String formatAddress(String line1, String line2, String city, State state, String zip) {
		return FormatUtil.formatAddress(line1, line2, city, state, zip, ", ");
	}
	
	public static String formatAddress(String line1, String line2, String city, String state, String zip,
			String lineSeparator) {
		StringBuffer addressBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(line1)) {
			addressBuff.append(line1);
		}
		if (StringUtils.isNotEmpty(line2)) {
			addressBuff.append(" " + line2);
		}
		if (StringUtils.isNotEmpty(city)) {
			addressBuff.append(lineSeparator + city);
		}
		if (StringUtils.isNotEmpty(state)) {
				addressBuff.append(" " + state);
		}
		if (StringUtils.isNotEmpty(zip)) {
			addressBuff.append(" " + zip);
		}
		
		return addressBuff.toString();
	}
	
	public static String formatAddress(String line1, String line2, String city, State state, String zip,
			String lineSeparator) {
		String stateStr = state == null ? StringUtils.EMPTY : state.getName();
		return formatAddress(line1, line2, city, stateStr, zip, lineSeparator);
	}
	
	public static String formatContactDetails(String name, String phone) {
		return formatContactDetails(name, phone, " ");
	}
	
	public static String formatContactDetails(String name, String phone, String sep) {
		String contactDetails = StringUtils.isEmpty(name) ? StringUtils.EMPTY : name;
		if (StringUtils.isNotEmpty(phone)) {
			contactDetails += (sep + formatPhone(phone));
		}
		return contactDetails;
	}
	
	public static String formatDateTime(Date date, String time) {
		String formattedDateTime = StringUtils.EMPTY;
		if (date == null) {
			return formattedDateTime;
		}
		
		formattedDateTime = inputDateFormat.format(date);
		if (StringUtils.isNotEmpty(time)) {
			formattedDateTime += " " + time;
		}
		return formattedDateTime;
	}
	
	public static String formatDateTimeRange(Date date, String hoursFrom, String minutesFrom, 
			String hoursTo, String minutesTo) {
		String formattedDateTime = StringUtils.EMPTY;
		if (date == null) {
			return formattedDateTime;
		}
		
		formattedDateTime = inputDateFormat.format(date);
		if (StringUtils.isNotEmpty(hoursFrom)) {
			formattedDateTime += " " + hoursFrom;
		}
		if (StringUtils.isNotEmpty(minutesFrom)) {
			formattedDateTime += ":" + minutesFrom;
		}
		if (StringUtils.isNotEmpty(hoursTo)) {
			formattedDateTime += " - " + hoursTo;
		}
		if (StringUtils.isNotEmpty(minutesTo)) {
			formattedDateTime += ":" + minutesTo;
		}
		return formattedDateTime;
	}
	
	public static String format(BigDecimal bigDecimal) {
		String formattedStr = StringUtils.EMPTY;
		if (bigDecimal == null) {
			return formattedStr;
		}
		
		return decimalFormat.format(bigDecimal);
	}
	
	public static String formatFee(BigDecimal fee) {
		String formattedFee = StringUtils.EMPTY;
		if (fee == null) {
			return formattedFee;
		}
		
		return currencyFormat.format(fee);
	}
	
	public static String formatFee(BigDecimal fee, boolean preffixCurrency) {
		String formattedFee = StringUtils.EMPTY;
		if (fee == null) {
			return formattedFee;
		}
		
		if (preffixCurrency) {
			formattedFee = "$";
		}
		
		return (formattedFee + formatFee(fee));
	}
	
	public static String formatFeeWithSep(BigDecimal fee, boolean addSep) {
		String formattedFee = StringUtils.EMPTY;
		if (fee == null) {
			return formattedFee;
		}
		
		return currencyFormatWithSep.format(fee);
	}
	
	public static String formatFee(BigDecimal fee, boolean preffixCurrency, boolean addSep) {
		String formattedFee = StringUtils.EMPTY;
		if (fee == null) {
			return formattedFee;
		}
		
		if (!addSep) {
			return formatFee(fee, preffixCurrency);
		}
		
		if (preffixCurrency) {
			formattedFee = "$";
		}
		return (formattedFee + currencyFormatWithSep.format(fee));
	}
	
	public static String formatFee(String feeStr, boolean preffixCurrency, boolean addSep) {
		String formattedFee = StringUtils.EMPTY;
		if (StringUtils.isEmpty(feeStr)) {
			return formattedFee;
		}
		
		BigDecimal fee = new BigDecimal(feeStr);
		return formatFee(fee, preffixCurrency, addSep);
	}
}
