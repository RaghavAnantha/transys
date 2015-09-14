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
<br/>
<form:form action="save.do" name="typeForm"  commandName="modelObject" method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Company Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="companyName" cssClass="flat"  />
			 	<br><form:errors path="companyName" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Customer ID" /></td>
			<td align="${left}">
				<form:input path="id" cssClass="flat"/>
			 	
			</td>
		</tr>
		
		<tr>
		<td class="form-left"><transys:label code="Status" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="status" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${status}" itemValue="id" itemLabel="name" />
				</form:select> 
			 	<br><form:errors path="status" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: #337AB7;">Billing Address</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Address Line1"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="billingAddressLine1" cssClass="flat"/>
				 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Address Line2"/></td>
			<td align="${left}">
				<form:input path="billingAddressLine2" cssClass="flat" />
				 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input cssClass="flat" path="city" />
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat"  maxlength="5"
					 onkeypress="return onlyNumbers(event, false)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span	class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="state" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>	
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: #337AB7;">Billing Contact</td>
		</tr>	
		<tr>
		<td class="form-left"><transys:label code="Contact Name" /><span class="errorMessage"></span></td>
		<td align="${left}">
				<form:input path="contactName" cssClass="flat" />	 	
			</td>
		<td class="form-left"><transys:label code="Alt Phone1" /></td>
			<td align="${left}">
				<form:input path="altPhone1" cssClass="flat"  maxlength="12" 
					id="altPhone1" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="altPhone1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" cssClass="flat"  maxlength="12" 
					id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			
			
	<!--  		<td class="form-left"><transys:label code="Alt Phone2" /></td>
			<td align="${left}">
				<form:input path="altPhone2" cssClass="flat"  maxlength="12" 
					id="altPhone2" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="altPhone2" cssClass="errorMessage" />
			</td> -->
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Fax" /></td>
			<td align="${left}">
				<form:input path="fax" cssClass="flat"	 maxlength="12" 
					id="fax" onkeypress="return onlyNumbers(event, false)" onblur="return formatFax();"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: #337AB7;">Notes/Comments</td>
		</tr>
		<tr>
		<td colspan=10><form:textarea row="5" path="notes" cssClass="flat"	 id="notes" style="width:100%; height:150%;"/>
				<br><form:errors path="notes" cssClass="errorMessage" />
		</td>
		
		</tr>
	   
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit"  id="create" onclick="return validations()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>