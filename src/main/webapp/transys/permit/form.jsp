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
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><transys:label code="Add/Edit Permit"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.number" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.number" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Order Number" /></td>
			<td align="${left}">
				<form:input path="order.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="order.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.customer.companyName" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.customer.companyName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.deliveryAddress.line1" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.deliveryAddress.line1" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Delivery Street" /></td>
			<td align="${left}">
				<form:input path="permit.deliveryAddress.line2" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.deliveryAddress.line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.permitAddress" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.permitAddress" cssClass="errorMessage" />
			</td>
			
			<%-- <td class="form-left"><transys:label code="Permit Street" /></td>
			<td align="${left}">
				<form:input path="permit.permitStreet" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.permitStreet" cssClass="errorMessage" />
			</td> --%>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.locationType.locationType" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.locationType.locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.permitClass.permitClass" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.permitClass.permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:input path="permit.type.type" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.type.type" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Start Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.startDate" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.startDate" cssClass="errorMessage" />
			</td>
			
			<%-- <td class="form-left"><transys:label code="End Date" /></td>
			<td align="${left}">
				<form:input path="permit.endDate" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.endDate" cssClass="errorMessage" />
			</td> --%>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.fee" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permit.parkingMeter" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.parkingMeter" cssClass="errorMessage" />
			</td>
		</tr>
	   
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit"  id="create" onclick="return true" value="<transys:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
