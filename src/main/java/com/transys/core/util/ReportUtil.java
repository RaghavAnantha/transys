package com.transys.core.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.transys.core.report.WaterMarkRenderer;

import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class ReportUtil {
	public static String JASPER_PRINT_SESSION_ATTRIBUTE_KEY = ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE;

	public static void addJasperPrint(HttpServletRequest request, JasperPrint jp, String className) {
		request.getSession().setAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY, jp);
		System.out.println(className + "->Added Jasper Print");
	}
	
	public static String removeJasperPrint(HttpServletRequest request, String className) {
		request.getSession().removeAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		System.out.println(className + "->Removed Jasper Print");
		return "Success";
	}
	
	public static WaterMarkRenderer instantiateWaterMarkRenderer(boolean display, String displayText) {
		WaterMarkRenderer waterMarkRenderer = new WaterMarkRenderer(display);
		waterMarkRenderer.setPageOrientaion(WaterMarkRenderer.PAGE_ORIENTATION_LANDSCAPE);
		waterMarkRenderer.setDisplayText(displayText);
		waterMarkRenderer.setDebug(false);
		return waterMarkRenderer;
	}
	
	public static void addWaterMarkRendererReportParam(Map<String, Object> params, boolean display, String displayText) {
		WaterMarkRenderer waterMarkRenderer = instantiateWaterMarkRenderer(display, displayText);
		params.put("watermark", waterMarkRenderer); 
	}
	
	public static JasperPrint getJasperPrint(HttpServletRequest request) {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		return jasperPrint;
	}
}
