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
		<li><a href="invoicePaymentMain.do" id="invoicePaymentTab" data-toggle="tabajax" data-target="#invoicePayment">Invoice Payment</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;">
		<div id="createInvoice" class="tab-pane">
			<div id="createInvoiceLoadingImgDiv">${loadingMsg}</div>
			<%@include file="createInvoiceList.jsp"%>
		</div>
		<div id="manageInvoice" class="tab-pane">${loadingMsg}</div>
		<div id="invoicePayment" class="tab-pane">${loadingMsg}</div>	
	</div>
	
<script type="text/javascript">
	showTab('${activeTab}');
	showTab('${activeSubTab}');
	
	$('#manageInvoiceTab').click(function(e) {
		loadTab($(this));
		return false;
	});
	$('#invoicePaymentTab').click(function(e) {
		loadTab($(this));
		return false;
	});
	
	$(window).load(function() {
		$("#createInvoiceLoadingImgDiv").hide();
	});
</script>
</body>
</html>
