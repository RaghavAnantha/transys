<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Permit</title>
</head>
<body>
	<ul class="nav nav-tabs" id="permit_main_tabs">
		<li><a href="#managePermits" data-toggle="tab">Permits</a></li>
		<li><a href="/orderPermitAlert/main.do" id="orderPermitAlertTab" data-toggle="tabajax" data-target="#orderPermitsAlert">Order Permits Alert</a></li>
		<li><a href="#permitsReport" data-toggle="tab">Permits Report</a></li>
	</ul>

	<div class="tab-content" style="background-color: white;">
		<div id="managePermits" class="tab-pane">
			<c:if test="${mode == 'MANAGE'}">
				<%@include file="list.jsp"%>
			</c:if>
			<c:if test="${mode == 'ADD'}">
				<%@include file="addEdit.jsp"%>
			</c:if>
		</div>
		<div id="orderPermitsAlert" class="tab-pane"></div>	
		<div id="permitsReport" class="tab-pane">
			<p>Placeholder for permit reports</p>
		</div>
	</div>
	
<script type="text/javascript">
	function showTab(tab){
		$('.nav-tabs a[href="#' + tab + '"]').tab('show');
	};
	
	showTab('${activeTab}');
	showTab('${activeSubTab}');
	
	$('#orderPermitAlertTab').click(function(e) {
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
