<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateForm() {
	return true;
}

$("#orderNotesModalForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	$this.find("#closeOrderNotes").click();
        }
    });
    
    ev.preventDefault();
});
</script>

<form:form action="/order/saveOrderNotesModal.do" name="orderNotesModalForm" id="orderNotesModalForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="order.id" id="order.id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr>
			<td colspan=10>
				<form:textarea row="5" id="notesTabNotes" path="notes" cssClass="flat" style="width:100%;"/>
				<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="submitOrderNotes" onclick="return validateForm()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="closeOrderNotes" value="Close" class="flat btn btn-primary btn-sm" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderNotesServiceForm" id="orderNotesServiceForm" class="tab-color">
	<transys:datatable urlContext="order" baseObjects="${notesList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" dataQualifier="manageNotes">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" />
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("manageNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>