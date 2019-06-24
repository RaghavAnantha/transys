<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Invoices</title>
</head>
<body>
	<ul class="nav nav-tabs" id="invoice_main_tabs">
		<li><a href="#createInvoice" id="createInvoiceTab" data-toggle="tab" >Create Invoice</a></li>
		<li><a href="manageInvoiceMain.do" id="manageInvoiceTab" data-toggle="tabajax" data-target="#manageInvoice">Manage Invoice</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;">
		<div id="createInvoice" class="tab-pane">
			<%@include file="createInvoiceList.jsp"%>
		</div>
		<div id="manageInvoice" class="tab-pane">
			Loading...<img src="${ctx}/images/preloader.gif" id="loadingImage"/>
		</div>	
	</div>
	
	<script type="text/javascript">
		showTab('${activeTab}');
		showTab('${activeSubTab}');
		
		
		$('#manageInvoiceTab').click(function(e) {
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