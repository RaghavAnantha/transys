<%@include file="/common/taglibs.jsp"%>

<form:form action="/customer/saveDeliveryAddressModal.do" name="deliveryAddressModalForm" commandName="deliveryAddressModelObject" method="post" id="deliveryAddressModalForm">
	<form:hidden path="customer.id" id="custID" />
	<table id="form-table" class="table delivery">
		<tr>
			<td class="form-left"><transys:label code="Customer ID" /><span class="errorMessage">*</span></td>
			<td align="${left}">${deliveryAddressModelObject.customer.id}</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Address #" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line1" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Delivery Street" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="line2" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="line2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" cssClass="flat" onkeyup="checkVal(this.id)"/>
			 	<br><form:errors path="city" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="State" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat form-control input-sm" style="width: 174px !important" path="state" onchange="checkVal(this.id)">
					<form:option value="">------Please Select--------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat"  onkeyup="checkValDrop(this.id, this.value)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" id="create" onclick="return validateForm()" value="Save" class="flat btn btn-primary btn-sm" /> 
				<input type="reset" id="resetBtn" value="Reset" class="flat btn btn-primary btn-sm" /> 
				<input type="button" value="Close" class="flat btn btn-primary btn-sm" data-dismiss="modal" />
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">
$("tr a").click(function() {	
	var ids = ["line1", "line2", "city", "zipcode", "state"];
	
	for (var i= 0; i<ids.length; i++) {		
		$("table.delivery").find('#'+ids[i]).removeClass("border");	
	}
	
    var tableData = $(this).parent().parent().children("td").map(function() {
        return $(this).text();
    }).get();
    
    $('#line1').val($.trim(tableData[0]));
    $('#line2').val($.trim(tableData[1])); 
    $('table.delivery').find('#city').val($.trim(tableData[2]));
    $('table.delivery').find('#zipcode').val($.trim(tableData[4]));
	$("table.delivery select option").filter(function() {
	    return this.text == $.trim(tableData[3]); 
	}).attr('selected', true);
});

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
        	var address = jQuery.parseJSON(responseData);
        	appendDeliveryAddress(address);
        }
    });
    
    ev.preventDefault();
});
</script>