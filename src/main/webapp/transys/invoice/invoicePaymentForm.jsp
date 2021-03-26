<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getInvoicePaymentForm() {
	var form = $('#invoicePaymentForm');
	return form;
}

function loadInvoicePayment(data) {
	$("#invoicePayment").html(data);
}

function processInvoicePaymentBack() {
	$.get("invoicePaymentMain.do", function(data) {
		loadInvoicePayment(data);
    });
}

function processInvoicePaymentFormSubmit() {
	if (validateInvoicePaymentForm()) {
		var invoicePaymentForm = getInvoicePaymentForm();
		invoicePaymentForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateInvoicePaymentForm() {
	var missingData = validateInvoicePaymentMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateInvoicePaymentDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateInvoicePaymentMissingData() {
	var missingData = "";
	
	var form = getInvoicePaymentForm();
	if (form.find('#createInvoicePaymentCustomerId').val() == "") {
		missingData += "Customer, "
	}
	if (form.find('#createInvoicePaymentInvoiceNo').val() == "") {
		missingData += "Invoice #, "
	}
	if (form.find('#paymentMethod').val() == "") {
		missingData += "Payment method, "
	}
	if (form.find('#amountPaid').val() == "") {
		missingData += "Amount Paid, "
	}
	if (form.find("[name='paymentDate']").val() == "") {
		missingData += "Payment Date, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateInvoicePaymentDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateAmounts(); 
	validationMsg += validateAllDates();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function validateAmounts() {
	var validationMsg = "";
	
	var form = getInvoicePaymentForm();
	var amountPaid = form.find('#amountPaid').val();
	if (amountPaid != "") {
		if (!validateAmount(amountPaid, 15000)) {
			validationMsg += "Amount Paid, "
		}
	}
	
	return validationMsg;
}

function validateAllDates() {
	var validationMsg = "";
	
	var form = getInvoicePaymentForm();
	var paymentDate = form.find("[name='paymentDate']").val();
	if (paymentDate != "") {
		if (!validateDate(paymentDate)) {
			validationMsg += "Payment Date, "
		}
	}
	
	var ccExpDate = form.find("[name='ccExpDate']").val();
	if (ccExpDate != "") {
		if (!validateDate(ccExpDate)) {
			validationMsg += "CC Expiry Date, "
		}
	}
	
	return validationMsg;
}

function handleCreateInvoicePaymentCustomerChange() {
	var invoiceNoSelect = $('#createInvoicePaymentInvoiceNo');
	emptySelect(invoiceNoSelect);
	
	var customerSelect =  $('#createInvoicePaymentCustomerId');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateCreateInvoicePaymentInvoiceNos(customerId);
}

function retrieveAndPopulateCreateInvoicePaymentInvoiceNos(customerId) {
	$.ajax({
  		url: "payableInvoiceNosSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var invoiceNoList = jQuery.parseJSON(responseData);
    	   	populateCreateInvoicePaymentInvoiceNos(invoiceNoList);
		}
	});
}

function populateCreateInvoicePaymentInvoiceNos(invoiceNoList) {
	var invoiceNoSelect = $('#createInvoicePaymentInvoiceNo');
	$.each(invoiceNoList, function () {
   		$("<option />", {
   	        val: this,
   	        text: this
   	    }).appendTo(invoiceNoSelect);
   	});
}
</script>

<br />
<h5 style="margin-top: -15px; !important">Create Invoice Payment</h5>
<form:form action="saveInvoicePayment.do" id="invoicePaymentForm" name="invoicePaymentForm" commandName="invoicePaymentModelObject" method="post" >
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="createInvoicePayment" />
		<jsp:param name="errorCtx" value="createInvoicePayment" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td></td></tr>
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" 
					id="createInvoicePaymentCustomerId" path="invoice.customerId" 
					onChange="return handleCreateInvoicePaymentCustomerChange();">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customers}" itemValue="id" itemLabel="companyName"/>
				</form:select> 
				<form:errors path="invoice.customerId" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice #<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" 
					id="createInvoicePaymentInvoiceNo" path="invoice" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${invoiceNos}"/>
				</form:select> 
				<form:errors path="invoice" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Payment Information</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td class="form-left">Payment Method<span class="errorMessage">*</span></td>
			<td class="form-left">Payment Date<span class="errorMessage">*</span></td>
			<td class="form-left">Amount<span class="errorMessage">*</span></td>
			<td class="form-left">Check #</td>
			<td class="form-left">CC Reference #</td>
			<td class="form-left">CC Name</td>
			<td class="form-left">CC #</td>
			<td class="form-left">CC Expiry Date</td>
		</tr>
		<tr>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static wide">
				<form:input path="paymentDate" cssClass="flat" style="width:172px !important" id="datepicker8" maxlength="10" />
				<form:errors path="paymentDate" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="amountPaid" maxlength="7" cssClass="flat"/>
				<br><form:errors path="amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="checkNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="checkNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="ccReferenceNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="ccName" maxlength="50" cssClass="flat" />
				<br><form:errors path="ccName" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="ccNumber" maxlength="19" cssClass="flat" onChange="return formatCCNumber();" />
				<br><form:errors path="ccNumber" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="ccExpDate" cssClass="flat" style="width:172px !important" maxlength="10" />
				<br><form:errors path="ccExpDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="processInvoicePaymentFormSubmit();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="processInvoicePaymentBack();" />
			</td>
		</tr>
	</table>
</form:form>
	
