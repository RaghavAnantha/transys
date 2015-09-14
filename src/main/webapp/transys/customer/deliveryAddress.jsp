<%@include file="/common/taglibs.jsp"%>
<br />
<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td align="${left}" class="form-left"><transys:label
					code="Line 2" /></td>
			<td>
			<input class="flat" id="line2" name="line2" /></td>
			
			<td align="${left}" class="form-left"><transys:label
					code="City" /></td>
			<td>
			<input class="flat" id="city" name="city" /></td>
		</tr>
		
		<tr>
			
		</tr>

		<tr>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				class="btn btn-primary btn-sm"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Save"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="deliveryAddressForm" id="deliveryAddressForm" class="tab-color">
	<transys:datatable urlContext="customer" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true">
		<transys:textcolumn headerText="Delivery Address" dataField="line1" />
		<transys:textcolumn headerText="Address Line 2" dataField="line2" />
		<transys:textcolumn headerText="City" dataField="customer.city" />
		<transys:textcolumn headerText="State" dataField="state.name" />
		<transys:textcolumn headerText="Zipcode" dataField="customer.zipcode" />


	</transys:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


