<%@include file="/common/taglibs.jsp"%>

<br />
<h4 style="margin-top: -15px; !important">Add/Edit Material Types</h4>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Material ID" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="id" cssClass="flat" /> <br>
			<form:errors path="id" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Material Type" /></td>
			<td align="${left}"><form:input path="type" cssClass="flat" />
				<br>
			<form:errors path="type" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm" /> <input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "
				class="flat btn btn-primary btn-sm" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>