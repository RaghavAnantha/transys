<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Orders</h4>
<form:form action="list.do" method="get" name="searchForm" id="orderSearchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left">
				<transys:label code="Order #" /></td>
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
			</select></td>

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
			</select></td>
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
		<transys:textcolumn headerText="Status" dataField="orderStatus.status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


