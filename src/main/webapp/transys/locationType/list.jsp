<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Location Types</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Location Type ID" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="id" name="id"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${locationTypes}" var="aLocationType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['id'] == aLocationType.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aLocationType.id}" ${selected}>${aLocationType.id}</option>
					</c:forEach>
			</select></td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Location Type" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="locationType" name="locationType"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${locationTypes}" var="aLocationType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['locationType'] == aLocationType.locationType}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aLocationType.locationType}" ${selected}>${aLocationType.locationType}</option>
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
<form:form name="locationType.do" id="locationTypeObj" class="tab-color">
	<transys:datatable urlContext="locationType" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="locationType">
		<transys:textcolumn headerText="Location Type ID" dataField="id" />
		<transys:textcolumn headerText="Location Type" dataField="locationType" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


