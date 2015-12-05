<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>Transys</title>
<%@include file="/common/css.jsp"%>
<%@include file="/common/scripts.jsp"%>
<%@include file="/common/modal.jsp"%>
<decorator:head/>
</head>
<body dir="${dir}">
<table class="table">	
	<tr>
		<td height="55px">
			<jsp:include page="topMenu.jsp" />
		</td>
	</tr>
	<tr>
		<td valign="top">
			<decorator:body />
		</td>
	</tr>
	<tr>
		<td></td>
		<td class="footer navbar-fixed-bottom"><jsp:include page="footer.jsp" /></td>
	</tr>
</table>
</body>
</html>
