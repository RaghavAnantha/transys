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
	
	if (manageInvoiceSearchForm.find('#manageInvoiceInvoiceNo').val() != ""
			|| manageInvoiceSearchForm.find('#manageInvoiceOrderId').val() != "") {
		return missingData;
	}
	
	if (manageInvoiceSearchForm.find('#manageInvoiceCustomerId').val() == "") {
		missingData += "Company Name, ";
	}
	
	var orderDateFrom = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateFrom']").val();
	var orderDateTo = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateTo']").val();
	var invoiceDateFrom = manageInvoiceSearchForm.find("input[name='manageInvoiceInvoiceDateFrom']").val();
	var invoiceDateTo = manageInvoiceSearchForm.find("input[name='manageInvoiceInvoiceDateTo']").val();
	if ((orderDateFrom == "" || orderDateTo == "")
			&& (invoiceDateFrom == "" || invoiceDateTo == "")) {
		missingData += "Invoice/Orders, ";
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

function processDeleteInvoice(invoiceId) {
	$.get("deleteInvoice.do?id=" + invoiceId, function(data) {
		loadManageInvoice(data);
    });
	
	//document.location = "${ctx}/invoice/deleteInvoice.do?id=" + invoiceId;
}

function loadManageInvoice(data) {
	$("#manageInvoice").html(data);
}

function handleCustomerChange() {
	var deliveryAddressSelect = $('#manageInvoiceDeliveryAddress');
	emptySelect(deliveryAddressSelect);
	
	var customerSelect =  $('#manageInvoiceCustomerId');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateDeliveryAddress(customerId);
}

function retrieveAndPopulateDeliveryAddress(customerId) {
	$.ajax({
  		url: "deliveryAddressSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	populateDeliveryAddress(addressList);
		}
	});
}

function populateDeliveryAddress(addressList) {
	var deliveryAddressSelect = $('#manageInvoiceDeliveryAddress');
	$.each(addressList, function () {
   		$("<option />", {
   	        val: this.id,
   	        text: this.fullLine
   	    }).appendTo(deliveryAddressSelect);
   	});
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
				<select class="flat form-control input-sm" id="manageInvoiceCustomerId" name="manageInvoiceCustomerId" style="width:175px !important"
					onChange="return handleCustomerChange();">
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
			<td class="form-left">Delivery Address<span class="errorMessage"></span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="manageInvoiceDeliveryAddress" name="manageInvoiceDeliveryAddress" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceDeliveryAddress'] == aDeliveryAddress.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.fullLine}</option>
					</c:forEach>
				</select>
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Invoice #<span class="errorMessage"></span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="manageInvoiceInvoiceNo" name="manageInvoiceInvoiceNo" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${invoiceNos}" var="anInvoiceNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceInvoiceNo'] == anInvoiceNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anInvoiceNo}" ${selected}>${anInvoiceNo}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Order #<span class="errorMessage"></span></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="manageInvoiceOrderId" name="manageInvoiceOrderId" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${ordrIds}" var="anOrderId">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceOrderId'] == anOrderId}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderId}" ${selected}>${anOrderId}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice Date From<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker5" name="manageInvoiceInvoiceDateFrom" value="${sessionScope.searchCriteria.searchMap['manageInvoiceInvoiceDateFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Invoice Date To<span class="errorMessage">*</span></td>
			<td>
				<input class="flat" id="datepicker6" name="manageInvoiceInvoiceDateTo" value="${sessionScope.searchCriteria.searchMap['manageInvoiceInvoiceDateTo']}" style="width: 175px" />
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
		<transys:textcolumn headerText="Invoice #" dataField="id" />
		<transys:textcolumn headerText="Invoice Date" dataField="invoiceDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Order Date From" dataField="orderDateFrom" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Order Date To" dataField="orderDateTo" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Order Count" dataField="orderCount" />
		<transys:textcolumn headerText="Total Fees" dataField="totalFees" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Total Discount" dataField="totalDiscount" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Total Amt Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Bal Due" dataField="totalBalanceAmountDue" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:imagecolumn headerText="Download As PDF" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		<transys:imagecolumn headerText="Download As CSV" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
		<transys:imagecolumn headerText="Download As XLS" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
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
