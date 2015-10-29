<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Material Types</h5>
<form:form action="save.do" name="materialTypeForm" commandName="modelObject" method="post" id="materialTypeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageMaterialTypes" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Material Category<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="materialCategory" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${materialCategories}" itemValue="id" itemLabel="category" />
				</form:select> 
				<form:errors path="materialCategory" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left">Material Type<span class="errorMessage">*</span></td>
			<td>
				<form:input path="materialName" cssClass="flat" style="width:172px !important"/>
				<form:errors path="materialName" cssClass="errorMessage" />
			</td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="materialTypeComments" />
				<form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="create" onclick="return validate()" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" id="cancelBtn" value="Back" class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>