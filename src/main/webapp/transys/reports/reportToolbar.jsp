<%@ include file="/common/taglibs.jsp"%>
<c:set var="reportSearchForm" value="${param.reportSearchForm}" />
<c:set var="reportDataElem" value="${param.reportDataElem}" />
<table width="100%" class="reportToolBar">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${urlCtx}/export.do?type=pdf" onclick="return validateExport();">
				<img src="${pdfImage}" border="0" class="toolbarButton" title="PDF"/>
			</a>
			<a href="${urlCtx}/export.do?type=xlsx">
				<img src="${excelImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="XLSX"/>
			</a>
			<a href="${urlCtx}/export.do?type=csv">
				<img src="${csvImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="CSV"/>
			</a>
			<a href="${urlCtx}/export.do?type=print" target="_blank" onclick="return validateExport();">
				<img src="${printImage}" border="0" class="toolbarButton" title="Print"/>
			</a>
		</td>
		</tr>
		<tr>
			<td align="${left}" width="100%" valign="top">
				<div id="${reportDataElem}"></div>
			</td>
		</tr>
</table>
<script language="javascript">
function validateExport() {
	var reportData = $('#${reportDataElem}').html();
	if (reportData == "" || reportData.indexOf("No data") != -1) {
		showAlertDialog("Data Validation", "No data retrieved to export");
		return false;
	}
	
	return true;
}

$("#${reportSearchForm}").submit(function (ev) {
	if (!validateSubmit()) {
		return false;
	}
	
	var reportDataElem = $('#${reportDataElem}');
	reportDataElem.html("${reportLoadingMsg}");
	
	var $this = $(this);
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	reportDataElem.html("");
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		var errorMsg = responseData.replace("ErrorMsg: ", "");
       			showAlertDialog("Error", errorMsg);
        	} else {
        		reportDataElem.html(responseData);
        	}
        }
    });
    
    ev.preventDefault();
});

function processReportPaging(pagingLink) {
	var reportDataElem = $('#${reportDataElem}');
	reportDataElem.html("${reportLoadingMsg}");
	
	$.ajax({
  		url: pagingLink,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
        	reportDataElem.html("");
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		var errorMsg = responseData.replace("ErrorMsg: ", "");
       			showAlertDialog("Error", errorMsg);
        	} else {
        		reportDataElem.html(responseData);
        	}
        }
    });
	 
	return false; 
}
</script>