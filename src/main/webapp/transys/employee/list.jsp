<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Employees</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Employee ID" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="employeeID"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${employee}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employeeID'] == anEmployee.employeeID}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.employeeID}" ${selected}>${anEmployee.employeeID}</option>
					</c:forEach>
			</select></td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Employee First Name" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="firstName"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${employee}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['firstName'] == anEmployee.firstName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.firstName}" ${selected}>${anEmployee.firstName}</option>
					</c:forEach>
			</select></td>

		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Employee last Name" /></td>
						<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="lastName" name="lastName"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${employee}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['lastName'] == anEmployee.lastName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.lastName}" ${selected}>${anEmployee.lastName}</option>
					</c:forEach>
			</select></td>	

		</tr>
		
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Status" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="status.status"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${employeeStatus}" var="employeeStatus">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['status.status'] == employeeStatus.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${employeeStatus.status}" ${selected}>${employeeStatus.status}</option>
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
	<transys:datatable urlContext="employee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="employee">
		<transys:textcolumn headerText="Employee ID" dataField="employeeID" />
		<transys:textcolumn headerText="Employee First Name" dataField="firstName" />
		<transys:textcolumn headerText="Employee Last Name" dataField="lastName" />
		<transys:textcolumn headerText="Job Title" dataField="jobTitle.jobTitle" />
		<transys:textcolumn headerText="Phone" dataField="phone" />
		<transys:textcolumn headerText="Hire Date" dataField="hireDate" />
		<transys:textcolumn headerText="Termination Date" dataField="leaveDate" />
		<transys:textcolumn headerText="Status" dataField="status.status" />


	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


