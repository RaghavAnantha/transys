<%@ include file="/common/taglibs.jsp"%>

<%@page import="net.sf.jasperreports.engine.JasperPrint" %>
<%@page import="net.sf.jasperreports.engine.export.HtmlExporter" %>

<%@page import="net.sf.jasperreports.export.SimpleHtmlReportConfiguration"%>
<%@page import="net.sf.jasperreports.export.SimpleHtmlExporterConfiguration"%>
<%@page import="net.sf.jasperreports.export.SimpleHtmlExporterOutput"%>
<%@page import="net.sf.jasperreports.export.SimpleExporterInput"%>

<%@page import="net.sf.jasperreports.web.util.WebHtmlResourceHandler"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="com.transys.core.util.ReportUtil"%>

<%@page import="com.transys.model.vo.BaseVO"%>

<%
	JasperPrint jasperPrint = ReportUtil.getJasperPrint(request);
	HtmlExporter htmlExporter = new HtmlExporter();
	htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	
	SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(out);
	output.setImageHandler(new WebHtmlResourceHandler(request.getContextPath() + "/image?image={0}"));
	htmlExporter.setExporterOutput(output);
	
	BaseVO input = (BaseVO) request.getSession().getAttribute(ReportUtil.inputKey);
	int historyCount = input.getHistoryCount();
	
	String saveLabel = request.getParameter("saveLabel");
	String exportLabel = request.getParameter("exportLabel");
	String saveAction = request.getParameter("saveAction");
	String exportAction = request.getParameter("exportAction");
	
	String buttonClass = "class=\"flat btn btn-primary btn-sm btn-sm-ext\"";
	StringBuffer headerFooterBuffer = new StringBuffer("</td></tr></table><table width=\"100%\">");
	//headerFooterBuffer.append("<input type=\"button\" value=\"Back\" onclick=\"javascript:location.href='create.do'\">");
	headerFooterBuffer.append("&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Back\""
				+ " onclick=\"javascript:history.go("+historyCount+");\""
				+ " " + buttonClass + ">");
	headerFooterBuffer.append("&nbsp;&nbsp;<input type=\"button\" value=\""+saveLabel+"\""
				+ " onclick=\"javascript:location.href='" + saveAction + "'\""
				+ " " + buttonClass + ">");
	headerFooterBuffer.append("&nbsp;&nbsp;<input type=\"button\" value=\""+exportLabel+"\""
				+ " onclick=\"javascript:location.href='" + exportAction + "'\""
				+ " " + buttonClass + ">");
	headerFooterBuffer.append("</table>");
	
	SimpleHtmlExporterConfiguration htmlExporterConfig = new SimpleHtmlExporterConfiguration();
	htmlExporterConfig.setBetweenPagesHtml(StringUtils.EMPTY);
	//exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);*/
	
	//String header = "<html><head><style> @page {size: landscape; margin-left: 0.00in;margin-right: 0.00in;margin-top: 0.00in;margin-bottom: 0.00in;}</style></head><body>";
	String headerFooterStr = headerFooterBuffer.toString();
	htmlExporterConfig.setHtmlHeader(headerFooterStr);
	htmlExporterConfig.setHtmlFooter(headerFooterStr);
	String betweenPagesHtml = "<div style=\"display:block;page-break-before:always;\"></div>";
	htmlExporterConfig.setBetweenPagesHtml(betweenPagesHtml);
	htmlExporter.setConfiguration(htmlExporterConfig);
	
	SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
	reportConfig.setRemoveEmptySpaceBetweenRows(true);
	reportConfig.setBorderCollapse("separate");
	reportConfig.setZoomRatio(new Float(0.97));
	reportConfig.setIgnorePageMargins(true);
	htmlExporter.setConfiguration(reportConfig);
	
	try {
		htmlExporter.exportReport();
	} catch(Exception e){
		e.printStackTrace();
	}
%>

<script language="javascript">
$(window).on("load", function() {
	verifyAndRemoveJasperPrint('${ctx}');
});
</script>
