<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<ul class="nav nav-tabs" id="brands_tabs">
		<li><a href="#orders" data-toggle="tab">Orders</a></li>
		<li><a href="#orderReports" data-toggle="tab">Orders Report</a></li>
	</ul>

	<div class="tab-content">
		<div id="orders" class="tab-pane">
			<h3 style="margin-top: 0px !important">Add/Edit Orders</h3>
			<ul class="nav nav-tabs" id="orderTabs">
				<li><a href="#orderDetails" data-toggle="tab">Order Details</a></li>
				<li><a href="#dropOff" data-toggle="tab">Drop-Off</a></li>
				<li><a href="#" data-toggle="tab">Pickup</a></li>
				<li><a href="#" data-toggle="tab">Notes</a></li>
			</ul>
		</div>
		<div id="orderReports" class="tab-pane">
			<i>brands_unique interests go here</i>
		</div>
	</div>



	<div class="tab-content">
		<div id="orderDetails" class="tab-pane">
			<table>
				<tr>
					<td>Order#</td>
					<td><input type="text" class="form-control" id="usr">
					</td>
				</tr>
				<tr>
					<td>Customer</td>
					<td><select class="form-control">
							<option>awqq</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
					<td>
						<Button type="button" class="btn btn-primary">Add New
							Customer</button>
					</td>
				</tr>



			</table>

		</div>
		<div id="dropOff" class="tab-pane">
			<i>brands_unique interests go here</i>
		</div>
	</div>




</body>
</html>
