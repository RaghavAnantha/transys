<%@include file="/common/taglibs.jsp"%>

<br/>
<h4 style="margin-top: -15px; !important">Data Migration</h4>

<form:form action="migrate.do" method="POST" name="migrationForm" id="migrationForm" >
	<table id="form-table" class="table">
	 	<tr><td colspan="10"></td><td colspan="10"></td></tr>
 		<tr>
			<td></td>
			<td><input type="button" class="btn btn-primary btn-sm btn-sm-ext" onclick="document.forms['migrationForm'].submit();"
				value="Migrate" />
			</td>
		</tr>
	</table>
</form:form>
