<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processInvoicePaymentForm() {
	if (validateInvoicePaymentForm()) {
		var permitDetailsEditForm = $("#permitDetailsForm");
		permitDetailsEditForm.submit();
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
	
	var formatValidation = validateDetailsDataFormat();
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
	
	if ($('#customerSelect').val() == "") {
		missingData += "Customer, "
	}
	if ($('#deliveryAddressSelect').val() == "") {
		missingData += "Delivery Address, "
	}
	if ($('#locationTypeSelect').val() == "") {
		missingData += "Location Type, "
	}
	if ($('#permitClassSelect').val() == "") {
		missingData += "Permit Class, "
	}
	if ($("#permitTypeSelect").val() == "") {
		missingData += "Permit Type, "
	}
	if ($('#datepicker7').val() == "") {
		missingData += "Start Date, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateDetailsDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateAllText();
	validationMsg += validateFees(); 
	validationMsg += validateAllDates();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function validateAllText() {
	var validationMsg = "";
	
	var notes = $('#permitNotesTextArea').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	return validationMsg;
}

function validateFees() {
	var validationMsg = "";
	
	var parkingMeterFee = $('#parkingMeterFeeInput').val();
	if (parkingMeterFee != "") {
		if (!validateAmount(parkingMeterFee, 1500)) {
			validationMsg += "Parking Meter Fee, "
		}
	}
	
	return validationMsg;
}

function validateAllDates() {
	var validationMsg = "";
	
	var startDate = $("[name='startDate']").val();
	if (startDate != "") {
		if (!validateDate(startDate)) {
			validationMsg += "Start Date, "
		}
	}
	
	return validationMsg;
}
</script>

<form:form action="save.do" id="permitDetailsForm" name="permitDetailsForm" commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePermit" />
		<jsp:param name="errorCtx" value="managePermit" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td>
				<form:select cssClass="flat" path="invoice" style="min-width:184px; max-width:184px">
					<form:option value="">------Please Select------</form:option>
					<form:options items="${invoiceNos}"/>
				</form:select>
				<br><form:errors path="invoice" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Payment Method</td>
			<td class="form-left">Payment Date</td>
			<td class="form-left">Amount</td>
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
				<form:input path="amountPaid" maxlength="7" cssClass="flat" onChange="updateTotalPaid();"/>
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
				<input type="button" id="create" onclick="processInvoicePaymentForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back">" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
