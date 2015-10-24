<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Material Categories</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageMaterialCategories" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Material Category" /><span class="errorMessage">*</span></td>
			<td>
				<form:input path="category" cssClass="flat" style="width:350px !important"/>
				<form:errors path="category" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
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
				<form:textarea row="5" path="comments" cssClass="flat" id="materialCategoryComments" style="width:100%; height:150%;"/>
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>