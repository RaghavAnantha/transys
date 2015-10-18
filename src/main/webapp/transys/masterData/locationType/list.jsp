<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Location Types</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="Location Type" /></td>
			<td class="wide"><select
				class="flat form-control input-sm" id="locationType" name="locationType"
				style="width: 175px !important">
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
			<td colspan=10></td>
		</tr>

		<tr>
			<td></td>
			<td><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="locationType.do" id="locationTypeObj" class="tab-color">
	<transys:datatable urlContext="masterData/locationType" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="locationType">
		<transys:textcolumn headerText="Location Type ID" dataField="id" width="125px"/>
		<transys:textcolumn headerText="Location Type" dataField="locationType" />
	</transys:datatable>
	<%session.setAttribute("locationTypeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


