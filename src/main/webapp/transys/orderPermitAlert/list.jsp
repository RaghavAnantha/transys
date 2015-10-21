<%@include file="/common/taglibs.jsp"%>
<br/>
<h5 style="margin-top: -15px; !important">Order Permits Alert</h5>
<form:form action="/orderPermitAlert/list.do" method="get" name="orderPermitsAlertSearchForm" id="orderPermitsAlertSearchForm">
	<table width="100%" id="form-table">
		<tr>
		  <td class="form-left"><transys:label code="End Date From"/></td>
		  <td class="wide"><input class="flat" id="datepicker6" name="permit.endDateFrom" value="${sessionScope.searchCriteria.searchMap['permit.endDateFrom']}" style="width: 175px" /></td>
				
		  <td class="form-left"><transys:label code="End Date To"/></td>
	      <td class="wide"><input class="flat" id="datepicker7" name="permit.endDateTo" value="${sessionScope.searchCriteria.searchMap['permit.endDateTo']}" style="width: 175px" /></td>
	 	</tr>
		<tr>
		  <td class="form-left"><transys:label code="Delivery Address #"/></td>
				<td><select class="flat form-control input-sm" id="deliveryAddress" name="order.deliveryAddress.line1" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${allDeliveryAddresses}" var="deliveryAddressVar">
						 <c:set var="selected" value=""/>
						 <c:if test="${sessionScope.searchCriteria.searchMap['order.deliveryAddress.line1'] == deliveryAddressVar.line1}">
							<c:set var="selected" value="selected"/>
						</c:if> 
							<option value="${deliveryAddressVar.line1}" ${selected}>${deliveryAddressVar.line1}</option>
					</c:forEach>
				</select>
				</td>
			
			<td class="form-left"><transys:label code="Delivery Street"/></td>
			<td><select class="flat form-control input-sm" id="deliveryStreet" name="order.deliveryAddress.line2" style="width: 175px !important">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${allDeliveryAddresses}" var="deliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.deliveryAddress.line2'] == deliveryAddress.line2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${deliveryAddress.line2}" ${selected}>${deliveryAddress.line2}</option>
				</c:forEach>
			</select>
			</td> 
			<td colspan=10></td>
	 </tr>
	 
 	 <tr>
		  <td class="form-left"><transys:label code="Contact Name"/></td>
				<td><select class="flat form-control input-sm" id="contactName" name="order.customer.contactName" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${customer}" var="customer">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['order.customer.contactName'] == customer.contactName}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${customer.contactName}" ${selected}>${customer.contactName}</option>
					</c:forEach>
				</select>
				</td>
			
			<td class="form-left"><transys:label code="Phone Number"/></td>
			<td><select class="flat form-control input-sm" id="phone" name="order.customer.phone" style="width: 175px !important">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['order.customer.phone'] == customer.phone}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${customer.phone}" ${selected}>${customer.phone}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
	 <tr>
		  <td class="form-left"><transys:label code="Permit Number"/></td>
				<td><select class="flat form-control input-sm" id="permitNumber" name="permit.number" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${permit}" var="permit">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permit.number'] == permit.number}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${permit.number}" ${selected}>${permit.number}</option>
					</c:forEach>
				</select>
				</td>
			
			<td class="form-left"><transys:label code="Permit Status"/></td>
			<td><select class="flat form-control input-sm" id="permitStatus" name="permit.status.status" style="width: 175px !important">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${permitStatus}" var="permitStatus">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['permit.status.status'] == permitStatus.status}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${permitStatus.status}" ${selected}>${permitStatus.status}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		<td class="form-left"><transys:label code="Order Number"/></td>
			<td><select id="orderNumber" name="order.id" class="flat form-control input-sm" style="width: 175px !important">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${order}" var="order">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['order.id'] == order.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="${order.id}" ${selected}>${order.id}</option>
				</c:forEach>
			</select>
		</td>
		<td class="form-left"><transys:label code="Order Status"/></td>
		<td><select id="orderStatus" name="order.orderStatus" class="flat form-control input-sm" style="width: 175px !important">
				<option value="">------<transys:label code="Please Select"/>------</option>
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
				<input type="submit" id="submitOrderPermitsAlertSearch" onclick="return validateForm()" value="<transys:label code="Search"/>" class="btn btn-primary btn-sm" /> 
			</td>
		</tr>
	</table>
</form:form>

<form:form name="orderPrrmitsAlertServiceForm" id="orderPrrmitsAlertServiceForm" class="tab-color">
	<transys:datatable urlContext="orderPermitAlert" deletable="false"
		baseObjects="${orderPermitList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportXls="true" dataQualifier="orderPermitAlert">
		<transys:textcolumn headerText="Del. Address #" dataField="order.deliveryAddress.line1" width="100px"/>
		<transys:textcolumn headerText="Delivery Address Street" dataField="order.deliveryAddress.line2" />
		<transys:textcolumn headerText="Customer Name" dataField="order.customer.companyName" />
		<transys:textcolumn headerText="Order#" dataField="order.id" type="java.lang.Long" />
		<transys:textcolumn headerText="Contact" dataField="order.customer.contactName" />
		<transys:textcolumn headerText="Phone#" dataField="order.customer.phone" />
		<transys:textcolumn headerText="Order tatus" dataField="order.orderStatus.status" />
		<transys:textcolumn headerText="Delivery Date" dataField="order.deliveryDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="Permit#" dataField="permit.number" />
		<transys:textcolumn headerText="Permit Status" dataField="permit.status.status" />
		<transys:textcolumn headerText="Start Date" dataField="permit.startDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="End Date" dataField="permit.endDate" type="java.sql.Timestamp" dataFormat="MM/dd/yyy"/>
		<transys:anchorcolumn headerText="New Permit" linkText="Add New Permit" linkUrl="/permit/createModal.do" target="#addNewPermitModal" />
		<transys:anchorcolumn headerText="Add Order Notes" linkText="Add Order Notes" linkUrl="/order/orderNotesCreateModal.do" target="#addOrderNotesModal" />
	</transys:datatable>
	<%session.setAttribute("orderPermitAlertColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<div class="modal fade" id="addNewPermitModal" role="dialog" data-backdrop="static" data-remote="false" data-toggle="modal">
	<div class="modal-dialog" style="width:90% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add New Permit</h4>
       			<!-- <div id="permitValidations" style="color:red"></div> -->
      		 </div>	
			<div class="modal-body" id="addNewPermitModalBody"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="addOrderNotesModal" role="dialog" data-backdrop="static" data-remote="false" data-toggle="modal">
	<div class="modal-dialog" style="width:90% !important">
		<div class="modal-content">
		 	<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title">Add Order Notes</h4>
       			<div id="addOrderNotesModalValidations" style="color:red"></div>
      		 </div>	
			<div class="modal-body" id="addOrderNotesModalBody"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
function validateForm() {
	return true;
}

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
    var link = $(e.relatedTarget).attr("href");
    $(this).find("#addNewPermitModalBody").load(link);
});

$("#addOrderNotesModal").on("show.bs.modal", function(e) {
    var link = $(e.relatedTarget).attr("href");
    $(this).find("#addOrderNotesModalBody").load(link);
});
</script>

