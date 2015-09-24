<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Dumpster Prices</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Dumpster Size" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterSizes}" var="aSize">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aSize.dumpsterSize}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aSize.dumpsterSize}" ${selected}>${aSize.dumpsterSize}</option>
					</c:forEach>
			</select></td>	
			</tr>
			<tr>
				<td align="${left}" class="form-left"><transys:label code="Material Type" /></td>
				<td align="${left}" class="wide"><select
					class="flat form-control input-sm" id="materialType" name="materialType"
					style="width: 175px">
						<option value="">------
							<transys:label code="Please Select" />------
						</option>
						<c:forEach items="${materialTypes}" var="aType">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['materialType'] == aType.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aType.id}" ${selected}>${aType.type}</option>
						</c:forEach>
				</select></td>	
			</tr>
			<tr>
				<td align="${left}" class="form-left"><transys:label code="Dumpster Price" /></td>
				<td align="${left}" class="wide"><select
					class="flat form-control input-sm" id="price" name="price"
					style="width: 175px">
						<option value="">------
							<transys:label code="Please Select" />------
						</option>
						<c:forEach items="${dumpsterPrices}" var="aPrice">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['price'] == aPrice.price}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPrice.price}" ${selected}>${aPrice.price}</option>
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
<form:form name="dumpsterPrice.do" id="dumpsterPriceObj" class="tab-color">
	<transys:datatable urlContext="dumpsterPrice" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize" />
		<transys:textcolumn headerText="Material Type" dataField="materialType.type" />
		<transys:textcolumn headerText="Dumpster Price" dataField="price" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


