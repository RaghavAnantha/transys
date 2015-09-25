<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Additional Fees</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Description" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="city" name="description"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${additionalFees}" var="aFee">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['description'] == aFee.description}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aFee.description}" ${selected}>${aFee.description}</option>
					</c:forEach>
			</select></td>	
			</tr>
			<tr>
				<td align="${left}" class="form-left"><transys:label code="Fee" /></td>
				<td align="${left}" class="wide"><select
					class="flat form-control input-sm" id="fee" name="fee"
					style="width: 175px">
						<option value="">------
							<transys:label code="Please Select" />------
						</option>
						<c:forEach items="${additionalFees}" var="tempFee">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['fee'] == tempFee.fee}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${tempFee.fee}" ${selected}>${tempFee.fee}</option>
						</c:forEach>
				</select></td>	
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
<form:form name="additionalFee.do" id="additionalFeeObj" class="tab-color">
	<transys:datatable urlContext="additionalFee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Description" dataField="description" />
		<transys:textcolumn headerText="Fee" dataField="fee" />
	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


