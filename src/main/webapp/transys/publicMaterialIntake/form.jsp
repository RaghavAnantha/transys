<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Public Material Intake</h5>

<script type="text/javascript">
function populateMaterialTypes() {
	var materialTypeSelect = $("#materialTypeSelect");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	materialTypeSelect.append(firstOption);
	
	var materialCategorySelect = $("#materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	if (materialCategoryId == "") {
		return false;
	}
	
	$.ajax({
  		url: "retrieveMaterialTypes.do?" + "materialCategoryId=" + materialCategoryId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var materialTypesList = jQuery.parseJSON(responseData);
       		$.each(materialTypesList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.materialName
    	   	    }).appendTo(materialTypeSelect);
    	   	});
		}
	});
}

function validatePublicMaterialIntakeForm() {
	var missingData = validatePublicMaterialIntakeMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePublicMaterialIntakeDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePublicMaterialIntakeMissingData() {
	var missingData = "";
	
	if ($('#materialCategory').val() == "") {
		missingData += "Material Category, "
	}
	
	if ($('#materialTypeSelect').val() == "") {
		missingData += "Material Type, "
	}
	
	var intakeDate = $("[name='intakeDate']").val();
	if (intakeDate == "") {
		missingData += "Intake Date, "
	}
	
	if ($('#netWeightTonnage').val() == "") {
		missingData += "Net Weight Tonnage, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validatePublicMaterialIntakeDataFormat() {
	var validationMsg = "";
	
	var intakeDate = $("[name='intakeDate']").val();
	if (intakeDate != "") {
		if (!validateDate(intakeDate)) {
			validationMsg += "Intake Date, "
		}
	}
	
	var netWeightTonnage = $('#netWeightTonnage').val();
	if (netWeightTonnage != "") {
		if (!validateWeight(netWeightTonnage, 700000)) {
			validationMsg += "Net Weight Tonnage, "
		}
	}
	
	var notes = $('#publicMaterialIntakeComments').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function processPublicMaterialIntakeForm() {
	if (validatePublicMaterialIntakeForm()) {
		var publicMaterialIntakeForm = $("#publicMaterialIntakeForm");
		publicMaterialIntakeForm.submit();
	}
}
</script>

<form:form action="save.do" name="publicMaterialIntakeForm" commandName="modelObject" method="post" id="publicMaterialIntakeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePublicMaterialIntake" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<select class="flat form-control input-sm" id="materialCategory" name="materialCategory" style="width: 175px !important" onChange="return populateMaterialTypes();">
					<option value="">----Please Select----</option>
					<c:forEach items="${materialCategories}" var="aMaterialCategory">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.materialType.materialCategory.id == aMaterialCategory.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterialCategory.id}" ${selected}>${aMaterialCategory.category}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Material Type<span class="errorMessage">*</span></td>
			<td>
				<form:select id="materialTypeSelect" cssClass="flat form-control input-sm" path="materialType" style="width: 175px !important" > 
					<form:option value="">----Please Select----</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="materialName" />
				</form:select> 
			 	<form:errors path="materialType" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Intake Date<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="intakeDate" class="flat" id="datepicker7" name="intakeDate" style="width: 175px !important" />
				<form:errors path="intakeDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Net Tonnage<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="netWeightTonnage" cssClass="flat" style="width: 175px !important"  />
				<form:errors path="netWeightTonnage" cssClass="errorMessage" />
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
				<form:textarea row="5" path="comments" cssClass="flat" id="publicMaterialIntakeComments" style="width:50%; height:100%;"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="publicMaterialIntakeCreate" onclick="processPublicMaterialIntakeForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="publicMaterialIntakeCancel" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>