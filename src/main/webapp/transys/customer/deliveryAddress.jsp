<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateDeliveryAddressForm() {
	var missingData = validateDeliveryAddressMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateDeliveryAddressDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateDeliveryAddressMissingData() {
	var missingData = "";
	
	if ($('#deliveryAddressLine1').val() == "") {
		missingData += "Delivery Address #, "
	}
	
	if ($('#deliveryAddressLine2').val() == "") {
		missingData += "Delivery Street, "
	}
	
	if ($('#deliveryAddressCity').val() == "") {
		missingData += "City, "
	}
	
	if ($('#deliveryAddressZipcode').val() == "") {
		missingData += "Zipcode, "
	}
	
	if ($('#deliveryAddressStateSelect').val() == "") {
		missingData += "State, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateDeliveryAddressDataFormat() {
	var validationMsg = "";
	
	var deliveryAddressLine1 = $('#deliveryAddressLine1').val();
	if (deliveryAddressLine1 != "") {
		if (!validateAddressLine(deliveryAddressLine1, 50)) {
			validationMsg += "Delivery Address #, "
		}
	}
	
	var deliveryAddressLine2 = $('#deliveryAddressLine2').val();
	if (deliveryAddressLine2 != "") {
		if (!validateAddressLine(deliveryAddressLine2, 50)) {
			validationMsg += "Delivery Street, "
		}
	}
	
	var zipcode = $('#deliveryAddressZipcode').val();
	if (zipcode != "") {
		if (!validateZipCode(zipcode, 12)) {
			validationMsg += "Zipcode, "
		}
	}
	
	var city = $('#deliveryAddressCity').val();
	if (city != "") {
		if (!validateName(city, 50)) {
			validationMsg += "City, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function processDeliveryAddressForm() {
	if (validateDeliveryAddressForm()) {
		var deliveryAddressForm = $("#deliveryAddressForm");
		deliveryAddressForm.submit();
	}
}
</script>

<form:form id="deliveryAddressForm" action="saveDeliveryAddress.do" name="deliveryAddressForm" commandName="deliveryAddressModelObject" method="post" >
	<form:hidden path="id" />
	<form:hidden path="customer.id" id="customerId" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCustomerDeliveryAddress" />
		<jsp:param name="errorCtx" value="manageCustomerDeliveryAddress" />
	</jsp:include>
	<table id="form-table" class="table customerDeliveryAddress">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Customer ID</td>
			<td class="td-static">${deliveryAddressModelObject.customer.id}</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Address #<span class="errorMessage">*</span></td>
			<td>
				<form:input path="line1" cssClass="flat flat-ext" id="deliveryAddressLine1" maxlength="50"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Street<span class="errorMessage">*</span></td>
			<td>
				<form:input path="line2" cssClass="flat flat-ext" id="deliveryAddressLine2" maxlength="50"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input path="city" cssClass="flat flat-ext" id="deliveryAddressCity" maxlength="50"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" id="deliveryAddressStateSelect">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext" id="deliveryAddressZipcode" maxlength="12"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<c:set var="saveDisabled" value="" />
				<c:if test="${deliveryAddressModelObject.customer.id == null}">
					<c:set var="saveDisabled" value="disabled" />
				</c:if>
				<input type="button" id="deliveryAddressSubmitBtn" ${saveDisabled} onclick="processDeliveryAddressForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="deliveryAddressBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="deliveryAddressServiceForm" id="deliveryAddressServiceForm" class="tab-color">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" editableInScreen="true" baseObjects="${deliveryAddressList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" 
		dataQualifier="manageDeliveryAddress">
		<transys:textcolumn headerText="Id" dataField="id" />
		<transys:textcolumn headerText="Delivery Address Line1" dataField="line1"/>
		<transys:textcolumn headerText="Delivery Address Line2" dataField="line2" />
		<transys:textcolumn headerText="City" dataField="city" />
		<transys:textcolumn headerText="State" dataField="state.name" />
		<transys:textcolumn headerText="Zipcode" dataField="zipcode" />
	</transys:datatable>
	<%session.setAttribute("manageDeliveryAddressColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
function validate() {
	return true;
};

$("#deliveryAddressServiceForm").find("tr a").click(function() {
    var tableData = $(this).parent().parent().children("td").map(function() {
        return $(this).text();
    }).get();
    
    $("#deliveryAddressForm").find('#id').val($.trim(tableData[0]));
    $("#deliveryAddressForm").find('#deliveryAddressLine1').val($.trim(tableData[1]));
    $("#deliveryAddressForm").find('#deliveryAddressLine2').val($.trim(tableData[2])); 
    $("#deliveryAddressForm").find('#deliveryAddressCity').val($.trim(tableData[3]));
    $("#deliveryAddressForm").find('#deliveryAddressZipcode').val($.trim(tableData[5]));
	$("table.customerDeliveryAddress select option").filter(function() {
	    return this.text == $.trim(tableData[4]); 
	}).attr('selected', true);
});
</script>