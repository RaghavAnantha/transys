<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Customers</h5>
<form:form action="list.do" method="get" name="customerSearchForm">
	<table id="form-table" class="table">
		<tr>
			<td align="${left}" class="form-left">Company Name</td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="companyName" name="companyName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['companyName'] == customer.companyName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left">Customer ID</td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="customerId" name="id" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customerIds}" var="customerId">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == customerId}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customerId}" ${selected}>${customerId}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Contact Name</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="contactName" name="contactName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${contactNames}" var="aContactName">
						<c:if test="${not empty aContactName}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['contactName'] == aContactName}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aContactName}" ${selected}>${aContactName}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id="phone" name="phone" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${phones}" var="aPhone">
						<c:if test="${not empty aPhone}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['phone'] == aPhone}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPhone}" ${selected}>${aPhone}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Create Date From</td>
			<td class="wide">
				<input class="flat" id="datepicker" name="createdAtFrom" value="${sessionScope.searchCriteria.searchMap['createdAtFrom']}" style="width: 175px" />
			</td>
			<td class="form-left">Create Date To</td>
			<td>
				<input class="flat" id="datepicker1" name="createdAtTo" value="${sessionScope.searchCriteria.searchMap['createdAtTo']}" style="width: 175px" />
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Status" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="customerStatus" name="customerStatus" style="width: 175px !important">
					<option value="">----Please Select----</option>
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
			<td align="${left}">
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['customerSearchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<form:form name="customerServiceForm" id="customerServiceForm" class="tab-color">
	<transys:datatable urlContext="customer" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="manageCustomer">
		<transys:textcolumn headerText="Id" dataField="id" width="85px"/>
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="Contact Name" dataField="contactName" />
		<transys:textcolumn headerText="Phone" dataField="phone" />
		<transys:textcolumn headerText="Created Date" dataField="createdAt" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Status" dataField="customerStatus.status" />
	</transys:datatable>
	<%session.setAttribute("manageCustomerColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


