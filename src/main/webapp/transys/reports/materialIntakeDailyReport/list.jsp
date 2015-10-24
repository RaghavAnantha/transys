<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function populateAggTable(container, data) {
	var table = $("<table/>").addClass('table table-bordered');
    $.each(data, function(rowIndex, r) {
        var row = $("<tr/>");
        $.each(r, function(colIndex, c) { 
            row.append($("<t"+(rowIndex == 0 ?  "h" : "d")+"/>").text(c));
        });
        table.append(row);
    });
    return container.append(table);
}

$(document).ready(function() {
   // var data = '${data}' // not working ?
   var data = [["Type Of Material", "Roll-off (Tons)", "Public (Tons)", "Total Tons"], ["Concrete", 10.00, 0, 10.00]] // working
    var aggregateTable = populateAggTable($(document.body), data);
});
</script>

<br/>
<h4 style="margin-top: -15px; !important">Material Intake Daily Report</h4>
<form:form action="list.do" method="get" name="searchForm" id="materialIntakeDailyReportForm" >
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
	 <tr>
		  <td align="${left}" class="form-left"><transys:label code="Date"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker1" name="intakeDate" style="width: 175px" /></td>
	 </tr>
	<tr>
		<td align="${left}"></td>
		<td align="${left}"><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['materialIntakeDailyReportForm'].submit();"
			value="<transys:label code="Preview"/>" /></td>
	</tr>
	</table>
</form:form>



<a href="/reports/materialIntakeDailyReport/materialIntakeDailyReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/materialIntakeDailyReport/materialIntakeDailyReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="materialIntakeDailyReport" id="materialIntakeDailyReport" class="tab-color">
  <!-- <table class="table table-bordered" id="materialIntakeDailyReportTable">
	<thead>
	<tr class="table-heading">
		<td colspan="4"><b>Material Intake Daily Report </b></td>
	</tr>
      <tr>
        <th style="height:5px">Type Of Material</th>
        <th>Roll-Off</th>
        <th>Public</th>
        <th>Total</th>
      </tr>
    </thead>
    <tbody>
	<tr>
		<td class="form-left">Brick</td>
		<td class="form-left">12</td>
		<td class="form-left">20</td>
		<td class="form-left">320</td>
	</tr>
	</tbody>
</table>  -->
	<%session.setAttribute("dmaterialIntakeDailyReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

