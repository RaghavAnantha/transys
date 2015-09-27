<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Customers</h4>
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Company Name" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="companyName"
				name="companyName" style="width:175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['companyName'] == customer.companyName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
					</c:forEach>
			</select></td>

			<td align="${left}" class="form-left"><transys:label
					code="Customer ID" /></td>
			<td align="${left}"><select class="flat form-control input-sm"
				id="customerId" name="id" style="width:175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customerIds}" var="customerId">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['id'] == customerId.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customerId.id}" ${selected}>${customerId.id}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Contact Name" /></td>
			<td align="${left}" class="wide"><select class="flat form-control input-sm" id="contactName"
				name="contactName" style="width:175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customer}" var="name">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['contactName'] == name.contactName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${name.contactName}" ${selected}>${name.contactName}</option>
					</c:forEach>
			</select></td>

			<td align="${left}" class="form-left"><transys:label
					code="Phone Number" /></td>
			<td align="${left}"><select class="flat form-control input-sm"
				id="phone" name="phone" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customerIds}" var="phone">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['phone'] == phone.phone}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${phone.phone}" ${selected}>${phone.phone}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Create Date From" /></td>
			<td align="${left}" class="wide"><input class="flat"
				id="datepicker" name="createdAtFrom" style="width: 175px" /></td>

			<td align="${left}" class="form-left"><transys:label
					code="Create Date To" /></td>
			<td align="${left}"><input class="flat" id="datepicker1"
				name="createdAtTo" style="width: 175px" /></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Status" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="customerStatus" name="customerStatus" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customerStatuses}" var="aCustomerStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['customerStatus'] == aCustomerStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomerStatus.id}" ${selected}>${aCustomerStatus.status}</option>
					</c:forEach>
				</select>
			</td>
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
<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Customer ID" dataField="id" />
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="ContactName" dataField="contactName" />
		<transys:textcolumn headerText="Phone" dataField="phone" />
		<transys:textcolumn headerText="Created Date" dataField="createdAt" />
		<transys:textcolumn headerText="Status" dataField="customerStatus.status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


