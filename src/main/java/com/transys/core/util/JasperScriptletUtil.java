package com.transys.core.util;

import java.math.BigDecimal;

import java.util.Date;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class JasperScriptletUtil extends JRDefaultScriptlet {
	public static String format(Date date) {
		return FormatUtil.formatDate(date);
	}
	
	public static String format(BigDecimal bigDecimal) {
		return FormatUtil.format(bigDecimal);
	}
	
	public static String formatFee(BigDecimal fee) {
		return FormatUtil.formatFee(fee);
	}
	
	public static String formatFee(BigDecimal fee, boolean preffixCurrency) {
		return FormatUtil.formatFee(fee, preffixCurrency);
	}
	
	public static String formatFee(BigDecimal fee, boolean preffixCurrency, boolean addSep) {
		return FormatUtil.formatFee(fee, preffixCurrency, addSep);
	}
	
	public static String formatFee(String feeStr, boolean preffixCurrency, boolean addSep) {
		return FormatUtil.formatFee(feeStr, preffixCurrency, addSep);
	}
}
