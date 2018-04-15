<%@include file="/common/taglibs.jsp"%>

<form:form action="${ctx}/order/saveOrderNotesModal.do" name="orderNotesModalForm" id="orderNotesModalForm" commandName="notesModelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="order.id" id="order.id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Order #</td>
			<td class="td-static">${notesModelObject.order.id}</td>
		</tr>
		<tr><td class="form-left">Notes<span class="errorMessage">*</span></td></tr>
		<tr>
			<td colspan=10>
				<form:textarea row="10" id="orderNotesModalNotes" path="notes" cssClass="flat notes" maxlength="500"/>
				<form:errors path="notes" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="submitOrderNotes" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="closeOrderNotes" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderNotesModalDatatable" id="orderNotesModalDatatable" class="tab-color">
	<transys:datatable urlContext="order" baseObjects="${notesList}"
		searchCriteria="<%=null%>" cellPadding="2"
		searcheable="false" dataQualifier="manageOrderNotesModal">
		<transys:textcolumn headerText="Entered By" dataField="enteredBy" width="150px"/>
		<transys:textcolumn headerText="Date/Time" dataField="createdAt" width="75px" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="notes" />
	</transys:datatable>
	<%session.setAttribute("manageOrderNotesModalColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<script type="text/javascript">
function validateOrderNotesModalForm() {
	var missingData = validateOrderNotesModalMissingData();
	if (missingData != "") {
		var alertMsg = "<span><b>Please provide following required data:</b><br></span>"
					 + missingData;
		displayOrderNotesModalErrorMessage(alertMsg);
		
		return false;
	}
	
	var formatValidation = validateOrderNotesModalDataFormat();
	if (formatValidation != "") {
		var alertMsg = "<span><b>Please correct following invalid data:</b><br></span>"
					 + formatValidation;
		displayOrderNotesModalErrorMessage(alertMsg);
		
		return false;
	}
	
	return true;
}

function validateOrderNotesModalMissingData() {
	var missingData = "";
	
	if ($('#orderNotesModalNotes').val() == "") {
		missingData += "Notes, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	
	return missingData;
}

function validateOrderNotesModalDataFormat() {
	var validationMsg = "";
	
	var notes = $('#orderNotesModalNotes').val();
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

$("#orderNotesModalForm").submit(function (ev) {
	var $this = $(this);
	
	clearOrderNotestModalMessages();
	
	if (!validateOrderNotesModalForm()) {
		return false;
	}
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		displayOrderNotesModalErrorMessage(responseData.replace("ErrorMsg: ", ""));
        	} else {
        		$this.find("#closeOrderNotes").click();
        	}
        }
    });
    
    ev.preventDefault();
});
</script>