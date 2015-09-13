<%@include file="/common/taglibs.jsp"%>
<br/>
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><transys:label code="Manage Permits" /></b></td>
		</tr>
		<tr>
		  <td align="${left}" class="first"><transys:label code="Delivery Address #"/></td>
				<td align="${left}"><select id="deliveryAddress" name="deliveryAddress" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${deliveryAddress}" var="deliveryAddress">
						 <c:set var="selected" value=""/>
						 <c:if test="${sessionScope.searchCriteria.searchMap['line1'] == deliveryAddress.line1}">
							<c:set var="selected" value="selected"/>
						</c:if> 
							<option value="${deliveryAddress.line1}" ${selected}>${deliveryAddress.line1}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Delivery Street"/></td>
			<td align="${left}"><select id="deliveryStreet" name="deliveryStreet" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${deliveryAddress}" var="deliveryStreet">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == deliveryStreet.id}">
						<c:set var="selected" value="selected"/>
					</c:if> 
						<option value="${deliveryStreet.line2}" ${selected}>${deliveryStreet.line2}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 
 	 <tr>
		  <td align="${left}" class="first"><transys:label code="Contact Name"/></td>
				<td align="${left}"><select id="contactName" name="contactName" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${order}" var="order">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['deliveryContactName'] == order.deliveryContactName}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${order.deliveryContactName}" ${selected}>${order.deliveryContactName}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Phone Number"/></td>
			<td align="${left}"><select id="phoneNum" name="phoneNum" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${customer}" var="customer">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == customer.phone}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customer.phone}" ${selected}>${customer.phone}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="first"><transys:label code="Start Date From"/></td>
				<td align="${left}"><select id="stateDateFrom" name="startDateFrom" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${state}" var="startDateFrom">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['name'] == startDateFrom.name}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${startDateFrom.name}" ${selected}>${startDateFrom.name}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Start Date To"/></td>
			<td align="${left}"><select id="startDateTo" name="startDateTo" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${state}" var="startDateTo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == startDateTo.name}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${startDateTo.name}" ${selected}>${startDateTo.name}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="first"><transys:label code="End Date From"/></td>
				<td align="${left}"><select id="endDateFrom" name="endDateFrom" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${state}" var="endDateFrom">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['name'] == endDateFrom.name}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${endDateFrom.name}" ${selected}>${endDateFrom.name}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="End Date To"/></td>
			<td align="${left}"><select id="endDateTo" name="endDateTo" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${state}" var="endDateTo">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == endDateTo.name}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${endDateTo.name}" ${selected}>${endDateTo.name}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="first"><transys:label code="Permit Class"/></td>
				<td align="${left}"><select id="permitClass" name="permitClass" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${permitClass}" var="permitClass">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['permitClass'] == permitClass.permitClass}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${permitClass.permitClass}" ${selected}>${permitClass.permitClass}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Permit Type"/></td>
			<td align="${left}"><select id="permitType" name="permitType" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${permitType}" var="permitType">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == permitType.type}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${permitType.type}" ${selected}>${permitType.type}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="first"><transys:label code="Permit Number"/></td>
				<td align="${left}"><select id="permitNumber" name="permitNumber" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${permit}" var="permit">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['name'] == permit.number}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${permit.number}" ${selected}>${permit.number}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Permit Status"/></td>
			<td align="${left}"><select id="permitStatus" name="permitStatus" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${permitStatus}" var="permitStatus">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name'] == permitStatus.status}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${permitStatus.status}" ${selected}>${permitStatus.status}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
	 <tr>
		  <td align="${left}" class="first"><transys:label code="Customer Name"/></td>
				<td align="${left}"><select id="customerName" name="customerName" style="min-width:350px; max-width:350px">
					<option value="">------<transys:label code="Please Select"/>------</option>
					<c:forEach items="${customer}" var="customer">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['name'] == customer.contactName}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${customer.contactName}" ${selected}>${customer.contactName}</option>
					</c:forEach>
				</select>
				</td>
			
			<td align="${left}" class="first"><transys:label code="Order Number"/></td>
			<td align="${left}"><select id="orderNumber" name="orderNumber" style="min-width:200px; max-width:200px">
				<option value="">------<transys:label code="Please Select"/>------</option>
				<c:forEach items="${order}" var="order">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == order.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${order.id}" ${selected}>${order.id}</option>
				</c:forEach>
			</select>
			</td>
	 </tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<transys:datatable urlContext="permit" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false" 
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Delivery#" dataField="deliveryAddress.line1" />
		<transys:textcolumn headerText="DeliveryStreet" dataField="deliveryAddress.line2" />
		<transys:textcolumn headerText="Locn. Type" dataField="locationType.locationType" />
		<transys:textcolumn headerText="PermitType" dataField="type.type" />
		<transys:textcolumn headerText="PermitClass" dataField="permitClass.permitClass" />
		<transys:textcolumn headerText="StartDate" dataField="startDate" />
		<%-- <transys:textcolumn headerText="EndDate" dataField="endDate" /> --%>
		<%-- <transys:textcolumn headerText="CustomerName" dataField="customerName" /> --%>
		<transys:textcolumn headerText="Permit#" dataField="number" />
		<transys:textcolumn headerText="PermitFee" dataField="fee" />
		<%-- <transys:textcolumn headerText="Order#" dataField="orderNumber" /> --%>
		<transys:textcolumn headerText="PermitAddr#" dataField="permitAddress" />
		<transys:textcolumn headerText="Status" dataField="status.status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


