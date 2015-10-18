<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Material Intake for Recycle</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
 		<tr>
			<td align="${left}" class="form-left"><transys:label code="Material Type" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="materialType" name="materialType" style="width: 175px !important">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${materialTypes}" var="aMaterial">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['materialType.id'] == aMaterial.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aMaterial.id}" ${selected}>${aMaterial.materialName}</option>
					</c:forEach>
				</select>
			</td>	
			<td colspan=10></td>
		</tr>
		<tr>
		  <td align="${left}" class="form-left"><transys:label code="Intake Date"/></td>
		  <td align="${left}" class="wide"><input class="flat" id="datepicker1" name="intakeDate" value="${sessionScope.searchCriteria.searchMap['intakeDate']}" style="width: 175px" /></td>
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Net Weight In Tonnage" /></td>
			<td align="${left}" class="wide"><input name="netWeightInTonnage" style="width: 175px !important"  /></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="materialIntake.do" id="materialIntakeObj" class="tab-color">
	<transys:datatable urlContext="masterData/materialIntake" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="cityFee">
		<transys:textcolumn headerText="Material Name" dataField="materialType.id" />
		<transys:textcolumn headerText="Intake Date" dataField="intakeDate" />
		<transys:textcolumn headerText="Net Weight Tonnage" dataField="netWeightTonnage" />
	</transys:datatable>
	<%session.setAttribute("materialIntakeForRecycleList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


