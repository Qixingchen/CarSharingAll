CREATE DATABASE  IF NOT EXISTS `app_carsharingxmu` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `app_carsharingxmu`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: carsharing
-- ------------------------------------------------------
-- Server version	5.6.16

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
-- Table structure for table `car_info`
--

DROP TABLE IF EXISTS `car_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CarNum` varchar(12) NOT NULL,
  `DriverID` char(11) NOT NULL,
  `CarBrand` varchar(12) NOT NULL,
  `CarModel` varchar(12) NOT NULL,
  `CarColor` varchar(12) NOT NULL,
  `CarCapacity` int(11) NOT NULL,
  `DrivedYears` int(11) DEFAULT NULL,
  `CarPohto` varchar(45) DEFAULT NULL,
  `DriveLicense` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CarNum_UNIQUE` (`CarNum`),
  KEY `carinfo_driverid_idx` (`DriverID`),
  CONSTRAINT `carinfo_driverid` FOREIGN KEY (`DriverID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `carshare_deal`
--

DROP TABLE IF EXISTS `carshare_deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `carshare_deal` (
  `DealID` int(11) NOT NULL AUTO_INCREMENT,
  `DealTime` datetime NOT NULL,
  `SharingType` varchar(8) NOT NULL,
  `StartPlaceX` float(9,6) NOT NULL,
  `StartPlaceY` float(9,6) NOT NULL,
  `DestinationX` float(9,6) NOT NULL,
  `DestinationY` float(9,6) NOT NULL,
  `FinshStatus` int(11) NOT NULL,
  PRIMARY KEY (`DealID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cartake_deal`
--

DROP TABLE IF EXISTS `cartake_deal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cartake_deal` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DealID` int(11) NOT NULL,
  `DriverID` char(11) NOT NULL,
  `PassengerID` char(11) NOT NULL,
  `PassengerOrder` int(11) NOT NULL,
  `PosionX` float(9,6) NOT NULL,
  `PosionY` float(9,6) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `carshare_dealid_idx` (`DealID`),
  KEY `cartake_driverid_idx` (`DriverID`),
  KEY `cartake_passengerid_idx` (`PassengerID`),
  CONSTRAINT `cartake_dealid` FOREIGN KEY (`DealID`) REFERENCES `carshare_deal` (`DealID`),
  CONSTRAINT `cartake_driverid` FOREIGN KEY (`DriverID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cartake_passengerid` FOREIGN KEY (`PassengerID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `commute_request`
--

DROP TABLE IF EXISTS `commute_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commute_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` char(11) NOT NULL,
  `RequestTime` datetime NOT NULL,
  `StartPlaceX` float(9,6) NOT NULL,
  `StartPlaceY` float(9,6) NOT NULL,
  `StartPlace` varchar(80) NOT NULL,
  `DestinationX` float(9,6) NOT NULL,
  `DestinationY` float(9,6) NOT NULL,
  `Destination` varchar(80) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `WeekRepeat` varchar(7) NOT NULL,
  `SupplyCar` char(1) NOT NULL,
  `DealStatus` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `UserID_idx` (`UserID`),
  CONSTRAINT `commute` FOREIGN KEY (`UserID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `longway_publish`
--

DROP TABLE IF EXISTS `longway_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `longway_publish` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` char(11) NOT NULL,
  `PublishTime` datetime NOT NULL,
  `UserRole` char(1) NOT NULL,
  `StartDate` date NOT NULL,
  `StartPlace` varchar(45) NOT NULL,
  `Destination` varchar(45) NOT NULL,
  `NoteInfo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `longway_userid_idx` (`UserID`),
  CONSTRAINT `longway_userid` FOREIGN KEY (`UserID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shortway_request`
--

DROP TABLE IF EXISTS `shortway_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shortway_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` char(11) NOT NULL,
  `RequestTime` datetime NOT NULL,
  `UserRole` char(1) NOT NULL,
  `StartPlaceX` float(9,6) NOT NULL,
  `StartPlaceY` float(9,6) NOT NULL,
  `StartPlace` varchar(80) NOT NULL,
  `DestinationX` float(9,6) NOT NULL,
  `DestinationY` float(9,6) NOT NULL,
  `Destination` varchar(80) NOT NULL,
  `StartDate` date NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `DealStatus` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `UserID_idx` (`UserID`),
  CONSTRAINT `shortway_userid` FOREIGN KEY (`UserID`) REFERENCES `user_info` (`PhoneNum`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `PhoneNum` char(11) NOT NULL,
  `PassWord` char(64) NOT NULL,
  `Name` varchar(16) DEFAULT NULL,
  `Sex` char(2) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `PhotoAddress` varchar(45) DEFAULT NULL,
  `IdentifyNum` char(18) DEFAULT NULL,
  `CreditScore` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `PhoneNum_UNIQUE` (`PhoneNum`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8 COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-27 20:07:04
