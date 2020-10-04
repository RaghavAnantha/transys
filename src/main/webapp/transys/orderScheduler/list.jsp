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
  function buildVehicleLocationInfoWindowContent(aVehicleLocation) {
	var content = "<b>" + aVehicleLocation.VehicleNumber + "</b>"
				+ "<hr style=\"height:2px;padding:0px;margin: 0px;color:gray;background-color:gray\">"
				+ aVehicleLocation.DisplayState
				+ "&nbsp;" + aVehicleLocation.Heading 
				+ "&nbsp;" + aVehicleLocation.Speed + "&nbsp;" + "kmph"
				+ "<br/>" + aVehicleLocation.addressStr;
	return content;
}

function buildDeliveryOrderAddressInfoWindowContent(aDeliveryOrderAddress) {
	var content = "<b>"  + "Delivery Order Id: " + aDeliveryOrderAddress.orderId + "</b>"
				+ "<hr style=\"height:2px;padding:0px;margin:0px;color:gray;background-color:gray\">"
				+ aDeliveryOrderAddress.customerName
				+ "<br/>" + aDeliveryOrderAddress.fullAddress;
	return content;
}

function buildPickupOrderAddressInfoWindowContent(aPickupOrderAddress) {
	var content = "<b>" + "Pickup Order Id: " + aPickupOrderAddress.orderId + "</b>"
				+ "<hr style=\"height:2px;padding:0px;margin:0px;color:gray;background-color:gray\">"
				+ aPickupOrderAddress.customerName
				+ "<br/>" + aPickupOrderAddress.fullAddress;
	return content;
}

let map;
function initMap() {
	map = new google.maps.Map(document.getElementById("schedulerMap"), {
	  center: {
		lat: 41.771211,
	    lng: -87.7862544,
	  },
	  zoom: 11,
	});
	
	var bounds = new google.maps.LatLngBounds();
	
	var infoWindow = new google.maps.InfoWindow;
	var vehicleLocationInfoWindowContent = new Array();
	var deliveryOrderAddressInfoWindowContent = new Array();
	var pickupOrderAddressInfoWindowContent = new Array();
	
	var baseVehicleLocationIconUrl = "http://earth.google.com/images/kml-icons/track-directional/track-";
	
	var vehicleLocationList = ${vehicleLocationList};
	console.log(vehicleLocationList);
	for (var i = 0; i < vehicleLocationList.length; i++) {
		var aVehicleLocation = vehicleLocationList[i];
		
		var lat = aVehicleLocation.Latitude;
		var lng = aVehicleLocation.Longitude;
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		
		var iconUrl = baseVehicleLocationIconUrl;
		var iconUrlSuffix = "0";
		var heading = aVehicleLocation.Heading;
		var displayState = aVehicleLocation.DisplayState;
		if (displayState == "Stop") {
			iconUrlSuffix = "none"
		} else if (displayState == "Moving") {
			if (heading == "North") {
				iconUrlSuffix = "0"
			} else if (heading == "North East") {
				iconUrlSuffix = "2"
			} else if (heading == "East") {
				iconUrlSuffix = "4"
			} else if (heading == "South East") {
				iconUrlSuffix = "6"
			} else if (heading == "South") {
				iconUrlSuffix = "8"
			} else if (heading == "South West") {
				iconUrlSuffix = "10"
			} else if (heading == "West") {
				iconUrlSuffix = "12"
			} else if (heading == "Noth West") {
				iconUrlSuffix = "14"
			}
		}
		iconUrl += (iconUrlSuffix + ".png");
		var marker = new google.maps.Marker({
            map: map,
            position: latLng,
            icon: {
            	/*path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            	scale: 4,
            	rotation: aVehicleLocation.Direction*/
            	url: iconUrl,
            	scaledSize : new google.maps.Size(40, 40) // 30, 30
            },
			title: ('Vehicle: ' + aVehicleLocation.VehicleNumber)
        });
		
		var content = buildVehicleLocationInfoWindowContent(aVehicleLocation);
		vehicleLocationInfoWindowContent.push(content);
		google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
            	infoWindow.setContent(vehicleLocationInfoWindowContent[i]);
                infoWindow.open(map, marker);
            }
        })(marker, i));
	}
	
	var deliveryOrderAddressList = ${deliveryOrderAddressList};
	console.log(deliveryOrderAddressList);
	for (var i = 0; i < deliveryOrderAddressList.length; i++) {
		var aDeliveryOrderAddress = deliveryOrderAddressList[i];
		
		var lat = aDeliveryOrderAddress.latitude;
		var lng = aDeliveryOrderAddress.longitude;
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		var marker = new google.maps.Marker({
            map: map,
            position: latLng,
            label: {
                text: 'D',
                color: 'black',
                //fontSize: "8px"
              },
           	
            icon: {
            	url: "http://maps.google.com/mapfiles/kml/paddle/grn-blank.png",
            	scaledSize : new google.maps.Size(40, 40),
            	labelOrigin: new google.maps.Point(18,12)
            },
			title: 'Delivery Order Id: ' + aDeliveryOrderAddress.addressStr
        });
		
		var content = buildDeliveryOrderAddressInfoWindowContent(aDeliveryOrderAddress);
		deliveryOrderAddressInfoWindowContent.push(content);
		google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent(deliveryOrderAddressInfoWindowContent[i]);
                infoWindow.open(map, marker);
            }
        })(marker, i));
	}
	
	var pickupOrderAddressList = ${pickupOrderAddressList};
	console.log(pickupOrderAddressList);
	for (var i = 0; i < pickupOrderAddressList.length; i++) {
		var aPickupOrderAddress = pickupOrderAddressList[i];
		
		var lat = aPickupOrderAddress.latitude;
		var lng = aPickupOrderAddress.longitude;
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		var marker = new google.maps.Marker({
            map: map,
            position: latLng,
            icon: {
                url: "http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png",
                scaledSize : new google.maps.Size(35, 35)
            },
			title: 'Pickup Order Id: ' + aPickupOrderAddress.orderId
        });
		
        var content = buildPickupOrderAddressInfoWindowContent(aPickupOrderAddress);
		pickupOrderAddressInfoWindowContent.push(content);
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent(pickupOrderAddressInfoWindowContent[i]);
                infoWindow.open(map, marker);
            }
        })(marker, i));
	}
	
	map.fitBounds(bounds);
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
