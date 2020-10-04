<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transys</title>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top navheight" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand active" href="${ctx}/home.do">Transys</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li id="orderPage"><a href="${ctx}/order/main.do">Orders</a></li>
					<li id="permitPage"><a href="${ctx}/permit/main.do">Permits</a></li>
					<li id="customerPage"><a href="${ctx}/customer/main.do">Customers</a></li>
					<li id="invoicePage"><a href="${ctx}/invoice/createInvoiceMain.do">Invoices</a></li>
					<li id="orderSchedulerPage"><a href="${ctx}/orderScheduler/main.do">Scheduler</a></li>
					<li id="materialIntake"><a href="${ctx}/publicMaterialIntake/main.do">Public Material Intake</a></li>
					<li id="reportPage">
						<a href="#" data-toggle="dropdown" class="dropdown-toggle">Reports<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="${reportsMenuCtx}/ordersRevenueReport/main.do">Revenue Report</a></li>
							<li><a href="${reportsMenuCtx}/deliveryPickupReport/main.do">Delivery/Pickup Report</a></li>
							<li><a href="${reportsMenuCtx}/dumpsterOnsiteReport/main.do">Dumpsters On-site Report</a></li>
							<li><a href="${reportsMenuCtx}/dumpsterRentedReport/main.do">Dumpsters Rented Report</a></li>
							<li><a href="${reportsMenuCtx}/recycleReport/main.do">Recycle Report</a></li>
							<li><a href="${reportsMenuCtx}/materialIntakeDailyReport/main.do">Material Intake Daily Report</a></li>
							<li><a href="${reportsMenuCtx}/monthlyTransferStationIntakeReport/main.do">Monthly Transfer Station Intake Report</a></li>
						</ul>
					</li>
					<li id="masterDataPage">
						<a href="#" data-toggle="dropdown" class="dropdown-toggle">Master Data<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li class="dropdown dropdown-submenu"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Employees</a>
								<ul class="dropdown-menu">
									<li><a href="${ctx}/masterData/employee/main.do">Manage Employees</a></li>
									<li><a href="${ctx}/masterData/loginUser/main.do">Manage User Logins</a></li>
								</ul>
							</li>
							<li><a href="${masterDataMenuCtx}/materialType/main.do">Material Type</a></li>
							<li><a href="${masterDataMenuCtx}/materialCategory/main.do">Material Category</a></li>
							<li><a href="${masterDataMenuCtx}/locationType/main.do">Location Type</a></li>
							<li><a href="${masterDataMenuCtx}/recycleLocation/main.do">Recycle Location</a></li>
							<li><a href="${masterDataMenuCtx}/paymentMethod/main.do">Payment Method</a></li>
							<li><a href="${masterDataMenuCtx}/dumpsters/main.do">Dumpsters</a></li>
							<li><a href="${masterDataMenuCtx}/dumpsterPrice/main.do">Dumpster Price</a></li>
							<!--<li><a href="${masterDataMenuCtx}/customerDumpsterPrice/main.do">Customer Dumpster Price</a></li>-->
							<li><a href="${masterDataMenuCtx}/additionalFee/main.do">Additional Fee</a></li>
							<li><a href="${masterDataMenuCtx}/cityFee/main.do">City Fee</a></li>
							<li><a href="${masterDataMenuCtx}/overweightFee/main.do">Overweight Fee</a></li>
							<li><a href="${masterDataMenuCtx}/permitFee/main.do">Permit Fee</a></li>
						</ul>
					</li>
					<!-- <li id="migrationPage"><a href="${ctx}/migration/main.do">Migration</a></li> -->
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="#">
							<transys:label code="Welcome" />
							<c:if test="${userInfo==null}">
								<b><transys:label code="Guest!" /></b>
							</c:if> 
							<c:if test="${userInfo!=null}">
								<b>${userInfo.name}!</b>
								<br />
								<span style="font-size: 10px">
									<transys:label code="Last Login" /> : 
									<fmt:formatDate value="${userInfo.lastLoginDate}" pattern="MM-dd-yyyy HH:mm:ss" />
								</span>
							</c:if>
						</a>
					</li>
					<li>
						<c:if test="${sessionScope.userInfo != null}">
							<a href="<c:url value="/logout.do" />" >Logout</a>
						</c:if>
					</li>
				</ul>
			</div>
		</div>
	</nav>
</body>
<script type="text/javascript">
	$(function() {
		var loc = window.location.href;
		var activePage = "";
		if (loc.match('/order/')) {
			activePage = "orderPage";
		} else if (loc.match('/permit/')) {
			activePage = "permitPage";
		} else if (loc.match('/customer/')) {
			activePage = "customerPage";
		} else if (loc.match('/invoice/')) {
			activePage = "invoicePage";
		} else if (loc.match('/orderScheduler/')) {
			activePage = "orderSchedulerPage";
		} else if (loc.match('/reports/')) {
			activePage = "reportPage";
		} else if (loc.match('/masterData/')) {
			activePage = "masterDataPage";
		} /*else if (loc.match('/migration/')) {
			activePage = "migrationPage";
		} */
		$('#' + activePage).addClass("active");
	});
</script>
</html>
