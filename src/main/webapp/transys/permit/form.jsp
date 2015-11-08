<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/modal.jsp"%>

<script type="text/javascript">
function populateDeliveryAddress() {
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	deliveryAddressSelect.append(firstOption);
	
	var customerId = $('#customerSelect').val();
	$.ajax({
  		url: "customerDeliveryAddress.do?customerId=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	$.each(addressList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.fullLine
    	   	    }).appendTo(deliveryAddressSelect);
    	   	});
    
		}
	}); 
}

function populateEndDate() {
	$('#endDateInput').empty();
	var startDateValue = $('#datepicker7').val();
	var permitTypeValue = $('#permitTypeSelect').val();
	
	if (startDateValue != '' && permitTypeValue != '') {
		$.ajax({
	  		url: "calculatePermitEndDate.do?startDate=" + startDateValue + "&permitTypeId=" + permitTypeValue,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	    	   	$('#endDateInput').val(responseData);
			}
		});
	}
}

function populatePermitFee() {
	$('#permitFeeInput').empty();
	var permitClassValue = $('#permitClassSelect').val();
	var permitTypeValue = $('#permitTypeSelect').val();
	var startDateValue = $('#datepicker7').val();
	
	if (permitClassValue != '' && permitTypeValue != '' && startDateValue != '') {
		$.ajax({
	  		url: "getPermitFee.do?permitClassId=" + permitClassValue + "&permitTypeId=" + permitTypeValue + "&startDate=" + startDateValue,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	    	   	$('#permitFeeInput').html("$" + responseData);
	    	   	$('#fee').val(responseData);
			}
		});
	}
}

function validateForm() {
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

function validateMissingData() {
	var missingData = "";
	
	if ($('#customerSelect').val() == "") {
		missingData += "Customer, "
	}
	if ($('#deliveryAddressSelect').val() == "") {
		missingData += "Delivery Address, "
	}
	if ($('#locationTypeSelect').val() == "") {
		missingData += "Location Type, "
	}
	if ($('#permitClassSelect').val() == "") {
		missingData += "Permit Class, "
	}
	if ($("#permitTypeSelect").val() == "") {
		missingData += "Permit Type, "
	}
	if ($('#datepicker7').val() == "") {
		missingData += "Start Date, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateAllText();
	validationMsg += validateFees(); 
	validationMsg += validateAllDates();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function validateAllText() {
	var validationMsg = "";
	
	var notes = $('#permitNotesTextArea').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	return validationMsg;
}

function validateFees() {
	var validationMsg = "";
	
	var parkingMeterFee = $('#parkingMeterFeeInput').val();
	if (parkingMeterFee != "") {
		if (!validateAmount(parkingMeterFee, 1500)) {
			validationMsg += "Parking Meter Fee, "
		}
	}
	
	return validationMsg;
}

function validateAllDates() {
	var validationMsg = "";
	
	var startDate = $("[name='startDate']").val();
	if (startDate != "") {
		if (!validateDate(startDate)) {
			validationMsg += "Start Date, "
		}
	}
	
	return validationMsg;
}

</script>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePermit" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /></td>
			<td class="wide">
				<form:input path="number" cssClass="flat" style="width: 175px !important"  />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Order Number" /></td>
			<td>
				<input value="${associatedOrderID.order.id}" class="form-control form-control-ext" style="width:175px !important;height:22px" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important" onChange="return populateDeliveryAddress();"> 
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${editDeliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select> 
			 	<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="locationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<form:errors path="locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important" onChange="return populatePermitFee();" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<form:errors path="permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important"  onChange="populateEndDate();populatePermitFee();" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select> 
			 	<form:errors path="permitType" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left">Start Date<span class="errorMessage">*</span></td>
			<td class="wide">
			
			<c:set var="readonlyVar" value='false'/>
			<c:if test="${not empty associatedOrderID.order.id}">
				<c:set var="readonlyVar" value='true'/>
			</c:if>
			
			<form:input path="startDate" class="flat flat-ext form-control form-control-ext"
				id="datepicker7" name="startDate" style="width: 175px !important"  onChange="populateEndDate();populatePermitFee();" readonly="${readonlyVar}"/>
			
			</td>
			<form:errors path="startDate" cssClass="errorMessage" />
			<td class="form-left">End Date</td>
			<td>
				<form:input id="endDateInput" path="endDate" cssClass="flat flat-ext form-control form-control-ext" style="width:175px !important;height:22px !important" readonly="true" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td class="td-static" id="permitFeeInput">${modelObject.fee}</td>
			<form:hidden path="fee" id="fee" />
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="Yes" label="Yes"></form:option>
					<form:option value="No" label="No"></form:option>
				</form:select> 
			 	<form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /></td>
			<td>
				<form:input id="parkingMeterFeeInput" path="parkingMeterFee" cssClass="flat" style="width: 175px !important"  />
			 	<form:errors path="parkingMeterFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan="10">
				<c:set var="permitNotesDisabled" value="" />
				<c:if test="${modelObject.permitNotes != null and modelObject.permitNotes.size() > 0 
							and modelObject.permitNotes[0].notes != null and modelObject.permitNotes[0].notes.length() > 0}">
					<c:set var="permitNotesDisabled" value="true" />
				</c:if>
				<form:textarea id="permitNotesTextArea" readonly="${permitNotesDisabled}" row="5" path="permitNotes[0].notes" cssClass="form-control notes" style="width:55.5%;" />
				<form:errors path="permitNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button"  id="create" onclick="validateForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
