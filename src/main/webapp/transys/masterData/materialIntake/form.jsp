<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Material Intake For Recycle</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageMaterialIntake" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Material Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select id="materialTypeSelect" cssClass="flat form-control input-sm" path="materialType" style="width: 175px !important" > 
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="materialName" />
				</form:select> 
			 	<form:errors path="materialType" cssClass="errorMessage" />
			</td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Intake Date" /><span class="errorMessage">*</span></td>
			<td class="wide"><form:input path="intakeDate" class="flat" id="datepicker7" name="intakeDate" style="width: 175px !important" /></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Net Tonnage" /><span class="errorMessage">*</span></td>
			<td class="wide"><form:input path="netWeightTonnage" cssClass="flat" style="width: 175px !important"  /></td>
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
				<form:textarea row="5" path="comments" cssClass="flat" id="materialIntakeComments" style="width:100%; height:150%;"/>
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