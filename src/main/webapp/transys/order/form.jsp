<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/modal.jsp"%>

<script type="text/javascript">
function validateAndFormatPhone(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if(phone == "") {
		return;
	}
	
	if(phone.length < 10) {
		var alertMsg = "<p>Invalid Phone Number.</p>";
		showAlertDialog("Data validation", alertMsg);
		
		document.getElementById(phoneId).value = "";
		return true;
	} else {
		var formattedPhone = formatPhone(phone);
		document.getElementById(phoneId).value = formattedPhone;
	}	
}

function formatPhone(phone) {
	if(phone.length < 10) {
		return phone;
	}
	
	var str = new String(phone);
	if(str.match("-")) {
		return phone;
	}
	
	var p1 = str.substring(0,3);
	var p2 = str.substring(3,6);
	var p3 = str.substring(6,10);				
	return p1 + "-" + p2 + "-" + p3;
}

function populateCustomerInfo() {
	var customerSelect =  $('#customerSelect');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateDeliveryAddress();
	retrieveAndPopulateCustomerBillingAddress();
}

function retrieveAndPopulateDeliveryAddress() {
	var customerSelect =  $('#customerSelect');
	var customerId = customerSelect.val();
	$.ajax({
  		url: "customerDeliveryAddress.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	populateDeliveryAddress(addressList);
		}
	});
}

function populateDeliveryAddress(addressList) {
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	deliveryAddressSelect.append(firstOption);
	
	$.each(addressList, function () {
   		$("<option />", {
   	        val: this.id,
   	        text: this.fullLine
   	    }).appendTo(deliveryAddressSelect);
   	});
}

function handleDumpsterSizeChange() {
	populatePermitClass();
	populateMaterialCategories();
}

function populatePermitClass() {
	var dumpsterSizeSelect =  $('#dumpsterSize');
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	if (dumpsterSizeId == "") {
		return false;
	}
	
	var permitClassSelect1 = $("#permitClasses" + 1);
	var permitClassSelect2 = $('#permitClasses' + 2);
	var permitClassSelect3 = $('#permitClasses' + 3);
	
	permitClassSelect1.empty();
	permitClassSelect2.empty();
	permitClassSelect3.empty();
	
	var firstOption1 = $('<option value="">'+ "-----Please Select-----" +'</option>');
	permitClassSelect1.append(firstOption1);
	var firstOption2 = $('<option value="">'+ "-----Please Select-----" +'</option>');
	permitClassSelect2.append(firstOption2);
	var firstOption3 = $('<option value="">'+ "-----Please Select-----" +'</option>');
	permitClassSelect3.append(firstOption3);
	
	$.ajax({
  		url: "retrievePermitClass.do?dumpsterSizeId=" + dumpsterSizeId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var permitClassList = jQuery.parseJSON(responseData);
    	   	
    	   	$.each(permitClassList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.permitClass
    	   	    }).appendTo(permitClassSelect1);
    	   	});
    	   	
    	   	$.each(permitClassList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.permitClass
    	   	    }).appendTo(permitClassSelect2);
    	   	});
    	   	
    	   	$.each(permitClassList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.permitClass
    	   	    }).appendTo(permitClassSelect3);
    	   	});
		}
	});
}

function selectDeliveryAddressOption(value) {
	$('#deliveryAddressSelect').val(value);
}

function retrieveAndPopulateCustomerBillingAddress() {
	var customerId = $('#customerSelect').val();
	$.ajax({
  		url: "customerAddress.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var customer = jQuery.parseJSON(responseData);
    	   	populateCustomerBillingAddress(customer);
		}
	}); 
}

function populateCustomerBillingAddress(customer) {
	$('#billingAddressTd').html(customer.billingAddress);
	$('#billingContactTd').html(customer.contactName);
	$('#billingPhoneTd').html(customer.formattedPhone);
	$('#billingFaxTd').html(customer.formattedFax);
	$('#billingEmailTd').html(customer.email);
}

function appendDeliveryAddress(address) {
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	var newAddressOption = $('<option value=' + address.id + ' selected>'+ address.fullLine +'</option>');
	deliveryAddressSelect.append(newAddressOption);
}

function appendCustomer(customer) {
	var customerSelect = $('#customerSelect');
	var newCustomerOption = $('<option value=' + customer.id + ' selected>'+ customer.companyName +'</option>');
	customerSelect.append(newCustomerOption);
	
	populateCustomerBillingAddress(customer);
	
	populateDeliveryAddress(customer.deliveryAddress);
	selectDeliveryAddressOption(customer.deliveryAddress[0].id);
}

function appendPermit(permit) {
	var permitNumbersSelect = $("#permits\\[" + 0 + "\\]");
	var newPermitOption = $('<option value=' + permit.id + ' selected>'+ permit.number +'</option>');
	permitNumbersSelect.append(newPermitOption);
	
	populatePermitDetails(1, permit);
}

function populateMaterialCategories() {
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	if (dumpsterSizeId == "") {
		return false;
	}
	
	var materialCategorySelect = $("#materialCategory");
	materialCategorySelect.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	materialCategorySelect.append(firstOption);
	
	$.ajax({
  		url: "retrieveMaterialCategories.do?" + "dumpsterSizeId=" + dumpsterSizeId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var materialCategoriesList = jQuery.parseJSON(responseData);
       		$.each(materialCategoriesList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.category
    	   	    }).appendTo(materialCategorySelect);
    	   	});
		}
	});
}

function populateMaterialTypes() {
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	var materialCategorySelect = $("#materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	if (dumpsterSizeId == "" || materialCategoryId == "") {
		return false;
	}
	
	var materialTypeSelect = $("#materialType");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	materialTypeSelect.append(firstOption);
	
	$.ajax({
  		url: "retrieveMaterialTypes.do?" + "dumpsterSizeId=" + dumpsterSizeId + "&materialCategoryId=" + materialCategoryId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var materialTypesList = jQuery.parseJSON(responseData);
       		$.each(materialTypesList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.materialName
    	   	    }).appendTo(materialTypeSelect);
    	   	});
		}
	});
}

function populateDusmpsterPrice() {
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	var materialTypeSelect = $("#materialType");
	var materialTypeId = materialTypeSelect.val();
	
	if (dumpsterSizeId == "" || materialTypeId == "") {
		return false;
	}
	
	var dumpsterPriceInput = $("#orderFees\\.dumpsterPrice");
	
	$.ajax({
  		url: "retrieveDumpsterPrice.do?" + "dumpsterSizeId=" + dumpsterSizeId 
  								  		 + "&materialTypeId=" + materialTypeId,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		dumpsterPriceInput.val(responseData);
		}
	});
}

/*
function populateOverweightFee() {
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	var materialCategorySelect = $("#materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	var netWeightTonnage = $("#netWeightTonnage").val();
	
	if (dumpsterSizeId == "" || materialCategoryId == "" || netWeightTonnage == "") {
		return false;
	}
	
	var overweightFeeInput = $("#orderFees\\.overweightFee");
	
	$.ajax({
  		url: "retrieveOverweightFee.do?" + "dumpsterSizeId=" + dumpsterSizeId 
  								  		 + "&materialCategoryId=" + materialCategoryId
  								  		 + "&netWeightTonnage=" + netWeightTonnage,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		overweightFeeInput.val(responseData);
		}
	});
}*/

function populateCityFee() {
	var cityFeeDescriptionSelect = $("#orderFees\\.cityFeeType");
	var cityFeeId = cityFeeDescriptionSelect.val();
	
	if (cityFeeDescriptionSelect == "") {
		return false;
	}
	
	var cityFeeInput = $("#orderFees\\.cityFee");
	
	$.ajax({
  		url: "retrieveCityFee.do?" + "cityFeeId=" + cityFeeId ,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		cityFeeInput.val(responseData);
		}
	});
}

function populatePermitNumbers(index) {
	var customerSelect = $('#customerSelect');
	var customerId = customerSelect.val();
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	var deliveryAddressId = deliveryAddressSelect.val();
	
	var permitClassSelect = $("#permitClasses" + index);
	var permitClassId = permitClassSelect.val();
	
	var permitTypeSelect = $("#permitTypes" + index);
	var permitTypeId = permitTypeSelect.val();
	
	var deliveryDate = $("[name='deliveryDate']").val();
	
	if (permitClassId == "" || permitTypeId == "" || deliveryDate == "") {
		return false;
	}
	
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	permitNumbersSelect.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	permitNumbersSelect.append(firstOption);
	
	$.ajax({
  		url: "retrievePermit.do?" + "customerId=" + customerId
  								  + "&deliveryAddressId=" + deliveryAddressId
  								  + "&permitClassId=" + permitClassId
  								  + "&permitTypeId=" + permitTypeId
  								  + "&deliveryDate=" + deliveryDate,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var permitList = jQuery.parseJSON(responseData);
			if (jQuery.isEmptyObject(permitList)) {
				var alertMsg = "<p>No permits available for seleted criteria.</p>";
				showAlertDialog("No permits", alertMsg);
				
				return false;
    	   	}
    	   	
    	   	$.each(permitList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.number
    	   	    }).appendTo(permitNumbersSelect);
    	   	});
		}
	}); 
}

function retrievePermitDetails(index) {
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	var permitId = permitNumbersSelect.val();
	
	$.ajax({
  		url: "retrievePermit.do?" + "permitId=" + permitId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var permitList = jQuery.parseJSON(responseData);
    	   	var permit = permitList[0];
    	   	
    	   	populatePermitDetails(index, permit);
		}
	}); 
}

function populatePermitDetails(index, permit) {
	var permitValidFrom = $("#permitValidFrom" + index);
	var permitValidTo = $("#permitValidTo" + index);
	var permitFee = $("#orderFees\\.permitFee" + index);
	
	var permitAddressSelect = $("#permitAddress" + index);
	
	permitValidFrom.html(permit.formattedStartDate);
   	permitValidTo.html(permit.endDate);
   	
   	/*var permitFeeVal = "0.00"
	if (permit.status.status == 'Pending') {
		permitFeeVal = permit.fee;
   	}*/
   	permitFee.val(permit.fee);
   	
	permitAddressSelect.empty();
   	var permitAddressList = permit.permitAddress;
   	$.each(permitAddressList, function () {
   	    $("<option />", {
   	        val: this.id,
   	        text: this.fullLine
   	    }).appendTo(permitAddressSelect);
   	});

   	populateTotalPermitFees();
}

function populateTotalPermitFees() {
	var totalPermitFees = parseFloat(0.00);
	
	var permitFee1 = $("#orderFees\\.permitFee" + 1).val();
	if (permitFee1 != "") {
		totalPermitFees += parseFloat(permitFee1);
	}
	var permitFee2 = $("#orderFees\\.permitFee" + 2).val();
	if (permitFee2 != "") {
		totalPermitFees += parseFloat(permitFee2);
	}
	var permitFee3 = $("#orderFees\\.permitFee" + 3).val();
	if (permitFee3 != "") {
		totalPermitFees += parseFloat(permitFee3);
	}
	
	$("#orderFees\\.totalPermitFees").val(totalPermitFees);
}

function populateTotalAdditionalFees() {
	var totalAdditionalFees = parseFloat(0.00);
	
	var additionalFee1 = $("#orderFees\\.additionalFee" + 1).val();
	if (additionalFee1 != "") {
		totalAdditionalFees += parseFloat(additionalFee1);
	}
	
	var additionalFee2 = $("#orderFees\\.additionalFee" + 2).val();
	if (additionalFee2 != "") {
		totalAdditionalFees += parseFloat(additionalFee2);
	}
	
	var additionalFee3 = $("#orderFees\\.additionalFee" + 3).val();
	if (additionalFee3 != "") {
		totalAdditionalFees += parseFloat(additionalFee3);
	}
	
	$("#orderFees\\.totalAdditionalFees").val(totalAdditionalFees);
}

function populateTotalFees() {
	var dumpsterPrice = $("#orderFees\\.dumpsterPrice").val();
	var overweightFee = $("#orderFees\\.overweightFee").val();
	var cityFee = $("#orderFees\\.cityFee").val();
	var totalPermitFees = $("#orderFees\\.totalPermitFees").val();
	var totalAdditionalFees = $("#orderFees\\.totalAdditionalFees").val();
	var discountPercentage = $("#orderFees\\.discountPercentage").val();
	
	var totalFees = parseFloat(dumpsterPrice);
	if (overweightFee != "") {
		totalFees += parseFloat(overweightFee);
	}
	if (cityFee != "") {
		totalFees += parseFloat(cityFee);
	} 
	if (totalPermitFees != "") {
		totalFees += parseFloat(totalPermitFees);
	}
	if (totalAdditionalFees != "") {
		totalFees += parseFloat(totalAdditionalFees);
	}
	var discountAmount = parseFloat(0.00);
	if (discountPercentage != "") {
		discountAmount = ((totalFees * parseFloat(discountPercentage))/parseFloat(100.00));
	}
	
	$("#orderFees\\.discountAmount").val(discountAmount);
	$("#orderFees\\.totalFees").val(totalFees - discountAmount);
}

function validateForm() {
	return true;
}

function processForm() {
	if (validateForm()) {
		verifyExchangeOrderAndSubmit();
	}
}

function verifyExchangeOrderAndSubmit() {
	var isExchangeIndicator = $('#isExchange');
	isExchangeIndicator.val("false");
	
	var orderAddEditForm = $("#orderAddEditForm");
	var id = orderAddEditForm.find("#id");
	if (id.val() != "") {
		orderAddEditForm.submit();
		return false;
	}
	
	var selectedCustomerId = $('#customerSelect').val();
	var selectedDeliveryAddressId = $('#deliveryAddressSelect').val();
	
    $.ajax({
        type: "GET",
        url: "retrieveMatchingOrder.do" + "?customerId=" + selectedCustomerId + "&deliveryAddressId=" + selectedDeliveryAddressId,
        success: function(responseData, textStatus, jqXHR) {
        	var existingDroppedOffOrderId = responseData;
        	if (existingDroppedOffOrderId != "") {
        		$('#existingDroppedOffOrderId').val(existingDroppedOffOrderId);
        		
        		var exchMsg = "<p>There is already a Dumpster delivered to this address with the Order# "
      			  		   	  + existingDroppedOffOrderId
    			  		   	  + " and can be picked up as an Exchange Order.<br><br>"
    			  		   	  + "Would you like to create an Exchange Order?</p>";
    			  	
        		showConfirmDialog("Confirm Exchange Order", exchMsg);
        	} else {
        		isExchangeIndicator.val("false");
        		$('#existingDroppedOffOrderId').val("");
        		orderAddEditForm.submit();
        	}
        }
    });
    
    return false;
}
</script>
<form:form action="save.do" name="orderAddEditForm" commandName="modelObject" method="post" id="orderAddEditForm">
	<form:hidden path="id" id="id" />
	<input type="hidden" name="isExchange" id="isExchange" value="false" />
	<input type="hidden" name="existingDroppedOffOrderId" id="existingDroppedOffOrderId" value="" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageOrder" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td></td></tr>
		<tr>
			<td class="form-left">Order #<span class="errorMessage">*</span></td>
			<td class="wide td-static">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer" /><span class="errorMessage"></span></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="customerSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="customer" onChange="return populateCustomerInfo();"> 
						<form:option value="">-----Please Select-----</form:option>
						<form:options items="${customers}" itemValue="id" itemLabel="companyName" />
					</form:select>
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="/customer/createModal.do" id="addCustomerLink">
						<img src="/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label> 
				<form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Address" /><span class="errorMessage"></span></td>
			<td class="td-static" id="billingAddressTd">${modelObject.customer.getBillingAddress()}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Contact" /><span class="errorMessage"></span></td>
			<td class="td-static" id="billingContactTd">${modelObject.customer.contactName}</td>
			<td class="form-left"><transys:label code="Fax"/></td>
			<td class="td-static" id="billingFaxTd">${modelObject.customer.getFormattedFax()}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone" /><span class="errorMessage"></span></td>
			<td class="td-static" id="billingPhoneTd">${modelObject.customer.getFormattedPhone()}</td>
			<td class="form-left"><transys:label code="Email"/></td>
			<td class="td-static" id="billingEmailTd">${modelObject.customer.email}</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Delivery Information</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td class="form-left">Delivery Address<span class="errorMessage"></span></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width:172px !important">
						<form:option value="">-----Please Select-----</form:option>
						<form:options items="${deliveryAddresses}" itemValue="id" itemLabel="fullLine"/>
					</form:select> 
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="/customer/deliveryAddressCreateModal.do" id="addDeliveryAddressLink" >
						<img src="/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label>
				<form:errors path="deliveryAddress" cssClass="errorMessage" />
			</td>
			<td class="form-left"><span style="color:blue">Pickup Order #</span></td>
			<td>
				<span style="color:blue;">${modelObject.pickupOrderId}</span>
			</td>
		</tr>
		<tr>
			<td class="form-left">Contact Name<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryContactName" cssClass="flat" style="width:172px !important" />
			 	<br><form:errors path="deliveryContactName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone1"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryContactPhone1" cssClass="flat" style="width:172px !important" maxlength="12" 
					id="deliveryContactPhone1" onkeypress="return onlyNumbers(event, false)" onblur="return validateAndFormatPhone('deliveryContactPhone1');"/>
				<br><form:errors path="deliveryContactPhone1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Phone2"/></td>
			<td>
				<form:input path="deliveryContactPhone2" id="deliveryContactPhone2" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="deliveryContactPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Date"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryDate" cssClass="flat" style="width:172px !important" id="datepicker7" name="deliveryDate"/>
				 <br><form:errors path="deliveryDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Delivery Time"/></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourFrom" cssClass="flat form-control input-sm" style="width:99px !important" path="deliveryHourFrom"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
				&nbsp;to&nbsp;
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourTo" cssClass="flat form-control input-sm" style="width:99px !important" path="deliveryHourTo"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
			 	<br><form:errors path="deliveryHourFrom" cssClass="errorMessage" /><form:errors path="deliveryHourTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Location" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterLocationSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterLocation"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${dusmpsterLocationTypes}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<form:errors path="dumpsterLocation" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Dumpster Size"/><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterSize" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterSize" onchange="return handleDumpsterSizeChange(); "> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id" itemLabel="size" />
				</form:select> 
			 	<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<select class="flat form-control form-control-ext input-sm" id="materialCategory" name="materialCategory" id="materialCategory" style="width: 172px !important" onChange="return populateMaterialTypes();">
					<option value="">-----Please Select-----</option>
					<c:forEach items="${materialCategories}" var="aMaterialCategory">
						<c:set var="selected" value="" />
						<c:if test="${modelObject.materialType.materialCategory.id == aMaterialCategory.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterialCategory.id}" ${selected}>${aMaterialCategory.category}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left"><transys:label code="Material Type"/></td>
			<td>
				<form:select id="materialType" cssClass="flat form-control input-sm" style="width:172px !important" path="materialType"  onChange="return populateDusmpsterPrice();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="materialName" />
				</form:select> 
				<form:errors path="materialType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td></td></tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Permit Information</td>
		</tr>
		<tr>
			<td></td>
		</tr>
	</table>
	<table id="form-table" class="table">
		<tr>
	    	<td class="form-left"><transys:label code="Permit1 Class"/><span class="errorMessage">*</span></td>
	        <td>
				<select class="flat form-control input-sm" id="permitClasses1" name="permitClasses1" style="width:172px !important">
					<option value="">-----Please Select-----</option>
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
	        <td>
				<select class="flat form-control input-sm" id="permitClasses2" name="permitClasses2" style="width:172px !important">
					<option value="">-----Please Select-----</option>
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
	        <td>
				<select class="flat form-control input-sm" id="permitClasses3" name="permitClasses3" style="width:172px !important">
					<option value="">-----Please Select-----</option>
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
	        <td>
				<select class="flat form-control input-sm" id="permitTypes1" name="permitTypes1" style="width:172px !important" onChange="return populatePermitNumbers(1);">
					<option value="">-----Please Select-----</option>
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
	        <td>
	        	<select class="flat form-control input-sm" id="permitTypes2" name="permitTypes2" style="width:172px !important" onChange="return populatePermitNumbers(2);">
					<option value="">-----Please Select-----</option>
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
	        <td>
	        	<select class="flat form-control input-sm" id="permitTypes3" name="permitTypes3" style="width:172px !important" onChange="return populatePermitNumbers(3);">
					<option value="">-----Please Select-----</option>
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
	        <td>
	        	<label style="display: inline-block; font-weight: normal">
		        	<select class="flat form-control input-sm" id="permits[0]" name="permits[0]" style="width:172px !important" onChange="return retrievePermitDetails(1);">
						<option value="">-----Please Select-----</option>
						<c:if test="${modelObject.permits != null and modelObject.permits[0] != null and modelObject.permits[0].number != null}">
							<c:set var="chosenPermit" value="${modelObject.permits[0]}" />
							<c:set var="allPermitsOfChosenType" value="${allPermitsOfChosenTypesList[0]}" />
							<c:forEach items="${allPermitsOfChosenType}" var="aPermitOfChosenType">
								<c:set var="selected" value="" />
								<c:if test="${aPermitOfChosenType.id == chosenPermit.id}">
									<c:set var="selected" value="selected" />
								</c:if>
								<option value="${aPermitOfChosenType.id}" ${selected}>${aPermitOfChosenType.number}</option>
							</c:forEach>
						</c:if>
					</select>
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="/permit/createForCustomerModal.do" id="addPermitLink" >
						<img src="/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label>
	        </td>
	        <td class="form-left"><transys:label code="Permit2 Number"/><span class="errorMessage">*</span></td>
	        <td>
	        	<select class="flat form-control input-sm" id="permits[1]" name="permits[1]" style="width:172px !important" onChange="return retrievePermitDetails(2);">
					<option value="">-----Please Select-----</option>
					<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
						<c:set var="chosenPermit" value="${modelObject.permits[1]}" />
						<c:set var="allPermitsOfChosenType" value="${allPermitsOfChosenTypesList[1]}" />
						<c:forEach items="${allPermitsOfChosenType}" var="aPermitOfChosenType">
							<c:set var="selected" value="" />
							<c:if test="${aPermitOfChosenType.id == chosenPermit.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitOfChosenType.id}" ${selected}>${aPermitOfChosenType.number}</option>
						</c:forEach>
					</c:if>
				</select>
	        </td>
	        <td class="form-left"><transys:label code="Permit3 Number"/><span class="errorMessage">*</span></td>
	        <td>
	        	<select class="flat form-control input-sm" id="permits[2]" name="permits[2]" style="width:172px !important" onChange="return retrievePermitDetails(3);">
					<option value="">-----Please Select-----</option>
					<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
						<c:set var="chosenPermit" value="${modelObject.permits[2]}" />
						<c:set var="allPermitsOfChosenType" value="${allPermitsOfChosenTypesList[2]}" />
						<c:forEach items="${allPermitsOfChosenType}" var="aPermitOfChosenType">
							<c:set var="selected" value="" />
							<c:if test="${aPermitOfChosenType.id == chosenPermit.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitOfChosenType.id}" ${selected}>${aPermitOfChosenType.number}</option>
						</c:forEach>
					</c:if>
				</select>
	        </td>
		</tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Valid From"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidFrom1">${modelObject.permits[0].formattedStartDate}</td>
	        <td class="form-left"><transys:label code="Permit2 Valid From"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidFrom2">${modelObject.permits[1].formattedStartDate}</td>
	        <td class="form-left"><transys:label code="Permit3 Valid From"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidFrom3">${modelObject.permits[2].formattedStartDate}</td>
	    </tr>
	    <tr>
	    	<td class="form-left"><transys:label code="Permit1 Valid To"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidTo1">${modelObject.permits[0].formattedEndDate}</td>
	        <td class="form-left" ><transys:label code="Permit2 Valid To"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidTo2">${modelObject.permits[1].formattedEndDate}</td>
	        <td class="form-left"><transys:label code="Permit3 Valid To"/><span class="errorMessage">*</span></td>
	        <td class="td-static" id="permitValidTo3">${modelObject.permits[2].formattedEndDate}</td>
	    </tr>
	    <tr>
	      <td class="form-left">Permit1 Address</td>
	      <td>
        	<select class="flat form-control input-sm" id="permitAddress1" name="permitAddress1" style="width:172px !important">
				<c:if test="${modelObject.permits != null and modelObject.permits[0] != null and modelObject.permits[0].number != null}">
					<c:forEach items="${modelObject.permits[0].permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.id}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>
				</c:if>
			</select>
	      </td>
	      <td class="form-left">Permit2 Address</td>
	      <td>
        	<select class="flat form-control input-sm" id="permitAddress2" name="permitAddress2" style="width:172px !important">
				<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
					<c:forEach items="${modelObject.permits[1].permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.id}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>
				</c:if>
			</select>
	      </td>
	      <td class="form-left">Permit3 Address</td>
	      <td>
        	<select class="flat form-control input-sm" id="permitAddress3" name="permitAddress3" style="width:172px !important">
				<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
					<c:forEach items="${modelObject.permits[2].permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.id}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>
				</c:if>
			</select>
	      </td>
		</tr>
	    <tr>
	    	<td class="form-left">Permit1 Fee<span class="errorMessage">*</span></td>
	        <td class="td-static"><form:input path="orderFees.permitFee1" style="width:172px !important" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
	        <td class="form-left">Permit2 Fee<span class="errorMessage">*</span></td>
	        <td class="td-static"><form:input path="orderFees.permitFee2" style="width:172px !important" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
	        <td class="form-left">Permit3 Fee<span class="errorMessage">*</span></td>
	        <td class="td-static"><form:input path="orderFees.permitFee3" style="width:172px !important" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
	    </tr>
	</table>
	<table id="form-table" class="table">
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Rates/Fees Information</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td class="form-left">Total Permit Fees</td>
			<td class="wide">
				<form:input path="orderFees.totalPermitFees" cssClass="form-control" readonly="true" style="width:172px;height:24px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Price"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.dumpsterPrice" cssClass="form-control" readonly="true" style="width:172px;height:24px !important" />
				<form:errors path="orderFees.dumpsterPrice" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Overweight Fee"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.overweightFee" cssClass="form-control" readonly="true" style="width:172px;height:24px !important" />
				<form:errors path="orderFees.overweightFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City Fee"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.cityFee" cssClass="form-control" readonly="true" style="width:172px;height:24px !important" />
				<form:errors path="orderFees.cityFee" cssClass="errorMessage" />
			</td>
			<td class="form-left">Description<span class="errorMessage">*</span></td>
			<td>
				<form:select id="orderFees.cityFeeType" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.cityFeeType" onChange="return populateCityFee();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${cityFeeDetails}" itemValue="id" itemLabel="suburbName" />
				</form:select>
				<form:errors path="orderFees.cityFeeType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Additional Fee1"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.additionalFee1" style="width:172px !important" cssClass="flat" onchange="return populateTotalAdditionalFees();"/>
				<form:errors path="orderFees.additionalFee1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Description"/><span class="errorMessage">*</span></td>
				<td>
				<form:select id="additionalFee1Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee1Type"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee1Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Additional Fee2"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.additionalFee2" style="width:172px !important" cssClass="flat" />
				<form:errors path="orderFees.additionalFee2" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Description"/><span class="errorMessage">*</span></td>
			<td>
				<form:select id="additionalFee2Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee2Type"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee2Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Additional Fee3"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.additionalFee3" style="width:172px !important" cssClass="flat" />
				<form:errors path="orderFees.additionalFee3" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Description"/><span class="errorMessage">*</span></td>
				<td>
				<form:select id="additionalFee3Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee3Type"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee3Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left form-left-ext"><transys:label code="Total Additional Fees"/></td>
			<td>
				<form:input path="orderFees.totalAdditionalFees" cssClass="form-control" readonly="true" style="width:172px;height:24px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Discount %"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.discountPercentage" style="width:172px !important" cssClass="flat" onchange="return populateTotalFees();"/>
				<br><form:errors path="orderFees.discountPercentage" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Discount Amount"/></td>
			<td>
				<form:input path="orderFees.discountAmount" cssClass="form-control" readonly="true" style="width:172px;height:24px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Total Fees"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.totalFees" cssClass="form-control" readonly="true" style="width:172px;height:24px !important"/>
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Payment Information</td>
		</tr>
		<tr><td></td></tr>
	</table>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Payment Method<span class="errorMessage">*</span></td>
			<td class="form-left">Payment Date</td>
			<td class="form-left">Amount<span class="errorMessage">*</span></td>
			<td class="form-left">CC Reference #<span class="errorMessage">*</span></td>
			<td class="form-left">Check #<span class="errorMessage">*</span></td>
		</tr>
		<tr>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="orderPayment[0].paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="orderPayment[0].paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static wide">
				<c:if test="${modelObject.orderPayment[0] != null and modelObject.orderPayment[0].id != null}">
					${modelObject.orderPayment[0].formattedCreatedAt}
				</c:if>
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].amountPaid" cssClass="flat" />
				<br><form:errors path="orderPayment[0].amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].ccReferenceNum" cssClass="flat" />
				<br><form:errors path="orderPayment[0].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].checkNum" cssClass="flat" />
				<br><form:errors path="orderPayment[0].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="orderPayment[1].paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="orderPayment[1].paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static">
				<c:if test="${modelObject.orderPayment[1] != null and modelObject.orderPayment[1].id != null}">
					${modelObject.orderPayment[1].formattedCreatedAt}
				</c:if>
			</td>
			<td>
				<form:input path="orderPayment[1].amountPaid" cssClass="flat" />
				<br><form:errors path="orderPayment[1].amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[1].ccReferenceNum" cssClass="flat" />
				<br><form:errors path="orderPayment[1].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="orderPayment[1].checkNum" cssClass="flat" />
				<br><form:errors path="orderPayment[1].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="orderPayment[2].paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="orderPayment[2].paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static">
				<c:if test="${modelObject.orderPayment[2] != null and modelObject.orderPayment[2].id != null}">
					${modelObject.orderPayment[2].formattedCreatedAt}
				</c:if>
			</td>
			<td class="wide">
				<form:input path="orderPayment[2].amountPaid" cssClass="flat" />
				<br><form:errors path="orderPayment[2].amountPaid" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="orderPayment[2].ccReferenceNum" cssClass="flat" />
				<br><form:errors path="orderPayment[2].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[2].checkNum" cssClass="flat" />
				<br><form:errors path="orderPayment[2].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Total Amount Paid</td>
			<td class="td-static" style="font-weight: bold">${modelObject.totalAmountPaid}</td>
			<td class="form-left">Balance Due</td>
			<c:set var="balanceDueAlertClass" value="" />
			<c:if test="${modelObject.balanceAmountDue > 0}">
				<c:set var="balanceDueAlertClass" value="errorMessage" />
			</c:if>
			<td class="td-static"><span class="${balanceDueAlertClass}">${modelObject.balanceAmountDue}</span></td>
		</tr>
	</table>
	<table id="form-table" class="table">
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
				<c:set var="orderNotesDisabled" value="" />
				<c:if test="${modelObject.orderNotes != null and modelObject.orderNotes.size() > 0 
							and modelObject.orderNotes[0].notes != null and modelObject.orderNotes[0].notes.length() > 0}">
					<c:set var="orderNotesDisabled" value="true" />
				</c:if>
				<form:textarea readonly="${orderNotesDisabled}" row="5" path="orderNotes[0].notes" cssClass="form-control" style="width:100%; height:100%;"/>
				<form:errors path="orderNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="orderCreate" onclick="processForm();" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="orderCancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
		<tr><td></td></tr>
		<tr><td></td></tr>
	</table>
</form:form>

<script type="text/javascript">
$("#confirmDialogYes").click(function (ev) {
	var isExchangeIndicator = $('#isExchange');
	isExchangeIndicator.val("true");
	
	var orderAddEditForm = $("#orderAddEditForm");
	orderAddEditForm.submit();
});

$("#addCustomerLink").click(function (ev) {
	var url = $(this).attr("href");
	showPopupDialog("Add Customer", url);
	
	ev.preventDefault();
});

$("#addDeliveryAddressLink").click(function (ev) {
	var customerId = $('#customerSelect').val();
	
	var url = $(this).attr("href");
	url += "?customerId=" + customerId;
	
	showPopupDialog("Add Delivery Address", url);
	
	ev.preventDefault();
});

$("#addPermitLink").click(function (ev) {
	var customerId = $('#customerSelect').val();
	var deliveryAddressId = $('#deliveryAddressSelect').val();
	var locationTypeId = $('#dumpsterLocationSelect').val();
	
	var permitClassSelect = $("#permitClasses" + 1);
	var permitClassId = permitClassSelect.val();
	
	var permitTypeSelect = $("#permitTypes" + 1);
	var permitTypeId = permitTypeSelect.val();
	
	var deliveryDate = $("[name='deliveryDate']").val();
	
	if (customerId == "" || deliveryAddressId == "" || locationTypeId == "" 
			|| permitClassId == "" || permitTypeId == "" || deliveryDate == "") {
		return false;
	}
	
	var orderId = $('#id').val();
	
	$.ajax({
  		url: "/permit/validatePermitCanBeAdded.do?orderId=" + orderId + "&deliveryAddressId=" + deliveryAddressId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var validationErrorMsg = jQuery.parseJSON(responseData);
    	   	if (validationErrorMsg != "") {
    	   		showAlertDialog("Order Permit Data Validation", validationErrorMsg);
    	   		return false;
    	   	}
    	   	
    	   	var url = $(this).attr("href");
    		url += "?customerId=" + customerId 
    			+  "&deliveryAddressId=" + deliveryAddressId
    			+  "&locationTypeId=" + locationTypeId
    			+  "&permitClassId=" + permitClassId
    			+  "&permitTypeId=" + permitTypeId
    			+  "&deliveryDate=" + deliveryDate;
    		
    		showPopupDialog("Add Permit", url);
		}
	});
	
	ev.preventDefault();
});
</script>


