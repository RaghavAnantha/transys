<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Recycle Report</h4>

<script type="text/javascript">
function populateMaterialTypes() {
	var materialTypeSelect = $("#materialType");
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
				<select class="flat form-control input-sm" id=materialType name="materialType" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${materialTypes}" var="aMaterialType">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['materialType'] == aMaterialType.id}">
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
		<td></td>
		<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['recycleReportForm'].submit();"
			value="<transys:label code="Preview"/>" /></td>
	</tr>
	</table>
</form:form>

<a href="/reports/recycleReport/generateRecycleReport.do?type=xls"><img src="/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="/reports/recycleReport/generateRecycleReport.do?type=pdf"><img src="/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="recycleReport" id="recycleReport" class="tab-color">
 	<transys:datatable urlContext="reports/recycleReport"  baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" dataQualifier="recycleReport">
		<transys:textcolumn headerText="Material Category" dataField="materialCategory" />
		<transys:textcolumn headerText="Material Type" dataField="materialName" />
		<transys:textcolumn headerText="Tons" dataField="totalNetTonnage" />
		<transys:textcolumn headerText="Location" dataField="recycleLocation" />
	</transys:datatable>
	<%session.setAttribute("recycleReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

