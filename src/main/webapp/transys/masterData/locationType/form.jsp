<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processLocationTypeForm() {
	if (validateLocationTypeForm()) {
		var locationTypeForm = $("#locationTypeForm");
		locationTypeForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateLocationTypeForm() {
	var missingData = validateLocationTypeMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateLocationTypeDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateLocationTypeMissingData() {
	var missingData = "";
	
	if ($('#locationType').val() == "") {
		missingData += "Location Type, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateLocationTypeDataFormat() {
	var validationMsg = "";
	
	var locationType = $('#locationType').val();
	if (!validateMaterial(locationType, 30)) {
		validationMsg += "Location Type, ";
	}
	
	var notes = $('#locationTypeComments').val();
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
<h5 style="margin-top: -15px; !important">Add/Edit Location Type</h5>
<form:form action="save.do" name="locationTypeForm" commandName="modelObject" method="post" id="locationTypeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageLocationTypes" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Location Type" /><span class="errorMessage">*</span></td>
			<td><form:input path="locationType" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="locationType" cssClass="errorMessage" /></td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="locationTypeComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>		
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processLocationTypeForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>