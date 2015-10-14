<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Material Categories</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material Category" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="materialType" name="materialCategory" style="width: 175px">
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
<form:form name="materialCategory.do" id="materialCategory" class="tab-color">
	<transys:datatable urlContext="masterData/materialCategory" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="materialCategory" >
		
		<transys:textcolumn headerText="Material Category ID" dataField="id" />
		<transys:textcolumn headerText="Material Category" dataField="category" />
	</transys:datatable>
	<%session.setAttribute("materialCategoryColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


