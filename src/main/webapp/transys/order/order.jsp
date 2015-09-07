<%@include file="/common/taglibs.jsp"%>
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

	<div class="tab-content tab-color">
		<div id="orders" class="tab-pane">
			<h3 style="margin-top: 0px !important">Manage Orders</h3>
			<form>
				<table>
					<tr>
						<td><transys:label code="Order#: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
						<td><transys:label code="Order Status: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
					</tr>
					<tr>
						<td><transys:label code="Customer: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
						<td><transys:label code="Delivery Address: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>

					</tr>
					<tr>
						<td><transys:label code="Phone Number: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>

					</tr>
					<tr>
						<td><transys:label code="Dumpster Size: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
						<td><transys:label code="Dumpster#: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>

					</tr>
					<tr>
						<td><transys:label code="Delivery Date From: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
						<td><transys:label code="Delivery Date To: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>

					</tr>
					<tr>
						<td><transys:label code="Pickup Date From: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>
						<td><transys:label code="Pickup Date To: " /></td>
						<td><select class="form-control">
								<option>awqq</option>
								<option>adajnwj</option>
								<option>najbwja</option>
								<option>nawja</option>
						</select></td>

					</tr>
					<tr>
						<td></td>
						<td><Button type="button" class="btn btn-primary">Search</button></td>
					</tr>
					<tr>
						<td><Button type="button" class="btn btn-primary">Add
								New Order</button></td>
					</tr>
				</table>
			</form>
<div class="container-fluid">
			<table class="table table-striped">
					<tr>
						<th>Order#</th>
						<th>Customer</th>
						<th>Contact</th>
						<th>Phone</th>
						<th>Delivery Address</th>
						<th>City</th>
						<th>Dumpster Size</th>
						<th>Dumpster#</th>
						<th>Delivery Date</th>
						<th>Pickup Date</th>
						<th>Dumpster Price</th>
						<th>Permit Fee</th>
						<th>City Fee</th>
						<th>Overwt. Fee</th>
						<th>Addintl Fee</th>
						<th>Total Amount</th>
						<th>Status</th>
						<th>E</th>
						<th>P</th>
					</tr>		
				</table>
</div>


		</div>
		<div id="orderReports" class="tab-pane">

			<h3 style="margin-top: 0px !important">Orders Report</h3>
			<table>
				<tr>
					<td>Company Name:</td>
					<td><select class="form-control">
							<option>awqq</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
				</tr>
				<tr>
					<td>Order Date From:</td>
					<td><select class="form-control">
							<option>awqq</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
					<td>Order Date To:</td>
					<td><select class="form-control">
							<option>awqq</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
				</tr>
				<tr>
					<td>Contact Name:</td>
					<td><select class="form-control">
							<option>awqqa</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
					<td>Order Status:</td>
					<td><select class="form-control">
							<option>Open</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select>
				</tr>
				<tr>
					<td>Phone Number:</td>
					<td><select class="form-control">
							<option>awqq</option>
							<option>adajnwj</option>
							<option>najbwja</option>
							<option>nawja</option>
					</select></td>
				</tr>
				<tr>
					<td></td>
					<td><Button type="button" class="btn btn-primary">Preview</button>
					</td>
				</tr>
			</table>
		</div>








	</div>






</body>
</html>
