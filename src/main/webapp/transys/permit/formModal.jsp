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
	var startDateValue = $('#datepicker8').val();
	var permitTypeValue = $('#permitTypeSelect').val();
	
	if (startDateValue != '' && permitTypeValue != '') {
		
		$.ajax({
	  		url: "calculatePermitEndDate.do?startDate=" + startDateValue + "&permitTypeId=" + permitTypeValue,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	    	   	$('#endDateInputPopUp').val(responseData);
			}
		});
	}
}

$("#permitModalFromAlertForm").submit(function (ev) {
	var $this = $(this);
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	var permit = jQuery.parseJSON(responseData);
        }
    });
    
    ev.preventDefault();
});
</script>
<br/>
<form:form action="/permit/savePermitFromAlert.do" name="permitModalFromAlertForm" id="permitModalFromAlertForm" commandName="modelObject" method="post" >
	<form:hidden path="orderId" value="${associatedOrderID.id}" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="number" cssClass="flat" style="width: 175px" />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Order Number" /></td>
			<td align="${left}">
				<input value="${associatedOrderID.order.id}" cssClass="flat" style="width: 175px" readOnly="true"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important">
				<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select>
			 	<br><form:errors path="customer" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="fee" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:options items="${deliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select>
			 	<br><form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="locationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important">
				<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select>
			 	<br><form:errors path="locationType.locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important">
				<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select>
			 	<br><form:errors path="permitClass.permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important" onChange="return populateEndDate();">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select>
			 	<br><form:errors path="permitType.permitType" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Start Date" /></td>
			<td align="${left}"><form:input path="startDate" class="flat"
				id="datepicker8" name="startDate" style="width: 175px"  onChange="return populateEndDate();"/></td>
				
			<td class="form-left"><transys:label code="End Date" /></td>
			<td align="${left}">
				<form:input id="endDateInputPopUp" path="endDate" cssClass="flat" style="width: 175px" readonly="true" />
			 	<br><form:errors path="endDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="parkingMeter" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="parkingMeterFee" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="parkingMeterFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Permit Addresses" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:options items="${permitAddress}" itemValue="id" itemLabel="fullLine" />
				 	<br><form:errors path="permitAddress" cssClass="errorMessage" />
			 	</form:select>
			</td>
		</tr>
	   
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="createPermitFromAlert" onclick="return true;" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" />
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="closePermitFromAlertClose" value="Close" class="flat btn btn-primary btn-sm" data-dismiss="modal" />		
			</td>
		</tr>
	</table>
</form:form>	
