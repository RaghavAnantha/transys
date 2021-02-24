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

function processUploadDoc(fileElem, csrfParam) {
	var fileElem = document.getElementById(fileElem);
	var noOfFiles = fileElem.files.length;
	if (noOfFiles == 0) {
		document.getElementById("manageDocsErrorDiv").innerHTML = "Please choose a doc to upload!";
		return;
	}
	
	var file = fileElem.files.item(0).name;
	var id = document.getElementById("id").value;
	$.ajax({
  		url: "ajax.do?action=doesDocExist" + "&id=" + id + "&file=" + file,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var uploadFile = true;
       		if (responseData == "true") {
       			if (!confirm("Do you want to replace the doc already uploaded?")) {
       				uploadFile = false;
           		}
       		}
       		if (uploadFile) {
       			uploadDoc(csrfParam);
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

function submitManageDocsForm(action, csrfParam) {
	var form = getForm();
	
	var manageDocsCtx = "managedocs";
	var actionPrefix = "";
	if (document.location.href.indexOf(manageDocsCtx) == -1) {
		actionPrefix = manageDocsCtx + "/";
	}
	form.action = actionPrefix + action + "?" + csrfParam;
	form.submit();
}

function uploadDoc(csrfParam) {
	submitManageDocsForm("uploaddoc.do", csrfParam);
}

function processDownloadDoc(fileListElem, csrfParam) {
	if (!validateCheckedDocs(fileListElem)) {
		return;
	}
	
	submitManageDocsForm("downloaddoc.do", csrfParam);
}

function processDeleteDoc(fileListElem, csrfParam) {
	if (!validateCheckedDocs(fileListElem)) {
		return;
	}
	
	if (confirm("Do you want to Delete the selected doc?")) {
		submitManageDocsForm("deletedoc.do", csrfParam);
  	}
}