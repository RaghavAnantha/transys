CREATE TABLE `transys`.`orderInvoiceHeader` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
   `invoiceDate` datetime NOT NULL,
   `customerId` bigint(20) NOT NULL,
  `companyName` varchar(60) NOT NULL,
   `billingAddressLine1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `billingAddressLine2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(10) NOT NULL,
  `zip` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `contactName` varchar(150) NOT NULL,
  `phone1` varchar(20) NOT NULL,
  `phone2` varchar(20) DEFAULT NULL,
   `fax` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
   `email` varchar(50) DEFAULT NULL,
     `orderDateFrom` datetime NOT NULL,
     `orderDateTo` datetime NOT NULL,
    `orderCount` int(20) DEFAULT '0',
    `totalFees` decimal(9,2) DEFAULT '0.00',
    `totalDiscount` decimal(9,2) DEFAULT '0.00',
     `totalAmountPaid` decimal(9,2) DEFAULT '0.00',
	`totalBalanceAmountDue` decimal(9,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `transys`.`orderInvoiceDetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `orderId` bigint(20) NOT NULL,
  `invoiceId` bigint(20) NOT NULL,
  `orderDate` datetime NOT NULL,
  `deliveryAddressId` bigint(20) NOT NULL,
  `deliveryContactName` varchar(150) NOT NULL,
  `deliveryContactPhone1` varchar(20) NOT NULL,
  `deliveryContactPhone2` varchar(20) DEFAULT NULL,
  `deliveryDate` datetime NOT NULL,
  `deliveryAddressLine1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `deliveryAddressLine2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `deliveryAddressCity` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `deliveryAddressState` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `deliveryAddressZip` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `locationType` varchar(30) NOT NULL,
  `dumpsterSize` varchar(25) NOT NULL,
  `materialType` varchar(50) NOT NULL,
  `materialCategory` varchar(100) NOT NULL,
  `grossWeight` decimal(9,2) DEFAULT '0.00',
  `netWeightLb` decimal(9,2) DEFAULT '0.00',
  `netWeightTonnage` decimal(9,2) DEFAULT '0.00',
  `tare` decimal(9,2) DEFAULT '0.00',
  `scaleTicketNumber` varchar(50) DEFAULT NULL,
  `pickupDate` datetime DEFAULT NULL,
  `orderStatus` varchar(50) NOT NULL,
  `pickUpDriver` varchar(150) DEFAULT NULL,
  `dropOffDriver` varchar(150) DEFAULT NULL,
  `deliveryHourFrom` varchar(10) NOT NULL,
  `deliveryHourTo` varchar(10) NOT NULL,
  `deliveryMinutesFrom` varchar(5) DEFAULT NULL,
  `deliveryMinutesTo` varchar(5) DEFAULT NULL,
  `pickupOrderId` bigint(20) DEFAULT NULL,
  `dumpsterPrice` decimal(6,2) DEFAULT '0.00',
  `cityFee` decimal(6,2) DEFAULT '0.00',
  `cityFeeSuburbName` varchar(50) DEFAULT NULL,
  `permitNum1` varchar(25) DEFAULT NULL,
  `permitNum2` varchar(25) DEFAULT NULL,
  `permitNum3` varchar(25) DEFAULT NULL,
  `permitFee1` decimal(6,2) DEFAULT '0.00',
  `permitFee2` decimal(6,2) DEFAULT '0.00',
  `permitFee3` decimal(6,2) DEFAULT '0.00',
  `permitType1` varchar(25) DEFAULT NULL,
  `permitType2` varchar(25) DEFAULT NULL,
  `permitType3` varchar(25) DEFAULT NULL,
  `permitClass1` varchar(25) DEFAULT NULL,
  `permitClass2` varchar(25) DEFAULT NULL,
  `permitClass3` varchar(25) DEFAULT NULL,
  `totalPermitFees` decimal(6,2) DEFAULT '0.00',
  `overweightFee` decimal(6,2) DEFAULT '0.00',
  `overweightTonLimit` decimal(9,2) DEFAULT '0.00',
  `additionalFee1Desc` varchar(50) DEFAULT NULL,
  `additionalFee1` decimal(6,2) DEFAULT '0.00',
  `additionalFee2Desc` varchar(50) DEFAULT NULL,
  `additionalFee2` decimal(6,2) DEFAULT '0.00',
  `additionalFee3Desc` varchar(50) DEFAULT NULL,
  `additionalFee3` decimal(6,2) DEFAULT '0.00',
  `totalAdditionalFees` decimal(6,2) DEFAULT '0.00',
   `tonnageFees` decimal(6,2) DEFAULT '0.00',
  `discountAmount` decimal(6,2) DEFAULT '0.00',
  `totalFees` decimal(6,2) DEFAULT '0.00',
   `totalAmountPaid` decimal(6,2) DEFAULT '0.00',
  `balanceAmountDue` decimal(6,2) DEFAULT '0.00',
   `paymentMethod1` varchar(20) DEFAULT NULL,
   `paymentDate1` datetime DEFAULT NULL,
   `paymentAmount1` decimal(6,2) DEFAULT '0.00',
   `paymentCCRefNum1` varchar(50) DEFAULT NULL,
   `paymentCheckNum1` varchar(50) DEFAULT NULL,
    `paymentMethod2` varchar(20) DEFAULT NULL,
   `paymentDate2` datetime DEFAULT NULL,
   `paymentAmount2` decimal(6,2) DEFAULT '0.00',
   `paymentCCRefNum2` varchar(50) DEFAULT NULL,
   `paymentCheckNum2` varchar(50) DEFAULT NULL,
    `paymentMethod3` varchar(20) DEFAULT NULL,
   `paymentDate3` datetime DEFAULT NULL,
   `paymentAmount3` decimal(6,2) DEFAULT '0.00',
   `paymentCCRefNum3` varchar(50) DEFAULT NULL,
   `paymentCheckNum3` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
   KEY `orderInvoiceDetailsInvoiceHeaderRef_idx` (`invoiceId`),
  CONSTRAINT `orderInvoiceDetailsInvoiceHeaderRef` FOREIGN KEY (`invoiceId`) REFERENCES `orderInvoiceHeader` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

ALTER TABLE `transys`.`orderInvoiceHeader` 
ADD INDEX `ordInvHdrCustomer_IDX` (`customerId` ASC)  COMMENT '',
ADD INDEX `ordInvHdrInvoiceDate_IDX` (`invoiceDate` ASC)  COMMENT '';

ALTER TABLE `transys`.`orderInvoiceDetails` 
ADD UNIQUE INDEX `ordInvDets_invoiceIdOrderId_UNIQ` (`invoiceId` ASC, `orderId` ASC)  COMMENT '';

ALTER TABLE `transys`.`orderInvoiceDetails` 
ADD INDEX `orderInvoiceDetailsOrderId_idx` (`orderId` ASC)  COMMENT '',
ADD INDEX `orderInvoiceDetailsOrderDate_idx` (`orderDate` ASC)  COMMENT '',
ADD INDEX `orderInvoiceDetailsDelAddsId_idx` (`deliveryAddressId` ASC)  COMMENT '';

ALTER TABLE `transys`.`transysOrder` 
ADD COLUMN `invoiced` VARCHAR(10) NULL DEFAULT 'N' COMMENT '' AFTER `balanceAmountDue`;

ALTER TABLE `transys`.`transysOrder` 
ADD COLUMN `invoiceDate` DATETIME NULL DEFAULT NULL COMMENT '' AFTER `invoiced`,
ADD COLUMN `invoiceId` BIGINT(20) NULL DEFAULT NULL COMMENT '' AFTER `invoiceDate`;
------------

CREATE TABLE `transys`.`orderInvoicePayment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `amountPaid` decimal(6,2) NOT NULL DEFAULT '0.00',
  `ccExpDate` datetime DEFAULT NULL,
  `ccName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ccNumber` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ccReferenceNum` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `checkNum` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `paymentDate` datetime NOT NULL,
  `invoiceId` bigint(20) NOT NULL,
  `paymentMethodId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `orderInvoicePaymentInvoiceHeaderRef_idx` (`invoiceId`),
  KEY `orderInvoicePaymentMethodRef_idx` (`paymentMethodId`),
  CONSTRAINT `orderInvoicePaymentInvoiceHeaderRef` FOREIGN KEY (`invoiceId`) REFERENCES `orderInvoiceHeader` (`id`),
  CONSTRAINT `orderInvoicePaymentMethodRef` FOREIGN KEY (`paymentMethodId`) REFERENCES `paymentMethodType` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

----------
ALTER TABLE `transys`.`transysOrder` 
CHANGE COLUMN `invoiceDate` `invoiceDates` VARCHAR(100) NULL DEFAULT NULL COMMENT '' ,
CHANGE COLUMN `invoiceId` `invoiceIds` VARCHAR(100) NULL DEFAULT NULL COMMENT '' ;

ALTER TABLE `transys`.`orderPayment` 
ADD COLUMN `invoiceId` BIGINT(20) NULL COMMENT '' AFTER `ccExpDate`,
ADD COLUMN `invoicePaymentId` BIGINT(20) NULL COMMENT '' AFTER `invoiceId`;

ALTER TABLE `transys`.`orderInvoicePayment` 
ADD COLUMN `amountAvailable` DECIMAL(6,2) NOT NULL DEFAULT '0.00' COMMENT '' AFTER `paymentMethodId`,
ADD COLUMN `notes` VARCHAR(500) NULL DEFAULT NULL COMMENT '' AFTER `amountAvailable`;

ALTER TABLE `transys`.`orderInvoiceHeader` 
ADD COLUMN `notes` VARCHAR(500) NULL DEFAULT NULL COMMENT '' AFTER `totalBalanceAmountDue`,
ADD COLUMN `totalInvoicePaymentDone` DECIMAL(9,2) NULL DEFAULT '0.00' COMMENT '' AFTER `notes`,
ADD COLUMN `totalInvoiceBalanceDue` DECIMAL(9,2) NULL DEFAULT '0.00' COMMENT '' AFTER `totalInvoicePaymentDone`,
ADD COLUMN `totalInvoiceBalanceAvailable` DECIMAL(9,2) NULL DEFAULT '0.00' COMMENT '' AFTER `totalInvoiceBalanceDue`;

update transys.transysOrder
set invoiced = 'N'
where invoiced is null;

update transys.transysOrder
set invoiced = 'N'
where invoiced = 'Y' or invoiced is null;

update transys.transysOrder
set invoiceDates=null, invoiceIds=null
where invoiced = 'N'

delete from transys.orderInvoiceDetails
where invoiceId in (1,2);

delete from transys.orderInvoiceHeader
where id in (1,2);

select * from transys.transysOrder
where invoiced = 'Y' or invoiced is null
order by id desc;


-----
ALTER TABLE `transys`.`orderInvoiceDetails` 
ADD COLUMN `invoicedAmount` DECIMAL(6,2) NULL COMMENT '' AFTER `paymentCheckNum3`;

update transys.orderInvoiceDetails
set invoicedAmount = balanceAmountDue;

ALTER TABLE `transys`.`orderInvoiceDetails` 
CHANGE COLUMN `invoicedAmount` `invoicedAmount` DECIMAL(6,2) NOT NULL DEFAULT '0.00' COMMENT '' ;

ALTER TABLE `transys`.`orderInvoiceHeader` 
ADD COLUMN `totalInvoicedAmount` DECIMAL(9,2) NOT NULL DEFAULT '0.00' COMMENT '' AFTER `totalInvoiceBalanceAvailable`;

update transys.orderInvoiceHeader
set totalInvoicedAmount = totalBalanceAmountDue;

ALTER TABLE `transys`.`orderPayment` 
ADD COLUMN `invoiceDate` DATETIME NULL DEFAULT NULL COMMENT '' AFTER `invoicePaymentId`;

---------
ALTER TABLE `transys`.`transysOrder` 
ADD COLUMN `invoicedAmount` DECIMAL(6,2) NULL DEFAULT '0.00' COMMENT '' AFTER `pickupVehicleId`;

select sum(invoicedAmount), orderId
from transys.orderInvoiceDetails
group by orderId
order by orderId asc

update transys.transysOrder set invoicedAmount=	380	where id=	37944;