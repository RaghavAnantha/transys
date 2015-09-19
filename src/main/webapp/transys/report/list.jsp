<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters On-site Report</h4>
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="dumpsterSize" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterInfo}" var="size">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == size.dumpsterSize}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${size.dumpsterSize}" ${selected}>${size.dumpsterSize}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="status" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="status"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterInfo}" var="status">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['status'] == status.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${status.status}" ${selected}>${status.status}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="dumpsterOnsiteReport" id="dumpsterOnsiteReport" class="tab-color">
	<transys:datatable urlContext="report"  baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize" />
		<transys:textcolumn headerText="Dumpster#" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Status" dataField="status" />
		<transys:textcolumn headerText="Comments" dataField="dumpsterSize" />


	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


