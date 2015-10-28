<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Customer List Report</h5>
<form:form action="customerListReport.do" method="get" name="customersListReport" id="customersListReport">
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Company Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.companyName" name="companyName" style="width:175px !important">
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
		</tr>
		<tr>
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.contactName" name="contactName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${contactNames}" var="aContactName">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['contactName'] == aContactName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aContactName}" ${selected}>${aContactName}</option>
					</c:forEach>
				</select>
			</td>	
		</tr>
		<tr>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id="customerListReport.phone" name="phone" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${phones}" var="aPhone">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['phone'] == aPhone}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aPhone}" ${selected}>${aPhone}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Status</td>
			<td >
				<select class="flat form-control input-sm" id="customerListReport.customerStatus" name="customerStatus" style="width: 175px !important">
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
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['customersListReport'].submit();"
				value="Preview"/></td>
		</tr>
	</table>
</form:form>

<a href="/customer/generateCustomerListReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/customer/generateCustomerListReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="customersListReportDetails" id="customersListReportDetails" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${customerListReportList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="customerListReport">
		<transys:textcolumn headerText="Id" dataField="id" width="85px"/>
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="Contact Name" dataField="contactName" />
		<transys:textcolumn headerText="Phone Number" dataField="phoneNumber" />
		<transys:textcolumn headerText="Status" dataField="status" />
		<transys:textcolumn headerText="Total Orders" dataField="totalOrders" />
		<transys:textcolumn headerText="Total Amount" dataField="totalOrderAmount" />
	</transys:datatable>
	<%session.setAttribute("customerListReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


