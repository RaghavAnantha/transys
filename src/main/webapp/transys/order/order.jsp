<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Orders</title>
</head>
<body>
	<ul class="nav nav-tabs" id="order_main_tabs">
		<li><a href="#manageOrder" data-toggle="tab" >Orders</a></li>
		<li><a href="#orderReports" data-toggle="tab" >Order Reports</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;">
		<div id="manageOrder" class="tab-pane">
			<%@include file="list.jsp"%>
		</div>
		<div id="orderReports" class="tab-pane">
			<br/>
			<p>Placeholder for order reports</p>
		</div>
	</div>

	<div class="modal fade" id="editModal" role="dialog">
		<div class="modal-dialog" style="width:90% !important">
			<div class="modal-content">
				<div class="modal-header">		
					<h4 class="modal-title">Add/Edit Orders</h4>
					<div id="validations" style="color:red"></div>
				</div>
				<div class="modal-body"> 
					<ul class="nav nav-tabs" id="order_edit_tabs">
						<li><a href="#orderDetails" data-toggle="tab" >Order Details</a></li>
						<li><a href="#dropOff" data-toggle="tab" >Drop-Off</a></li>
						<li><a href="#pickup" data-toggle="tab" >Pickup</a></li>
						<li><a href="#notes" data-toggle="tab" >Notes</a></li>
					</ul>
					<div class="tab-content">
						<div id="orderDetails" class="tab-pane">
							<%@include file="form.jsp"%>
						</div>
						<div id="dropOff" class="tab-pane">
						</div>
						<div id="pickup" class="tab-pane">
						</div>
						<div id="notes" class="tab-pane">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
	
<script type="text/javascript">
	function showTab(tab){
		$('.nav-tabs a[href="#' + tab + '"]').tab('show');
	};
	
	showTab('${activeTab}');
	
	$('[data-toggle="tabajax"]').click(function(e) {
	    var $this = $(this),
	        loadurl = $this.attr('href'),
	        targ = $this.attr('data-target');
	
	    $.get(loadurl, function(data) {
	        $(targ).html(data);
	    });
	
	    $this.tab('show');
	    return false;
	});
	
	$("#editModal").on("show.bs.modal", function(e) {
	    var link = $(e.relatedTarget);
	    $(this).find("#orderDetails").load(link.attr("href"));
	    $('.nav-tabs a[href="#orderDetails"]').tab('show');
	});	
</script>

</body>
</html>
