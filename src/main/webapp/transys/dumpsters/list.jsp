<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Dumpsters</h4>

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
			<td align="${left}" class="form-left"><transys:label code="Dumpster Number" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="dumpsterNum" name="dumpsterNum"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsters}" var="aDumpsterForNum">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['dumpsterNum'] == aDumpsterForNum.dumpsterNum}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterForNum.dumpsterNum}" ${selected}>${aDumpsterForNum.dumpsterNum}</option>
					</c:forEach>
			</select></td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Status" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="customer" name="status"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterStatus}" var="aStatus">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['status'] == aStatus.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aStatus.id}" ${selected}>${aStatus.status}</option>
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
<form:form name="dumpsters.do" id="dumpstersObj" class="tab-color">
	<transys:datatable urlContext="dumpsters" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="dumpsters">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster Number" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Status" dataField="status.status" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


