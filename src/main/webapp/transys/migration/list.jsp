<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Data Migration</h4>

<form:form action="migrate.do" method="POST" name="migrationForm" id="migrationForm" >
	<table id="form-table" class="table">
	 	<tr>
	 		<td class="form-left">Migration Data Type</td>
			<td class="wide">
	 			<select class="flat form-control input-sm" id="migrationDataType" name="migrationDataType" style="width: 175px !important">
					<option value="">---Please Select---</option>
					<c:forEach items="${migrationDataTypeList}" var="aMigrationDataType">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['migrationDataType'] == aMigrationDataType}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aMigrationDataType}" ${selected}>${aMigrationDataType}</option>
					</c:forEach>
				</select>
			</td>
			<td colspan=10></td>
		</tr>
 		<tr>
 			<td></td>
			<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['migrationForm'].submit();"
				value="Migrate" />
			</td>
		</tr>
	</table>
</form:form>
