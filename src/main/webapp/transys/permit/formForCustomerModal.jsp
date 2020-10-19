<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function validatePermitModalForCustomerForm() {
	var missingData = validatePermitModalForCustomerMissingData();
	if (missingData != "") {
		var alertMsg = "<span><b>Please provide following required data:</b><br></span>"
					 + missingData;
		displayPopupDialogErrorMessage(alertMsg, true);
		
		return false;
	}
	
	var formatValidation = validatePermitModalForCustomerDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		displayPopupDialogErrorMessage(alertMsg, true);
		
		return false;
	}
	
	return true;
}

function validatePermitModalForCustomerMissingData() {
	var missingData = "";
	
	if ($('#permitModalPermitAddressLine1').val() == "") {
		missingData += "Permit Address #, "
	}
	
	if ($('#permitModalPermitAddressLine2').val() == "") {
		missingData += "Permit Street, "
	}
	
	if ($('#permitModalPermitAddressCity').val() == "") {
		missingData += "Permit City, "
	}
	
	/*if ($('#permitModalPermitAddressZipcode').val() == "") {
		missingData += "Permit Zipcode, "
	}*/
	
	if ($('#permitModalPermitAddressStateSelect').val() == "") {
		missingData += "Permit State, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validatePermitModalForCustomerDataFormat() {
	var validationMsg = "";
	
	var permitAddressLine1 = $('#permitModalPermitAddressLine1').val();
	if (permitAddressLine1 != "") {
		if (!validateAddressLine(permitAddressLine1, 50)) {
			validationMsg += "Permit Address #, "
		}
	}
	
	var permitAddressLine2 = $('#permitModalPermitAddressLine2').val();
	if (permitAddressLine2 != "") {
		if (!validateAddressLine(permitAddressLine2, 50)) {
			validationMsg += "Permit Street, "
		}
	}
	
	var permitAddressZipcode = $('#permitModalPermitAddressZipcode').val();
	if (permitAddressZipcode != "") {
		if (!validateZipCode(permitAddressZipcode, 12)) {
			validationMsg += "Permit Zipcode, "
		}
	}
	
	var permitAddressCity = $('#permitModalPermitAddressCity').val();
	if (permitAddressCity != "") {
		if (!validateName(permitAddressCity, 50)) {
			validationMsg += "Permit City, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

$("#permitForCustomerModalForm").submit(function (ev) {
	var $this = $(this);
	
	clearPopupDialogMessages();

	if (!validatePermitModalForCustomerForm()) {
		return false;
	}
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
            	displayPopupDialogErrorMessage(responseData.replace("ErrorMsg: ", ""), false);
        	} else {
        		var permit = jQuery.parseJSON(responseData);
        		
        		displayPopupDialogSuccessMessage("Permit saved successfully", false);
        		appendPermit(permit);
        	}
        }
    });
    
    ev.preventDefault();
});

function populatePermitEndDate() {
	var permitEndDateInput = $('#endDate');
	permitEndDateInput.val("");
	
	var startDate = $("[name='startDate']").val();
	var permitTypeId = $('#permitTypeSelect').val();
	
	if (startDate == '' || permitTypeId == '') {
		return false;
	}
	
	$.ajax({
  		url: "${ctx}/permit/calculatePermitEndDate.do?startDate=" + startDate + "&permitTypeId=" + permitTypeId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		permitEndDateInput.val(responseData);
		}
	});
}
</script>
<form:form action="${ctx}/permit/saveForCustomerModal.do" name="permitForCustomerModalForm" id="permitForCustomerModalForm" commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left">Permit Number</td>
			<td class="wide">
				<form:input path="number" cssClass="flat" style="width: 175px" />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitCustomerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important"> 
					<form:options items="${customers}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<form:errors path="customer" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitDeliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:options items="${deliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select> 
			 	<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<!--
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td>
				<select id="permitAddressFromOrderSelect" class="flat form-control input-sm" style="width: 175px !important" >
					<c:forEach items="${permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.fullLine}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>					
			 	</select>
			</td>
			-->
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Location Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitLocationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important" >
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<form:errors path="locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important" >
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<form:errors path="permitClass" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Permit Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important" >
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select> 
			 	<form:errors path="permitType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Start Date" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="startDate" id="datepicker8" class="flat" style="width: 175px" onChange="return populatePermitEndDate();" />
				<br><form:errors path="startDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="End Date" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="endDate" cssClass="form-control form-control-ext" readonly="true" style="width:175px !important;height:22px" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
		<td class="td-static" id="fee">$${modelObject.fee}</td>
		</tr>
		<tr>
			<td class="form-left">Parking Meter</td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="No"></form:option>
					<form:option value="Yes"></form:option>
				</form:select> 
			 	<form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /></td>
			<td>
				<form:input path="parkingMeterFee" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="parkingMeterFee" cssClass="errorMessage" />
			 	<form:hidden path="fee" id="fee" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Permit Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Permit Address #<span class="errorMessage">*</span></td>
			<td>
				<form:input path="permitAddress[0].line1" id="permitModalPermitAddressLine1" cssClass="flat flat-ext" maxlength="50"/>
			 	<br><form:errors path="permitAddress[0].line1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Permit Street<span class="errorMessage">*</span></td>
			<td>
				<form:input path="permitAddress[0].line2" id="permitModalPermitAddressLine2" cssClass="flat flat-ext" maxlength="50"/>
			 	<br><form:errors path="permitAddress[0].line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input path="permitAddress[0].city" id="permitModalPermitAddressCity" cssClass="flat flat-ext"/>
			 	<br><form:errors path="permitAddress[0].city" cssClass="errorMessage" />
			</td>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="permitAddress[0].state" id="permitModalPermitAddressStateSelect">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="permitAddress[0].state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode</td>
			<td>
				<form:input path="permitAddress[0].zipcode" id="permitModalPermitAddressZipcode" cssClass="flat flat-ext" maxlength="12"/>
			 	<br><form:errors path="permitAddress[0].zipcode" cssClass="errorMessage" />
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
				<c:set var="notesIndex" value='0'/>
			 	<c:if test="${not empty modelObject.permitNotes}">
					<c:set var="notesIndex" value='${modelObject.permitNotes.size()}'/>
				</c:if>
				<form:textarea row="5" path="permitNotes[${notesIndex}].notes" cssClass="form-control notes" style="width:695px;"/>
				<form:errors path="permitNotes[${notesIndex}].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="createPermitForCustomer" onclick="return true;" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="closePermitForCustomer" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>
	
