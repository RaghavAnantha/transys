<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
/*function updateEncodedPassword() {
	var pswd = $('#passwordInput').val();
	alert(pswd);
	var encodedString = btoa(pswd);
	var plainString = atob(encodedString);
	alert(encodedString + ">" + plainString);	
}*/

function processUserLoginForm() {
	if (validateUserLoginForm()) {
		var userLoginForm = $("#userLoginForm");
		userLoginForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateUserLoginForm() {
	var missingData = validateUserLoginMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateUserLoginDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateUserLoginMissingData() {
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
	if ($('#changePassword').val() == "") {
		missingData += "Confirm Password, "
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

function validateUserLoginDataFormat() {
	var validationMsg = "";
	
	var userName = $('#username').val();
	if (userName != "") {
		if (!validateName(userName, 15)) {
			validationMsg += "Username, "
		}
	}
	
	var passwordInput = $('#passwordInput').val();
	if (passwordInput != "") {
		if (!validatePassword(passwordInput, 15)) {
			validationMsg += "Password, "
		}
	}
	
	var changePassword = $('#changePassword').val();
	if (changePassword != "") {
		if (!validatePassword(changePassword, 15)) {
			validationMsg += "Confirm Password, "
		}
	}
	
	if (passwordInput != changePassword) {
		validationMsg += "Passwords do not match.";
	}
	
	var notes = $('#loginUserComments').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Comments, "
		}
	}
	
	return validationMsg;
}

</script>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit User Logins</h5>
<form:form action="save.do" name="userLoginForm" commandName="modelObject" method="post" id="userLoginForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageLoginUsers" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
		<td class="form-left"><transys:label code="Employee Name" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="employee" style="width: 175px !important"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> 
			 	<form:errors path="employee" cssClass="errorMessage" />
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
			<td class="form-left"><transys:label code="Confirm Password" /><span class="errorMessage">*</span></td>
			<td><input id="changePassword" type="password" class="flat" style="width: 175px !important"/> <br>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Role" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="role" style="width:175px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${roles}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="role" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Account Status" /></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="accountStatus" style="width:175px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${employeeStatus}" itemValue="id" itemLabel="status" />
				</form:select> 
				<br><form:errors path="accountStatus" cssClass="errorMessage" />
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
				<input type="button" id="create" onclick="return processUserLoginForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="window.location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>