<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function showAlertDialog(title, msg) {
	$('#alertDialogTitle').html(title);
	$('#alertDialogBody').html(msg);
	$("#alertDialog").modal('show');
}

function showConfirmDialog(title, msg) {
	$('#confirmDialogTitle').html(title);
	$('#confirmDialogBody').html(msg);
	$("#confirmDialog").modal('show');
}

function showPopupDialog(title, url) {
	$("#popupDialog").modal('show');
	$("#popupDialogTitle").html(title);
	
	$("#popupDialogBody").html("");
	$("#popupDialogBody").load(url);
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

<div class="modal fade" id="confirmDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:50% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="confirmDialogHeader">
		 		<h4 class="modal-title" id="confirmDialogTitle">Confirm</h4>
		 	</div>	
			<div class="modal-body" id="confirmDialogBody"></div>
			<div class="modal-footer" id="confirmDialogFooter">
			   <button type="button" data-dismiss="modal" class="btn btn-primary" id="confirmDialogYes">Yes</button>
			   <button type="button" data-dismiss="modal" class="btn" id="confirmDialogNo">No</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="popupDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:90% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="popupDialogHeader">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title" id="popupDialogTitle">Title</h4>
       			<div id="popupDialogValidations" style="color:red"></div>
      		 </div>	
			
			<div class="modal-body" id="popupDialogBody"></div>
		</div>
	</div>
</div>
