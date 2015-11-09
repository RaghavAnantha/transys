<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateDropOffDriverForm() {
	var missingData = validateDropOffDriverMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateDropOffDriverDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateDropOffDriverMissingData() {
	var missingData = "";
	
	if ($('#dropOffDriverSelect').val() == "") {
		missingData += "Drop-off Driver, "
	}
	
	if ($('#dumpsterNumSelect').val() == "") {
		missingData += "Dumpster #, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateDropOffDriverDataFormat() {
	var validationMsg = "";
	
	var checkNum = $('#dropOffDriverCheckNum').val();
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

function processDropOffDriverForm() {
	if (validateDropOffDriverForm()) {
		var dropOffDriverForm = $("#dropOffDriverAddEditForm");
		dropOffDriverForm.submit();
	}
}
</script>

<form:form action="saveDropOffDriver.do" name="dropOffDriverAddEditForm" commandName="modelObject" method="post" id="dropOffDriverAddEditForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDropOffDriver" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Drop-off Driver" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dropOffDriverSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="dropOffDriver"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="dropOffDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster #" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterNumSelect" cssClass="flat form-control input-sm" path="dumpster" style="width:172px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${orderDumpsters}" itemValue="id" itemLabel="dumpsterNum" />
				</form:select> 
				<form:errors path="dumpster" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Check #"/></td>
			<td>
				<form:input path="orderPayment[0].checkNum" id="dropOffDriverCheckNum" cssClass="flat" style="width:172px !important"/>
				<form:errors path="orderPayment[0].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="dropOffDriverCreate" onclick="processDropOffDriverForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="dropOffDriverBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>