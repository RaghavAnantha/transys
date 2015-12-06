<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processMaterialTypeForm() {
	if (validateMaterialTypeForm()) {
		var materialTypeForm = $("#materialTypeForm");
		materialTypeForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateMaterialTypeForm() {
	var missingData = validateMaterialTypeMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateMaterialTypeDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateMaterialTypeMissingData() {
	var missingData = "";
	
	if ($('#materialCategory').val() == "") {
		missingData += "Material Category, "
	}
	
	if ($('#materialName').val() == "") {
		missingData += "Material Type, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateMaterialTypeDataFormat() {
	var validationMsg = "";
	
	var category = $('#materialCategory').val();
	if (!validateMaterial(category, 100)) {
		validationMsg += "Material Category, ";
	}
	
	var materialType = $('#materialName').val();
	if (!validateMaterial(materialType, 50)) {
		validationMsg += "Material Type, ";
	}
	
	var notes = $('#materialTypeComments').val();
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
<h5 style="margin-top: -15px; !important">Add/Edit Material Types</h5>
<form:form action="save.do" name="materialTypeForm" commandName="modelObject" method="post" id="materialTypeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageMaterialTypes" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="materialCategory" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${materialCategories}" itemValue="id" itemLabel="category" />
				</form:select> 
				<form:errors path="materialCategory" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Material Type<span class="errorMessage">*</span></td>
			<td>
				<form:input path="materialName" cssClass="flat" style="width:172px !important"/>
				<form:errors path="materialName" cssClass="errorMessage" />
			</td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="materialTypeComments" />
				<form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processMaterialTypeForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>