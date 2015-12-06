<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">

function processEmployeeForm() {
	if (validateEmployeeForm()) {
		var employeeForm = $("#employeeForm");
		employeeForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateEmployeeForm() {
	var missingData = validateEmployeeMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateEmployeeDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateEmployeeMissingData() {
	var missingData = "";
	
	if ($('#employeeId').val() == "") {
		missingData += "Employee Id, "
	}
	
	if ($('#firstName').val() == "") {
		missingData += "First Name, "
	}
	
	if ($('#lastName').val() == "") {
		missingData += "Last Name, "
	}
	
	if ($('#jobTitle').val() == "") {
		missingData += "Job Title, "
	}
	
	if ($('#address').val() == "") {
		missingData += "Address, "
	}
	
	if ($('#city').val() == "") {
		missingData += "City, "
	}
	
	if ($('#state').val() == "") {
		missingData += "State, "
	}
	
	if ($('#zipcode').val() == "") {
		missingData += "Zipcode, "
	}
	
	if ($('#phone').val() == "") {
		missingData += "Phone, "
	}
	
	if ($('#email').val() == "") {
		missingData += "Email, "
	}
	
	if ($("[name='hireDate']").val() == "") {
		missingData += "Hire Date, "
	}
	
	if ($('#status').val() == "") {
		missingData += "Status, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateEmployeeDataFormat() {
	var validationMsg = "";
	
	var employeeId = $('#employeeId').val();
	if (employeeId != "") {
		if (!validateDumpsterNum(employeeId, 15)) {
			validationMsg += "Employee Id, "
		}
	}
	
	var firstName = $('#firstName').val();
	if (firstName != "") {
		if (!validateName(firstName, 50)) {
			validationMsg += "First Name, "
		}
	}
	
	var lastName = $('#lastName').val();
	if (lastName != "") {
		if (!validateName(lastName, 50)) {
			validationMsg += "Last Name, "
		}
	}
	
	var address = $('#address').val();
	if (address != "") {
		if (!validateAddressLine(address, 100)) {
			validationMsg += "Address, "
		}
	}
	
	var zipcode = $('#zipcode').val();
	if (zipcode != "") {
		if (!validateZipCode(zipcode, 12)) {
			validationMsg += "Zipcode, "
		}
	}
	
	var city = $('#city').val();
	if (city != "") {
		if (!validateName(city, 50)) {
			validationMsg += "City, "
		}
	}
	
	var phone = $('#phone').val();
	if (phone != "") {
		if (!validatePhone(phone, 20)) {
			validationMsg += "Phone, "
		}
	}
	
	var email = $('#email').val();
	if (email != "") {
		if (!validateEmail(email)) {
			validationMsg += "Email, "
		}
	}
	
	var hireDate = $("[name='hireDate']").val();
	if (hireDate != "") {
		if (!validateDate(hireDate)) {
			validationMsg += "Hire Date, "
		}
	}
	
	var leaveDate = $("[name='leaveDate']").val();
	if (leaveDate != "") {
		if (!validateDate(leaveDate)) {
			validationMsg += "Termination Date, "
		}
	}
	
	if (hireDate != "" && leaveDate != "") {
		if (!validateDateRange(hireDate, leaveDate)) {
			validationMsg += "Hire Date > Termination date, ";
		}
	}
	
	var notes = $('#employeeComments').val();
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

<h5 style="margin-top: -15px; !important">Add/Edit Employee</h5>
<form:form action="save.do" name="employeeForm" commandName="modelObject" method="post" id="employeeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageEmployees" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Employee ID" /><span
				class="errorMessage">*</span></td>
			<td><form:input path="employeeId" cssClass="flat" style="width: 175px !important"/> <br>
			<form:errors path="employeeId" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="First Name" /><span class="errorMessage">*</span></td>
			<td ><form:input path="firstName" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="firstName" cssClass="errorMessage" /></td>
			<td class="form-left"><transys:label code="Last Name" /><span class="errorMessage">*</span></td>
			<td ><form:input path="lastName" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="lastName" cssClass="errorMessage" /></td>
			<td colspan=10></td>
		</tr>
		
		<tr>
		<td class="form-left"><transys:label code="Job Title" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="jobTitle" cssClass="flat form-control input-sm" path="jobTitle" style="width: 175px !important" onChange="return populateDeliveryAddress();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${jobTitleValues}" itemValue="id" itemLabel="jobTitle" />
				</form:select> 
			 	<form:errors path="jobTitle" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Address" /><span class="errorMessage">*</span></td>
			<td><form:input path="address" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="address" cssClass="errorMessage" /></td>
			
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td><form:input path="city" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="city" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="state" style="width:172px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td><form:input path="zipcode" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="zipcode" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left">Phone<span class="errorMessage">*</span></td>
			<td><form:input path="phone" cssClass="flat" style="width: 175px !important" maxlength="12" onblur="return validateAndFormatPhone('phone');"/>
				<br>
			<form:errors path="phone" cssClass="errorMessage" /></td>
			
			<td class="form-left"><transys:label code="E-mail" /><span class="errorMessage">*</span></td>
			<td><form:input path="email" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="email" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Hire Date" /><span class="errorMessage">*</span></td>
			<td class="wide"><form:input path="hireDate" class="flat"
				id="datepicker7" name="hireDate" style="width: 175px" /></td>
				
				<td class="form-left"><transys:label code="Termination Date" /></td>
			<td class="wide"><form:input path="leaveDate" class="flat"
				id="datepicker3" name="leaveDate" style="width: 175px" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Status" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="status" style="width:175px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${employeeStatus}" itemValue="id" itemLabel="status" />
				</form:select> 
				<br><form:errors path="status" cssClass="errorMessage" />
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="employeeComments"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>		
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="return processEmployeeForm();" value="Save"
					class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back"
					class="flat btn btn-primary btn-sm btn-sm-ext" onClick="window.location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>