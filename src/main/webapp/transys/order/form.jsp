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

function populateCustomerInfo() {
	populateDeliveryAddress();
	populateCustomerAddress();
}

function populateDeliveryAddress() {
	var customerSelect =  $('#customerSelect');
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "-------Please Select------" +'</option>');
	deliveryAddressSelect.append(firstOption);
	
	var customerId = customerSelect.val();
	$.ajax({
  		url: "customerDeliveryAddress.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	$.each(addressList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.line1 + " " + this.line2
    	   	    }).appendTo(deliveryAddressSelect);
    	   	});
		}
	});
}

function populateCustomerAddress() {
	var customerId = $('#customerSelect').val();
	$.ajax({
  		url: "customerAddress.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var customer = jQuery.parseJSON(responseData);
    	   	
    	   	var address = customer.billingAddressLine1 + ", " 
    	   					+ customer.city + ", " + customer.state.name + ", " + customer.zipcode
    	   	$('#address').html(address);
    	   	
    		$('#contact').html(customer.contactName);
    		$('#fax').html(customer.fax);
    		
    		var phone = customer.phone;
    		var p1 = phone.substring(0,3);
			var p2 = phone.substring(3,6);
			var p3 = phone.substring(6,10);				
			$('#phone').html(p1 + "-" + p2 + "-" + p3);
    		
    		$('#email').html(customer.email);
		}
	}); 
}

function appendDeliveryAddress(address) {
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	var newAddressOption = $('<option value=' + address.id + '>'+ address.line1 + " " + address.line2 +'</option>');
	deliveryAddressSelect.append(newAddressOption);
}

function appendCustomer(customer) {
	var customerSelect = $('#customerSelect');
	var newCustomerOption = $('<option value=' + customer.id + '>'+ customer.companyName +'</option>');
	customerSelect.append(newCustomerOption);
}

function populatePermitNumbers(index) {
	var permitClassSelect = $("#permitClasses" + index);
	var permitClassId = permitClassSelect.val();
	
	var permitTypeSelect = $("#permitTypes" + index);
	var permitTypeId = permitTypeSelect.val();
	
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	permitNumbersSelect.empty();
	
	var firstOption = $('<option value="">'+ "-------Please Select------" +'</option>');
	permitNumbersSelect.append(firstOption);
	
	$.ajax({
  		url: "retrievePermit.do?" + "permitClassId=" + permitClassId + "\&permitTypeId=" + permitTypeId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var permitList = jQuery.parseJSON(responseData);
    	   	$.each(permitList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.number
    	   	    }).appendTo(permitNumbersSelect);
    	   	});
		}
	}); 
}

function populatePermitDateAndFee(index) {
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	var permitId = permitNumbersSelect.val();
	
	var permitValidFrom = $("#permitValidFrom" + index);
	var permitValidTo = $("#permitValidTo" + index);
	var permitFee = $("#permitFee" + index);
	
	$.ajax({
  		url: "retrievePermit.do?" + "permitId=" + permitId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var permitList = jQuery.parseJSON(responseData);
    	   	var permit = permitList[0];
    	   	
    	   	permitValidFrom.html(permit.startDate);
    	   	permitValidTo.html(permit.endDate);
    	   	permitFee.html(permit.fee);
		}
	}); 
}

$("#addDeliveryAddressModal").on("show.bs.modal", function(e) {
	var customerId = $('#customerSelect').val();
	
    var link = $(e.relatedTarget).attr("href");
    link += "?customerId=" + customerId;
    
    $(this).find("#addDeliveryAddressModalBody").load(link);
});

$("#addCustomerModal").on("show.bs.modal", function(e) {
	var link = $(e.relatedTarget).attr("href");
    
    $(this).find("#addCustomerModalBody").load(link);
});
</script>
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Order #" /><span class="errorMessage">*</span></td>
			<td align="${left}">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<label style="display: inline-block; font-weight: normal">
					<form:select id="customerSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="customer" onChange="return populateCustomerInfo();"> 
						<form:option value="">-------Please Select------</form:option>
						<form:options items="${customers}" itemValue="id" itemLabel="companyName" />
					</form:select>
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="/customer/createModal.do" id="addCustomerLink" data-backdrop="static" data-remote="false" data-toggle="modal" data-target="#addCustomerModal">
						<img src="/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label> 
				<br><form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Address" /><span class="errorMessage"></span></td>
			<c:set var="address" value="${modelObject.customer.billingAddressLine1}" />
			<td align="${left}" id="address">${address}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Contact" /><span class="errorMessage"></span></td>
			<td align="${left}" id="contact">${modelObject.customer.contactName}</td>
			<td class="form-left"><transys:label code="Fax"/></td>
			<td align="${left}" id="fax">${modelObject.customer.fax}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone" /><span class="errorMessage"></span></td>
			<td align="${left}" id="phone">${modelObject.customer.phone}</td>
			<td class="form-left"><transys:label code="Email"/></td>
			<td align="${left}" id="email">${modelObject.customer.phone}</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Delivery Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width:172px !important">
						<form:option value="">-------Please Select------</form:option>
						<form:options items="${deliveryAddresses}" itemValue="id" itemLabel="fullLine"/>
					</form:select> 
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="/customer/deliveryAddressCreateModal.do" id="addDeliveryAddressLink" data-backdrop="static" data-remote="false" data-toggle="modal" data-target="#addDeliveryAddressModal">
						<img src="/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label>
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
					id="deliveryContactPhone1" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
				<br><form:errors path="deliveryContactPhone1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Phone2"/></td>
			<td align="${left}">
				<form:input path="deliveryContactPhone2" id="deliveryContactPhone2" cssClass="flat" />
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
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourFrom" cssClass="flat form-control input-sm" style="width:95px !important" path="deliveryHourFrom"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
				&nbsp;
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryMinutesFrom" cssClass="flat form-control input-sm" style="width:55px !important" path="deliveryMinutesFrom"> 
						<form:options items="${deliveryMinutes}" />
					</form:select>
				</label>
				&nbsp;to&nbsp;
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourTo" cssClass="flat form-control input-sm" style="width:95px !important" path="deliveryHourTo"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
				&nbsp;
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryMinutesTo" cssClass="flat form-control input-sm" style="width:55px !important" path="deliveryMinutesTo"> 
						<form:options items="${deliveryMinutes}" />
					</form:select>
				</label>
			 	<br><form:errors path="deliveryHourFrom" cssClass="errorMessage" /><form:errors path="deliveryHourTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Location" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="dumpsterLocationSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterLocation"> 
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${dusmpsterLocationTypes}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<br><form:errors path="dumpsterLocation" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="dumpsterSize" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterSize"> 
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${dumpsters}" itemValue="dumpsterSize" itemLabel="dumpsterSize" />
				</form:select> 
			 	<br><form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Material Type"/></td>
			<td align="${left}">
				<form:select id="materialType" cssClass="flat form-control input-sm" style="width:172px !important" path="materialType"> 
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="type" />
				</form:select> 
				<br><form:errors path="materialType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Permit Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
	    	<td class="form-left"><transys:label code="Permit1 Class"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
				<select class="flat form-control input-sm" id="permitClasses1" name="permitClasses1" style="width:172px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitClasses}" var="aPermitClass">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[0] != null and modelObject.permits[0].permitClass != null}">
							<c:if test="${modelObject.permits[0].permitClass.id == aPermitClass.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitClass.id}" ${selected}>${aPermitClass.permitClass}</option>
					</c:forEach>
				</select>
		 	</td>
		 	<td class="form-left"><transys:label code="Permit2 Class"/></td>
	        <td align="${left}">
				<select class="flat form-control input-sm" id="permitClasses2" name="permitClasses2" style="width:172px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitClasses}" var="aPermitClass">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].permitClass != null}">
							<c:if test="${modelObject.permits[1].permitClass.id == aPermitClass.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitClass.id}" ${selected}>${aPermitClass.permitClass}</option>
					</c:forEach>
				</select>
		 	</td>
		 	<td class="form-left"><transys:label code="Permit3 Class"/></td>
	        <td align="${left}">
				<select class="flat form-control input-sm" id="permitClasses3" name="permitClasses3" style="width:172px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitClasses}" var="aPermitClass">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].permitClass != null}">
							<c:if test="${modelObject.permits[2].permitClass.id == aPermitClass.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitClass.id}" ${selected}>${aPermitClass.permitClass}</option>
					</c:forEach>
				</select>
		 	</td>
	    </tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Type"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
				<select class="flat form-control input-sm" id="permitTypes1" name="permitTypes1" style="width:172px !important" onChange="return populatePermitNumbers(1);">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitTypes}" var="aPermitType">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[0] != null and modelObject.permits[0].permitType != null}">
							<c:if test="${modelObject.permits[0].permitType.id == aPermitType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitType.id}" ${selected}>${aPermitType.permitType}</option>
					</c:forEach>
				</select>
	        </td>
	        <td class="form-left"><transys:label code="Permit2 Type"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
	        	<select class="flat form-control input-sm" id="permitTypes2" name="permitTypes2" style="width:172px !important" onChange="return populatePermitNumbers(2);">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitTypes}" var="aPermitType">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].permitType != null}">
							<c:if test="${modelObject.permits[1].permitType.id == aPermitType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitType.id}" ${selected}>${aPermitType.permitType}</option>
					</c:forEach>
				</select>
	        </td>
	        <td class="form-left"><transys:label code="Permit3 Type"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
	        	<select class="flat form-control input-sm" id="permitTypes2" name="permitTypes2" style="width:172px !important" onChange="return populatePermitNumbers(3);">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitTypes}" var="aPermitType">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].permitType != null}">
							<c:if test="${modelObject.permits[2].permitType.id == aPermitType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${aPermitType.id}" ${selected}>${aPermitType.permitType}</option>
					</c:forEach>
				</select>
	        </td>
	    </tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Number"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
	        	<select class="flat form-control input-sm" id="permits[0]" name="permits[0]" style="width:172px !important" onChange="return populatePermitDateAndFee(1);">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:if test="${modelObject.permits != null and modelObject.permits[0] != null and modelObject.permits[0].number != null}">
						<c:set var="selected" value="selected" />
						<option value="${modelObject.permits[0].id}" ${selected}>${modelObject.permits[0].number}</option>
					</c:if>
				</select>
	        </td>
	        <td class="form-left"><transys:label code="Permit2 Number"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
	        	<select class="flat form-control input-sm" id="permits[1]" name="permits[1]" style="width:172px !important" onChange="return populatePermitDateAndFee(2);">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
						<c:set var="selected" value="selected" />
						<option value="${modelObject.permits[1].id}" ${selected}>${modelObject.permits[1].number}</option>
					</c:if>
				</select>
	        </td>
	        <td class="form-left"><transys:label code="Permit3 Number"/><span class="errorMessage">*</span></td>
	        <td align="${left}">
	        	<select class="flat form-control input-sm" id="permits[2]" name="permits[2]" style="width:172px !important" onChange="return populatePermitDateAndFee(3);">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
						<c:set var="selected" value="selected" />
						<option value="${modelObject.permits[2].id}" ${selected}>${modelObject.permits[2].number}</option>
					</c:if>
				</select>
	        </td>
		</tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Valid From"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidFrom1">${modelObject.permits[0].startDate}</td>
	        <td class="form-left"><transys:label code="Permit2 Valid From"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidFrom2"></td>
	        <td class="form-left"><transys:label code="Permit3 Valid From"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidFrom3"></td>
	    </tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Valid To"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidTo1">${modelObject.permits[0].endDate}</td>
	        <td class="form-left" ><transys:label code="Permit2 Valid To"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidTo2"></td>
	        <td class="form-left"><transys:label code="Permit3 Valid To"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitValidTo3"></td>
	    </tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Fee"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitFee1">${modelObject.permits[0].endDate}</td>
	        <td class="form-left"><transys:label code="Permit2 Fee"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitFee2">${modelObject.permits[1].endDate}</td>
	        <td class="form-left"><transys:label code="Permit3 Fee"/><span class="errorMessage">*</span></td>
	        <td align="${left}" id="permitFee3"></td>
	    </tr>
	    <tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Scale/Weights Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Gross"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="grossWeight" cssClass="flat" />
				<br><form:errors path="grossWeight" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Tare"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="tare" cssClass="flat" />
				<br><form:errors path="tare" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Net Lb"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="netWeightLb" cssClass="flat" />
				<br><form:errors path="netWeightLb" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Net Tonnage"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="netWeightTonnage" cssClass="flat" />
				<br><form:errors path="netWeightTonnage" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Rates/Fees Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Price"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.dumpsterPrice" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.dumpsterPrice" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="City Fee"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.cityFee" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.cityFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Fee"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.permitFees" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.permitFees" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Overweight Fee"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.overweightFee" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.overweightFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Additional Fee"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.additionalFee" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.additionalFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Total Fees"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.totalFees" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.totalFees" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Payment Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Payment Method"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select id="paymentMethod" cssClass="flat form-control input-sm" style="width:172px !important" path="orderPaymentInfo.paymentMethod"> 
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="type" />
				</form:select>
				<br><form:errors path="orderPaymentInfo.paymentMethod" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="CC Refernce #"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.ccReferenceNum" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Check #"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.checkNum" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="orderNotes[0].notes" cssClass="flat" id="notes" style="width:100%; height:150%;"/>
				<br><form:errors path="orderNotes[0].notes" cssClass="errorMessage" />
			</td>
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

<div class="modal fade" id="addDeliveryAddressModal" role="dialog">
	<div class="modal-dialog" style="width:90% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add Delivery Address</h4>
       			<div id="deliveryAddressValidations" style="color:red"></div>
      		 </div>	
			
			<div class="modal-body" id="addDeliveryAddressModalBody"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="addCustomerModal" role="dialog">
	<div class="modal-dialog" style="width:90% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add Customer</h4>
       			<div id="customerValidations" style="color:red"></div>
      		 </div>	
			
			<div class="modal-body" id="addCustomerModalBody"></div>
		</div>
	</div>
</div>
