/*
SQLyog Community v12.0 (32 bit)
MySQL - 5.6.11 : Database - venus_android
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`venus_android` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci */;

USE `venus_android`;

/*Table structure for table `tgroup_chat` */

DROP TABLE IF EXISTS `tgroup_chat`;

CREATE TABLE `tgroup_chat` (
  `intNomorMUser` int(11) NOT NULL,
  `txtMessage` text COLLATE latin1_general_ci NOT NULL,
  `dtInsertTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dtInsertTime`),
  KEY `intNomorMUserAndroid` (`intNomorMUser`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

/*Data for the table `tgroup_chat` */

LOCK TABLES `tgroup_chat` WRITE;

insert  into `tgroup_chat`(`intNomorMUser`,`txtMessage`,`dtInsertTime`) values (9,'gh','2016-06-20 21:37:32'),(9,'gh','2016-06-20 21:37:34'),(9,'1234gh','2016-06-20 21:37:35'),(9,'esadfgh','2016-06-20 21:37:36'),(9,'data baru','2016-06-20 21:51:45'),(9,'coba insert','2016-06-20 21:55:03'),(9,'tes','2016-06-20 23:37:07'),(9,'lalala','2016-06-20 23:37:32'),(9,'tes','2016-06-20 23:40:49'),(9,'yahooo','2016-06-20 23:48:11'),(9,'tes','2016-06-20 23:55:42'),(9,'yahooo','2016-06-20 23:57:19'),(9,'bjzz','2016-06-21 11:29:24'),(9,'lala','2016-06-21 11:39:16'),(9,'tes','2016-06-21 11:40:48'),(9,'yahoooo','2016-06-21 11:40:58'),(9,'lalalalal','2016-06-21 11:49:52'),(9,'wooooof','2016-06-21 11:49:56'),(9,'lolOK olo','2016-06-21 11:54:15'),(9,'shjsksk','2016-06-21 11:54:17');

UNLOCK TABLES;

/*Table structure for table `tjadwal` */

DROP TABLE IF EXISTS `tjadwal`;

CREATE TABLE `tjadwal` (
  `intNomorUserManager` int(11) NOT NULL,
  `intNomorUserBEX` int(11) NOT NULL,
  `intNomorCustomer` int(11) NOT NULL,
  `dtTanggal` date NOT NULL,
  `tmJam` time NOT NULL,
  `dtInsertTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `intStatus` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

/*Data for the table `tjadwal` */

LOCK TABLES `tjadwal` WRITE;

UNLOCK TABLES;

/*Table structure for table `tprivate_chat` */

DROP TABLE IF EXISTS `tprivate_chat`;

CREATE TABLE `tprivate_chat` (
  `intNomorMUser_from` int(11) NOT NULL,
  `intNomorMUser_to` int(11) NOT NULL,
  `txtMessage` text COLLATE latin1_general_ci NOT NULL,
  `dtInsertTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dtInsertTime`),
  KEY `intNomorMUserAndroid` (`intNomorMUser_to`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

/*Data for the table `tprivate_chat` */

LOCK TABLES `tprivate_chat` WRITE;

insert  into `tprivate_chat`(`intNomorMUser_from`,`intNomorMUser_to`,`txtMessage`,`dtInsertTime`) values (9,92,'detection','2016-06-21 19:52:17');

UNLOCK TABLES;

/*Table structure for table `tsetting` */

DROP TABLE IF EXISTS `tsetting`;

CREATE TABLE `tsetting` (
  `intNomor` int(11) NOT NULL AUTO_INCREMENT,
  `varNama` varchar(200) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `intNilai` int(11) NOT NULL DEFAULT '0',
  `dtInsertDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `intStatus` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`intNomor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

/*Data for the table `tsetting` */

LOCK TABLES `tsetting` WRITE;

UNLOCK TABLES;

/*Table structure for table `ttracking` */

DROP TABLE IF EXISTS `ttracking`;

CREATE TABLE `ttracking` (
  `intNomorUser` int(11) NOT NULL,
  `decLatitude` decimal(30,6) NOT NULL DEFAULT '0.000000',
  `decLongitude` decimal(30,6) NOT NULL DEFAULT '0.000000',
  `dtInsertDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `intStatus` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

/*Data for the table `ttracking` */

LOCK TABLES `ttracking` WRITE;

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
