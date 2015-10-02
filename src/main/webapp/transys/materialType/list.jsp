<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Material Types</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material ID" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="id" name="id"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${materialType}" var="aMaterial">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['id'] == aMaterial.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterial.id}" ${selected}>${aMaterial.id}</option>
					</c:forEach>
			</select></td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material Type" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="materialName" name="materialName"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${materialType}" var="aMaterial">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['materialName'] == aMaterial.materialName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterial.id}" ${selected}>${aMaterial.materialName}</option>
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
<form:form name="materialType.do" id="materialType" class="tab-color">
	<transys:datatable urlContext="materialType" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="materialType">
		<transys:textcolumn headerText="Material ID" dataField="id" />
		<transys:textcolumn headerText="Material Type" dataField="type" />
	</transys:datatable>
	<%session.setAttribute("materialTypeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


