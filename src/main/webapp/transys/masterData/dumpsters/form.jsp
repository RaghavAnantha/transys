<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">

function processDumpsterForm() {
	if (validateDumpsterForm()) {
		var dumpsterEditForm = $("#dumpsterEditForm");
		dumpsterEditForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateDumpsterForm() {
	var missingData = validateDumpsterMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateDumpsterDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateDumpsterMissingData() {
	var missingData = "";
	
	if ($('#dumpsterSize').val() == "") {
		missingData += "Dumpster Size, "
	}
	if ($('#dumpsterNum').val() == "") {
		missingData += "Dumpster Number, "
	}
	if ($('#dumpsterStatus').val() == "") {
		missingData += "Dumpster Size, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateDumpsterDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateDumpsterCommentsText();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	
	return validationMsg;
}

function validateDumpsterCommentsText() {
	var validationMsg = "";
	
	var notes = $('#dumpsterComments').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Comments, "
		}
	}
	
	return validationMsg;
}

</script>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Dumpsters</h5>
<form:form id="dumpsterEditForm" action="save.do" name="dumpsterEditForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDumpsters" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterSize" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterSize"> 
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id" itemLabel="size" />
				</form:select> 
			 	<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Number" /><span class="errorMessage">*</span></td>
			<td><form:input id="dumpsterNum" path="dumpsterNum" cssClass="flat" style="width:172px !important"/> <br>
			<form:errors path="dumpsterNum" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Status" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterStatus" cssClass="flat form-control input-sm" style="width:172px !important" path="status" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterStatus}" itemValue="id"  itemLabel="status" />
				</form:select> 
				<form:errors path="status" cssClass="errorMessage" />
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
				<form:textarea id="dumpsterComments" row="5" path="comments" cssClass="flat notes" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><input type="button" id="create" onclick="return processDumpsterForm();" value="Save"
				class="flat btn btn-primary btn-sm btn-sm-ext" /> <input type="button" id="cancelBtn" value="<transys:label code="Back"/>"
				class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>