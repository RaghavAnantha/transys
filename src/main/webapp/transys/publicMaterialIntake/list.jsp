<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Public Material Intake</h5>

<script type="text/javascript">
function populateMaterialTypes() {
	var materialCategorySelect = $("#materialType\\.materialCategory");
	var materialCategoryId = materialCategorySelect.val();
	
	if (materialCategoryId == "") {
		return false;
	}
	
	var materialTypeSelect = $("#materialType");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	materialTypeSelect.append(firstOption);
	
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

<form:form action="list.do" method="get" name="publicMaterialIntakeSearchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Material Category</td>
			<td>
				<select class="flat form-control input-sm" id="materialType.materialCategory" name="materialType.materialCategory" style="width: 175px !important" onChange="return populateMaterialTypes();">
					<option value="">----Please Select----</option>
					<c:forEach items="${materialCategories}" var="aMaterialCategory">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['materialType.materialCategory'] == aMaterialCategory.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterialCategory.id}" ${selected}>${aMaterialCategory.category}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
 		<tr>
			<td class="form-left">Material Type</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="materialType" name="materialType" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${materialTypes}" var="aMaterial">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['materialType'] == aMaterial.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterial.id}" ${selected}>${aMaterial.materialName}</option>
					</c:forEach>
				</select>
			</td>	
			<td colspan=10></td>
		</tr>
		<tr>
		  <td class="form-left">Intake Date</td>
		  <td class="wide"><input class="flat" id="datepicker1" name="intakeDate" value="${sessionScope.searchCriteria.searchMap['intakeDate']}" style="width: 175px" /></td>
		</tr>
		<tr>
			<td class="form-left">Net Tonnage</td>
			<td class="wide"><input name="netWeightTonnage" value="${sessionScope.searchCriteria.searchMap['netWeightTonnage']}" class="flat flat-ext" style="width: 175px !important"  /></td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['publicMaterialIntakeSearchForm'].submit();"
				value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<form:form name="materialIntakeServiceForm" id="materialIntakeServiceForm" class="tab-color">
	<transys:datatable urlContext="publicMaterialIntake" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="publicMaterialIntake">
		<transys:textcolumn headerText="Material Category" dataField="materialType.materialCategory.category" />
		<transys:textcolumn headerText="Material Type" dataField="materialType.materialName" />
		<transys:textcolumn headerText="Intake Date" dataField="intakeDate" dataFormat="MM/dd/yyyy" width="80px"/>
		<transys:textcolumn headerText="Net Tonnage" dataField="netWeightTonnage" width="80px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="comments" />
	</transys:datatable>
	<%session.setAttribute("publicMaterialIntakeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


