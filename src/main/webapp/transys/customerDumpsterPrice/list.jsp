<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Customer Dumpster Prices</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Customer" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="customer" name="customer"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${customers}" var="aCustomer">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['customer'] == aCustomer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCustomer.id}" ${selected}>${aCustomer.companyName}</option>
					</c:forEach>
			</select></td>	
			</tr>
			<tr>
				<td align="${left}" class="form-left"><transys:label code="Dumpster Price" /></td>
				<td align="${left}" class="wide"><select
					class="flat form-control input-sm" id="dumpsterPrice" name="dumpsterPrice"
					style="width: 175px">
						<option value="">------
							<transys:label code="Please Select" />------
						</option>
						<c:forEach items="${dumpsterPrices}" var="aPrice">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['dumpsterPrice'] == aPrice.dumpsterPrice}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPrice.dumpsterPrice}" ${selected}>${aPrice.dumpsterPrice}</option>
						</c:forEach>
				</select></td>	
			</tr>
			
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="customerDumpsterPrice.do" id="customerDumpsterPriceObj" class="tab-color">
	<transys:datatable urlContext="customerDumpsterPrice" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Dumpster Price" dataField="dumpsterPrice" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


