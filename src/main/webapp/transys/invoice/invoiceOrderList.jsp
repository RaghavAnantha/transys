<%@include file="/common/taglibs.jsp"%>

<form:form name="invoiceOrderServiceForm" id="invoiceOrderServiceForm" class="tab-color">
	<transys:datatable urlContext="order" nested="true" padBottom="false"
		deletable="false" editable="false" cancellable="false" insertable="false" baseObjects="${list}"
		searchCriteria="<%=null%>" cellPadding="2"
		pagingLink="manageInvoiceSearch.do" multipleSelect="false" searcheable="false"
		exportPdf="false" exportXls="false" drawToolbar="false" drawPaging="false" dataQualifier="invoiceOrder">
		<transys:textcolumn headerText="Inv. #" dataField="invoiceHeader.id" width="55px"/>
		<transys:textcolumn headerText="Ord. #" width="70px" dataField="orderId" />
		<transys:textcolumn headerText="Del. Adds." dataField="deliveryAddressFullLine" />
		<transys:textcolumn headerText="City" dataField="deliveryAddressCity" />
		<transys:textcolumn headerText="Size" dataField="dumpsterSize" />
		<transys:textcolumn headerText="Ord. Dt" width="82px" dataField="formattedOrderDate"/>
		<transys:textcolumn headerText="Del. Dt" width="82px" dataField="formattedDeliveryDate"/>
		<transys:textcolumn headerText="Pick. Dt" width="82px" dataField="formattedPickupDate"/>
		<transys:textcolumn headerText="Dmp." dataField="dumpsterPrice" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Ton." dataField="tonnageFees" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Permit" dataField="totalPermitFees" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="City" dataField="cityFee" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Overwt." dataField="overweightFee" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Addnl." dataField="totalAdditionalFees" width="62px" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Disc." width="62px" dataField="discountAmount" type="java.math.BigDecimal"/>
		<transys:textcolumn headerText="Tot Amt" dataField="totalFees" width="72px" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Amt Paid" dataField="totalAmountPaid" width="72px" type="java.math.BigDecimal" />
		<transys:textcolumn headerText="Bal Due" dataField="balanceAmountDue" width="72px" type="java.math.BigDecimal" />
	</transys:datatable>
</form:form>