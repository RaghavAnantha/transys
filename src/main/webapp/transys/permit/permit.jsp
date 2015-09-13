<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Permit</title>
</head>
<body>
	<ul class="nav nav-tabs" id="permit_tabs">
		<li><a href="#managePermit" data-toggle="tab">Permits</a></li>
		<li><a href="#orderPermitAlert" data-toggle="tab">Order Permits Alert</a></li>
		<li><a href="#permitsReport" data-toggle="tab">Permits Report</a></li>
	</ul>

	<div class="tab-content tab-color">
		<div id="managePermit" class="tab-pane">
			<%@include file="list.jsp"%>	
		</div>
 		 <div id="orderPermitAlert" class="tab-pane">
			<%@include file="list.jsp"%>
		</div> 
		<div id="permitsReport" class="tab-pane">
			<%@include file="form.jsp"%>
		</div>
	</div>
	
	<div class="modal fade" id="editModal" role="dialog">
				<div class="modal-dialog" style="width:90% !important">
					<div class="modal-content">
						<div class="modal-header">		
							<h4 class="modal-title">Add Permit</h4>
						</div>
						<div class="modal-body"> 
						</div>
						<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
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
	
	$("#editModal").on("show.bs.modal", function(e) {
	    var link = $(e.relatedTarget);
	    $(this).find(".modal-body").load(link.attr("href"));
	    
	});	
	
/* 	$("#myModal").on('hidden.bs.modal', function () {
		var url = "/permit/main.do"
		window.location.href = url;
    });
	 */
</script>

</body>
</html>
