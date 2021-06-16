<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function populateNetWeight() {
	var grossWeight = $('#grossWeight').val();
	var tare = $('#tare').val();
	
	if (grossWeight == "" || tare == "") {
		return;
	}

	var grossWeightFloat = parseFloat(grossWeight);
	var tareFloat = parseFloat(tare);
	if (tareFloat > grossWeightFloat) {
		var validationMsg = "Tare cannot be greater than Gross Weight"
		showAlertDialog("Data Validation", validationMsg);
		return false;
	}
	
	var netLbFloat = grossWeightFloat - tareFloat;
	var netTonnageFloat = netLbFloat/2000.00;

	$('#netWeightLb').val(netLbFloat.toFixed(2))
	$('#netWeightTonnage').val(netTonnageFloat.toFixed(2));
}

function validatePickupDriverForm() {
	var missingData = validatePickupDriverMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePickupDriverDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePickupDriverMissingData() {
	var missingData = "";
	
	var pickupDate = $("[name='pickupDate']").val();
	if (pickupDate == "") {
		missingData += "Pickup Date, "
	}
	
	if ($('#pickupDriverSelect').val() == "") {
		missingData += "Pickup Driver, "
	}
	
	if ($('#pickupVehicleNumSelect').val() == "") {
		missingData += "Vehicle #, "
	}
	
	/*if ($('#grossWeight').val() == "") {
		missingData += "Gross Weight, "
	}
	
	if ($('#tare').val() == "") {
		missingData += "Tare, "
	}
	
	if ($('#netWeightLb').val() == "") {
		missingData += "Net Weight Lb, "
	}
	
	if ($('#netWeightTonnage').val() == "") {
		missingData += "Net Weight Tonnage, "
	}*/
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validatePickupDriverDataFormat() {
	var validationMsg = "";
	
	var pickupDate = $("[name='pickupDate']").val();
	if (pickupDate != "") {
		if (!validateDate(pickupDate)) {
			validationMsg += "Pickup Date, "
		}
	}
	
	var grossWeight = $('#grossWeight').val();
	if (grossWeight != "") {
		if (!validateWeight(grossWeight, 700000)) {
			validationMsg += "Gross Weight, "
		}
	}
	
	var tare = $('#tare').val();
	if (tare != "") {
		if (!validateWeight(tare, 700000)) {
			validationMsg += "Tare, "
		}
	}
	
	var netWeightLb = $('#netWeightLb').val();
	if (netWeightLb != "") {
		if (!validateWeight(netWeightLb, 700000)) {
			validationMsg += "Net Weight Lb, "
		}
	}
	
	var netWeightTonnage = $('#netWeightTonnage').val();
	if (netWeightTonnage != "") {
		if (!validateWeight(netWeightTonnage, 700000)) {
			validationMsg += "Net Weight Tonnage, "
		}
	}
	
	var scaleTicketNumber = $('#scaleTicketNumber').val();
	if (scaleTicketNumber != "") {
		if (!validateText(scaleTicketNumber, 50)) {
			validationMsg += "Scale Ticket Number, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function processOrderReadyForPickup() {
	clearProcessOrderMsgs();
	
	var orderId = $('#id').val();
	var readyForPickup = $('#readyForPickupSelect').val();
	
	if (readyForPickup == "") {
		return false;
	}
	
	$.ajax({
  		url: "processOrderReadyForPickup.do?" + "orderId=" + orderId 
  								  		 	  + "&readyForPickup=" + readyForPickup,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("successfully") == -1) {
       			var errorMsgDiv = $('#processOrderErrorMessage');
       			errorMsgDiv.html(responseData);
       		} else {
       			var successMsgDiv = $('#processOrderSuccessMessage');
	       		successMsgDiv.html(responseData);
	       		
	       		var pickupDriverSaveBtn = $('#pickupDriverSaveBtn');
	       		var pickupDriverCloseBtn = $('#pickupDriverCloseBtn');
	       		if (readyForPickup == 'Yes') {
	       			pickupDriverSaveBtn.removeAttr('disabled');
	       			pickupDriverCloseBtn.removeAttr('disabled');
	       		} else {
	       			pickupDriverSaveBtn.attr('disabled', 'disabled');
	       			pickupDriverCloseBtn.attr('disabled', 'disabled');
	       		}
       		}
		}
	});
}

function processOrderReopen() {
	clearProcessOrderMsgs();
	
	var orderId = $('#id').val();
	
	$.ajax({
  		url: "processOrderReopen.do?" + "orderId=" + orderId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("successfully") == -1) {
       			var errorMsgDiv = $('#processOrderErrorMessage');
       			errorMsgDiv.html(responseData);
       		} else {
       			var successMsgDiv = $('#processOrderSuccessMessage');
	       		successMsgDiv.html(responseData);
	       		
	       		var pickupDriverSaveBtn = $('#pickupDriverSaveBtn');
	       		pickupDriverSaveBtn.removeAttr('disabled');
	       		
	       		var pickupDriverCloseBtn = $('#pickupDriverCloseBtn');
	       		pickupDriverCloseBtn.removeAttr('disabled');
	       		
	       		var pickupDriverReopenBtn = $('#pickupDriverReopenBtn');
	       		pickupDriverReopenBtn.attr('disabled', 'disabled');
	       		
	       		var readyForPickupSelect = $('#readyForPickupSelect');
	       		readyForPickupSelect.val('Yes');
	       		var readyForPickupSubmitBtn = $('#readyForPickupSubmitBtn');
	       		readyForPickupSubmitBtn.removeAttr('disabled');
       		}
		}
	});
}

function clearProcessOrderMsgs() {
	var errorMsgDiv = $('#processOrderErrorMessage');
	var successMsgDiv = $('#processOrderSuccessMessage');
	errorMsgDiv.html("");
	successMsgDiv.html("");
}

function processPickupDriverForm(closeOrder) {
	clearProcessOrderMsgs();
	
	if (validatePickupDriverForm()) {
		var pickupDriverForm = $("#pickupDriverAddEditForm");
		
		var action = pickupDriverForm.attr('action');
		action += "?closeOrder="+closeOrder;
		pickupDriverForm.attr('action', action);
		
		pickupDriverForm.submit();
	}
}
</script>

<form:form action="savePickupDriver.do" name="pickupDriverAddEditForm" commandName="modelObject" method="post" id="pickupDriverAddEditForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePickupDriver" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td colspan="2">
				<div id="processOrderErrorMessage" class="error" style="padding:0px;"></div>
   				<div id="processOrderSuccessMessage" class="message" style="padding:0px;"></div>
			</td>
		</tr>
		<tr>
			<td class="form-left">Order #</td>
			<td class="td-static">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Ready For Pickup<span class="errorMessage">*</span></td>
			<td>
				<c:set var="yesSelected" value="" />
				<c:if test="${modelObject.orderStatus.status == 'Pick Up'}">
					<c:set var="yesSelected" value="selected" />
				</c:if>
				<c:set var="noSelected" value="" />
				<c:if test="${modelObject.orderStatus.status == 'Dropped Off'}">
					<c:set var="noSelected" value="selected" />
				</c:if>
				<select class="flat form-control input-sm" id="readyForPickupSelect" name="readyForPickupSelect" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<option value="Yes" ${yesSelected}>Yes</option>
					<option value="No" ${noSelected}>No</option>
				</select>
			</td>
			<td>
				<c:set var="readyForPickupDisabled" value="" />
				<c:if test="${modelObject.id == null 
					|| (modelObject.orderStatus.status != 'Dropped Off' && modelObject.orderStatus.status != 'Pick Up')}">
					<c:set var="readyForPickupDisabled" value="disabled" />
				</c:if>
				<input type="button" id="readyForPickupSubmitBtn" ${readyForPickupDisabled} onclick="processOrderReadyForPickup();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Pickup Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left">Pickup Date<span class="errorMessage">*</span></td>
			<td>
				<form:input path="pickupDate" cssClass="flat" id="datepicker6" name="pickupDate" style="width:172px !important"/>
				<form:errors path="pickupDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Pickup Driver<span class="errorMessage">*</span></td>
			<td>
				<form:select id="pickupDriverSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="pickupDriver"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="pickupDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Vehicle #<span class="errorMessage">*</span></td>
			<td>
				<form:select id="pickupVehicleNumSelect" cssClass="flat form-control input-sm" path="pickupVehicleId" style="width:172px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${orderVehicles}" itemValue="id" itemLabel="number" />
				</form:select> 
				<form:errors path="pickupVehicleId" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Scale/Weights Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left">Gross</td>
			<td class="wide">
				<form:input path="grossWeight" cssClass="flat" style="width:172px !important" onChange="populateNetWeight();"/>
				<br><form:errors path="grossWeight" cssClass="errorMessage" />
			</td>
			<td class="form-left">Tare</td>
			<td>
				<form:input path="tare" cssClass="flat" style="width:172px !important" onChange="populateNetWeight();"/>
				<br><form:errors path="tare" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Net Lb</td>
			<td>
				<form:input path="netWeightLb" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightLb" cssClass="errorMessage" />
			</td>
			<td class="form-left">Net Tonnage</td>
			<td>
				<form:input path="netWeightTonnage" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightTonnage" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Scale Ticket No.</td>
			<td>
				<form:input path="scaleTicketNumber" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="scaleTicketNumber" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<c:set var="saveDisabled" value="" />
				<!--modelObject.orderStatus.status != 'Open'-->
				<c:if test="${modelObject.id == null || modelObject.orderStatus.status != 'Pick Up'}">
					<c:set var="saveDisabled" value="disabled" />
				</c:if>
				<c:set var="closeDisabled" value="" />
				<c:if test="${modelObject.id == null || modelObject.orderStatus.status != 'Pick Up'}">
					<c:set var="closeDisabled" value="disabled" />
				</c:if>
				<c:set var="reopenDisabled" value="" />
				<c:if test="${modelObject.id == null || modelObject.orderStatus.status != 'Closed'}">
					<c:set var="reopenDisabled" value="disabled" />
				</c:if>
				<input type="button" id="pickupDriverSaveBtn" ${saveDisabled} onclick="processPickupDriverForm('false');" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="pickupDriverCloseBtn" ${closeDisabled} onclick="processPickupDriverForm('true');" value="Close Order" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="pickupDriverReopenBtn" ${reopenDisabled} onclick="processOrderReopen();" value="Re-Open Order" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="pickupDriverBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>