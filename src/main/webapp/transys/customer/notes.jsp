<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function validateForm() {
	return true;
}
</script>

<form:form action="saveCustomerNotes.do" name="customerNotesForm" id="customerNotesForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="customer.id" id="customer.id" />
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" id="notesTabNotes" path="notes" cssClass="flat notes"/>
				<form:errors path="notes" cssClass="errorMessage" />
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

<form:form name="customerNotesDatatable" id="customerNotesDatatable" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${notesList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" dataQualifier="manageCustomerNotes">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" dataFormat="MM/dd/yyy" width="75px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("manageCustomerNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>