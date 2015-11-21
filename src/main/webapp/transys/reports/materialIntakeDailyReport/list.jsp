<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Material Intake Daily Report</h4>
<form:form action="list.do" method="get" name="materialIntakeDailyReportForm" id="materialIntakeDailyReportForm" >
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
	 <tr>
		  <td class="form-left">Date</td>
		  <td class="wide">
		  		<input class="flat" id="datepicker1" name="intakeDate" value="${sessionScope.searchCriteria.searchMap['intakeDate']}" style="width: 175px" />
		  </td>
	 </tr>
	<tr>
		<td></td>
		<td>
			<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['materialIntakeDailyReportForm'].submit();"
				value="<transys:label code="Preview"/>" />
			<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
		</td>
	</tr>
	</table>
</form:form>

<a href="/reports/materialIntakeDailyReport/generateMaterialIntakeDailyReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/materialIntakeDailyReport/generateMaterialIntakeDailyReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="materialIntakeDailyReport" id="materialIntakeDailyReport" class="tab-color">
 	<transys:datatable urlContext="reports/materialIntakeDailyReport"  baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="materialIntakeDailyReport">
		<transys:textcolumn headerText="Date" dataField="reportDate" width="80px"/>
		<transys:textcolumn headerText="Material Type" dataField="materialName" />
		<transys:textcolumn headerText="Roll-off Tons" dataField="rollOffTons" />
		<transys:textcolumn headerText="Public Intake Tons" dataField="publicIntakeTons" />
		<transys:textcolumn headerText="Total Tons" dataField="totalTons" />
	</transys:datatable>
	<%session.setAttribute("materialIntakeDailyReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

