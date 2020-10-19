<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	return true;
}
</script>
<br />
<h5 style="margin-top: -15px; !important">Customer List Report</h5>
<form:form action="${urlCtx}/search.do" method="get" name="customerListReportSearchForm" id="customerListReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Company Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.id" name="id" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.contactName" name="contactName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${contactNames}" var="aContactName">
						<c:if test="${not empty aContactName}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['contactName'] == aContactName}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aContactName}" ${selected}>${aContactName}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>	
		</tr>
		<tr>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.phone" name="phone" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${phones}" var="aPhone">
						<c:if test="${not empty aPhone}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['phone'] == aPhone}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPhone}" ${selected}>${aPhone}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Status</td>
			<td >
				<select class="flat form-control input-sm" id="customerListReport.customerStatus" name="customerStatus" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customerStatuses}" var="aCustomerStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['customerStatus'] == aCustomerStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomerStatus.id}" ${selected}>${aCustomerStatus.status}</option>
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
		<tr><td></td></tr>
	</table>
</form:form>
<jsp:include page="${reportsMenuCtx}/reportToolbar.jsp">
	<jsp:param name="reportSearchForm" value="customerListReportSearchForm" />
	<jsp:param name="reportDataElem" value="customerListReportData" />
</jsp:include>
