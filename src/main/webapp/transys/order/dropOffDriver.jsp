<%@include file="/common/taglibs.jsp"%>

<%@page import="com.transys.model.OrderStatus"%>

<script type="text/javascript">
function validateDropOffDriverForm() {
	var missingData = validateDropOffDriverMissingData();
	if (missingData != "") {
		var alertMsg = "<span style='color:red'><b>Please provide following required data:</b><br></span>"
					 + missingData;
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}

function validateDropOffDriverMissingData() {
	var missingData = "";
	
	if ($('#dropOffDriverSelect').val() == "") {
		missingData += "Drop off Driver, "
	}
	
	if ($('#dumpsterNumSelect').val() == "") {
		missingData += "Dumpster #, "
	}
	
	if ($('#dropVehicleNumSelect').val() == "") {
		missingData += "Vehicle #, "
	}
	
	if (missingData != "") {
		missingData = missingData.substring(0, missingData.length - 2);
	}
	return missingData;
}

function processDropOffDriverForm(changeToDroppedOff) {
	if (validateDropOffDriverForm()) {
		var dropOffDriverForm = getDropOffDriverForm();
		
		var action = dropOffDriverForm.attr('action');
		action += "?changeToDroppedOff="+changeToDroppedOff;
		dropOffDriverForm.attr('action', action);
		
		dropOffDriverForm.submit();
	}
}

function processRevertToOpen() {
	var dropOffDriverForm = getDropOffDriverForm();
	dropOffDriverForm.attr('action', 'revertDropOffToOpen.do');
	dropOffDriverForm.submit();
}

function getDropOffDriverForm() {
	var dropOffDriverForm = $("#dropOffDriverAddEditForm");
	return dropOffDriverForm;
}
</script>

<form:form action="saveDropOffDriver.do" name="dropOffDriverAddEditForm" commandName="modelObject" method="post" id="dropOffDriverAddEditForm">
	<form:hidden path="id" id="id" />
	<input type="hidden" name="currentlyAssignedDumpsterId" id="currentlyAssignedDumpsterId" value="${currentlyAssignedDumpsterId}" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDropOffDriver" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Order #</td>
			<td class="td-static">${modelObject.id}</td>
		</tr>
		<tr>
			<td class="form-left">Drop-off Driver<span class="errorMessage">*</span></td>
			<td>
				<form:select id="dropOffDriverSelect" cssClass="flat form-control input-sm" style="width:172px !important" path="dropOffDriver"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="dropOffDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Dumpster #<span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterNumSelect" cssClass="flat form-control input-sm" path="dumpster" style="width:172px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${orderDumpsters}" itemValue="id" itemLabel="dumpsterNum" />
				</form:select> 
				<form:errors path="dumpster" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">Vehicle #<span class="errorMessage">*</span></td>
			<td>
				<form:select id="dropVehicleNumSelect" cssClass="flat form-control input-sm" path="dropOffVehicleId" style="width:172px !important">
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${orderVehicles}" itemValue="id" itemLabel="number" />
				</form:select> 
				<form:errors path="dropOffVehicleId" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<c:set var="saveDisabled" value="" />
				<!--modelObject.orderStatus.status != 'Open'-->
				<c:if test="${modelObject.id == null}">
					<c:set var="saveDisabled" value="disabled" />
				</c:if>
				<c:set var="changeToDroppedOff" value="" />
				<c:if test="${(modelObject.id == null) || (modelObject.orderStatus.status != 'Open')}">
					<c:set var="changeToDroppedOff" value="disabled" />
				</c:if>
				<c:set var="revertToOpenDisabled" value="" />
				<c:if test="${(modelObject.id == null) || (modelObject.orderStatus.status != 'Dropped Off')}">
					<c:set var="revertToOpenDisabled" value="disabled" />
				</c:if>
				<input type="button" id="dropOffDriverCreate" ${saveDisabled} onclick="processDropOffDriverForm('false');" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="dropOffDriverDropedOff" ${changeToDroppedOff} onclick="processDropOffDriverForm('true');" value="Change To Dropped Off" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="dropOffDriverRevert" ${revertToOpenDisabled} onclick="processRevertToOpen();" value="Revert To Open" class="flat btn btn-primary btn-sm btn-sm-ext" />
				<input type="button" id="dropOffDriverBackBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>