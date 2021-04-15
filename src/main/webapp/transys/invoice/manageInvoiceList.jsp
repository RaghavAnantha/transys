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
		var alertMsg = "<span style='color:red'><b>Please provide one of the following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateManageInvoiceSearchMissingData() {
	var missingData = "";
	
	var manageInvoiceSearchForm = getManageInvoiceSearchForm();
	
	var customer = manageInvoiceSearchForm.find('#manageInvoiceCustomerId').val(); 
	var deliveryAddress = manageInvoiceSearchForm.find('#manageInvoiceDeliveryAddress').val();
	if (manageInvoiceSearchForm.find('#manageInvoiceInvoiceNo').val() != ""
			|| manageInvoiceSearchForm.find('#manageInvoiceOrderId').val() != ""
			|| customer != ""
			|| deliveryAddress != "") {
		return missingData;
	}
	
	var orderDateFrom = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateFrom']").val();
	var orderDateTo = manageInvoiceSearchForm.find("input[name='manageInvoiceOrderDateTo']").val();
	var invoiceDateFrom = manageInvoiceSearchForm.find("input[name='manageInvoiceInvoiceDateFrom']").val();
	var invoiceDateTo = manageInvoiceSearchForm.find("input[name='manageInvoiceInvoiceDateTo']").val();
	if ((orderDateFrom == "" || orderDateTo == "")
			&& (invoiceDateFrom == "" || invoiceDateTo == "")
			&& (customer == "")
			&& (deliveryAddress == "")) {
		missingData += "Invoice #/ Order #/ Customer/ Delivery Address/ Invoice or Order Dates, ";
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
	$.ajax({
  		url: "isInvoicableDeletable.do?id=" + invoiceId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData != "true") {
        		showAlertDialog("Data Validation", "Invoice# " + invoiceId
        				+ " cannot be deleted because invoice payment has been made");
        		return false;
        	}
       		document.location.href = "deleteInvoice.do?id=" + invoiceId;
		}
	});
	/*$.get("deleteInvoice.do?id=" + invoiceId, function(data) {
		loadManageInvoice(data);
    });*/
}

function loadManageInvoice(data) {
	$("#manageInvoice").html(data);
}

function handleManageInvoiceCustomerChange() {
	var deliveryAddressSelect = $('#manageInvoiceDeliveryAddress');
	emptySelect(deliveryAddressSelect);
	
	var customerSelect =  $('#manageInvoiceCustomerId');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateManageInvoiceDeliveryAddress(customerId);
}

function retrieveAndPopulateManageInvoiceDeliveryAddress(customerId) {
	$.ajax({
  		url: "deliveryAddressSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	populateManageInvoiceDeliveryAddress(addressList);
		}
	});
}

function populateManageInvoiceDeliveryAddress(addressList) {
	var deliveryAddressSelect = $('#manageInvoiceDeliveryAddress');
	$.each(addressList, function () {
   		$("<option />", {
   	        val: this.id,
   	        text: this.fullLine
   	    }).appendTo(deliveryAddressSelect);
   	});
}

function processMakeInvoicePayment(invoiceId) {
	var $tabToBeShown = findTabToBeShown('invoicePaymentMain.do');
    loadTab($tabToBeShown, "createInvoicePayment.do?invoiceId="+invoiceId);
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
					onChange="return handleManageInvoiceCustomerChange();">
					<option value="">-----Please Select-----</option>
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
					<option value="">-----Please Select-----</option>
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
				<!-- 
				<select class="flat form-control input-sm" id="manageInvoiceInvoiceNo" name="manageInvoiceInvoiceNo" style="width:175px !important">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${invoiceNos}" var="anInvoiceNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceInvoiceNo'] == anInvoiceNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anInvoiceNo}" ${selected}>${anInvoiceNo}</option>
					</c:forEach>
				</select>
				 -->
				<input class="flat" id="manageInvoiceInvoiceNo" name="manageInvoiceInvoiceNo" 
					value="${sessionScope.searchCriteria.searchMap['manageInvoiceInvoiceNo']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Order #<span class="errorMessage"></span></td>
			<td class="wide">
				<!--  
				<select class="flat form-control input-sm" id="manageInvoiceOrderId" name="manageInvoiceOrderId" style="width:175px !important">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${orderIds}" var="anOrderId">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['manageInvoiceOrderId'] == anOrderId}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderId}" ${selected}>${anOrderId}</option>
					</c:forEach>
				</select>
				-->
				<input class="flat" id="manageInvoiceOrderId" name="manageInvoiceOrderId" 
					value="${sessionScope.searchCriteria.searchMap['manageInvoiceOrderId']}" style="width: 175px !important" />
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
			<td>
				<a href="createInvoiceMain.do">
					<img src="${addImage}" title="Create Invoice" class="toolbarButton" border="0">
				</a>
			</td>
			<td width="90">
				<a href="${ctx}/invoice/manageInvoiceExport.do?dataQualifier=manageInvoice&amp;type=pdf">
					<img src="${pdfImage}" style="float:right;" class="toolbarButton" border="0" title="PDF">
				</a>&nbsp;
				<a href="${ctx}/invoice/manageInvoiceExport.do?dataQualifier=manageInvoice&amp;type=xlsx">
					<img src="${excelImage}" style="float:right;" class="toolbarButton" border="0" title="XLSX">
				</a>&nbsp;
				<a href="${ctx}/invoice/manageInvoiceExport.do?dataQualifier=manageInvoice&amp;type=csv">
					<img src="${csvImage}" style="float:right;" class="toolbarButton" border="0" title="CSV">
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
		<transys:textcolumn headerText="Inv.#" dataField="id" width="55px"/>
		<transys:textcolumn headerText="Inv. Date" width="70px" dataField="invoiceDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Customer" dataField="companyName" />
		<transys:textcolumn headerText="Contact" dataField="contactName" width="100px"/>
		<transys:textcolumn headerText="Phone" dataField="formattedPhone1" width="100px"/>
		<transys:textcolumn headerText="Ord. Dt. Fr." width="73px" dataField="orderDateFrom" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Ord. Dt. To" width="73px" dataField="orderDateTo" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Ord. Cnt"  width="32px" dataField="orderCount" />
		<transys:textcolumn headerText="Inv. Amt"  width="70px" dataField="totalBalanceAmountDue" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Pay. made"  width="70px" dataField="totalInvoicePaymentDone" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Bal. Due"  width="70px" dataField="totalInvoiceBalanceDue" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Notes" dataField="notes" />
		<transys:imagecolumn headerText="Pay" width="32px" linkUrl="javascript:processMakeInvoicePayment('{id}');" imageSrc="fas fa-dollar-sign" HAlign="center" title="Make Payment"/>
		<transys:imagecolumn headerText="PDF" width="32px" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=pdf" imageSrc="${pdfImage}" HAlign="center" title="PDF"/>
		<transys:imagecolumn headerText="CSV" width="32px" linkUrl="${ctx}/invoice/downloadInvoice.do?id={id}&type=csv" imageSrc="${csvImage}" HAlign="center" title="CSV"/>
	</transys:datatable>
	<%session.setAttribute("manageInvoiceColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
$("#confirmDialogYes").unbind('click').bind('click', function (ev) {
	var confirmPurpose = getConfirmDialogPurpose();
	if (confirmPurpose.indexOf('DEL_INVOICE') != -1) {
		var invoiceId = confirmPurpose.split(":")[1];
		processDeleteInvoice(invoiceId);
	}
});
</script>
