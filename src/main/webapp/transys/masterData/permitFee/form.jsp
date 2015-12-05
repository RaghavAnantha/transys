<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processPermitFeeForm() {
	if (validatePermitFeeForm()) {
		var permitFeeForm = $("#permitFeeForm");
		permitFeeForm.submit();
		return true;
	} else {
		return false;
	}
}

function validatePermitFeeForm() {
	var missingData = validatePermitFeeMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePermitFeeDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePermitFeeMissingData() {
	var missingData = "";
	
	if ($('#permitClass').val() == "") {
		missingData += "Permit Class, "
	}
	
	if ($('#permitType').val() == "") {
		missingData += "Permit Type, "
	}
	
	if ($('#fee').val() == "") {
		missingData += "Fee, "
	}
	
	if ($("[name='effectiveStartDate']").val() == "") {
		missingData += "Effective Start Date, "
	}
	
	if ($("[name='effectiveEndDate']").val() == "") {
		missingData += "Effective End Date, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validatePermitFeeDataFormat() {
	var validationMsg = "";
	
	var fee = $('#fee').val();
	if (!validateAmount(fee, 3000)) {
		validationMsg += "Fee, ";
	}
	
	var effectiveStartDate = $("[name='effectiveStartDate']").val();
	var effectiveEndDate = $("[name='effectiveEndDate']").val();
	if (!validateDateRange(effectiveStartDate, effectiveEndDate)) {
		validationMsg += "Effectve date range, ";
	}
	
	var notes = $('#permitFeeComments').val();
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
<h5 style="margin-top: -15px; !important">Add/Edit Permit Fee</h5>
<form:form action="save.do" name="permitFeeForm" commandName="modelObject" method="post" id="permitFeeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePermitFee" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="permitClass" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
				<form:errors path="permitClass" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="permitType" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${permitType}" itemValue="id"  itemLabel="permitType" />
				</form:select> 
				<form:errors path="permitType" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td><form:input path="fee" cssClass="flat" style="width:172px !important"/> <br>
			<form:errors path="fee" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left form-left-ext">Effective Date From<span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" style="width:172px !important"/></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /><span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  style="width:172px !important"/></td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="permitFeeComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>		
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processPermitFeeForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>