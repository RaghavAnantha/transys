<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Master Data</title>
</head>
<body>
<ul class="nav nav-tabs" id="customer_tabs">
		<li><a href="#employees" data-toggle="tab">Employees</a></li>
		<li><a href="#dumpsters" data-toggle="tab">Dumpsters</a></li>
		<li><a href="#materialType" data-toggle="tab">Material Type</a></li>
		<li><a href="#paymentMethod" data-toggle="tab">Payment Method</a></li>
		<li><a href="#dumpsterSize" data-toggle="tab">Dumpster Size</a></li>
		<li><a href="#locationType" data-toggle="tab">Location Type</a></li>
		<li><a href="#dumpsterPrice" data-toggle="tab">Dumpster Price</a></li>
		<li><a href="#customerDumpsterPrice" data-toggle="tab">Customer Dumpster Price</a></li>
		<li><a href="#cityFee" data-toggle="tab">City Fee</a></li>
		<li><a href="#permitFee" data-toggle="tab">Permit Fee</a></li>
		<li><a href="#overWeightFee" data-toggle="tab">Over Weight Fee</a></li>
		<li><a href="#additionalFee" data-toggle="tab">Additional Fee</a></li>
	</ul>
	
	<div class="tab-content" style="background-color: white;">
		<div id="employees" class="tab-pane">
			<%@include file="list.jsp"%>
		</div>
	</div>

</body>
</html>