<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Customers</title>
</head>
<body>
	<ul class="nav nav-tabs" id="customer_tabs">
		<li><a href="#manageCustomer" data-toggle="tab" >Customers</a></li>
		<li><a href="#customerReports" data-toggle="tab" >Customer Reports</a></li>
	</ul>
	<div class="tab-content tab-color">
		<div id="manageCustomer" class="tab-pane">
			<%@include file="list.jsp"%>
		</div>
		<div id="customerReports" class="tab-pane">
			<br/>
			<ul class="nav nav-tabs" id="customer_tabs">
				<li><a href="#customerList" data-toggle="tab" class="active">Customer List</a></li>
				<li><a href="#customerOrderReports" data-toggle="tab">Customer Order Reports</a></li>
			</ul>
		</div>
	</div>
	
	<div class="tab-content tab-color">
	<div id="customerList" class="tab-pane">
			<p>PlaceHolder for customer List</p>
	</div>
	<div id="customerOrderReports" class="tab-pane">
			<p>PlaceHolder for customer Order Reports</p>
	</div>
	
	</div>
	
	
	<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog" style="width:90% !important">
					<div class="modal-content">
						<div class="modal-header">		
							<h4 class="modal-title">Add/Edit Customers</h4>
							<div id="validations" style="color:red"></div>
						</div>
						<div class="modal-body" style="background-color: #EAEAF3;"> 
						
						</div>
					</div>
				</div>
	</div>	
	
	

<script type="text/javascript">
	function showTab(tab){
		    $('.nav-tabs a[href="#' + tab + '"]').tab('show');		    
	};
	
	//showTab('manageCustomer');
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
	
	$("#myModal").on("show.bs.modal", function(e) {
	    var link = $(e.relatedTarget);
	    $(this).find(".modal-body").load(link.attr("href"));
	    
	});	
	
	
	
	function validations() {
		
		if ($("#typeForm").find('input[id="companyName"]').val().length == 0 || 
				$("#typeForm").find('input[id="billingAddressLine1"]').val().length ==0 ||
				$("#typeForm").find('input[id="city"]').val().length ==0
				
			) {	
			$("#validations").html("Please fill out the required fields Name, Address Line 1 and City");
			return false;		
		}
		
		return true;
	};
	
	
</script>

</body>
</html>
