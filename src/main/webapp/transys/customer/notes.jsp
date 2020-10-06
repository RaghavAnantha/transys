<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateCustomerNotesForm() {
	var missingData = validateCustomerNotesrMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateCustomerNotesDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateCustomerNotesrMissingData() {
	var missingData = "";
	
	if ($('#customerNotesTabNotes').val() == "") {
		missingData += "Notes, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateCustomerNotesDataFormat() {
	var validationMsg = "";
	
	var notes = $('#customerNotesTabNotes').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	return validationMsg;
}

function processCustomerNotesForm() {
	if (validateCustomerNotesForm()) {
		var customerNotesForm = $("#customerNotesForm");
		customerNotesForm.submit();
	}
}
</script>

<form:form action="saveCustomerNotes.do" name="customerNotesForm" id="customerNotesForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="customer.id" id="customer.id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCustomerNotes" />
		<jsp:param name="errorCtx" value="manageCustomerNotes" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Customer Id</td>
			<td class="td-static">${notesModelObject.customer.id}</td>
		</tr>
		<tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" id="customerNotesTabNotes" path="notes" cssClass="flat notes" maxlength="500"/>
				<form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<c:set var="saveDisabled" value="" />
				<c:if test="${notesModelObject.customer.id == null}">
					<c:set var="saveDisabled" value="disabled" />
				</c:if>
				<input type="button" id="customerNotesCreate" ${saveDisabled} onclick="processCustomerNotesForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="customerNotesBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="customerNotesDatatable" id="customerNotesDatatable" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${notesList}"
		searchCriteria="<%=null%>" cellPadding="2" searcheable="false" dataQualifier="manageCustomerNotes">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" dataFormat="MM/dd/yyyy HH:mm:ss" width="140px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("manageCustomerNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>