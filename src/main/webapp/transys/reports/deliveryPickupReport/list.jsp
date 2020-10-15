<%@include file="/common/taglibs.jsp"%>

<script language="javascript">
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
<form:form action="searchModel.do" method="get" name="deliveryPickUpReportSearchForm" id="deliveryPickUpReportSearchForm" >
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="${msgCtx}" />
		<jsp:param name="errorCtx" value="${errorCtx}" />
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
<jsp:include page="../reportToolbar.jsp">
	<jsp:param name="reportSearchForm" value="deliveryPickUpReportSearchForm" />
	<jsp:param name="reportDataElem" value="deliveryPickupReportData" />
</jsp:include>
