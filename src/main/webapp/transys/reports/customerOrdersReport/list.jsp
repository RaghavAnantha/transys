<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	return true;
}
</script>
<br />
<h5 style="margin-top: -15px; !important">Customer Orders Report</h5>
<form:form action="${urlCtx}/search.do" method="get" name="customerOrdersReportSearchForm" id="customerOrdersReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Company Name</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="customerOrdersReport.companyName" name="customer.companyName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer.companyName'] == customer.companyName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerOrdersReport.contactName" name="customer.contactName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${contactNames}" var="aContactName">
						<c:if test="${not empty aContactName}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.contactName'] == aContactName}">
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
				<select class="flat form-control input-sm" id="customerOrdersReport.phone" name="customer.phone" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${phones}" var="aPhone">
						<c:if test="${not empty aPhone}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.phone'] == aPhone}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPhone}" ${selected}>${aPhone}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>	
			<td class="form-left">Order Status</td>
			<td >
				<select class="flat form-control input-sm" id="customerOrdersReport.orderStatus" name="orderStatus" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${orderStatuses}" var="anOrderStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['orderStatus'] == anOrderStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderStatus.id}" ${selected}>${anOrderStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Order Date From</td>
			<td>
				<input class="flat" id="datepicker11" name="createdAtFrom" value="${sessionScope.searchCriteria.searchMap['createdAtFrom']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Order Date To</td>
			<td>
				<input class="flat" id="datepicker12" name="createdAtTo" value="${sessionScope.searchCriteria.searchMap['createdAtTo']}" style="width: 175px !important" />
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
<jsp:include page="../reportToolbar.jsp">
	<jsp:param name="reportSearchForm" value="customerOrdersReportSearchForm" />
	<jsp:param name="reportDataElem" value="customerOrdersReportData" />
</jsp:include>
