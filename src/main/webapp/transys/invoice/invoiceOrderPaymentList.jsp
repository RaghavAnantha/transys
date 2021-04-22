<%@include file="/common/taglibs.jsp"%>

<form:form name="invoiceOrderPaymentServiceForm" id="invoiceOrderPaymentServiceForm" class="tab-color">
	<transys:datatable urlContext="invoice" nested="true" padBottom="false"
		deletable="false" editable="false" cancellable="false" insertable="false" baseObjects="${list}"
		searchCriteria="<%=null%>" cellPadding="2"
		pagingLink="invoicePaymentSearch.do" multipleSelect="false" searcheable="false"
		exportPdf="false" exportXls="false" drawToolbar="false" drawPaging="false" dataQualifier="invoiceOrderPayment">
		<transys:textcolumn headerText="Inv. #" dataField="invoiceId" width="55px"/>
		<transys:textcolumn headerText="Inv. Pay. #" width="70px" dataField="invoicePaymentId" />
		<transys:textcolumn headerText="Ord. #" width="70px" dataField="order.id" />
		<transys:textcolumn headerText="Del. Adds." dataField="order.deliveryAddressFullLine" />
		<transys:textcolumn headerText="City" dataField="order.deliveryCity" />
		<transys:textcolumn headerText="Ord. Dt" width="70px" dataField="order.formattedCreatedAt"/>
		<transys:textcolumn headerText="Del. Dt" width="70px" dataField="order.formattedDeliveryDate"/>
		<transys:textcolumn headerText="Pick. Dt" width="70px" dataField="order.formattedPickupDate"/>
		<transys:textcolumn headerText="Ord. Pay. #" width="70px" dataField="id" />
		<transys:textcolumn headerText="Pay. Method" width="110px" dataField="paymentMethod.method" />
		<transys:textcolumn headerText="Pay. Dt" width="70px" dataField="paymentDate" dataFormat="MM/dd/yyyy"/>
		<transys:textcolumn headerText="Amt. Paid" width="68px" dataField="amountPaid" type="java.math.BigDecimal" dataFormat="#####0.00"/>
		<transys:textcolumn headerText="Check #" dataField="checkNum" />
		<transys:textcolumn headerText="CC Ref. #" dataField="ccReferenceNum" />
		<transys:textcolumn headerText="CC Name" dataField="ccName" />
		<transys:textcolumn headerText="CC #" dataField="ccNumber" />
		<transys:textcolumn headerText="CC Exp. Dt" width="70px" dataField="ccExpDate" dataFormat="MM/dd/yyyy"/>
	</transys:datatable>
</form:form>