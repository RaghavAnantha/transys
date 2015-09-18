<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Orders</h4>
<form:form action="list.do" method="get" name="searchForm" id="orderSearchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Order #" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="id" name="id" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${orderIds}" var="anOrderId">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['id'] == anOrderId.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderId.id}" ${selected}>${anOrderId.id}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Order Status" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
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
			<td align="${left}" class="form-left"><transys:label code="Customer" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="customer" name="customer" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Delivery Address" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress'] == aDeliveryAddress.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.line1}</option>
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
			<td align="${left}" class="form-left"><transys:label code="Dumpster Size" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=dumpsterSize name="dumpsterSize" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${dumpsters}" var="aDumpster">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == anOrder.dumpsterSize}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpster.id}" ${selected}>${aDumpster.dumpsterSize}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Dumpster #" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id=dumpsterNum name="dumpster" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${dumpsters}" var="aDumpster">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterNum'] == aDumpster.dumpsterNum}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpster.id}" ${selected}>${aDumpster.dumpsterNum}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Delivery Date from" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=deliveryDate name="deliveryDate" style="width: 175px">
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
				<select class="flat form-control input-sm" id=deliveryDate name="deliveryDate" style="width: 175px">
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
			<td align="${left}" class="form-left"><transys:label code="Pick Up Date from" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=pickupDate name="pickupDate" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['pickupDate'] == anOrder.pickupDate}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.pickupDate}" ${selected}>${anOrder.pickupDate}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Pick Up Date to" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=pickupDate name="pickupDate" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${order}" var="anOrder">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['pickupDate'] == anOrder.pickupDate}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrder.pickupDate}" ${selected}>${anOrder.pickupDate}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" class="btn btn-primary btn-sm" onclick="document.forms['searchForm'].submit();"
					value="<transys:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderServiceForm" id="orderServiceForm" class="tab-color">
	<transys:datatable urlContext="order" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize" />
		<transys:textcolumn headerText="Dmpstr #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" />
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


