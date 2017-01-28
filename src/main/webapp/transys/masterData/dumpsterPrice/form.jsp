<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processDumpsterPriceForm() {
	if (validateDumpsterPriceForm()) {
		var dumpsterPriceForm = $("#dumpsterPriceForm");
		dumpsterPriceForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateDumpsterPriceForm() {
	var missingData = validateDumpsterPriceMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateDumpsterPriceDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateDumpsterPriceMissingData() {
	var missingData = "";
	
	if ($('#materialCategorySelect').val() == "") {
		missingData += "Material Category, "
	}
	
	/*if ($('#materialTypeSelect').val() == "") {
		missingData += "Material Type, "
	}*/
	
	if ($('#dumpsterSize').val() == "") {
		missingData += "Dumpster Size, "
	}
	
	if ($('#price').val() == "") {
		missingData += "Dumpster Price, "
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

function validateDumpsterPriceDataFormat() {
	var validationMsg = "";
	
	var dumpsterPrice = $('#price').val();
	if (!validateAmount(dumpsterPrice, 3000)) {
		validationMsg += "Dumpster Price, ";
	}
	
	var tonnageFee = $('#tonnageFee').val();
	if (tonnageFee != "") {
		if (!validateAmount(tonnageFee, 1000)) {
			validationMsg += "Tonnage Fee, ";
		}
	}
	
	var effectiveStartDate = $("[name='effectiveStartDate']").val();
	var effectiveEndDate = $("[name='effectiveEndDate']").val();
	if (!validateDateRange(effectiveStartDate, effectiveEndDate)) {
		validationMsg += "Effectve date range, ";
	}
	
	var notes = $('#dumpsterPriceComments').val();
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

function populateMaterialTypes() {
	var materialTypeSelect = $("#materialTypeSelect");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	materialTypeSelect.append(firstOption);
	
	var materialCategorySelect = $("#materialCategorySelect");
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
</script>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Dumpster Price</h5>

<form:form action="save.do" name="dumpsterPriceForm" commandName="modelObject" method="post" id="dumpsterPriceForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDumpsterPrice" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer</td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:175px !important" path="customer" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customers}" itemValue="id"  itemLabel="companyName" />
				</form:select> 
				<form:errors path="customer" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<form:select id="materialCategorySelect" cssClass="flat form-control input-sm" path="materialCategory" style="width: 175px !important" 
					onChange="return populateMaterialTypes();"> 
					<form:option value="">----Please Select----</form:option>
					<form:options items="${materialCategories}" itemValue="id" itemLabel="category" />
				</form:select> 
			 	<form:errors path="materialCategory" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Material Type</td>
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
			<td class="form-left">Dumpster Size<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:173px !important" path="dumpsterSize" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id"  itemLabel="size" />
				</form:select> 
				<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Dumpster Price<span class="errorMessage">*</span></td>
			<td>
				<form:input path="price" cssClass="flat" style="width:173px !important"/>
				<br> 
				<form:errors path="price" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Tonnage Fee</td>
			<td>
				<form:input path="tonnageFee" cssClass="flat" style="width:173px !important"/>
				<br> 
				<form:errors path="tonnageFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left form-left-ext">Effective Date From<span class="errorMessage">*</span></td>
			<td>
				<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" style="width:173px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left">Effective Date To<span class="errorMessage">*</span></td>
			<td>
				<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  style="width:173px !important"/>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="dumpsterPriceComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processDumpsterPriceForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>