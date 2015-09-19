<%@include file="/common/taglibs.jsp"%>
<h4>Add/Edit Orders</h4>
<ul class="nav nav-tabs" id="order_edit_tabs">
	<li><a href="#orderDetails" data-toggle="tab">Order Details</a></li>
	<li><a href="#dropOff" data-toggle="tab">Drop Off</a></li>
	<li><a href="#pickup" data-toggle="tab">Pickup</a></li>
	<li><a href="#orderNotesTab" data-toggle="tab">Notes</a></li>
</ul>
<div class="tab-content" style="background-color: white;">
	<div id="orderDetails" class="tab-pane">
		<%@include file="form.jsp"%>
	</div>
	<div id="dropOff" class="tab-pane">
	</div>
	<div id="pickup" class="tab-pane">
	</div>
	<div id="orderNotesTab" class="tab-pane">
		<%@include file="notes.jsp"%>
	</div>
</div>