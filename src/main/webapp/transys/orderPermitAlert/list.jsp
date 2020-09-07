<%@include file="/common/taglibs.jsp"%>
<br/>
<h5 style="margin-top: -15px; !important">Order Permits Alert</h5>
<form:form action="${ctx}/orderPermitAlert/list.do" method="get" name="orderPermitsAlertSearchForm" id="orderPermitsAlertSearchForm">
	<table width="100%" id="form-table">
		<tr>
		  	<td class="form-left">End Date From</td>
			<td class="wide"><input class="flat" id="datepicker6" name="permit.endDateFrom" value="${sessionScope.searchCriteria.searchMap['permit.endDateFrom']}" style="width: 175px" /></td>
			<td class="form-left">End Date To</td>
		    <td class="wide"><input class="flat" id="datepicker7" name="permit.endDateTo" value="${sessionScope.searchCriteria.searchMap['permit.endDateTo']}" style="width: 175px" /></td>
	 	</tr>
		<tr>
			<td class="form-left">Delivery Address</td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress" name="order.deliveryAddress.id" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.deliveryAddress.id'] == aDeliveryAddress.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.fullLine}</option>
					</c:forEach>
				</select>
			</td>
			<!-- 
		  	<td class="form-left form-left-ext">Delivery Addrs Line1</td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress" name="order.deliveryAddress.line1" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${deliveryAddressesLine1}" var="aDeliveryAddressLine1">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.deliveryAddress.line1'] == aDeliveryAddressLine1}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${aDeliveryAddressLine1}" ${selected}>${aDeliveryAddressLine1}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left form-left-ext"><transys:label code="Delivery Addrs Line2"/></td>
			<td>
				<select class="flat form-control input-sm" id="deliveryStreet" name="order.deliveryAddress.line2" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${deliveryAddressesLine2}" var="aDeliveryAddressLine2">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.deliveryAddress.line2'] == aDeliveryAddressLine2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${aDeliveryAddressLine2}" ${selected}>${aDeliveryAddressLine2}</option>
					</c:forEach>
				</select>
			</td> 
			 -->
			<td colspan=10></td>
		 </tr>
		 <!--  
	 	 <tr>
		 	<td class="form-left"><transys:label code="Contact Name"/></td>
			<td>
				<select class="flat form-control input-sm" id="contactName" name="order.customer.contactName" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${contactName}" var="contactNameVar">
						<c:if test="${not empty contactNameVar}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['order.customer.contactName'] == contactNameVar}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${contactNameVar}" ${selected}>${contactNameVar}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left"><transys:label code="Phone Number"/></td>
			<td>
				<select class="flat form-control input-sm" id="phone" name="order.customer.phone" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${phone}" var="phoneVar">
						<c:if test="${not empty phoneVar}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['order.customer.phone'] == phoneVar}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${phoneVar}" ${selected}>${phoneVar}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
		 </tr>
		 -->
		 <tr>
		  	<td class="form-left">Permit Number</td>
			<td>
				<select class="flat form-control input-sm" id="permitNumber" name="permit.number" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:if test="${sessionScope.searchCriteria.searchMap['permit.number'] == 'To Be Assigned'}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="To Be Assigned" ${selected}>To Be Assigned</option>
					<c:forEach items="${permit}" var="permitNumber">
						<c:if test="${permitNumber != 'To Be Assigned'}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permit.number'] == permitNumber}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${permitNumber}" ${selected}>${permitNumber}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Permit Status</td>
			<td>
				<select class="flat form-control input-sm" id="permitStatus" name="permit.status.status" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${permitStatus}" var="permitStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['permit.status.status'] == permitStatus.status}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${permitStatus.status}" ${selected}>${permitStatus.status}</option>
					</c:forEach>
				</select>
			</td>
			<td colspan=10></td>
		 </tr>
		 <tr>
			<td class="form-left">Order Number</td>
			<td>
				<!--
				<select id="orderNumber" name="order.id" class="flat form-control input-sm" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${order}" var="order">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.id'] == order}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${order}" ${selected}>${order}</option>
					</c:forEach>
				</select>
				-->
				<input class="flat" id="orderNumber" name="order.id" value="${sessionScope.searchCriteria.searchMap['order.id']}" style="width: 175px !important" />
			</td>
			<td class="form-left">Order Status</td>
			<td>
				<select id="orderStatus" name="order.orderStatus" class="flat form-control input-sm" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${orderStatuses}" var="anOrderStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.orderStatus'] == anOrderStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrderStatus.id}" ${selected}>${anOrderStatus.status}</option>
					</c:forEach>
				</select>
			</td>
		 </tr>
		<tr>
			<td></td>
			<td>
				<input type="submit" id="submitOrderPermitsAlertSearch" value="Search" class="btn btn-primary btn-sm btn-sm-ext" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/> 
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderPrrmitsAlertServiceForm" id="orderPrrmitsAlertServiceForm" class="tab-color">
	<transys:datatable urlContext="orderPermitAlert" deletable="false"
		baseObjects="${orderPermitList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="list.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportXls="true" dataQualifier="orderPermitAlert">
		<transys:textcolumn headerText="Del. L1" dataField="permit.deliveryAddress.line1"/>
		<transys:textcolumn headerText="Del. L2" dataField="permit.deliveryAddress.line2" />
		<transys:textcolumn headerText="Customer Name" dataField="order.customer.companyName" />
		<transys:anchorcolumn headerText="Order#" linkUrl="javascript:processGoToOrder('{order.id}');" linkText="{order.id}" dataField="order.id" type="java.lang.Long"/>
		<transys:textcolumn headerText="Contact" dataField="order.customer.contactName" />
		<transys:textcolumn headerText="Phone#" dataField="order.customer.phone" />
		<transys:textcolumn headerText="Order Status" dataField="order.orderStatus.status" />
		<transys:textcolumn headerText="Delivery Date" dataField="order.deliveryDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Permit#" dataField="permit.number" />
		<transys:textcolumn headerText="Permit Status" dataField="permit.status.status" />
		<transys:textcolumn headerText="Start Date" dataField="permit.startDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="End Date" dataField="permit.endDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyyy"/>
		<transys:anchorcolumn headerText="New Permit" linkText="Add New Permit" linkUrl="/permit/createModal.do?id={id}" target="#addNewPermitModal" />
		<transys:anchorcolumn headerText="Add Order Notes" linkText="Add Order Notes" linkUrl="/order/orderNotesCreateModal.do?id={id}" target="#addOrderNotesModal" />
	</transys:datatable>
	<%session.setAttribute("orderPermitAlertColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<div class="modal fade" id="addNewPermitModal" role="dialog" data-backdrop="static" data-remote="false" data-toggle="modal">
	<div class="modal-dialog" style="width:60% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add New Permit</h4>
       			<div id="permitModalMessages">
       				<div id="permitModalErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
       				<div id="permitModalSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
       			</div>
      		 </div>	
			<div class="modal-body" id="addNewPermitModalBody"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="addOrderNotesModal" role="dialog" data-backdrop="static" data-remote="false" data-toggle="modal">
	<div class="modal-dialog" style="width:60% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add Order Notes</h4>
       			<div id="orderNotesModalMessages">
       				<div id="orderNotesModalErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
       				<div id="orderNotesModalSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
       			</div>
      		 </div>	
			<div class="modal-body" id="addOrderNotesModalBody"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
$("#orderPermitsAlertSearchForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	$("#orderPermitsAlert").html(responseData)
        }
    });
    
    ev.preventDefault();
});

$("#addNewPermitModal").on("show.bs.modal", function(e) {
	$("#addNewPermitModalBody").html("");
	clearPermitModalMessages();
	
    var link = $(e.relatedTarget).attr("href");
    $(this).find("#addNewPermitModalBody").load(link);
});

$("#addOrderNotesModal").on("show.bs.modal", function(e) {
	$("#addOrderNotesModalBody").html("");
	clearOrderNotestModalMessages();
	
    var link = $(e.relatedTarget).attr("href");
    $(this).find("#addOrderNotesModalBody").load(link);
});

$("#addNewPermitModal").on("hidden.bs.modal", function(e) {
	$("#addNewPermitModalBody").html("");
	clearPermitModalMessages();
});

$("#addOrderNotesModal").on("hidden.bs.modal", function(e) {
	$("#addOrderNotesModalBody").html("");
	clearOrderNotestModalMessages();
});

function displayPermitModalErrorMessage(message) {
	var errorMsgHtml = "<img src=\"${ctx}/images/iconWarning.gif\" alt=\"Warning\" class=\"icon\" />" 
					 + "&nbsp;" 
					 + message;
	$("#permitModalErrorMessage").html(errorMsgHtml);
}

function displayPermitModalSuccessMessage(message) {
	var successMsgHtml = "<img src=\"${ctx}/images/iconInformation.gif\" alt=\"Information\" class=\"icon\" />" 
					   + "&nbsp;" 
					   + message;
	$("#permitModalSuccessMessage").html(successMsgHtml);
}

function clearPermitModalErrorMessage() {
	$("#permitModalErrorMessage").html("");
}

function clearPermitModalSuccessMessage() {
	$("#permitModalSuccessMessage").html("");
}

function clearPermitModalMessages() {
	clearPermitModalErrorMessage();
	clearPermitModalSuccessMessage();
}

function displayOrderNotesModalErrorMessage(message) {
	var errorMsgHtml = "<img src=\"${ctx}/images/iconWarning.gif\" alt=\"Warning\" class=\"icon\" />" 
					 + "&nbsp;" 
					 + message;
	$("#orderNotesModalErrorMessage").html(errorMsgHtml);
}

function displayOrderNotesModalSuccessMessage(message) {
	var successMsgHtml = "<img src=\"${ctx}/images/iconInformation.gif\" alt=\"Information\" class=\"icon\" />" 
					   + "&nbsp;" 
					   + message;
	$("#orderNotesModalSuccessMessage").html(successMsgHtml);
}

function clearOrderNotesModalErrorMessage() {
	$("#orderNotesModalErrorMessage").html("");
}

function clearOrderNotesModalSuccessMessage() {
	$("#orderNotesModalSuccessMessage").html("");
}

function clearOrderNotestModalMessages() {
	clearOrderNotesModalErrorMessage();
	clearOrderNotesModalSuccessMessage();
}
</script>

