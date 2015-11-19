<%@include file="/common/taglibs.jsp"%>
<script>
function populateCustomerDeliveryAddress() {
	var customerSelect =  $('#customer');
	var deliveryAddressSelect = $('#deliveryAddress');
	
	deliveryAddressSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
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
<h5 style="margin-top: -15px; !important">Manage Orders</h5>
<form:form action="list.do" method="get" name="orderSearchForm" id="orderSearchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Order #</td>
			<td>
				<select class="flat form-control input-sm" id="id" name="id" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${orderIds}" var="anOrderId">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == anOrderId}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderId}" ${selected}>${anOrderId}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Order Status</td>
			<td>
				<select class="flat form-control input-sm" id="orderStatus" name="orderStatus" style="width: 175px !important">
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
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Customer" /></td>
			<td>
				<select class="flat form-control input-sm" id="customer" name="customer" style="width: 175px !important" onChange="return populateCustomerDeliveryAddress();">
					<option value="">----Please Select----</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left"><transys:label code="Delivery Address" /></td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px !important">
					<option value="">----Please Select----</option>
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
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id=deliveryContactPhone1 name="deliveryContactPhone1" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${deliveryContactPhones}" var="aDeliveryContactPhone">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryContactPhone1'] == aDeliveryContactPhone}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryContactPhone}" ${selected}>${aDeliveryContactPhone}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Dumpster Size" /></td>
			<td>
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">----Please Select----</option>
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
			<td class="form-left"><transys:label code="Dumpster #" /></td>
			<td>
				<select class="flat form-control input-sm" id="dumpster" name="dumpster" style="width: 175px !important">
					<option value="">----Please Select----</option>
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
			<td class="form-left">Delivery Date From</td>
			<td>
				<input class="flat" id="datepicker" name="deliveryDateFrom" value="${sessionScope.searchCriteria.searchMap['deliveryDateFrom']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Delivery Date To</td>
			<td>
				<input class="flat" id="datepicker1" name="deliveryDateTo" value="${sessionScope.searchCriteria.searchMap['deliveryDateTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Pick Up Date From" /></td>
			<td class="wide">
				<input class="flat" id="datepicker2" name="pickupDateFrom" value="${sessionScope.searchCriteria.searchMap['pickupDateFrom']}" style="width: 175px !important" />
			</td>
			<td class="form-left"><transys:label code="Pick Up Date To" /></td>
			<td>
				<input class="flat" id="datepicker3" name="pickupDateTo" value="${sessionScope.searchCriteria.searchMap['pickupDateTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td class="form-left form-left-ext"><transys:label code="Balance Amount Due" /></td>
			<td>
				<c:set var="balanceAmountDueYesChecked" value="" />
				<c:if test="${sessionScope.searchCriteria.searchMap['balanceAmountDue'] == '!=0.00'}">
					<c:set var="balanceAmountDueYesChecked" value="checked" />
				</c:if>
				<c:set var="balanceAmountDueNoChecked" value="" />
				<c:if test="${sessionScope.searchCriteria.searchMap['balanceAmountDue'] == '0.00'}">
					<c:set var="balanceAmountDueNoChecked" value="checked" />
				</c:if>
				<span>
					<input id="balanceAmountDueYes" name="balanceAmountDue" type="radio" value="!=0.00" ${balanceAmountDueYesChecked} />
					&nbsp;<label style="font-weight: normal; font-size: 13px;" for="balanceAmountDueYes">Yes</label>
				</span>
				&nbsp;
				<span>
					<input id="balanceAmountDueNo" name="balanceAmountDue" type="radio" value="0.00"  ${balanceAmountDueNoChecked} />
					&nbsp;<label style="font-weight: normal; font-size: 13px;" for="balanceAmountDueNo">No</label>
				</span>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['orderSearchForm'].submit();"
					value="<transys:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderServiceForm" id="orderServiceForm" class="tab-color">
	<transys:datatable urlContext="order" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="manageOrders">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Contact" dataField="deliveryContactName" />
		<transys:textcolumn headerText="Phone" dataField="deliveryContactPhone1" />
		<transys:textcolumn headerText="Del L1" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="Del L2" dataField="deliveryAddress.line2" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dmpstr Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dmpstr #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Del Dt" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Dt" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Dmpstr Price" dataField="orderFees.dumpsterPrice" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="OverWt Fee" dataField="orderFees.overweightFee" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Addnl Fee" dataField="orderFees.totalAdditionalFees" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Total Amt" dataField="orderFees.totalFees" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
	</transys:datatable>
	<%session.setAttribute("manageOrdersColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


