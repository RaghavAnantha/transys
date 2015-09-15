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
				<form:input path="number" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<%-- <td class="form-left"><transys:label code="Order Number" /></td>
			<td align="${left}">
				<form:input path="order.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="order.id" cssClass="errorMessage" />
			</td> --%>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="customer" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<br><form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<%--<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="deliveryAddress.id" cssClass="errorMessage" />
			</td> 
			
			 <td class="form-left"><transys:label code="Delivery Street" /></td>
			<td align="${left}">
				<form:input path="deliveryAddress.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="deliveryAddress.id" cssClass="errorMessage" />
			</td>
		</tr>
		 <tr>
			<td class="form-left"><transys:label code="Permit Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="permitAddress" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permitAddress" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Street" /></td>
			<td align="${left}">
				<form:input path="permit.permitStreet" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.permitStreet" cssClass="errorMessage" />
			</td>
		</tr> --%>
		<tr>
			<td class="form-left"><transys:label code="LocationType" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="locationType" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${locationType}" itemValue="id" itemLabel="locationType" />
				</form:select> 
			 	<br><form:errors path="locationType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="permitClass" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitClass}" itemValue="id" itemLabel="permitClass" />
				</form:select> 
			 	<br><form:errors path="permitClass" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="type" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${type}" itemValue="id" itemLabel="type" />
				</form:select> 
			 	<br><form:errors path="type" cssClass="errorMessage" />
			</td>
		</tr>
		
		<%-- <tr>
			<td align="${left}" class="form-left"><transys:label
					code="Start Date" /></td>
			<td align="${left}" class="wide"><form:input path="startDate" class="flat"
				id="datepicker7" name="startDate" style="width: 163px" /></td>
		</tr> --%>
		
		<%-- <tr> 
			<td class="form-left"><transys:label code="Start Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<td align="${left}" class="wide"><form:input path="startDate" class="flat" id="datepicker" name="startDate" style="width: 163px" />
			 	<br><form:errors path="startDate" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="End Date" /></td>
			<td align="${left}">
				<form:input path="permit.endDate" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="permit.endDate" cssClass="errorMessage" />
			</td>
		</tr> --%>
		<tr>
		<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="fee" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><transys:label code="Parking Meter" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="parkingMeter" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="parkingMeter" cssClass="errorMessage" />
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
	
