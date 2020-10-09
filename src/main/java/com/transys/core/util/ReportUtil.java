package com.transys.core.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.transys.core.report.WaterMarkRenderer;

import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class ReportUtil {
	public static String JASPER_PRINT_SESSION_ATTRIBUTE_KEY = ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE;
	public static String FREEZE_ROW_NO_KEY = "FREEZE_ROW_NO";
	
	protected static Logger log = LogManager.getLogger("com.transys.core.util.ReportUtil");
	
	public static void addJasperPrint(HttpServletRequest request, JasperPrint jp, String className) {
		request.getSession().setAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY, jp);
		log.info(className + "->Added Jasper Print");
	}
	
	public static String removeJasperPrint(HttpServletRequest request, String className) {
		request.getSession().removeAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		log.info(className + "->Removed Jasper Print");
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
	
	public static boolean hasJasperPrint(HttpServletRequest request) {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		return (jasperPrint != null ? true : false);
	}
}
