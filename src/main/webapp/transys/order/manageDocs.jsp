<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getForm() {
	return document.forms["manageDocsForm"];
}
</script>

<form:form action="uploaddoc.do" method="post" name="manageDocsForm" id="manageDocsForm"
	enctype="multipart/form-data" commandName="modelObject">
<form:hidden path="id" id="id" />
<jsp:include page="/common/messages.jsp">
	<jsp:param name="msgCtx" value="manageDocs" />
</jsp:include>

<table id="form-table" class="table">
	<tr><td></td></tr>
	<tr>
		<td class="form-left">Order #</td>
		<td class="td-static">${modelObject.id}</td>
	</tr>
	<tr>
		<td class="form-left">Uploaded Docs<span class="errorMessage"></span>
		<td style="line-height: 1.42857143;font-size: 13px;"><form:checkboxes items="${fileList}" path="fileList"/></td>
		<td><form:errors path="fileList" cssClass="error" /></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" id="downloadFileBtn" value="Download" class="btn btn-primary btn-sm btn-sm-ext" 
				onclick="javascript:processDownloadDoc('fileList', '${csrfParam}');"/>
			&nbsp;
			<input type="button" id="deleteFileBtn" value="Delete" class="btn btn-primary btn-sm btn-sm-ext" 
				onclick="javascript:processDeleteDoc('fileList', '${csrfParam}');" />
		</td>
	</tr>
	<tr><td></td></tr>
	<tr>
		<td colspan=10 class="section-header" style="line-height: 0.7;font-size: 13px;font-weight: bold;color: white;">Upload Docs</td>
	</tr>
	<tr>
		<td></td>
	</tr>
	<tr>
		<td class="form-left">Upload Doc<span class="errorMessage"></span>
		<td>
			<input style="font-size:13px; padding:0 0px" type="file" id="dataFile" name="dataFile" onchange="return validateFileToUpload('dataFile', 'pdf')"/>
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" id="uploadBtn" value="Upload" class="btn btn-primary btn-sm btn-sm-ext" onclick="javascript:processUploadDoc('dataFile', '${csrfParam}');"/>
			&nbsp;
			<input type="button" id="cancelBtn" class="btn btn-primary btn-sm btn-sm-ext" value="Cancel" class="flat"
				onClick="document.location.href='list.do'" />
		</td>
	</tr>
</table>
</form:form>

<div id="manageDocsErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following errors occured while managing Docs:</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
