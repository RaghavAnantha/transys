<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateExport() {
	var reportData = $('#dumpsterOnsiteReportData').html();
	if (reportData == "") {
		showAlertDialog("Data Validation", "No data retrieved to export");
		return false;
	}
	
	return true;
}

function validateSubmit() {
	return true;
}
</script>
<br />
<h4 style="margin-top: -15px; !important">Dumpsters On-site Report</h4>
<form:form action="search.do" method="get" name="dumpstersOnsiteReportSearchForm" id="dumpstersOnsiteReportSearchForm">
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="dumpsterOnsiteReport" />
		<jsp:param name="errorCtx" value="dumpsterOnsiteReport" />
	</jsp:include>
	<table id="form-table" class="table">
	 	<tr><td colspan=10></td><td colspan=10></td></tr>
		<tr>
			<td class="form-left">Dumpster Size</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="dumpsterSize" name="dumpsterSize" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${dumpsterSizes}" var="aDumpsterSize">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['dumpsterSize'] == aDumpsterSize.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDumpsterSize.id}" ${selected}>${aDumpsterSize.size}</option>
					</c:forEach>
				</select>
			</td>
			<td class="form-left">Status</td>
			<td class="wide">
				<select class="flat form-control input-sm" id="status" name="status" style="width: 175px !important">
					<option value="">----Please Select----</option>
					<c:forEach items="${dumpsterStatus}" var="status">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == status.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${status.id}" ${selected}>${status.status}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="submit" class="btn btn-primary btn-sm btn-sm-ext"
					value="<transys:label code="Preview"/>" />
				<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="export.do?type=pdf" onclick="return validateExport();">
				<img src="${pdfImage}" border="0" class="toolbarButton" title="PDF"/>
			</a>
			<a href="export.do?type=xlsx">
				<img src="${excelImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="XLSX"/>
			</a>
			<a href="export.do?type=csv">
				<img src="${csvImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="CSV"/>
			</a>
			<a href="export.do?type=print" target="_blank" onclick="return validateExport();">
				<img src="${printImage}" border="0" class="toolbarButton" title="Print"/>
			</a>
		</td>
		</tr>
		<tr>
			<td align="${left}" width="100%" valign="top">
				<div id="dumpsterOnsiteReportData"></div>
			</td>
		</tr>
</table>
<script language="javascript">
$("#dumpstersOnsiteReportSearchForm").submit(function (ev) {
	if (!validateSubmit()) {
		return false;
	}
	
	var reportData = $('#dumpsterOnsiteReportData');
	reportData.html("${reportLoadingMsg}");
	
	var $this = $(this);
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	reportData.html("");
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		var errorMsg = responseData.replace("ErrorMsg: ", "");
       			showAlertDialog("Error", errorMsg);
        	} else {
        		reportData.html(responseData);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>
