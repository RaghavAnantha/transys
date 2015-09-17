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
INSERT INTO `address` VALUES (1,NULL,NULL,NULL,NULL,5,'4818W','VAN BUREN','XYZ',1,'3344',1),(3,NULL,NULL,NULL,NULL,5,'E Lemon st','Lemon st','Chicago',1,'28262',1);
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
  `chargeCompany` int(11) DEFAULT '1',
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
INSERT INTO `customer` VALUES (5,'2015-09-10 21:22:45',1,NULL,NULL,NULL,'adf',NULL,NULL,'sdfdsf','df','Raghav','1234567890','Raghav','1234567890','34',4,NULL,NULL,NULL,NULL,1),(6,'2015-09-11 21:05:43',1,NULL,NULL,NULL,'line',NULL,NULL,'line2','Il','Bharat','1234567890','Bharat','1234567890','22323',5,NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dumpsterInfo`
--

DROP TABLE IF EXISTS `dumpsterInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dumpsterInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dumpsterSize` varchar(5) DEFAULT NULL,
  `dumpsterPrice` double DEFAULT NULL,
  `maxWeight` int(20) DEFAULT NULL,
  `overWeightPrice` double DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dumpsterInfo`
--

LOCK TABLES `dumpsterInfo` WRITE;
/*!40000 ALTER TABLE `dumpsterInfo` DISABLE KEYS */;
INSERT INTO `transys`.`dumpsterInfo` (`id`, `dumpsterSize`, `dumpsterPrice`) VALUES ('2', '20 yd', '20');
/*!40000 ALTER TABLE `dumpsterInfo` ENABLE KEYS */;
UNLOCK TABLES;

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
  `jobTitle` varchar(100) DEFAULT NULL,
  `address` tinytext,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `transys`.`employee` (`id`, `fname`, `lname`, `address`, `city`, `state`, `zip`) VALUES ('1', 'Raghav', 'Anantha', '123', 'Chicago', '1', '28262');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trans_order`
--

DROP TABLE IF EXISTS `trans_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trans_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `custID` bigint(11) NOT NULL,
  `deliveryContactName` varchar(150) DEFAULT NULL,
  `deliveryContactPhone1` varchar(25) DEFAULT NULL,
  `deliveryContactPhone2` varchar(25) DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `deliveryTimeFrom` datetime DEFAULT NULL,
  `deliveryAddress` bigint(20) DEFAULT NULL,
  `dumpsterLocation` varchar(50) DEFAULT NULL,
  `dumpsterSize` varchar(5) DEFAULT NULL,
  `typeOfMaterial` varchar(50) DEFAULT NULL,
  -- `paymentInfo` bigint(20) DEFAULT NULL,
  -- `notes` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  -- `weightInfo` bigint(20) DEFAULT '0',
  `dumpsterNum` varchar(20) DEFAULT NULL,
  -- `driverInfo` bigint(20) DEFAULT NULL,
  `pickupDate` datetime DEFAULT NULL,
  `orderStatus` bigint(20) DEFAULT NULL,
  -- `permits` bigint(20) DEFAULT NULL,
  `deliveryTimeTo` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key1_idx` (`custID`),
  KEY `deliveryAddressRef_idx` (`deliveryAddress`),
  KEY `orderStatusRef_idx` (`orderStatus`),
  -- KEY `driverInfoRef_idx` (`driverInfo`),
  -- KEY `weightInfoRef_idx` (`weightInfo`),
  -- KEY `paymentInfoRef_idx` (`paymentInfo`),
  CONSTRAINT `customerRef` FOREIGN KEY (`custID`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deliveryAddressRef` FOREIGN KEY (`deliveryAddress`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `driverInfoRef` FOREIGN KEY (`driverInfo`) REFERENCES `orderDriverInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `orderStatusRef` FOREIGN KEY (`orderStatus`) REFERENCES `orderStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  -- CONSTRAINT `paymentInfoRef` FOREIGN KEY (`paymentInfo`) REFERENCES `orderPaymentInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `weightInfoRef` FOREIGN KEY (`weightInfo`) REFERENCES `orderWeightInfo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trans_order`
--

LOCK TABLES `trans_order` WRITE;
/*!40000 ALTER TABLE `trans_order` DISABLE KEYS */;
INSERT INTO `transys`.`trans_order`(`id`, `custID`, `deliveryContactName`, `deliveryContactPhone1`, `deliveryDate`, `deliveryAddress`, `dumpsterLocation`, `dumpsterSize`, `typeOfMaterial`, `dumpsterNum`, `pickupDate`, `orderStatus`) VALUES ('1', '5', 'Raghav', '1234567890', curdate(), '3', 'Street', '10 yd', 'Drywall', '299-87-10', curdate(), '1');	
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
-- Table structure for table `orderDriverInfo`
--

DROP TABLE IF EXISTS `orderDriverInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderDriverInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderID` bigint(20) NOT NULL,
  `pickUpDriver` bigint(20) NOT NULL,
  `dropOffDriver` bigint(20) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderDriverInfo`
--

LOCK TABLES `orderDriverInfo` WRITE;
/*!40000 ALTER TABLE `orderDriverInfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderDriverInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderNotes`
--

DROP TABLE IF EXISTS `orderNotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderNotes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderID` bigint(11) NOT NULL,
  `notes` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `orderNotesOrderRef_idx` (`orderID`),
  CONSTRAINT `orderNotesOrderRef` FOREIGN KEY (`orderID`) REFERENCES `trans_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `orderID` bigint(20) NOT NULL,
  `dumpsterPrice` double DEFAULT NULL,
  `cityFee` double DEFAULT NULL,
  `additionalFee` double DEFAULT NULL,
  `methodOfPayment` varchar(50) DEFAULT NULL,
  `reference` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  `additionalFees` double DEFAULT NULL,
  `modeOfPayment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
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
  CONSTRAINT `orderPermitRef` FOREIGN KEY (`orderID`) REFERENCES `trans_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
/*!40000 ALTER TABLE `orderStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderWeightInfo`
--

DROP TABLE IF EXISTS `orderWeightInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderWeightInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderID` bigint(20) NOT NULL,
  `grossWeight` double DEFAULT NULL,
  `netWeight` double DEFAULT NULL,
  `tare` double DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `delete_flag` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderWeightInfo`
--

LOCK TABLES `orderWeightInfo` WRITE;
/*!40000 ALTER TABLE `orderWeightInfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderWeightInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permit`
--

DROP TABLE IF EXISTS `permit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` bigint(20) NOT NULL,
  `class` bigint(20) DEFAULT NULL,
  `number` varchar(15) DEFAULT NULL,
  `fee` varchar(12) DEFAULT 0,
  `startDate` datetime DEFAULT NULL,
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
  PRIMARY KEY (`id`),
  KEY `permitStatusRef_idx` (`status`),
  KEY `customerPermitRef_idx` (`customerID`),
  KEY `deliveryAddressPermitRef_idx` (`deliveryAddress`),
  KEY `locationTypeRef_idx` (`locationType`),
  KEY `permitClassRef_idx` (`class`),
  KEY `permitTypeRef_idx` (`type`),
  CONSTRAINT `customerPermitRef` FOREIGN KEY (`customerID`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `deliveryAddressPermitRef` FOREIGN KEY (`deliveryAddress`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `locationTypeRef` FOREIGN KEY (`locationType`) REFERENCES `locationType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitClassRef` FOREIGN KEY (`class`) REFERENCES `permitClass` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitStatusRef` FOREIGN KEY (`status`) REFERENCES `permitStatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `permitTypeRef` FOREIGN KEY (`type`) REFERENCES `permitType` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permit`
--

LOCK TABLES `permit` WRITE;
/*!40000 ALTER TABLE `permit` DISABLE KEYS */;
INSERT INTO `permit` VALUES (1,1,1,'1234','50',curdate(),'1301-11W',1,2,'Test',NULL,NULL,NULL,NULL,1,5,1,NULL),(9,1,1,'6','0.0',curdate(),NULL,1,1,NULL,'2015-09-15 16:25:46',1,NULL,NULL,1,6,1,'yes'),(10,2,2,'9','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 16:28:40',1,NULL,NULL,1,6,1,'no'),(11,2,2,'123','0.0',curdate(),NULL,2,2,NULL,'2015-09-15 17:18:54',1,NULL,NULL,1,6,1,'yes'),(12,2,2,'12345','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 17:23:22',1,NULL,NULL,1,6,1,'ywa'),(13,2,2,'56','0.0',curdate(),NULL,2,1,NULL,'2015-09-15 17:28:14',1,NULL,NULL,1,5,1,'yes');
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
  `class` varchar(20) NOT NULL,
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
INSERT INTO `permitClass` VALUES (1,'CLASS A',NULL,NULL,NULL,NULL,1,NULL),(2,'CLASS B',NULL,NULL,NULL,NULL,1,NULL),(3,'DRIVE',NULL,NULL,NULL,NULL,1,NULL);
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
  `type` varchar(10) NOT NULL,
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
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
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
