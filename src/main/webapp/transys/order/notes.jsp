<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateOrderNotesForm() {
	var missingData = validateOrderNotesrMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	var formatValidation = validateOrderNotesDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span style='color:red'><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateOrderNotesrMissingData() {
	var missingData = "";
	
	if ($('#orderNotesTabNotes').val() == "") {
		missingData += "Notes, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function validateOrderNotesDataFormat() {
	var validationMsg = "";
	
	var notes = $('#orderNotesTabNotes').val();
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

function processOrderNotesForm() {
	if (validateOrderNotesForm()) {
		var orderNotesForm = $("#orderNotesForm");
		orderNotesForm.submit();
	}
}

function processOrderNotesDelete(orderNotesId) {
	if (!confirm("Do you want to Delete Order Notes # " + orderNotesId + "?")) {
		return;
	}
	
	document.location = "${ctx}/order/deleteOrderNotes.do?orderNotesId=" + orderNotesId;
}
</script>

<form:form action="saveOrderNotes.do" name="orderNotesForm" id="orderNotesForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="order.id" id="order.id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageOrderNotes" />
		<jsp:param name="errorCtx" value="manageOrderNotes" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Order #</td>
			<td class="td-static">${notesModelObject.order.id}</td>
		</tr>
		<tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" id="orderNotesTabNotes" path="notes" cssClass="flat notes"/>
				<br><form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<c:set var="saveDisabled" value="" />
				<c:if test="${notesModelObject.order.id == null}">
					<c:set var="saveDisabled" value="disabled" />
				</c:if>
				<input type="button" id="orderNotesCreate" ${saveDisabled} onclick="processOrderNotesForm();" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="orderNotesBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderNotesDatatable" id="orderNotesDatatable" class="tab-color">
	<transys:datatable urlContext="order" baseObjects="${notesList}"
		searchCriteria="<%=null%>" cellPadding="2" searcheable="false" editable="true" editableInScreen="true"
		dataQualifier="manageOrderNotes">
		<transys:textcolumn headerText="Id" dataField="id" />
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px" />
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" dataFormat="MM/dd/yyy" width="75px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
		<transys:imagecolumn headerText="DEL" linkUrl="javascript:processOrderNotesDelete('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center" width="35px"/>
	</transys:datatable>
	<%session.setAttribute("manageOrderNotesColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
$("#orderNotesDatatable").find("tr a").click(function() {
    var tableData = $(this).parent().parent().children("td").map(function() {
        return $(this).text();
    }).get();
    
    var notesText = $.trim(tableData[3]);
    if (notesText.indexOf("AUDIT") != -1) {
    	showAlertDialog("Data Validation", "Audit notes cannot be edited");
    	return;
    }
    
    $("#orderNotesForm").find('#id').val($.trim(tableData[0]));
    $("#orderNotesForm").find('#orderNotesTabNotes').val(notesText);
});
</script>