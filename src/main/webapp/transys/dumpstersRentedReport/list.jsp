<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters On-site Report</h4>
<form:form action="list.do" method="get" name="dumpstersRentedReportsearchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Dumpster Size" /></td>
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
			
			<td align="${left}" class="form-left"><transys:label
					code="Status" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="status" name="status"
				style="width: 168px !important">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterStatus}" var="status">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['status'] == status.status}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${status.id}" ${selected}>${status.status}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['dumpstersRentedReportsearchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>

<a href="/dumpstersRentedReports/generateDumpstersRentedReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/dumpstersRentedReports/generateDumpstersRentedReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="dumpstersRentedReport" id="dumpstersRentedReport" class="tab-color">
	<transys:datatable urlContext="dumpstersRentedReports"  baseObjects="${dumpsterInfoList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="dumpsterOnsiteReport">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster#" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Delivery Address" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Status" dataField="status.status" />


	</transys:datatable>
	<%session.setAttribute("dumpsterOnsiteReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


