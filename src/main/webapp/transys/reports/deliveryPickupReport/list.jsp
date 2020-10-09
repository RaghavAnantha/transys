<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
function validateExport() {
	var reportData = $('#deliveryPickupReportData').html();
	if (reportData == "") {
		showAlertDialog("Data Validation", "No data retrieved to export");
		return false;
	}
	
	return true;
}

function validateSubmit() {
	var deliveryDateFrom = $("[name='deliveryDateFrom']").val();
	var deliveryDateTo = $("[name='deliveryDateTo']").val();
	var pickupDateFrom = $("[name='pickupDateFrom']").val();
	var pickupDateTo = $("[name='pickupDateTo']").val();
	var deliveryAddress = $('#deliveryAddress').val();
	
	var missingData = false;
	if (deliveryDateFrom == "" && deliveryDateTo == ""
			&& pickupDateFrom == "" && pickupDateTo == ""
			&& deliveryAddress == "") {
		missingData = true;
	}
	
	if (missingData) {
		var alertMsg = "<span style='color:red'><b>Please select any search criteria</b><br></span>"
		showAlertDialog("Data Validation", alertMsg);
		
		return false;
	}
	
	return true;
}
</script>
<br/>
<h4 style="margin-top: -15px; !important">Delivery/Pickup Report</h4>
<form:form action="search.do" method="get" name="deliveryPickUpReportSearchForm" id="deliveryPickUpReportSearchForm" >
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="deliveryPickupReport" />
		<jsp:param name="errorCtx" value="deliveryPickupReport" />
	</jsp:include>
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
		<tr>
			<td class="form-left "><transys:label code="Delivery Date From"/></td>
			<td class="wide"><input class="flat" id="datepicker1" name="deliveryDateFrom" style="width: 175px" /></td>
			
			<td align="${left}" class="form-left"><transys:label code="Delivery Date To"/></td>
			   <td align="${left}" class="wide"><input class="flat" id="datepicker2" name="deliveryDateTo" style="width: 175px" /></td>
		 </tr>
		 <tr>
			<td align="${left}" class="form-left"><transys:label code="Pickup Date From"/></td>
			<td align="${left}" class="wide"><input class="flat" id="datepicker3" name="pickupDateFrom" style="width: 175px" /></td>
			<td align="${left}" class="form-left"><transys:label code="Pickup Date To"/></td>
			<td align="${left}" class="wide"><input class="flat" id="datepicker4" name="pickupDateTo" style="width: 175px" /></td>
		 </tr>
		 <tr>
			<td align="${left}" class="form-left"><transys:label code="Delivery Address" /></td>
			<td align="${left}">
				<select class="flat form-control input-sm" id="deliveryAddress" name="deliveryAddress" style="width: 175px !important">
					<option value="">------<transys:label code="Please Select" />------</option>
					<c:forEach items="${deliveryAddresses}" var="aDeliveryAddress">
						<c:if test="${not empty aDeliveryAddress.line1}">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['deliveryAddress'] == aDeliveryAddress.id}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${aDeliveryAddress.id}" ${selected}>${aDeliveryAddress.fullLine}</option>
						</c:if>
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
				<div id="deliveryPickupReportData"></div>
			</td>
		</tr>
</table>
<script language="javascript">
$("#deliveryPickUpReportSearchForm").submit(function (ev) {
	if (!validateSubmit()) {
		return false;
	}
	
	var reportData = $('#deliveryPickupReportData');
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
