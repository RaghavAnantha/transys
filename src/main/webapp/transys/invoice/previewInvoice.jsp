<%@ include file="/common/taglibs.jsp"%>

<jsp:include page="../base/htmlReport.jsp">
	<jsp:param name="saveLabel" value="Save Invoice" />
	<jsp:param name="exportLabel" value="Export Invoice Preview As PDF" />
	<jsp:param name="saveAction" value="saveInvoice.do" />
	<jsp:param name="exportAction" value="previewInvoiceExport.do?type=pdf" />
</jsp:include>

