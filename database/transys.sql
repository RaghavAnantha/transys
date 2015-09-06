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
  `status` int(11) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `hidden` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `hierarchy` varchar(255) DEFAULT NULL,
  `url_context` varchar(255) DEFAULT NULL,
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
INSERT INTO `business_object` VALUES (1,'/home/home.do','LUTransport',1,'LUTransport','/home/home.do',NULL,NULL,NULL,NULL,1,1,0,NULL,'/1/',NULL),(110,'/operator/tripsheet/list.do?rst=1','Trip Sheet',2,'Trip Sheet','/operator/tripsheet/home.do,/operator/tripsheet/list.do,/operator/tripsheet/create.do,/operator/tripsheet/save.do,/operator/tripsheet/ajax.do',NULL,NULL,NULL,NULL,1,2,0,1,'/1/110/',NULL),(120,'/operator/fuellog/list.do?rst=1','Fuel Log',2,'Fuel Log','/operator/fuellog/home.do,/operator/fuellog/list.do,/operator/fuellog/create.do,/operator/fuellog/save.do,/operator/fuellog/ajax.do',NULL,NULL,NULL,NULL,1,3,0,1,'/1/120/',NULL),(140,'/operator/ticket/list.do?rst=1','Tickets',2,'Tickets','/operator/ticket/home.do,/operator/ticket/list.do,/operator/ticket/create.do,/operator/ticket/save.do,/operator/ticket/ajax.do,/operator/ticket/edit.do,/operator/ticket/delete.do,/operator/ticket/changestatus.do,/operator/ticket/search.do,,/operator/ticket/bulkdelete.do,/operator/ticket/export.do',NULL,NULL,NULL,NULL,1,4,0,1,'/1/140/',NULL),(150,'/operator/login/changepassword.do','Profile',2,'Profile','/operator/login/changepassword.do,/operator/login/updatepassword.do',NULL,NULL,NULL,NULL,1,5,0,1,'/1/150/',NULL),(201,'/admin/location/list.do?rst=1','Location',2,'Location','/admin/location/list.do,/admin/location/create.do,/admin/location/edit.do,/admin/location/delete.do,/admin/location/save.do,/admin/location/search.do,/admin/location/export.do',NULL,NULL,NULL,NULL,1,5,0,1,'/1/201/',NULL),(202,'/admin/vehicle/home.do','Vehicles',2,'Vehicles','/admin/vehicle/home.do',NULL,NULL,NULL,NULL,1,6,0,1,'/1/202/',NULL),(203,'/admin/driver/list.do?rst=1','Parties',2,'Parties','/admin/driver/list.do,/admin/driver/create.do,/admin/driver/edit.do,/admin/driver/delete.do,/admin/driver/save.do,/admin/driver/search.do,/admin/driver/export.do',NULL,NULL,NULL,NULL,1,2,0,1,'/1/203/',NULL),(204,'/admin/access/home.do','Access',2,'Access','/admin/access/home.do',NULL,NULL,NULL,NULL,1,7,0,1,'/1/204/',NULL),(205,'/admin/billingrate/list.do?rst=1','Rates',2,'Rates','/admin/billingrate/list.do,/admin/billingrate/create.do,/admin/billingrate/edit.do,/admin/billingrate/delete.do,/admin/billingrate/save.do,/admin/billingrate/search.do,/admin/billingrate/ajax.do,/admin/billingrate/export.do',NULL,NULL,NULL,NULL,1,3,0,1,'/1/205/',NULL),(206,'/admin/staticdata/list.do?rst=1','Static Data',2,'Static Data','/admin/staticdata/list.do,/admin/staticdata/delete.do,/admin/staticdata/save.do,/admin/staticdata/edit.do,/admin/staticdata/create.do,/admin/staticdata/export.do,,/admin/staticdata/search.do',NULL,NULL,NULL,NULL,1,8,0,1,'/1/206/',NULL),(301,'/reportuser/report/billing/start.do','Reports',2,'Reports','/reportuser/report/home.do',NULL,NULL,NULL,NULL,1,2,0,1,'/1/301/',NULL),(1401,'/operator/ticket/list.do?rst=1','Manage Ticket',3,'Manage Ticket','/operator/ticket/home.do,/operator/ticket/list.do,/operator/ticket/create.do,/operator/ticket/save.do,/operator/ticket/ajax.do,/operator/ticket/edit.do,/operator/ticket/delete.do,/operator/ticket/changestatus.do,/operator/ticket/search.do',NULL,NULL,NULL,NULL,1,1,0,140,'/1/140/1401/',NULL),(1402,'/uploadData/ticket.do','Upload Ticket',3,'Upload Ticket','/uploadData/ticket.do,/uploadData/ticket/upload.do',NULL,NULL,NULL,NULL,1,2,0,140,'/1/140/1402/',NULL),(1403,'/operator/ticket/deleteAll.do','Delete Tickets',3,'Delete Tickets','/operator/ticket/deleteAll.do',NULL,NULL,NULL,NULL,1,3,0,140,'/1/140/1403/',NULL),(2011,'/admin/location/list.do?rst=1','Manage Location',3,'Manage Location','/admin/location/list.do,/admin/location/create.do,/admin/location/edit.do,/admin/location/delete.do,/admin/location/save.do,/admin/location/search.do',NULL,NULL,NULL,NULL,1,1,0,201,'/1/201/2011/',NULL),(2021,'/admin/vehicle/list.do?rst=1','Manage Vehicles',3,'Manage Vehicles','/admin/vehicle/list.do,/admin/vehicle/create.do,/admin/vehicle/edit.do,/admin/vehicle/delete.do,/admin/vehicle/save.do,/admin/vehicle/search.do,/admin/vehicle/export.do',NULL,NULL,NULL,NULL,1,1,0,202,'/1/202/2021/',NULL),(2022,'/admin/vehiclepermit/list.do?rst=1','Vehicle Permit',3,'Vehicle Permit','/admin/vehiclepermit/list.do,/admin/vehiclepermit/create.do,/admin/vehiclepermit/edit.do,/admin/vehiclepermit/delete.do,/admin/vehiclepermit/save.do,/admin/vehiclepermit/search.do,/admin/vehiclepermit/export.do',NULL,NULL,NULL,NULL,1,2,0,202,'/1/202/2022/',NULL),(2023,'/admin/vehicletolltag/list.do?rst=1','Vehicle Toll Tag',3,'Vehicle Toll Tag','/admin/vehicletolltag/list.do,/admin/vehicletolltag/create.do,/admin/vehicletolltag/edit.do,/admin/vehicletolltag/delete.do,/admin/vehicletolltag/save.do,/admin/vehicletolltag/search.do,/admin/vehicletolltag/export.do',NULL,NULL,NULL,NULL,1,3,0,202,'/1/202/2023/',NULL),(2031,'/admin/driver/list.do?rst=1','Drivers',3,'Drivers','/admin/driver/list.do,/admin/driver/create.do,/admin/driver/edit.do,/admin/driver/delete.do,/admin/driver/save.do,/admin/driver/search.do,/admin/driver/export.do',NULL,NULL,NULL,NULL,1,1,0,203,'/1/203/2031/',NULL),(2032,'/admin/subcontractor/list.do?rst=1','Subcontractors',3,'Subcontractors','/admin/subcontractor/list.do,/admin/subcontractor/create.do,/admin/subcontractor/edit.do,/admin/subcontractor/delete.do,/admin/subcontractor/save.do,/admin/subcontractor/search.do,/admin/subcontractor/export.do',NULL,NULL,NULL,NULL,1,1,0,203,'/1/203/2032/',NULL),(2041,'/admin/access/user/list.do?rst=1','Manage Users',3,'Manage Users','/admin/access/user/list.do,/admin/access/user/create.do,/admin/access/user/save.do,/admin/access/user/edit.do,/admin/access/user/delete.do,/admin/access/user/uploadImage.do,/admin/access/user/removeImage.do,/admin/access/user/search.do',NULL,NULL,NULL,NULL,1,1,0,204,'/1/204/2041/',NULL),(2042,'/admin/access/role/list.do?rst=1','Manage Roles',3,'Manage Roles','/admin/access/role/list.do,/admin/access/role/create.do,/admin/access/role/save.do,/admin/access/role/edit.do,/admin/access/role/delete.do,/admin/access/role/uploadImage.do,/admin/access/role/removeImage.do,/admin/access/role/search.do',NULL,NULL,NULL,NULL,1,2,0,204,'/1/204/2042/',NULL),(2043,'/admin/access/roleprivilege/list.do?rst=1','Manage Permission',3,'Manage Permission','/admin/access/roleprivilege/list.do,/admin/access/roleprivilege/create.do,/admin/access/roleprivilege/save.do,/admin/access/roleprivilege/edit.do,/admin/access/roleprivilege/delete.do,/admin/access/roleprivilege/search.do',NULL,NULL,NULL,NULL,1,3,1,204,'/1/204/2043/',NULL),(2050,'/admin/companycategory/list.do?rst=1','Manage Company Category',3,'Manage Company Category','/admin/companycategory/list.do,/admin/companycategory/edit.do,/admin/companycategory/create.do,/admin/companycategory/save.do,/admin/companycategory/search.do,/admin/companycategory/delete.do',NULL,NULL,NULL,NULL,1,1,0,205,'/1/205/2050/',NULL),(2051,'/admin/billingrate/list.do?rst=1','Manage Billing Rate',3,'Manage Billing Rate','/admin/billingrate/list.do,/admin/billingrate/create.do,/admin/billingrate/edit.do,/admin/billingrate/delete.do,/admin/billingrate/save.do,/admin/billingrate/search.do,/admin/billingrate/ajax.do',NULL,NULL,NULL,NULL,1,2,0,205,'/1/205/2051/',NULL),(2052,'/admin/fuelsurchargepadd/list.do?rst=1','Fuel Surcharge Padd',3,'Fuel Surcharge Padd','/admin/fuelsurchargepadd/list.do,/admin/fuelsurchargepadd/create.do,/admin/fuelsurchargepadd/edit.do,/admin/fuelsurchargepadd/delete.do,/admin/fuelsurchargepadd/save.do',NULL,NULL,NULL,NULL,1,3,0,205,'/1/205/2052/',NULL),(2053,'/admin/tonnagepremium/list.do?rst=1','Tonnage Premium',3,'Tonnage Premium','/admin/tonnagepremium/list.do,/admin/tonnagepremium/create.do,/admin/tonnagepremium/edit.do,/admin/tonnagepremium/delete.do,/admin/tonnagepremium/save.do',NULL,NULL,NULL,NULL,1,3,0,205,'/1/205/2053',NULL),(2054,'/admin/fuelsurchargeweeklyrate/list.do?rst=1','Fuel Surcharge Weekly Rate',3,'Fuel Surcharge Weekly Rate','/admin/fuelsurchargeweeklyrate/list.do,/admin/fuelsurchargeweeklyrate/create.do,/admin/fuelsurchargeweeklyrate/edit.do,/admin/fuelsurchargeweeklyrate/delete.do,/admin/fuelsurchargeweeklyrate/search.do,/admin/fuelsurchargeweeklyrate/save.do',NULL,NULL,NULL,NULL,1,4,0,205,'/1/205/2054/',NULL),(2055,'/admin/subcontractorrate/list.do?rst=1','Manage Subcontractor Rate',3,'Manage Subcontractor Rate','/admin/subcontractorrate/list.do,/admin/subcontractorrate/create.do,/admin/subcontractorrate/edit.do,/admin/subcontractorrate/delete.do,/admin/subcontractorrate/save.do,/admin/subcontractorrate/search.do,/admin/subcontractorrate/ajax.do',NULL,NULL,NULL,NULL,1,5,0,205,'/1/205/2055/',NULL),(3011,'/reportuser/report/billing/start.do','Invoicing',3,'Invoicing','/reportuser/report/billing/start.do,/reportuser/report/billing/save.do,/reportuser/report/billing/search.do,/reportuser/report/billing/export.do,/reportuser/report/billing/ajax.do',NULL,NULL,NULL,NULL,1,1,0,301,'/1/301/3011/',NULL),(3012,'/reportuser/report/billinghistory/start.do','Billing History',3,'Billing History','/reportuser/report/billinghistory/start.do,/reportuser/report/billinghistory/search.do,/reportuser/report/billinghistory/export.do',NULL,NULL,NULL,NULL,1,2,0,301,'/1/301/3012/',NULL),(3013,'/admin/uploadinvoice/list.do?rst=1','Manage Invoices',3,'Manage Invoices','/admin/uploadinvoice/list.do,/admin/uploadinvoice/create.do,/admin/uploadinvoice/save.do,/admin/uploadinvoice/edit.do,/admin/uploadinvoice/delete.do,/admin/uploadinvoice/search.do,/admin/uploadInvoice/downloadinvoice.do',NULL,NULL,NULL,NULL,1,3,0,301,'/1/301/3013/',NULL),(3014,'/reportuser/report/subcontractorbilling/start.do','Subcontractor Report',3,'Subcontractor Report','/reportuser/report/subcontractorbilling/start.do,/reportuser/report/subcontractorbilling/save.do,/reportuser/report/subcontractorbilling/search.do,/reportuser/report/subcontractorbilling/export.do,/reportuser/report/subcontractorbilling/ajax.do',NULL,NULL,NULL,NULL,1,4,0,301,'/1/301/3014/',NULL);
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
  `status` int(11) DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `customer_name_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fax` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `zipcode` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fvq6be0iquj59i5x3d51959b5` (`state`),
  CONSTRAINT `FK_fvq6be0iquj59i5x3d51959b5` FOREIGN KEY (`state`) REFERENCES `state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,NULL,NULL,NULL,NULL,1,'Test Address','Customer1','Customer2',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
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
  `status` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `theme` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,NULL,NULL,NULL,NULL,1,'ADMIN',NULL),(2,NULL,NULL,NULL,NULL,1,'OPERATOR',NULL),(3,NULL,NULL,NULL,NULL,1,'REPORTUSER',NULL),(4,'2012-03-16 01:43:40',1,NULL,NULL,1,'DATA ENTRY_BILLING',NULL),(5,'2012-03-16 14:25:34',1,NULL,NULL,1,'TEST',NULL);
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
  `status` int(11) DEFAULT NULL,
  `permission_type` int(11) DEFAULT NULL,
  `business_object_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
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
INSERT INTO `role_privilege` VALUES (605,'2012-03-15 07:59:40',NULL,NULL,NULL,1,NULL,1,3),(607,'2012-03-15 07:59:40',NULL,NULL,NULL,1,NULL,301,3),(608,'2012-03-15 07:59:40',NULL,NULL,NULL,1,NULL,3011,3),(609,'2012-03-15 07:59:40',NULL,NULL,NULL,1,NULL,3012,3),(610,'2012-03-15 07:59:40',NULL,NULL,NULL,1,NULL,3013,3),(642,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,1,4),(643,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,301,4),(644,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,3011,4),(645,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,3012,4),(646,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,3013,4),(647,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,140,4),(648,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,1401,4),(649,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,1402,4),(650,'2012-03-16 10:16:07',NULL,NULL,NULL,1,NULL,150,4),(651,'2012-03-16 10:29:44',NULL,NULL,NULL,1,NULL,1,2),(652,'2012-03-16 10:29:44',NULL,NULL,NULL,1,NULL,140,2),(653,'2012-03-16 10:29:44',NULL,NULL,NULL,1,NULL,1401,2),(654,'2012-03-16 10:29:44',NULL,NULL,NULL,1,NULL,150,2),(655,'2012-03-16 14:25:50',NULL,NULL,NULL,1,NULL,1,5),(656,'2012-03-16 14:25:50',NULL,NULL,NULL,1,NULL,301,5),(657,'2012-03-16 14:25:50',NULL,NULL,NULL,1,NULL,3011,5),(658,'2012-03-16 14:25:50',NULL,NULL,NULL,1,NULL,3012,5),(659,'2012-03-16 14:25:50',NULL,NULL,NULL,1,NULL,3013,5),(689,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,1,1),(690,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,301,1),(691,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,3011,1),(692,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,3012,1),(693,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,3013,1),(694,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,203,1),(695,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2031,1),(696,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2032,1),(697,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,205,1),(698,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2051,1),(699,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2052,1),(700,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2053,1),(701,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2054,1),(702,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,140,1),(703,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,1401,1),(704,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,1402,1),(705,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,150,1),(706,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,201,1),(707,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2011,1),(708,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,202,1),(709,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2021,1),(710,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2022,1),(711,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2023,1),(712,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,204,1),(713,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2041,1),(714,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2042,1),(715,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,2043,1),(716,'2012-04-11 08:55:24',NULL,NULL,NULL,1,NULL,206,1),(717,NULL,NULL,NULL,NULL,NULL,NULL,2055,1),(718,NULL,NULL,NULL,NULL,NULL,NULL,3014,1);
/*!40000 ALTER TABLE `role_privilege` ENABLE KEYS */;
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
  `status` int(11) DEFAULT NULL,
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
INSERT INTO `user_info` VALUES (1,NULL,NULL,NULL,NULL,1,1,NULL,NULL,'admin','2012-04-19 05:14:20','admin',NULL,NULL,'admin','2011@admin',NULL,NULL,'admin',1,NULL),(2,NULL,NULL,NULL,NULL,1,1,NULL,NULL,'operator','2012-03-16 10:06:32','operator',NULL,NULL,'operator','2011@operator',NULL,NULL,'operator',2,'2011-11-26 00:00:00'),(3,NULL,NULL,NULL,NULL,1,1,NULL,NULL,'reportuser','2012-03-15 08:10:10','reportuser',NULL,NULL,'reportuser','2011@reportuser',NULL,NULL,'reportuser',3,NULL),(4,'2012-03-16 10:47:41',NULL,NULL,NULL,1,0,0,'','Sabina','2012-03-16 10:47:55','Graca',0,'','SG','sabina1','',NULL,'gracas',1,NULL),(5,'2012-03-16 01:38:38',NULL,NULL,NULL,1,1,0,'','Bozena','2012-03-16 10:41:19','Szozda',0,'','BS','bozena1','',NULL,'szozdab',2,NULL),(6,'2012-03-16 01:45:43',NULL,NULL,NULL,1,1,0,'','Ela','2012-03-16 10:28:26','Bulawa',0,'','EB','elaela1','',NULL,'bulawae',2,NULL),(8,'2012-03-16 01:35:57',NULL,NULL,NULL,1,1,0,'','Kathy','2012-04-06 15:58:52','Karistinos',0,'','KK','kathy1','',NULL,'karistinosk',2,NULL),(9,'2012-03-16 14:22:12',NULL,NULL,NULL,1,1,0,'','Maria','2012-03-16 14:24:41','Navarro',0,'','MN','maria1','',NULL,'navarrom',1,NULL),(16,'2012-03-16 14:27:55',NULL,NULL,NULL,1,0,0,'','Natalia',NULL,'Tomczak',0,'','NT','natalia1','',NULL,'tomczakn',2,NULL),(17,'2012-03-16 14:29:05',NULL,NULL,NULL,1,0,0,'','Wesli',NULL,'Bulawa',0,'','WB','wesli1','',NULL,'bulawaw',1,NULL),(18,'2012-03-22 13:17:04',NULL,NULL,NULL,1,0,0,'','John',NULL,'Wilkosz',0,'','Wilkosz','wilkosz1','',NULL,'Wilkosz',2,NULL),(19,'2012-03-22 13:17:32',NULL,NULL,NULL,1,0,0,'','Nets',NULL,'Nets',0,'','Nets','netsnets1','',NULL,'Nets',2,NULL);
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
  `status` int(11) DEFAULT NULL,
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
INSERT INTO `user_role` VALUES (1,NULL,NULL,NULL,NULL,1,1,1);
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

-- Dump completed on 2015-09-06 22:11:53
