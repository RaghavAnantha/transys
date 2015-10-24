<%@include file="/common/taglibs.jsp"%>
<br/>
<h4 style="margin-top: -15px; !important">Orders Revenue Report</h4>
<form:form action="list.do" method="get" name="searchForm" id="ordersRevenueReportSearchForm">
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td class="form-left"><transys:label code="Order Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker1" name="createdAtFrom" style="width: 175px" /></td>
					
			<td class="form-left"><transys:label code="Order Date To"/></td>
		    <td class="wide"><input class="flat" id="datepicker2" name="createdAtTo" style="width: 175px" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" class="btn btn-primary btn-sm" onclick="document.forms['ordersRevenueReportSearchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>
<hr class="hr-ext">
<table class="table" id="form-table">
	 <tr>
		<td class="form-left"><transys:label code="Order Date Range:" /><span class="errorMessage"></span></td>
		  <td class="wide td-static" id="orderDateRange" >${orderDateFrom} To ${orderDateTo}</td>
	 </tr>
	<tr>
		<td class="form-left" style="width:190px"><transys:label code="Dumpster Price Total:" /><span
			class="errorMessage"></span></td>
		<td class="td-static" id="totalDumpsterPrice">${totalDumpsterPrice}</td>
		
		<td class="form-left"><transys:label code="Permit Fees Total:" /><span
			class="errorMessage"></span></td>
		<td class="td-static" id="totalPermitFees">${totalPermitFees}</td>
	</tr>
	<tr>
		<td class="form-left"><transys:label code="City Fees Total:" /><span
			class="errorMessage"></span></td>
		<td class="td-static" id="totalCityFees">${totalCityFees}</td>
		<td class="form-left" style="width:190px"><transys:label code="Over Weight Fees Total:" /><span
			class="errorMessage"></span></td>
		<td class="td-static" id="totalOverweightFees">${totalOverweightFees}</td>
	</tr>
	<tr>
		<td class="form-left"><transys:label code="Total Fees:" /></td>
		<td class="td-static" id="aggregatedTotalFees">${aggregatedTotalFees}</td>
	</tr>
</table>

<a href="/reports/ordersRevenueReport/generateOrdersRevenueReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/ordersRevenueReport/generateOrdersRevenueReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="ordersRevenueReport" id="ordersRevenueReport" class="tab-color">
	<transys:datatable urlContext="reports/ordersRevenueReports"  baseObjects="${ordersList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="orderRevenueReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<%-- <transys:textcolumn headerText="Payment Method" dataField="orderPayment[0].paymentMethod.method" />
		<transys:textcolumn headerText="Check #" dataField="orderPayment[0].checkNum" />
		<transys:textcolumn headerText="CC Reference #" dataField="orderPayment[0].ccReferenceNum" /> --%>
		<transys:textcolumn headerText="Dumpster Price" dataField="orderFees.dumpsterPrice" />
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" />
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" />
		<transys:textcolumn headerText="Overweight Fee" dataField="orderFees.overweightFee" />
		<transys:textcolumn headerText="Additional Fee" dataField="orderFees.totalAdditionalFees" />
		<transys:textcolumn headerText="Total Fees" dataField="orderFees.totalFees" />
	</transys:datatable>
	<%session.setAttribute("orderRevenueReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

