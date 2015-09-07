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

--
-- Table structure for table `business_object`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/`transys` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `transys`;

DROP TABLE IF EXISTS `business_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_object` (
  `ID` bigint(20) NOT NULL,
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
  `state` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `alt_phone_1` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `alt_phone_2` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `chargeCompany` int(11) DEFAULT '1',
  `delete_flag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_fvq6be0iquj59i5x3d51959b5` (`state`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=719 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
  `delete_flag` int(11) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `code` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
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
  `delete_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1437D8A23F28F717` (`role_id`),
  CONSTRAINT `FK1437D8A23F28F717` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,NULL,NULL,NULL,NULL,1,NULL,NULL,'admin','2012-04-19 05:14:20','admin',NULL,NULL,'admin','admin',NULL,NULL,'admin',1,NULL,1);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `default_flag` int(11) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK143BF46A3F28F717` (`role_id`),
  KEY `FK143BF46AE453BAF7` (`user_id`),
  CONSTRAINT `FK143BF46A3F28F717` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK143BF46AE453BAF7` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-07 16:25:57
