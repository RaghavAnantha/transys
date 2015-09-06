<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
<div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/transysapp/home">Transysy</a>
    </div>
    <div>
      <ul class="nav navbar-nav">
        <li class="active"><a href="/transysapp/order">Orders</a></li>
        <li><a href="#">Permits</a></li> 
        <li ><a href="/transysapp/customer">Customers</a></li> 
        <li><a href="#">Reports</a></li>
        <li><a href="#">Master Data</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><transys:label
						code="Welcome" /> <c:if test="${sessionScope.userInfo==null}">
						<b><transys:label code="Bharat!" /></b>
					</c:if> <c:if test="${sessionScope.userInfo!=null}">
						<b>${sessionScope.userInfo.firstName}
							${sessionScope.userInfo.lastName}!</b>
						<br />
						<span style="font-size: 10px"><transys:label
								code="Last Login" /> : <fmt:formatDate
								value="${sessionScope.userInfo.lastLoginDate}"
								pattern="MM-dd-yyyy HH:mm:ss" /></span>
					</c:if></a></li>			
        <li><c:if test="${sessionScope.userInfo !=null}"><a href="${ctx}/j_spring_security_logout">Logout</a>
									</c:if></li>
      </ul>
    </div>
  </div>
  </nav>
</body>
</html>