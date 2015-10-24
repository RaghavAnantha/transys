<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit City Fee</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageCityFee" />
	</jsp:include>
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left">City<span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="suburbName" cssClass="flat" style="width:173px !important"/>
				<br><form:errors path="suburbName" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Fee" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="fee" cssClass="flat" style="width:173px !important"/>
				<br><form:errors path="fee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left form-left-ext"><transys:label code="Effective Date From" /><span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveStartDate" cssClass="flat" id="datepicker7" name="effectiveStartDate" style="width:173px !important"/></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /><span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveEndDate" cssClass="flat" id="datepicker8" name="effectiveEndDate"  style="width:173px !important"/></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
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
			<td>&nbsp;</td>
			<td colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm btn-sm-ext" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>