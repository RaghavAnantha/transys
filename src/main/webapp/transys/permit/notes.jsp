<%@include file="/common/taglibs.jsp"%>
<form:form action="savePermitNotes.do" name="typeForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="permit" id="permit.id" />
	<table id="form-table" width="100%">
		<tr><td colspan=10></td></tr>
	    <tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<%-- <td class="form-left"><transys:label code="Notes" /></td> --%>
				<form:textarea row="5" id="notesTextArea" path="notes" cssClass="flat" style="width:100%;"/>
			 	<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="create" onclick="return validateform()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Back"/>" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="permit" baseObjects="${notesList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" dataQualifier="managePermitNotes">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" dataFormat="MM/dd/yyy" width="75px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("managePermitNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>