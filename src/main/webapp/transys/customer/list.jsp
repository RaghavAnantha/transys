<%@include file="/common/taglibs.jsp"%>
<br/>
<h3 style="margin-top:-15px; !important">Manage Customers</h3>
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<%-- <td align="${left}" class="first"><transys:label code="First Name" /></td>
			<td align="${left}"><input name="firstName" type="text"
				value="${sessionScope.searchCriteria.searchMap.firstName}" /></td>
			<td align="${left}" class="first"><transys:label code="Last Name" /></td>
			<td align="${left}"><input name="lastName" type="text"
				value="${sessionScope.searchCriteria.searchMap.lastName}" /></td>
	 --%>	
	 <td align="${left}" class="form-left"><transys:label code="Company Name"/></td>
			<td align="${left}" class="wide"><select class="flat form-control input-sm" id="companyName" name="companyName" style="width:175px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${customer}" var="customer">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['companyName'] == customer.companyName}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
				</c:forEach>
			</select>
			</td>
			
			<td align="${left}" class="form-left"><transys:label code="Customer ID"/></td>
			<td align="${left}"><select class="flat form-control input-sm" id="customerId" name="id" style="width:175px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${customerIds}" var="customerId">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == customerId.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customerId.id}" ${selected}>${customerId.id}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button" class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="delete.do" id="serviceForm">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Name" dataField="companyName" />
		<transys:textcolumn headerText="Customer ID" dataField="id" />
		<transys:textcolumn headerText="Address Line1" dataField="billingAddressLine1" />
		<transys:textcolumn headerText="Address Line2" dataField="billingAddressLine2" />
		<transys:textcolumn headerText="City" dataField="city" />
		<transys:textcolumn headerText="State" dataField="state.name" />
		<transys:textcolumn headerText="Zipcode" dataField="zipcode" />
		<transys:textcolumn headerText="Phone" dataField="phone" />
		<transys:textcolumn headerText="Fax" dataField="fax" />
		
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


