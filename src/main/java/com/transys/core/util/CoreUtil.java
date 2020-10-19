package com.transys.core.util;

import java.math.BigDecimal;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CoreUtil {
	public static String toString(String[] stringArr) {
		if (stringArr == null || stringArr.length <= 0) {
			return StringUtils.EMPTY;
		}
		
		String str = ArrayUtils.toString(stringArr);
		str = StringUtils.replacePattern(str, "\\{|\\}", StringUtils.EMPTY);
		return str;
	}
	
	public static String toString(List<String> strList) {
		if (strList == null || strList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String[] stringArr = new String[strList.size()];
		return toString(strList.toArray(stringArr));
	}
	
	public static String concatenate(String str1, String str2, String sep) {
		StringBuffer concStrBuff = new StringBuffer();
		if (StringUtils.isNotEmpty(str1)) {
			concStrBuff.append(str1);
		}
		if (StringUtils.isNotEmpty(str2)) {
			concStrBuff.append(sep)
						  .append(str2);
		}
		
		return concStrBuff.toString();
	}
	
	public static String concatenate(String str1, String str2) {
		return concatenate(str1, str2, " ");
	}
	
	public static String defaultIfEmpty(String str, String defaultStr) {
		return StringUtils.defaultIfEmpty(str, defaultStr);
	}
	
	public static String defaultIfEmpty(String str) {
		return StringUtils.defaultIfEmpty(str, StringUtils.EMPTY);
	}
	
	public static BigDecimal defaultIfNull(BigDecimal bigDecimal, BigDecimal defaultValue) {
		return bigDecimal == null ? defaultValue : bigDecimal;
	}
	
	public static BigDecimal defaultIfNull(BigDecimal bigDecimal) {
		return bigDecimal == null ? new BigDecimal(0.00) : bigDecimal;
	}
}
