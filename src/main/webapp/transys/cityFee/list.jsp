<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage City Fees</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="City" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="suburbName" name="suburbName" style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${cityFees}" var="aCity">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.cityFeeSearchCriteria.searchMap['suburbName'] == aCity.suburbName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCity.suburbName}" ${selected}>${aCity.suburbName}</option>
					</c:forEach>
				</select>
			</td>	
		</tr>
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Fee" /></td>
			<td align="${left}" class="wide">
				<select class="flat form-control input-sm" id="fee" name="fee" style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${cityFees}" var="aCityFee">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.cityFeeSearchCriteria.searchMap['fee'] == aCityFee.fee}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCityFee.fee}" ${selected}>${aCityFee.fee}</option>
					</c:forEach>
				</select>
			</td>	
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
<form:form name="cityFee.do" id="cityFeeObj" class="tab-color">
	<transys:datatable urlContext="cityFee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['cityFeeSearchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="cityFee">
		<transys:textcolumn headerText="City" dataField="suburbName" />
		<transys:textcolumn headerText="Fee" dataField="fee" />
		<transys:textcolumn headerText="Effective Date From" dataField="effectiveDateFrom" />
		<transys:textcolumn headerText="Effective Date To" dataField="effectiveDateTo" />
	</transys:datatable>
	<%session.setAttribute("cityFeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


