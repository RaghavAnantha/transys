<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters On-site Report</h4>
<form:form action="list.do" method="get" name="dumpstersOnsiteReportsearchForm">
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Dumpster Size</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">Please Select------</option>
					<c:forEach items="${dumpsterSizes}" var="aDumpsterSize">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aDumpsterSize.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterSize.id}" ${selected}>${aDumpsterSize.size}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Status</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="status" name="status" style="width: 175px !important">
					<option value="">------Please Select------</option>
					<c:forEach items="${dumpsterStatus}" var="status">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == status.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${status.id}" ${selected}>${status.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['dumpstersOnsiteReportsearchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>

<a href="/reports/dumpsterOnsiteReport/generateDumpsterOnsiteReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/dumpsterOnsiteReport/generateDumpsterOnsiteReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="dumpsterOnsiteReport" id="dumpsterOnsiteReport" class="tab-color">
	<transys:datatable urlContext="reports/dumpsterOnsiteReports"  baseObjects="${dumpsterInfoList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="dumpsterOnsiteReport">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster#" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Status" dataField="status.status" />


	</transys:datatable>
	<%session.setAttribute("dumpsterOnsiteReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


