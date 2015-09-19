<%@include file="/common/taglibs.jsp"%>
<h5>Add Permit Notes</h5>

<form:form action="savePermitNotes.do" name="typeForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="permit.id" id="permit.id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr>
			<td colspan=10>
				<%-- <td align="${left}" class="form-left"><transys:label code="Notes" /></td> --%>
				<form:textarea row="5" id="notesTextArea" path="notes" cssClass="flat" style="width:100%;"/>
			 	<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validateform()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="permit" insertable="true" baseObjects="${notesList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false">
		<transys:textcolumn headerText="Entered By" dataField="createdBy" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" />
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>