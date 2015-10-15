<%@include file="/common/taglibs.jsp"%>

<br />
<h4 style="margin-top: -15px; !important">Add/Edit Permit Fee</h4>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="permitClass.id" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitClass}" itemValue="id"  itemLabel="permitClass" />
				</form:select> 
				<br><form:errors path="permitClass.id" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width:172px !important" path="permitType.id" >
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${permitType}" itemValue="id"  itemLabel="permitType" />
				</form:select> 
				<br><form:errors path="permitType.id" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Fee" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="fee" cssClass="flat" /> <br>
			<form:errors path="fee" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Effective Date From" /></td>
			<td align="${left}">
			<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /></td>
			<td align="${left}">
			<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  /></td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10 class="danger" style="font-size: 13px;font-weight: bold;color: white;">Notes/Comments</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td colspan=10>
				<form:textarea row="5" path="comments" cssClass="flat" id="permitFeeComments" style="width:100%; height:150%;"/>
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
			<td align="${left}" colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm" /> <input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "
				class="flat btn btn-primary btn-sm" /> <input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>"
				class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>