<%@ include file="/common/taglibs.jsp"%>

<%@page import="com.transys.core.util.ReportUtil"%>

<jsp:include page="/common/messages.jsp">
	<jsp:param name="msgCtx" value="${msgCtx}" />
	<jsp:param name="errorCtx" value="${errorCtx}" />
</jsp:include>

<%
	if (ReportUtil.hasJasperPrint(request)) {
%>		
		<jsp:include page="../inlineHtmlReport.jsp">
			<jsp:param name="pagingLink" value="searchModelWithPaging.do" />
		</jsp:include>
<%	} else {%>
		<%="Error occured while processing request" %>
<%	}
%>

