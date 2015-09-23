<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Customers List Report</h4>
<form:form action="customerListReport.do" method="get" name="customersListReport">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Company Name" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="companyNameListReport"
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
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Contact Name" /></td>
			<td align="${left}" class="wide"><select class="flat form-control input-sm" id="contactNameListReport"
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
		</tr>
		<tr>
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
			<td align="${left}" class="form-left"><transys:label code="Status" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="status" name="status" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customer}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == aCustomer.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.status}" ${selected}>${aCustomer.status}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['customersListReport'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="searchCustomersListReport" id="searchCustomersListReport" class="tab-color">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" insertable="true" baseObjects="${customerlist}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Customer ID" dataField="id" />
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="ContactName" dataField="contactName" />
		<transys:textcolumn headerText="Phone Number" dataField="phone" />
		<transys:textcolumn headerText="Status" dataField="createdAt" />
		<transys:textcolumn headerText="Total Orders" dataField="status" />
		<transys:textcolumn headerText="Total Amount" dataField="status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


