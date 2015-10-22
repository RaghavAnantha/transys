<%@ include file="/common/taglibs.jsp"%>
<%-- Error Messages --%>
<c:if test="${param.errorCtx == errorCtx}">
	<c:if test="${not empty error}">
	    <div class="error">
	        <c:forEach var="error" items="${error}">
				<img src="${ctx}/images/iconWarning.gif" alt="Warning" class="icon" />
				<c:out value="${error}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="error"/>
	</c:if>
</c:if>

<%-- Success Messages --%>
<c:if test="${param.msgCtx == msgCtx}">
	<c:if test="${not empty msgCtx}">
	    <div class="message">
	        <c:forEach var="message" items="${msg}">
	            <img src="${ctx}/images/iconInformation.gif" alt="Information" class="icon" />
	            <c:out value="${message}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="msg"/>
	</c:if>
</c:if>


