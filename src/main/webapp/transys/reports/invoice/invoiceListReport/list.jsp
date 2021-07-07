<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateSubmit() {
	var missingData = validateInvoiceListReportSearchMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
			 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateInvoiceListReportSearchMissingData() {
	var form = $('#invoiceListReportSearchForm');
	var invoiceDateFrom = form.find("[name='invoiceListReportInvoiceDateFrom']").val();
	var invoiceDateTo = form.find("[name='invoiceListReportInvoiceDateTo']").val();
	var customer = form.find('#invoiceListReportCustomer').val();
	var invoiceNo = form.find('#invoiceListReportInvoiceNo').val();
	
	var missingData = "";
	if (customer.length == 0) {
		missingData = "Customer, ";
	}
	if ((invoiceDateFrom.length == 0 || invoiceDateTo.length == 0)
			&& invoiceNo.length == 0) {
		missingData += "Invoice Dates/Invoice #, ";
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}
</script>
<br />
<h5 style="margin-top: -15px; !important">Invoice Report</h5>
<form:form action="${urlCtx}/invoice/reports/invoiceListReportSearch.do" method="get" name="invoiceListReportSearchForm" id="invoiceListReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Customer</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="invoiceListReportCustomer" name="invoiceListReportCustomer" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['invoiceListReportCustomer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice #<span class="errorMessage"></span></td>
			<td class="wide">
				<input class="flat" id="invoiceListReportInvoiceNo" name="invoiceListReportInvoiceNo" 
					value="${sessionScope.searchCriteria.searchMap['invoiceListReportInvoiceNo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice Date From<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker13" name="invoiceListReportInvoiceDateFrom" value="${sessionScope.searchCriteria.searchMap['invoiceListReportInvoiceDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Invoice Date To<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker14" name="invoiceListReportInvoiceDateTo" value="${sessionScope.searchCriteria.searchMap['invoiceListReportInvoiceDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="submit" class="btn btn-primary btn-sm btn-sm-ext" value="Preview" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
		<tr><td></td></tr>
	</table>
</form:form>
<jsp:include page="${reportsMenuCtx}/reportToolbar.jsp">
	<jsp:param name="reportSearchForm" value="invoiceListReportSearchForm" />
	<jsp:param name="reportDataElem" value="invoiceListReportData" />
	<jsp:param name="reportExportAction" value="${urlCtx}/invoice/reports/invoiceListReportExport.do" />
</jsp:include>
