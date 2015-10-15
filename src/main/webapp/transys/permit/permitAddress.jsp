<%@include file="/common/taglibs.jsp"%>
<h5>Add Permit Address</h5>

<form:form action="savePermitAddress.do" name="typeForm" commandName="permitAddressModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="permit.id" id="permit.id" />
	<table id="form-table" class="table delivery">
		<tr>
			<td class="form-left"><transys:label code="Permit Address #" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line1" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Street" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line2" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" onchange="checkVal(this.id)">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat"  onkeyup="checkValDrop(this.id, this.value)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validateForm()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="permit" deletable="true"
		editable="true" editableInScreen="true" baseObjects="${permitAddressList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" dataQualifier="manageNotes">
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
	
	$("tr a").click(function() {	
		var ids = ["line1", "line2", "city", "zipcode", "state"];
		
		for (var i= 0; i<ids.length; i++) {		
			$("table.delivery").find('#'+ids[i]).removeClass("border");	
		}
		
	    var tableData = $(this).parent().parent().children("td").map(function() {
	        return $(this).text();
	    }).get();
	    
	    $('#line1').val($.trim(tableData[0]));
	    $('#line2').val($.trim(tableData[1])); 
	    $('table.delivery').find('#city').val($.trim(tableData[2]));
	    $('table.delivery').find('#zipcode').val($.trim(tableData[4]));
		$("table.delivery select option").filter(function() {
		    return this.text == $.trim(tableData[3]); 
		}).attr('selected', true);
	});
</script>