<%@include file="/common/taglibs.jsp"%>
<br/>
<form:form action="saveDropOffDriver.do" name="typeForm" commandName="modelObject" method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Drop-off Driver" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select id="dropOffDriver" cssClass="flat form-control input-sm" style="width:175px" path="pickupDriver"> 
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				 <br><form:errors path="pickupDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster #" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select id="dumpsterNum" cssClass="flat form-control input-sm" path="dumpster" style="width:175px">
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${dumpsters}" itemValue="id" itemLabel="dumpsterNum" />
				</form:select> 
				<br><form:errors path="dumpster" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Check #"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="orderPaymentInfo.checkNum" cssClass="flat" />
				<br><form:errors path="orderPaymentInfo.checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>