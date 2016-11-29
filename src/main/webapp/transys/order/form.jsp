<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function populateCustomerInfo() {
	emptyCustomerBillingAddress();
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	emptySelect(deliveryAddressSelect);
	
	resetPermit(1);
	
	var customerSelect =  $('#customerSelect');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateDeliveryAddress();
	retrieveAndPopulateCustomerBillingAddress();
}

function emptySelect(selectElem) {
	selectElem.empty();
	
	var firstOption = $('<option value="">'+ "-----Please Select-----" +'</option>');
	selectElem.append(firstOption);
}

function retrieveAndPopulateDeliveryAddress() {
	var customerSelect = $('#customerSelect');
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
	emptySelect(deliveryAddressSelect);
	
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
	var permitClassSelect1 = $("#permitClasses" + 1);
	var permitClassSelect2 = $('#permitClasses' + 2);
	var permitClassSelect3 = $('#permitClasses' + 3);
	
	emptySelect(permitClassSelect1);
	emptySelect(permitClassSelect2);
	emptySelect(permitClassSelect3);
	
	var dumpsterSizeSelect =  $('#dumpsterSize');
	var dumpsterSizeId = dumpsterSizeSelect.val();
	if (dumpsterSizeId == "") {
		return false;
	}
	
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
	emptyCustomerBillingAddress();
	
	$('#billingAddressTd').html(customer.billingAddress);
	$('#billingContactTd').html(customer.contactName);
	$('#billingPhoneTd').html(customer.formattedPhone);
	$('#billingFaxTd').html(customer.formattedFax);
	$('#billingEmailTd').html(customer.email);
}

function emptyCustomerBillingAddress() {
	$('#billingAddressTd').html("");
	$('#billingContactTd').html("");
	$('#billingPhoneTd').html("");
	$('#billingFaxTd').html("");
	$('#billingEmailTd').html("");
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
	var materialCategorySelect = $("#materialCategory");
	emptySelect(materialCategorySelect);
	
	var materialTypeSelect = $("#materialType");
	emptySelect(materialTypeSelect);
	
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	if (dumpsterSizeId == "") {
		return false;
	}
	
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
	var materialTypeSelect = $("#materialType");
	emptySelect(materialTypeSelect);
	
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	var materialCategorySelect = $("#materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	if (dumpsterSizeId == "" || materialCategoryId == "") {
		return false;
	}
	
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

function populateDumpsterPrice() {
	var dumpsterPriceInput = $("#orderFees\\.dumpsterPrice");
	dumpsterPriceInput.val("");
	
	populateTotalFees();
	
	var dumpsterSizeSelect = $("#dumpsterSize");
	var dumpsterSizeId = dumpsterSizeSelect.val();
	
	var materialTypeSelect = $("#materialType");
	var materialTypeId = materialTypeSelect.val();
	
	var customerSelect = $('#customerSelect');
	var customerId = customerSelect.val();
	
	if (dumpsterSizeId == "" || materialTypeId == "" || customerId == "") {
		return false;
	}
	
	$.ajax({
  		url: "retrieveDumpsterPrice.do?" + "dumpsterSizeId=" + dumpsterSizeId 
  								  		 + "&materialTypeId=" + materialTypeId
  								  		 + "&customerId=" + customerId,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		dumpsterPriceInput.val(responseData);
       		
       		populateTotalFees();
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
  		url: "calculateOverweightFee.do?" + "dumpsterSizeId=" + dumpsterSizeId 
  								  		 + "&materialCategoryId=" + materialCategoryId
  								  		 + "&netWeightTonnage=" + netWeightTonnage,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		overweightFeeInput.val(responseData);
		}
	});
}*/

function populateCityFee() {
	var cityFeeInput = $("#orderFees\\.cityFee");
	cityFeeInput.val("");
	
	populateTotalFees();
	
	var cityFeeDescriptionSelect = $("#orderFees\\.cityFeeType");
	var cityFeeId = cityFeeDescriptionSelect.val();
	if (cityFeeId == "") {
		return false;
	}
	
	$.ajax({
  		url: "retrieveCityFee.do?" + "cityFeeId=" + cityFeeId ,
  								  
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		cityFeeInput.val(responseData);
       		
       		populateTotalFees();
		}
	});
}

function handleDeliveryAddressChange() {
	resetPermit(1);
}

function handleDeliveryDateChange() {
	resetPermit(1);
}

function handleDumpsterLocationChange() {
	resetPermit(1);
}

function handlePermitClassChange(index) {
	resetPermit(index);
}

function resetPermit(index) {
	var permitTypeSelect = $("#permitTypes" + index);
	permitTypeSelect.val("");
	
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	emptySelect(permitNumbersSelect);
}

function populatePermitNumbers(index) {
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	emptySelect(permitNumbersSelect);
	
	var customerSelect = $('#customerSelect');
	var customerId = customerSelect.val();
	
	var deliveryAddressSelect = $('#deliveryAddressSelect');
	var deliveryAddressId = deliveryAddressSelect.val();
	
	var permitClassSelect = $("#permitClasses" + index);
	var permitClassId = permitClassSelect.val();
	
	var permitTypeSelect = $("#permitTypes" + index);
	var permitTypeId = permitTypeSelect.val();
	
	var locationTypeSelect = $('#dumpsterLocationSelect');
	var locationTypeId = locationTypeSelect.val();
	
	var deliveryDate = $("[name='deliveryDate']").val();
	
	if (customerId == "" || deliveryAddressId == "" || permitClassId == "" 
			|| permitTypeId == "" || deliveryDate == "" || locationTypeId == "") {
		var alertMsg = "Please select Customer, Delivery address, Delivery date, Location, Permit class and Type for retrieving permits."
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	$.ajax({
  		url: "retrievePermit.do?" + "customerId=" + customerId
  								  + "&deliveryAddressId=" + deliveryAddressId
  								  + "&permitClassId=" + permitClassId
  								  + "&permitTypeId=" + permitTypeId
  								  + "&deliveryDate=" + deliveryDate
  								  + "&locationTypeId=" + locationTypeId,
  								  
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
	emptyPermitDetails(index);
	
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	var permitId = permitNumbersSelect.val();
	
	if (permitId == "") {
		return false;
	}
	
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

function emptyPermitDetails(index) {
	var permitValidFrom = $("#permitValidFrom" + index);
	var permitValidTo = $("#permitValidTo" + index);
	var permitFee = $("#orderFees\\.permitFee" + index);
	
	var permitAddressSelect = $("#permitAddress" + index);
	
	permitValidFrom.html("");
	permitValidTo.html("");
	permitFee.val("");
	emptySelect(permitAddressSelect);
	
	populateTotalPermitFees();
}

function populateTotalPermitFees() {
	var totalPermitFees = parseFloat(0.00);
	
	var permitFee1 = $("#orderFees\\.permitFee" + 1).val();
	if (permitFee1 != "") {
		totalPermitFees += parseFloat(permitFee1);
	}
	var permitFee2Elem = $("#orderFees\\.permitFee" + 2);
	if (permitFee2Elem.length) {
		var permitFee2 = permitFee2Elem.val();
		if (permitFee2 != "") {
			totalPermitFees += parseFloat(permitFee2);
		}
	}
	var permitFee3Elem = $("#orderFees\\.permitFee" + 3);
	if (permitFee3Elem.length) {
		var permitFee3 = permitFee3Elem.val();
		if (permitFee3 != "") {
			totalPermitFees += parseFloat(permitFee3);
		}
	}
	
	$("#orderFees\\.totalPermitFees").val(totalPermitFees);
	
	populateTotalFees();
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
	
	$("#orderFees\\.totalAdditionalFees").val(totalAdditionalFees.toFixed(2));
	
	populateTotalFees();
}

function populateTotalFees() {
	var dumpsterPrice = $("#orderFees\\.dumpsterPrice").val();
	var tonnageFee = $("#orderFees\\.tonnageFee").val();
	var overweightFee = $("#orderFees\\.overweightFee").val();
	var cityFee = $("#orderFees\\.cityFee").val();
	var totalPermitFees = $("#orderFees\\.totalPermitFees").val();
	var totalAdditionalFees = $("#orderFees\\.totalAdditionalFees").val();
	var discountAmount = $("#orderFees\\.discountAmount").val();
	
	var totalFees = parseFloat("0.00");
	
	if (dumpsterPrice != "") {
		totalFees += parseFloat(dumpsterPrice);
	}
	if (tonnageFee != "") {
		totalFees += parseFloat(tonnageFee);
	}
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
	var discountAmountFloat = parseFloat(0.00);
	if (discountAmount != "") {
		discountAmountFloat = parseFloat(discountAmount);
	} 
	
	var finalTotalFees = (totalFees - discountAmountFloat).toFixed(2);
	$("#orderFees\\.totalFees").val(finalTotalFees);
	
	var totalPaid = $('#totalPaid').html();
	if (totalPaid != "") {
		var totalPaidFloat = parseFloat(totalPaid);
		var balanceDue = finalTotalFees - totalPaidFloat;
		$('#balanceDue').html(balanceDue.toFixed(2)); 
	}
}

function validateOrderForm() {
	var missingData = validateOrderMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateOrderDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateOrderMissingData() {
	var missingData = "";
	
	if ($('#customerSelect').val() == "") {
		missingData += "Customer, "
	}
	if ($('#deliveryAddressSelect').val() == "") {
		missingData += "Delivery Address, "
	}
	if ($('#deliveryContactName').val() == "") {
		missingData += "Contact Name, "
	}
	if ($('#deliveryContactPhone1').val() == "") {
		missingData += "Phone1, "
	}
	if ($("[name='deliveryDate']").val() == "") {
		missingData += "Delivery Date, "
	}
	if ($('#deliveryHourFrom').val() == "--") {
		missingData += "Delivery Hour From, "
	}
	if ($('#deliveryHourTo').val() == "--") {
		missingData += "Delivery Hour To, "
	}
	if ($('#dumpsterLocationSelect').val() == "") {
		missingData += "Dumpster Location, "
	}
	if ($('#dumpsterSize').val() == "") {
		missingData += "Dumpster Size, "
	}
	if ($('#materialCategory').val() == "") {
		missingData += "Material Category, "
	}
	if ($('#materialType').val() == "") {
		missingData += "Material Type, "
	}
	
	/*if (!validateMissingPermit()) {
		missingData += "Permit, "
	}*/
	
	if ($('#orderFees\\.dumpsterPrice').val() == "") {
		missingData += "Dumpster Price, "
	}
	
	for (i = 1; i < 4; i++) {
		missingData += validateMissingOrderPayment(i)
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateOrderDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateAllOrderText();
	validationMsg += validateOrderFees(); 
	validationMsg += validateAllOrderPhones();
	validationMsg += validateAllOrderDates();
	validationMsg += validateOrderDeliveryTime();
	validationMsg += validateOrderTotalAmountPaid();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function validateAllOrderText() {
	var validationMsg = "";
	
	var deliveryContactName = $('#deliveryContactName').val();
	if (deliveryContactName != "") {
		if (!validateName(deliveryContactName, 50)) {
			validationMsg += "Contact Name, "
		}
	}
	
	var notes = $('#orderNotes0\\.notes').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	for (i = 1; i < 4; i++) {
		validationMsg += validatePaymentReferenceNum(i)
	}
	
	return validationMsg;
}

function validateMissingPermit() {
	if ($('#permits\\[0\\]').val() == "") {
		return false;
	}
	
	var permit1Elem = $('#permits\\[1\\]');
	if (permit1Elem.length) {
		if (permit1Elem.val() == "") {
			return false;
		}
	}
	
	var permit2Elem = $('#permits\\[2\\]');
	if (permit2Elem.length) {
		if (permit2Elem.val() == "") {
			return false;
		}
	}
	
	return true;
}

function validateMissingOrderPayment(index) {
	var missingData = "";
	
	var paymentMethod = $('#orderPayment' + (index-1) + '\\.paymentMethod').val();
	var amountPaid = $('#orderPayment'  + (index-1) + '\\.amountPaid').val();
	var ccReferenceNum = $('#orderPayment'  + (index-1) +  '\\.ccReferenceNum').val();
	var checkNum = $('#orderPayment' + (index-1) + '\\.checkNum').val();
	var paymentDateId = 'orderPayment[' + (index-1) + '].paymentDate';
	var paymentDate = $("[name='" + paymentDateId + "']").val();
	if (paymentMethod != "") {
		/*if (amountPaid == "") {
			missingData += "Payment Amount " + index + ", ";
		}*/
		 
		// Credit card
		if (paymentMethod == "3" && checkNum != "") {
			missingData += "Cc Reference # " + index + ", ";
		// Not cash
		} else if ((paymentMethod == "1" || paymentMethod == "4" || paymentMethod == "5") && ccReferenceNum != "") {
			missingData += "Check # " + index + ", ";
		}
		
		if ((amountPaid != "" && amountPaid != "0.00") && paymentDate == "") {
			missingData += "Payment Date " + index + ", ";
		}
		
		if (paymentDate != "" && (amountPaid == "" || amountPaid == "0.00")) {
			missingData += "Amount Paid " + index + ", ";
		}
		
		if ((amountPaid != "" && amountPaid != "0.00") || paymentDate != "") {
			// Credit card
			if (paymentMethod == "3" && ccReferenceNum == "") {
				missingData += "Cc Reference # " + index + ", ";
			// Not cash
			} else if ((paymentMethod == "1" || paymentMethod == "4" || paymentMethod == "5") && checkNum == "") {
				missingData += "Check # " + index + ", ";
			}
		}
	} else {
		if (amountPaid != "" || ccReferenceNum != "" || checkNum != "" || paymentDate != "") {
			missingData += "Payment method " + index + ", ";
		}
	}
	
	return missingData;
}

function validateOrderFees() {
	var validationMsg = "";
	
	var dumpsterPrice = $('#orderFees\\.dumpsterPrice').val();
	if (dumpsterPrice != "") {
		if (!validateAmount(dumpsterPrice, 1500)) {
			validationMsg += "Dumpster Price, "
		}
	}
	
	var cityFeeType = $('#orderFees\\.cityFeeType').val();
	var cityFee = $('#orderFees\\.cityFee').val();
	if (cityFeeType != "" && cityFee == "") {
		validationMsg += "City Fee, "
	}
	if (cityFeeType == "" && cityFee != "") {
		validationMsg += "City Fee Description, "
	}
	if (cityFee != "") {
		if (!validateAmount(cityFee, 1000)) {
			validationMsg += "City Fee, "
		}
	}
	
	for (i = 1; i < 4; i++) {
		validationMsg += validateAdditionalFees(i)
	}
	
	var overweightFee = $('#orderFees\\.overweightFee').val();
	if (overweightFee != "") {
		if (!validateAmount(overweightFee, 1000)) {
			validationMsg += "Over weight Fee, "
		}
	}
	
	var tonnageFee = $('#orderFees\\.tonnageFee').val();
	if (tonnageFee != "") {
		if (!validateAmount(tonnageFee, 3000)) {
			validationMsg += "Tonnage Fee, "
		}
	}
	
	for (i = 1; i < 4; i++) {
		validationMsg += validatePaymentAmount(i)
	}
	
	for (i = 1; i < 4; i++) {
		validationMsg += validatePaymentDate(i)
	}
	
	for (i = 1; i < 4; i++) {
		validationMsg += validatePermitFees(i)
	}
	
	return validationMsg;
}

function validateAdditionalFees(index) {
	var validationMsg = "";
	
	var additionalFeeType = $('#additionalFee' + index + 'Type').val();
	var additionalFee = $('#orderFees\\.additionalFee' + index).val();
	if (additionalFeeType != "" && additionalFee == "") {
		validationMsg += "Additional Fee " + index + ", "
	}
	if (additionalFeeType == "" && additionalFee != "") {
		validationMsg += "Additional Fee " + index + " Description, "
	}
	if (additionalFee != "") {
		if (!validateAmount(additionalFee, 1000)) {
			validationMsg += "Additional Fee " + index + ", "
		}
	}
	
	return validationMsg;
}

function validatePaymentAmount(index) {
	var validationMsg = "";
	
	var orderPayment = $('#orderPayment' + (index-1) + '\\.amountPaid').val();
	if (orderPayment != "") {
		if (!validateAmount(orderPayment, null)) {
			validationMsg += "Order Payment " + index + " Amount, "
		}
	}
	
	return validationMsg;
}

function validatePaymentDate(index) {
	var validationMsg = "";
	
	var paymentDateId = 'orderPayment[' + (index-1) + '].paymentDate';
	var paymentDate = $("[name='" + paymentDateId + "']").val();
	if (paymentDate != "") {
		if (!validateDate(paymentDate)) {
			validationMsg += "Order Payment " + index + " Date, "
		}
	}
	
	return validationMsg;
}

function validateOrderTotalAmountPaid() {
	var validationMsg = "";
	
	var totalPaid = parseFloat(0.00);
	var orderPayment1 = $('#orderPayment0\\.amountPaid').val();
	if (orderPayment1 != "" && validateAmount(orderPayment1, null)) {
		totalPaid += parseFloat(orderPayment1);
	}
	var orderPayment2 = $('#orderPayment1\\.amountPaid').val();
	if (orderPayment2 != "" && validateAmount(orderPayment2, null)) {
		totalPaid += parseFloat(orderPayment2);
	}
	var orderPayment3 = $('#orderPayment2\\.amountPaid').val();
	if (orderPayment3 != "" && validateAmount(orderPayment3, null)) {
		totalPaid += parseFloat(orderPayment3);
	}
	
	var totalFeesStr = $('#orderFees\\.totalFees').val();
	if (totalFeesStr != "" && validateAmount(totalFeesStr, null)) {
		var totalFees = parseFloat(totalFeesStr);
		if (totalPaid > totalFees) {
			validationMsg += "Total amount paid > Total fees, ";
		}
	}
	
	return validationMsg;
}

function validatePaymentReferenceNum(index) {
	var validationMsg = "";
	
	var ccRefernceNum = $('#orderPayment' + (index-1) + '\\.ccReferenceNum').val();
	if (ccRefernceNum != "") {
		if (!validateReferenceNum(ccRefernceNum, 50)) {
			validationMsg += "Order Payment " + index + " CC Reference #, "
		}
	}
	
	var checkNum = $('#orderPayment' + (index-1) + '\\.checkNum').val();
	if (checkNum != "") {
		if (!validateReferenceNum(checkNum, 50)) {
			validationMsg += "Order Payment " + index + " Check #, "
		}
	}
	
	if (ccRefernceNum != "" && checkNum != "") {
		validationMsg += "Order Payment " + index + ": Both CC Reference # and Check # specified, ";
	}
	
	return validationMsg;
}

function validatePermitFees(index) {
	var validationMsg = "";
	
	var permitFeeElem = $('#orderFees\\.permitFee' + index);
	if (!permitFeeElem.length) {
		return validationMsg;
	}
	
	var permitNumbersSelect = $("#permits\\[" + (index-1) + "\\]");
	var permitId = permitNumbersSelect.val();
	if (permitId == "") {
		return validationMsg;
	}
	
	var permitFee = permitFeeElem.val();
	if (!validateAmount(permitFee, 500)) {
		validationMsg += "Permit " + index + " Fee, "
	}
	
	return validationMsg;
}

function validateAllOrderPhones() {
	var validationMsg = "";
	
	var deliveryContactPhone1 = $('#deliveryContactPhone1').val();
	if (deliveryContactPhone1 != "") {
		if (!validatePhone(deliveryContactPhone1)) {
			validationMsg += "Phone 1, "
		}
	}
	
	var deliveryContactPhone2 = $('#deliveryContactPhone2').val();
	if (deliveryContactPhone2 != "") {
		if (!validatePhone(deliveryContactPhone2)) {
			validationMsg += "Phone 2, "
		}
	}
	
	return validationMsg;
}

function validateAllOrderDates() {
	var validationMsg = "";
	
	var deliveryDate = $("[name='deliveryDate']").val();
	if (deliveryDate != "") {
		if (!validateDate(deliveryDate)) {
			validationMsg += "Delivery Date, "
		}
	}
	
	return validationMsg;
}

function validateOrderDeliveryTime() {
	var validationMsg = "";
	
	var deliveryHourFrom = $('#deliveryHourFrom').val();
	var deliveryHourTo = $('#deliveryHourTo').val();
	
	if (!validateTimeRange(deliveryHourFrom, deliveryHourTo)) {
		validationMsg += "Delivery Time, "
	}
	
    return validationMsg;
}

function processOrderForm() {
	if (validateOrderForm()) {
		verifyExchangeOrderAndSubmit();
	}
}

function verifyExchangeOrderAndSubmit() {
	var isExchangeIndicatorElem = $('#isExchange');
	var existingDroppedOffOrderIdElem = $('#existingDroppedOffOrderId');
	isExchangeIndicatorElem.val("false");
	existingDroppedOffOrderIdElem.val("");
	
	var idElem = $('#id');
	if (idElem.val() != "") {
		orderAddEditForm.submit();
		return false;
	} 
		
	var pickUpOrderIdElem = $('#pickupOrderId');
	if (pickUpOrderIdElem.val() != "") {
		isExchangeIndicatorElem.val("true");
		existingDroppedOffOrderIdElem.val(pickUpOrderIdElem.val());
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
        		existingDroppedOffOrderIdElem.val(existingDroppedOffOrderId);
        		
        		var exchMsg = "<p>There is already a Dumpster delivered to this address with the Order# "
      			  		   	  + existingDroppedOffOrderId
    			  		   	  + " and can be picked up as an Exchange Order.<br><br>"
    			  		   	  + "Would you like to create an Exchange Order?</p>";
    			  	
        		showConfirmDialog("Confirm Exchange Order", exchMsg);
        	} else {
        		isExchangeIndicatorElem.val("false");
        		existingDroppedOffOrderIdElem.val("");
        		orderAddEditForm.submit();
        	}
        }
    });
    
    return false;
}

function updateTotalPaid() {
	var amountPaid1 = $('#orderPayment'  + 0 + '\\.amountPaid').val();
	var amountPaid2 = $('#orderPayment'  + 1 + '\\.amountPaid').val();
	var amountPaid3 = $('#orderPayment'  + 2 + '\\.amountPaid').val();
	
	var amountPaid1Float = parseFloat(0.00);
	if (amountPaid1 != "") {
		amountPaid1Float = parseFloat(amountPaid1);
	}
	var amountPaid2Float = parseFloat(0.00);
	if (amountPaid2 != "") {
		amountPaid2Float = parseFloat(amountPaid2);
	}
	var amountPaid3Float = parseFloat(0.00);
	if (amountPaid3 != "") {
		amountPaid3Float = parseFloat(amountPaid3);
	}
	
	var totalFeesFloat = parseFloat(0.00);
	var totalFees = $('#orderFees\\.totalFees').val(); 
	if (totalFees != "") {
		totalFeesFloat = parseFloat(totalFees);
	}
	
	var totalPaidFloat = parseFloat(0.00);
	var balanceDueFloat = parseFloat(0.00);
	
	totalPaidFloat = amountPaid1Float + amountPaid2Float + amountPaid3Float;
	balanceDueFloat = totalFeesFloat - totalPaidFloat;
	$('#totalPaid').html(totalPaidFloat.toFixed(2)); 
	$('#balanceDue').html(balanceDueFloat.toFixed(2)); 
}
</script>
<form:form action="save.do" name="orderAddEditForm" commandName="modelObject" method="post" id="orderAddEditForm">
	<form:hidden path="id" id="id" />
	<form:hidden path="pickupOrderId" id="pickupOrderId" />
	
	<input type="hidden" name="isExchange" id="isExchange" value="false" />
	<input type="hidden" name="existingDroppedOffOrderId" id="existingDroppedOffOrderId" value="" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageOrder" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td></td></tr>
		<tr>
			<td class="form-left">Order #</td>
			<td class="wide td-static" id="orderIdTd">${modelObject.id}</td>
			<td class="form-left">Status</td>
			<td class="td-static">${modelObject.orderStatus.status}</td>
		</tr>
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="customerSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="customer" onChange="return populateCustomerInfo();"> 
						<form:option value="">-----Please Select-----</form:option>
						<form:options items="${customers}" itemValue="id" itemLabel="companyName" />
					</form:select>
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="${ctx}/customer/createModal.do" id="addCustomerLink">
						<img src="${ctx}/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label> 
				<form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Address<span class="errorMessage"></span></td>
			<td class="td-static" id="billingAddressTd">${modelObject.customer.getBillingAddress("<br>")}</td>
		</tr>
		<tr>
			<td class="form-left">Contact<span class="errorMessage"></span></td>
			<td class="td-static" id="billingContactTd">${modelObject.customer.contactName}</td>
			<td class="form-left">Fax</td>
			<td class="td-static" id="billingFaxTd">${modelObject.customer.getFormattedFax()}</td>
		</tr>
		<tr>
			<td class="form-left">Phone<span class="errorMessage"></span></td>
			<td class="td-static" id="billingPhoneTd">${modelObject.customer.getFormattedPhone()}</td>
			<td class="form-left">Email</td>
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
			<td class="form-left">Delivery Address<span class="errorMessage">*</span></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryAddressSelect" cssClass="flat form-control input-sm" path="deliveryAddress" style="width:172px !important" onChange="return handleDeliveryAddressChange();">
						<form:option value="">-----Please Select-----</form:option>
						<form:options items="${deliveryAddresses}" itemValue="id" itemLabel="fullLine"/>
					</form:select> 
				</label>
				<label style="display: inline-block; font-weight: normal">
					&nbsp;
					<a href="${ctx}/customer/deliveryAddressCreateModal.do" id="addDeliveryAddressLink" >
						<img src="${ctx}/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
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
			<td class="form-left">City</td>
			<td>
				<form:select id="orderFees.cityFeeType" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.cityFeeType" onChange="return populateCityFee();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${cityFeeDetails}" itemValue="id" itemLabel="suburbName" />
				</form:select>
				<form:errors path="orderFees.cityFeeType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Contact Name<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryContactName" cssClass="flat" style="width:172px !important" maxlength="50" />
			 	<form:errors path="deliveryContactName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone1<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryContactPhone1" cssClass="flat" style="width:172px !important" maxlength="12" 
					id="deliveryContactPhone1" onblur="return validateAndFormatPhone('deliveryContactPhone1');"/>
				<form:errors path="deliveryContactPhone1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Phone2</td>
			<td>
				<form:input path="deliveryContactPhone2" id="deliveryContactPhone2" cssClass="flat" style="width:172px !important" maxlength="12"
					onblur="return validateAndFormatPhone('deliveryContactPhone2');"/>
				<form:errors path="deliveryContactPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Date<span class="errorMessage">*</span></td>
			<td>
				<form:input path="deliveryDate" cssClass="flat" style="width:172px !important" id="datepicker7" name="deliveryDate" maxlength="10" onChange="return handleDeliveryDateChange();"/>
				<form:errors path="deliveryDate" cssClass="errorMessage" />
			</td>
			<td class="form-left">Delivery Time<span class="errorMessage">*</span></td>
			<td>
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourFrom" cssClass="flat form-control input-sm" style="width:85px !important" path="deliveryHourFrom"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
				&nbsp;to&nbsp;
				<label style="display: inline-block; font-weight: normal">
					<form:select id="deliveryHourTo" cssClass="flat form-control input-sm" style="width:85px !important" path="deliveryHourTo"> 
						<form:options items="${deliveryHours}" />
					</form:select>
				</label>
			 	<form:errors path="deliveryHourFrom" cssClass="errorMessage" /><form:errors path="deliveryHourTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Dumpster Location<span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterLocationSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterLocation" onchange="return handleDumpsterLocationChange();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${dusmpsterLocationTypes}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<form:errors path="dumpsterLocation" cssClass="errorMessage" />
			</td>
			<td class="form-left">Dumpster Size<span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterSize" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterSize" onchange="return handleDumpsterSizeChange();"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id" itemLabel="size" />
				</form:select>
			 	<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<select class="flat form-control input-sm" id="materialCategory" name="materialCategory" id="materialCategory" style="width: 172px !important" onChange="return populateMaterialTypes();">
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
			<td class="form-left">Material Type<span class="errorMessage">*</span></td>
			<td>
				<form:select id="materialType" cssClass="flat form-control input-sm" style="width:172px !important" path="materialType"  onChange="return populateDumpsterPrice();"> 
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
	    	<td class="form-left">Permit1 Class<span class="errorMessage"></span></td>
	        <td>
				<select class="flat form-control input-sm" id="permitClasses1" name="permitClasses1" style="width:172px !important" onChange="return handlePermitClassChange(1);">
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
		 	<c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].permitClass != null}">
				<td class="form-left">Permit2 Class/></td>
		        <td>
					<select class="flat form-control input-sm" id="permitClasses2" name="permitClasses2" style="width:172px !important" onChange="return handlePermitClassChange(2);">
						<option value="">-----Please Select-----</option>
						<c:forEach items="${permitClasses}" var="aPermitClass">
							<c:set var="selected" value="" />
							<c:if test="${modelObject.permits[1].permitClass.id == aPermitClass.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitClass.id}" ${selected}>${aPermitClass.permitClass}</option>
						</c:forEach>
					</select>
			 	</td>
			</c:if>
			<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].permitClass != null}">
				<td class="form-left">Permit3 Class</td>
		        <td>
					<select class="flat form-control input-sm" id="permitClasses3" name="permitClasses3" style="width:172px !important" onChange="return handlePermitClassChange(3);">
						<option value="">-----Please Select-----</option>
						<c:forEach items="${permitClasses}" var="aPermitClass">
							<c:set var="selected" value="" />
							<c:if test="${modelObject.permits[2].permitClass.id == aPermitClass.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitClass.id}" ${selected}>${aPermitClass.permitClass}</option>
						</c:forEach>
					</select>
			 	</td>
			</c:if>
	    </tr>
	    <tr>
	    	<td class="form-left">Permit1 Type<span class="errorMessage"></span></td>
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
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].permitType != null}">
				<td class="form-left">Permit2 Type<span class="errorMessage">*</span></td>
		        <td>
		        	<select class="flat form-control input-sm" id="permitTypes2" name="permitTypes2" style="width:172px !important" onChange="return populatePermitNumbers(2);">
						<option value="">-----Please Select-----</option>
						<c:forEach items="${permitTypes}" var="aPermitType">
							<c:set var="selected" value="" />
							<c:if test="${modelObject.permits[1].permitType.id == aPermitType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitType.id}" ${selected}>${aPermitType.permitType}</option>
						</c:forEach>
					</select>
		        </td>
			</c:if>
	        <c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].permitType != null}">
				<td class="form-left">Permit3 Type<span class="errorMessage">*</span></td>
		        <td>
		        	<select class="flat form-control input-sm" id="permitTypes3" name="permitTypes3" style="width:172px !important" onChange="return populatePermitNumbers(3);">
						<option value="">-----Please Select-----</option>
						<c:forEach items="${permitTypes}" var="aPermitType">
							<c:set var="selected" value="" />
							<c:if test="${modelObject.permits[2].permitType.id == aPermitType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitType.id}" ${selected}>${aPermitType.permitType}</option>
						</c:forEach>
					</select>
		        </td>
			</c:if>
	    </tr>
	    <tr>
	    	<td class="form-left">Permit1 Number<span class="errorMessage"></span></td>
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
					<a href="${ctx}/permit/createForCustomerModal.do" id="addPermitLink" >
						<img src="${ctx}/images/addnew.png" border="0" style="float:bottom" class="toolbarButton">
					</a>
				</label>
	        </td>
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
				<td class="form-left">Permit2 Number<span class="errorMessage"></span></td>
		        <td>
		        	<select class="flat form-control input-sm" id="permits[1]" name="permits[1]" style="width:172px !important" onChange="return retrievePermitDetails(2);">
						<option value="">-----Please Select-----</option>
						<c:set var="chosenPermit" value="${modelObject.permits[1]}" />
						<c:set var="allPermitsOfChosenType" value="${allPermitsOfChosenTypesList[1]}" />
						<c:forEach items="${allPermitsOfChosenType}" var="aPermitOfChosenType">
							<c:set var="selected" value="" />
							<c:if test="${aPermitOfChosenType.id == chosenPermit.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitOfChosenType.id}" ${selected}>${aPermitOfChosenType.number}</option>
						</c:forEach>
					</select>
		        </td>
			</c:if>
			<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
				<td class="form-left">Permit3 Number<span class="errorMessage"></span></td>
		        <td>
		        	<select class="flat form-control input-sm" id="permits[2]" name="permits[2]" style="width:172px !important" onChange="return retrievePermitDetails(3);">
						<option value="">-----Please Select-----</option>
						<c:set var="chosenPermit" value="${modelObject.permits[2]}" />
						<c:set var="allPermitsOfChosenType" value="${allPermitsOfChosenTypesList[2]}" />
						<c:forEach items="${allPermitsOfChosenType}" var="aPermitOfChosenType">
							<c:set var="selected" value="" />
							<c:if test="${aPermitOfChosenType.id == chosenPermit.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitOfChosenType.id}" ${selected}>${aPermitOfChosenType.number}</option>
						</c:forEach>
					</select>
		        </td>	
			</c:if>	
		</tr>
	    <tr>
	    	<td class="form-left">Permit1 Valid From</td>
	        <td class="td-static" id="permitValidFrom1">${modelObject.permits[0].formattedStartDate}</td>
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
				<td class="form-left">Permit2 Valid From</td>
	       		<td class="td-static" id="permitValidFrom2">${modelObject.permits[1].formattedStartDate}</td>
			</c:if>
			<c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
				<td class="form-left">Permit3 Valid From</td>
	        	<td class="td-static" id="permitValidFrom3">${modelObject.permits[2].formattedStartDate}</td>
			</c:if>
	    </tr>
	    <tr>
	    	<td class="form-left">Permit1 Valid To</td>
	        <td class="td-static" id="permitValidTo1">${modelObject.permits[0].formattedEndDate}</td>
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
				<td class="form-left" >Permit2 Valid To</td>
		        <td class="td-static" id="permitValidTo2">${modelObject.permits[1].formattedEndDate}</td>
			</c:if>	
	        <c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
				<td class="form-left">Permit3 Valid To</td>
	        	<td class="td-static" id="permitValidTo3">${modelObject.permits[2].formattedEndDate}</td>
			</c:if>
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
	      <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
		  	<td class="form-left">Permit2 Address</td>
		      <td>
	        	<select class="flat form-control input-sm" id="permitAddress2" name="permitAddress2" style="width:172px !important">
					<c:forEach items="${modelObject.permits[1].permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.id}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>
				</select>
		      </td>
		  </c:if>		
	      <c:if test="${modelObject.permits != null and modelObject.permits[2] != null and modelObject.permits[2].number != null}">
		  	<td class="form-left">Permit3 Address</td>
		      <td>
	        	<select class="flat form-control input-sm" id="permitAddress3" name="permitAddress3" style="width:172px !important">
					<c:forEach items="${modelObject.permits[2].permitAddress}" var="aPermitAddress">
						<option value="${aPermitAddress.id}" ${selected}>${aPermitAddress.fullLine}</option>
					</c:forEach>
				</select>
		      </td>
		  </c:if>
		</tr>
	    <tr>
	    	<td class="form-left">Permit1 Fee</td>
	        <td class="td-static"><form:input path="orderFees.permitFee1" style="width:172px !important" maxlength="7" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
				<td class="form-left">Permit2 Fee</td>
	        	<td class="td-static"><form:input path="orderFees.permitFee2" style="width:172px !important" maxlength="7" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
	        </c:if>
	        <c:if test="${modelObject.permits != null and modelObject.permits[1] != null and modelObject.permits[1].number != null}">
				<td class="form-left">Permit3 Fee</td>
	        	<td class="td-static"><form:input path="orderFees.permitFee3" style="width:172px !important" maxlength="7" cssClass="flat" onChange="return populateTotalPermitFees();"/></td>
			</c:if>
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
				<form:input path="orderFees.totalPermitFees" readonly="true" cssClass="form-control form-control-ext" style="width:172px;height:22px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left">Dumpster Price<span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.dumpsterPrice" cssClass="form-control form-control-ext" style="width:172px;height:22px !important" maxlength="7" onChange="return populateTotalFees();"/>
				<form:errors path="orderFees.dumpsterPrice" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Tonnage Fee</td>
			<td>
				<form:input path="orderFees.tonnageFee" cssClass="form-control form-control-ext" style="width:172px;height:22px !important" maxlength="7" onChange="return populateTotalFees();"/>
				<form:errors path="orderFees.tonnageFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Overweight Fee</td>
			<td>
				<form:input path="orderFees.overweightFee" cssClass="form-control form-control-ext" style="width:172px;height:22px !important" maxlength="7" onChange="return populateTotalFees();"/>
				<form:errors path="orderFees.overweightFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City Fee</td>
			<td>
				<form:input path="orderFees.cityFee" cssClass="form-control form-control-ext" style="width:172px;height:22px !important" maxlength="7" onChange="return populateTotalFees();"/>
				<form:errors path="orderFees.cityFee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Additional Fee1</td>
			<td>
				<form:input path="orderFees.additionalFee1" style="width:172px !important" cssClass="flat" maxlength="7" onchange="return populateTotalAdditionalFees();"/>
				<form:errors path="orderFees.additionalFee1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Description</td>
				<td>
				<form:select id="additionalFee1Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee1Type" > 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee1Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Additional Fee2</td>
			<td>
				<form:input path="orderFees.additionalFee2" style="width:172px !important" cssClass="flat" maxlength="7" onchange="return populateTotalAdditionalFees();"/>
				<form:errors path="orderFees.additionalFee2" cssClass="errorMessage" />
			</td>
			<td class="form-left">Description</td>
			<td>
				<form:select id="additionalFee2Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee2Type"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee2Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Additional Fee3</td>
			<td>
				<form:input path="orderFees.additionalFee3" style="width:172px !important" cssClass="flat" maxlength="7" onchange="return populateTotalAdditionalFees();"/>
				<form:errors path="orderFees.additionalFee3" cssClass="errorMessage" />
			</td>
			<td class="form-left">Description</td>
				<td>
				<form:select id="additionalFee3Type" cssClass="flat form-control input-sm" style="width:172px !important" path="orderFees.additionalFee3Type"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${additionalFeeTypes}" itemValue="id" itemLabel="description" />
				</form:select>
				<form:errors path="orderFees.additionalFee3Type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Total Additional Fees</td>
			<td>
				<form:input path="orderFees.totalAdditionalFees" readonly="true" cssClass="form-control form-control-ext" style="width:172px;height:22px !important"/>
			</td>
		</tr>
		<tr>
			<td class="form-left">Discount Amount</td>
			<td>
				<form:input path="orderFees.discountAmount" cssClass="form-control form-control-ext" style="width:172px;height:22px !important" onchange="return populateTotalFees();"/>
			</td>
		</tr>
		<tr>
			<td class="form-left">Total Fees<span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderFees.totalFees" cssClass="form-control form-control-ext" readonly="true" style="width:172px;height:22px !important"/>
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
			<td class="form-left">Payment Method</td>
			<td class="form-left">Payment Date</td>
			<td class="form-left">Amount</td>
			<td class="form-left">CC Reference #</td>
			<td class="form-left">Check #</td>
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
				<form:input path="orderPayment[0].paymentDate" cssClass="flat" style="width:172px !important" id="datepicker8" maxlength="10" />
				<form:errors path="orderPayment[0].paymentDate" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].amountPaid" maxlength="7" cssClass="flat" onChange="updateTotalPaid();"/>
				<br><form:errors path="orderPayment[0].amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].ccReferenceNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="orderPayment[0].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[0].checkNum" maxlength="50" cssClass="flat" />
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
			<td class="td-static wide">
				<form:input path="orderPayment[1].paymentDate" cssClass="flat" style="width:172px !important" id="datepicker9" maxlength="10" />
				<form:errors path="orderPayment[1].paymentDate" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="orderPayment[1].amountPaid" maxlength="7" cssClass="flat" onChange="updateTotalPaid();"/>
				<br><form:errors path="orderPayment[1].amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[1].ccReferenceNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="orderPayment[1].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="orderPayment[1].checkNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="orderPayment[1].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important;" path="orderPayment[2].paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="orderPayment[2].paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static wide">
				<form:input path="orderPayment[2].paymentDate" cssClass="flat" style="width:172px !important" id="datepicker10" maxlength="10" />
				<form:errors path="orderPayment[2].paymentDate" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[2].amountPaid" maxlength="7" cssClass="flat" onChange="updateTotalPaid();"/>
				<br><form:errors path="orderPayment[2].amountPaid" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="orderPayment[2].ccReferenceNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="orderPayment[2].ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="orderPayment[2].checkNum" maxlength="50" cssClass="flat" />
				<br><form:errors path="orderPayment[2].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left" style="text-align: right;" colspan="2">Total Amount Paid:</td>
			<td class="td-static">
				<span id="totalPaid" style="font-weight: bold; padding: 0 10px;">${modelObject.totalAmountPaid}</span>
				<span style="font-weight: bold; float:right;">Balance Due:</span>
			</td>
			<c:set var="balanceDueAlertClass" value="" />
			<c:if test="${modelObject.balanceAmountDue > 0}">
				<c:set var="balanceDueAlertClass" value="errorMessage" />
			</c:if>
			<td class="td-static">
				<span class="${balanceDueAlertClass}" id="balanceDue" style="font-weight: bold;font-size: 13px; padding: 0 10px;">${modelObject.balanceAmountDue}</span>
			</td>
			<td></td>
			<td></td>
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
							and modelObject.orderNotes[0].notes != null and modelObject.orderNotes[0].notes.length() > 0
							and modelObject.id != null}">
					<c:set var="orderNotesDisabled" value="true" />
				</c:if>
				<form:textarea readonly="${orderNotesDisabled}" row="5" path="orderNotes[0].notes" maxlength="500" cssClass="form-control" style="width:50%; height:100%;"/>
				<form:errors path="orderNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td colspan="2">
				<input type="button" id="orderCreate" onclick="processOrderForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="orderCancelBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
				
				<c:set var="printDisabled" value="" />
				<c:if test="${modelObject.id == null}">
					<c:set var="printDisabled" value="disabled" />
				</c:if>
				<input type="button" id="orderPrintBtn" ${printDisabled} value="Print" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='printOrder.do?orderId=${modelObject.id}'" />
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

$("#confirmDialogNo").click(function (ev) {
	var isExchangeIndicator = $('#isExchange');
	isExchangeIndicator.val("false");
	
	$('#existingDroppedOffOrderId').val("");
	
	var orderAddEditForm = $("#orderAddEditForm");
	orderAddEditForm.submit();
});

$("#addCustomerLink").click(function (ev) {
	var orderId = $('#id').val();
	if (orderId != "") {
		showAlertDialog("Data Validation", "New customer cannot be added to existing order");
	} else {
		var url = $(this).attr("href");
		showPopupDialog("Add Customer", url);
	}
	
	ev.preventDefault();
});

$("#addDeliveryAddressLink").click(function (ev) {
	var customerId = $('#customerSelect').val();
	if (customerId == "") {
		var alertMsg = "Please select Customer for adding Delivery address."
		showAlertDialog("Data Validation", alertMsg);
	} else {
		var url = $(this).attr("href");
		url += "?customerId=" + customerId;
		
		showPopupDialog("Add Delivery Address", url);
	}
	
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
		var alertMsg = "Please select Customer, Delivery address, Delivery date, Location, Permit class and Type for creating the new permit."
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var orderId = $('#id').val();
	
	var newPermitUrl = $(this).attr("href");
	
	$.ajax({
  		url: "${ctx}/permit/validatePermitCanBeAdded.do?orderId=" + orderId + "&deliveryAddressId=" + deliveryAddressId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var validationErrorMsg = jQuery.parseJSON(responseData);
    	   	if (validationErrorMsg != "") {
    	   		showAlertDialog("Order Permit Data Validation", validationErrorMsg);
    	   		return false;
    	   	}
    	   	
    	   	newPermitUrl += "?customerId=" + customerId 
    					 +  "&deliveryAddressId=" + deliveryAddressId
    					 +  "&locationTypeId=" + locationTypeId
    					 +  "&permitClassId=" + permitClassId
    					 +  "&permitTypeId=" + permitTypeId
    					 +  "&deliveryDate=" + deliveryDate;
    		
    		showPopupDialog("Add Permit", newPermitUrl);
		}
	});
	
	ev.preventDefault();
});
</script>


