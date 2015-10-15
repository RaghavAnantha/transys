<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Master Data</title>
</head>
<body>
<div class="tab-content" style="background-color: white;">
	<div id="employees" class="tab-pane">
		<c:if test="${mode == 'MANAGE'}">
			<%@include file="list.jsp"%>
		</c:if>
		<c:if test="${mode == 'ADD'}">
			<%@include file="form.jsp"%>
		</c:if>
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

</script>

</body>
</html>