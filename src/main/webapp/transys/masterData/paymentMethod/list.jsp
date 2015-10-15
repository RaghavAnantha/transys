<%@include file="/common/taglibs.jsp"%>
<br />
<h4 style="margin-top: -15px; !important">Manage Payment Methods</h4>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label code="Payment Method" /></td>
			<td align="${left}" class="wide"><select
				class="flat form-control input-sm" id="method" name="method"
				style="width: 175px">
					<option value="">------
						<transys:label code="Please Select" />------
					</option>
					<c:forEach items="${paymentMethods}" var="apaymentMethod">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['method'] == apaymentMethod.method}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${apaymentMethod.method}" ${selected}>${apaymentMethod.method}</option>
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
<form:form name="paymentMethod.do" id="paymentMethod" class="tab-color">
	<transys:datatable urlContext="masterData/paymentMethod" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="paymentMethod">
		<transys:textcolumn headerText="Payment Method ID" dataField="id" />
		<transys:textcolumn headerText="Payment Method" dataField="method" />
	</transys:datatable>
	<%session.setAttribute("paymentMethodColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


