<%@include file="/common/taglibs.jsp"%>
<form:form action="savePickupDriver.do" name="savePickupDriverForm" commandName="modelObject" method="post" id="savePickupDriverForm">
	<form:hidden path="id" id="id" />
	<table id="form-table" class="table">
		<tr><td colspan="2"></td></tr>
		<tr>
			<td class="form-left">Pickup Date<span class="errorMessage">*</span></td>
			<td>
				<form:input path="pickupDate" cssClass="flat" id="datepicker6" name="pickupDate" style="width:172px !important"/>
				 <form:errors path="pickupDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Pickup Driver" /><span class="errorMessage"></span></td>
			<td>
				<form:select id="pickupDriver" cssClass="flat form-control input-sm" style="width:172px !important" path="pickupDriver"> 
					<form:option value="">-----Please Select-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="pickupDriver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Check #"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="orderPayment[0].checkNum" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="orderPayment[0].checkNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td colspan=10 class="section-header" style="line-height: 1;font-size: 13px;font-weight: bold;color: white;">Scale/Weights Information</td>
		</tr>
		<tr>
			<td colspan="10"></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Gross"/><span class="errorMessage">*</span></td>
			<td class="wide">
				<form:input path="grossWeight" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="grossWeight" cssClass="errorMessage" />
			</td>
			<td class="form-left">Tare<span class="errorMessage">*</span></td>
			<td>
				<form:input path="tare" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="tare" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Net Lb"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="netWeightLb" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightLb" cssClass="errorMessage" />
			</td>
			<td class="form-left"><transys:label code="Net Tonnage"/><span class="errorMessage">*</span></td>
			<td>
				<form:input path="netWeightTonnage" cssClass="flat" style="width:172px !important"/>
				<br><form:errors path="netWeightTonnage" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">
				<input type="submit" id="submitPickupDriver" onclick="return validate()" value="<transys:label code="Save"/>" class="flat btn btn-primary btn-sm" /> 
				<input type="button" id="cancelPickupDriver" value="Cancel" class="flat btn btn-primary btn-sm" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>