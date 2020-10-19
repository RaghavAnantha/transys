<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	var orderDateFrom = $("[name='createdAtFrom']").val();
	var orderDateTo = $("[name='createdAtTo']").val();
	var customer = $('#customer').val();
	
	var missingData = false;
	if (orderDateFrom.length() == 0 && orderDateTo.length() == 0
			&& customer.length() == 0) {
		missingData = true;
	}	
	
	if (missingData) {
		var alertMsg = "<span style='color:red'><b>Please select any search criteria</b><br></span>"
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}
</script>
<br />
<h5 style="margin-top: -15px; !important">Orders Report</h5>
<form:form action="${urlCtx}/search.do" method="get" name="ordersReportSearchForm" id="ordersReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Customer</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="customer" name="customer" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
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
			<td class="form-left">Order Status</td>
			<td>
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${orderStatuses}" var="oStatus">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['orderStatus'] == oStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${oStatus.id}" ${selected}>${oStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id=deliveryContactPhone1 name="deliveryContactPhone1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${deliveryContactPhones}" var="aDeliveryContactPhone">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryContactPhone1'] == aDeliveryContactPhone}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryContactPhone}" ${selected}>${aDeliveryContactPhone}</option>
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
	<jsp:param name="reportSearchForm" value="ordersReportSearchForm" />
	<jsp:param name="reportDataElem" value="orderListReportData" />
</jsp:include>
