<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Recycle Location</h5>

<script type="text/javascript">
function populateMaterialTypes() {
	var materialTypeSelect = $("#materialType");
	materialTypeSelect.empty();
	
	var firstOption = $('<option value="">'+ "----Please Select----" +'</option>');
	materialTypeSelect.append(firstOption);
	
	var materialCategorySelect = $("#materialType\\.materialCategory");
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

<form:form action="list.do" method="get" name="recycleLocationSearchForm">
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
			<td class="form-left">Location</td>
			<td class="wide"><input name="location" value="${sessionScope.searchCriteria.searchMap['location']}" class="flat flat-ext" style="width: 175px !important"  /></td>
		</tr>
		<tr>
			<td class="form-left">Status</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="status" name="status" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${statuses}" var="aStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == aStatus}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aStatus}" ${selected}>${aStatus}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td class="form-left">Effective Date From</td>
		  	<td class="wide">
		  		<input class="flat" id="datepicker1" name="effectiveStartDate" value="${sessionScope.searchCriteria.searchMap['effectiveStartDate']}" style="width: 175px" />
		  	</td>
		</tr>
		<tr>
			<td class="form-left">Effective Date To</td>
		  	<td class="wide">
		  		<input class="flat" id="datepicker2" name="effectiveEndDate" value="${sessionScope.searchCriteria.searchMap['effectiveEndDate']}" style="width: 175px" />
		  	</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['recycleLocationSearchForm'].submit();"
				value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<form:form name="recycleLocationServiceForm" id="recycleLocationServiceForm" class="tab-color">
	<transys:datatable urlContext="masterData/recycleLocation" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="recycleLocation">
		<transys:textcolumn headerText="Material Category" dataField="materialType.materialCategory.category" />
		<transys:textcolumn headerText="Material Type" dataField="materialType.materialName" />
		<transys:textcolumn headerText="Location" dataField="location"/>
		<transys:textcolumn headerText="Status" dataField="status" width="80px"/>
		<transys:textcolumn headerText="Eff Date Fr" dataField="effectiveStartDate" dataFormat="MM/dd/yyy" width="80px"/>
		<transys:textcolumn headerText="Eff Date To" dataField="effectiveEndDate" dataFormat="MM/dd/yyy" width="80px"/>
		<transys:textcolumn headerText="Notes/Comments" dataField="comments" />
	</transys:datatable>
	<%session.setAttribute("recycleLocationColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


