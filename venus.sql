-- --------------------------------------------------------
-- Host:                         156.67.210.104
-- Server version:               5.6.34-log - MySQL Community Server (GPL)
-- Server OS:                    Linux
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table antonnw_venus.mtempcustomer
DROP TABLE IF EXISTS `mtempcustomer`;
CREATE TABLE IF NOT EXISTS `mtempcustomer` (
  `intNomor` int(11) NOT NULL AUTO_INCREMENT,
  `intNomorMJenisCustomer` int(11) NOT NULL DEFAULT '0',
  `intNomorMKlasifikasiCustomer` int(11) NOT NULL DEFAULT '0',
  `intNomorMKotaPengiriman` int(11) NOT NULL DEFAULT '0',
  `intNomorMKotaPenagihan` int(11) NOT NULL DEFAULT '0',
  `intNomorMSales` int(11) NOT NULL DEFAULT '0',
  `intNomorMArea` int(11) NOT NULL DEFAULT '0',
  `vcKode` varchar(50) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcNama` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcKontak` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcKeterangan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcNPWP` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcAlamatNPWP` varchar(500) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcAlamatPengiriman` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcAlamatPenagihan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcTeleponPengiriman` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcTeleponPenagihan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcHPPengiriman` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcHPPenagihan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcFaxPengiriman` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcFaxPenagihan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `vcEmailPenagihan` varchar(250) COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `intPT` tinyint(4) NOT NULL DEFAULT '1',
  `intJT` tinyint(4) NOT NULL DEFAULT '0',
  `decPlafon` decimal(30,6) NOT NULL DEFAULT '0.000000',
  `decLatitude` decimal(30,6) NOT NULL DEFAULT '0.000000',
  `decLongitude` decimal(30,6) NOT NULL DEFAULT '0.000000',
  `intInsertUserID` int(11) NOT NULL DEFAULT '0',
  `dtInsertTime` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
  `intUpdateUserID` int(11) NOT NULL DEFAULT '0',
  `dtUpdateTime` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
  `intDeleteUserID` int(11) NOT NULL DEFAULT '0',
  `dtDeleteTime` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
  `intStatus` tinyint(4) NOT NULL DEFAULT '1',
  `intPrinted` int(4) NOT NULL DEFAULT '0',
  `intStatusApproved` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`intNomor`),
  KEY `intNomorMJenisCustomer` (`intNomorMJenisCustomer`),
  KEY `intNomorMKotaPengiriman` (`intNomorMKotaPengiriman`),
  KEY `intNomorMKotaPenagihan` (`intNomorMKotaPenagihan`),
  KEY `intNomorMSales` (`intNomorMSales`),
  KEY `intNomorMArea` (`intNomorMArea`),
  KEY `vcNama` (`vcNama`),
  KEY `vcKode` (`vcKode`),
  CONSTRAINT `mtempcustomer_ibfk_1` FOREIGN KEY (`intNomorMJenisCustomer`) REFERENCES `mjeniscustomer` (`intNomor`) ON UPDATE CASCADE,
  CONSTRAINT `mtempcustomer_ibfk_2` FOREIGN KEY (`intNomorMKotaPengiriman`) REFERENCES `mkota` (`intNomor`) ON UPDATE CASCADE,
  CONSTRAINT `mtempcustomer_ibfk_3` FOREIGN KEY (`intNomorMKotaPenagihan`) REFERENCES `mkota` (`intNomor`) ON UPDATE CASCADE,
  CONSTRAINT `mtempcustomer_ibfk_4` FOREIGN KEY (`intNomorMSales`) REFERENCES `msales` (`intNomor`) ON UPDATE CASCADE,
  CONSTRAINT `mtempcustomer_ibfk_5` FOREIGN KEY (`intNomorMArea`) REFERENCES `marea` (`intNomor`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci ROW_FORMAT=COMPACT;

-- Dumping data for table antonnw_venus.mtempcustomer: ~0 rows (approximately)
/*!40000 ALTER TABLE `mtempcustomer` DISABLE KEYS */;
/*!40000 ALTER TABLE `mtempcustomer` ENABLE KEYS */;

-- Dumping structure for table antonnw_venus.muser_android
DROP TABLE IF EXISTS `muser_android`;
CREATE TABLE IF NOT EXISTS `muser_android` (
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

-- Dumping data for table antonnw_venus.muser_android: ~16 rows (approximately)
/*!40000 ALTER TABLE `muser_android` DISABLE KEYS */;
REPLACE INTO `muser_android` (`intNomor`, `intNomorMUser`, `intNomorMSales`, `vcUserID`, `vcMD5Password`, `vcGCMId`, `vcJabatan`, `intStatus`, `vcHash`) VALUES
	(0, 0, 0, '-', '-', 'eyw5rnz5aMY:APA91bErfwJ0drYmAP70Y5uR2VBAxX4tEFupF0h4BRcfIXowFeE3n_a2l4aqwE0x3qaBc6hF8LUlDoBkG7xEDxlnNkjv7gZ-xnihSxofeIwYgwse3Mdwu0AykOFXmGAxYe0C5wfMFEdL', '-', 0, ''),
	(1, 9, 61, 'admin', '21232f297a57a5a743894a0e4a801fc3', '', 'MANAGER', 1, '020967f4-cbeb-11e6-8b98-0cc47a6bd596'),
	(3, 32, 60, 'anton', '21232f297a57a5a743894a0e4a801fc3', '', 'MANAGER', 1, ''),
	(4, 33, 62, 'liana', '21232f297a57a5a743894a0e4a801fc3', 'fZH_ZaqItDY:APA91bEI0pZdt_cDzi2seTjr6mo3wfXPN1eO7gNVy-YOnD2bmpDQsNze8CWYafmss5QyOfXsdV29MdxBHA5paB0lz2Ts_SubOYvLCAmVjHlLck43ceMgHESiJphv-FWyh2miORtwvHdY', 'SALES', 1, 'aba0a810-cc33-11e6-8b98-0cc47a6bd596'),
	(6, 35, 0, 'feli', '21232f297a57a5a743894a0e4a801fc3', '', 'OWNER', 1, ''),
	(7, 36, 63, 'patrick', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(8, 37, 64, 'fabian', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(9, 38, 65, 'teguh', '21232f297a57a5a743894a0e4a801fc3', 'f8i_1prdv1E:APA91bEpN-rx32ibdzEs_JYI9MHVGAqsEDL1ULlCWSkYt7IH6_XVcwMffCbBHqb_rCOPCf3_tcz2mzyjjWtzAloRvL3EpVf2-5XqhB-R84JC_eBkD2bRKXh6A7BTeT7whKCMnKe6neRi', 'SALES', 1, ''),
	(10, 39, 66, 'valent', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(13, 40, 67, 'agung', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(14, 41, 68, 'andrew', '21232f297a57a5a743894a0e4a801fc3', '', 'MANAGER', 1, ''),
	(15, 42, 69, 'gusnawi', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(16, 43, 70, 'hariyanto', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(17, 44, 71, 'tian', '21232f297a57a5a743894a0e4a801fc3', '', 'MANAGER', 1, ''),
	(18, 45, 72, 'ferry', '21232f297a57a5a743894a0e4a801fc3', '', 'SALES', 1, ''),
	(19, 46, 73, 'andi', '21232f297a57a5a743894a0e4a801fc3', 'e9ofo6_jcP4:APA91bHPvgBEegTam1_gv3vKmMpOHi3j1vkgzabIpPITtXga1hrkGgu0NOumsRB5ycbPM0RCB7zjGSzRP1fE99PpB_zvlrQwmi2xO9GXQwPREHUYA4BELpidyKCTVS81_AnY13L4gpw2', 'SALES', 1, '');
/*!40000 ALTER TABLE `muser_android` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
