<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function showAlert(title, msg) {
	$('#alertDialogTitle').html(title);
	$('#alertDialogBody').html(msg);
	$("#alertDialog").modal('show');
}
</script>

<div class="modal fade" id="alertDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:30% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="alertDialogHeader">
		 		<h4 class="modal-title" id="alertDialogTitle">Alert</h4>
		 	</div>	
			<div class="modal-body" id="alertDialogBody"></div>
			<div class="modal-footer" id="alertDialogFooter">
			   <button type="button" data-dismiss="modal" class="btn btn-primary" id="alertDialogOk">OK</button>
			</div>
		</div>
	</div>
</div>
