<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Recycle Report</h4>

<script type="text/javascript">
function populateMaterialTypes() {
	var materialTypeSelect = $("#id");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	materialTypeSelect.append(firstOption);
	
	var materialCategorySelect = $("#materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	if (materialCategoryId == "") {
		return false;
	}
	
	$.ajax({
  		url: "retrieveMaterialTypes.do?" + "materialCategoryId=" + materialCategoryId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var materialTypesList = jQuery.parseJSON(responseData);
       		$.each(materialTypesList, function () {
    	   		$("<option />", {
    	   	        val: this.id,
    	   	        text: this.materialName
    	   	    }).appendTo(materialTypeSelect);
    	   	});
		}
	});
}
</script>

<form:form action="list.do" method="get" name="recycleReportForm" id="recycleReportForm" >
	<table id="form-table" class="table">
	 <tr><td colspan="10"></td><td colspan="10"></td></tr>
 		<tr>
		<td class="form-left">Material Category</td>
		<td class="wide">
			<select class="flat form-control input-sm" id="materialCategory" name="materialCategory" style="width: 175px !important" onChange="return populateMaterialTypes();">
				<option value="">----Please Select----</option>
				<c:forEach items="${materialCategories}" var="aMaterialCategory">
					<c:set var="selected" value="" />
					<c:if test="${sessionScope.searchCriteria.searchMap['materialCategory'] == aMaterialCategory.id}">
						<c:set var="selected" value="selected" />
					</c:if>
					<option value="${aMaterialCategory.id}" ${selected}>${aMaterialCategory.category}</option>
				</c:forEach>
			</select>
		</td>
		<td class="form-left">Material Type</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="id" name="id" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${materialTypes}" var="aMaterialType">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == aMaterialType.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterialType.id}" ${selected}>${aMaterialType.materialName}</option>
					</c:forEach>
		</select></td>
	</tr>
	 <tr>
		  <td class="form-left">Date From</td>
		  <td class="wide">
		  		<input class="flat" id="datepicker1" name="recycleDateFrom" value="${sessionScope.searchCriteria.searchMap['recycleDateFrom']}" style="width: 175px" />
		  </td>
		  <td class="form-left">Date To</td>
		  <td class="wide">
		  		<input class="flat" id="datepicker2" name="recycleDateTo" value="${sessionScope.searchCriteria.searchMap['recycleDateTo']}" style="width: 175px" />
		  </td>
	 </tr>
	 <tr>
		  <td class="form-left">Date From</td>
		  <td class="wide">
		  		<input class="flat" id="datepicker1" name="recycleDateFrom" value="${sessionScope.searchCriteria.searchMap['recycleDateFrom']}" style="width: 175px" />
		  </td>
		  <td class="form-left">Date To</td>
		  <td class="wide">
		  		<input class="flat" id="datepicker2" name="recycleDateTo" value="${sessionScope.searchCriteria.searchMap['recycleDateTo']}" style="width: 175px" />
		  </td>
	 </tr>
	<tr>
		<td></td>
		<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['materialIntakeDailyReportForm'].submit();"
			value="<transys:label code="Preview"/>" /></td>
	</tr>
	</table>
</form:form>

<%-- <a href="/reports/materialIntakeDailyReport/generateMaterialIntakeDailyReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
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
</form:form> --%>

