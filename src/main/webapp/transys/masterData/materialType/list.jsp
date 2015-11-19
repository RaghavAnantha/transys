<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Material Types</h5>

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
<form:form action="list.do" method="get" name="materialTypeSearchForm">
	<table id="form-table" class="table">
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
			<td colspan=10></td>
		</tr>
		<tr>
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
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext"
				onclick="document.forms['materialTypeSearchForm'].submit();"
				value="<transys:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<form:form name="materialTypeServiceForm" id="materialTypeServiceForm" class="tab-color">
	<transys:datatable urlContext="masterData/materialType" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="materialType" >
		<transys:textcolumn headerText="Material Id" dataField="id" width="125px"/>
		<transys:textcolumn headerText="Material Category" dataField="materialCategory.category" />
		<transys:textcolumn headerText="Material Name" dataField="materialName" />
		<transys:textcolumn headerText="Notes/Comments" dataField="comments" />
	</transys:datatable>
	<%session.setAttribute("materialTypeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


