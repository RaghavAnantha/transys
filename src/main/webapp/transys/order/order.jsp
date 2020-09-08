<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Orders</title>
</head>
<body>
	<ul class="nav nav-tabs" id="order_main_tabs">
		<li><a href="#manageOrders" data-toggle="tab" >Orders</a></li>
		<li><a href="#orderReports" data-toggle="tab" >Order Reports</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;">
		<div id="manageOrders" class="tab-pane">
			<div id="manageOrderLoadingImgDiv">Loading....<img src="${preLoaderImage}" id="manageOrderLoadingImage"/></div>
			<c:if test="${mode == 'MANAGE'}">
				<%@include file="list.jsp"%>
			</c:if>
			<c:if test="${mode == 'ADD'}">
				<%@include file="addEdit.jsp"%>
			</c:if>
		</div>
		<div id="orderReports" class="tab-pane">
			<%@include file="ordersReport.jsp"%>
		</div>
	</div>
	
<script type="text/javascript">
	function showTab(tab){
		$('.nav-tabs a[href="#' + tab + '"]').tab('show');
	};
	
	showTab('${activeTab}');
	showTab('${activeSubTab}');
	
	$(window).load(function() {
		$("#manageOrderLoadingImgDiv").hide();
	});
</script>
</body>
</html>
