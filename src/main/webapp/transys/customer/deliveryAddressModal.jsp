<%@include file="/common/taglibs.jsp"%>

<form:form action="${ctx}/customer/saveDeliveryAddressModal.do" name="deliveryAddressModalForm" commandName="deliveryAddressModelObject" method="post" id="deliveryAddressModalForm">
	<form:hidden path="customer.id" id="customerId" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Customer ID</td>
			<td class="td-static">${deliveryAddressModelObject.customer.id}</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Address #<span class="errorMessage">*</span></td>
			<td>
				<form:input path="line1" cssClass="flat flat-ext" id="deliveryAddressModalLine1" maxlength="50"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Street<span class="errorMessage">*</span></td>
			<td>
				<form:input path="line2" cssClass="flat flat-ext" id="deliveryAddressModalLine2" maxlength="50"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input path="city" cssClass="flat flat-ext" id="deliveryAddressModalCity" maxlength="50"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" id="deliveryAddressModalStateSelect">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode</td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext" id="deliveryAddressModalZipcode" maxlength="12"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="deliveryAddressModalSubmitBtn" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">
function validateDeliveryAddressModalForm() {
	var missingData = validateDeliveryAddressModalMissingData();
	if (missingData != "") {
		var alertMsg = "<span><b>Please provide following required data:</b><br></span>"
					 + missingData;
		displayPopupDialogErrorMessage(alertMsg, false);
		
		return false;
	}
	
	var formatValidation = validateDeliveryAddressModalDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		displayPopupDialogErrorMessage(alertMsg, false);
		
		return false;
	}
	
	return true;
}

function validateDeliveryAddressModalMissingData() {
	var missingData = "";
	
	if ($('#deliveryAddressModalLine1').val() == "") {
		missingData += "Delivery Address #, "
	}
	
	if ($('#deliveryAddressModalLine2').val() == "") {
		missingData += "Delivery Street, "
	}
	
	if ($('#deliveryAddressModalCity').val() == "") {
		missingData += "City, "
	}
	
	/*if ($('#deliveryAddressModalZipcode').val() == "") {
		missingData += "Zipcode, "
	}*/
	
	if ($('#deliveryAddressModalStateSelect').val() == "") {
		missingData += "State, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateDeliveryAddressModalDataFormat() {
	var validationMsg = "";
	
	var deliveryAddressLine1 = $('#deliveryAddressModalLine1').val();
	if (deliveryAddressLine1 != "") {
		if (!validateAddressLine(deliveryAddressLine1, 50)) {
			validationMsg += "Delivery Address #, "
		}
	}
	
	var deliveryAddressLine2 = $('#deliveryAddressModalLine2').val();
	if (deliveryAddressLine2 != "") {
		if (!validateAddressLine(deliveryAddressLine2, 50)) {
			validationMsg += "Delivery Street, "
		}
	}
	
	var zipcode = $('#deliveryAddressModalZipcode').val();
	if (zipcode != "") {
		if (!validateZipCode(zipcode, 12)) {
			validationMsg += "Zipcode, "
		}
	}
	
	var city = $('#deliveryAddressModalCity').val();
	if (city != "") {
		if (!validateName(city, 50)) {
			validationMsg += "City, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

$("#deliveryAddressModalForm").submit(function (ev) {
	var $this = $(this);
	
	clearPopupDialogMessages();
	
	if (!validateDeliveryAddressModalForm()) {
		return false;
	}
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		displayPopupDialogErrorMessage(responseData, false);
        	} else {
        		var address = jQuery.parseJSON(responseData);
        		
        		displayPopupDialogSuccessMessage("Delivery address saved successfully", false);
        		appendDeliveryAddress(address);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>