<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports</title>
</head>
<body>
<ul class="nav nav-tabs" id="reports-tab">
		<li><a href="#revenue" data-toggle="tab">Revenue Report</a></li>
		<li><a href="#delpick" data-toggle="tab">Delivery/Pickup Report</a></li>
		<li><a href="#onsite" data-toggle="tab">Dumpsters On-site Reports</a></li>
		<li><a href="#rented" data-toggle="tab">Dumpsters Rented Reports</a></li>
		<li><a href="#recycle" data-toggle="tab">Recycle Reports</a></li>
	</ul>
	
	<div class="tab-content" style="background-color: white;">
		<div id="onsite" class="tab-pane">
			<%@include file="list.jsp"%>
		</div>
	</div>

</body>
</html>