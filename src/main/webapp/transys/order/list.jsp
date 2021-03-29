<%@include file="/common/taglibs.jsp"%>
<script>
function processExchange(orderId) {
	if (!confirm("Do you want to Exchange Order # " + orderId + "?")) {
		return;
	}
	
	$.ajax({
        type: "GET",
        url: "isOrderExchangable.do?" + "id=" + orderId,
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData != "true") {
        		showAlertDialog("Data Validation", responseData);
        		return false;
        	} 
        	document.location = "${ctx}/order/createExchange.do?id=" + orderId;
        }
    });
}

function processCancel(url, orderId) {
	if (!confirm("Do you want to Cancel Order # " + orderId + "?")) {
		return;
	}
	
	$.ajax({
  		url: "cancel.do?id=" + orderId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") != -1) {
        		var xpath = ".//form[@id='orderServiceForm']/table[@class='datagrid']/tbody/tr/td[text()='"
                    + orderId + "']/following-sibling::td[text()='Open']";
				var statusTd = document.evaluate(xpath, document, null, XPathResult.ANY_TYPE, null).iterateNext();
				statusTd.textContent = "Canceled";
        	}
       		
        	showAlertDialog("Order update", responseData);
		}
	});
}

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

function processCreateInvoice(orderId) {
	document.location.href = "${ctx}/invoice/createInvoiceSearch.do?orderId="+orderId;
}
</script>
<br />
<h5 style="margin-top: -15px; !important">Manage Orders</h5>
<form:form action="search.do" method="get" name="orderSearchForm" id="orderSearchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Order #</td>
			<td>
				<!--
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
				-->
				<input class="flat" id="id" name="id" value="${sessionScope.searchCriteria.searchMap['id']}" style="width: 175px !important" />
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
			<!-- 
			<td class="form-left"><transys:label code="Delivery Street" /></td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress.line2" name="deliveryAddress.line2" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${deliveryAddressStreets}" var="aDeliveryAddressStreet">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line2'] == aDeliveryAddressStreet}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddressStreet}" ${selected}>${aDeliveryAddressStreet}</option>
					</c:forEach>
				</select>
			</td>
			 -->
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
			<td class="form-left">Pick Up Date From</td>
			<td class="wide">
				<input class="flat" id="datepicker2" name="pickupDateFrom" value="${sessionScope.searchCriteria.searchMap['pickupDateFrom']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Pick Up Date To</td>
			<td>
				<input class="flat" id="datepicker3" name="pickupDateTo" value="${sessionScope.searchCriteria.searchMap['pickupDateTo']}" style="width: 175px !important" />
			</td>
		</tr>
		<tr>
			<td class="form-left form-left-ext">Balance Amount Due</td>
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
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" 
					onclick="document.forms['orderSearchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderServiceForm" id="orderServiceForm" class="tab-color">
	<transys:datatable urlContext="order" deletable="false"
		editable="true" cancellable="true" insertable="true" itemPrintable="true"  
		manageDocs="true" baseObjects="${list}"
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
		<transys:textcolumn headerText="Dmp. Size" dataField="dumpsterSize.size" width="34px"/>
		<transys:textcolumn headerText="Dmp. #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Del Dt" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Dt" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Dmp. Price" dataField="orderFees.dumpsterPrice"  width="34px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Ton. Fee" dataField="orderFees.tonnageFee"  width="30px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Permit Fee" dataField="orderFees.totalPermitFees"  width="35px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="City Fee" dataField="orderFees.cityFee"  width="32px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Over. Fee" dataField="orderFees.overweightFee"  width="34px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Add. Fee" dataField="orderFees.totalAdditionalFees"  width="34px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Disc." dataField="orderFees.discountAmount" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Tot Amt" dataField="orderFees.totalFees" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Status" dataField="orderStatus.status"/>
		<transys:textcolumn headerText="Doc" dataField="hasDocs"/>
		<transys:textcolumn headerText="Inv" dataField="invoiced"/>
		<transys:imagecolumn headerText="EXC" linkUrl="javascript:processExchange('{id}');" imageSrc="${imageCtx}/exchange.png" HAlign="center" title="Exchange"/>
	</transys:datatable>
	<%session.setAttribute("manageOrdersColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


