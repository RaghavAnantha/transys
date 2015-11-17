<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateCustomerForm() {
	var missingData = validateCustomerMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateCustomerDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateCustomerMissingData() {
	var missingData = "";
	
	if ($('#companyName').val() == "") {
		missingData += "Company Name, "
	}
	
	if ($('#customerTypeSelect').val() == "") {
		missingData += "Customer Type, "
	}
	
	if ($('#chargeCompanySelect').val() == "") {
		missingData += "Charge Company, "
	}
	
	if ($('#customerStatusSelect').val() == "") {
		missingData += "Status, "
	}
	
	if ($('#billingAddressLine1').val() == "") {
		missingData += "Address Line1, "
	}
	
	if ($('#city').val() == "") {
		missingData += "City, "
	}
	
	if ($('#zipcode').val() == "") {
		missingData += "Zipcode, "
	}
	
	if ($('#stateSelect').val() == "") {
		missingData += "State, "
	}
	
	if ($('#contactName').val() == "") {
		missingData += "Contact Name, "
	}
	
	if ($('#phone').val() == "") {
		missingData += "Phone, "
	}
	
	if ($('#email').val() == "") {
		missingData += "Email, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateCustomerDataFormat() {
	var validationMsg = "";
	
	var companyName = $('#companyName').val();
	if (companyName != "") {
		if (!validateCompanyName(companyName, 50)) {
			validationMsg += "Company Name, "
		}
	}
	
	var billingAddressLine1 = $('#billingAddressLine1').val();
	if (billingAddressLine1 != "") {
		if (!validateAddressLine(billingAddressLine1, 50)) {
			validationMsg += "Address Line1, "
		}
	}
	
	var billingAddressLine2 = $('#billingAddressLine2').val();
	if (billingAddressLine2 != "") {
		if (!validateAddressLine(billingAddressLine2, 50)) {
			validationMsg += "Address Line2, "
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
	
	var contactName = $('#contactName').val();
	if (contactName != "") {
		if (!validateName(contactName, 100)) {
			validationMsg += "Contact Name, "
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
	
	var notes = $('#customerNotes').val();
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

function processCustomerForm() {
	if (validateCustomerForm()) {
		var customerForm = $("#customerForm");
		customerForm.submit();
	}
}
</script>
<form:form action="save.do" name="customerForm" id="customerForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCustomer" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left">Company Name<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="companyName" cssClass="flat flat-ext" maxlength="100" />
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			<td class="form-left">Customer Id</td>
			<td class="td-static">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Customer Type<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="customerTypeSelect" path="customerType" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerTypes}" itemValue="id" itemLabel="customerType"/>
				</form:select> 
				<form:errors path="customerType" cssClass="errorMessage" />
			</td> 
			<td class="form-left">Created Date</td>
			<td class="td-static">
				<c:if test="${modelObject.id != null}">
					${modelObject.formattedCreatedAt}
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="form-left">Charge Company<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="chargeCompanySelect" path="chargeCompany" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${chargeCompanyOptions}" />
				</form:select> 
				<form:errors path="chargeCompany" cssClass="errorMessage" />
			</td>
			<td class="form-left">Total Orders</td>
			<td class="td-static">
				<c:if test="${totalOrders != null}">
					${totalOrders}
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="form-left">Status<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="customerStatusSelect" path="customerStatus" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerStatuses}" itemValue="id" itemLabel="status" />
				</form:select> 
				<form:errors path="customerStatus" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Last Delivery" /></td>
			<td class="td-static">
				<c:if test="${formattedDeliveryDate != null}">
					${formattedDeliveryDate}
				</c:if>
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Billing Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Address Line1<span class="errorMessage">*</span></td>
			<td>
				<form:input path="billingAddressLine1" cssClass="flat flat-ext" maxlength="50" />
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Address Line2</td>
			<td>
				<form:input path="billingAddressLine2" cssClass="flat flat-ext" maxlength="50" />
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input cssClass="flat flat-ext" path="city" maxlength="50" />
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext" maxlength="12" />
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="state" id="stateSelect" style="width:172px !important">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>	
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Billing Contact</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
		<td class="form-left">Contact Name<span class="errorMessage">*</span></td>
		<td>
			<form:input path="contactName" cssClass="flat flat-ext" maxlength="100" />	 	
		</td>
		<td class="form-left">Alt Phone1</td>
			<td>
				<form:input path="altPhone1" cssClass="flat flat-ext" maxlength="12" id="altPhone1" onblur="return validateAndFormatPhone('altPhone1');"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone<span class="errorMessage">*</span></td>
			<td>
				<form:input path="phone" cssClass="flat flat-ext" maxlength="12" id="phone" onblur="return validateAndFormatPhone('phone');"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left">Alt Phone2</td>
			<td>
				<form:input path="altPhone2" cssClass="flat flat-ext" maxlength="12" id="altPhone2" onblur="return validateAndFormatPhone('altPhone2');"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Email<span class="errorMessage">*</span></td>
			<td>
				<form:input path="email" cssClass="flat flat-ext" id="email" maxlength="50" />
				<br><form:errors path="email" cssClass="errorMessage" />
			</td>
			<td class="form-left">Fax</td>
			<td>
				<form:input path="fax" cssClass="flat flat-ext" maxlength="12" id="fax" onblur="return validateAndFormatPhone('fax');"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
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
				<c:set var="customerNotesDisabled" value="" />
				<c:if test="${modelObject.customerNotes != null and modelObject.customerNotes.size() > 0 
							and modelObject.customerNotes[0].notes != null and modelObject.customerNotes[0].notes.length() > 0
							and modelObject.id != null}">
					<c:set var="customerNotesDisabled" value="true" />
				</c:if>
				<form:textarea row="5" readonly="${customerNotesDisabled}" path="customerNotes[0].notes" maxlength="500" cssClass="form-control notes" id="customerNotes" style="width:50%;"/>
				<form:errors path="customerNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="customerSubmitBtn" onclick="processCustomerForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="customerBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr><td colspan="2"></td></tr>
	</table>
</form:form>