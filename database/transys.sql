-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: 127.0.0.1    Database: transys
-- ------------------------------------------------------
-- Server version	5.6.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`transys` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `transys`;

--
-- Table structure for table `deliveryAddress`
--

DROP TABLE IF EXISTS `deliveryAddress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliveryAddress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `customerId` bigint(20) NOT NULL,
  `line1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `line2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` bigint(20) NOT NULL,
  `zip` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `deliveryAddressCustomerRef_idx` (`customerId`),
  KEY `deliveryAddressStateRef_idx` (`state`),
  CONSTRAINT `deliveryAddressStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deliveryAddressCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliveryAddress`
--

LOCK TABLES `deliveryAddress` WRITE;
/*!40000 ALTER TABLE `deliveryAddress` DISABLE KEYS */;
INSERT INTO `deliveryAddress` VALUES (1,'2015-09-10 21:22:45',1,NULL,NULL,5,'4818 W Van Buren',NULL,'Chicago',1,'28262',1),
(3,'2015-09-10 21:22:45',1,NULL,NULL,5,'1121 E Lemon st',NULL,'Chicago',1,'28262',1);
INSERT INTO `deliveryAddress` (`id`, `created_at`, `created_by`, `customerId`, `line1`, `line2`, `city`, `state`, `zip`, `delete_flag`) 
VALUES ('4', '2015-09-10 21:22:45',1,'6', '1890 Chesterfield Ct', NULL, 'Chicago', '1', '28262', '1');
/*!40000 ALTER TABLE `deliveryAddress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permitAddress`
--

DROP TABLE IF EXISTS `permitAddress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitAddress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `permitId` bigint(20) NOT NULL,
  `line1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `line2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `state` bigint(20) NOT NULL,
  `zip` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `permitRef_idx` (`permitId`),
  KEY `stateRef_idx` (`state`),
  CONSTRAINT `permitAddressPermitRef` FOREIGN KEY (`permitId`) REFERENCES `permit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitAddressStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitAddress`
--

LOCK TABLES `permitAddress` WRITE;
/*!40000 ALTER TABLE `permitAddress` DISABLE KEYS */;
INSERT INTO `permitAddress` VALUES (1,'2015-09-10 21:22:45',1,NULL,NULL,1,'4818 W VAN BUREN',NULL,'Chicago',1,'28262',1),
(2,'2015-09-10 21:22:45',1,NULL,NULL,1,'1121 E Lemon st',NULL,'Chicago',1,'28262',1);
/*!40000 ALTER TABLE `permitAddress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessObject`
--

DROP TABLE IF EXISTS `businessObject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessObject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(200) NOT NULL,
  `displayTag` varchar(60) DEFAULT NULL,
  `objectLevel` int(11) DEFAULT NULL,
  `objectName` varchar(60) DEFAULT NULL,
  `url` varchar(5000) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `defaultFlag` int(11) DEFAULT NULL,
  `displayOrder` int(11) DEFAULT NULL,
  `hidden` int(11) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `hierarchy` varchar(255) DEFAULT NULL,
  `urlContext` varchar(255) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `businessObjectParentRef_Idx` (`parentId`),
  CONSTRAINT `businessObjectParentRef` FOREIGN KEY (`parentId`) REFERENCES `businessObject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessObject`
--

LOCK TABLES `businessObject` WRITE;
/*!40000 ALTER TABLE `businessObject` DISABLE KEYS */;
/*!40000 ALTER TABLE `businessObject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `customerStatusId` bigint(20) NOT NULL,
  `billingAddressLine1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `customerTypeId` bigint(20) NOT NULL,
  `billingAddressLine2` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `companyName` varchar(60) COLLATE utf8_unicode_ci NOT NULL,
  `fax` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contactName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `zipcode` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
  `state` bigint(20) NOT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `altPhone1` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `altPhone2` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `chargeCompany` varchar(5) NOT NULL,
  `dumpsterDiscount` decimal(6,2) DEFAULT 0.00,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_customerCompanyName` (`companyName`),
  CONSTRAINT `customerStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customerStatusRef` FOREIGN KEY (`customerStatusId`) REFERENCES `customerStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customerTypeRef` FOREIGN KEY (`customerTypeId`) REFERENCES `customerType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`id`,`created_at`,`created_by`,`modified_at`,`modified_by`,`customerStatusId`,`billingAddressLine1`,`customerTypeId`,`billingAddressLine2`,`city`,`companyName`,`fax`,`contactName`,`phone`,`zipcode`,`state`,`email`,`altPhone1`,`altPhone2`,`chargeCompany`,`delete_flag`) VALUES (5,'2015-09-10 21:22:45',1,'2015-10-25 18:37:31',1,1,'2324 N Camelback Rd',1,'','Chicago','Aberdeen Construction','223-456-7890','Raghav','223-456-7890','28262',1,'abc@aberdeen.com','','','Yes',1);
INSERT INTO `customer` (`id`,`created_at`,`created_by`,`modified_at`,`modified_by`,`customerStatusId`,`billingAddressLine1`,`customerTypeId`,`billingAddressLine2`,`city`,`companyName`,`fax`,`contactName`,`phone`,`zipcode`,`state`,`email`,`altPhone1`,`altPhone2`,`chargeCompany`,`delete_flag`) VALUES (6,'2015-09-11 21:05:43',1,NULL,NULL,1,'1321 W Main St',1,NULL,'Chicago','Gibbons Construction','435-768-7890','Hema','465-768-7890','22323',1,'xyz@gibbons.com',NULL,NULL,'Yes',1);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dumpster`
--

DROP TABLE IF EXISTS `dumpster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpster` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dumpsterSizeId` bigint(20) NOT NULL,
  `dumpsterNum` varchar(50) NOT NULL,
  `status` bigint(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dumpsterDumpsterNum` (`dumpsterNum`),
  KEY `dumpsterInfoDumpsterStatusRef_Idx` (`status`),
  CONSTRAINT `dumpsterInfoDumpsterStatusRef` FOREIGN KEY (`status`) REFERENCES `dumpsterStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dumpsterInfoDumpsterSizeRef` FOREIGN KEY (`dumpsterSizeId`) REFERENCES `dumpsterSize` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpster`
--

LOCK TABLES `dumpster` WRITE;
/*!40000 ALTER TABLE `dumpster` DISABLE KEYS */;
INSERT INTO `dumpster` VALUES (2,1,'20W-113-21',3,NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
INSERT INTO `dumpster` VALUES (3,2,'30W-456-31',1,NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `dumpster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dumpsterStatus`
--

DROP TABLE IF EXISTS `dumpsterStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpsterStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dumpsterStatusDumpsterStatus` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterStatus`
--

LOCK TABLES `dumpsterStatus` WRITE;
/*!40000 ALTER TABLE `dumpsterStatus` DISABLE KEYS */;
INSERT INTO `dumpsterStatus` VALUES (1,'Available',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'In Repair',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(3,'Dropped Off',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `dumpsterStatus` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `jobTitleId` bigint(20) NOT NULL,
  `address` tinytext DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `hireDate` datetime NOT NULL,
  `leaveDate` datetime DEFAULT NULL,
  `comments` longtext DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `status` bigint(20) NOT NULL,
  `employeeId` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_employeeEmployeeId` (`employeeId`),
  UNIQUE KEY `UK_employeeEmployeeName` (`firstName`, `lastName`),
  KEY `employeeJobTitleRef_idx` (`jobTitleId`),
  KEY `employeeStateRef_idx` (`state`),
  KEY `employeeStatusRef_idx` (`status`),
  CONSTRAINT `employeeStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `employeeStatusRef` FOREIGN KEY (`status`) REFERENCES `employeeStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `employeeJobTitleRef` FOREIGN KEY (`jobTitleId`) REFERENCES `jobTitle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` (`id`,`firstName`,`lastName`,`jobTitleId`,`address`,`city`,`state`,`zip`,`phone`,`email`,`hireDate`,`leaveDate`,`comments`, `created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`,`status`,`employeeId`) VALUES (1,'admin','admin',6,'123 Claude Freeman Dr','Chicago',1,'28262','773-987-2221','kasiaFigura@gmail.com','2015-09-02 00:00:00',NULL,NULL,'2015-09-24 12:21:31',1,'2015-09-24 14:39:45',1,1,1,'007');
INSERT INTO `employee` (`id`,`firstName`,`lastName`,`jobTitleId`,`address`,`city`,`state`,`zip`,`phone`,`email`,`hireDate`,`leaveDate`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`,`status`,`employeeId`) VALUES (2,'Kasia','Figura',5,'5521 Milwaukee','Chicago',1,'60630','773-987-2221','kasiaFigura@gmail.com','2015-09-01 00:00:00',NULL,NULL,'2015-09-24 12:21:31',1,NULL,NULL,1,1,'123');
INSERT INTO `employee` (`id`,`firstName`,`lastName`,`jobTitleId`,`address`,`city`,`state`,`zip`,`phone`,`email`,`hireDate`,`leaveDate`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`,`status`,`employeeId`) VALUES (4,'Scott','Eaker',2,'5521 Milwaukee','Chicago',1,'60630','773-987-2221','kasiaFigura@gmail.com','2015-09-01 00:00:00',NULL,NULL,'2015-09-24 12:28:04',1,NULL,NULL,1,1,'234');
INSERT INTO `employee` (`id`,`firstName`,`lastName`,`jobTitleId`,`address`,`city`,`state`,`zip`,`phone`,`email`,`hireDate`,`leaveDate`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`,`status`,`employeeId`) VALUES (5,'Aldo','Valazquez',7,'5521 Milwaukee','Chicago',1,'60630','773-987-2221','kasiaFigura@gmail.com','2015-09-01 00:00:00',NULL,NULL,'2015-09-24 12:40:41',1,NULL,NULL,1,1,'345');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employeeStatus`
--

DROP TABLE IF EXISTS `employeeStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employeeStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_employeeStatusStatus` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeeStatus`
--

LOCK TABLES `employeeStatus` WRITE;
/*!40000 ALTER TABLE `employeeStatus` DISABLE KEYS */;
INSERT INTO `employeeStatus` VALUES (1,'Active',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'Inactive',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `employeeStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobTitle`
--

DROP TABLE IF EXISTS `jobTitle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobTitle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `jobTitle` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobTitle`
--

LOCK TABLES `jobTitle` WRITE;
/*!40000 ALTER TABLE `jobTitle` DISABLE KEYS */;
INSERT INTO `jobTitle` VALUES 
(1,'2015-09-10 21:22:45',1,NULL,NULL,'President',NULL,1),
(2,'2015-09-10 21:22:45',1,NULL,NULL,'Roll-Off Driver',NULL,1),
(3,'2015-09-10 21:22:45',1,NULL,NULL,'Semi Driver',NULL,1),
(4,'2015-09-10 21:22:45',1,NULL,NULL,'Driver',NULL,1),
(5,'2015-09-10 21:22:45',1,NULL,NULL,'Receptionist',NULL,1),
(6,'2015-09-10 21:22:45',1,NULL,NULL,'Office',NULL,1),
(7,'2015-09-10 21:22:45',1,NULL,NULL,'Roll-Off Truck Driver',NULL,1),
(8,'2015-09-10 21:22:45',1,NULL,NULL,'Representative',NULL,1);
/*!40000 ALTER TABLE `jobTitle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `transysOrder`
--

DROP TABLE IF EXISTS `transysOrder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transysOrder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customerId` bigint(20) NOT NULL,
  `deliveryContactName` varchar(150) NOT NULL,
  `deliveryContactPhone1` varchar(20) NOT NULL,
  `deliveryContactPhone2` varchar(20) DEFAULT NULL,
  `deliveryDate` datetime NOT NULL,
  `deliveryAddressId` bigint(20) NOT NULL,
  `locationTypeId` bigint(20) NOT NULL,
  `dumpsterSizeId` bigint(20) NOT NULL,
  `materialTypeId` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `grossWeight` decimal(9,2) DEFAULT 0.00,
  `netWeightLb` decimal(9,2) DEFAULT 0.00,
  `netWeightTonnage` decimal(9,2) DEFAULT 0.00,
  `tare` decimal(9,2) DEFAULT 0.00,
  `scaleTicketNumber` varchar(50) DEFAULT NULL,
  `dumpsterId` bigint(20) DEFAULT NULL,
  `pickupDate` datetime DEFAULT NULL,
  `orderStatusId` bigint(20) NOT NULL,
  `pickUpDriverId` bigint(20) DEFAULT NULL,
  `dropOffDriverId` bigint(20) DEFAULT NULL,
  `deliveryHourFrom` varchar(10) NOT NULL,
  `deliveryHourTo` varchar(10) NOT NULL,
  `deliveryMinutesFrom` varchar(5) DEFAULT NULL,
  `deliveryMinutesTo` varchar(5) DEFAULT NULL,
  `pickupOrderId` bigint(20) DEFAULT NULL,
  `totalAmountPaid` decimal(6,2) DEFAULT 0.00,
  `balanceAmountDue` decimal(6,2) DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `orderCustomerRef_idx` (`customerId`),
  CONSTRAINT `orderCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderDeliveryAddressRef` FOREIGN KEY (`deliveryAddressId`) REFERENCES `deliveryAddress` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderStatusRef` FOREIGN KEY (`orderStatusId`) REFERENCES `orderStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderDumpsterRef` FOREIGN KEY (`dumpsterId`) REFERENCES `dumpster` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderMaterialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `materialType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderDumpsterLocationRef` FOREIGN KEY (`locationTypeId`) REFERENCES `locationType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderDumpsterSizeRef` FOREIGN KEY (`dumpsterSizeId`) REFERENCES `dumpsterSize` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderPickupDriverUserInfoRef` FOREIGN KEY (`pickUpDriverId`) REFERENCES `userInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderDropOffDriverUserInfoRef` FOREIGN KEY (`dropOffDriverId`) REFERENCES `userInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transysOrder`
--

LOCK TABLES `transysOrder` WRITE;
/*!40000 ALTER TABLE `transysOrder` DISABLE KEYS */;
INSERT INTO `transysOrder` (`id`,`customerId`,`deliveryContactName`,`deliveryContactPhone1`,`deliveryContactPhone2`,`deliveryDate`,`deliveryAddressId`,`locationTypeId`,`dumpsterSizeId`,`materialTypeId`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`,`grossWeight`,`netWeightLb`,`netWeightTonnage`,`tare`,`dumpsterId`,`pickupDate`,`orderStatusId`,`pickUpDriverId`,`dropOffDriverId`,`deliveryHourFrom`,`deliveryHourTo`,`deliveryMinutesFrom`,`deliveryMinutesTo`,`pickupOrderId`,`totalAmountPaid`,`balanceAmountDue`) VALUES (1,5,'Raghav','223-456-7890','223-456-7890','2015-10-09 00:00:00',1,1,1,1,'2015-10-09 00:00:00',1,'2015-10-15 14:51:57',1,1,NULL,NULL,NULL,NULL,2,NULL,2,NULL,2,'12 PM','1 PM','00','15',NULL,190.00,45.20);
/*!40000 ALTER TABLE `transysOrder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationType`
--

DROP TABLE IF EXISTS `locationType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `locationType` varchar(30) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_locationTypeLocationType` (`locationType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationType`
--

LOCK TABLES `locationType` WRITE;
/*!40000 ALTER TABLE `locationType` DISABLE KEYS */;
INSERT INTO `locationType` VALUES (1,'Alley',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),(2,'Street (Curb)',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),(3,'Driveway',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `locationType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customerType`
--

DROP TABLE IF EXISTS `customerType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customerType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customerType` varchar(50) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_customerTypeCustomerType` (`customerType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerType`
--

LOCK TABLES `customerType` WRITE;
/*!40000 ALTER TABLE `customerType` DISABLE KEYS */;
INSERT INTO `customerType` VALUES (1,'Commercial',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `customerType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customerNotes`
--

DROP TABLE IF EXISTS `customerNotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customerNotes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customerId` bigint(20) NOT NULL,
  `notes` varchar(500) NOT NULL,
  `notesType` VARCHAR(10) NOT NULL,
  `entered_by` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `customerNotesCustomerRef_idx` (`customerId`),
  CONSTRAINT `customerNotesCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerNotes`
--

LOCK TABLES `customerNotes` WRITE;
/*!40000 ALTER TABLE `customerNotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `customerNotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderNotes`
--

DROP TABLE IF EXISTS `orderNotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderNotes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(20) NOT NULL,
  `notes` varchar(500) NOT NULL,
  `notesType` VARCHAR(10) NOT NULL,
  `entered_by` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `orderNotesOrderRef_idx` (`orderId`),
  CONSTRAINT `orderNotesOrderRef` FOREIGN KEY (`orderId`) REFERENCES `transysOrder` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderNotes`
--

LOCK TABLES `orderNotes` WRITE;
/*!40000 ALTER TABLE `orderNotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderNotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderFees`
--

DROP TABLE IF EXISTS `orderFees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderFees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(20) NOT NULL,
  `dumpsterPrice` decimal(6,2) DEFAULT 0.00,
  `cityFee` decimal(6,2) DEFAULT 0.00,
  `cityFeeId` bigint(20) DEFAULT NULL,
  `permitFee1` decimal(6,2) DEFAULT 0.00,
  `permitFee2` decimal(6,2) DEFAULT 0.00,
  `permitFee3` decimal(6,2) DEFAULT 0.00,
  `totalPermitFees` decimal(6,2) DEFAULT 0.00,
  `overweightFee` decimal(6,2) DEFAULT 0.00,
  `additionalFee1Id` bigint(20) DEFAULT NULL,
  `additionalFee1` decimal(6,2) DEFAULT 0.00,
  `additionalFee2Id` bigint(20) DEFAULT NULL,
  `additionalFee2` decimal(6,2) DEFAULT 0.00,
  `additionalFee3Id` bigint(20) DEFAULT NULL,
  `additionalFee3` decimal(6,2) DEFAULT 0.00,
  `totalAdditionalFees` decimal(6,2) DEFAULT 0.00,
  `tonnageFee` decimal(6,2) DEFAULT 0.00,
  `discountAmount` decimal(6,2) DEFAULT 0.00,
  `totalFees` decimal(6,2) DEFAULT 0.00,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  CONSTRAINT `orderFeesOrderRef` FOREIGN KEY (`orderId`) REFERENCES `transysOrder` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderFeesCityFeeRef` FOREIGN KEY (`cityFeeId`) REFERENCES `cityFee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderFeesAddnlFee1Ref` FOREIGN KEY (`additionalFee1Id`) REFERENCES `additionalFee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderFeesAddnlFee2Ref` FOREIGN KEY (`additionalFee2Id`) REFERENCES `additionalFee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderFeesAddnlFee3Ref` FOREIGN KEY (`additionalFee3Id`) REFERENCES `additionalFee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderFees`
--

LOCK TABLES `orderFees` WRITE;
/*!40000 ALTER TABLE `orderFees` DISABLE KEYS */;
INSERT INTO `orderFees` (`id`,`orderId`,`dumpsterPrice`,`cityFee`,`cityFeeId`,`permitFee1`,`permitFee2`,`permitFee3`,`totalPermitFees`,`overweightFee`,`additionalFee1Id`,`additionalFee1`,`additionalFee2Id`,`additionalFee2`,`additionalFee3Id`,`additionalFee3`,`totalAdditionalFees`,`discountAmount`,`totalFees`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (1,1,240.00,0.00,5,50.00,NULL,NULL,50.00,0.00,NULL,NULL,NULL,NULL,NULL,NULL,0.00,4.80,235.20,'2015-10-09 19:19:03',1,NULL,1,1);
/*!40000 ALTER TABLE `orderFees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderPayment`
--

DROP TABLE IF EXISTS `orderPayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderPayment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(20) NOT NULL,
  `paymentMethodId` bigint(20) NOT NULL,
  `ccReferenceNum` varchar(50) DEFAULT NULL,
  `checkNum` varchar(50) DEFAULT NULL,
  `amountPaid` decimal(6,2) DEFAULT 0.00,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `paymentDate` DATETIME DEFAULT NULL,
  `ccName` VARCHAR(100) DEFAULT NULL,
  `ccNumber` VARCHAR(45) DEFAULT NULL,
  `ccExpDate` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `orderPaymentPaymentMethodRef` FOREIGN KEY (`paymentMethodId`) REFERENCES `paymentMethodType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderPaymentOrderRef` FOREIGN KEY (`orderId`) REFERENCES `transysOrder` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderPayment`
--

LOCK TABLES `orderPayment` WRITE;
/*!40000 ALTER TABLE `orderPayment` DISABLE KEYS */;
INSERT INTO `orderPayment` (`id`,`orderId`,`paymentMethodId`,`ccReferenceNum`,`checkNum`,`amountPaid`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (1,1,1,'','123',100.00,'2015-10-09 19:19:03',1,NULL,1,1);
INSERT INTO `orderPayment` (`id`,`orderId`,`paymentMethodId`,`ccReferenceNum`,`checkNum`,`amountPaid`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (2,1,2,'','',45.00,'2015-10-15 12:11:36',1,NULL,1,1);
INSERT INTO `orderPayment` (`id`,`orderId`,`paymentMethodId`,`ccReferenceNum`,`checkNum`,`amountPaid`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (3,1,3,'67','',45.00,'2015-10-15 12:14:37',1,NULL,1,1);
/*!40000 ALTER TABLE `orderPayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderPermits`
--

DROP TABLE IF EXISTS `orderPermits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderPermits` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(20) NOT NULL,
  `permitId` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_orderPermit` (`orderId`,`permitId`),
  KEY `orderPermitRef_idx` (`orderId`),
  KEY `permitRef_idx` (`permitId`),
  CONSTRAINT `orderPermitOrderRef` FOREIGN KEY (`orderId`) REFERENCES `transysOrder` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderPermitPermitRef` FOREIGN KEY (`permitId`) REFERENCES `permit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderPermits`
--

LOCK TABLES `orderPermits` WRITE;
/*!40000 ALTER TABLE `orderPermits` DISABLE KEYS */;
INSERT INTO `orderPermits` VALUES (3,1,1,'2012-03-16 14:25:34',1,NULL,NULL,1);
/*!40000 ALTER TABLE `orderPermits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderStatus`
--

DROP TABLE IF EXISTS `orderStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_orderStatusStatus` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderStatus`
--

LOCK TABLES `orderStatus` WRITE;
/*!40000 ALTER TABLE `orderStatus` DISABLE KEYS */;
INSERT INTO `orderStatus` VALUES 
(1,'Open',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'Dropped Off',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(3,'Closed',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(4,'Pick Up',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(5,'Cancelled',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);

/*!40000 ALTER TABLE `orderStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customerStatus`
--

DROP TABLE IF EXISTS `customerStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customerStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_customerStatusStatus` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerStatus`
--

LOCK TABLES `customerStatus` WRITE;
/*!40000 ALTER TABLE `customerStatus` DISABLE KEYS */;
INSERT INTO `customerStatus` VALUES
(1,'Active',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'Inactive',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `customerStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permit`
--

DROP TABLE IF EXISTS `permit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permitTypeId` bigint(20) NOT NULL,
  `number` varchar(25) DEFAULT NULL,
  `fee` decimal(6,2) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  `locationTypeId` bigint(20) NOT NULL,
  `status` bigint(20) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `customerId` bigint(20) NOT NULL,
  `deliveryAddressId` bigint(20) NOT NULL,
  `parkingMeter` varchar(3) NOT NULL,
  `parkingMeterFee` decimal(6,2) DEFAULT 0.00,
  `permitClassId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `permitStatusRef_idx` (`status`),
  KEY `permitCustomerRef_idx` (`customerId`),
  KEY `permitDeliveryAddressRef_idx` (`deliveryAddressId`),
  KEY `permitLocationTypeRef_idx` (`locationTypeId`),
  KEY `permitTypeRef_idx` (`permitTypeId`),
  KEY `permitClassRef_Idx` (`permitClassId`),
  CONSTRAINT `permitClassRef` FOREIGN KEY (`permitClassId`) REFERENCES `permitClass` (`id`),
  CONSTRAINT `permitCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitDeliveryAddressRef` FOREIGN KEY (`deliveryAddressId`) REFERENCES `deliveryAddress` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitLocationTypeRef` FOREIGN KEY (`locationTypeId`) REFERENCES `locationType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitStatusRef` FOREIGN KEY (`status`) REFERENCES `permitStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitTypeRef` FOREIGN KEY (`permitTypeId`) REFERENCES `permitType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permit`
--

LOCK TABLES `permit` WRITE;
/*!40000 ALTER TABLE `permit` DISABLE KEYS */;
 INSERT INTO `permit` VALUES
 (1,1,'1301-11W',50,'2015-08-09 00:00:00','2015-11-09 00:00:00',1,3,'2015-09-22 00:00:00',1,'2015-09-22 21:51:56', NULL, 1,5,1,'Yes',1.0,1),(2,1,'5667890',90,'2015-09-22 00:00:00','2015-09-25 00:00:00',1,2,'2015-09-23 12:27:14',1,NULL,NULL,1,6,4,'Yes',90,1),
 (3,2,'1987-34E',90,'2015-09-22 00:00:00','2015-09-25 00:00:00',1,2,'2015-09-23 12:27:14',1,NULL,NULL,1,6,4,'Yes',90,1);
/*!40000 ALTER TABLE `permit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permitClass`
--

DROP TABLE IF EXISTS `permitClass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitClass` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `permitClass` varchar(25) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permitClassClass` (`permitClass`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitClass`
--

LOCK TABLES `permitClass` WRITE;
/*!40000 ALTER TABLE `permitClass` DISABLE KEYS */;
INSERT INTO `permitClass` VALUES (1,'2015-09-10 21:22:45',1,NULL,NULL,1,'CLASS A',NULL),
(2,'2015-09-22 13:47:10',1,NULL,NULL,1,'CLASS B',NULL),
(3,'2015-09-22 13:47:10',1,NULL,NULL,1,'CLASS D',NULL);
/*!40000 ALTER TABLE `permitClass` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `permitStatus`
--

DROP TABLE IF EXISTS `permitStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` varchar(15) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permitStatusStatus` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitStatus`
--

LOCK TABLES `permitStatus` WRITE;
/*!40000 ALTER TABLE `permitStatus` DISABLE KEYS */;
INSERT INTO `permitStatus` VALUES 
(1,'Pending',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'Available',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(3,'Assigned',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(4,'Expired',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(5,'Cancelled',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `permitStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permitType`
--

DROP TABLE IF EXISTS `permitType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permitType` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permitTypeType` (`permitType`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitType`
--

LOCK TABLES `permitType` WRITE;
/*!40000 ALTER TABLE `permitType` DISABLE KEYS */;
INSERT INTO `permitType` VALUES 
(1,'3 days',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1),
(2,'30 days',NULL,'2015-09-10 21:22:45',1,NULL,NULL,1);
/*!40000 ALTER TABLE `permitType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `theme` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'2015-09-10 21:22:45',0,NULL,NULL,1,'ADMIN',NULL,1),
(2,'2015-09-10 21:22:45',1,NULL,NULL,0,'OPERATOR',NULL,1),
(3,'2015-09-10 21:22:45',1,NULL,NULL,0,'REPORTUSER',NULL,1),
(4,'2012-03-16 01:43:40',1,NULL,NULL,0,'DATA_ENTRY_BILLING',NULL,1),
(5,'2012-03-16 14:25:34',1,NULL,NULL,0,'TEST',NULL,1),
(6,'2012-03-16 14:25:34',1,NULL,NULL,0,'DRIVER',NULL,1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolePrivilege`
--

DROP TABLE IF EXISTS `rolePrivilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolePrivilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `permissionType` int(11) NOT NULL,
  `businessObjectId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `rolePrivelegeRoleRef_Idx` (`roleId`),
  KEY `rolePrivelegeBusinessObjectRef_Idx` (`businessObjectId`),
  CONSTRAINT `rolePrivelegeRoleRef` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `rolePrivelegeBusinessObjectRef` FOREIGN KEY (`businessObjectId`) REFERENCES `businessObject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolePrivilege`
--

LOCK TABLES `rolePrivilege` WRITE;
/*!40000 ALTER TABLE `rolePrivilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `rolePrivilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `delete_flag` int(11) DEFAULT '1',
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `code` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_stateStateCode` (`code`),
  UNIQUE KEY `UK_stateName` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,'2012-03-16 14:25:34',1,1,NULL,NULL,'IL','Illinois'),
(2,'2012-03-16 14:25:34',1,1,NULL,NULL,'WI','Wisconsin'),
(3,'2012-03-16 14:25:34',1,1,NULL,NULL,'IN','Indiana'),
(4,'2012-03-16 14:25:34',1,1,NULL,NULL,'MI','Michigan'),
(5,'2012-03-16 14:25:34',1,1,NULL,NULL,'OH','Ohio');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userInfo`
--

DROP TABLE IF EXISTS `userInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `accountStatusId` bigint(20) NOT NULL,
  `lastLoginDate` datetime DEFAULT NULL,
  `loginAttempts` int(11) DEFAULT 0,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `roleId` bigint(20) NOT NULL,
  `employeeId` bigint(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_userInfoUsername` (`username`),
  UNIQUE KEY `UK_userInfoEmployeeId` (`employeeId`),
  CONSTRAINT `userInfoRole_Ref` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`),
  CONSTRAINT `userInfoEmployee_Ref` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`),
  CONSTRAINT `userInfoAccountStatus_Ref` FOREIGN KEY (`accountStatusId`) REFERENCES `employeeStatus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userInfo`
--

LOCK TABLES `userInfo` WRITE;
/*!40000 ALTER TABLE `userInfo` DISABLE KEYS */;
INSERT INTO `transys`.`userInfo` 
(`id`, `created_at`, `created_by`, `employeeId`, `accountStatusId`, `lastLoginDate`, `password`, `username`, `roleId`, `delete_flag`) 
VALUES ('1', '2012-03-16 14:25:34',1, 1, 1, '2015-09-19 12:41:30', '$2a$10$9Jz3jhE0r8YDBGOrVficE.QyU277kdCCcOdyl4Smw4dzc2838hnku', 'admin', 1, '1');
INSERT INTO `transys`.`userInfo` 
(`id`, `created_at`, `created_by`, `employeeId`, `accountStatusId`, `lastLoginDate`, `password`, `username`, `roleId`, `delete_flag`) 
VALUES ('2', '2012-03-16 14:25:34',1, 5, 1, '2015-09-19 12:41:30', '$2a$10$QLEcB0yKXegpSdlNrAGW7.rFRZm4tFkEDoGkG.Jg.k2hoTtCgBrYi', 'aldoval', 6, '1');
INSERT INTO `transys`.`userInfo` 
(`id`, `created_at`, `created_by`, `employeeId`, `accountStatusId`,`lastLoginDate`, `password`, `username`, `roleId`, `delete_flag`) 
VALUES ('3', '2012-03-16 14:25:34',1, 4, 1, '2015-09-19 12:41:30', '$2a$10$Did5mpz3ROVMNa9ZQemgwObia5AuGs8iNmMw0wxIFHb39/CCL2cki', 'scotteak', 6, '1');

/*!40000 ALTER TABLE `userInfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `permitNotes`
--

DROP TABLE IF EXISTS `permitNotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitNotes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permitId` bigint(20) NOT NULL,
  `notes` varchar(500) NOT NULL,
  `notesType` VARCHAR(10) NOT NULL,
  `entered_by` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `permitNotesPermitRef_Idx` (`permitId`),
  CONSTRAINT `permitNotesPermitRef` FOREIGN KEY (`permitId`) REFERENCES `permit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitNotes`
--

LOCK TABLES `permitNotes` WRITE;
/*!40000 ALTER TABLE `permitNotes` DISABLE KEYS */;
/*!40000 ALTER TABLE `permitNotes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `materialType`
--

DROP TABLE IF EXISTS `materialType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `materialType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `materialCategoryId` bigint(20) NOT NULL,
  `materialName` varchar(50) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_materialTypeMaterialName` (`materialName`),
  CONSTRAINT `materialTypeMaterialCategoryRef` FOREIGN KEY (`materialCategoryId`) REFERENCES `materialCategory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materialType`
--

LOCK TABLES `materialType` WRITE;
/*!40000 ALTER TABLE `materialType` DISABLE KEYS */;
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (1,1,'Concrete',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (2,1,'Dirt',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (3,1,'Bricks',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (4,2,'Cardboard',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (5,2,'Carpet',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialType` (`id`,`materialCategoryId`,`materialName`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (6,3,'Flat Roofing',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
/*!40000 ALTER TABLE `materialType` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


--
-- Table structure for table `materialCategory`
--

DROP TABLE IF EXISTS `materialCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `materialCategory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(100) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_materialCategoryMaterialCategory` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materialCategory`
--

LOCK TABLES `materialCategory` WRITE;
/*!40000 ALTER TABLE `materialCategory` DISABLE KEYS */;
INSERT INTO `materialCategory` (`id`,`category`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (1,'Clean Construction Demolition Debris (CCDD)',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialCategory` (`id`,`category`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (2,'Contruction/Demolition Debris (C&D)',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `materialCategory` (`id`,`category`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (3,'Roofing',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
/*!40000 ALTER TABLE `materialCategory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `dumpsterSize`
--

DROP TABLE IF EXISTS `dumpsterSize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpsterSize` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `size` varchar(25) NOT NULL,
  `permitClassId` bigint(20) NOT NULL,
  `comments` varchar(20) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dumpsterSizeDumpsterSize` (`size`),
  CONSTRAINT `dumpsterSizePermitClassRef` FOREIGN KEY (`permitClassId`) REFERENCES `permitClass` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterSize`
--

LOCK TABLES `dumpsterSize` WRITE;
/*!40000 ALTER TABLE `dumpsterSize` DISABLE KEYS */;
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (1,'6 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (2,'10 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (3,'15 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (4,'20 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (5,'25 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (6,'30 yd',1,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
INSERT INTO `dumpsterSize` (`id`,`size`,`permitClassId`,`comments`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (7,'40 yd',2,NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `paymentMethodType`
--

DROP TABLE IF EXISTS `paymentMethodType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentMethodType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `method` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_paymentMethodTypePaymentMethod` (`method`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentMethodType`
--

LOCK TABLES `paymentMethodType` WRITE;
/*!40000 ALTER TABLE `paymentMethodType` DISABLE KEYS */;
INSERT INTO `paymentMethodType` VALUES (1,'Company Check',NULL,'2012-03-16 14:25:34',1,'2015-09-24 22:51:23',1,1),
(2,'Cash',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1),
(3,'Credit Card',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1),
(4,'Charge',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1),
(5,'Money Order',NULL,'2012-03-16 14:25:34',1,NULL,NULL,1);
/*!40000 ALTER TABLE `paymentMethodType` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `dumpsterPrice`
--

DROP TABLE IF EXISTS `dumpsterPrice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpsterPrice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dumpsterSizeId` bigint(20) NOT NULL,
  `materialCategoryId` bigint(20) NOT NULL,
  `materialTypeId` bigint(20) DEFAULT NULL,
  `customerId` bigint(20) DEFAULT NULL,
  `price` decimal(6,2) NOT NULL,
  `tonnageFee` decimal(6,2) DEFAULT 0.00,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `dumpsterPriceMaterialCategoryRef_idx` (`materialCategoryId`),
  KEY `dumpsterPriceMaterialTypeRef_idx` (`materialTypeId`),
  CONSTRAINT `dumpsterPriceMaterialCategoryRef` FOREIGN KEY (`materialCategoryId`) REFERENCES `materialCategory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dumpsterPriceMaterialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `materialType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dumpsterPriceDumpsterSizeRef` FOREIGN KEY (`dumpsterSizeId`) REFERENCES `dumpsterSize` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dumpsterPriceCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterPrice`
--

LOCK TABLES `dumpsterPrice` WRITE;
/*!40000 ALTER TABLE `dumpsterPrice` DISABLE KEYS */;
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (5,3,6,NULL,NULL,118.00,33.29,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 04:37:38',1,'2017-01-28 11:08:07',1,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (6,1,1,NULL,NULL,240.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 04:58:45',1,'2017-01-28 11:09:26',1,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (7,2,1,NULL,NULL,300.00,NULL,'','2016-01-09 00:00:00','2018-12-31 00:00:00','2017-01-28 05:02:01',1,'2017-01-28 06:16:35',1,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (8,4,6,NULL,NULL,118.00,33.29,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 05:55:24',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (9,5,6,NULL,NULL,118.00,33.29,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 05:56:48',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (10,6,6,NULL,NULL,118.00,33.29,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:11:31',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (11,1,2,NULL,NULL,240.00,NULL,'','2017-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:21:34',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (12,2,2,NULL,NULL,300.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:23:32',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (13,3,2,NULL,NULL,350.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:24:32',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (14,4,2,NULL,NULL,390.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:26:49',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (15,5,2,NULL,NULL,440.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:28:08',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (16,6,2,NULL,NULL,470.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:29:02',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (17,7,2,NULL,NULL,630.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:30:51',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (18,1,3,6,NULL,320.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:36:03',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (19,1,3,35,NULL,270.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:36:48',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (20,2,3,6,NULL,380.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:37:52',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (21,2,3,35,NULL,330.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:38:30',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (22,3,3,6,NULL,430.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:39:13',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (23,3,3,35,NULL,380.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:39:54',1,NULL,NULL,1);
INSERT INTO `dumpsterPrice` (`id`,`dumpsterSizeId`,`materialCategoryId`,`materialTypeId`,`customerId`,`price`,`tonnageFee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (24,4,3,35,NULL,430.00,NULL,'','2016-01-01 00:00:00','2018-12-31 00:00:00','2017-01-28 06:41:02',1,NULL,NULL,1);

/*!40000 ALTER TABLE `dumpsterPrice` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `customerDumpsterPrice`
--

DROP TABLE IF EXISTS `customerDumpsterPrice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customerDumpsterPrice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customerId` bigint(20) NOT NULL,
  `dumpsterSizeId` bigint(20) NOT NULL,
  `materialTypeId` bigint(20) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `customerDumpsterPriceCustomerRef_idx` (`customerId`),
  CONSTRAINT `customerDumpsterPriceCustomerRef` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customerDumpsterPriceMaterialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `materialType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customerDumpsterPriceDumpsterSizeRef` FOREIGN KEY (`dumpsterSizeId`) REFERENCES `dumpsterSize` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerDumpsterPrice`
--

LOCK TABLES `customerDumpsterPrice` WRITE;
/*!40000 ALTER TABLE `customerDumpsterPrice` DISABLE KEYS */;
INSERT INTO `customerDumpsterPrice` VALUES 
(5,6,1,1, 300.00,NULL,'2015-09-25 11:48:14', '2025-09-25 11:48:14','2015-09-25 11:48:14',1,'2015-09-25 11:50:40',1,1);
/*!40000 ALTER TABLE `customerDumpsterPrice` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `cityFee`
--

DROP TABLE IF EXISTS `cityFee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cityFee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `suburbName` varchar(50) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cityFee`
--

LOCK TABLES `cityFee` WRITE;
/*!40000 ALTER TABLE `cityFee` DISABLE KEYS */;
INSERT INTO `cityFee` (`id`,`suburbName`,`fee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (5,'Chicago',0.00,NULL,'2015-09-25 12:31:34','2020-09-25 12:31:34','2015-09-25 12:31:34',1,'2015-09-25 12:32:00',1,1);
INSERT INTO `cityFee` (`id`,`suburbName`,`fee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (6,'Arlington Heights',150.00,NULL,'2015-09-25 12:31:34','2020-09-25 12:31:34','2015-09-25 12:31:34',1,'2015-09-25 12:32:00',1,1);
INSERT INTO `cityFee` (`id`,`suburbName`,`fee`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (7,'Bedford Park',80.00,NULL,'2015-09-25 12:31:34','2020-09-25 12:31:34','2015-09-25 12:31:34',1,'2015-09-25 12:32:00',1,1);
/*!40000 ALTER TABLE `cityFee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `overweightFee`
--

DROP TABLE IF EXISTS `overweightFee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `overweightFee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dumpsterSizeId` bigint(20) NOT NULL,
  `materialCategoryId` bigint(20) NOT NULL,
  `tonLimit` decimal(9,2) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  CONSTRAINT `overweightFeeDumpsterSizeRef` FOREIGN KEY (`dumpsterSizeId`) REFERENCES `dumpsterSize` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `overweightFeeMaterialCategoryRef` FOREIGN KEY (`materialCategoryId`) REFERENCES `materialCategory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `overweightFee`
--

LOCK TABLES `overweightFee` WRITE;
/*!40000 ALTER TABLE `overweightFee` DISABLE KEYS */;
INSERT INTO `overweightFee` VALUES 
(5,1,1,50.00, 45.00, NULL,'2015-09-25 12:31:34', '2020-09-25 12:31:34', '2015-09-25 12:31:34',1,'2015-09-25 12:32:00',1,1);
/*!40000 ALTER TABLE `overweightFee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `permitFee`
--

DROP TABLE IF EXISTS `permitFee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permitFee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permitClassId` bigint(20) NOT NULL,
  `permitTypeId` bigint(20) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `permitFeePermitClassRef_idx` (`permitClassId`),
  KEY `permitFeePermitTypeRef_idx` (`permitTypeId`),
  CONSTRAINT `permitFeePermitClassRef` FOREIGN KEY (`permitClassId`) REFERENCES `permitClass` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitFeePermitTypeRef` FOREIGN KEY (`permitTypeId`) REFERENCES `permitType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitFee`
--

LOCK TABLES `permitFee` WRITE;
/*!40000 ALTER TABLE `permitFee` DISABLE KEYS */;
INSERT INTO `permitFee` VALUES 
(5,1,1,65.00,NULL,'2015-09-25 14:46:36','2025-09-25 14:46:36','2015-09-25 14:46:36',1,'2015-09-25 15:45:42',1,1),
(6,1,2,115.00,NULL,'2015-09-25 14:46:36','2025-09-25 14:46:36','2015-09-25 15:45:56',1,NULL,NULL,1),
(7,2,1,130.00,NULL,'2015-09-25 14:46:36','2025-09-25 14:46:36','2015-09-25 15:46:19',1,NULL,NULL,1),
(8,2,2,230.00,NULL,'2015-09-25 14:46:36','2025-09-25 14:46:36','2015-09-25 15:46:37',1,NULL,NULL,1),
(9,3,2,115.00,NULL,'2016-10-01 00:00:00','2025-10-01 00:00:00','2016-10-22 06:57:26',1,NULL,NULL,1);
/*!40000 ALTER TABLE `permitFee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


--
-- Table structure for table `additionalFee`
--

DROP TABLE IF EXISTS `additionalFee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `additionalFee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(100) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `additionalFee`
--

LOCK TABLES `additionalFee` WRITE;
/*!40000 ALTER TABLE `additionalFee` DISABLE KEYS */;
INSERT INTO `additionalFee` VALUES 
(5,'Move Box',190.00,NULL,'2015-09-25 15:38:13','2025-09-25 15:38:13','2015-09-25 15:38:13',1,NULL,NULL,1);
/*!40000 ALTER TABLE `additionalFee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `publicMaterialIntake`
--

DROP TABLE IF EXISTS `publicMaterialIntake`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publicMaterialIntake` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `materialTypeId` bigint(20) NOT NULL,
  `intakeDate` datetime NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `netWeightTonnage` decimal(9,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_publicMaterialIntake` (`materialTypeId`, `intakeDate`),
  KEY `publicMaterialIntakeMaterialTypeRef_idx` (`materialTypeId`),
  CONSTRAINT `publicMaterialIntakeMaterialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `materialType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publicMaterialIntake`
--

LOCK TABLES `publicMaterialIntake` WRITE;
/*!40000 ALTER TABLE `publicMaterialIntake` DISABLE KEYS */;
INSERT INTO `publicMaterialIntake` VALUES (4,1,'2015-10-10 00:00:00',NULL,'2015-10-11 11:04:37',1,NULL,NULL,1,30.00),
(5,2,'2015-10-10 00:00:00',NULL,'2015-10-11 11:06:54',1,NULL,NULL,1,12.00);
/*!40000 ALTER TABLE `publicMaterialIntake` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `recycleLocation`
--

DROP TABLE IF EXISTS `recycleLocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recycleLocation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `materialTypeId` bigint(20) NOT NULL,
  `location` varchar(100) NOT NULL,
  `status` varchar(20) NOT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `effectiveStartDate` datetime NOT NULL,
  `effectiveEndDate` datetime NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `recycleLocationMaterialTypeRef_idx` (`materialTypeId`),
  CONSTRAINT `recycleLocationMaterialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `materialType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recycleLocation`
--

LOCK TABLES `recycleLocation` WRITE;
/*!40000 ALTER TABLE `recycleLocation` DISABLE KEYS */;
INSERT INTO `recycleLocation` (`id`,`materialTypeId`,`location`,`status`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (6,3,'location1','Active','test','2015-10-13 00:00:00','2015-10-30 00:00:00','2015-10-24 23:23:03',1,'2015-10-24 23:46:53',1,1);
INSERT INTO `recycleLocation` (`id`,`materialTypeId`,`location`,`status`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (7,1,'location2','Active','test','2015-10-08 00:00:00','2015-12-31 00:00:00','2015-10-25 00:19:08',1,NULL,NULL,1);
INSERT INTO `recycleLocation` (`id`,`materialTypeId`,`location`,`status`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (8,2,'location3','Active','test','2015-10-01 00:00:00','2015-11-30 00:00:00','2015-10-30 00:19:08',1,NULL,NULL,1);
INSERT INTO `recycleLocation` (`id`,`materialTypeId`,`location`,`status`,`comments`,`effectiveStartDate`,`effectiveEndDate`,`created_at`,`created_by`,`modified_at`,`modified_by`,`delete_flag`) VALUES (9,1,'Atkinson Landfill','Active','test','2015-10-01 00:00:00','2016-10-30 00:00:00','2015-10-30 00:19:08',1,NULL,NULL,1);
/*!40000 ALTER TABLE `recycleLocation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-11  8:42:26
