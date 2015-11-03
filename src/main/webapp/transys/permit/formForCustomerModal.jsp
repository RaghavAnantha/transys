<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function populatePermitEndDate() {
	var permitEndDateInput = $('#endDate');
	permitEndDateInput.val("");
	
	var startDate = $("[name='startDate']").val();
	var permitTypeId = $('#permitTypeSelect').val();
	
	if (startDate == '' || permitTypeId == '') {
		return false;
	}
	
	$.ajax({
  		url: "/permit/calculatePermitEndDate.do?startDate=" + startDate + "&permitTypeId=" + permitTypeId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		permitEndDateInput.val(responseData);
		}
	});
}

$("#permitForCustomerModalForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
            	displayPopupDialogErrorMessage(responseData.replace("ErrorMsg: ", ""));
        	} else {
        		var permit = jQuery.parseJSON(responseData);
        		
        		displayPopupDialogSuccessMessage("Permit saved successfully");
        		appendPermit(permit);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>
<form:form action="/permit/saveForCustomerModal.do" name="permitForCustomerModalForm" id="permitForCustomerModalForm" commandName="modelObject" method="post" >
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
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitCustomerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important"> 
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitDeliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:options items="${deliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select> 
			 	<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td>
				<select id="permitAddressFromOrderSelect" class="flat form-control input-sm" style="width: 175px !important" >
					<c:forEach items="${permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.fullLine}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>					
			 	</select>
			</td>
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
			<td class="form-left">Parking Meter<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="Yes"></form:option>
					<form:option value="No"></form:option>
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
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="permitNotes[0].notes" cssClass="form-control notes" />
				<form:errors path="permitNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="createPermitForCustomer" onclick="return true;" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="closePermitForCustomer" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>
	
