<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function populateDeliveryAddress() {
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "------Please Select--------" +'</option>');
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
    	   	        text: this.line1 + ", " + this.line2
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
	
	alert(permitClassValue + "," + permitTypeValue + "," + startDateValue)
	
	if (permitClassValue != '' && permitTypeValue != '') {
		$.ajax({
	  		url: "getPermitFee.do?permitClassId=" + permitClassValue + "&permitTypeId=" + permitClassValue + "&startDate=" + startDateValue,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	    	   	$('#permitFeeInput').val(responseData);
			}
		});
	}
}

</script>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="number" cssClass="flat" style="width: 175px !important"  />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Order Number" /></td>
			<td>
				<input value="${associatedOrderID.order.id}" class="form-control" style="width:172px;height:24px !important" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important" onChange="return populateDeliveryAddress();"> 
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<form:errors path="customer" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td>
				<form:input id="permitFeeInput" path="fee" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${editDeliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select> 
			 	<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="locationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<form:errors path="locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important" onChange="return populatePermitFee();" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<form:errors path="permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td>
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important"  onChange="return populateEndDate();populatePermitFee();" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select> 
			 	<form:errors path="permitType" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left">Start Date</td>
			<td class="wide">
				<form:input path="startDate" class="flat"
				id="datepicker7" name="startDate" style="width: 175px !important"  onChange="return populateEndDate();"/></td>
				<form:errors path="startDate" cssClass="errorMessage" />
			<td class="form-left">End Date</td>
			<td>
				<form:input id="endDateInput" path="endDate" cssClass="flat flat-ext form-control" style="width:172px;height:24px !important" readonly="true" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="Yes" label="Yes"></form:option>
					<form:option value="No" label="No"></form:option>
				</form:select> 
			 	<form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="parkingMeterFee" cssClass="flat" style="width: 175px !important"  />
			 	<form:errors path="parkingMeterFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit"  id="create" onclick="return true" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
