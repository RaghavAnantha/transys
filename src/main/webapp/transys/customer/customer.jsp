<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Customers</title>
</head>
<body>
	<ul class="nav nav-tabs" id="customer_main_tabs">
		<li><a href="#manageCustomer" data-toggle="tab">Customers</a></li>
		<li><a href="#customerReports" data-toggle="tab">Customer Reports</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;padding-top: 5px;">
		<div id="manageCustomer" class="tab-pane">
			<c:if test="${mode == 'MANAGE'}">
				<%@include file="list.jsp"%>
			</c:if>
			<c:if test="${mode == 'ADD'}">
				<%@include file="addEdit.jsp"%>
			</c:if>
		</div>
		<div id="customerReports" class="tab-pane">
			<br />
			<ul class="nav nav-tabs" id="customer_reports_tabs">
				<li><a href="#customerList" data-toggle="tab" class="active">Customer List</a></li>
				<li><a href="#customerOrderReports" data-toggle="tab">Customer Order Reports</a></li>
			</ul>

			<div class="tab-content tab-color">
				<div id="customerList" class="tab-pane">
					<p>Placeholder for Customer List</p>
				</div>
				<div id="customerOrderReports" class="tab-pane">
					<p>Placeholder for Customer Order Reports</p>
				</div>

			</div>
		</div>
	</div>

	<div class="modal fade" id="editModal" role="dialog">
		<div class="modal-dialog" style="width: 90% !important">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">Add/Edit Customers</h4>
					<div id="validations" style="color: red"></div>
				</div>
				<div class="modal-body">
					<ul class="nav nav-tabs" id="customer_edit_tabs">
						<li><a href="#billingInfo" data-toggle="tab">Billing Info</a></li>
						<li><a href="/customer/address.do" data-toggle="tabajax"
							data-target="#deliveryAddress">Delivery Addresses</a></li>
					</ul>
					<div class="tab-content">
						<div id="billingInfo" class="tab-pane"></div>
						<div id="deliveryAddress" class="tab-pane"></div>

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
	showTab('${activeSubTab}');
	
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
	    $(this).find("#billingInfo").load(link.attr("href"));
	    $(this).find("#deliveryAddress").load(link.attr("href"));
	    $('.nav-tabs a[href="#billingInfo"]').tab('show');
	    
	});	
	
	function validate() {
		var ids = ["companyName", "billingAddressLine1", "city"];
		var bool = false
		
		for (var i= 0; i<ids.length; i++) {	
		
		if ($("#typeForm").find('input[id="'+ids[i] +'"]').val().length == 0 ) {	
			
			$("#typeForm").find('input[id="'+ids[i] +'"]').addClass("border");
			bool = true;
		}
		
			//$("#validations").html("Please fill out the required fields Name, Address Line 1 and City");			
		} if (bool){
			return false;
		}
		
		return true;
	};
</script>

</body>
</html>
