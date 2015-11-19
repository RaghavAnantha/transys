<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage City Fees</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="City" /></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="suburbName" name="suburbName" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${cityFees}" var="aCity">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['suburbName'] == aCity.suburbName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCity.suburbName}" ${selected}>${aCity.suburbName}</option>
					</c:forEach>
				</select>
			</td>	
			<td class="form-left"><transys:label code="Effective Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker6" name="effectiveStartDate" value="${sessionScope.searchCriteria.searchMap['effectiveStartDate']}" style="width: 175px" /></td>
			<td colspan=10></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Fee" /></td>
			<td class="wide">
				<select class="flat form-control input-sm" id="fee" name="fee" style="width: 175px !important">
					<option value="">----Please Select----
					</option>
					<c:forEach items="${uniqueCityFees}" var="aCityFee">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['fee'] == aCityFee}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCityFee}" ${selected}>${aCityFee}</option>
					</c:forEach>
				</select>
			</td>	
			 <td class="form-left"><transys:label code="Effective Date To"/></td>
		     <td class="wide"><input class="flat" id="datepicker7" name="effectiveEndDate" value="${sessionScope.searchCriteria.searchMap['effectiveEndDate']}" style="width: 175px !important" /></td> 
		</tr>
		<tr>
			<td></td>
			<td><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="cityFee.do" id="cityFeeObj" class="tab-color">
	<transys:datatable urlContext="masterData/cityFee" deletable="false"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['cityFeeSearchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="cityFee">
		<transys:textcolumn headerText="City" dataField="suburbName" />
		<transys:textcolumn headerText="Fee" dataField="fee" />
		<transys:textcolumn headerText="Effective Date From" dataField="effectiveStartDate" dataFormat="MM/dd/yyyy" />
		<transys:textcolumn headerText="Effective Date To" dataField="effectiveEndDate" dataFormat="MM/dd/yyyy" />
	</transys:datatable>
	<%session.setAttribute("cityFeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


