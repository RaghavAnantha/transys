<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Employees</h4>
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Employee ID" /></td>
			<td align="${left}" class="wide"><input class="flat"
				id="employeeId" name="id" style="width: 163px" /></td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Employee First Name" /></td>
			<td align="${left}" class="wide"><input class="flat"
				id="employeeId" name="firstName" style="width: 163px" /></td>

		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Employee last Name" /></td>
			<td align="${left}" class="wide"><input class="flat" id="employeeId" name="lastName" style="width: 163px" /></td>

		</tr>
		
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Status" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="status"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${customer}" var="Status">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['contactName'] == Status.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${Status.status}" ${selected}>${Status.status}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="employee.do" id="employee" class="tab-color">
	<transys:datatable urlContext="masterdata" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Employee ID" dataField="id" />
		<transys:textcolumn headerText="Employee First Name" dataField="firstName" />
		<transys:textcolumn headerText="Employee Last Name" dataField="lastName" />
		<transys:textcolumn headerText="Status" dataField="status" />


	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


