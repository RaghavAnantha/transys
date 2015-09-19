<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top navheight"
		role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand active" href="/home.do">Transys</a>
		</div>
		<div>
			<ul class="nav navbar-nav">
				<li id="orderPage"><a href="/order/main.do">Orders</a></li>
				<li id="permitPage"><a href="/permit/main.do">Permits</a></li>
				<li id="customerPage"><a href="/customer/main.do">Customers</a></li>
				<li id="reportPage"><a href="/report/main.do">Reports</a></li>
				<li id="masterDataPage"><a href="/masterdata/main.do">Master Data</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><transys:label code="Welcome" /> <c:if
							test="${userInfo==null}">
							<b><transys:label code="Guest!" /></b>
						</c:if> <c:if test="${userInfo!=null}">
							<b>${userInfo.name}!</b>
							<br />
							<span style="font-size: 10px"><transys:label
									code="Last Login" /> : <fmt:formatDate
									value="${userInfo.lastLoginDate}" pattern="MM-dd-yyyy HH:mm:ss" /></span>
						</c:if></a></li>
				<li><c:if test="${sessionScope.userInfo !=null}">
						<a href="${ctx}/j_spring_security_logout">Logout</a>
					</c:if></li>
			</ul>
		</div>
	</div>
	</nav>
</body>
<script type="text/javascript">
	$(function() {
		var loc = window.location.href;
		if (loc.match('/order')) {
			$('#orderPage').addClass("active");
		} else if (loc.match('/permit')) {
			$('#permitPage').addClass("active");
		} else if (loc.match('/customer')) {
			$('#customerPage').addClass("active");
		} else if (loc.match('/reports')) {
			$('#reportPage').addClass("active");
		} else if (loc.match('/master')) {
			$('#masterDataPage').addClass("active");
		}
	});
</script>
</html>
