package com.transys.core.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static String getRealPath(HttpServletRequest request, String fileName) {
		return request.getSession().getServletContext().getRealPath(fileName);
	}
	
	public static String getLogoFilePath(HttpServletRequest request) {
		return getRealPath(request, "/images/" + "rds_logo_2.png");
	}
}
