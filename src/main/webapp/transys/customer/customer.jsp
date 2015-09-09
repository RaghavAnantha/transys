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
		<li><a href="#orders" data-toggle="tab">Manage Customer</a></li>
		<li><a href="#orderReports" data-toggle="tab">Add Customer</a></li>
	</ul>

	<div class="tab-content tab-color">
		<div id="orders" class="tab-pane">
			<h3 style="margin-top: 0px !important">Manage Customer</h3>
			
			<form:form action="customer" method="get" name="searchForm">
				<table width="100%" id="form-table">
					<tr class="table-heading">
						<td colspan="4"><b><transys:label code="Search Customer" /></b></td>
					</tr>
					<tr>
						<%-- <td align="${left}" class="first"><transys:label code="First Name" /></td>
						<td align="${left}"><input name="firstName" type="text"
							value="${sessionScope.searchCriteria.searchMap.firstName}" /></td>
						<td align="${left}" class="first"><transys:label code="Last Name" /></td>
						<td align="${left}"><input name="lastName" type="text"
							value="${sessionScope.searchCriteria.searchMap.lastName}" /></td>
				 --%>	
				 <td align="${left}" class="first"><transys:label code="Name"/></td>
						<td align="${left}"><select id="companyName" name="companyName" style="min-width:350px; max-width:350px">
							<option value="">------<transys:label code="Please Select"/>------</option>
							<c:forEach items="${customer}" var="customer">
								<c:set var="selected" value=""/>
								<c:if test="${sessionScope.searchCriteria.searchMap['companyName'] == customer.companyName}">
									<c:set var="selected" value="selected"/>
								</c:if>
									<option value="${customer.companyName}" ${selected}>${customer.companyName}</option>
							</c:forEach>
						</select>
						</td>
						
						<td align="${left}" class="first"><transys:label code="Customer ID"/></td>
						<td align="${left}"><select id="customerId" name="id" style="min-width:200px; max-width:200px">
							<option value="">------<transys:label code="Please Select"/>------</option>
							<c:forEach items="${customerIds}" var="customerId">
								<c:set var="selected" value=""/>
								<c:if test="${sessionScope.searchCriteria.searchMap['id'] == customerId.id}">
									<c:set var="selected" value="selected"/>
								</c:if>
									<option value="${customerId.id}" ${selected}>${customerId.id}</option>
							</c:forEach>
						</select>
						</td>
				 </tr>
					<tr>
						<td align="${left}"></td>
						<td align="${left}"><input type="button"
							onclick="document.forms['searchForm'].submit();"
							value="<transys:label code="Search"/>" /></td>
					</tr>
				</table>
			</form:form>
			<br />
			<form:form name="delete.do" id="serviceForm">
				<transys:datatable urlContext="transysapp/customer" deletable="true"
					editable="true" insertable="true" baseObjects="${list}"
					searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
					pagingLink="search.do" multipleDelete="false" searcheable="false" 
					exportPdf="true" exportXls="true">
					<transys:textcolumn headerText="Name" dataField="companyName" />
					<transys:textcolumn headerText="Customer ID" dataField="id" />
					<transys:textcolumn headerText="Address Line1" dataField="billingAddressLine1" />
					<transys:textcolumn headerText="Address Line2" dataField="billingAddressLine2" />
					<transys:textcolumn headerText="City" dataField="city" />
					<transys:textcolumn headerText="State" dataField="state.name" />
					<transys:textcolumn headerText="Zipcode" dataField="zipcode" />
					<transys:textcolumn headerText="Phone" dataField="phone" />
					<transys:textcolumn headerText="Fax" dataField="fax" />
					
				</transys:datatable>
				<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
				</form:form>


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
	    $('.nav-tabs a[href="#' + tab + '"]').tab('show');
};

activaTab('orders');
   

</script>


</body>
</html>
