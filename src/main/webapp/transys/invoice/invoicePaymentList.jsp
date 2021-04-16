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
		var alertMsg = "<span style='color:red'><b>Please provide one of the following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateInvoicePaymentSearchMissingData() {
	var missingData = "";
	
	var invoicePaymentSearchForm = getInvoicePaymentSearchForm();;
	
	if (invoicePaymentSearchForm.find('#invoicePaymentInvoiceNo').val() != ""
			|| invoicePaymentSearchForm.find('#invoicePaymentCustomerId').val() != "") {
		return missingData;
	}
	
	var paymentDateFrom = invoicePaymentSearchForm.find("input[name='invoicePaymentDateFrom']").val();
	var paymentDateTo = invoicePaymentSearchForm.find("input[name='invoicePaymentDateTo']").val();
	var orderDateFrom = invoicePaymentSearchForm.find("input[name='invoicePaymentOrderDateFrom']").val();
	var orderDateTo = invoicePaymentSearchForm.find("input[name='invoicePaymentOrderDateTo']").val();
	var invoiceDateFrom = invoicePaymentSearchForm.find("input[name='invoicePaymentInvoiceDateFrom']").val();
	var invoiceDateTo = invoicePaymentSearchForm.find("input[name='invoicePaymentInvoiceDateTo']").val();
	if ((paymentDateFrom == "" || paymentDateTo == "")
			&& (orderDateFrom == "" || orderDateTo == "")
			&& (invoiceDateFrom == "" || invoiceDateTo == "")) {
		missingData += "Invoice #/ Customer/ Payment, Invoice or Order Dates, ";
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function confirmDeleteInvoicePayment(invoicePaymentId) {
	showConfirmDialogWithPurpose("Confirm Invoice Payment Delete", "Do you want to Delete Invoice Payment#: " + invoicePaymentId + "?",
			"DEL_INVOICE_PAYMENT:"+invoicePaymentId);
}

function processDeleteInvoicePayment(invoicePaymentId) {
	$.get("deleteInvoicePayment.do?id=" + invoicePaymentId, function(data) {
		loadInvoicePayment(data);
    });
	
	//document.location = "${ctx}/invoice/deleteInvoicePayment.do?id=" + invoiceId;
}

function processEditInvoicePayment(invoicePaymentId) {
	$.get("editInvoicePayment.do?id=" + invoicePaymentId, function(data) {
		loadInvoicePayment(data);
    });
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
<h5 style="margin-top: -15px; !important">Manage Invoice Payment</h5>
<form:form action="invoicePaymentSearch.do" method="get" id="invoicePaymentSearchForm" name="invoicePaymentSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageInvoicePayment" />
		<jsp:param name="errorCtx" value="manageInvoicePayment" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer<span class="errorMessage"></span></td>
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
			<td class="form-left">Invoice #<span class="errorMessage"></span></td>
			<td class="wide">
				<!-- 
				<select class="flat form-control input-sm" id="invoicePaymentInvoiceNo" name="invoicePaymentInvoiceNo" style="width:175px !important">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${invoiceNos}" var="anInvoiceNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceNo'] == anInvoiceNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anInvoiceNo}" ${selected}>${anInvoiceNo}</option>
					</c:forEach>
				</select>
				 -->
				<input class="flat" id="invoicePaymentInvoiceNo" name="invoicePaymentInvoiceNo" 
					value="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceNo']}" style="width: 175px !important" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Payment Date From<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker7" name="invoicePaymentDateFrom" value="${sessionScope.searchCriteria.searchMap['invoicePaymentDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Payment Date To<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker8" name="invoicePaymentDateTo" value="${sessionScope.searchCriteria.searchMap['invoicePaymentDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice Date From<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker9" name="invoicePaymentInvoiceDateFrom" value="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Invoice Date To<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker10" name="invoicePaymentInvoiceDateTo" value="${sessionScope.searchCriteria.searchMap['invoicePaymentInvoiceDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Order Date From<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker11" name="invoicePaymentOrderDateFrom" value="${sessionScope.searchCriteria.searchMap['invoicePaymentOrderDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Order Date To<span class="errorMessage"></span></td>
			<td>
				<input class="flat" id="datepicker12" name="invoicePaymentOrderDateTo" value="${sessionScope.searchCriteria.searchMap['invoicePaymentOrderDateTo']}" style="width: 175px" />
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
					<img src="${addImage}" title="Make Invoice Payment" class="toolbarButton" border="0">
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
		exportPdf="false" exportXls="false" drawToolbar="false" dataQualifier="invoicePayment">
		<transys:textcolumn headerText="Inv. #" dataField="invoice.id" width="55px"/>
		<transys:textcolumn headerText="Inv. Dt" width="70px" dataField="invoice.invoiceDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Customer" dataField="invoice.companyName" />
		<transys:textcolumn headerText="Pay. #" width="60px" dataField="id" />
		<transys:textcolumn headerText="Pay. Method" width="110px" dataField="paymentMethod.method" />
		<transys:textcolumn headerText="Pay. Dt" width="70px" dataField="paymentDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Amt. Paid" width="68px" dataField="amountPaid" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Check #" dataField="checkNum" />
		<transys:textcolumn headerText="CC Ref. #" dataField="ccReferenceNum" />
		<transys:textcolumn headerText="CC Name" dataField="ccName" />
		<transys:textcolumn headerText="CC #" dataField="ccNumber" />
		<transys:textcolumn headerText="CC Exp. Dt" width="70px" dataField="ccExpDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Notes" dataField="notes" />
		<transys:imagecolumn width="32px" headerText="PDF" linkUrl="${ctx}/invoice/downloadInvoicePayment.do?id={id}&type=pdf" imageSrc="${pdfImage}" HAlign="center" title="PDF"/>
		<transys:imagecolumn width="32px" headerText="CSV" linkUrl="${ctx}/invoice/downloadInvoicePayment.do?id={id}&type=csv" imageSrc="${csvImage}" HAlign="center" title="CSV"/>
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
		processDeleteInvoicePayment(invoicePaymentId);
	}
});
</script>
