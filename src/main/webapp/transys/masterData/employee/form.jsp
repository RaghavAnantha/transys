<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function formatPhone(){	
	var phone = document.getElementById("phone").value;
	if(phone != ""){
		if(phone.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("phone").value = "";
			return true;
		}
		else{
			var str = new String(phone);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var phone = p1 + "-" + p2 + "-" + p3;
				document.getElementById("phone").value = phone;
			}
		}
	}	
}

</script>
<br />

<h4 style="margin-top: -15px; !important">Add/Edit Employee</h4>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Employee ID" /><span
				class="errorMessage">*</span></td>
			<td><form:input path="employeeId" cssClass="flat" style="width: 175px !important"/> <br>
			<form:errors path="employeeId" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="First Name" /></td>
			<td ><form:input path="firstName" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="firstName" cssClass="errorMessage" /></td>
			<td class="form-left"><transys:label code="Last Name" /></td>
			<td ><form:input path="lastName" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="lastName" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		<td class="form-left"><transys:label code="Job Title" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="jobTitle" cssClass="flat form-control input-sm" path="jobTitle" style="width: 175px !important" onChange="return populateDeliveryAddress();"> 
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${jobTitleValues}" itemValue="id" itemLabel="jobTitle" />
				</form:select> 
			 	<form:errors path="jobTitle" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Address" /></td>
			<td><form:input path="address" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="address" cssClass="errorMessage" /></td>
			
			<td class="form-left"><transys:label code="City" /></td>
			<td><form:input path="city" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="city" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="State" /></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="state.id" style="width:172px !important">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state.id" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Zipcode" /></td>
			<td><form:input path="zipcode" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="zipcode" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Phone" /></td>
			<td align="${left}"><form:input path="phone" cssClass="flat" style="width: 175px !important" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
				<br>
			<form:errors path="phone" cssClass="errorMessage" /></td>
			
			<td class="form-left"><transys:label code="EMail" /></td>
			<td><form:input path="email" cssClass="flat" style="width: 175px !important"/>
				<br>
			<form:errors path="email" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Hire Date" /></td>
			<td class="wide"><form:input path="hireDate" class="flat"
				id="datepicker7" name="hireDate" style="width: 175px" /></td>
				
				<td align="${left}" class="form-left"><transys:label code="Termination Date" /></td>
			<td align="${left}" class="wide"><form:input path="leaveDate" class="flat"
				id="datepicker3" name="leaveDate" style="width: 175px" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Status" /></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="status" style="width:172px !important">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${employeeStatus}" itemValue="id" itemLabel="status" />
				</form:select> 
				<br><form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="comments" cssClass="flat" id="employeeComments" style="width:100%; height:150%;"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>		
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
			<input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
			<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>