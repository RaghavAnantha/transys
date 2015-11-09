<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Customer Orders Report</h5>
<form:form action="customerOrdersReport.do" method="get" name="customerOrdersReport" id="customerOrdersReport">
	<table id="form-table" class="table">
		<tr><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Company Name</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="customerOrdersReport.companyName" name="customer.companyName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer.companyName'] == customer.companyName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id="customerOrdersReport.contactName" name="customer.contactName" style="width:175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${contactNames}" var="aContactName">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer.contactName'] == aContactName}">
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
				<select class="flat form-control input-sm" id="customerOrdersReport.phone" name="customer.phone" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${phones}" var="aPhone">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer.phone'] == aPhone}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aPhone}" ${selected}>${aPhone}</option>
					</c:forEach>
				</select>
			</td>	
			<td class="form-left">Order Status</td>
			<td >
				<select class="flat form-control input-sm" id="customerOrdersReport.orderStatus" name="orderStatus" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${orderStatuses}" var="anOrderStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['orderStatus'] == anOrderStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderStatus.id}" ${selected}>${anOrderStatus.status}</option>
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
		<td class="wide td-static" id="companyNameTd">${customerOrdersReportCompanyName}</td>
		<td class="form-left">&nbsp;&nbsp;Order Date Range:</td>
		<td class="wide td-static" id="orderDateRangeTd">${customerOrdersReportOrderDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${customerOrdersReportOrderDateTo}</td>
	</tr>
	<tr>
		<td class="form-left">Total Orders:</td>
		<td class="wide td-static" id="totalOrdersTd">${customerOrdersReportTotalOrders}</td>
		<td class="form-left">&nbsp;&nbsp;Total Amount:</td>
		<td class="wide td-static" id="totalAmountTd">${customerOrdersReportTotalOrderAmount}</td>
	</tr>
</table>

<a href="/customer/generateCustomerOrdersReport.do?type=xlsx"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/customer/generateCustomerOrdersReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="customerOrdersReportDetails" id="customerOrdersReportDetails" class="tab-color">
	<transys:datatable urlContext="customer" baseObjects="${customerOrdersReportList[0].orderList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" dataQualifier="customerOrdersReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Del Adds" dataField="deliveryAddressFullLine" />
		<transys:textcolumn headerText="City" dataField="deliveryCity" />
		<transys:textcolumn headerText="Status" dataField="status" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
		<%--<transys:textcolumn headerText="Pymt. method" dataField="orderPayment[0].paymentMethod.method" />--%>	
		<transys:textcolumn headerText="Dumpster Price" dataField="dumpsterPrice" />
		<transys:textcolumn headerText="City Fee" dataField="cityFee" />
		<transys:textcolumn headerText="Permit Fee" dataField="permitFees" />
		<transys:textcolumn headerText="OvrWt. Fee" dataField="overweightFee" />
		<transys:textcolumn headerText="Addnl. Fees" dataField="additionalFees" />
		<transys:textcolumn headerText="Total Fees" dataField="totalFees" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid"/>
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" />	
	</transys:datatable>
	<%session.setAttribute("customerOrdersReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


