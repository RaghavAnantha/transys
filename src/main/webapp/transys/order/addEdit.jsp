<%@include file="/common/taglibs.jsp"%>
<br />
<h5 style="margin-top: -15px; !important">Add/Edit Orders</h5>
<ul class="nav nav-tabs" id="order_edit_tabs">
	<li><a href="#orderDetails" data-toggle="tab">Order Details</a></li>
	<li><a href="#dropOffDriver" data-toggle="tab">Drop Off</a></li>
	<li><a href="#pickupDriver" data-toggle="tab">Pickup</a></li>
	<li><a href="#orderNotes" data-toggle="tab">Notes</a></li>
	<li><a href="#manageDocs" data-toggle="tab">Manage Docs</a></li>
</ul>
<div class="tab-content" style="background-color: white;">
	<div id="orderDetails" class="tab-pane">
		<%@include file="form.jsp"%>
	</div>
	<div id="dropOffDriver" class="tab-pane">
		<%@include file="dropOffDriver.jsp"%>
	</div>
	<div id="pickupDriver" class="tab-pane">
		<%@include file="pickupDriver.jsp"%>
	</div>
	<div id="orderNotes" class="tab-pane">
		<%@include file="notes.jsp"%>
	</div>
	<div id="manageDocs" class="tab-pane">
		<%@include file="manageDocs.jsp"%>
	</div>
</div>