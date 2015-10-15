<%@include file="/common/taglibs.jsp"%>

<br />
<h4 style="margin-top: -15px; !important">Add/Edit Material Intake For Recycle</h4>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material Type" /></td>
			<td align="${left}">
				<form:select id="materialTypeSelect" cssClass="flat form-control input-sm" path="materialType" style="width: 175px !important" > 
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="materialName" />
				</form:select> 
			 	<br><form:errors path="materialType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Intake Date" /></td>
			<td align="${left}" class="wide"><form:input path="intakeDate" class="flat" id="datepicker7" name="intakeDate" style="width: 175px" /></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Tons" /></td>
			<td align="${left}" class="wide"><form:input path="netWeightTonnage" cssClass="flat" style="width: 175px"  /></td>
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