<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Monthly Transfer Station Intake Report</h4>

<form:form action="/reports/monthlyTransferStationIntakeReport/generateExcelReport.do" method="get" name="monthlyTransferStationIntakeReportForm" id="monthlyTransferStationIntakeReportForm" >
	<table id="form-table" class="table">
	 <tr><td colspan="10"></td><td colspan="10"></td></tr>
 		<tr>
		<td class="form-left">Month</td>
		<td class="wide">
			<select class="flat form-control input-sm" id="month" name="month" style="width: 175px !important">
				<option value="">----Please Select----</option>
				<c:forEach items="${months}" var="aMonth">
					<c:if test="${not empty aMonth}">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['month'] == aMonth}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMonth}" ${selected}>${aMonth}</option>
					</c:if>
				</c:forEach>
			</select>
		</td>
		<tr>
		<td></td>
		<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['monthlyTransferStationIntakeReportForm'].submit();"
			value="<transys:label code="Generate Excel"/>" /></td>
	</tr>
	</tr>
	</table>
</form:form>

<!-- <a href="/reports/monthlyTransferStationIntakeReport/generateReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a> -->
<!-- <a href="/reports/monthlyTransferStationIntakeReport/generateReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a> -->

