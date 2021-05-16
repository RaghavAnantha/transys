<%@include file="/common/taglibs.jsp"%>

<style>
.hide-calendar .ui-datepicker-calendar {
    display: none;
}
</style>

<script type="text/javascript">
function getInvoicePaymentForm() {
	var form = $('#invoicePaymentForm');
	return form;
}

function loadInvoicePayment(data) {
	$("#invoicePayment").html(data);
}

function processInvoicePaymentBack() {
	$.get("invoicePaymentMain.do", function(data) {
		loadInvoicePayment(data);
    });
}

function processInvoicePaymentFormSubmit() {
	if (validateInvoicePaymentForm()) {
		var invoicePaymentForm = getInvoicePaymentForm();
		invoicePaymentForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateInvoicePaymentForm() {
	var missingData = validateInvoicePaymentMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateInvoicePaymentDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateInvoicePaymentMissingData() {
	var missingData = "";
	
	var form = getInvoicePaymentForm();
	if (form.find('#createInvoicePaymentCustomerId').val() == "") {
		missingData += "Customer, "
	}
	if (form.find('#createInvoicePaymentInvoiceNo').val() == "") {
		missingData += "Invoice #, "
	}
	if (form.find('#paymentMethod').val() == "") {
		missingData += "Payment method, "
	}
	var amountPaid = form.find('#amountPaid').val();
	if (amountPaid == "") {
		missingData += "Amount Paid, "
	}
	if (form.find("[name='paymentDate']").val() == "") {
		missingData += "Payment Date, "
	}
	
	missingData += validateMissingInvoicePayment();
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateMissingInvoicePayment() {
	var missingData = "";
	
	var form = getInvoicePaymentForm();
	var paymentMethod = form.find('#paymentMethod').val();
	if (paymentMethod == "") {
		return missingData;
	}
	
	var checkNum = form.find('#checkNum').val();
	var ccReferenceNum = form.find('#ccReferenceNum').val();
	var ccName = form.find('#ccName').val();
	var ccNumber = form.find('#ccNumber').val();
	var ccExpDate = form.find("[name='ccExpDate']").val();
	
	// Credit card
	if (paymentMethod == "3" && checkNum != "") {
		missingData += "Cc Reference#, ";
	}
	// Not cash
	if ((paymentMethod == "1" || paymentMethod == "4" || paymentMethod == "5") && ccReferenceNum != "") {
		missingData += "Check #, ";
	}
	// Credit card
	if (paymentMethod == "3") {
		if (ccReferenceNum == "") {
			missingData += "Cc Reference#, ";
		}
		if (ccName == "") {
			missingData += "Cc Name, ";
		}
		if (ccNumber == "") {
			missingData += "Cc#, ";
		}
		if (ccExpDate == "") {
			missingData += "Cc Exp Date, ";
		}
		
	}
	// Not cash
	if ((paymentMethod == "1" || paymentMethod == "4" || paymentMethod == "5") && checkNum == "") {
		missingData += "Check#, ";
	}
	return missingData;
}

function validateInvoicePaymentDataFormat() {
	var validationMsg = "";
	
	validationMsg += validateAllAmounts(); 
	validationMsg += validateAllDates();
	validationMsg += validateAllCCNumbers();
	validationMsg += validatePaymentReferenceNum();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function validateAllCCNumbers() {
	var form = getInvoicePaymentForm();
	var ccNumber = form.find('#ccNumber').val();
	return validateCCNumber(ccNumber);
}

function validatePaymentReferenceNum() {
	var validationMsg = "";
	
	var form = getInvoicePaymentForm();
	var ccReferenceNum = form.find('#ccReferenceNum').val();
	var checkNum = form.find('#checkNum').val();
	
	if (ccReferenceNum != "") {
		if (!validateReferenceNum(ccReferenceNum, 50)) {
			validationMsg += "CC Reference#, "
		}
	}
	if (checkNum != "") {
		if (!validateReferenceNum(checkNum, 50)) {
			validationMsg += "Check#, "
		}
	}
	if (ccReferenceNum != "" && checkNum != "") {
		validationMsg += "Both CC Reference# and Check# specified, ";
	}
	
	return validationMsg;
}


function validateAllAmounts() {
	var validationMsg = "";
	
	var form = getInvoicePaymentForm();
	var amountPaid = form.find('#amountPaid').val();
	if (amountPaid != "") {
		if (!validateAmountAndRange(amountPaid, 0, 15000)) {
			validationMsg += "Amount Paid, "
		}
	}
	
	return validationMsg;
}

function validateAllDates() {
	var validationMsg = "";
	
	var form = getInvoicePaymentForm();
	var paymentDate = form.find("[name='paymentDate']").val();
	if (paymentDate != "") {
		if (!validateDate(paymentDate)) {
			validationMsg += "Payment Date, "
		}
	}
	
	var ccExpDate = form.find("[name='ccExpDate']").val();
	if (ccExpDate != "") {
		if (!validateExpiryDate(ccExpDate)) {
			validationMsg += "CC Expiry Date, "
		}
	}
	
	return validationMsg;
}

function updateCCNumber() {
	var form = getInvoicePaymentForm();
	var ccNumElem =  form.find('#ccNumber');
	var formattedCCNumber = formatCCNumber(ccNumElem.val());
	ccNumElem.val(formattedCCNumber);
}

function handleCreateInvoicePaymentCustomerChange() {
	var invoiceNoSelect = $('#createInvoicePaymentInvoiceNo');
	emptySelect(invoiceNoSelect);
	
	var customerSelect =  $('#createInvoicePaymentCustomerId');
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	retrieveAndPopulateCreateInvoicePaymentInvoiceNos(customerId);
}

function retrieveAndPopulateCreateInvoicePaymentInvoiceNos(customerId) {
	$.ajax({
  		url: "payableInvoiceNosSearch.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var invoiceNoList = jQuery.parseJSON(responseData);
    	   	populateCreateInvoicePaymentInvoiceNos(invoiceNoList);
		}
	});
}

function populateCreateInvoicePaymentInvoiceNos(invoiceNoList) {
	var invoiceNoSelect = $('#createInvoicePaymentInvoiceNo');
	$.each(invoiceNoList, function () {
   		$("<option />", {
   	        val: this,
   	        text: this
   	    }).appendTo(invoiceNoSelect);
   	});
}

function handleCreateInvoicePaymentInvoiceNoChange() {
	var invoicePaymentBalanceDueElem = $('#invoicePaymentBalanceDue');
	invoicePaymentBalanceDueElem.html("");
	
	var invoiceNo = $('#createInvoicePaymentInvoiceNo').val();
	if (invoiceNo == "") {
		return false;
	}
	
	$.ajax({
  		url: "retrieveInvoiceBalanceDue.do?invoiceNo=" + invoiceNo,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (!invoicePaymentBalanceDueElem.hasClass('errorMessage')) {
       			invoicePaymentBalanceDueElem.addClass('errorMessage');
       		}
       		invoicePaymentBalanceDueElem.html(responseData);
		}
	});
}
</script>

<br/>
<h5 style="margin-top: -15px; !important">Make Invoice Payment</h5>
<form:form action="saveInvoicePayment.do" id="invoicePaymentForm" name="invoicePaymentForm" commandName="invoicePaymentModelObject" method="post" >
	<form:hidden path="id" id="id" />
	<form:hidden path="createdBy" id="createdBy" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="createInvoicePayment" />
		<jsp:param name="errorCtx" value="createInvoicePayment" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td></td></tr>
		<tr>
			<td class="form-left">Payment #</td>
			<td class="wide td-static" id="paymentIdTd">${invoicePaymentModelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Customer<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" 
					id="createInvoicePaymentCustomerId" path="invoice.customerId" 
					onChange="return handleCreateInvoicePaymentCustomerChange();">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customers}" itemValue="id" itemLabel="companyName"/>
				</form:select> 
				<form:errors path="invoice.customerId" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Invoice #<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" 
					id="createInvoicePaymentInvoiceNo" path="invoice"
					onChange="return handleCreateInvoicePaymentInvoiceNoChange();">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${invoiceNos}"/>
				</form:select> 
				<form:errors path="invoice" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<c:set var="balanceDueAlertClass" value="" />
			<c:if test="${invoicePaymentModelObject.invoiceBalanceDue != null
							and invoicePaymentModelObject.invoiceBalanceDue > 0}">
				<c:set var="balanceDueAlertClass" value="errorMessage" />
			</c:if>
			<td class="form-left">Balance Due</td>
	       	<td class="td-static">
	       		<span class="${balanceDueAlertClass}" id="invoicePaymentBalanceDue" style="font-weight: bold;font-size: 13px; padding: 0 14px;">
	       			${invoicePaymentModelObject.invoiceBalanceDueStr}
	       		</span>
	       	</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Payment Information</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td class="form-left">Payment Method<span class="errorMessage">*</span></td>
			<td class="form-left">Payment Date<span class="errorMessage">*</span></td>
			<td class="form-left">Amount<span class="errorMessage">*</span></td>
			<td class="form-left">Check #</td>
			<td class="form-left">CC Reference #</td>
			<td class="form-left">CC Name</td>
			<td class="form-left">CC #</td>
			<td class="form-left">CC Expiry Date</td>
		</tr>
		<tr>
			<td class="wide">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="paymentMethod"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${paymentMethods}" itemValue="id" itemLabel="method" />
				</form:select>
				<form:errors path="paymentMethod" cssClass="errorMessage" />
			</td>
			<td class="td-static wide">
				<form:input path="paymentDate" cssClass="flat" style="width:172px !important" id="datepicker8" maxlength="10" />
				<form:errors path="paymentDate" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="amountPaid" onkeypress="return onlyNumbers(event, true)" maxlength="8" cssClass="flat"/>
				<form:errors path="amountPaid" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="checkNum" maxlength="50" cssClass="flat" />
				<form:errors path="checkNum" cssClass="errorMessage" />
			</td>
			<td class="wide">
				<form:input path="ccReferenceNum" maxlength="50" cssClass="flat" />
				<form:errors path="ccReferenceNum" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="ccName" maxlength="50" cssClass="flat" />
				<form:errors path="ccName" cssClass="errorMessage" />
			</td>
			<td>
				<form:input path="ccNumber" maxlength="19" cssClass="flat" 
					onkeypress="return onlyNumbers(event, false)" onChange="return updateCCNumber();" />
				<form:errors path="ccNumber" cssClass="errorMessage" />
			</td>
			<td class="td-static wide">
				<form:input path="ccExpDate" cssClass="flat" style="width:172px !important" maxlength="7" />
				<form:errors path="ccExpDate" cssClass="errorMessage" />
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
		<tr><td class="form-left">Notes<span class="errorMessage"></span></td></tr>
		<tr>
			<td colspan="7">
				<form:textarea row="5" id="invoicePaymentNotes" path="notes" cssClass="flat notes"/>
				<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" onclick="processInvoicePaymentFormSubmit();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="processInvoicePaymentBack();" />
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">
$(function() {
	var $ccExpDateElem = getInvoicePaymentForm().find("#ccExpDate");
	setExpiryDatepicker($ccExpDateElem);
});

$("#invoicePaymentForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	loadInvoicePayment(responseData);
        }
    });
    
    ev.preventDefault();
});
</script>
