<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Delivery/Pickup Report</h4>
<form:form action="list.do" method="get" name="searchForm" id="deliveryPickUpReportSearchForm" >
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
	 <tr>
		  <td class="form-left "><transys:label code="Delivery Date From"/></td>
		  <td class="wide"><input class="flat" id="datepicker1" name="deliveryDateFrom" style="width: 175px" /></td>
				
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
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:if test="${not empty aDeliveryAddress.line1}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress'] == aDeliveryAddress.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.fullLine}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['deliveryPickUpReportSearchForm'].submit();"
					value="<transys:label code="Preview"/>" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
		<tr><td></td></tr>
	</table>
</form:form>
<hr class="hr-ext">
<table class="table" id="form-table"> 
	<tr><td></td></tr>
	<tr>
		<td class="form-left">Total Dumpsters/Boxes Delivered (or Picked up) for:</td>
	</tr>
</table>
<table class="table" id="form-table"> 	
	<tr>
		<td class="form-left form-left-ext"><transys:label code="Delivery Date Range:" /></td>
		<td class="wide td-static" id="deliveryDateRange">${deliveryDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${deliveryDateTo}</td>

		<td class="form-left  td-static">Pickup Date Range:</td>
		<td id="pickupDateRange" class="td-static">${pickupDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${pickupDateTo}</td>
	</tr>
	<tr>
		<td class="td-static">${dumpsterSizeAggregation}</td>
	</tr> 
</table>

<a href="generateDeliveryPickupReport.do?type=xls"><img src="${ctx}/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="generateDeliveryPickupReport.do?type=pdf"><img src="${ctx}/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="deliveryPickupReport" id="deliveryPickupReport" class="tab-color">
	<transys:datatable urlContext="reports/deliveryPickupReport"  baseObjects="${ordersList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="deliveryPickupReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.fullLine" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
	</transys:datatable>
	<%session.setAttribute("deliveryPickupReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

