<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function formatPhone(){	
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

function formatFax(){
	var fax = document.getElementById("fax").value;
	if(fax != ""){
		if(fax.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("fax").value = "";
			return true;
		}
		else{
			var str = new String(fax);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var fax = p1 + "-" + p2 + "-" + p3;
				document.getElementById("fax").value = fax;
			}
		}
	}	
}	
</script>
<form:form action="save.do" name="customerForm" id="customerForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr><td colspan="10"></td></tr>
		<tr>
			<td class="form-left">Company Name<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="companyName" cssClass="flat flat-ext" />
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Customer ID" /></td>
			<td align="${left}">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Type" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customerType" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customerTypes}" itemValue="id" itemLabel="customerType"/>
				</form:select> 
				<br><form:errors path="customerType" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Created Date" /></td>
			<td align="${left}">${modelObject.formattedCreatedAt}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Charge Company" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="chargeCompany" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${chargeCompanyOptions}" />
				</form:select> 
				<br><form:errors path="chargeCompany" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Total Orders" /></td>
			<td align="${left}"></td>
		</tr>
		<tr>
			<td class="form-left">Status</td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customerStatus" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customerStatuses}" itemValue="id" itemLabel="status" />
				</form:select> 
				<br><form:errors path="customerStatus" cssClass="errorMessage" />
			</td> 
			<td class="form-left"><transys:label code="Last Delivery" /></td>
			<td align="${left}"></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Billing Address</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Address Line1"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="billingAddressLine1" cssClass="flat flat-ext"/>
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Address Line2"/></td>
			<td align="${left}">
				<form:input path="billingAddressLine2" cssClass="flat flat-ext" />
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input cssClass="flat flat-ext" path="city" />
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat flat-ext"  maxlength="5" onkeypress="return onlyNumbers(event, false)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="state" style="width:172px !important">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>	
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Billing Contact</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>	
		<tr>
		<td class="form-left"><transys:label code="Contact Name" /><span class="errorMessage"></span></td>
		<td align="${left}">
			<form:input path="contactName" cssClass="flat flat-ext" />	 	
		</td>
		<td class="form-left"><transys:label code="Alt Phone1" /></td>
			<td align="${left}">
				<form:input path="altPhone1" cssClass="flat flat-ext"  maxlength="12" 
					id="altPhone1" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" cssClass="flat flat-ext"  maxlength="12" 
					id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Alt Phone2" /></td>
			<td align="${left}">
				<form:input path="altPhone2" cssClass="flat flat-ext"  maxlength="12" 
					id="altPhone2" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Email" /></td>
			<td align="${left}">
				<form:input path="email" cssClass="flat flat-ext" id="email" />
				 <br><form:errors path="email" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Fax" /></td>
			<td align="${left}">
				<form:input path="fax" cssClass="flat flat-ext" maxlength="12" 
					id="fax" onkeypress="return onlyNumbers(event, false)" onblur="return formatFax();"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="customerNotes[0].notes" cssClass="flat" id="customerNotes" style="width:100%; height:150%;"/>
				<br><form:errors path="customerNotes[0].notes" cssClass="errorMessage" />
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
		<tr><td colspan="2"></td></tr>
	</table>
</form:form>