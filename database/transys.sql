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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `custID` bigint(20) NOT NULL,
  `line1` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `line2` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  `zip` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `customerAddressRef_idx` (`custID`),
  KEY `stateRef_idx` (`state`),
  CONSTRAINT `addressStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `customerDeliveryAddressRef` FOREIGN KEY (`custID`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,NULL,NULL,NULL,NULL,5,'4818 W','VAN BUREN','Chicago',1,'28262',1),(3,NULL,NULL,NULL,NULL,5,'1121 E','Lemon st','Chicago',1,'28262',1);
INSERT INTO `transys`.`address` (`id`, `custID`, `line1`, `line2`, `city`, `state`, `zip`, `delete_flag`) VALUES ('4', '6', '1890', 'Chesterfield Ct', 'Chicago', '1', '28262', '1');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_object`
--

DROP TABLE IF EXISTS `business_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_object` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTION` varchar(200) DEFAULT NULL,
  `DISPLAY_TAG` varchar(60) DEFAULT NULL,
  `OBJECT_LEVEL` int(11) DEFAULT NULL,
  `OBJECT_NAME` varchar(60) DEFAULT NULL,
  `URL` varchar(5000) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `hidden` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `hierarchy` varchar(255) DEFAULT NULL,
  `url_context` varchar(255) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKF1A8A89E816B01E9` (`parent_id`),
  CONSTRAINT `FKF1A8A89E816B01E9` FOREIGN KEY (`parent_id`) REFERENCES `business_object` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_object`
--

LOCK TABLES `business_object` WRITE;
/*!40000 ALTER TABLE `business_object` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `status` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL,
  `billing_address_line1` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `billing_address_line2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `company_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `contact_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zipcode` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  `email` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `alt_phone_1` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `alt_phone_2` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `chargeCompany` varchar(5) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_fvq6be0iquj59i5x3d51959b5` (`state`),
  CONSTRAINT `stateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (5,'2015-09-10 21:22:45',1,NULL,NULL,'Active','2324 N Camelback Rd',NULL,NULL,NULL,'Chicago','Aberdeen Construction','1234567890','Raghav','1234567890','28262',1,'abc@aberdeen.com',NULL,NULL,NULL,1),(6,'2015-09-11 21:05:43',1,NULL,NULL,'Active','1321 W Main St',NULL,NULL,NULL,'Chicago','Gibbons Construction','1234567890','Bharat','1234567890','22323',1,'abc@aberdeen.com',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dumpsterInfo`
--

--
-- Table structure for table `dumpsterInfo`
--

DROP TABLE IF EXISTS `dumpsterInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpsterInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dumpsterSize` varchar(5) DEFAULT NULL,
  `dumpsterNum` varchar(50) DEFAULT NULL,
  `dumpsterPrice` double DEFAULT NULL,
  `status` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `dumpsterStatusRef_idx` (`status`),
  CONSTRAINT `dumpsterStatusRef` FOREIGN KEY (`status`) REFERENCES `dumpsterStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterInfo`
--

LOCK TABLES `dumpsterInfo` WRITE;
/*!40000 ALTER TABLE `dumpsterInfo` DISABLE KEYS */;
INSERT INTO `dumpsterInfo` VALUES (2,'20 yd','20W-113-21',20,1,NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `dumpsterInfo` ENABLE KEYS */;
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterStatus`
--

LOCK TABLES `dumpsterStatus` WRITE;
/*!40000 ALTER TABLE `dumpsterStatus` DISABLE KEYS */;
INSERT INTO `dumpsterStatus` VALUES (1,'Available',NULL,NULL,NULL,NULL,1),(2,'In-Repair',NULL,NULL,NULL,NULL,1),(3,'Dropped-Off',NULL,NULL,NULL,NULL,1);
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
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `jobTitle` bigint(20) DEFAULT NULL,
  `address` tinytext,
  `city` varchar(50) DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  `zip` varchar(12) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `hiredate` datetime DEFAULT NULL,
  `leavedate` datetime DEFAULT NULL,
  `comments` longtext,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(12) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `status` bigint(20) DEFAULT NULL,
  `employeeID` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `employeeID_UNIQUE` (`employeeID`),
  KEY `jobTitleRef_idx` (`jobTitle`),
  KEY `stateRef_idx` (`state`),
  KEY `employeeStatusRef_idx` (`status`),
  CONSTRAINT `employeeStateRef` FOREIGN KEY (`state`) REFERENCES `state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `employeeStatusRef` FOREIGN KEY (`status`) REFERENCES `employeeStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `jobTitleRef` FOREIGN KEY (`jobTitle`) REFERENCES `jobTitle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Raghav','Anantha',1,'123','Chicago',1,'28262','','','2015-09-02 00:00:00','2015-09-30 00:00:00',NULL,NULL,NULL,NULL,NULL,'2015-09-24 14:39:45',1,1,1,'007'),(2,'Kasia','Figura',5,'5521 Milwaukee','Chicago',1,'60630','773-987-2221','kasiaFigura@gmail.com','2015-09-01 00:00:00','2015-09-29 00:00:00',NULL,NULL,NULL,'2015-09-24 12:21:31',1,NULL,NULL,1,NULL,'123'),(4,'Scott','Eaker',2,'','',1,'','','kasiaFigura@gmail.com','2015-09-01 00:00:00','2015-09-14 00:00:00',NULL,NULL,NULL,'2015-09-24 12:28:04',1,NULL,NULL,1,NULL,'234'),(5,'Aldo','Valazquez',7,'','',1,'','','','2015-09-01 00:00:00','2015-09-29 00:00:00',NULL,NULL,NULL,'2015-09-24 12:40:41',1,NULL,NULL,1,1,'345');
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employeeStatus`
--

LOCK TABLES `employeeStatus` WRITE;
/*!40000 ALTER TABLE `employeeStatus` DISABLE KEYS */;
INSERT INTO `employeeStatus` VALUES (1,'Active',NULL,NULL,NULL,NULL,1),(2,'Inactive',NULL,NULL,NULL,NULL,1);
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `jobTitle` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobTitle`
--

LOCK TABLES `jobTitle` WRITE;
/*!40000 ALTER TABLE `jobTitle` DISABLE KEYS */;
INSERT INTO `jobTitle` VALUES (1,NULL,NULL,NULL,NULL,'President',1),(2,NULL,NULL,NULL,NULL,'Roll-Off Driver',1),(3,NULL,NULL,NULL,NULL,'Semi Driver',1),(4,NULL,NULL,NULL,NULL,'Driver',1),(5,NULL,NULL,NULL,NULL,'Receptionist',1),(6,NULL,NULL,NULL,NULL,'Office',1),(7,NULL,NULL,NULL,NULL,'Roll-Off Truck Driver',1),(8,NULL,NULL,NULL,NULL,'Representative',1);
/*!40000 ALTER TABLE `jobTitle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `trans_order`
--

DROP TABLE IF EXISTS `trans_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trans_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custID` bigint(20) NOT NULL,
  `deliveryContactName` varchar(150) DEFAULT NULL,
  `deliveryContactPhone1` varchar(25) DEFAULT NULL,
  `deliveryContactPhone2` varchar(25) DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `deliveryAddressId` bigint(20) DEFAULT NULL,
  `locationTypeId` bigint(20) NOT NULL,
  `dumpsterSize` varchar(5) DEFAULT NULL,
  `materialType` varchar(50) DEFAULT NULL,
  -- `dumpsterPrice` double DEFAULT NULL,
  -- `paymentInfo` bigint(20) DEFAULT NULL,
  -- `notes` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  -- `weightInfo` bigint(20) DEFAULT '0',
  `grossWeight` double DEFAULT NULL,
  `netWeightLb` double DEFAULT NULL,
  `netWeightTonnage` double DEFAULT NULL,
  `tare` double DEFAULT NULL,
  `dumpsterId` bigint(20) DEFAULT NULL,
  -- `driverInfo` bigint(20) DEFAULT NULL,
  `pickupDate` datetime DEFAULT NULL,
  `orderStatusId` bigint(20) NOT NULL,
  -- `permits` bigint(20) DEFAULT NULL,
  `pickUpDriverId` bigint(20) DEFAULT NULL,
  `dropOffDriverId` bigint(20) DEFAULT NULL,
  `deliveryHourFrom` varchar(10) DEFAULT NULL,
  `deliveryHourTo` varchar(10) DEFAULT NULL,
  `deliveryMinutesFrom` varchar(5) DEFAULT NULL,
  `deliveryMinutesTo` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key1_idx` (`custID`),
  -- KEY `deliveryAddressRef_idx` (`deliveryAddressId`),
  -- KEY `orderStatusRef_idx` (`orderStatusId`),
  -- KEY `driverInfoRef_idx` (`driverInfo`),
  -- KEY `weightInfoRef_idx` (`weightInfo`),
  -- KEY `paymentInfoRef_idx` (`paymentInfo`),
  CONSTRAINT `customerRef` FOREIGN KEY (`custID`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `maerialTypeRef` FOREIGN KEY (`materialTypeId`) REFERENCES `material_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deliveryAddressRef` FOREIGN KEY (`deliveryAddressId`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `driverInfoRef` FOREIGN KEY (`driverInfo`) REFERENCES `orderDriverInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderStatusRef` FOREIGN KEY (`orderStatusId`) REFERENCES `orderStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `paymentInfoRef` FOREIGN KEY (`paymentInfo`) REFERENCES `orderPaymentInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `weightInfoRef` FOREIGN KEY (`weightInfo`) REFERENCES `orderWeightInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  CONSTRAINT `dumpsterRef` FOREIGN KEY (`dumpsterId`) REFERENCES `dumpsterInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dumpsterLocationRef` FOREIGN KEY (`locationTypeId`) REFERENCES `locationType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pickupDriverRef` FOREIGN KEY (`pickUpDriverId`) REFERENCES `user_info` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `dropOffDriverRef` FOREIGN KEY (`dropOffDriverId`) REFERENCES `user_info` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trans_order`
--

LOCK TABLES `trans_order` WRITE;
/*!40000 ALTER TABLE `trans_order` DISABLE KEYS */;
INSERT INTO `transys`.`trans_order` (`id`, `custID`, `deliveryContactName`, `deliveryContactPhone1`, `deliveryContactPhone2`, `deliveryDate`, `deliveryAddressId`, `locationTypeId`, `dumpsterSize`, `materialType`, `created_at`, `delete_flag`, `grossWeight`, `netWeightLb`, `netWeightTonnage`, `tare`, `dumpsterId`, `pickupDate`, `orderStatusId` , `created_by`) VALUES (1, 5, 'Raghav', '123-456-7890', '123-456-7890', curdate(), 3, 1, '20 yd', 1, curdate(), 1, '10.0', '10.0', '10.0', '10.0', 2, curdate(), 1, 1);
/*!40000 ALTER TABLE `trans_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationType`
--

DROP TABLE IF EXISTS `locationType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `locationType` varchar(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationType`
--

LOCK TABLES `locationType` WRITE;
/*!40000 ALTER TABLE `locationType` DISABLE KEYS */;
INSERT INTO `locationType` VALUES (1,'Alley',NULL,NULL,NULL,NULL,1),(2,'Curb',NULL,NULL,NULL,NULL,1),(3,'Drive',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `locationType` ENABLE KEYS */;
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
  `notes` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `orderNotesOrderRef_idx` (`orderId`),
  CONSTRAINT `orderNotesOrderRef` FOREIGN KEY (`orderId`) REFERENCES `trans_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Table structure for table `orderPaymentInfo`
--

DROP TABLE IF EXISTS `orderPaymentInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderPaymentInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderId` bigint(20) NOT NULL,
  `dumpsterPrice` double DEFAULT NULL,
  `cityFee` double DEFAULT NULL,
  `permitFees` double DEFAULT NULL,
  `overweightFee` double DEFAULT NULL,
  `additionalFee` double DEFAULT NULL,
  `paymentMethod` varchar(50) DEFAULT NULL,
  `ccReferenceNum` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `totalFees` double DEFAULT NULL,
  `checkNum` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `orderPaymentOrderRef` FOREIGN KEY (`orderId`) REFERENCES `trans_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderPaymentInfo`
--

LOCK TABLES `orderPaymentInfo` WRITE;
/*!40000 ALTER TABLE `orderPaymentInfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderPaymentInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderPermits`
--

DROP TABLE IF EXISTS `orderPermits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderPermits` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderID` bigint(20) NOT NULL,
  `permitID` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `orderPermitRef_idx` (`orderID`),
  KEY `permitRef_idx` (`permitID`),
  CONSTRAINT `orderPermitOrderRef` FOREIGN KEY (`orderID`) REFERENCES `trans_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitRef` FOREIGN KEY (`permitID`) REFERENCES `permit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderPermits`
--

LOCK TABLES `orderPermits` WRITE;
/*!40000 ALTER TABLE `orderPermits` DISABLE KEYS */;
INSERT INTO `orderPermits` VALUES (3,1,1,NULL,NULL,NULL,NULL,1);
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderStatus`
--

LOCK TABLES `orderStatus` WRITE;
/*!40000 ALTER TABLE `orderStatus` DISABLE KEYS */;
INSERT INTO `transys`.`orderstatus` (`id`, `status`) VALUES ('1', 'Open');
INSERT INTO `orderStatus` VALUES (2,'Dropped-off',NULL,NULL,NULL,NULL,1),(3,'Picked Up',NULL,NULL,NULL,NULL,1),(4,'Closed',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `orderStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permit`
--

DROP TABLE IF EXISTS `permit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permitType` bigint(20) NOT NULL,
  `number` varchar(15) DEFAULT NULL,
  `fee` double DEFAULT '0',
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `permitAddress` varchar(100) DEFAULT NULL,
  `locationType` bigint(20) DEFAULT NULL,
  `status` bigint(20) NOT NULL DEFAULT '1',
  `comments` mediumtext,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `customerID` bigint(20) DEFAULT NULL,
  `deliveryAddress` bigint(20) NOT NULL,
  `parkingMeter` varchar(3) DEFAULT NULL,
  `parkingMeterFee` double DEFAULT '0',
  `orderID` bigint(20) DEFAULT NULL,
  `permitClass` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `permitStatusRef_idx` (`status`),
  KEY `customerPermitRef_idx` (`customerID`),
  KEY `deliveryAddressPermitRef_idx` (`deliveryAddress`),
  KEY `locationTypeRef_idx` (`locationType`),
  KEY `permitTypeRef_idx` (`permitType`),
  KEY `FK_b8158603jnlm5s4u1kuyxecl9` (`permitClass`),
  CONSTRAINT `FK_b8158603jnlm5s4u1kuyxecl9` FOREIGN KEY (`permitClass`) REFERENCES `permitClass` (`id`),
  CONSTRAINT `customerPermitRef` FOREIGN KEY (`customerID`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deliveryAddressPermitRef` FOREIGN KEY (`deliveryAddress`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `locationTypeRef` FOREIGN KEY (`locationType`) REFERENCES `locationType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitStatusRef` FOREIGN KEY (`status`) REFERENCES `permitStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitTypeRef` FOREIGN KEY (`permitType`) REFERENCES `permitType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permit`
--

LOCK TABLES `permit` WRITE;
/*!40000 ALTER TABLE `permit` DISABLE KEYS */;
INSERT INTO `permit` VALUES (1,1,'1301-11W',50,'2015-09-22 00:00:00','2015-09-25 00:00:00','4818W VAN BUREN',1,2,NULL,'2015-09-22 00:00:00',NULL,'2015-09-22 21:51:56',1,1,5,1,'Yes',0,NULL,1),(2,1,'5667890',90,'2015-09-22 00:00:00','2015-09-25 00:00:00',NULL,1,2,NULL,'2015-09-23 12:27:14',1,NULL,NULL,1,6,4,'Yes',90,NULL,1);
-- INSERT INTO `permit` VALUES (1,1,1,'1234','50',curdate(),'1301-11W',1,2,'Test',NULL,NULL,NULL,NULL,1,5,1,NULL);
-- ,(9,1,1,'6','0.0',curdate(),NULL,1,1,NULL,'2015-09-15 16:25:46',1,NULL,NULL,1,6,1,'yes')
-- ,(10,2,2,'9','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 16:28:40',1,NULL,NULL,1,6,1,'no')
-- ,(11,2,2,'123','0.0',curdate(),NULL,2,2,NULL,'2015-09-15 17:18:54',1,NULL,NULL,1,6,1,'yes')
-- ,(12,2,2,'12345','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 17:23:22',1,NULL,NULL,1,6,1,'ywa')
-- ,(13,2,2,'56','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 17:28:14',1,NULL,NULL,1,5,1,'yes');
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `permitClass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitClass`
--

LOCK TABLES `permitClass` WRITE;
/*!40000 ALTER TABLE `permitClass` DISABLE KEYS */;
INSERT INTO `permitClass` VALUES (1,NULL,NULL,NULL,NULL,1,'CLASS A'),(2,'2015-09-22 13:47:10',NULL,NULL,NULL,1,'CLASS B'),(3,'2015-09-22 13:43:51',NULL,NULL,NULL,1,'CLASS C');
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitStatus`
--

LOCK TABLES `permitStatus` WRITE;
/*!40000 ALTER TABLE `permitStatus` DISABLE KEYS */;
INSERT INTO `permitStatus` VALUES (1,'Pending',NULL,NULL,NULL,NULL,1),(2,'Available',NULL,NULL,NULL,NULL,1),(3,'Assigned',NULL,NULL,NULL,NULL,1),(4,'Expired',NULL,NULL,NULL,NULL,1);
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
  `permitType` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitType`
--

LOCK TABLES `permitType` WRITE;
/*!40000 ALTER TABLE `permitType` DISABLE KEYS */;
INSERT INTO `permitType` VALUES (1,'3 days',NULL,NULL,NULL,NULL,1),(2,'30 days',NULL,NULL,NULL,NULL,1);
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
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `theme` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,NULL,NULL,NULL,NULL,1,'ADMIN',NULL,NULL),(2,NULL,NULL,NULL,NULL,1,'OPERATOR',NULL,NULL),(3,NULL,NULL,NULL,NULL,1,'REPORTUSER',NULL,NULL),(4,'2012-03-16 01:43:40',1,NULL,NULL,1,'DATA ENTRY_BILLING',NULL,NULL),(5,'2012-03-16 14:25:34',1,NULL,NULL,1,'TEST',NULL,NULL);
INSERT INTO `transys`.`role` (`id`, `default_flag`, `name`) VALUES ('6', '1', 'DRIVER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_privilege`
--

DROP TABLE IF EXISTS `role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `permission_type` int(11) DEFAULT NULL,
  `business_object_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45FBD6283F28F717` (`role_id`),
  KEY `FK45FBD628708FFC58` (`business_object_id`),
  CONSTRAINT `FK45FBD6283F28F717` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK45FBD628708FFC58` FOREIGN KEY (`business_object_id`) REFERENCES `business_object` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_privilege`
--

LOCK TABLES `role_privilege` WRITE;
/*!40000 ALTER TABLE `role_privilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `code` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,NULL,NULL,1,NULL,NULL,'IL','Illinois'),(2,NULL,NULL,1,NULL,NULL,'WI','Wisconsin'),(3,NULL,NULL,1,NULL,NULL,'IN','Indiana'),(4,NULL,NULL,1,NULL,NULL,'MI','Michigan'),(5,NULL,NULL,1,NULL,NULL,'OH','Ohio');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `account_status` int(11) DEFAULT NULL,
  `agree_terms` tinyint(4) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_attempts` int(11) DEFAULT NULL,
  `mobile_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `bill_batch_date` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK1437D8A23F28F717` (`role_id`),
  CONSTRAINT `FK1437D8A23F28F717` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,NULL,NULL,NULL,NULL,1,NULL,NULL,'admin','2015-09-10 21:32:56','admin',NULL,NULL,'admin','admin',NULL,NULL,'admin',1,NULL,1);
INSERT INTO `transys`.`user_info` (`id`, `account_status`, `first_name`, `last_login_date`, `last_name`, `name`, `password`, `username`, `role_id`, `delete_flag`) VALUES ('2', '1', 'Robert', '2015-09-19 12:41:30', 'De La Rosa', 'Robert De La Rosa', 'robert', 'robert', '6', '1');
INSERT INTO `transys`.`user_info` (`id`, `account_status`, `first_name`, `last_login_date`, `last_name`, `name`, `password`, `username`, `role_id`, `delete_flag`) VALUES ('3', '1', 'Thomas', '2015-09-19 12:41:30', 'De Silva', 'Thomas de Silva', 'thomas', 'thomas', '6', '1');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
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
  `permitID` bigint(20) NOT NULL,
  `notes` varchar(250) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `permitNotesRef` (`permitID`),
  CONSTRAINT `permitNotesRef` FOREIGN KEY (`permitID`) REFERENCES `permit` (`id`)
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
-- Table structure for table `materialtype`
--

DROP TABLE IF EXISTS `materialtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `materialtype` (
  `id` bigint(20) NOT NULL,
  `typeName` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materialtype`
--

LOCK TABLES `materialtype` WRITE;
/*!40000 ALTER TABLE `materialtype` DISABLE KEYS */;
INSERT INTO `materialtype` VALUES (1,'Concrete',NULL,NULL,NULL,NULL,1),(2,'Dirt',NULL,NULL,NULL,NULL,1),(3,'Bricks',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `materialtype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

--
-- Table structure for table `paymentMethod`
--

DROP TABLE IF EXISTS `paymentMethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentMethod` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `method` varchar(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentMethod`
--

LOCK TABLES `paymentMethod` WRITE;
/*!40000 ALTER TABLE `paymentMethod` DISABLE KEYS */;
INSERT INTO `paymentMethod` VALUES (1,'Company Check',NULL,NULL,'2015-09-24 22:51:23',1,1),(2,'Cash',NULL,NULL,NULL,NULL,1),(3,'Credit Card',NULL,NULL,NULL,NULL,1),(4,'Charge',NULL,NULL,NULL,NULL,1),(5,'Money Order',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `paymentMethod` ENABLE KEYS */;
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
  `dumpsterSize` varchar(5) NOT NULL,
  `materialType` bigint(20) NOT NULL,
  `price` decimal(6,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `materialTypeDumpsterPriceRef_idx` (`materialType`),
  CONSTRAINT `materialTypeDumpsterPriceRef` FOREIGN KEY (`materialType`) REFERENCES `materialtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterPrice`
--

LOCK TABLES `dumpsterPrice` WRITE;
/*!40000 ALTER TABLE `dumpsterPrice` DISABLE KEYS */;
INSERT INTO `dumpsterPrice` VALUES (1,'6 yd',1,240.00,NULL,NULL,NULL,NULL,1),(2,'20 yd',2,300.00,NULL,NULL,NULL,NULL,1);
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
  `customer` bigint(20) NOT NULL,
  `dumpsterPrice` decimal(6,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `customerDumpsterPriceRef_idx` (`customer`),
  CONSTRAINT `customerDumpsterPriceRef` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customerDumpsterPrice`
--

LOCK TABLES `customerDumpsterPrice` WRITE;
/*!40000 ALTER TABLE `customerDumpsterPrice` DISABLE KEYS */;
INSERT INTO `customerDumpsterPrice` VALUES (5,5,300.00,'2015-09-25 11:48:14',1,'2015-09-25 11:50:40',1,1);
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
  `city` varchar(40) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
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
INSERT INTO `cityFee` VALUES (5,'Chicago',35.00,'2015-09-25 12:31:34',1,'2015-09-25 12:32:00',1,1);
/*!40000 ALTER TABLE `cityFee` ENABLE KEYS */;
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
  `permitClass` bigint(20) NOT NULL,
  `permitType` bigint(20) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueClassType` (`permitClass`,`permitType`),
  KEY `permitClassFeeRef_idx` (`permitClass`),
  KEY `permitTypeFeeRef_idx` (`permitType`),
  CONSTRAINT `permitClassFeeRef` FOREIGN KEY (`permitClass`) REFERENCES `permitClass` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitTypeFeeRef` FOREIGN KEY (`permitType`) REFERENCES `permitType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permitFee`
--

LOCK TABLES `permitFee` WRITE;
/*!40000 ALTER TABLE `permitFee` DISABLE KEYS */;
INSERT INTO `permitFee` VALUES (5,1,1,65.00,'2015-09-25 14:46:36',1,'2015-09-25 15:45:42',1,1),(6,1,2,115.00,'2015-09-25 15:45:56',1,NULL,NULL,1),(7,2,1,130.00,'2015-09-25 15:46:19',1,NULL,NULL,1),(8,2,2,230.00,'2015-09-25 15:46:37',1,NULL,NULL,1);
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
  `description` varchar(40) NOT NULL,
  `fee` decimal(6,2) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
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
INSERT INTO `additionalFee` VALUES (5,'Move Box',190.00,'2015-09-25 15:38:13',1,NULL,NULL,1);
/*!40000 ALTER TABLE `additionalFee` ENABLE KEYS */;
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
