<%@include file="/common/taglibs.jsp"%>
<%@include file="/common/modal.jsp"%>

<script type="text/javascript">
function validateAndFormatPhone(phoneId) {	
	var phone = document.getElementById(phoneId).value;
	if (phone == "") {
		return;
	}
	
	if (phone.length < 10  || phone.length > 12 
			|| (phone.length > 10 && !phone.match("-"))) {
		var alertMsg = "<p>Invalid Phone/Fax Number.</p>";
		showAlertDialog("Data validation", alertMsg);
		
		document.getElementById(phoneId).value = "";
		return false;
	} else {
		var formattedPhone = formatPhone(phone);
		document.getElementById(phoneId).value = formattedPhone;
	}	
}

function formatPhone(phone) {
	if (phone.length < 10) {
		return phone;
	}
	
	var str = new String(phone);
	if (str.match("-")) {
		return phone;
	}
	
	var p1 = str.substring(0,3);
	var p2 = str.substring(3,6);
	var p3 = str.substring(6,10);				
	return p1 + "-" + p2 + "-" + p3;
}
</script>
<form:form action="save.do" name="customerForm" id="customerForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCustomer" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left">Company Name<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="companyName" cssClass="flat flat-ext" />
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			<td class="form-left">Customer Id</td>
			<td class="td-static">${modelObject.id}</td>
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
			<td class="td-static">
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
			<td class="form-left">Total Orders</td>
			<td class="td-static">
				<c:if test="${totalOrders != null}">
					${totalOrders}
				</c:if>
			</td>
		</tr>
		<tr>
			<td class="form-left">Status<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customerStatus" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${customerStatuses}" itemValue="id" itemLabel="status" />
				</form:select> 
				<form:errors path="customerStatus" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Last Delivery" /></td>
			<td class="td-static">
				<c:if test="${formattedDeliveryDate != null}">
					${formattedDeliveryDate}
				</c:if>
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
			<td align="${left}">
				<form:input path="billingAddressLine1" cssClass="flat flat-ext"/>
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left">Address Line2</td>
			<td align="${left}">
				<form:input path="billingAddressLine2" cssClass="flat flat-ext" />
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input cssClass="flat flat-ext" path="city" />
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat flat-ext"  maxlength="5" onkeypress="return onlyNumbers(event, false)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td align="${left}">
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
		<td align="${left}">
			<form:input path="contactName" cssClass="flat flat-ext" />	 	
		</td>
		<td class="form-left">Alt Phone1</td>
			<td align="${left}">
				<form:input path="altPhone1" cssClass="flat flat-ext"  maxlength="12" 
					id="altPhone1" onblur="return validateAndFormatPhone('altPhone1');"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone<span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="phone" cssClass="flat flat-ext"  maxlength="12" 
					id="phone" onblur="return validateAndFormatPhone('phone');"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left">Alt Phone2</td>
			<td align="${left}">
				<form:input path="altPhone2" cssClass="flat flat-ext"  maxlength="12" 
					id="altPhone2" onblur="return validateAndFormatPhone('altPhone2');"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Email</td>
			<td align="${left}">
				<form:input path="email" cssClass="flat flat-ext" id="email" />
				 <br><form:errors path="email" cssClass="errorMessage" />
			</td>
			<td class="form-left">Fax</td>
			<td align="${left}">
				<form:input path="fax" cssClass="flat flat-ext" maxlength="12" 
					id="fax" onblur="return validateAndFormatPhone('fax');"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
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
				<c:set var="customerNotesDisabled" value="" />
				<c:if test="${modelObject.customerNotes != null and modelObject.customerNotes.size() > 0 
							and modelObject.customerNotes[0].notes != null and modelObject.customerNotes[0].notes.length() > 0}">
					<c:set var="customerNotesDisabled" value="true" />
				</c:if>
				<form:textarea row="5" readonly="${customerNotesDisabled}" path="customerNotes[0].notes" cssClass="form-control" id="customerNotes" style="width:100%; height:100%;"/>
				<form:errors path="customerNotes[0].notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr><td colspan="2"></td></tr>
	</table>
</form:form>