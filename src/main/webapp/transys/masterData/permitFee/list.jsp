<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Manage Permit Fees</h5>

<form:form action="list.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr>
			<td class="form-left"><transys:label code="Permit Class" /></td>
			<td class="wide"><select
				class="flat form-control input-sm" id="permitClass" name="permitClass"
				style="width: 175px !important">
					<option value="">----Please Select----
					</option>
					<c:forEach items="${permitClass}" var="aClass">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['permitClass'] == aClass.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aClass.id}" ${selected}>${aClass.permitClass}</option>
					</c:forEach>
			</select></td>	
			<td class="form-left"><transys:label code="Effective Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker6" name="effectiveStartDate" value="${sessionScope.searchCriteria.searchMap['effectiveStartDate']}" style="width: 175px" /></td>
			<td colspan=10></td>
			</tr>
			<tr>
			<td class="form-left"><transys:label code="Permit Type" /></td>
			<td class="wide"><select
				class="flat form-control input-sm" id="permitType" name="permitType"
				style="width: 175px !important">
					<option value="">----Please Select----
					</option>
					<c:forEach items="${permitType}" var="aType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['permitType'] == aType.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aType.id}" ${selected}>${aType.permitType}</option>
					</c:forEach>
			</select></td>
			 <td class="form-left"><transys:label code="Effective Date To"/></td>
		     <td class="wide"><input class="flat" id="datepicker7" name="effectiveEndDate" value="${sessionScope.searchCriteria.searchMap['effectiveEndDate']}" style="width: 175px" /></td> 
			</tr>
			<tr>
				<td class="form-left"><transys:label code="Permit Fee" /></td>
				<td class="wide"><select
					class="flat form-control input-sm" id="fee" name="fee"
					style="width: 175px !important">
						<option value="">----Please Select----
						</option>
						<c:forEach items="${permitFees}" var="aPermitPrice">
							<c:set var="selected" value="" />
							<c:if
								test="${sessionScope.searchCriteria.searchMap['fee'] == aPermitPrice}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aPermitPrice}" ${selected}>${aPermitPrice}</option>
						</c:forEach>
				</select></td>	
			</tr>
			
		<tr>
			<td></td>
			<td><input type="button"
				class="btn btn-primary btn-sm btn-sm-ext"
				onclick="document.forms['searchForm'].submit();"
				value="<transys:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<form:form name="permitFee.do" id="permitFeeObj" class="tab-color">
	<transys:datatable urlContext="masterData/permitFee" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" dataQualifier="permitFee">
		<transys:textcolumn headerText="Permit Class" dataField="permitClass.permitClass" />
		<transys:textcolumn headerText="Permit Type" dataField="permitType.permitType" />
		<transys:textcolumn headerText="Permit Fee" dataField="fee" />
		<transys:textcolumn headerText="Effective Date From" dataField="effectiveStartDate" dataFormat="MM/dd/yyy" />
		<transys:textcolumn headerText="Effective Date To" dataField="effectiveEndDate" dataFormat="MM/dd/yyy" />
	</transys:datatable>
	<%session.setAttribute("permitFeeColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


