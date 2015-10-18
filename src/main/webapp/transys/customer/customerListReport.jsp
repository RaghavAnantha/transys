<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Customer List Report</h5>
<form:form action="customerListReport.do" method="get" name="customersListReport" id="customersListReport">
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Company Name" /></td>
			<td align="${left}"><select
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
			<td align="${left}"><select class="flat form-control input-sm" id="contactNameListReport"
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
				id="phone" name="phone" style="width: 175px !important">
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
			<td align="${left}">
				<select class="flat form-control input-sm" id="customerStatus" name="customerStatus" style="width: 175px !important">
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
				onclick="document.forms['customersListReport'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
<a href="/customer/generateCustomerListReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/customer/generateCustomerListReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
</form:form>
<form:form name="customersListReportDetails" id="customersListReportDetails" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${customerReportVOList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="customerListReport">
		<transys:textcolumn headerText="Id" dataField="id" width="30px"/>
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="Contact Name" dataField="contactName" />
		<transys:textcolumn headerText="Phone Number" dataField="phoneNumber" />
		<transys:textcolumn headerText="Status" dataField="status" />
		<transys:textcolumn headerText="Total Orders" dataField="totalOrders" />
		<transys:textcolumn headerText="Total Amount" dataField="totalAmount" />
	</transys:datatable>
	<%session.setAttribute("customerListReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


