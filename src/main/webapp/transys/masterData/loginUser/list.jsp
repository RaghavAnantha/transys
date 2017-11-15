<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage User Logins</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left form-left-ext">Employee First Name</td>
			<td><select class="flat form-control input-sm" id="status" name="employee.firstName" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${employees}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employee.firstName'] == anEmployee.firstName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.firstName}" ${selected}>${anEmployee.firstName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
		<td class="form-left form-left-ext">Employee Last Name</td>
			<td>
				<select class="flat form-control input-sm" id="lastName" name="employee.lastName" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${employees}" var="anEmployee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employee.lastName'] == anEmployee.lastName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anEmployee.lastName}" ${selected}>${anEmployee.lastName}</option>
					</c:forEach>
				</select>
			</td>	
		</tr>
		<tr>
			<td class="form-left">User Role</td>
			<td><select class="flat form-control input-sm" id="role" name="role.name"
					style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${roles}" var="aRole">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['role.name'] == aRole.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aRole.name}" ${selected}>${aRole.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Account Status</td>
			<td><select class="flat form-control input-sm" id="accountStatus" name="accountStatus.status"
					style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${employeeStatus}" var="employeeStatus">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['accountStatus.status'] == employeeStatus.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${employeeStatus.status}" ${selected}>${employeeStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext"
					onclick="document.forms['searchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<form:form name="loginUser.do" id="loginUser" class="tab-color">
	<transys:datatable urlContext="masterData/loginUser" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="loginUser">
		<transys:textcolumn headerText="Employee Id" dataField="employee.id" width="80px"/>
		<transys:textcolumn headerText="First Name" dataField="employee.firstName"/>
		<transys:textcolumn headerText="Last Name" dataField="employee.lastName"/>
		<transys:textcolumn headerText="Username" dataField="username" />
		<transys:textcolumn headerText="Role" dataField="role.name" />
		<transys:textcolumn headerText="Last Login Date" dataField="formattedLastLoginDate" />
		<transys:textcolumn headerText="Status" dataField="employee.status.status" />
		<%--
		<transys:textcolumn headerText="Notes/Comments" dataField="comments" />
		<transys:textcolumn headerText="Job Title" dataField="employee.jobTitle.jobTitle" />
		<transys:textcolumn headerText="Job Title Id" dataField="employee.jobTitle.id" />
		 --%>
	</transys:datatable>
	<%session.setAttribute("loginUserColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


