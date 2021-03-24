<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getInvoicePaymentSearchForm() {
	var form = $('#invoicePaymentSearchForm');
	return form;
}

function processInvoicePaymentSearch() {
	if (validateInvoicePaymentSearchForm()) {
		var invoicePaymentSearchForm = getInvoicePaymentSearchForm();
		invoicePaymentSearchForm.submit();
	}
}

function validateInvoicePaymentSearchForm() {
	var missingData = validateInvoicePaymentSearchMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateInvoicePaymentSearchMissingData() {
	var missingData = "";
	
	var invoicePaymentSearchForm = getInvoicePaymentSearchForm();;
	
	if (invoicePaymentSearchForm.find('#invoicePaymentInvoiceNo').val() != "") {
		return missingData;
	}
	
	if (invoicePaymentSearchForm.find('#invoicePaymentCustomerId').val() == "") {
		missingData += "Company Name, ";
	}
	
	var orderDateFrom = invoicePaymentSearchForm.find("input[name='invoicePaymentOrderDateFrom']").val();
	var orderDateTo = invoicePaymentSearchForm.find("input[name='invoicePaymentOrderDateTo']").val();
	var invoiceDateFrom = invoicePaymentSearchForm.find("input[name='invoicePaymentInvoiceDateFrom']").val();
	var invoiceDateTo = invoicePaymentSearchForm.find("input[name='invoicePaymentInvoiceDateTo']").val();
	if ((orderDateFrom == "" || orderDateTo == "")
			&& (invoiceDateFrom == "" || invoiceDateTo == "")) {
		missingData += "Invoice #/Invoice or Order Dates, ";
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function confirmDeleteInvoice(invoiceId) {
	showConfirmDialogWithPurpose("Confirm Invoice Delete", "Do you want to Delete Invoice #: " + invoiceId + "?",
			"DEL_INVOICE:"+invoiceId);
}

function processDeleteInvoicePaymnet(invoiceId) {
	$.get("deleteInvoicePayment.do?id=" + invoiceId, function(data) {
		loadInvoicePayment(data);
    });
	
	//document.location = "${ctx}/invoice/deleteInvoicePayment.do?id=" + invoiceId;
}

function processInvoicePaymentCreate() {
	$.get("createInvoicePayment.do", function(data) {
		loadInvoicePayment(data);
    });
}

function loadInvoicePayment(data) {
	$("#invoicePayment").html(data);
}
</script>

<br />
<h5 style="margin-top: -15px; !important">Manage Invoice Payments</h5>
<form:form action="invoicePaymentSearch.do" method="get" id="invoicePaymentSearchForm" name="invoicePaymentSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageInvoicePayment" />
		<jsp:param name="errorCtx" value="manageInvoicePayment" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="invoicePaymentCustomerId" name="invoicePaymentCustomerId" style="width:175px !important">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['invoicePaymentCustomerId'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Invoice #<span class="errorMessage"><span class="errorMessage">*</span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="invoicePaymentInvoiceNo" name="invoicePaymentInvoiceNo" style="width:175px !important">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${invoiceNos}" var="anInvoiceNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['innvoicePaymentInvoiceNo'] == anInvoiceNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anInvoiceNo}" ${selected}>${anInvoiceNo}</option>
					</c:forEach>
				</select>
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Invoice Date From<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker5" name="invoicePaymentInvoiceDateFrom" value="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Invoice Date To<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker6" name="invoicePaymentInvoiceDateTo" value="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Order Date From<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker3" name="invoicePaymentOrderDateFrom" value="${sessionScope.searchCriteria.searchMap['invoicePaymentOrderDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Order Date To<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker4" name="invoicePaymentOrderDateTo" value="${sessionScope.searchCriteria.searchMap['invoicePaymentOrderDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" id="invoicePaymentSearchSubmitBtn" onclick="processInvoicePaymentSearch();" value="Search" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<table width="100%" class="tab-color">
	<tbody>
		<tr><td/></tr>
		<tr>
			<td>
				<a href="javascript:;" onclick="processInvoicePaymentCreate();">
					<img src="${addImage}" title="Create Invoice" class="toolbarButton" border="0">
				</a>
			</td>
			<td width="90">
				<a href="${ctx}/invoice/invoicePaymentExport.do?dataQualifier=invoicePayment&amp;type=pdf">
					<img src="${pdfImage}" style="float:right;" class="toolbarButton" border="0" title="PDF">
				</a>&nbsp;
				<a href="${ctx}/invoice/invoicePaymentExport.do?dataQualifier=invoicePayment&amp;type=xlsx">
					<img src="${excelImage}" style="float:right;" class="toolbarButton" border="0" title="XLSX">
				</a>&nbsp;
				<a href="${ctx}/invoice/invoicePaymentExport.do?dataQualifier=invoicePayment&amp;type=csv">
					<img src="${csvImage}" style="float:right;" class="toolbarButton" border="0" title="CSV">
				</a>
			</td>
		</tr>
	</tbody>
</table>
<form:form name="invoicePaymentServiceForm" id="invoicePaymentServiceForm" class="tab-color">
	<transys:datatable urlContext="invoice" deletable="false"
		editable="false" cancellable="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="invoicePaymentSearch.do" multipleSelect="false" searcheable="false"
		exportPdf="false" exportXls="false" dataQualifier="invoicePayment">
		<transys:textcolumn headerText="Invoice #" dataField="invoice.id" />
		<transys:textcolumn headerText="Payment Method" dataField="paymentMethod.method" />
		<transys:textcolumn headerText="Payment Date" dataField="paymentDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Amt Paid" dataField="amountPaid" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Check Name" dataField="checkNum" />
		<transys:textcolumn headerText="CC Name" dataField="ccName" />
		<transys:textcolumn headerText="CC #" dataField="ccNumber" />
		<transys:textcolumn headerText="CC Reference #" dataField="ccReferenceNum" />
		<transys:textcolumn headerText="CC Exp. Date" dataField="ccExpDate" dataFormat="MM/dd/yyyy"/>
		<transys:imagecolumn headerText="Download As PDF" linkUrl="${ctx}/invoice/downloadInvoicePayment.do?id={id}&type=pdf" imageSrc="${pdfImage}" HAlign="center" title="PDF"/>
		<transys:imagecolumn headerText="Download As XLS" linkUrl="${ctx}/invoice/downloadInvoicePayment.do?id={id}&type=xlsx" imageSrc="${excelImage}" HAlign="center" title="XLSX"/>
		<transys:imagecolumn headerText="Download As CSV" linkUrl="${ctx}/invoice/downloadInvoicePayment.do?id={id}&type=csv" imageSrc="${csvImage}" HAlign="center" title="CSV"/>
		<transys:imagecolumn headerText="DEL" linkUrl="javascript:confirmDeleteInvoicePayment('{id}');" imageSrc="${deleteImage}" HAlign="center" title="Delete"/>
	</transys:datatable>
	<%session.setAttribute("invoicePaymentColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
$("#invoicePaymentSearchForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	loadInvoicePayment(responseData);
        }
    });
    
    ev.preventDefault();
});

$("#confirmDialogYes").unbind('click').bind('click', function (ev) {
	var confirmPurpose = getConfirmDialogPurpose();
	if (confirmPurpose.indexOf('DEL_INVOICE_PAYMENT') != -1) {
		var invoicePaymnetId = confirmPurpose.split(":")[1];
		processDeleteInvoicePaymnet(invoicePaymentId);
	}
});
</script>
