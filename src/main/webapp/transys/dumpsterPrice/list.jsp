<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Dumpster Prices</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Dumpster Size" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px">
					<option value="">------<transys:label code="Please Select" />------</option>
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
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material Category" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="materialCategory" name="materialCategory"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${materialCategory}" var="aType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['materialCategory'] == aType.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aType.id}" ${selected}>${aType.category}</option>
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
		exportPdf="true" exportXls="true" dataQualifier="dumpsterPrice">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Material Category" dataField="materialCategory.category" />
		<transys:textcolumn headerText="Dumpster Price" dataField="price" />
	</transys:datatable>
	<%session.setAttribute("dumpsterPriceColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


