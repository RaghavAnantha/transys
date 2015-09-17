<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function formatPhone() {	
	var phone = document.getElementById("phone").value;
	if(phone != ""){
		if(phone.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("phone").value = "";
			return true;
		}
		else{
			var str = new String(phone);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var phone = p1 + "-" + p2 + "-" + p3;
				document.getElementById("phone").value = phone;
			}
		}
	}	
}

function validate() {
	return true;
};

function populateDeliveryAddress() {
	$('#deliveryAddressSelect').empty();
	
	var customerId = $('#customerSelect').val();
	$.ajax({
  		url: "customerDeliveryAddress.do?customerId=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	$.each(addressList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.line1
    	   	    }).appendTo('#deliveryAddressSelect');
    	   	});
		}
	}); 
}
</script>
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Order #" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="id" cssClass="flat"  />
			 	<br><form:errors path="id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select id="customerSelect" cssClass="flat form-control input-sm" style="width:175px" path="customer" onChange="return populateDeliveryAddress();"> 
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${customers}" itemValue="id" itemLabel="companyName" />
				</form:select> 
				 <br><form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Delivery Information</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width:175px">
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${deliveryAddresses}" itemValue="id" itemLabel="line1" />
				</form:select> 
				<br><form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Contact Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryContactName" cssClass="flat"  />
			 	<br><form:errors path="deliveryContactName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone1"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryContactPhone1" cssClass="flat" maxlength="12" 
					id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
				<br><form:errors path="deliveryContactPhone1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Phone2"/></td>
			<td align="${left}">
				<form:input path="deliveryContactPhone2" cssClass="flat" />
				 <br><form:errors path="deliveryContactPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Date"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryDate" cssClass="flat" id="datepicker7" name="deliveryDate"/>
				 <br><form:errors path="deliveryDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Delivery Time"/></td>
			<td align="${left}">
				<form:input path="deliveryTimeFrom" cssClass="flat" />&nbsp;to&nbsp;<form:input path="deliveryTimeTo" cssClass="flat" />
				 <br><form:errors path="deliveryTimeFrom" cssClass="errorMessage" /><form:errors path="deliveryTimeTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Location" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="dumpsterLocation" cssClass="flat"  />
			 	<br><form:errors path="id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="dumpsterSize" cssClass="flat"/>
				 <br><form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Material Type"/></td>
			<td align="${left}">
				<form:input path="typeOfMaterial" cssClass="flat" />
				 <br><form:errors path="typeOfMaterial" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Permit Information</td>
		</tr>
		<c:forEach var="aPermit" varStatus="status" items="${permits}">
		    <tr>
		    	<td class="form-left"><transys:label code="Permit${status.index + 1} Class"/><span class="errorMessage">*</span></td>
		        <td align="${left}">
					<form:input path="permits[${status.index}].id" cssClass="flat"/>
				</td>
		    </tr>
		</c:forEach>

		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Scale/Weights Information</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Rates/Fees Information</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Payment Information</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>