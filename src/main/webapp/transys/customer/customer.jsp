<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Customers</title>
</head>
<body>
	<ul class="nav nav-tabs" id="customer_tabs">
		<li><a href="#manageCustomer" data-toggle="tab">Manage Customer</a></li>
		<li><a href="#customerReports" data-toggle="tab">Customer Reports</a></li>
	</ul>

	<div class="tab-content tab-color">
		<div id="manageCustomer" class="tab-pane">
			<%@include file="list.jsp"%>
		</div>
		<div id="customerReports" class="tab-pane">
			<%@include file="form.jsp"%>
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
</script>

</body>
</html>
