package com.transys.core.util;

import javax.servlet.http.HttpServletRequest;

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
	
	public static String getLogoFilePath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/images/" + "rds_logo.png");
	}
}
