<%@include file="/common/taglibs.jsp"%>

<%@page import="com.transys.service.map.MapConfigConstants"%>

<script language="javascript">
function validateSubmit() {
	return true;
}
</script>

<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
<script
  src="https://maps.googleapis.com/maps/api/js?key=<%=MapConfigConstants.apiKey%>&callback=initMap&libraries=&v=weekly"
  defer
></script>

<script>
"use strict";

var rdsLat = <%=MapConfigConstants.rdsLatitude%>;
var rdsLng = <%=MapConfigConstants.rdsLongitude%>;

/*
var baseVehicleLocationMovingIconUrl = "http://earth.google.com/images/kml-icons/track-directional/track-";
var baseVehicleLocationStaticIconUrl = "http://maps.google.com/mapfiles/kml/pal3/icon";

var rdsIconUrl = "http://maps.google.com/mapfiles/kml/paddle/R.png";
var deliveryOrderIconUrl = "http://maps.google.com/mapfiles/kml/paddle/orange-blank.png";
var pickupOrderIconUrl = "http://maps.google.com/mapfiles/kml/pushpin/red-pushpin.png";
*/

var baseVehicleLocationMovingIconUrl = "${mapImageCtx}/track-";
var baseVehicleLocationStaticIconUrl = "${mapImageCtx}/icon";

var rdsIconUrl = "${mapImageCtx}/R.png";
var deliveryOrderIconUrl = "${mapImageCtx}/orange-blank.png";
var pickupOrderIconUrl = "${mapImageCtx}/red-pushpin.png";

var hrTagDark = "<hr style=\"height:2px;padding:0px;margin:0px;color:black;background-color:black\">";
var hrTagLight = "<hr style=\"height:1px;padding:0px;margin:0px;color:gray;background-color:gray\">";

var directionsService;
//var directionsRenderer;

let map;
var bounds;
var infoWindow;

var startLatLng;

var vehicleLocationInfoWindowContent = new Array();
var deliveryOrderAddressInfoWindowContent = new Array();
var pickupOrderAddressInfoWindowContent = new Array();

function buildVehicleLocationIconUrl(heading, displayState) {
	var iconUrl = baseVehicleLocationMovingIconUrl;
	var iconUrlSuffix = "0";
	if (displayState == "Stop") {
		//iconUrlSuffix = "none";
		iconUrl = baseVehicleLocationStaticIconUrl;
		iconUrlSuffix = 38;
	} if (displayState == "Idle") {
		iconUrl = baseVehicleLocationStaticIconUrl;
		iconUrlSuffix = 53;
	} else if (displayState == "Moving") {
		if (heading == "North") {
			iconUrlSuffix = "0";
		} else if (heading == "North East") {
			iconUrlSuffix = "2";
		} else if (heading == "East") {
			iconUrlSuffix = "4";
		} else if (heading == "South East") {
			iconUrlSuffix = "6";
		} else if (heading == "South") {
			iconUrlSuffix = "8";
		} else if (heading == "South West") {
			iconUrlSuffix = "10";
		} else if (heading == "West") {
			iconUrlSuffix = "12";
		} else if (heading == "North West") {
			iconUrlSuffix = "14";
		}
	}
	iconUrl += (iconUrlSuffix + ".png");
	
	return iconUrl;
}

function buildVehicleLocationInfoWindowContent(aVehicleLocation) {
	var content = "<b>" + aVehicleLocation.VehicleNumber
				+ "&nbsp;&nbsp;&nbsp;" + "Dumpster No." + "&nbsp;Dumpster Size" + "</b>"
				+ hrTagDark
				+ aVehicleLocation.DisplayState
				+ "&nbsp;" + aVehicleLocation.Heading 
				+ "&nbsp;" + aVehicleLocation.Speed + "&nbsp;" + "mph"
				+ "<br/>" + aVehicleLocation.addressStr
				+ "<br/>" + "Last Updated: " + aVehicleLocation.updateDateTime
				+ hrTagLight
				+ "Delivery Order #: 12345"
				+ "<br/>" + "Company Name"
				+ "<br/>" + "Delivery Address"
				+ hrTagLight
				+ "Driver Name";
				
	return content;
}

function buildDeliveryOrderAddressInfoWindowContent(aDeliveryOrderAddress) {
	var content = "<b>"  + "Delivery Order #: " + aDeliveryOrderAddress.orderId 
				+ "&nbsp;Delivery Date Time: " + aDeliveryOrderAddress.deliveryDateTimeRange + "</b>"
				+ hrTagDark
				+ aDeliveryOrderAddress.customerName
				+ "<br/>" + aDeliveryOrderAddress.fullAddress
				+ hrTagLight
				+ "Dumpster: " + aDeliveryOrderAddress.dumpsterSize;
	return content;
}

function buildPickupOrderAddressInfoWindowContent(aPickupOrderAddress) {
	var content = "<b>" + "Pickup Order #: " + aPickupOrderAddress.orderId + "</b>"
				+ hrTagDark
				+ aPickupOrderAddress.customerName
				+ "<br/>" + aPickupOrderAddress.fullAddress
				+ hrTagLight
				+ "Dumpster: " + aPickupOrderAddress.dumpsterNum + "&nbsp;" + aPickupOrderAddress.dumpsterSize;
	return content;
}

function buildRoutePromptInfoWindowContent(lat, lng) {
	var content =  "Select for calculating distance:" + "<br/>"
			 + "<input type=\"radio\" id=\"distanceLocationSelectorStart\" name=\"distanceLocationSelector\" value=\"distanceLocationSelectorStart\" onclick=\"processDistanceCalculationStart("+lat+","+lng+");\">"
			 + "<label for=\"distanceLocationSelectorStart\" style=\"text-align:center;vertical-align:middle;\">&nbsp;&nbsp;Start</label><br>"
			 + "<input type=\"radio\" id=\"distanceLocationSelectorEnd\" name=\"distanceLocationSelector\" value=\"distanceLocationSelectorEnd\" onclick=\"processDistanceCalculationEnd("+lat+","+lng+");\">"
			 + "<label for=\"distanceLocationSelectorEnd\" style=\"text-align:center;vertical-align:middle;\">&nbsp;&nbsp;End</label><br>";
	return content;
}

function buildRouteInfoWindowContent(startAddress, endAddress, distance, duration) {
	var content = "<b>Start: </b>" + startAddress + "<br/>"
			 + "<b>End: </b>" + endAddress + "<br/>"
			 + "<b>Distance: </b>" + distance + "<b> Duration: </b>" + duration;
	return content;
}

function initMap() {
	map = new google.maps.Map(document.getElementById("orderSchedulerMap"), {
		center: {
			lat: rdsLat,
			lng: rdsLng
		 },
		 zoom: 11
	});
	
	addRDSMarker();
	
	directionsService = new google.maps.DirectionsService();
	//directionsRenderer = new google.maps.DirectionsRenderer({suppressMarkers: true});
	//directionsRenderer.setMap(map);
	
	bounds = new google.maps.LatLngBounds();
	infoWindow = new google.maps.InfoWindow;
		
	var vehicleLocationList = ${vehicleLocationList};
	plotVehicleLocations(vehicleLocationList);
	
	var deliveryOrderAddressList = ${deliveryOrderAddressList};
	plotDeliveryOrderLocations(deliveryOrderAddressList);
	
	var pickupOrderAddressList = ${pickupOrderAddressList};
	plotPickupOrderLocations(pickupOrderAddressList);
	
	map.fitBounds(bounds);
}

function addRDSMarker() {
	var latLng = new google.maps.LatLng(rdsLat, rdsLng);
	var title = "RDS";
	var iconScaledSize = new google.maps.Size(40, 40);
	var marker = constructMarker(latLng, rdsIconUrl, iconScaledSize, title);
	addMarkerClickListenerForInfo(marker, "Ravenswood Disposal Service");
	addMarkerRightClickListenerForRoute(marker);
}

function plotVehicleLocations(vehicleLocationList) {
	if (vehicleLocationList == null || vehicleLocationList.length <= 0) {
		return;
	}
	
	var defaultIconScaledSize = new google.maps.Size(27, 27);
	var movingIconScaledSize = new google.maps.Size(38, 38);
	for (var i = 0; i < vehicleLocationList.length; i++) {
		var aVehicleLocation = vehicleLocationList[i];
		var lat = aVehicleLocation.Latitude;
		var lng = aVehicleLocation.Longitude;
		if (lat == null || lng == null) {
			continue;
		}
		
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		
		var heading = aVehicleLocation.Heading;
		var displayState = aVehicleLocation.DisplayState;
		var iconUrl = buildVehicleLocationIconUrl(heading, displayState);
		var iconScaledSize = (displayState == "Moving" ? movingIconScaledSize : defaultIconScaledSize);
		
		var title = ('Vehicle: ' + aVehicleLocation.VehicleNumber);
		var marker = constructMarker(latLng, iconUrl, iconScaledSize, title);
		
		var content = buildVehicleLocationInfoWindowContent(aVehicleLocation);
		vehicleLocationInfoWindowContent.push(content);
		
		addMarkerClickListenerForInfo(marker, content);
		addMarkerRightClickListenerForRoute(marker);
	}
}

function plotDeliveryOrderLocations(deliveryOrderAddressList) {
	if (deliveryOrderAddressList == null || deliveryOrderAddressList.length <= 0) {
		return;
	}
	
	var iconScaledSize = new google.maps.Size(40, 40);
	var iconLabelOrigin = new google.maps.Point(21, 12);
	for (var i = 0; i < deliveryOrderAddressList.length; i++) {
		var aDeliveryOrderAddress = deliveryOrderAddressList[i];
		var lat = aDeliveryOrderAddress.latitude;
		var lng = aDeliveryOrderAddress.longitude;
		if (lat == null || lng == null) {
			continue;
		}
		
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		
		var marker = new google.maps.Marker({
            map: map,
            position: latLng,
            label: {
                text: 'D',
                color: 'black'
                //fontSize: "8px"
              },
            icon: {
            	url: deliveryOrderIconUrl,
            	scaledSize : iconScaledSize,
            	labelOrigin: iconLabelOrigin
            },
			title: 'Delivery Order #: ' + aDeliveryOrderAddress.orderId
        });
		
		var content = buildDeliveryOrderAddressInfoWindowContent(aDeliveryOrderAddress);
		deliveryOrderAddressInfoWindowContent.push(content);
		
		addMarkerClickListenerForInfo(marker, content);
		addMarkerRightClickListenerForRoute(marker);
	}
}

function plotPickupOrderLocations(pickupOrderAddressList) {
	if (pickupOrderAddressList == null || pickupOrderAddressList.length <= 0) {
		return;
	}
	
	var iconScaledSize = new google.maps.Size(35, 35);
	for (var i = 0; i < pickupOrderAddressList.length; i++) {
		var aPickupOrderAddress = pickupOrderAddressList[i];
		var lat = aPickupOrderAddress.latitude;
		var lng = aPickupOrderAddress.longitude;
		if (lat == null || lng == null) {
			continue;
		}
		
		var latLng = new google.maps.LatLng(lat, lng);
		bounds.extend(latLng);
		
		var title = ('Pickup Order #:' + aPickupOrderAddress.orderId);
		var marker = constructMarker(latLng, pickupOrderIconUrl, iconScaledSize, title);
		
        var content = buildPickupOrderAddressInfoWindowContent(aPickupOrderAddress);
		pickupOrderAddressInfoWindowContent.push(content);
       
		addMarkerClickListenerForInfo(marker, content);
        addMarkerRightClickListenerForRoute(marker);
	}
}

function constructMarker(latLng, iconUrl, iconScaledSize, title) {
	var marker = new google.maps.Marker({
        map: map,
        position: latLng,
        icon: {
        	/*path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
        	scale: 4,
        	rotation: aVehicleLocation.Direction*/
        	url: iconUrl,
        	scaledSize: iconScaledSize
        },
		title: title
    });
	
	return marker;
}

function addMarkerClickListenerForInfo(marker, info) {
	google.maps.event.addListener(marker, 'click', (function(marker) {
    	return function() {
           infoWindow.setContent(info);
           infoWindow.open(map, marker);
       }
   })(marker));
}

function addMarkerRightClickListenerForRoute(marker) {
	google.maps.event.addListener(marker, 'rightclick', (function(marker) {
        return function() {
        	var lat = marker.getPosition().lat();
        	var lng = marker.getPosition().lng();
        	var content = buildRoutePromptInfoWindowContent(lat, lng);
        	infoWindow.setContent(content);
        	infoWindow.open(map, marker);
        }
    })(marker));
}

function processDistanceCalculationStart(startLat, startLng) {
	startLatLng = {lat: startLat, lng: startLng};
	infoWindow.close();
}

function drawRoute(path, routeInfo) {
	var pathOptions = {
		path: path,
		strokeColor: '#0000CC',
		strokeWeight: 5,
		opacity: 0.4,
		geodesic: true,
		map: map
	}
	
	var routePolyline = new google.maps.Polyline(pathOptions);
	google.maps.event.addListener(routePolyline, 'click', function(event) {
		infoWindow.setContent(routeInfo);
		infoWindow.setPosition(event.latLng);
		infoWindow.open(map);
	});
	google.maps.event.addListener(routePolyline, 'rightclick', function(event) {
		routePolyline.setMap(null);
		routePolyline = null;
	});
	/*google.maps.event.addListener(routePolyline, 'dblclick', (function(event, msg) {
        return function() {
        	infoWindow.open(map, event.latLng);
        }
    })(event, msg));*/
}

function processDistanceCalculationEnd(endLat, endLng) {
	if (!startLatLng) {
		return;
	}
	
	var endLatLng = {lat: endLat, lng: endLng};
	var request = {
		origin: startLatLng,
		destination: endLatLng,
		travelMode: 'DRIVING'
	};
	directionsService.route(request, function(result, status) {
		if (status != 'OK') {
			return;
		}
		if (result == null || result.routes.length <= 0 || result.routes[0].legs.length <= 0) {
			return;
		}
		
		var leg = result.routes[0].legs[0];
		var startAddress = leg.start_address;
		var endAddress = leg.end_address;
		var distance = leg.distance.text;
		var duration = leg.duration.text;
		var routeInfo = buildRouteInfoWindowContent(startAddress, endAddress, distance, duration);
   		infoWindow.setContent(routeInfo);
   		infoWindow.open(map);
   		
   		//directionsRenderer.setDirections(result);
   		var path = result.routes[0].overview_path;  
   		drawRoute(path, routeInfo);
	});
}
</script>
<br/>
<h5 style="margin-top: -15px; !important">Order Scheduler</h5>
<div>
    <div class="accordion" id="orderSchedulerAccordion" style="margin-top: -15px; !important">
        <div class="card">
            <div class="card-header" id="orderSchedulerCardHeader">
                <h2 class="mb-0">
                    <button type="button" class="btn btn-link btn-sm-ext" data-toggle="collapse" data-target="#orderSchedulerCollapse"><i class="fa fa-plus"></i> Search</button>									
                </h2>
            </div>
            <div id="orderSchedulerCollapse" class="collapse" aria-labelledby="orderSchedulerCardHeader" data-parent="#orderSchedulerAccordion">
                <div class="card-body">
                    <form:form action="search.do" method="get" name="orderSchedulerSearchForm" id="orderSchedulerSearchForm" >
						<jsp:include page="/common/messages.jsp">
							<jsp:param name="msgCtx" value="${msgCtx}" />
							<jsp:param name="errorCtx" value="${errorCtx}" />
						</jsp:include>
						<table id="form-table" class="table">
						 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
							<tr>
								  <td class="form-left "><transys:label code="Delivery Date From"/></td>
								  <td class="wide">
								  	<input class="flat" id="datepicker1" name="deliveryDateFrom" style="width: 175px" 
								  		value="${sessionScope.searchCriteria.searchMap['deliveryDateFrom']}"/>
								  </td>
								  <td class="form-left"><transys:label code="Delivery Date To"/></td>
							      <td class="wide">
							      	<input class="flat" id="datepicker2" name="deliveryDateTo" style="width: 175px" 
							      		value="${sessionScope.searchCriteria.searchMap['deliveryDateTo']}"/>
							      </td>
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
										value="<transys:label code="Search"/>"/>
									<input type="reset" class="btn btn-primary btn-sm btn-sm-ext" value="Clear"/>
								</td>
							</tr>
							<tr><td></td></tr>
						</table>
					</form:form>
                </div>
            </div>
        </div>
     </div>
</div>
<!--  
<div id="orderSchedulerConsoleDiv">
    <label for="orderSchedulerConsole" style="text-align:center;vertical-align:middle;float:right">Console&nbsp;&nbsp;<input class="flat" id="orderSchedulerConsole" name="orderSchedulerConsole" style="width:350px; float:right" /></label>
</div>
-->
<div id="orderSchedulerMap" style="width: 100%; height: 500px;"></div>

<script language="javascript">
$("#orderSchedulerSearchForm").submit(function (ev) {
	if (!validateSubmit()) {
		return false;
	}
});
</script>
<script type="text/javascript" src="${jsCtx}/accordian.js"></script>
