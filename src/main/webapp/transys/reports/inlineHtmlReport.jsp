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
	
	String buttonClass = "class=\"flat btn btn-primary btn-sm btn-sm-ext\"";
	StringBuffer headerFooterBuffer = new StringBuffer("</td></tr></table><table width=\"100%\">");
	headerFooterBuffer.append("<tr><td/></tr></table>");
	
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
	//reportConfig.setBorderCollapse("separate");
	reportConfig.setZoomRatio(new Float(0.97));
	reportConfig.setIgnorePageMargins(true);
	htmlExporter.setConfiguration(reportConfig);
	
	try {
		htmlExporter.exportReport();
	} catch(Exception e){
		e.printStackTrace();
	}
	
	//request.getSession().removeAttribute(net.sf.jasperreports.j2ee.servlets.ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
%>

<c:if test="${param.pagingLink != null and param.pagingLink != ''}">
	<tg:pagingJS searchCriteria="${sessionScope.searchCriteria}" pagingLink="${param.pagingLink}"></tg:pagingJS>
</c:if>

<script language="javascript">
	removeJasperPrint();
</script>
