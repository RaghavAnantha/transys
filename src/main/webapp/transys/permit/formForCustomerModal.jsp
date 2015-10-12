<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function populatePermitEndDate() {
	var permitEndDateInput = $('#endDate');
	permitEndDateInput.val("");
	
	var startDate = $('#startDate').val();
	var permitType = $('#permitTypeSelect').val();
	
	if (startDate != '' && permitType != '') {
		$.ajax({
	  		url: "/permit/calculatePermitEndDate.do?startDate=" + startDate + "&permitType=" + permitType,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	       		permitEndDateInput.val(responseData);
			}
		});
	}
}

$("#permitForCustomerModalForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	var permit = jQuery.parseJSON(responseData);
        	appendPermit(permit);
        }
    });
    
    ev.preventDefault();
});
</script>
<br/>
<form:form action="/permit/saveForCustomerModal.do" name="permitForCustomerModalForm" id="permitForCustomerModalForm" commandName="modelObject" method="post" >
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="number" cssClass="flat" style="width: 175px" />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="permitCustomerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important"> 
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
				<form:select id="permitDeliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:options items="${deliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select> 
			 	<br><form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Location Type" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="permitLocationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important" >
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<br><form:errors path="locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important" >
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<br><form:errors path="permitClass" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important" >
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select> 
			 	<br><form:errors path="permitType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Start Date" /></td>
			<td align="${left}">
				<form:input path="startDate" class="flat" style="width: 175px" onChange="return populatePermitEndDate();" />
				<br><form:errors path="startDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="End Date" /></td>
			<td align="${left}">
				<form:input path="endDate" cssClass="form-control" readonly="true" style="width:172px;height:25px !important" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="Yes"></form:option>
					<form:option value="No"></form:option>
				</form:select> 
			 	<br><form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="parkingMeterFee" cssClass="flat" style="width: 175px"  />
			 	<br><form:errors path="parkingMeterFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="createPermitForCustomer" onclick="return true;" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="closePermitForCustomer" value="Close" class="flat btn btn-primary btn-sm" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>
	
