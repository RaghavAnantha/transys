<%@include file="/common/taglibs.jsp"%>
<br/>
<h5 style="margin-top: -15px; !important">Manage Permits</h5>
<form:form action="list.do" method="get" name="permitSearchForm" id="permitSearchForm">
	<table width="100%" id="form-table">
		<tr>
		  	<td align="${left}" class="form-left"><transys:label code="Delivery Address #"/></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress.line1" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${allDeliveryAddresses}" var="deliveryAddressVar">
						 <c:set var="selected" value=""/>
						 <c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line1'] == deliveryAddressVar.line1}">
							<c:set var="selected" value="selected"/>
						</c:if> 
							<option value="${deliveryAddressVar.line1}" ${selected}>${deliveryAddressVar.line1}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="form-left"><transys:label code="Delivery Street"/></td>
			<td align="${left}"><select class="flat form-control input-sm" id="deliveryStreet" name="deliveryAddress.line2" style="width: 175px !important"">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${allDeliveryAddresses}" var="deliveryAddress">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress.line2'] == deliveryAddress.line2}">
							<c:set var="selected" value="selected"/>
						</c:if> 
						<option value="${deliveryAddress.line2}" ${selected}>${deliveryAddress.line2}</option>
				</c:forEach>
			</select>
			</td>
			<td colspan=10></td>
	 	</tr>
	 
 	 	<tr>
		  <td align="${left}" class="form-left"><transys:label code="Contact Name"/></td>
				<td align="${left}"><select class="flat form-control input-sm" id="contactName" name="customer.contactName" style="width: 175px !important"">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${customer}" var="customer">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['customer.contactName'] == customer.contactName}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${customer.contactName}" ${selected}>${customer.contactName}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="form-left"><transys:label code="Phone Number"/></td>
			<td align="${left}"><select class="flat form-control input-sm" id="phoneNum" name="customer.phone" style="width: 175px !important"">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['customer.phone'] == customer.phone}">
							<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customer.phone}" ${selected}>${customer.phone}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Start Date From"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker1" name="startDateFrom" value="${sessionScope.searchCriteria.searchMap['startDateFrom']}" style="width: 175px" /></td>
				
		  <td align="${left}" class="form-left"><transys:label code="Start Date To"/></td>
	      <td align="${left}" class="wide"><input class="flat" id="datepicker2" name="startDateTo" value="${sessionScope.searchCriteria.searchMap['startDateTo']}" style="width: 175px" /></td>
			
	 </tr>
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="End Date From"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker3" name="endDateFrom" value="${sessionScope.searchCriteria.searchMap['endDateFrom']}" style="width: 175px" /></td>
				
		  <td align="${left}" class="form-left"><transys:label code="End Date To"/></td>
	      <td align="${left}" class="wide"><input class="flat" id="datepicker4" name="endDateTo" value="${sessionScope.searchCriteria.searchMap['endDateTo']}" style="width: 175px" /></td>
			
	 </tr>
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Permit Class"/></td>
				<td align="${left}"><select class="flat form-control input-sm" id="permitClass" name="permitClass.permitClass" style="width: 175px !important"">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${permitClass}" var="permitClass">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['permitClass.permitClass'] == permitClass.permitClass}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${permitClass.permitClass}" ${selected}>${permitClass.permitClass}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="form-left"><transys:label code="Permit Type"/></td>
			<td align="${left}"><select class="flat form-control input-sm" id="permitType" name="permitType.permitType" style="width: 175px !important"">
				<option value="">------<transys:label code="Please Select"/>------</option>
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
		  <td align="${left}" class="form-left"><transys:label code="Permit Number"/></td>
				<td align="${left}"><select class="flat form-control input-sm" id="permitNumber" name="number" style="width: 175px !important"">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${permit}" var="permit">
						<c:if test="${not empty permit.number}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['number'] == permit.number}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${permit.number}" ${selected}>${permit.number}</option>
						</c:if>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="form-left"><transys:label code="Permit Status"/></td>
			<td align="${left}"><select class="flat form-control input-sm" id="permitStatus" name="status.status" style="width: 175px !important"">
				<option value="">------<transys:label code="Please Select"/>------</option>
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
		  <td align="${left}" class="form-left"><transys:label code="Customer Name"/></td>
				<td align="${left}"><select class="flat form-control input-sm" id="customerName" name="customer.companyName" style="width: 175px !important"">
					<option value="">------<transys:label code="Please Select"/>------</option>
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
			
			<td align="${left}" class="form-left"><transys:label code="Order Number"/></td>
			<td align="${left}"><select id="orderNumber" name="exclude.order.id" class="flat form-control input-sm" style="width: 175px !important"">
				<option value="">------<transys:label code="Please Select"/>------</option>
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
			<td align="${left}"></td>
			<td align="${left}"><input type="button" class="btn btn-primary btn-sm"
				onclick="document.forms['permitSearchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>

<form:form name="delete.do" id="serviceForm" class="tab-color">
	<transys:datatable urlContext="permit" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportXls="true" dataQualifier="managePermits">
		<transys:textcolumn headerText="Delivery Address #" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="DeliveryStreet" dataField="deliveryAddress.line2" /> 
		<transys:textcolumn headerText="Locn. Type" dataField="locationType.locationType" />
		<transys:textcolumn headerText="PermitType" dataField="permitType.permitType" />
		<transys:textcolumn headerText="PermitClass" dataField="permitClass.permitClass" />
		<transys:textcolumn headerText="StartDate" dataField="startDate" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="EndDate" dataField="endDate" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="CustomerName" dataField="customer.companyName" />
		<transys:textcolumn headerText="Permit#" dataField="number" />
		<transys:textcolumn headerText="PermitFee" dataField="fee" />
		<transys:textcolumn headerText="Order#" dataField="orderId" />
		<transys:textcolumn headerText="Status" dataField="status.status" />
	</transys:datatable>
	<%session.setAttribute("managePermitsColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


