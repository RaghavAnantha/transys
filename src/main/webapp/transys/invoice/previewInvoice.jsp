<%@ include file="/common/taglibs.jsp"%>

<%@page import="com.transys.core.util.ReportUtil"%>

<jsp:include page="/common/messages.jsp">
	<jsp:param name="msgCtx" value="invoicePreview" />
	<jsp:param name="errorCtx" value="invoicePreview" />
</jsp:include>

<%
	if (ReportUtil.hasJasperPrint(request)) {
%>		
		<jsp:include page="../base/htmlReport.jsp">
			<jsp:param name="saveLabel" value="Save Invoice" />
			<jsp:param name="exportLabel" value="Export Invoice Preview As PDF" />
			<jsp:param name="saveAction" value="saveInvoice.do" />
			<jsp:param name="exportAction" value="previewInvoiceExport.do?type=pdf" />
		</jsp:include>
<%	} else {%>
		<%="Error occured while processing request" %>
<%	}
%>

