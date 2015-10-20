<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Additional Fees</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="Description" /></td>
			<td class="wide"><select
				class="flat form-control input-sm" id="city" name="description"
				style="width: 175px !important">
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
			<td class="form-left"><transys:label code="Effective Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker6" name="effectiveStartDate" value="${sessionScope.searchCriteria.searchMap['effectiveStartDate']}" style="width: 175px" /></td>
			<td colspan=10></td>
			</tr>
			<tr>
				<td class="form-left"><transys:label code="Fee" /></td>
				<td class="wide"><select
					class="flat form-control input-sm" id="fee" name="fee"
					style="width: 175px  !important">
						<option value="">------
							<transys:label code="Please Select" />------
						</option>
						<c:forEach items="${uniqueAdditionalFees}" var="tempFee">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['fee'] == tempFee}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${tempFee}" ${selected}>${tempFee}</option>
						</c:forEach>
				</select></td>	
			 <td class="form-left"><transys:label code="Effective Date To"/></td>
		     <td class="wide"><input class="flat" id="datepicker7" name="effectiveEndDate" value="${sessionScope.searchCriteria.searchMap['effectiveEndDate']}" style="width: 175px" /></td> 
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
<form:form name="additionalFee.do" id="additionalFeeObj" class="tab-color">
	<transys:datatable urlContext="masterData/additionalFee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="additionalFee">
		<transys:textcolumn headerText="Description" dataField="description" />
		<transys:textcolumn headerText="Fee" dataField="fee" />
		<transys:textcolumn headerText="Effective Date From" dataField="effectiveStartDate" dataFormat="MM/dd/yyy" />
		<transys:textcolumn headerText="Effective Date To" dataField="effectiveEndDate" dataFormat="MM/dd/yyy" />
	</transys:datatable>
	<%session.setAttribute("additionalFeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


