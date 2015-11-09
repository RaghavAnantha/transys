<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validatePickupDriverForm() {
	var missingData = validatePickupDriverMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePickupDriverDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePickupDriverMissingData() {
	var missingData = "";
	
	var pickupDate = $("[name='pickupDate']").val();
	if (pickupDate == "") {
		missingData += "Pickup Date, "
	}
	
	if ($('#pickupDriverSelect').val() == "") {
		missingData += "Pickup Driver, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validatePickupDriverDataFormat() {
	var validationMsg = "";
	
	var checkNum = $('#pickupDriverCheckNum').val();
	if (checkNum != "") {
		if (!validateReferenceNum(checkNum, 50)) {
			validationMsg += "Check #, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function processPickupDriverForm() {
	if (validatePickupDriverForm()) {
		var pickupDriverForm = $("#pickupDriverAddEditForm");
		pickupDriverForm.submit();
	}
}
</script>

<form:form action="savePickupDriver.do" name="pickupDriverAddEditForm" commandName="modelObject" method="post" id="pickupDriverAddEditForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePickupDriver" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan="2"></td></tr>
		<tr>
			<td class="form-left">Pickup Date<span class="errorMessage">*</span></td>
			<td>
				<form:input path="pickupDate" cssClass="flat" id="datepicker6" name="pickupDate" style="width:172px !important"/>
				 <form:errors path="pickupDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Pickup Driver<span class="errorMessage">*</span></td>
			<td>
				<form:select id="pickupDriverSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="pickupDriver"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="pickupDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Check #</td>
			<td>
				<form:input path="orderPayment[0].checkNum" id="pickupDriverCheckNum" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="orderPayment[0].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Scale/Weights Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left">Gross<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="grossWeight" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="grossWeight" cssClass="errorMessage" />
			</td>
			<td class="form-left">Tare<span class="errorMessage">*</span></td>
			<td>
				<form:input path="tare" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="tare" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Net Lb"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="netWeightLb" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightLb" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Net Tonnage"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="netWeightTonnage" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightTonnage" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="pickupDriverSubmitBtn" onclick="processPickupDriverForm();" value="Close Order" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="pickupDriverBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>