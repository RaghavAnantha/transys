<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processPaymentMethodForm() {
	if (validatePaymentMethodForm()) {
		var paymentMethodForm = $("#paymentMethodForm");
		paymentMethodForm.submit();
		return true;
	} else {
		return false;
	}
}

function validatePaymentMethodForm() {
	var missingData = validatePaymentMethodMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePaymentMethodDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePaymentMethodMissingData() {
	var missingData = "";
	
	if ($('#method').val() == "") {
		missingData += "Payment Method, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validatePaymentMethodDataFormat() {
	var validationMsg = "";
	
	var method = $('#method').val();
	if (!validateMaterial(method, 20)) {
		validationMsg += "Payment Method, ";
	}
	
	var notes = $('#paymentMethodComments').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Comments, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	
	return validationMsg;
}
</script>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Payment Method</h5>
<form:form action="save.do" name="paymentMethodForm" commandName="modelObject" method="post" id="paymentMethodForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePaymentMethods" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Payment Method" /><span class="errorMessage">*</span></td>
			<td ><form:input path="method" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="method" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="comments" cssClass="flat notes" id="paymentMethodComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>	
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processPaymentMethodForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>