<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Monthly Transfer Station Intake Report</h4>

<form:form action="/reports/monthlyTransferStationIntakeReport/generateExcelReport.do" method="POST" name="monthlyTransferStationIntakeReportForm" id="monthlyTransferStationIntakeReportForm" >
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
 		<tr>
			<td class="form-left">Year</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="year" name="year" style="width: 150px">
					<option value="">--</option>
					<c:forEach items="${years}" var="aYear">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['year'] == aYear}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aYear}" ${selected}>${aYear}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Month</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="month" name="month" style="width: 150px">
					<option value="">--Please Select--</option>
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
		</tr>
		<tr><td colspan="10"></td><td colspan="10"></td></tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['monthlyTransferStationIntakeReportForm'].submit();"
					value="<transys:label code="Generate Excel"/>" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
