<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function validateFileToUpload(fileElem, fileTypeAllowed) {
	var fileElem = document.getElementById(fileElem);
	var file = fileElem.value;
	var fileExt = file.lastIndexOf("." + fileTypeAllowed);
	var commaIndx = file.indexOf(",");
	if (fileExt == -1) {    
		showAlertDialog("Data Validation", "Only " + fileTypeAllowed + " file is allowed");
		fileElem.value = "";   
		return false;
	} 
	if (commaIndx != -1) {    
		showAlertDialog("Data Validation", "File name cannot have commas");
		fileElem.value = "";   
		return false;
	}
	
	return true;
}

function processUploadDoc(fileElem) {
	var fileElem = document.getElementById(fileElem);
	var noOfFiles = fileElem.files.length;
	if (noOfFiles == 0) {
		document.getElementById("manageDocsErrorDiv").innerHTML = "Please choose a doc to upload!";
		return;
	}
	
	var file = fileElem.files.item(0).name;
	var id = document.getElementById("id").value;
	var ctxAdj = getCtxAdjustment();
	$.ajax({
  		url: ctxAdj + "ajax.do?action=doesDocExist" + "&id=" + id + "&file=" + file,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var uploadFile = true;
       		if (responseData == "true") {
       			if (!confirm("Do you want to replace the doc already uploaded?")) {
       				uploadFile = false;
           		}
       		}
       		if (uploadFile) {
       			uploadDoc();
       		}
		}
	});
}

function validateCheckedDocs(fileListElem) {
	var checkedCount = 0;
	var checkedDocs = document.getElementsByName(fileListElem);
	for (var i = 0; i < checkedDocs.length; i++) {
		if (checkedDocs[i].checked) {
			checkedCount++;
		}
	}
	if (checkedCount == 0) {
		showAlertDialog("Data Validation", "Select a doc");
		return false;
	}
	if (checkedCount > 1) {
		showAlertDialog("Data Validation", "Select only one doc");
		return false;
	}
	
	return true;
}

function submitManageDocsForm(action) {
	var form = getForm();
	
	var manageDocsCtx = "managedocs";
	var actionPrefix = "";
	if (document.location.href.indexOf(manageDocsCtx) == -1) {
		actionPrefix = manageDocsCtx + "/";
	}
	form.action = actionPrefix + action + "?" + "${csrfParam}";
	form.submit();
}

function uploadDoc() {
	submitManageDocsForm("uploaddoc.do");
}

function processDownloadDoc(fileListElem) {
	if (!validateCheckedDocs(fileListElem)) {
		return;
	}
	
	submitManageDocsForm("downloaddoc.do");
}

function processDeleteDoc(fileListElem) {
	if (!validateCheckedDocs(fileListElem)) {
		return;
	}
	
	if (confirm("Do you want to Delete the selected doc?")) {
		submitManageDocsForm("deletedoc.do");
  	}
}
function getForm() {
	return document.forms["manageDocsForm"];
}

function getCtxAdjustment() {
	var manageDocsCtx = "managedocs";
	var ctxAdj = "";
	if (document.location.href.indexOf(manageDocsCtx) != -1) {
		ctxAdj = "../";
	}
	return ctxAdj;
}

function processCancel() {
	document.location.href = getCtxAdjustment() + "list.do";
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
		<td style="line-height: 1.42857143;font-size: 13px;">
			<form:checkboxes items="${fileList}" path="fileList"/>
		</td>
		<td><form:errors path="fileList" cssClass="error" /></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<input type="button" id="downloadFileBtn" value="Download" class="btn btn-primary btn-sm btn-sm-ext" 
				onclick="javascript:processDownloadDoc('fileList');"/>
			&nbsp;
			<input type="button" id="deleteFileBtn" value="Delete" class="btn btn-primary btn-sm btn-sm-ext" 
				onclick="javascript:processDeleteDoc('fileList');" />
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
			<input type="button" id="uploadBtn" value="Upload" class="btn btn-primary btn-sm btn-sm-ext" onclick="processUploadDoc('dataFile');"/>
			&nbsp;
			<input type="button" id="cancelBtn" class="btn btn-primary btn-sm btn-sm-ext" value="Cancel" class="flat"
				onClick="processCancel();" />
		</td>
	</tr>
</table>
</form:form>

<div id="manageDocsErrorDiv" style="font-size:0.9em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following errors occured while managing Docs:</b><br/>
	<c:forEach var="anError" items="${errorList}">
		<FONT color=#F2290F><STRONG>${anError}</strong></FONT>
	</c:forEach>
</c:if>
