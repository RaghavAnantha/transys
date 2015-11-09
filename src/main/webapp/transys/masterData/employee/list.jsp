<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Employees</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Employee Id" /></td>
			<td>
				<select class="flat form-control input-sm" id="status" name="employeeId" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${employee}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['employeeId'] == anEmployee.employeeId}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.employeeId}" ${selected}>${anEmployee.employeeId}</option>
					</c:forEach>
			</select></td>	
		</tr>
		<tr>
			<td class="form-left form-left-ext"><transys:label
					code="Employee First Name" /></td>
			<td><select
				class="flat form-control input-sm" id="status" name="firstName" style="width: 175px !important">
					<option value="">----Please Select----
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
			<td class="form-left form-left-ext"><transys:label code="Employee Last Name" /></td>
			<td >
				<select class="flat form-control input-sm" id="lastName" name="lastName" style="width: 175px !important">
					<option value="">----Please Select----</option>
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
			<td class="form-left"><transys:label
					code="Status" /></td>
			<td><select
				class="flat form-control input-sm" id="status" name="status.status"
				style="width: 175px !important">
					<option value="">----Please Select----
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
			<td></td>
			<td><input type="button"
				class="btn btn-primary btn-sm btn-sm-ext"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="employee.do" id="employee" class="tab-color">
	<transys:datatable urlContext="masterData/employee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="employee">
		<transys:textcolumn headerText="Employee Id" dataField="employeeId" width="80px"/>
		<transys:textcolumn headerText="Employee First Name" dataField="firstName"/>
		<transys:textcolumn headerText="Employee Last Name" dataField="lastName" />
		<transys:textcolumn headerText="Job Title" dataField="jobTitle.jobTitle" />
		<transys:textcolumn headerText="Phone" dataField="phone" />
		<transys:textcolumn headerText="Hire Date" dataField="hireDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Termination Date" dataField="leaveDate" dataFormat="MM/dd/yyyy" />
		<transys:textcolumn headerText="Status" dataField="status.status" />


	</transys:datatable>
	<%session.setAttribute("employeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


