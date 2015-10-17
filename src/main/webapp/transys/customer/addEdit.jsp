<%@include file="/common/taglibs.jsp"%>

<br />
<h5 style="margin-top: -15px; !important">Add/Edit Customers</h5>
<ul class="nav nav-tabs" id="customer_edit_tabs">
	<li><a href="#billing" data-toggle="tab">Billing Info</a></li>
	<li><a href="#delivery" data-toggle="tab">Delivery Address</a></li>
</ul>
<div class="tab-content" style="background-color: white;">
	<div id="billing" class="tab-pane">
		<%@include file="form.jsp"%>
	</div>
	<div id="delivery" class="tab-pane">
		<%@include file="deliveryAddress.jsp"%>
	</div>
</div>