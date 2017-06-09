/*
SQLyog Community v12.0 (32 bit)
MySQL - 5.6.34-log : Database - antonnw_venus
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`antonnw_venus` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `antonnw_venus`;

/*Table structure for table `muser_android` */

DROP TABLE IF EXISTS `muser_android`;

CREATE TABLE `muser_android` (
  `intNomor` int(11) NOT NULL AUTO_INCREMENT,
  `intNomorMUser` int(11) NOT NULL DEFAULT '0',
  `intNomorMSales` int(11) NOT NULL DEFAULT '0',
  `vcUserID` varchar(100) NOT NULL,
  `vcMD5Password` varchar(100) NOT NULL,
  `vcGCMId` varchar(200) NOT NULL,
  `vcJabatan` varchar(100) NOT NULL,
  `intStatus` int(4) NOT NULL DEFAULT '1',
  `vcHash` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`intNomor`,`intNomorMUser`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `muser_android` */

LOCK TABLES `muser_android` WRITE;

insert  into `muser_android`(`intNomor`,`intNomorMUser`,`intNomorMSales`,`vcUserID`,`vcMD5Password`,`vcGCMId`,`vcJabatan`,`intStatus`,`vcHash`) values (0,0,0,'-','-','eyw5rnz5aMY:APA91bErfwJ0drYmAP70Y5uR2VBAxX4tEFupF0h4BRcfIXowFeE3n_a2l4aqwE0x3qaBc6hF8LUlDoBkG7xEDxlnNkjv7gZ-xnihSxofeIwYgwse3Mdwu0AykOFXmGAxYe0C5wfMFEdL','-',0,''),(1,9,61,'admin','21232f297a57a5a743894a0e4a801fc3','fkcUROtF0ks:APA91bESTpJmXJmYwjty24vzEXXzm9EUXq0CH3HgYebhQqYJavwHb3OsSqddvL6Y-cyQtUFKX15ISQz61cuxwo0MeyFO31rI-O2ys_hH0iOTl-fVgwPGkM7H0eLyH99Gy38DBMwO6fzB','MANAGER',1,'4e1dbf4f-ce9a-11e6-8b98-0cc47a6bd596'),(3,76,60,'anton','21232f297a57a5a743894a0e4a801fc3','caK-5aWdsII:APA91bEBzJsP7SDQRfLNQFhFAkwHQTg55XniljwXS9vVG5dVBBxhFtMjK3C7NehyobX75sGiybd1xb6OmXLKy7jVSg_KmnP6PX_r9H9OD4O7q1YkY0p-tyBMH9Ytz0geqLpQnqc7aPw0','MANAGER',1,'623caaa7-ce86-11e6-8b98-0cc47a6bd596'),(4,33,62,'liana','21232f297a57a5a743894a0e4a801fc3','fZH_ZaqItDY:APA91bEI0pZdt_cDzi2seTjr6mo3wfXPN1eO7gNVy-YOnD2bmpDQsNze8CWYafmss5QyOfXsdV29MdxBHA5paB0lz2Ts_SubOYvLCAmVjHlLck43ceMgHESiJphv-FWyh2miORtwvHdY','SALES',1,'f47f3a45-ce85-11e6-8b98-0cc47a6bd596'),(6,35,0,'feli','21232f297a57a5a743894a0e4a801fc3','','OWNER',1,''),(7,36,63,'patrick','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(8,37,64,'fabian','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(9,38,65,'teguh','21232f297a57a5a743894a0e4a801fc3','f8i_1prdv1E:APA91bEpN-rx32ibdzEs_JYI9MHVGAqsEDL1ULlCWSkYt7IH6_XVcwMffCbBHqb_rCOPCf3_tcz2mzyjjWtzAloRvL3EpVf2-5XqhB-R84JC_eBkD2bRKXh6A7BTeT7whKCMnKe6neRi','SALES',1,''),(10,39,66,'valent','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(13,40,67,'agung','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(14,41,68,'andrew','21232f297a57a5a743894a0e4a801fc3','','MANAGER',1,''),(15,42,69,'gusnawi','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(16,43,70,'hariyanto','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(17,44,71,'tian','21232f297a57a5a743894a0e4a801fc3','','MANAGER',1,''),(18,45,72,'ferry','21232f297a57a5a743894a0e4a801fc3','','SALES',1,''),(19,46,73,'andi','21232f297a57a5a743894a0e4a801fc3','e9ofo6_jcP4:APA91bHPvgBEegTam1_gv3vKmMpOHi3j1vkgzabIpPITtXga1hrkGgu0NOumsRB5ycbPM0RCB7zjGSzRP1fE99PpB_zvlrQwmi2xO9GXQwPREHUYA4BELpidyKCTVS81_AnY13L4gpw2','SALES',1,''),(20,32,0,'efy','21232f297a57a5a743894a0e4a801fc3','fkcUROtF0ks:APA91bESTpJmXJmYwjty24vzEXXzm9EUXq0CH3HgYebhQqYJavwHb3OsSqddvL6Y-cyQtUFKX15ISQz61cuxwo0MeyFO31rI-O2ys_hH0iOTl-fVgwPGkM7H0eLyH99Gy38DBMwO6fzB','SALES ADMIN',1,'659f6fa9-ce9d-11e6-8b98-0cc47a6bd596');

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
