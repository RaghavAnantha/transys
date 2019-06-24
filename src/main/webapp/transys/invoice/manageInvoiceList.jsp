<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getManageInvoiceSearchForm() {
	var form = $('#manageInvoiceSearchForm');
	return form;
}

function processManageInvoiceSearch() {
	if (validateManageInvoiceSearchForm()) {
		var manageInvoiceSearchForm = getManageInvoiceSearchForm();
		manageInvoiceSearchForm.submit();
	}
}

function validateManageInvoiceSearchForm() {
	var missingData = validateManageInvoiceSearchMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateManageInvoiceSearchMissingData() {
	var missingData = "";
	
	var manageInvoiceSearchForm = getManageInvoiceSearchForm();
	
	if (manageInvoiceSearchForm.find('#manageInvoiceCustomerId').val() == "") {
		missingData += "Company Name, "
	}
	
	var orderDateFrom = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateFrom']").val();
	if (orderDateFrom == "") {
		missingData += "Order Date From, "
	}
	
	var orderDateTo = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateTo']").val();
	if (orderDateTo == "") {
		missingData += "Order Date To, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function confirmDeleteInvoice(invoiceId) {
	showConfirmDialogWithPurpose("Confirm Invoice Delete", "Do you want to Delete Invoice id: " + invoiceId + "?",
			"DEL_INVOICE:"+invoiceId);
}

function processDeleteInvoice(invoiceId) {
	$.get("deleteInvoice.do?id=" + invoiceId, function(data) {
		loadManageInvoice(data);
    });
	
	//document.location = "${ctx}/invoice/deleteInvoice.do?id=" + invoiceId;
}

function loadManageInvoice(data) {
	$("#manageInvoice").html(data);
}
</script>

<br />
<h5 style="margin-top: -15px; !important">Manage Invoice</h5>
<form:form action="manageInvoiceSearch.do" method="get" id="manageInvoiceSearchForm" name="manageInvoiceSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageInvoice" />
		<jsp:param name="errorCtx" value="manageInvoice" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="manageInvoiceCustomerId" name="manageInvoiceCustomerId" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceCustomerId'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Invoice #<span class="errorMessage"></span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="invoiceNo" name="invoiceNo" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${invoiceNos}" var="anInvoiceNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['invoiceNo'] == anInvoiceNo}">
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
				<input class="flat" id="datepicker5" name="invoiceDateFrom" value="${sessionScope.searchCriteria.searchMap['invoiceDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Invoice Date To<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker6" name="invoiceDateTo" value="${sessionScope.searchCriteria.searchMap['invoiceDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Order Date From<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker3" name="manageInvoiceOrderDateFrom" value="${sessionScope.searchCriteria.searchMap['manageInvoiceOrderDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Order Date To<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker4" name="manageInvoiceOrderDateTo" value="${sessionScope.searchCriteria.searchMap['manageInvoiceOrderDateTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" id="manageInvoiceSearchSubmitBtn" onclick="processManageInvoiceSearch();" value="Search" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<table width="100%" class="tab-color">
	<tbody>
		<tr><td/></tr>
		<tr>
			<td></td>
			<td width="90">
				<a href="/transys/invoice/manageInvoiceExport.do?dataQualifier=manageInvoice&amp;type=pdf">
					<img src="/transys/images/pdf.png" style="float:right;" class="toolbarButton" border="0">
				</a>&nbsp;
				<a href="/transys/invoice/manageInvoiceExport.do?dataQualifier=manageInvoice&amp;type=csv">
					<img src="/transys/images/excel.png" style="float:right;" class="toolbarButton" border="0">
				</a>
			</td>
		</tr>
	</tbody>
</table>
<form:form name="manageInvoiceServiceForm" id="manageInvoiceServiceForm" class="tab-color">
	<transys:datatable urlContext="invoice" deletable="false"
		editable="false" cancellable="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="manageInvoiceSearch.do" multipleSelect="false" searcheable="false"
		exportPdf="false" exportXls="false" dataQualifier="manageInvoice">
		<transys:textcolumn headerText="Id" dataField="id" />
		<transys:textcolumn headerText="Invoice #" dataField="invoiceNo" />
		<transys:textcolumn headerText="Invoice Date" dataField="invoiceDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Customer" dataField="companyName" />
		<transys:textcolumn headerText="Total Amt" dataField="totalFees" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:imagecolumn headerText="Download As PDF" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		<transys:imagecolumn headerText="DEL" linkUrl="javascript:confirmDeleteInvoice('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center"/>
	</transys:datatable>
	<%session.setAttribute("manageInvoiceColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
$("#manageInvoiceSearchForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	$("#manageInvoice").html(responseData)
        }
    });
    
    ev.preventDefault();
});

$("#confirmDialogYes").unbind('click').bind('click', function (ev) {
	var confirmPurpose = getConfirmDialogPurpose();
	if (confirmPurpose.indexOf('DEL_INVOICE') != -1) {
		var invoiceId = confirmPurpose.split(":")[1];
		processDeleteInvoice(invoiceId);
	}
});
</script>
