<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>

<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.util.*" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource" %>
<%@ page import="net.sf.jasperreports.j2ee.servlets.ImageServlet" %>

<%@ page import="com.transys.model.vo.invoice.InvoiceVO" %>

<jsp:include page="/common/messages.jsp">
	<jsp:param name="msgCtx" value="invoicePreview" />
	<jsp:param name="errorCtx" value="invoicePreview" />
</jsp:include>
	
<%
	//request.getAttribute("jasperPrint");
	JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
	
	JRHtmlExporter exporter = new JRHtmlExporter();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
	
	InvoiceVO input = (InvoiceVO) request.getSession().getAttribute("input");
	int historyCount = input.getHistoryCount();
	
	StringBuffer buffer = new StringBuffer("</td></tr></table><table width=\"100%\"><tr><td width=\"50%\">&nbsp;</td></tr>\n");
	buffer.append("&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Back\" onclick=\"javascript:history.go("
			+ historyCount
			+ ");\" class=\"flat btn btn-primary btn-sm btn-sm-ext\">");
	buffer.append("&nbsp;&nbsp;<input type=\"button\" value=\"Save\" onclick=\"javascript:location.href='"+request.getContextPath()+"/invoice/saveInvoice.do'\" class=\"flat btn btn-primary btn-sm btn-sm-ext\">");
	buffer.append("&nbsp;&nbsp;<input type=\"button\" value=\"Export As PDF\" onclick=\"javascript:location.href='"+request.getContextPath()+"/invoice/previewInvoiceExport.do?type=pdf'\" class=\"flat btn btn-primary btn-sm btn-sm-ext\">");
	buffer.append("</table>\n");
	
	exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, buffer.toString());
	exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, buffer.toString());
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, (Map)request.getAttribute("IMAGES_MAP"));
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/image?image=");
	//exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
	
	try {
		exporter.exportReport();
	} catch(Exception e){
		e.printStackTrace();
	}
	
	//request.removeAttribute("jasperPrint");
	//request.getSession().removeAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
%>
