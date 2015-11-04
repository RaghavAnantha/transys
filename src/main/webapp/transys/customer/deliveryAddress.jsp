<%@include file="/common/taglibs.jsp"%>

<form:form id="editDeliveryAddressForm" action="saveDeliveryAddress.do" name="typeForm" commandName="deliveryAddressModelObject" method="post" >
	<form:hidden path="id" />
	<form:hidden path="customer.id" id="customerId" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCustomerDeliveryAddress" />
		<jsp:param name="errorCtx" value="manageCustomerDeliveryAddress" />
	</jsp:include>
	<table id="form-table" class="table customerDeliveryAddress">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Customer ID" /></td>
			<td class="td-static">${deliveryAddressModelObject.customer.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address #" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line1" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Street" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line2" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" onchange="checkVal(this.id)">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat flat-ext"  onkeyup="checkValDrop(this.id, this.value)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validateForm()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
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
		<transys:textcolumn headerText="Delivery Address #" dataField="line1" width="150px"/>
		<transys:textcolumn headerText="Delivery Street" dataField="line2" />
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
	var ids = ["id", "line1", "line2", "city", "zipcode", "state"];
	
	for (var i= 0; i<ids.length; i++) {		
		$("table.customerDeliveryAddress").find('#'+ids[i]).removeClass("border");	
	}
	
    var tableData = $(this).parent().parent().children("td").map(function() {
        return $(this).text();
    }).get();
    
    $("#editDeliveryAddressForm").find('#id').val($.trim(tableData[0]));
    $("#editDeliveryAddressForm").find('#line1').val($.trim(tableData[1]));
    $("#editDeliveryAddressForm").find('#line2').val($.trim(tableData[2])); 
    $("#editDeliveryAddressForm").find('#city').val($.trim(tableData[3]));
    $("#editDeliveryAddressForm").find('#zipcode').val($.trim(tableData[5]));
	$("table.customerDeliveryAddress select option").filter(function() {
	    return this.text == $.trim(tableData[4]); 
	}).attr('selected', true);
});

function checkVal(id) {
	$("table.customerDeliveryAddress").find('#'+id).removeClass("border");
}

function checkValDrop(id, val) {
	if (val != "") {
		$("table.customerDeliveryAddress").find('#'+id).removeClass("border");
	}
}

function validateForm() {
	var ids = ["line1", "line2", "city", "zipcode", "state"];
	var bool = false	
	for (var i = 0; i < ids.length; i++) {		
		if ($("table.customerDeliveryAddress").find('#'+ids[i]).val().length == 0 ) {	
			
			$("table.customerDeliveryAddress").find('#'+ids[i]).addClass("border");
			bool = true;
		}	
	} 
	
	if (bool) {
		return false;
	}
	
	return true;
};

</script>