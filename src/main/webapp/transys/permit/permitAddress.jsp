<%@include file="/common/taglibs.jsp"%>
<form:form id="permitAddressForm" action="savePermitAddress.do" name="permitAddressForm" commandName="permitAddressModelObject" method="post">
	<form:hidden path="id" />
	<form:hidden path="permit.id" id="permit.id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="savePermitAddress" />
	</jsp:include>
	<table id="form-table" class="table editPermitAddress">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Permit Address #<span class="errorMessage">*</span></td>
			<td>
				<form:input path="line1" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Street" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="line2" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="city" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" onchange="checkVal(this.id)">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input path="zipcode" cssClass="flat flat-ext"  onkeyup="checkValDrop(this.id, this.value)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="create" onclick="return validateForm()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="permitAddressServiceForm" id="permitAddressServiceForm" class="tab-color">
	<transys:datatable urlContext="permit" deletable="true"
		editable="true" editableInScreen="true" baseObjects="${permitAddressList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" dataQualifier="manageNotes">
		<transys:textcolumn headerText="Id" dataField="id" width="50px"/>
		<transys:textcolumn headerText="Address #" dataField="line1" />
		<transys:textcolumn headerText="Address Street" dataField="line2" />
		<transys:textcolumn headerText="City" dataField="city" />
		<transys:textcolumn headerText="State" dataField="state.name" />
		<transys:textcolumn headerText="Zipcode" dataField="zipcode" />
	</transys:datatable>
	<%session.setAttribute("managePermitAddressColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
	function validate() {
		return true;
	};
	
	$("#permitAddressServiceForm").find("tr a").click(function() {	
		var ids = ["id", "line1", "line2", "city", "zipcode", "state"];
		
		for (var i= 0; i<ids.length; i++) {		
			$("table.editPermitAddress").find('#'+ids[i]).removeClass("border");	
		}
		
	    var tableData = $(this).parent().parent().children("td").map(function() {
	        return $(this).text();
	    }).get();
	    
	    $("#permitAddressForm").find('#id').val($.trim(tableData[0]));
	    $("#permitAddressForm").find('#line1').val($.trim(tableData[1]));
	    $("#permitAddressForm").find('#line2').val($.trim(tableData[2])); 
	    $('table.editPermitAddress').find('#city').val($.trim(tableData[3]));
	    $('table.editPermitAddress').find('#zipcode').val($.trim(tableData[5]));
		$("table.editPermitAddress select option").filter(function() {
		    return this.text == $.trim(tableData[4]); 
		}).attr('selected', true);
	});
</script>