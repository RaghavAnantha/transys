<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getCreateInvoiceSearchForm() {
	var form = $('#createInvoiceSearchForm');
	return form;
}

function getCreateInvoiceServiceForm() {
	var form = $('#createInvoiceServiceForm');
	return form;
}

function processCreateInvoiceSearch() {
	if (validateCreateInvoiceSearchForm()) {
		var createInvoiceSearchForm = getCreateInvoiceSearchForm();
		createInvoiceSearchForm.submit();
	}
}

function validateCreateInvoiceSearchForm() {
	var missingData = validateCreateInvoiceSearchMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide one of the following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateCreateInvoiceSearchMissingData() {
	var missingData = "";
	
	var createInvoiceSearchForm = getCreateInvoiceSearchForm();
	
	if (createInvoiceSearchForm.find('#orderId').val() != "") {
		return missingData;
	}
	
	if (createInvoiceSearchForm.find('#customerId').val() == "") {
		missingData += "Customer, ";
	}
	
	var orderDateFrom = createInvoiceSearchForm.find("input[name='orderDateFrom']").val();
	var orderDateTo = createInvoiceSearchForm.find("input[name='orderDateTo']").val();
	var deliveryDateFrom = createInvoiceSearchForm.find("input[name='deliveryDateFrom']").val();
	var deliveryDateTo = createInvoiceSearchForm.find("input[name='deliveryDateTo']").val();
	var pickupDateFrom = createInvoiceSearchForm.find("input[name='pickupDateFrom']").val();
	var pickupDateTo = createInvoiceSearchForm.find("input[name='pickupDateTo']").val();
	if (orderDateFrom == "" && orderDateTo == ""
			&& deliveryDateFrom == "" && deliveryDateTo == ""
			&& pickupDateFrom == "" && pickupDateTo == "") {
		missingData += "Order #/ Dates, ";
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function processCreateInvoiceParamsDialogSubmit() {
	//var invoiceParamsDialogInvoiceNo = document.getElementById("createInvoiceParamsDialogInvoiceNo").value;
	
	var invoiceParamsDialogInvoiceDate = document.getElementById("createInvoiceParamsDialogInvoiceDate").value;
	if (!validateDate(invoiceParamsDialogInvoiceDate)) {
		var alertMsg = "<span><b>Please specify valid invoice date</b><br></span>";
		displayPopupDialogErrorMessage(alertMsg, false);

		return false;
	}
	
	var createInvoiceParamsDialogNotes = document.getElementById("createInvoiceParamsDialogNotes").value;
	if (createInvoiceParamsDialogNotes != "") {
		if (!validateText(createInvoiceParamsDialogNotes, 500)) {
			var alertMsg = "<span><b>Please correct following invalid data: Notes</b><br></span>";
			displayPopupDialogErrorMessage(alertMsg, false);

			return false;
		}
	}
	
	setCreateInvoiceServiceFormValues(invoiceParamsDialogInvoiceDate, createInvoiceParamsDialogNotes);
	
	$('#createInvoiceParamsDialogCancelBtn').click();
	
	var form = getCreateInvoiceServiceForm();
	form.attr('action', 'previewInvoice.do');
	form.submit();
}

function setCreateInvoiceServiceFormValues(invoiceDate, invoiceNotes) {
	var createInvoiceServiceForm = getCreateInvoiceServiceForm();
	
	/*var serviceFormInvoiceNoElem = createInvoiceServiceForm.find('#invoiceNo');
	serviceFormInvoiceNoElem.val(invoiceNo);*/
	
	var serviceFormInvoiceDateElem = createInvoiceServiceForm.find('#invoiceDate');
	serviceFormInvoiceDateElem.val(invoiceDate);
	
	var serviceFormInvoiceNotesElem = createInvoiceServiceForm.find('#invoiceNotes');
	serviceFormInvoiceNotesElem.val(invoiceNotes);
}
	
function createInvoice() {
	//var serviceForm = document.getElementById("invoiceServiceForm");
	//var inputs = serviceForm.elements.namedItem("id");
	var inputs = getCreateInvoiceServiceForm().find('input[name=id]');
	var submitForm = false;
	for (var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
			break;
		}
	}
	if (!submitForm) {
		showAlertDialog("Data Validation", "Please select at least one order");
		return;
	}
	
	openCreateInvoiceParamsDialog();
}
	
function openCreateInvoiceParamsDialog() {
	var invoiceParamsDialogId = "createInvoiceParamsDialog";
	showPopupDialogWithHtml("Invoice params", invoiceParamsDialogId); 
}

function handleCreateInvoiceCustomerChange() {
	var deliveryAddressSelect = $('#deliveryAddress');
	emptySelect(deliveryAddressSelect);
	
	var orderIdSelect = $('#orderId');
	emptySelect(orderIdSelect);
	
	var customerSelect =  $('#customerId');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateCreateInvoiceDeliveryAddress(customerId);
	retrieveAndPopulateCreateInvoiceOrderIds(customerId);
}

function retrieveAndPopulateCreateInvoiceDeliveryAddress(customerId) {
	$.ajax({
  		url: "deliveryAddressSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	populateCreateInvoiceDeliveryAddress(addressList);
		}
	});
}

function populateCreateInvoiceDeliveryAddress(addressList) {
	var deliveryAddressSelect = $('#deliveryAddress');
	$.each(addressList, function () {
   		$("<option />", {
   	        val: this.id,
   	        text: this.fullLine
   	    }).appendTo(deliveryAddressSelect);
   	});
}

function retrieveAndPopulateCreateInvoiceOrderIds(customerId) {
	$.ajax({
  		url: "invoicableOrderIdsSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var orderIdList = jQuery.parseJSON(responseData);
    	   	populateCreateInvoiceOrderIds(orderIdList);
		}
	});
}

function populateCreateInvoiceOrderIds(orderIdList) {
	var orderIdSelect = $('#orderId');
	$.each(orderIdList, function () {
   		$("<option />", {
   	        val: this,
   	        text: this
   	    }).appendTo(orderIdSelect);
   	});
}
</script>

<br />
<h5 style="margin-top: -15px; !important">Create Invoice</h5>
<form:form action="createInvoiceSearch.do" method="get" id="createInvoiceSearchForm" name="createInvoiceSearchForm" commandName="modelObject">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="createInvoice" />
		<jsp:param name="errorCtx" value="createInvoice" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="customerId" path="customerId" 
					onChange="return handleCreateInvoiceCustomerChange();">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customers}" itemValue="id" itemLabel="companyName"/>
				</form:select> 
				<form:errors path="customerId" cssClass="errorMessage" />
			</td>
			<td class="form-left">Delivery Address<span class="errorMessage"></span></td>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="deliveryAddress" path="deliveryAddress" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${deliveryAddresses}" itemValue="id" itemLabel="fullLine"/>
				</form:select> 
				<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Order<span class="errorMessage"></span></td>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="orderId" path="orderId">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${orderIds}"/>
				</form:select>
				<form:errors path="orderId" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Order Date From<span class="errorMessage"></span></td>
			<td>
				<form:input path="orderDateFrom" cssClass="flat" style="width:172px !important" id="datepicker" maxlength="10"/>
				<br/><form:errors path="orderDateFrom" cssClass="errorMessage" />
			</td>
			<td class="form-left">Order Date To<span class="errorMessage"></span></td>
			<td>
				<form:input path="orderDateTo" cssClass="flat" style="width:172px !important" id="datepicker1" maxlength="10"/>
				<br/><form:errors path="orderDateTo" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Delivery Date From<span class="errorMessage"></span></td>
			<td>
				<form:input path="deliveryDateFrom" cssClass="flat" style="width:172px !important" id="datepicker2" maxlength="10"/>
				<br/><form:errors path="deliveryDateFrom" cssClass="errorMessage" />
			</td>
			<td class="form-left">Delivery Date To<span class="errorMessage"></span></td>
			<td>
				<form:input path="deliveryDateTo" cssClass="flat" style="width:172px !important" id="datepicker3" maxlength="10"/>
				<br/><form:errors path="deliveryDateTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Pickup Date From<span class="errorMessage"></span></td>
			<td>
				<form:input path="pickupDateFrom" cssClass="flat" style="width:172px !important" id="datepicker4" maxlength="10"/>
				<br/><form:errors path="pickupDateFrom" cssClass="errorMessage" />
			</td>
			<td class="form-left">Pickup Date To<span class="errorMessage"></span></td>
			<td>
				<form:input path="pickupDateTo" cssClass="flat" style="width:172px !important" id="datepicker5" maxlength="10"/>
				<br/><form:errors path="pickupDateTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" id="createInvoiceSearchSubmitBtn" onclick="processCreateInvoiceSearch();" value="Search" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
				<input type="button" id="createInvoiceSearchBackBtn" onclick="document.location.href='manageInvoiceMain.do'" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%" class="tab-color">
	<tbody>
		<tr><td/></tr>
		<tr>
			<td>
				<a href="javascript:;" onclick="createInvoice();">
					<img src="${editImage}" title="Create Invoice Preview" class="toolbarButton" border="0">
					Create Invoice Preview
				</a>
			</td>
			<td width="90">
				<a href="${ctx}/invoice/createInvoiceExport.do?dataQualifier=createInvoice&amp;type=pdf">
					<img src="${pdfImage}" style="float:right;" class="toolbarButton" border="0" title="PDF">
				</a>&nbsp;
				<a href="${ctx}/invoice/createInvoiceExport.do?dataQualifier=createInvoice&amp;type=xlsx">
					<img src="${excelImage}" style="float:right;" class="toolbarButton" border="0" title="XLSX">
				</a>&nbsp;
				<a href="${ctx}/invoice/createInvoiceExport.do?dataQualifier=createInvoice&amp;type=csv">
					<img src="${csvImage}" style="float:right;" class="toolbarButton" border="0" title="CSV">
				</a>
			</td>
		</tr>
	</tbody>
</table>
<form:form name="createInvoiceServiceForm" id="createInvoiceServiceForm" class="tab-color">
	<input type=hidden id="invoiceDate" name="invoiceDate" value=""/>
	<input type=hidden id="invoiceNotes" name="invoiceNotes" value=""/>
	
	<!-- To make datepicker work in modal dialog -->
	<input type=hidden id="createInvoiceParamsDialogInvoiceDate" name="createInvoiceParamsDialogInvoiceDate" value=""/>
	
	<transys:datatable urlContext="invoice" drawToolbar="false" deletable="false"
		editable="false" cancellable="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="createInvoiceSearch.do" multipleSelect="true" searcheable="false"
		exportPdf="false" exportXls="false" dataQualifier="createInvoice">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Ord Dt" dataField="createdAt" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Del L1" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="Del L2" dataField="deliveryAddress.line2" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dmp. Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dmp. #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Del Dt" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Dt" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Dmp. Price" dataField="orderFees.dumpsterPrice" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Ton. Fee" dataField="orderFees.tonnageFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Over Fee" dataField="orderFees.overweightFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Add Fee" dataField="orderFees.totalAdditionalFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Disc." dataField="orderFees.discountAmount" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Tot. Amt" dataField="orderFees.totalFees" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Inv Amt" dataField="invoicedAmount" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
		<transys:textcolumn headerText="Inv." dataField="invoiced"/>
	</transys:datatable>
	<%session.setAttribute("createInvoiceColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<div id="createInvoiceParamsDialog" title="Invoice params" style="display:none;">
	<div id="createInvoiceParamsDialogBody">
		<table id="form-table" class="table">
			<tr>
				<!--
				<td class="form-left">Invoice #<span class="errorMessage">*</span></td>
				<td>
					<input type="text" id="createInvoiceParamsDialogInvoiceNo" style="min-width:175px; max-width:175px" 
						maxlength="50" class="flat flat-ext">
				</td> -->
			</tr>
			<tr>
				<td class="form-left">Invoice Date<span class="errorMessage">*</span></td>
				<td>
					<input type="text" id="createInvoiceParamsDialogInvoiceDate" class="flat" size="15" style="width:175px"/> 
					<script type="text/javascript">
						var $dateElem = $("#createInvoiceParamsDialogInvoiceDate");
						setDatepicker($dateElem);
					</script>
				</td>
			</tr>
			<tr>
				<td class="form-left">Notes<span class="errorMessage"></span></td>
				<td>
					<textarea id="createInvoiceParamsDialogNotes" rows="10" cols="70" class="flat notes"></textarea>
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="2">
					<input type="button" id="createInvoiceParamsDialogSubmitBtn" value="Create Invoice Preview" class="flat btn btn-primary btn-sm btn-sm-ext" 
						onclick="javascript:processCreateInvoiceParamsDialogSubmit();"/>
					<input type="button" id="createInvoiceParamsDialogCancelBtn" value="Cancel" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
				</td>
			</tr>
		</table>
	</div>
</div>