<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/modal.jsp"%>

<script type="text/javascript">
/*function updateEncodedPassword() {
	var pswd = $('#passwordInput').val();
	alert(pswd);
	var encodedString = btoa(pswd);
	var plainString = atob(encodedString);
	alert(encodedString + ">" + plainString);	
}*/

function validateLoginUserForm() {
	
	var missingData = validateMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
	
}

function validateDataFormat() {
	
	var validationMsg = "";
	
	var pswd = $('#passwordInput').val();
	var changePswd = $('#changePassword').val();
	
	if (pswd != changePswd) {
		validationMsg += "Passwords do not match.";
	}
	
	return validationMsg;
}

function validateMissingData() {
	var missingData = "";
	
	if ($('#employee').val() == "") {
		missingData += "Employee, "
	}
	if ($('#username').val() == "") {
		missingData += "Username, "
	}
	if ($('#passwordInput').val() == "") {
		missingData += "Password, "
	}
	if ($('#role').val() == "") {
		missingData += "Role, "
	}
	if ($("#accountStatus").val() == "") {
		missingData += "Account Status, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

</script>


<br />
<h5 style="margin-top: -15px; !important">Add/Edit Login User</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageLoginUsers" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
		<td class="form-left"><transys:label code="Employee Name" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="employee" cssClass="flat form-control input-sm" path="employee.id" style="width: 175px !important" onChange="return populateDeliveryAddress();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> 
			 	<form:errors path="employee.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Username" /><span class="errorMessage">*</span></td>
			<td ><form:input id="username" path="username" cssClass="flat" style="width: 175px !important"/>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Password" /><span
				class="errorMessage">*</span></td>
			<td><form:input id="passwordInput" type="password" path="password" cssClass="flat" style="width: 175px !important" /> <br>
			<form:errors path="password" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Confirm Password" /><span
				class="errorMessage">*</span></td>
			<td><input id="changePassword" type="password" class="flat" style="width: 175px !important"/> <br>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Role" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="role" cssClass="flat form-control input-sm" path="role.id" style="width:175px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${roles}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="role.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Account Status" /></td>
			<td>
				<form:select id="accountStatus" cssClass="flat form-control input-sm" path="accountStatus.id" style="width:175px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${employeeStatus}" itemValue="id" itemLabel="status" />
				</form:select> 
				<br><form:errors path="accountStatus.id" cssClass="errorMessage" />
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="loginUserComments"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>		
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
			<input type="submit" id="create" onclick="return validateLoginUserForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
			<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>