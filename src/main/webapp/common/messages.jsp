<%@ include file="/common/taglibs.jsp"%>
<%-- Error Messages --%>
<c:if test="${param.errorCtx == errorCtx}">
	<c:if test="${not empty error}">
	    <div class="error">
	    	<div style="margin-top: 5px"></div>
	        <c:forEach var="error" items="${error}">
				<img src="${iconWarningImage}" alt="Warning" class="icon" />
				<c:out value="${error}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="error"/>
	</c:if>
</c:if>

<%-- Success Messages --%>
<c:if test="${param.msgCtx == msgCtx}">
	<c:if test="${not empty msg}">
	    <div class="message">
	    	<div style="margin-top: 5px"></div>
	        <c:forEach var="message" items="${msg}">
	            <img src="${iconInformationImage}" alt="Information" class="icon" />
	            <c:out value="${message}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="msg"/>
	</c:if>
</c:if>


