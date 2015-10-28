<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Customer Orders Report</h5>
<form:form action="customerOrdersReport.do" method="get" name="customerOrdersReport" id="customerOrdersReport">
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Company Name</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="companyNameCustomerOrdersReport" name="companyName" style="width:175px !important">
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
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id="contactNameCustomerOrdersReport" name="contactName" style="width:175px !important">
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
				<select class="flat form-control input-sm" id="phoneCustomerOrdersReport" name="phone" style="width: 175px !important">
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
			<td class="form-left">Status</td>
			<td >
				<select class="flat form-control input-sm" id="customerStatusCustomerOrdersReport" name="customerStatus" style="width: 175px !important">
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
			<td class="form-left">Order Date From</td>
			<td>
				<input class="flat" id="datepicker11" name="createdAtFrom" value="${sessionScope.searchCriteria.searchMap['createdAtFrom']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Order Date To</td>
			<td>
				<input class="flat" id="datepicker12" name="createdAtTo" value="${sessionScope.searchCriteria.searchMap['createdAtTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['customerOrdersReport'].submit();"
				value="Preview"/></td>
		</tr>
		<tr><td></td></tr>
	</table>
</form:form>

<hr class="hr-ext">
<table class="table" id="form-table"> 
	<tr><td colspan=10></td><td colspan=10></td></tr>
	<tr>
		<td class="form-left">Company Name:</td>
		<td class="wide td-static" id="companyNameTd">${companyName}
		<td class="form-left">&nbsp;&nbsp;Order Date Range:</td>
		<td class="wide td-static" id="orderDateRangeTd">${orderDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${orderDateTo}</td>
	</tr>
	<tr>
		<td class="form-left">Total Orders:</td>
		<td class="wide td-static" id="totalOrdersTd">${totalOrders}
		<td class="form-left">&nbsp;&nbsp;Total Amount:</td>
		<td class="wide td-static" id="totalAmountTd">${totalOrderAmount}</td>
	</tr>
</table>

<a href="/customer/generateCustomerOrdersReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/customer/generateCustomerOrdersReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="customerOrdersReportDetails" id="customerOrdersReportDetails" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${customerOrdersReportList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="customerOrdersReport">
		<transys:textcolumn headerText="Id" dataField="id" width="85px"/>
		<transys:textcolumn headerText="Company Name" dataField="companyName" />
		<transys:textcolumn headerText="Contact Name" dataField="contactName" />
		<transys:textcolumn headerText="Phone Number" dataField="phoneNumber" />
		<transys:textcolumn headerText="Status" dataField="status" />
		<transys:textcolumn headerText="Total Orders" dataField="totalOrders" />
		<transys:textcolumn headerText="Total Amount" dataField="totalOrderAmount" />
	</transys:datatable>
	<%session.setAttribute("customerOrdersReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


