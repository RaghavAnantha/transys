<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">

function processPermitNotesForm() {
	if (validateNotesForm()) {
		var permitNotesEditForm = $("#permitNotesForm");
		permitNotesEditForm.submit();
		return true;
	} else {
		return false;
	}
}

function validateNotesForm() {
	
	var missingData = validatePermitNotesMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validatePermitNotesDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validatePermitNotesMissingData() {
	var missingData = "";
	
	if ($('#notesTextArea').val() == "") {
		missingData += "Permit Notes, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validatePermitNotesDataFormat() {
	var validationMsg = "";
	
	validationMsg += validatePermitNotesText();
	
	if (validationMsg != "") {
		validationMsg = validationMsg.substring(0, validationMsg.length - 2);
	}
	
	return validationMsg;
}

function validatePermitNotesText() {
	var validationMsg = "";
	
	var notes = $('#notesTextArea').val();
	if (notes != "") {
		if (!validateText(notes, 500)) {
			validationMsg += "Notes, "
		}
	}
	
	return validationMsg;
}

</script>

<form:form  id="permitNotesForm" action="savePermitNotes.do" name="typeForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="permit.id" id="permit.id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="savePermitNotes" />
	</jsp:include>
	<table id="form-table" width="100%">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Permit Number</td>
			<td class="td-static">${notesModelObject.permit.number}</td>
		</tr>
	    <tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" id="notesTextArea" path="notes" cssClass="flat notes"/>
			 	<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
			<c:set var="permitNotesSaveDisabled" value="" />
				<c:if test="${notesModelObject.permit.id == null}">
					<c:set var="permitNotesSaveDisabled" value="disabled" />
			</c:if>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="button" id="create" ${permitNotesSaveDisabled} onclick="return processPermitNotesForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="permitNotesDatatable" id="permitNotesDatatable" class="tab-color">
	<transys:datatable urlContext="permit" baseObjects="${notesList}"
		searchCriteria="<%=null%>" cellPadding="2" searcheable="false" dataQualifier="managePermitNotes">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" dataFormat="MM/dd/yyy hh:mm:ss" width="140px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("managePermitNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>