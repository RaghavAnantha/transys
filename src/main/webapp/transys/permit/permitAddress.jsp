<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	function processPermitAddressForm() {
		if (validatePermitAddressForm()) {
			var permitAddressEditForm = $("#permitAddressForm");
			permitAddressEditForm.submit();
			return true;
		} else {
			return false;
		}
	}

	function validatePermitAddressForm() {
		var missingData = validatePermitAddressMissingData();
		if (missingData != "") {
			var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
						 + missingData;
			showAlertDialog("Data Validation", alertMsg);
			
			return false;
		}
		
		var formatValidation = validatePermitAddressDataFormat();
		if (formatValidation != "") {
			var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
						 + formatValidation;
			showAlertDialog("Data Validation", alertMsg);
			
			return false;
		}
		
		return true;
	}
	
	function validatePermitAddressMissingData() {
		var missingData = "";
		
		if ($('#permitAddressFormLine1').val() == "") {
			missingData += "Permit Address #, "
		}
		if ($('#permitAddressFormLine2').val() == "") {
			missingData += "Permit Street, "
		}
		if ($('#permitAddressFormCity').val() == "") {
			missingData += "City, "
		}
		if ($('#permitAddressFormState').val() == "") {
			missingData += "State, "
		}
		if ($("#permitAddressFormZipcode").val() == "") {
			missingData += "Zipcode, "
		}
		
		if (missingData != "") {
			missingData = missingData.substring(0, missingData.length - 2);
		}
		
		return missingData;
	}
	
	function validatePermitAddressDataFormat() {
		var validationMsg = "";
		
		validationMsg += validatePermitAddressZipCode();
		
		if (validationMsg != "") {
			validationMsg = validationMsg.substring(0, validationMsg.length - 2);
		}
		return validationMsg;
	}
	
	function validatePermitAddressZipCode() {
		var validationMsg = "";
		
		var zipcode = $("#permitAddressFormZipcode").val();
		if (zipcode != "") {
			var chk = validateZipCode(zipcode);
			
			if (!validateZipCode(zipcode)) {
				validationMsg += "Zipcode, "
			}
		}
		
		return validationMsg;
	}
</script>

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
				<form:input id="permitAddressFormLine1" path="line1" cssClass="flat flat-ext" />
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Street" /><span class="errorMessage">*</span></td>
			<td>
				<form:input id="permitAddressFormLine2" path="line2" cssClass="flat flat-ext" />
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td>
				<form:input id="permitAddressFormCity" path="city" cssClass="flat flat-ext" />
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">State<span class="errorMessage">*</span></td>
			<td>
				<form:select id="permitAddressFormState" cssClass="flat form-control input-sm" style="width: 174px !important" path="state" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Zipcode<span class="errorMessage">*</span></td>
			<td>
				<form:input id="permitAddressFormZipcode" path="zipcode" cssClass="flat flat-ext" />
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<c:set var="permitAddressSaveDisabled" value="" />
				<c:if test="${permitAddressModelObject.permit.id == null}">
					<c:set var="permitAddressSaveDisabled" value="disabled" />
			</c:if>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" ${permitAddressSaveDisabled} onclick="return processPermitAddressForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
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
	$("#permitAddressServiceForm").find("tr a").click(function() {	
		var tableData = $(this).parent().parent().children("td").map(function() {
	        return $(this).text();
	    }).get();
	    
	    $("#permitAddressForm").find('#id').val($.trim(tableData[0]));
	    $("#permitAddressForm").find('#permitAddressFormLine1').val($.trim(tableData[1]));
	    $("#permitAddressForm").find('#permitAddressFormLine2').val($.trim(tableData[2])); 
	    $("#permitAddressForm").find('#permitAddressFormCity').val($.trim(tableData[3]));
	    $("#permitAddressForm").find('#permitAddressFormZipcode').val($.trim(tableData[5]));
		$("table.editPermitAddress select option").filter(function() {
		    return this.text == $.trim(tableData[4]); 
		}).attr('selected', true);
	});
</script>

