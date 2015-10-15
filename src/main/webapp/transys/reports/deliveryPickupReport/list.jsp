<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Delivery/ Pickup Report</h4>
<form:form action="list.do" method="get" name="searchForm" id="deliveryPickUpReportSearchForm" >
	<table width="100%" id="form-table">
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Delivery Date From"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker1" name="deliveryDateFrom" style="width: 175px" /></td>
				
		  <td align="${left}" class="form-left"><transys:label code="Delivery Date To"/></td>
	      <td align="${left}" class="wide"><input class="flat" id="datepicker2" name="deliveryDateTo" style="width: 175px" /></td>
	 </tr>
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Pickup Date From"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker3" name="pickupDateFrom" style="width: 175px" /></td>
				
		  <td align="${left}" class="form-left"><transys:label code="Pickup Date To"/></td>
	      <td align="${left}" class="wide"><input class="flat" id="datepicker4" name="pickupDateTo" style="width: 175px" /></td>
	 </tr>
	 <tr>
		<td align="${left}" class="form-left"><transys:label code="Delivery Address" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress'] == aDeliveryAddress.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.line1} , ${aDeliveryAddress.line2}</option>
					</c:forEach>
				</select>
			</td>
	</tr>
	<tr>
		<td align="${left}"></td>
		<td align="${left}"><input type="button" class="btn btn-primary btn-sm" onclick="document.forms['deliveryPickUpReportSearchForm'].submit();"
			value="<transys:label code="Preview"/>" /></td>
	</tr>
	</table>
</form:form>
<table id="aggregationTable" width="100%" id="form-table" >
	<tr>
		<td class="form-left"><transys:label code="Total Dumpsters/Boxes Delivered (or Picked up) for:" />
	</tr>
	<tr>
		<td class="form-left" style="width:350px"><transys:label code="Delivery Date Range:" /><span class="errorMessage"></span></td>
		<td align="${left}" id="deliveryDateRange">${deliveryDateFrom} To ${deliveryDateTo}</td>

		<td class="form-left" style="width:200px"><transys:label code="Pickup Date Range:" /><span class="errorMessage"></span></td>
		<td align="${left}" id="pickupDateRange">${pickupDateFrom} To ${pickupDateTo}</td>
	</tr>
	<tr>
		<td>${dumpsterSizeAggregation}</td>
	</tr> 
</table>

<a href="/reports/deliveryPickupReport/generateDeliveryPickupReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/deliveryPickupReport/generateDeliveryPickupReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="deliveryPickupReport" id="deliveryPickupReport" class="tab-color">
	<transys:datatable urlContext="reports/deliveryPickupReport"  baseObjects="${ordersList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="deliveryPickupReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" />
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" />
	</transys:datatable>
	<%session.setAttribute("deliveryPickupReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

