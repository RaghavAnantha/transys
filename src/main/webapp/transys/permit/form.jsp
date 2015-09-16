<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function populateDeliveryAddress() {
   var customer = document.getElementById("customerSelect").value;
   alert("data " + customer);/*  */
   //do ajax now
   $.ajax({
  	 url: "customerDeliveryAddress.do?customer=" + customer,
       type: "GET",
       success: function(responseData, textStatus, jqXHR) {
    	   var ccustomer = ${chosenCustomer}
           alert("Data for customer id " + ccustomer.id);
       }
   }); 
 }

</script>
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
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
				<form:select id="customerSelect" cssClass="flat form-control input-sm" path="customer" > <!-- onChange="return populateDeliveryAddress();"> -->
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customer}" itemValue="id" itemLabel="companyName" />
				</form:select> 
			 	<br><form:errors path="customer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="deliveryAddress.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="deliveryAddress.id" cssClass="errorMessage" />
			</td> 
			
			<%-- <td class="form-left"><transys:label code="Delivery Street" /></td>
			<td align="${left}">
				<form:input path="deliveryAddress.id" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="deliveryAddress.id" cssClass="errorMessage" />
			</td> --%>
		</tr>
		 <%--<tr>
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
					<form:options items="${permitType}" itemValue="id" itemLabel="type" />
				</form:select> 
			 	<br><form:errors path="type" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Start Date" /></td>
			<td align="${left}" class="wide"><form:input path="startDate" class="flat"
				id="datepicker7" name="startDate" style="width: 163px" /></td>
		</tr>
		
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
	
