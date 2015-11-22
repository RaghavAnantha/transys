<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Overweight Fee</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table id="form-table" class="table">
		<tr>
			<td class="form-left">Material Category</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="materialCategory" name="materialCategory" style="width: 175px !important">
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
			<td class="form-left">Effective Date From</td>
			<td class="wide"><input class="flat" id="datepicker6" name="effectiveStartDate" value="${sessionScope.searchCriteria.searchMap['effectiveStartDate']}" style="width: 175px" /></td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left">Dumpster Size</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${dumpsterSizes}" var="aDumpsterSize">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aDumpsterSize.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterSize.id}" ${selected}>${aDumpsterSize.size}</option>
					</c:forEach>
				</select>
			</td>	
			<td class="form-left">Effective Date To</td>
		    <td class="wide"><input class="flat" id="datepicker7" name="effectiveEndDate" value="${sessionScope.searchCriteria.searchMap['effectiveEndDate']}" style="width: 175px" /></td>
		</tr>
		<tr>
			<td class="form-left">Overweight Fee</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="price" name="fee" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${overweightFee}" var="aPrice">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['fee'] == aPrice}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aPrice}" ${selected}>${aPrice}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Ton Limit</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="price" name=tonLimit style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${tonLimits}" var="eachValue">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['tonLimit'] == eachValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${eachValue}" ${selected}>${eachValue}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['searchForm'].submit();"
					value="Search" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<form:form name="overweightFee.do" id="overweightFeeObj" class="tab-color">
	<transys:datatable urlContext="masterData/overweightFee" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="overweightFee">
		<transys:textcolumn headerText="Material Category" dataField="materialCategory.category" />
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Ton Limit" dataField="tonLimit" />
		<transys:textcolumn headerText="Overweight Fee" dataField="fee" />
		<transys:textcolumn headerText="Effective Date From" dataField="effectiveStartDate" dataFormat="MM/dd/yyyy" />
		<transys:textcolumn headerText="Effective Date To" dataField="effectiveEndDate" dataFormat="MM/dd/yyyy" />
	</transys:datatable>
	<%session.setAttribute("overweightFeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


