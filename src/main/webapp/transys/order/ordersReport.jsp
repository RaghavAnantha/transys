<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Orders Report</h5>
<form:form action="orderReport.do" method="get" name="orderReportSearchForm" id="orderReportSearchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Company Name" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="compName" name="customer" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customers}" var="name">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['customer'] == name.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${name.id}" ${selected}>${name.companyName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Delivery Date from" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=deliveryDateFrom name="deliveryDate" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['deliveryDate'] == anOrder.deliveryDate}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.deliveryDate}" ${selected}>${anOrder.deliveryDate}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Delivery Date to" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=deliveryDateTo name="deliveryDate" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['deliveryDate'] == anOrder.deliveryDate}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.deliveryDate}" ${selected}>${anOrder.deliveryDate}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Contact Name" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="contactName" name="deliveryContactName" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="aOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['deliveryContactName'] == aOrder.deliveryContactName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aOrder.deliveryContactName}" ${selected}>${aOrder.deliveryContactName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Order Status" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${orderStatuses}" var="oStatus">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['orderStatus'] == oStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${oStatus.id}" ${selected}>${oStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Phone Number" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=deliveryContactPhone1 name="deliveryContactPhone1" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['deliveryContactPhone1'] == anOrder.deliveryContactPhone1}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.deliveryContactPhone1}" ${selected}>${anOrder.deliveryContactPhone1}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" class="btn btn-primary btn-sm" onclick="document.forms['orderReportSearchForm'].submit();"
					value="<transys:label code="Preview"/>" />
			</td>
		</tr>
	</table>
</form:form>

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
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" />
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" />
		<transys:textcolumn headerText="Pymt. method" dataField="orderPayment[0].paymentMethod.method" />	
		<transys:textcolumn headerText="Dumpster Price" dataField="orderFees.dumpsterPrice" />
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" />
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" />
		<transys:textcolumn headerText="OvrWt. Fee" dataField="orderFees.overweightFee" />
		<transys:textcolumn headerText="Total Fees" dataField="orderFees.totalFees" />	
	</transys:datatable>
	<%session.setAttribute("orderReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
