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
			<ul class="nav nav-tabs" id="customer_reports_tabs">
				<li><a href="#customerListReport" data-toggle="tab" class="active">Customer List Report</a></li>
				<li><a href="#customerOrderReport" data-toggle="tab">Customer Order Report</a></li>
			</ul>

			<div class="tab-content tab-color">
				<div id="customerListReport" class="tab-pane">
					<%@include file="customerListReport.jsp"%>
				</div>
				<div id="customerOrderReport" class="tab-pane">
					<p>Placeholder for Customer Order Report</p>
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
	
	function validate() {
		var ids = ["companyName", "billingAddressLine1", "city"];
		var bool = false
		
		for (var i= 0; i<ids.length; i++) {	
			if ($("#typeForm").find('input[id="'+ids[i] +'"]').val().length == 0 ) {	
				
				$("#typeForm").find('input[id="'+ids[i] +'"]').addClass("border");
				bool = true;
			}		
		} 
		//$("#validations").html("Please fill out the required fields Name, Address Line 1 and City");	
		if (bool){
			return false;
		}
		
		return true;
	};
</script>
</body>
</html>
