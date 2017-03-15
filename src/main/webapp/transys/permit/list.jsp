<%@include file="/common/taglibs.jsp"%>
<br/>
<h5 style="margin-top: -15px; !important">Manage Permits</h5>
<form:form action="list.do" method="get" name="permitSearchForm" id="permitSearchForm">
	<table id="form-table" class="table">
		<tr>
		  	<td class="form-left form-left-ext">Delivery Addrs Line1</td>
			<td>
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress.line1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${allDeliveryAddresses}" var="deliveryAddressVar1">
						<c:if test="${not empty deliveryAddressVar1.line1}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line1'] == deliveryAddressVar1.line1}">
								<c:set var="selected" value="selected"/>
							</c:if> 
							<option value="${deliveryAddressVar1.line1}" ${selected}>${deliveryAddressVar1.line1}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left form-left-ext">Delivery Addrs Line2</td>
			<td><select class="flat form-control input-sm" id="deliveryStreet" name="deliveryAddress.line2" style="width: 175px !important">
				<option value="">------Please Select------</option>
				<c:forEach items="${allDeliveryAddresses}" var="deliveryAddressVar2">
					<c:if test="${not empty deliveryAddressVar2.line2}">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line2'] == deliveryAddressVar2.line2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${deliveryAddressVar2.line2}" ${selected}>${deliveryAddressVar2.line2}</option>
					</c:if>
				</c:forEach>
			</select>
			</td>
			<td colspan=10></td>
	 	</tr>
	 	<tr>
		  	<td class="form-left form-left-ext">Permit Addrs Line1</td>
			<td>
				<select class="flat form-control input-sm" id="permitAddress" name="permitAddress[0].line1" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${allPermitAddressesLine1}" var="aPermitAddressLine1">
						<c:if test="${not empty aPermitAddressLine1}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permitAddress[0].line1'] == aPermitAddressLine1}">
								<c:set var="selected" value="selected"/>
							</c:if> 
							<option value="${aPermitAddressLine1}" ${selected}>${aPermitAddressLine1}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td class="form-left form-left-ext">Permit Addrs Line2</td>
			<td><select class="flat form-control input-sm" id="permitStreet" name="permitAddress[0].line2" style="width: 175px !important">
				<option value="">------Please Select------</option>
				<c:forEach items="${allPermitAddressesLine2}" var="aPermitAddressLine2">
					<c:if test="${not empty aPermitAddressLine2}">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['permitAddress[0].line2'] == aPermitAddressLine2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${aPermitAddressLine2}" ${selected}>${aPermitAddressLine2}</option>
					</c:if>
				</c:forEach>
			</select>
			</td>
			<td colspan=10></td>
	 	</tr>
 	 	<tr>
		  <td class="form-left">Contact Name</td>
				<td><select class="flat form-control input-sm" id="contactName" name="customer.contactName" style="width: 175px !important">
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
			<td><select class="flat form-control input-sm" id="phoneNum" name="customer.phone" style="width: 175px !important">
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
				<td><select class="flat form-control input-sm" id="permitClass" name="permitClass.permitClass" style="width: 175px !important"">
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
			<td><select class="flat form-control input-sm" id="permitType" name="permitType.permitType" style="width: 175px !important">
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
				<td><select class="flat form-control input-sm" id="permitNumber" name="number" style="width: 175px !important">
				<c:set var="selected" value=""/>
					<option value="">------Please Select------</option>
					<c:if test="${sessionScope.searchCriteria.searchMap['number'] == 'To Be Assigned'}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="To Be Assigned" ${selected}>To Be Assigned</option>
					
					<c:forEach items="${permit}" var="permit">
						<c:if test="${permit.number != 'To Be Assigned'}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['number'] == permit.number}">
								<c:set var="selected" value="selected"/>
							</c:if>
							<option value="${permit.number}" ${selected}>${permit.number}</option>
						</c:if>
					</c:forEach>
					<c:set var="selected" value=""/>
				</select>
				</td>
			
			<td class="form-left">Permit Status</td>
			<td><select class="flat form-control input-sm" id="permitStatus" name="status.status" style="width: 175px !important"">
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
		  <td class="form-left">Customer Name</td>
				<td><select class="flat form-control input-sm" id="customerName" name="customer.companyName" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${customer}" var="customer">
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
			
			<td class="form-left">Order Number</td>
			<td><select id="orderNumber" name="exclude.order.id" class="flat form-control input-sm" style="width: 175px !important">
				<option value="">------Please Select------</option>
				<c:forEach items="${order}" var="order">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['exclude.order.id'] == order.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="${order.id}" ${selected}>${order.id}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['permitSearchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="permit" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="list.do" multipleDelete="false" searcheable="false" 
		exportPdf="false" exportXlsx="true" dataQualifier="managePermits">
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
		<transys:textcolumn headerText="Order#" dataField="associatedOrderIds" />
		<transys:textcolumn headerText="Status" dataField="status.status" />
	</transys:datatable>
	<%session.setAttribute("managePermitsColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


