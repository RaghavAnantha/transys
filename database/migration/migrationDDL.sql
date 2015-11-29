USE `transys`;

CREATE TABLE `oldCustomers` (
  `id` int(11) NOT NULL,
  `companyname` varchar(150) DEFAULT NULL,
  `contactname` varchar(150) DEFAULT NULL,
  `address1` tinytext,
  `address2` tinytext,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `zip` varchar(15) DEFAULT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `altPhone1` varchar(25) DEFAULT NULL,
  `altPhone2` varchar(25) DEFAULT NULL,
  `fax` varchar(25) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `custtype` char(1) DEFAULT NULL,
  `chargecompany` char(1) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `comments` mediumtext,
  `WhenEdited` datetime DEFAULT NULL,
  `WhoEdited` int(11) DEFAULT NULL,
  `WhenCreated` datetime DEFAULT NULL,
  `WhoCreated` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `oldAddresses` (
  `id` int(11) NOT NULL,
  `custID` int(11) NOT NULL,
  `address1` varchar(150) DEFAULT NULL,
  `address2` varchar(150) DEFAULT NULL,
  `city` varchar(150) DEFAULT NULL,
  `state` varchar(150) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `comments` longtext,
  `status` char(1) DEFAULT NULL,
  `WhoEdited` int(11) DEFAULT NULL,
  `WhenEdited` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
--  KEY `oldAddressesCustomerRef_idx` (`custID`),
--  CONSTRAINT `oldAddressesCustomerRef` FOREIGN KEY (`custID`) REFERENCES `oldCustomers` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `oldDumpsters` (
  `id` int(11) NOT NULL,
  `dumpsternum` varchar(20) DEFAULT NULL,
  `dumpstersize` decimal(18,0) DEFAULT NULL,
  `comments` longtext,
  `InRepair` char(1) DEFAULT NULL,
  `orderID` int(11) DEFAULT NULL,
  `RentedAddressID` int(11) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `WhoEdited` int(11) DEFAULT NULL,
  `WhenEdited` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




