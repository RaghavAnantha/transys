<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Dumpsters</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDumpsters" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="dumpsterSize" cssClass="flat form-control input-sm" style="width:172px !important" path="dumpsterSize"> 
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id" itemLabel="size" />
				</form:select> 
			 	<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Number" /><span class="errorMessage">*</span></td>
			<td><form:input path="dumpsterNum" cssClass="flat" style="width:172px !important"/> <br>
			<form:errors path="dumpsterNum" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Status" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="status" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterStatus}" itemValue="id"  itemLabel="status" />
				</form:select> 
				<form:errors path="status" cssClass="errorMessage" />
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
				<form:textarea row="5" path="comments" cssClass="flat" id="dumpsterComments" style="width:100%; height:150%;"/>
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