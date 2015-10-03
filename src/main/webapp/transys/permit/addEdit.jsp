<%@include file="/common/taglibs.jsp"%>
<h4>Add/Edit Permits</h4>
<ul class="nav nav-tabs" id="permit_edit_tabs">
	<li><a href="#permitDetails" data-toggle="tab">Permit Details</a></li>
	<li><a href="#permitAddress" data-toggle="tab">Permit Address</a></li>
	<li><a href="#permitNotes" data-toggle="tab">Permit Notes</a></li>
</ul>
<div class="tab-content" style="background-color: white;">
	<div id="permitDetails" class="tab-pane">
		<%@include file="form.jsp"%>
	</div>
	<div id="permitAddress" class="tab-pane">
		<%@include file="permitAddress.jsp"%>
	</div>
	<div id="permitNotes" class="tab-pane">
		<%@include file="notes.jsp"%>
	</div>
</div>