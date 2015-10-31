<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function validateAndFormatPhoneModal(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if (phone == "") {
		return;
	}
	
	if (phone.length < 10  || phone.length > 12
			|| (phone.length > 10 && !phone.match("-"))) {
		var alertMsg = "Invalid Phone Number";
		alert(alertMsg);
		
		document.getElementById(phoneId).value = "";
		return false;
	} else {
		var formattedPhone = formatPhone(phone);
		document.getElementById(phoneId).value = formattedPhone;
	}	
}

$("#customerModalForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		displayPopupDialogErrorMessage(responseData);
        	} else {
        		var customer = jQuery.parseJSON(responseData);
        		
        		$("#tdCustomerId").html(customer.id);
        		$("#tdCustomerCreationDt").html(customer.formattedCreatedAt);
        		
        		displayPopupDialogSuccessMessage("Customer saved successfully");
        		appendCustomer(customer);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>

<form:form action="/customer/saveModal.do" name="customerModalForm" commandName="modelObject" method="post" id="customerModalForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Company Name<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="companyName" cssClass="flat flat-ext"  />
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			<td class="form-left">Customer Id</td>
			<td class="td-static" id="tdCustomerId">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Customer Type<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customerType" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerTypes}" itemValue="id" itemLabel="customerType"/>
				</form:select> 
				<form:errors path="customerType" cssClass="errorMessage" />
			</td>
			<td class="form-left">Created Date</td>
			<td class="td-static" id="tdCustomerCreationDt">
				<c:if test="${modelObject.id != null}">
					${modelObject.formattedCreatedAt}
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="form-left">Charge Company<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="chargeCompany" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${chargeCompanyOptions}" />
				</form:select> 
				<form:errors path="chargeCompany" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Status<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customerStatus" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerStatuses}" itemValue="id" itemLabel="status"/>
				</form:select> 
				<form:errors path="customerStatus" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Billing Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Address Line1<span class="errorMessage">*</span></td>
			<td>
				<form:input path="billingAddressLine1" cssClass="flat flat-ext"/>
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Address Line2</td>
			<td>
				<form:input path="billingAddressLine2" cssClass="flat flat-ext" />
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td>
				<form:input cssClass="flat flat-ext" path="city" />
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext"  maxlength="5" />
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" path="state" style="width:172px !important">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Billing Contact</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
		<td class="form-left">Contact Name<span class="errorMessage">*</span></td>
		<td>
			<form:input path="contactName" cssClass="flat flat-ext" />	 	
		</td>
		<td class="form-left">Alt Phone1</td>
			<td>
				<form:input path="altPhone1" cssClass="flat flat-ext" maxlength="12" 
					id="altPhone1" onblur="return validateAndFormatPhoneModal('altPhone1');"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone<span class="errorMessage">*</span></td>
			<td>
				<form:input path="phone" cssClass="flat flat-ext" maxlength="12" 
					id="phone" onblur="return validateAndFormatPhoneModal('phone');"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left">Alt Phone2</td>
			<td>
				<form:input path="altPhone2" cssClass="flat flat-ext" maxlength="12" 
					id="altPhone2" onblur="return validateAndFormatPhoneModal('altPhone2');"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Email<span class="errorMessage">*</span></td>
			<td>
				<form:input path="email" cssClass="flat flat-ext" id="email" />
				<br><form:errors path="email" cssClass="errorMessage" />
			</td>
			<td class="form-left">Fax</td>
			<td>
				<form:input path="fax" cssClass="flat flat-ext" maxlength="12" 
					id="fax" onblur="return validateAndFormatPhoneModal('fax');"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Delivery Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Delivery Address #<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress[0].line1" cssClass="flat flat-ext"/>
			 	<br><form:errors path="deliveryAddress[0].line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Delivery Street<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress[0].line2" cssClass="flat flat-ext"/>
			 	<br><form:errors path="deliveryAddress[0].line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress[0].city" cssClass="flat flat-ext"/>
			 	<br><form:errors path="deliveryAddress[0].city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="deliveryAddress[0].state">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="deliveryAddress[0].state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress[0].zipcode" cssClass="flat flat-ext"/>
			 	<br><form:errors path="deliveryAddress[0].zipcode" cssClass="errorMessage" />
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
				<form:textarea row="5" path="customerNotes[0].notes" cssClass="flat" id="customerNotes" style="width:100%; height:100%;"/>
				<form:errors path="customerNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>