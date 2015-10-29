<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Dumpster Price</h5>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post" id="typeForm">
	<form:hidden path="id" id="id" />
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="manageDumpsterPrice" />
	</jsp:include>
	<table id="form-table" class="table">
	
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:173px !important" path="dumpsterSize" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${dumpsterSizes}" itemValue="id"  itemLabel="size" />
				</form:select> 
				<form:errors path="dumpsterSize" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Material Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat form-control input-sm" style="width:173px !important" path="materialType" >
					<form:option value="">----Please Select----</form:option>
					<form:options items="${materialTypes}" itemValue="id" itemLabel="materialName" />
				</form:select> 
				<form:errors path="materialType" cssClass="errorMessage" />
			</td> 
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Price" /><span class="errorMessage">*</span></td>
			<td><form:input path="price" cssClass="flat" style="width:173px !important"/>
			<br> 
			<form:errors path="price" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left form-left-ext"><transys:label code="Effective Date From" /><span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveStartDate" class="flat" id="datepicker7" name="effectiveStartDate" style="width:173px !important"/></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Effective Date To" /><span class="errorMessage">*</span></td>
			<td>
			<form:input path="effectiveEndDate" class="flat" id="datepicker8" name="effectiveEndDate"  style="width:173px !important"/></td>
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
				<form:textarea row="5" path="comments" cssClass="flat notes" id="dumpsterPriceComments" />
				<br><form:errors path="comments" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td colspan=10></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2"><input type="submit" id="create" onclick="return validate()" value="<transys:label code="Save"/>"
				class="flat btn btn-primary btn-sm btn-sm-ext" /> <input type="button" id="cancelBtn" value="<transys:label code="Back"/>"
				class="flat btn btn-primary btn-sm btn-sm-ext" onClick="location.href='main.do'" /></td>
		</tr>
	</table>
</form:form>