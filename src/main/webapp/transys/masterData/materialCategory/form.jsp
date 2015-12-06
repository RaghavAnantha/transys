<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processMaterialCategoryForm() {
	if (validateMaterialCategoryForm()) {
		var materialCategoryForm = $("#materialCategoryForm");
		materialCategoryForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateMaterialCategoryForm() {
	var missingData = validateMaterialCategoryMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateMaterialCategoryDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateMaterialCategoryMissingData() {
	var missingData = "";
	
	if ($('#category').val() == "") {
		missingData += "Material Category, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateMaterialCategoryDataFormat() {
	var validationMsg = "";
	
	var category = $('#category').val();
	if (!validateMaterial(category, 100)) {
		validationMsg += "Material Category, ";
	}
	
	var notes = $('#materialCategoryComments').val();
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
<h5 style="margin-top: -15px; !important">Add/Edit Material Categories</h5>
<form:form action="save.do" name="materialCategoryForm" commandName="modelObject" method="post" id="materialCategoryForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageMaterialCategories" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Material Category" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="category" cssClass="flat" style="width:350px !important"/>
				<form:errors path="category" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="materialCategoryComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processMaterialCategoryForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>