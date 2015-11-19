<%@include file="/common/taglibs.jsp"%>

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
	
	clearPermitModalMessages();
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		//displayPermitModalErrorMessage(responseData.substring(("ErrorMsg: ").length()));
        		displayPermitModalErrorMessage(responseData.replace("ErrorMsg: ", ""));
        	} else {
        		//var permit = jQuery.parseJSON(responseData);
        		displayPermitModalSuccessMessage("Permit saved successfully");
        		//$this.find("#closePermitFromAlert").click();
        	}
        }
    });
    
    ev.preventDefault();
});
</script>
<form:form action="/permit/savePermitFromAlert.do" name="permitModalFromAlertForm" id="permitModalFromAlertForm" commandName="modelObject" method="post" >
	<form:hidden path="orderId" value="${associatedOrderID.id}" />
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left">Permit Number</td>
			<td class="wide">
				<form:input path="number" cssClass="flat flat-ext" style="width: 175px" />
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
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" style="width: 175px !important">
				<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select>
			 	<form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width: 175px !important" >
					<form:options items="${deliveryAddress}" itemValue="id" itemLabel="fullLine" />
				</form:select>
			 	<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td>
				<select class="flat form-control input-sm" style="width: 175px !important">
					<c:forEach items="${permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.fullLine}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>	
			 	</select>
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="locationTypeSelect" cssClass="flat form-control input-sm" path="locationType" style="width: 175px !important">
				<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select>
			 	<form:errors path="locationType.locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitClassSelect" cssClass="flat form-control input-sm" path="permitClass" style="width: 175px !important">
				<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select>
			 	<form:errors path="permitClass.permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitTypeSelect" cssClass="flat form-control input-sm" path="permitType" style="width: 175px !important" onChange="return populateEndDate();">
					<form:options items="${permitType}" itemValue="id" itemLabel="permitType" />
				</form:select>
			 	<form:errors path="permitType.permitType" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left wide">Start Date</td>
			<td><form:input path="startDate" cssClass="flat flat-ext form-control form-control-ext" id="startDateInputPopUp" name="startDate" style="width: 175px" readonly="true" /></td> <!-- onChange="return populateEndDate();" -->
			<td class="form-left"><transys:label code="End Date" /></td>
			<td>
				<form:input id="endDateInputPopUp" path="endDate" cssClass="flat flat-ext form-control form-control-ext" style="width:175px !important;height:22px" readonly="true" />
			 </td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td class="td-static" id="fee">$${modelObject.fee}</td>
			<%-- <td>
				<form:input path="fee" cssClass="flat flat-ext" style="width: 175px"  />
			 	<form:errors path="fee" cssClass="errorMessage" />
			</td> --%>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="parkingMeter" style="width: 175px !important" >
					<form:option value="No"></form:option>
					<form:option value="Yes"></form:option>
				</form:select> 
			 	<br><form:errors path="parkingMeter" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Parking Meter Fee" /></td>
			<td>
				<form:input path="parkingMeterFee" cssClass="flat flat-ext" style="width: 175px"  />
			 	<br><form:errors path="parkingMeterFee" cssClass="errorMessage" />
			 	<form:hidden path="fee" id="fee" />
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
				<form:textarea row="5" path="permitNotes[0].notes" cssClass="form-control notes" />
				<form:errors path="permitNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="createPermitFromAlert" onclick="return true;" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="closePermitFromAlert" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />		
			</td>
		</tr>
	</table>
</form:form>	
