<%@include file="/common/taglibs.jsp"%>
<script>
function populateCustomerDeliveryAddress() {
	var customerSelect =  $('#customer');
	var deliveryAddressSelect = $('#deliveryAddress');
	
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "------Please Select------" +'</option>');
	deliveryAddressSelect.append(firstOption);
	
	var customerId = customerSelect.val();
	if (customerId == "") {
		return false;
	}
	
	$.ajax({
  		url: "customerDeliveryAddress.do?id=" + customerId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
    	   	var addressList = jQuery.parseJSON(responseData);
    	   	$.each(addressList, function () {
    	   	    $("<option />", {
    	   	        val: this.id,
    	   	        text: this.fullLine
    	   	    }).appendTo(deliveryAddressSelect);
    	   	});
		}
	}); 
}
</script>
<br />
<h4 style="margin-top: -15px; !important">Manage Orders</h4>
<form:form action="list.do" method="get" name="orderSearchForm" id="orderSearchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Order #" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="id" name="id" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${orderIds}" var="anOrderId">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == anOrderId.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderId.id}" ${selected}>${anOrderId.id}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Order Status" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px !important">
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
			<td colspan=10></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Customer" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="customer" name="customer" style="width: 175px !important" onChange="return populateCustomerDeliveryAddress();">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Delivery Address" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress'] == aDeliveryAddress.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.fullLine}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Phone Number" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id=deliveryContactPhone1 name="deliveryContactPhone1" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
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
			<td align="${left}" class="form-left"><transys:label code="Dumpster Size" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${dumpsterSizes}" var="aDumpsterSize">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aDumpsterSize.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterSize.id}" ${selected}>${aDumpsterSize.size}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Dumpster #" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="dumpster" name="dumpster" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${dumpsters}" var="aDumpster">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['dumpster'] == aDumpster.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpster.id}" ${selected}>${aDumpster.dumpsterNum}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Delivery Date From" /></td>
			<td align="${left}" class="wide">
				<input class="flat" id="datepicker" name="deliveryDateFrom" value="${sessionScope.searchCriteria.searchMap['deliveryDateFrom']}" style="width: 175px !important" />
			</td>
			<td align="${left}" class="form-left"><transys:label code="Delivery Date To" /></td>
			<td align="${left}" class="wide">
				<input class="flat" id="datepicker1" name="deliveryDateTo" value="${sessionScope.searchCriteria.searchMap['deliveryDateTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Pick Up Date From" /></td>
			<td align="${left}" class="wide">
				<input class="flat" id="datepicker2" name="pickupDateFrom" value="${sessionScope.searchCriteria.searchMap['pickupDateFrom']}" style="width: 175px !important" />
			</td>
			<td align="${left}" class="form-left"><transys:label code="Pick Up Date To" /></td>
			<td align="${left}" class="wide">
				<input class="flat" id="datepicker3" name="pickupDateTo" value="${sessionScope.searchCriteria.searchMap['pickupDateTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" class="btn btn-primary btn-sm" onclick="document.forms['orderSearchForm'].submit();"
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
		exportPdf="true" exportXls="true" dataQualifier="manageOrders">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Delivery #" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="Delivery Street" dataField="deliveryAddress.line2" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dmpstr Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dmpstr #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" />
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" />
		<transys:textcolumn headerText="Dmpstr Price" dataField="orderFees.dumpsterPrice" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="OverWt Fee" dataField="orderFees.overweightFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Addnl Fee" dataField="orderFees.totalAdditionalFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Total Amt" dataField="orderFees.totalFees" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Amt. Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Bal. Due" dataField="balanceAmountDue" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
	</transys:datatable>
	<%session.setAttribute("manageOrdersColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


