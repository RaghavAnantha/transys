<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	return true;
}
</script>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters On-site Report</h4>
<form:form action="search.do" method="get" name="dumpstersOnsiteReportSearchForm" id="dumpstersOnsiteReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
	</jsp:include>
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Dumpster Size</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${dumpsterSizes}" var="aDumpsterSize">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aDumpsterSize.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterSize.id}" ${selected}>${aDumpsterSize.size}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Status</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="status" name="status" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${dumpsterStatus}" var="status">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == status.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${status.id}" ${selected}>${status.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="submit" class="btn btn-primary btn-sm btn-sm-ext"
					value="<transys:label code="Preview"/>" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<jsp:include page="../reportToolbar.jsp">
	<jsp:param name="reportSearchForm" value="dumpstersOnsiteReportSearchForm" />
	<jsp:param name="reportDataElem" value="dumpsterOnsiteReportData" />
</jsp:include>
