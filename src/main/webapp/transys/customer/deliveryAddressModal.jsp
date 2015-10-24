<%@include file="/common/taglibs.jsp"%>

<form:form action="/customer/saveDeliveryAddressModal.do" name="deliveryAddressModalForm" commandName="deliveryAddressModelObject" method="post" id="deliveryAddressModalForm">
	<form:hidden path="customer.id" id="customerId" />
	<table id="form-table" class="table delivery">
		<tr>
			<td class="form-left"><transys:label code="Customer ID" /></td>
			<td class="td-static">${deliveryAddressModelObject.customer.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address #" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line1" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Street" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line2" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" cssClass="flat flat-ext" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" onchange="checkVal(this.id)">
					<form:option value="">----Please Select----</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat flat-ext"  onkeyup="checkValDrop(this.id, this.value)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validateForm()" value="Save" class="flat btn btn-primary btn-sm btn-sm-ext" /> 
				<input type="button" value="Close" class="flat btn btn-primary btn-sm btn-sm-ext" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">
function checkVal(id) {
	$("table.delivery").find('#'+id).removeClass("border");
}

function checkValDrop(id, val) {
	if (val != "") {
		$("table.delivery").find('#'+id).removeClass("border");
	}
}

function validateForm() {
	var ids = ["line1", "line2", "city", "zipcode", "state"];
	var bool = false	
	for (var i = 0; i < ids.length; i++) {		
	if ($("table.delivery").find('#'+ids[i]).val().length == 0 ) {	
		
		$("table.delivery").find('#'+ids[i]).addClass("border");
		bool = true;
	}
				
	} if (bool){
		return false;
	}
	
	return true;
};

$("#deliveryAddressModalForm").submit(function (ev) {
	var $this = $(this);
	
    $.ajax({
        type: $this.attr('method'),
        url: $this.attr('action'),
        data: $this.serialize(),
        success: function(responseData, textStatus, jqXHR) {
        	if (responseData.indexOf("ErrorMsg") >= 0 ) {
        		displayPopupDialogErrorMessage(responseData);
        	} else {
        		var address = jQuery.parseJSON(responseData);
        		
        		displayPopupDialogSuccessMessage("Delivery address saved successfully");
        		appendDeliveryAddress(address);
        	}
        }
    });
    
    ev.preventDefault();
});
</script>