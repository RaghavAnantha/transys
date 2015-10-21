<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit City Fee</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<%@include file="/common/messages.jsp"%>
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left">City</td>
			<td class="wide">
				<form:input path="suburbName" cssClass="flat" style="width:173px !important"/>
				<br><form:errors path="suburbName" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Fee" /></td>
			<td>
				<form:input path="fee" cssClass="flat" style="width:173px !important"/>
				<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date From" /></td>
			<td>
			<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" style="width:173px !important"/></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /></td>
			<td>
			<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  style="width:173px !important"/></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="comments" cssClass="flat" id="cityFeeComments" style="width:100%; height:150%;"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>