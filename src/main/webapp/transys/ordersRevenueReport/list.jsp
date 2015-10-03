<%@include file="/common/taglibs.jsp"%>
<br/>
<h4 style="margin-top: -15px; !important">Orders Revenue Report</h4>
<form:form action="list.do" method="get" name="searchForm" id="ordersRevenueReportSearchForm">
	<table width="100%" id="form-table">
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Order Date From"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker1" name="createdAtFrom" style="width: 175px" /></td>
				
		  <td align="${left}" class="form-left"><transys:label code="Order Date To"/></td>
	      <td align="${left}" class="wide"><input class="flat" id="datepicker2" name="createdAtTo" style="width: 175px" /></td>
			
	 </tr>
	
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button" class="btn btn-primary btn-sm" onclick="document.forms['ordersRevenueReportSearchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>

<hr>
<table width="100%" id="form-table">
	 <tr>
		<td class="form-left"><transys:label code="Order Date Range:" /><span class="errorMessage"></span></td>
		  <td align="${left}" id="orderDateRange" >${orderDateFrom} To ${orderDateTo}</td>
	 </tr>
	<tr>
		<td class="form-left"><transys:label code="Dumpster Price Total:" /><span
			class="errorMessage"></span></td>
		<td align="${left}" id="totalDumpsterPrice">${totalDumpsterPrice}</td>
		
		<td class="form-left"><transys:label code="Permit Fees Total:" /><span
			class="errorMessage"></span></td>
		<td align="${left}" id="totalPermitFees">${totalPermitFees}</td>
	</tr>

	<tr>
		<td class="form-left"><transys:label code="City Fees Total:" /><span
			class="errorMessage"></span></td>
		<td align="${left}" id="totalCityFees">${totalCityFees}</td>
		<td class="form-left"><transys:label code="Over Weight Fees Total:" /><span
			class="errorMessage"></span></td>
		<td align="${left}" id="totalOverweightFees">${totalOverweightFees}</td>
	</tr>
	<tr>
		<td class="form-left"><transys:label code="TOTAL Fees:" /><span
			class="errorMessage"></span></td>
		<td align="${left}" id="aggregatedTotalFees">${aggregatedTotalFees}</td>
	</tr>
</table>

<a href="/ordersRevenueReport/generateOrdersRevenueReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/ordersRevenueReport/generateOrdersRevenueReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="ordersRevenueReport" id="ordersRevenueReport" class="tab-color">
	<transys:datatable urlContext="ordersRevenueReports"  baseObjects="${ordersList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="orderRevenueReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Payment Method" dataField="orderPaymentInfo.paymentMethod.method" />
		<transys:textcolumn headerText="Check #" dataField="orderPaymentInfo.checkNum" />
		<transys:textcolumn headerText="CC Reference #" dataField="orderPaymentInfo.ccReferenceNum" />
		<transys:textcolumn headerText="Dumpster Price" dataField="orderPaymentInfo.dumpsterPrice" />
		<transys:textcolumn headerText="City Fee" dataField="orderPaymentInfo.cityFee" />
		<transys:textcolumn headerText="Permit Fee" dataField="orderPaymentInfo.permitFees" />
		<transys:textcolumn headerText="Overweight Fee" dataField="orderPaymentInfo.overweightFee" />
		<transys:textcolumn headerText="Additional Fee" dataField="orderPaymentInfo.additionalFee" />
		<transys:textcolumn headerText="Total Fees" dataField="orderPaymentInfo.totalFees" />
	</transys:datatable>
	<%session.setAttribute("orderRevenueReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

