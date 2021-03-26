<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Invoices</title>
</head>
<body>
	<ul class="nav nav-tabs" id="invoice_main_tabs">
		<li><a href="#manageInvoice" id="manageInvoiceTab" data-toggle="tab" >Invoice</a></li>
		<li><a href="invoicePaymentMain.do" id="invoicePaymentTab" data-toggle="tabajax" data-target="#invoicePayment">Invoice Payment</a></li>
	</ul>
	<div class="tab-content" style="background-color: white;">
		<div id="manageInvoice" class="tab-pane">
			<div id="manageInvoiceLoadingImgDiv">${loadingMsg}</div>
			<c:if test="${mode == 'MANAGE'}">
				<%@include file="manageInvoiceList.jsp"%>
			</c:if>
			<c:if test="${mode == 'ADD'}">
				<%@include file="createInvoiceList.jsp"%>
			</c:if>
		</div>
		<div id="invoicePayment" class="tab-pane">${loadingMsg}</div>	
	</div>
	
<script type="text/javascript">
	showTab('${activeTab}');
	showTab('${activeSubTab}');
	
	$('#invoicePaymentTab').click(function(e) {
		loadTab($(this));
		return false;
	});
	
	$(window).load(function() {
		$("#manageInvoiceLoadingImgDiv").hide();
	});
</script>
</body>
</html>
