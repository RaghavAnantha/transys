<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function populateDeliveryAddress() {
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "-----------Please Select----------" +'</option>');
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
	  		url: "calculatePermitEndDate.do?startDate=" + startDateValue + "&permitType=" + permitTypeValue,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	    	   	$('#endDateInput').val(responseData);
			}
		});
	}
}

</script>
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="number" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<%-- <td class="form-left"><transys:label code="Order Number" /></td>
			<td align="${left}">
				<form:input path="order.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="order.id" cssClass="errorMessage" />
			</td> --%>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" onChange="return populateDeliveryAddress();"> 
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<br><form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width:175px">
					<form:option value="">-----------Please Select----------</form:option>
					<%-- <form:options items="${deliveryAddress}" itemValue="id" itemLabel="line1" /> --%>
				</form:select> 
				<%-- <form:input id="deliveryAddressInput" path="deliveryAddress.id" cssClass="flat" style="min-width:350px; max-width:350px"  /> --%>
			 	<br><form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
		</tr>
		 <%--<tr>
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permitAddress" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permitAddress" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Street" /></td>
			<td align="${left}">
				<form:input path="permit.permitStreet" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.permitStreet" cssClass="errorMessage" />
			</td>
		</tr> --%>
		<tr>
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="locationTypeSelect" cssClass="flat form-control input-sm" path="locationType.id" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<br><form:errors path="locationType.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="permitClass.id" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<br><form:errors path="permitClass.id" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType.id" onChange="return populateEndDate();" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select> 
			 	<br><form:errors path="permitType.id" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Start Date" /></td>
			<td align="${left}" class="wide"><form:input path="startDate" class="flat"
				id="datepicker7" name="startDate" style="width: 163px" onChange="return populateEndDate();"/></td>
				
			<td class="form-left"><transys:label code="End Date" /></td>
			<td align="${left}">
				<form:input id="endDateInput" path="endDate" cssClass="flat" style="min-width:350px; max-width:350px" readonly="true" />
			 	<br><form:errors path="endDate" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="fee" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" >
					<form:option value="Yes"></form:option>
					<form:option value="No"></form:option>
				</form:select> 
			 	<br><form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="parkingMeterFee" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="parkingMeterFee" cssClass="errorMessage" />
			</td>
		</tr>
	   
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit"  id="create" onclick="return true" value="<transys:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
