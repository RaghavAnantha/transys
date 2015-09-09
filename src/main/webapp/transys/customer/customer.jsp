<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		function formatPhone(){
			var phone = document.getElementById("phone").value;
			if(phone != ""){
				if(phone.length < 10){
					alert("Invalid Phone Number");
					document.getElementById("phone").value = "";
					return true;
				}
				else{
					var str = new String(phone);
					if(!str.match("-")){
						var p1 = str.substring(0,3);
						var p2 = str.substring(3,6);
						var p3 = str.substring(6,10);				
						var phone = p1 + "-" + p2 + "-" + p3;
						document.getElementById("phone").value = phone;
					}
				}
			}	
		}
		
		function formatFax(){
			var fax = document.getElementById("fax").value;
			if(fax != ""){
				if(fax.length < 10){
					alert("Invalid Phone Number");
					document.getElementById("fax").value = "";
					return true;
				}
				else{
					var str = new String(fax);
					if(!str.match("-")){
						var p1 = str.substring(0,3);
						var p2 = str.substring(3,6);
						var p3 = str.substring(6,10);				
						var fax = p1 + "-" + p2 + "-" + p3;
						document.getElementById("fax").value = fax;
					}
				}
			}	
		}
	</script>
	<ul class="nav nav-tabs" id="brands_tabs">
		<li><a href="/transysapp/customer" data-target="#orders" data-toggle="tabajax">Manage Customer</a></li>
      
		<li><a href="#orderReports" data-toggle="tab">Add Customer</a></li>
	</ul>

	<div class="tab-content tab-color">
		<div id="orders" class="tab-pane">
			
		</div>
		
		
		
		<div id="orderReports" class="tab-pane">

			<form:form action="/transysapp/customer" name="typeForm" commandName="modelObject" method="post">
			<form:hidden path="id" id="id" />
			<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
				<tr class="table-heading">
					<td align="${left}" colspan="4"><b><transys:label code="Add/Update Customers"/></b></td>
				</tr>
				<tr>
					<td class="form-left"><transys:label code="Name" /><span class="errorMessage">*</span></td>
					<td align="${left}">
						<form:input path="companyName" cssClass="flat" style="min-width:350px; max-width:350px"  />
					 	<br><form:errors path="companyName" cssClass="errorMessage" />
					</td>
					
					<td class="form-left"><transys:label code="Customer ID" /></td>
					<td align="${left}">
						<form:input path="id" cssClass="flat" style="min-width:200px; max-width:200px" />
					 	
					</td>
				</tr>
				<tr>
					<td class="form-left"><transys:label code="Address Line1"/><span	class="errorMessage"></span></td>
					<td align="${left}">
						<form:input path="billingAddressLine1" cssClass="flat" style="min-width:200px; max-width:200px"/>
						 <br><form:errors path="billingAddressLine1" cssClass="errorMessage" />
					</td>
					<td class="form-left"><transys:label code="Address Line2"/></td>
					<td align="${left}">
						<form:input path="billingAddressLine2" cssClass="flat" style="min-width:200px; max-width:200px"/>
						 <br><form:errors path="billingAddressLine2" cssClass="errorMessage" />
					</td>
				</tr>
				<tr>
					<td class="form-left"><transys:label code="City" /><span class="errorMessage"></span></td>
					<td align="${left}">
						<form:input cssClass="flat" path="city" style="min-width:200px; max-width:200px"/>
						<br><form:errors path="city" cssClass="errorMessage" />
					</td>
					<td class="form-left"><transys:label code="Zipcode" /><span class="errorMessage"></span></td>
					<td align="${left}">
						<form:input path="zipcode" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="5"
							 onkeypress="return onlyNumbers(event, false)"/>
					 	<br><form:errors path="zipcode" cssClass="errorMessage" />
					</td>
				</tr>
				<tr>
					<td class="form-left"><transys:label code="State" /><span	class="errorMessage"></span></td>
					<td align="${left}">
						<form:select cssClass="flat" path="state" style="min-width:204px; max-width:204px">
							<form:option value="">-----------Please Select----------</form:option>
							<form:options items="${state}" itemValue="id" itemLabel="name" />
						</form:select> 
						<br><form:errors path="state" cssClass="errorMessage" />
					</td>
				</tr>		
				<tr>
					<td class="form-left"><transys:label code="Phone" /></td>
					<td align="${left}">
						<form:input path="phone" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="12" 
							id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
					 	<br><form:errors path="phone" cssClass="errorMessage" />
					</td>
					<td class="form-left"><transys:label code="Fax" /></td>
					<td align="${left}">
						<form:input path="fax" cssClass="flat"	style="min-width:200px; max-width:200px" maxlength="12" 
							id="fax" onkeypress="return onlyNumbers(event, false)" onblur="return formatFax();"/>
						 <br><form:errors path="fax" cssClass="errorMessage" />
					</td>
				</tr>
			   
		
				<tr><td colspan="2"></td></tr>
				<tr>
					<td>&nbsp;</td>
					<td align="${left}" colspan="2"><input type="submit"
						name="create" id="create" onclick=""
						value="<transys:label code="Save"/>" class="flat" /> <input
						type="reset" id="resetBtn" value="<transys:label code="Reset"/>"
						class="flat" /> <input type="button" id="cancelBtn"
						value="<transys:label code="Cancel"/>" class="flat"
						onClick="location.href='list.do'" /></td>
				</tr>
			</table>
		</form:form>
			
		</div>








	</div>



<script type="text/javascript">

function activaTab(tab){
	    $('.nav-tabs a[href="' + tab + '"]').tab('show');
};

activaTab('/transysapp/customer');

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
