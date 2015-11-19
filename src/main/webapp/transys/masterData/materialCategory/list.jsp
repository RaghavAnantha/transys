<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Material Categories</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="Material Category" /></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="materialType" name="category" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${materialCategories}" var="aCategory">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['category'] == aCategory.category}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCategory.category}" ${selected}>${aCategory.category}</option>
					</c:forEach>
			</select></td>	
			<td colspan=10></td>
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
<form:form name="materialCategory.do" id="materialCategory" class="tab-color">
	<transys:datatable urlContext="masterData/materialCategory" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="materialCategory" >
		
		<transys:textcolumn headerText="Material Category ID" dataField="id" width="150px"/>
		<transys:textcolumn headerText="Material Category" dataField="category" />
	</transys:datatable>
	<%session.setAttribute("materialCategoryColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


