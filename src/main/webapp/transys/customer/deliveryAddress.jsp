<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function validate() {
	return true;
};
</script>
<br />
<form:form action="saveDeliveryAddress.do" name="typeForm" commandName="deliveryAddressModelObject" method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Customer ID" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="customer.id" cssClass="flat"  />
			 	<br><form:errors path="customer.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line1" cssClass="flat"  />
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Line 2" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line2" cssClass="flat"  />
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" cssClass="flat"  />
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" path="state" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Zip" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat"  />
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="deliveryAddressServiceForm" id="deliveryAddressServiceForm" class="tab-color">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" insertable="true" baseObjects="${deliveryAddressList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Delivery Address" dataField="line1" />
		<transys:textcolumn headerText="Address Line 2" dataField="line2" />
		<transys:textcolumn headerText="City" dataField="city" />
		<transys:textcolumn headerText="State" dataField="state.name" />
		<transys:textcolumn headerText="Zipcode" dataField="zipcode" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>