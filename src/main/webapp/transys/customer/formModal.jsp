<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateCustomerModalForm() {
	var missingData = validateCustomerModalMissingData();
	if (missingData != "") {
		var alertMsg = "<span><b>Please provide following required data:</b><br></span>"
					 + missingData;
		displayPopupDialogErrorMessage(alertMsg, true);
		
		return false;
	}
	
	var formatValidation = validateCustomerModalDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		displayPopupDialogErrorMessage(alertMsg, true);
		
		return false;
	}
	
	return true;
}

function validateCustomerModalMissingData() {
	var missingData = "";
	
	if ($('#customerModalCompanyName').val() == "") {
		missingData += "Company Name, "
	}
	
	if ($('#customerModalCustomerTypeSelect').val() == "") {
		missingData += "Customer Type, "
	}
	
	if ($('#customerModalChargeCompanySelect').val() == "") {
		missingData += "Charge Company, "
	}
	
	if ($('#customerModalCustomerStatusSelect').val() == "") {
		missingData += "Status, "
	}
	
	if ($('#customerModalBillingAddressLine1').val() == "") {
		missingData += "Address Line1, "
	}
	
	if ($('#customerModalCity').val() == "") {
		missingData += "City, "
	}
	
	if ($('#customerModalZipcode').val() == "") {
		missingData += "Zipcode, "
	}
	
	if ($('#customerModalStateSelect').val() == "") {
		missingData += "State, "
	}
	
	if ($('#customerModalContactName').val() == "") {
		missingData += "Contact Name, "
	}
	
	if ($('#customerModalPhone').val() == "") {
		missingData += "Phone, "
	}
	
	if ($('#customerModalEmail').val() == "") {
		missingData += "Email, "
	}
	
	if ($('#customerModalDeliveryAddressLine1').val() == "") {
		missingData += "Delivery Address #, "
	}
	
	if ($('#customerModalDeliveryAddressLine2').val() == "") {
		missingData += "Delivery Street, "
	}
	
	if ($('#customerModalDeliveryAddressCity').val() == "") {
		missingData += "Delivery City, "
	}
	
	if ($('#customerModalDeliveryAddressZipcode').val() == "") {
		missingData += "Delivery Zipcode, "
	}
	
	if ($('#customerModalDeliveryAddressStateSelect').val() == "") {
		missingData += "Delivery State, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateCustomerModalDataFormat() {
	var validationMsg = "";
	
	var companyName = $('#customerModalCompanyName').val();
	if (companyName != "") {
		if (!validateCompanyName(companyName, 100)) {
			validationMsg += "Company Name, "
		}
	}
	
	var billingAddressLine1 = $('#customerModalBillingAddressLine1').val();
	if (billingAddressLine1 != "") {
		if (!validateAddressLine(billingAddressLine1, 50)) {
			validationMsg += "Address Line1, "
		}
	}
	
	var billingAddressLine2 = $('#customerModalBillingAddressLine2').val();
	if (billingAddressLine2 != "") {
		if (!validateAddressLine(billingAddressLine2, 50)) {
			validationMsg += "Address Line2, "
		}
	}
	
	var zipcode = $('#customerModalZipcode').val();
	if (zipcode != "") {
		if (!validateZipCode(zipcode, 12)) {
			validationMsg += "Zipcode, "
		}
	}
	
	var city = $('#customerModalCity').val();
	if (city != "") {
		if (!validateName(city, 50)) {
			validationMsg += "City, "
		}
	}
	
	var contactName = $('#customerModalContactName').val();
	if (contactName != "") {
		if (!validateName(contactName, 100)) {
			validationMsg += "Contact Name, "
		}
	}
	
	var phone = $('#customerModalPhone').val();
	if (phone != "") {
		if (!validatePhone(phone, 20)) {
			validationMsg += "Phone, "
		}
	}
	
	var altPhone1 = $('#customerModalAltPhone1').val();
	if (altPhone1 != "") {
		if (!validatePhone(altPhone1, 20)) {
			validationMsg += "Alt Phone1, "
		}
	}
	
	var altPhone2 = $('#customerModalAltPhone2').val();
	if (altPhone2 != "") {
		if (!validatePhone(altPhone2, 20)) {
			validationMsg += "Alt Phone2, "
		}
	}
	
	var fax = $('#customerModalFax').val();
	if (fax != "") {
		if (!validatePhone(fax, 20)) {
			validationMsg += "Fax, "
		}
	}
	
	var email = $('#customerModalEmail').val();
	if (email != "") {
		if (!validateEmail(email)) {
			validationMsg += "Email, "
		}
	}
	
	var notes = $('#customerModalCustomerNotes').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	var deliveryAddressLine1 = $('#customerModalDeliveryAddressLine1').val();
	if (deliveryAddressLine1 != "") {
		if (!validateAddressLine(deliveryAddressLine1, 50)) {
			validationMsg += "Delivery Address #, "
		}
	}
	
	var deliveryAddressLine2 = $('#customerModalDeliveryAddressLine2').val();
	if (deliveryAddressLine2 != "") {
		if (!validateAddressLine(deliveryAddressLine2, 50)) {
			validationMsg += "Delivery Street, "
		}
	}
	
	var zipcode = $('#customerModalDeliveryAddressZipcode').val();
	if (zipcode != "") {
		if (!validateZipCode(zipcode, 12)) {
			validationMsg += "Delivery Zipcode, "
		}
	}
	
	var city = $('#customerModalDeliveryAddressCity').val();
	if (city != "") {
		if (!validateName(city, 50)) {
			validationMsg += "Delivery City, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

$("#customerModalForm").submit(function (ev) {
	var $this = $(this);
	
	clearPopupDialogMessages();
	
	if (!validateCustomerModalForm()) {
		return false;
	}
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		displayPopupDialogErrorMessage(responseData.replace("ErrorMsg: ", ""), true);
        	} else {
        		var customer = jQuery.parseJSON(responseData);
        		
        		$("#tdCustomerModalCustomerId").html(customer.id);
        		$("#tdCustomerModalCustomerCreationDt").html(customer.formattedCreatedAt);
        		
        		displayPopupDialogSuccessMessage("Customer saved successfully", true);
        		appendCustomer(customer);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>

<form:form action="${ctx}/customer/saveModal.do" name="customerModalForm" commandName="modelObject" method="post" id="customerModalForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Company Name<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="companyName" cssClass="flat flat-ext" maxlength="100" id="customerModalCompanyName"/>
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			<td class="form-left">Customer Id</td>
			<td class="td-static" id="tdCustomerModalCustomerId">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Customer Type<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" id="customerModalCustomerTypeSelect" style="width:172px !important" path="customerType" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerTypes}" itemValue="id" itemLabel="customerType"/>
				</form:select> 
				<form:errors path="customerType" cssClass="errorMessage" />
			</td>
			<td class="form-left">Created Date</td>
			<td class="td-static" id="tdCustomerModalCustomerCreationDt">
				<c:if test="${modelObject.id != null}">
					${modelObject.formattedCreatedAt}
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="form-left">Charge Company<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" id="customerModalChargeCompanySelect" style="width:172px !important" path="chargeCompany" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${chargeCompanyOptions}" />
				</form:select> 
				<form:errors path="chargeCompany" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Status<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" id="customerModalCustomerStatusSelect" path="customerStatus" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerStatuses}" itemValue="id" itemLabel="status"/>
				</form:select> 
				<form:errors path="customerStatus" cssClass="errorMessage" />
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
				<form:input path="billingAddressLine1" id="customerModalBillingAddressLine1" cssClass="flat flat-ext" maxlength="50"/>
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Address Line2</td>
			<td>
				<form:input path="billingAddressLine2" id="customerModalBillingAddressLine2" cssClass="flat flat-ext" maxlength="50"/>
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input cssClass="flat flat-ext" path="city" id="customerModalCity" maxlength="50"/>
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext" id="customerModalZipcode" maxlength="12" />
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="state" id="customerModalStateSelect" style="width:172px !important">
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
			<form:input path="contactName" cssClass="flat flat-ext" id="customerModalContactName" maxlength="100"/>	 	
		</td>
		<td class="form-left">Alt Phone1</td>
			<td>
				<form:input path="altPhone1" cssClass="flat flat-ext" maxlength="12" 
					id="customerModalAltPhone1" onblur="return validateAndFormatPhoneModal('customerModalAltPhone1');"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone<span class="errorMessage">*</span></td>
			<td>
				<form:input path="phone" cssClass="flat flat-ext" maxlength="12" 
					id="customerModalPhone" onblur="return validateAndFormatPhoneModal('customerModalPhone');"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left">Alt Phone2</td>
			<td>
				<form:input path="altPhone2" cssClass="flat flat-ext" maxlength="12" 
					id="customerModalAltPhone2" onblur="return validateAndFormatPhoneModal('customerModalAltPhone2');"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Email<span class="errorMessage">*</span></td>
			<td>
				<form:input path="email" cssClass="flat flat-ext" id="customerModalEmail" maxlength="50" />
				<br><form:errors path="email" cssClass="errorMessage" />
			</td>
			<td class="form-left">Fax</td>
			<td>
				<form:input path="fax" id="customerModalFax" cssClass="flat flat-ext" maxlength="12" 
					onblur="return validateAndFormatPhoneModal('customerModalFax');"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Delivery Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Delivery Address #<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryAddress[0].line1" id="customerModalDeliveryAddressLine1" cssClass="flat flat-ext" maxlength="50"/>
			 	<br><form:errors path="deliveryAddress[0].line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Street<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryAddress[0].line2" id="customerModalDeliveryAddressLine2" cssClass="flat flat-ext" maxlength="50"/>
			 	<br><form:errors path="deliveryAddress[0].line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryAddress[0].city" id="customerModalDeliveryAddressCity" cssClass="flat flat-ext"/>
			 	<br><form:errors path="deliveryAddress[0].city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="deliveryAddress[0].state" id="customerModalDeliveryAddressStateSelect">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="deliveryAddress[0].state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryAddress[0].zipcode" id="customerModalDeliveryAddressZipcode" cssClass="flat flat-ext" maxlength="12"/>
			 	<br><form:errors path="deliveryAddress[0].zipcode" cssClass="errorMessage" />
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
				<form:textarea row="5" path="customerNotes[0].notes" cssClass="flat" id="customerModalCustomerNotes" style="width:50%; height:100%;" maxlength="500"/>
				<form:errors path="customerNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td></td></tr>
		<tr>
			<td></td>
			<td colspan="2">
				<input type="submit" id="customerModalSubmitBtn" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>