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
			<div id="manageCustomerLoadingImgDiv">${loadingMsg}</div>
			<c:if test="${mode == 'MANAGE'}">
				<%@include file="list.jsp"%>
			</c:if>
			<c:if test="${mode == 'ADD'}">
				<%@include file="addEdit.jsp"%>
			</c:if>
		</div>
		<div id="customerReports" class="tab-pane" style="background-color: white;padding-top: 5px;">
			<ul class="nav nav-tabs" id="customer_reports_tabs">
				<li><a href="${ctx}/customer/customerListReportMain.do" id="customerListReportTab" data-toggle="tabajax" data-target="#customerListReport">Customer List Report</a></li>
				<li><a href="#customerOrdersReport" data-toggle="tab">Customer Orders Report</a></li>
			</ul>
			<div class="tab-content" style="background-color: white;padding-top: 5px;">
				<div id="customerListReport" class="tab-pane"></div>
				<div id="customerOrdersReport" class="tab-pane">
					<%@include file="customerOrdersReport.jsp"%>
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
		var error = false;
		var ids = ["companyName", "billingAddressLine1", "city"];
		for (var i= 0; i < ids.length; i++) {	
			if ($("#typeForm").find('input[id="'+ids[i] +'"]').val().length == 0 ) {
				$("#typeForm").find('input[id="'+ids[i] +'"]').addClass("border");
				error = true;
			}		
		} 
		
		if (error) {
			//$("#validations").html("Please fill out the required fields Name, Address Line 1 and City");	
			return false;
		} else {
			return true;
		}
	};
	
	$('#customerListReportTab').click(function(e) {
	    var $this = $(this),
	        loadurl = $this.attr('href'),
	        targ = $this.attr('data-target');
	
	    $.get(loadurl, function(data) {
	        $(targ).html(data);
	    });
	
	    $this.tab('show');
	    return false;
	});
	
	$(window).load(function() {
		$("#manageCustomerLoadingImgDiv").hide();
	});
</script>
</body>
</html>
