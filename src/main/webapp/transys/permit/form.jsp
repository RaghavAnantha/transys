<%@include file="/common/taglibs.jsp"%>
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
<br/>
<form:form action="save.do" name="typeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><transys:label code="Add/Edit Permit"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Permit Number" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="number" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="number" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Class" /></td>
			<td align="${left}">
				<form:input path="class" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="class" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><transys:label code="Status" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="status" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="status" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><transys:label code="Permit Class" /></td>
			<td align="${left}">
				<form:input path="class" cssClass="flat" style="min-width:350px; max-width:350px"  />
			 	<br><form:errors path="class" cssClass="errorMessage" />
			</td>
		</tr>
	   
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit"  id="create" onclick="return true" value="<transys:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<transys:label code="Reset"/> "class="flat" /> 
				<input type="button" id="cancelBtn" value="<transys:label code="Cancel"/>" class="flat" onClick="location.href='main.do'" />
			</td>
		</tr>
	</table>
</form:form>
	
