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
		<tr><td></td></tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<!--  
			<a href="export.do?type=print" target="_blank" onclick="return validateExport();">
				<img src="${printImage}" border="0" class="toolbarButton" title="Print"/>
			</a>
			-->
			<a href="export.do?type=pdf" onclick="return validateExport();">
				<img src="${pdfImage}" border="0" class="toolbarButton" title="PDF"/>
			</a>
			<a href="export.do?type=xlsx">
				<img src="${excelImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="XLSX"/>
			</a>
			<a href="export.do?type=csv">
				<img src="${csvImage}" border="0" class="toolbarButton" onclick="return validateExport();" title="CSV"/>
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
        		showAlertDialog("Exception", "Error while processing request");
        	} else {
        		reportData.html(responseData);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>

<!-- 
<hr class="hr-ext">
<table class="table" id="form-table"> 
	<tr><td></td></tr>
	<tr>
		<td class="form-left">Total Dumpsters/Boxes Delivered (or Picked up) for:</td>
	</tr>
</table>
<table class="table" id="form-table"> 	
	<tr>
		<td class="form-left form-left-ext"><transys:label code="Delivery Date Range:" /></td>
		<td class="wide td-static" id="deliveryDateRange">${deliveryDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${deliveryDateTo}</td>

		<td class="form-left  td-static">Pickup Date Range:</td>
		<td id="pickupDateRange" class="td-static">${pickupDateFrom}&nbsp;&nbsp;To&nbsp;&nbsp;${pickupDateTo}</td>
	</tr>
	<tr>
		<td class="td-static">${dumpsterSizeAggregation}</td>
	</tr> 
</table>

<a href="generateDeliveryPickupReport.do?type=xls"><img src="${ctx}/images/excel.png" border="0" style="float:right" class="toolbarButton"></a>
<a href="generateDeliveryPickupReport.do?type=pdf"><img src="${ctx}/images/pdf.png" border="0" style="float:right" class="toolbarButton"></a>
<form:form name="deliveryPickupReport" id="deliveryPickupReport" class="tab-color">
	<transys:datatable urlContext="reports/deliveryPickupReport"  baseObjects="${ordersList}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="list.do" dataQualifier="deliveryPickupReport">
		<transys:textcolumn headerText="Order #" dataField="id" />
		<transys:textcolumn headerText="Customer" dataField="customer.companyName" />
		<transys:textcolumn headerText="Delivery Address" dataField="deliveryAddress.fullLine" />
		<transys:textcolumn headerText="City" dataField="deliveryAddress.city" />
		<transys:textcolumn headerText="Dumpster Size" dataField="dumpsterSize.size" />
		<transys:textcolumn headerText="Dumpster #" dataField="dumpster.dumpsterNum" />
		<transys:textcolumn headerText="Delivery Date" dataField="deliveryDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Pickup Date" dataField="pickupDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Tonnage" dataField="netWeightTonnage" />
		<transys:textcolumn headerText="Driver Name" dataField="dropOffDriver.name" />
		<transys:textcolumn headerText="Location" dataField="dumpsterLocation" />
	</transys:datatable>
	<session.setAttribute("deliveryPickupReportColumnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
-->
