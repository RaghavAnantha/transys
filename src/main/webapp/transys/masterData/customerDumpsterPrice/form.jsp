<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Customer Dumpster Price</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<%@include file="/common/messages.jsp"%>
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left"><transys:label code="Customer" /></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="customer" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${customers}" itemValue="id"  itemLabel="companyName" />
				</form:select> 
				<br><form:errors path="customer" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Price" /><span class="errorMessage">*</span></td>
			<td><form:input path="dumpsterPrice" cssClass="flat" /> <br>
			<form:errors path="dumpsterPrice" cssClass="errorMessage" /></td>
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