<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Orders Report</h5>
<form:form action="orderReport.do" method="get" name="orderReportSearchForm" id="orderReportSearchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Company Name</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="compName" name="customer" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customers}" var="name">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer'] == name.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${name.id}" ${selected}>${name.companyName}</option>
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
			<td class="form-left">Contact Name</td>
			<td>
				<select class="flat form-control input-sm" id=deliveryContactName name="deliveryContactName" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${deliveryContactNames}" var="aDeliveryContactName">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryContactName'] == aDeliveryContactName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryContactName}" ${selected}>${aDeliveryContactName}</option>
					</c:forEach>
				</select>
			</td>
			
			<td class="form-left">Order Status</td>
			<td>
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${orderStatuses}" var="oStatus">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['orderStatus'] == oStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${oStatus.id}" ${selected}>${oStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id=deliveryContactPhone1 name="deliveryContactPhone1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryContactPhone1'] == anOrder.deliveryContactPhone1}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.deliveryContactPhone1}" ${selected}>${anOrder.deliveryContactPhone1}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm" onclick="document.forms['orderReportSearchForm'].submit();"
					value="<transys:label code="Preview"/>" />
			</td>
		</tr>
	</table>
</form:form>

<hr class="hr-ext">
<table class="table" id="form-table"> 
	<tr><td colspan=10></td><td colspan=10></td></tr>	
	<tr>
		<td class="form-left">Order Date Range:</td>
		<td class="wide td-static" id="orderDateRange">${orderDateFrom} To ${orderDateTo}</td>
	</tr>
</table>

<a href="/order/generateOrderReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/order/generateOrderReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="orderReportForm" id="orderReportForm" class="tab-color">
	<transys:datatable urlContext="order"  baseObjects="${orderReportList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" dataQualifier="orderReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Del Adds" dataField="deliveryAddress.fullLine" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" dataFormat="MM/dd/yyy"/>
		<%--<transys:textcolumn headerText="Pymt. method" dataField="orderPayment[0].paymentMethod.method" />--%>	
		<transys:textcolumn headerText="Dumpster Price" dataField="orderFees.dumpsterPrice" />
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" />
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" />
		<transys:textcolumn headerText="OvrWt. Fee" dataField="orderFees.overweightFee" />
		<transys:textcolumn headerText="Addnl. Fees" dataField="orderFees.totalAdditionalFees" />
		<transys:textcolumn headerText="Total Fees" dataField="orderFees.totalFees" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid"/>
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" />	
	</transys:datatable>
	<%session.setAttribute("orderReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
