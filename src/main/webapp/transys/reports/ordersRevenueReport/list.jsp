<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	var orderDateFrom = $("[name='createdAtFrom']").val();
	var orderDateTo = $("[name='createdAtTo']").val();
	
	var missingData = false;
	if (orderDateFrom == "" || orderDateTo == "") {
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
<br/>
<h4 style="margin-top: -15px; !important">Orders Revenue Report</h4>
<form:form action="search.do" method="get" name="ordersRevenueReportSearchForm" id="ordersRevenueReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="ordersRevenueReport" />
		<jsp:param name="errorCtx" value="ordersRevenueReport" />
	</jsp:include>
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Order Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker1" name="createdAtFrom" style="width: 175px" /></td>
					
			<td class="form-left"><transys:label code="Order Date To"/></td>
		    <td class="wide"><input class="flat" id="datepicker2" name="createdAtTo" style="width: 175px" /></td>
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
	<jsp:param name="reportSearchForm" value="ordersRevenueReportSearchForm" />
	<jsp:param name="reportDataElem" value="ordersRevenueReportData" />
</jsp:include>
