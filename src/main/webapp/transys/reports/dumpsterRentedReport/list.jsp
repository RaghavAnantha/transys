<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters Rented Report</h4>
<form:form action="list.do" method="get" name="dumpsterRentedReportsearchForm">
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Dumpster Size" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
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
				style="width: 175px !important">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${dumpsterStatus}" var="status">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['status'] == status.id}">
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
				onclick="document.forms['dumpsterRentedReportsearchForm'].submit();"
				value="<transys:label code="Preview"/>" /></td>
		</tr>
	</table>
</form:form>

<a href="/reports/dumpsterRentedReport/generateDumpsterRentedReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/dumpsterRentedReport/generateDumpsterRentedReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="dumpsterRentedReport" id="dumpsterRentedReport" class="tab-color">
	<transys:datatable urlContext="reports/dumpsterRentedReports"  baseObjects="${dumpsterInfoList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="dumpsterOnsiteReport">
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster#" dataField="dumpsterNum" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" dataFormat="MM/dd/yyy"/>
		<transys:textcolumn headerText="Status" dataField="status.status" />


	</transys:datatable>
	<%session.setAttribute("dumpsterOnsiteReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


