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
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
<script
  src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBgZDQfU6LvIVCGmNDRIk17R74GcUMjj5o&callback=initMap&libraries=&v=weekly"
  defer
></script>
<script>
"use strict";
let map;
function initMap() {
	map = new google.maps.Map(document.getElementById("schedulerMap"), {
	  center: {
		lat: 41.771211,
	    lng: -87.7862544,
	  },
	  zoom: 10,
	});
}
</script>
<br/>
<h4 style="margin-top: -15px; !important">Scheduler</h4>
<form:form action="search.do" method="get" name="schedulerSearchForm" id="schedulerSearchForm" >
	<jsp:include page="/common/messages.jsp">
		<jsp:param name="msgCtx" value="schedulerSearch" />
		<jsp:param name="errorCtx" value="schedulerSearch" />
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
<div id="schedulerMap" style="width: 100%; height: 500px;"></div>
<!--
<div id="staticSchedulerMap" style="width: 100%; height: 1000px;">
	<img src="https://maps.googleapis.com/maps/api/staticmap?center=41.771211,-87.7862544&zoom=10&size=2000x2000&maptype=roadmap&markers=color:red%7Clabel:C%7C41.771211,-87.7862544&markers=color:blue%7Clabel:S%7C41.799281,-87.742934&markers=color:green%7Clabel:G%7C41.962855,-87.716021&key=AIzaSyBgZDQfU6LvIVCGmNDRIk17R74GcUMjj5o"/>
</div>
-->
<script language="javascript">
$("#schedulerSearchForm").submit(function (ev) {
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
