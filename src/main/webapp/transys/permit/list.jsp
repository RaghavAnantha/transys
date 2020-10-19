<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processGoToOrder(orderIds) {
	if (orderIds.indexOf(",") != -1) {
		orderIds = orderIds.substring(0, orderIds.indexOf(","));
	}
	window.location = "${ctx}/order/edit.do?id="+orderIds;
}

function processCancel(url, permitId) {
	if (!confirm("Do you want to Cancel Permit Id " + permitId + "?")) {
		return;
	}
	
	$.ajax({
  		url: "cancel.do?id=" + permitId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") != -1) {
        		var xpath = ".//form[@id='permitServiceForm']/table[@class='datagrid']/tbody/tr/td[text()='"
                    + permitId + "']/following-sibling::td[text()!='Assigned']";
				var statusTd = document.evaluate(xpath, document, null, XPathResult.ANY_TYPE, null).iterateNext();
				statusTd.textContent = "Canceled";
        	}
       		
        	showAlertDialog("Permit update", responseData);
		}
	});
}
</script>

<br/>
<h5 style="margin-top: -15px; !important">Manage Permits</h5>
<form:form action="list.do" method="get" name="permitSearchForm" id="permitSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="managePermit" />
		<jsp:param name="errorCtx" value="managePermit" />
	</jsp:include>
	<table id="form-table" class="table">
		<tr>
			<td class="form-left"><transys:label code="Delivery Address" /></td>
			<td>
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
			<!-- 
		  	<td class="form-left form-left-ext">Delivery Addrs Line1</td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress.line1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${deliveryAddressesLine1}" var="aDeliveryAddressLine1">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line1'] == aDeliveryAddressLine1}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${aDeliveryAddressLine1}" ${selected}>${aDeliveryAddressLine1}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left form-left-ext">Delivery Addrs Line2</td>
			<td><select class="flat form-control input-sm" id="deliveryStreet" name="deliveryAddress.line2" style="width: 175px !important">
				<option value="">------Please Select------</option>
				<c:forEach items="${deliveryAddressesLine2}" var="aDeliveryAddressLine2">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line2'] == aDeliveryAddressLine2}">
						<c:set var="selected" value="selected"/>
					</c:if> 
					<option value="${aDeliveryAddressLine2}" ${selected}>${aDeliveryAddressLine2}</option>
				</c:forEach>
			</select>
			</td>
			 -->
			<td colspan=10></td>
	 	</tr>
	 	<tr>
		  	<td class="form-left form-left-ext">Permit Addrs Line1</td>
			<td>
				<select class="flat form-control input-sm" id="permitAddress" name="permitAddress[0].line1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitAddresses}" var="aPermitAddress">
						<c:if test="${not empty aPermitAddress.line1}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permitAddress[0].line1'] == aPermitAddress.line1}">
								<c:set var="selected" value="selected"/>
							</c:if> 
							<option value="${aPermitAddress.line1}" ${selected}>${aPermitAddress.line1}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left form-left-ext">Permit Addrs Line2</td>
			<td><select class="flat form-control input-sm" id="permitStreet" name="permitAddress[0].line2" style="width: 175px !important">
				<option value="">------Please Select------</option>
				<c:forEach items="${permitAddresses}" var="aPermitAddress">
					<c:if test="${not empty aPermitAddress.line2}">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['permitAddress[0].line2'] == aPermitAddress.line2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${aPermitAddress.line2}" ${selected}>${aPermitAddress.line2}</option>
					</c:if>
				</c:forEach>
			</select>
			</td>
			<td colspan=10></td>
	 	</tr>
	 	<!-- 
 	 	<tr>
		  <td class="form-left">Contact Name</td>
		  <td>
				<select class="flat form-control input-sm" id="contactName" name="customer.contactName" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${contactName}" var="contactName">
						<c:if test="${not empty contactName}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.contactName'] == contactName}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${contactName}" ${selected}>${contactName}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Phone Number</td>
			<td>
				<select class="flat form-control input-sm" id="phoneNum" name="customer.phone" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${phone}" var="phoneVar">
						<c:if test="${not empty phoneVar}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.phone'] == phoneVar}">
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
		  <td class="form-left">Start Date From</td>
		  <td class="wide"><input class="flat" id="datepicker1" name="startDateFrom" value="${sessionScope.searchCriteria.searchMap['startDateFrom']}" style="width: 175px" /></td>
				
		  <td class="form-left">Start Date To</td>
	      <td class="wide"><input class="flat" id="datepicker2" name="startDateTo" value="${sessionScope.searchCriteria.searchMap['startDateTo']}" style="width: 175px" /></td>
			
	 </tr>
	 <tr>
		  <td class="form-left">End Date From</td>
		  <td class="wide"><input class="flat" id="datepicker3" name="endDateFrom" value="${sessionScope.searchCriteria.searchMap['endDateFrom']}" style="width: 175px" /></td>
				
		  <td class="form-left">End Date To</td>
	      <td class="wide"><input class="flat" id="datepicker4" name="endDateTo" value="${sessionScope.searchCriteria.searchMap['endDateTo']}" style="width: 175px" /></td>
			
	 </tr>
	 <tr>
		  <td class="form-left">Permit Class</td>
		  <td>
				<select class="flat form-control input-sm" id="permitClass" name="permitClass.permitClass" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitClass}" var="permitClass">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permitClass.permitClass'] == permitClass.permitClass}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${permitClass.permitClass}" ${selected}>${permitClass.permitClass}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Permit Type</td>
			<td>
					<select class="flat form-control input-sm" id="permitType" name="permitType.permitType" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitType}" var="permitType">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permitType.permitType'] == permitType.permitType}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${permitType.permitType}" ${selected}>${permitType.permitType}</option>
					</c:forEach>
				</select>
			</td>
	 </tr>
	 <tr>
		  <td class="form-left">Permit Number</td>
			<td>
				<select class="flat form-control input-sm" id="permitNumber" name="number" style="width: 175px !important">
				<c:set var="selected" value=""/>
					<option value="">------Please Select------</option>
					<c:if test="${sessionScope.searchCriteria.searchMap['number'] == 'To Be Assigned'}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="To Be Assigned" ${selected}>To Be Assigned</option>
					<c:forEach items="${permit}" var="permitNumber">
						<c:if test="${permitNumber != 'To Be Assigned'}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['number'] == permitNumber}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${permitNumber}" ${selected}>${permitNumber}</option>
						</c:if>
					</c:forEach>
					<c:set var="selected" value=""/>
				</select>
			</td>
			<td class="form-left">Permit Status</td>
			<td>
				<select class="flat form-control input-sm" id="permitStatus" name="status.status" style="width: 175px !important"">
					<option value="">------Please Select------</option>
					<c:forEach items="${permitStatus}" var="permitStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['status.status'] == permitStatus.status}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${permitStatus.status}" ${selected}>${permitStatus.status}</option>
					</c:forEach>
				</select>
			</td>
	 </tr>
	 <tr>
		    <td class="form-left">Customer</td>
		    <!--  
			<td>
				<select class="flat form-control input-sm" id="customerName" name="customer.companyName" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customers}" var="customer">
						<c:if test='${not empty customer.companyName}'>
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.companyName'] == customer.companyName}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			-->
			<td>
				<select class="flat form-control input-sm" id="customer" name="customer" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Order Number</td>
			<td>
				<!--
				<select id="orderNumber" name="exclude.order.id" class="flat form-control input-sm" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${order}" var="order">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['exclude.order.id'] == order}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${order}" ${selected}>${order}</option>
					</c:forEach>
				</select>
				 -->
				<input class="flat" id="orderNumber" name="exclude.order.id" value="${sessionScope.searchCriteria.searchMap['exclude.order.id']}" style="width: 175px !important" />
			</td>
	 	</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" 
					onclick="document.forms['permitSearchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="permitServiceForm" class="tab-color">
	<transys:datatable urlContext="permit" deletable="false"
		editable="true" insertable="true" cancellable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="list.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportCsv="true" exportXlsx="true" exportPrint="true" useExport2="true" dataQualifier="managePermits">
		<transys:textcolumn headerText="Id" dataField="id" />
		<transys:textcolumn headerText="Status" dataField="status.status" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.fullLine"/>
		<transys:textcolumn headerText="Permit Address1" dataField="fullLinePermitAddress1" />
		<transys:textcolumn headerText="Permit Address2" dataField="fullLinePermitAddress2" />
		<transys:textcolumn headerText="Locn. Type" dataField="locationType.locationType" />
		<transys:textcolumn headerText="Permit Type" dataField="permitType.permitType" />
		<transys:textcolumn headerText="Permit Class" dataField="permitClass.permitClass" />
		<transys:textcolumn headerText="Start Date" dataField="startDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="End Date" dataField="endDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Customer Name" dataField="customer.companyName" />
		<transys:textcolumn headerText="Permit#" dataField="number" />
		<transys:textcolumn headerText="Permit Fee" dataField="fee" />
		<transys:anchorcolumn headerText="Order#" linkUrl="javascript:processGoToOrder('{associatedOrderIds}');" linkText="{associatedOrderIds}"/>
	</transys:datatable>
	<%session.setAttribute("managePermitsColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


