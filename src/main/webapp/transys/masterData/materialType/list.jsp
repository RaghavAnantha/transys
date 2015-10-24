<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Material Types</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="Material Category" /></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="materialType" name="materialCategory" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${materialCategories}" var="aCategory">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['materialCategory'] == aCategory.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCategory.id}" ${selected}>${aCategory.category}</option>
					</c:forEach>
			</select></td>	
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Material Type" /></td>
			<td class="wide"><select
				class="flat form-control input-sm" id="materialName" name="materialName"
				style="width: 175px !important">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${materialType}" var="aMaterial">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['materialName'] == aMaterial.materialName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterial.materialName}" ${selected}>${aMaterial.materialName}</option>
					</c:forEach>
			</select></td>
		</tr>

		<tr>
			<td></td>
			<td><input type="button"
				class="btn btn-primary btn-sm btn-sm-ext"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="materialType.do" id="materialType" class="tab-color">
	<transys:datatable urlContext="masterData/materialType" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="materialType" >
		
		<transys:textcolumn headerText="Material ID" dataField="id" width="125px"/>
		<transys:textcolumn headerText="Material Category" dataField="materialCategory.category" />
		<transys:textcolumn headerText="Material Name" dataField="materialName" />
	</transys:datatable>
	<%session.setAttribute("materialTypeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


