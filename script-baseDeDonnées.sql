-- phpMyAdmin SQL Dump
-- version 4.0.10.6
-- http://www.phpmyadmin.net
--
-- Host: mysql1.alwaysdata.com
-- Generation Time: May 02, 2016 at 06:28 PM
-- Server version: 5.1.66-0+squeeze1
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `maplille1_map`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `idadmin` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`idadmin`),
  KEY `user` (`user`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `groupe`
--

CREATE TABLE IF NOT EXISTS `groupe` (
  `idgroup` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(30) NOT NULL,
  PRIMARY KEY (`idgroup`),
  UNIQUE KEY `groupname` (`groupname`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

-- --------------------------------------------------------

--
-- Table structure for table `localisation`
--

CREATE TABLE IF NOT EXISTS `localisation` (
  `idlocalisation` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `longitude` float NOT NULL,
  `latitude` float NOT NULL,
  PRIMARY KEY (`idlocalisation`),
  KEY `user` (`user`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Table structure for table `userGroup`
--

CREATE TABLE IF NOT EXISTS `userGroup` (
  `idusergroup` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `groupe` int(11) NOT NULL,
  PRIMARY KEY (`idusergroup`),
  KEY `user` (`user`),
  KEY `group` (`groupe`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=47 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `iduser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=45 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`user`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `localisation`
--
ALTER TABLE `localisation`
  ADD CONSTRAINT `localisation_ibfk_1` FOREIGN KEY (`user`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `userGroup`
--
ALTER TABLE `userGroup`
  ADD CONSTRAINT `userGroup_ibfk_2` FOREIGN KEY (`groupe`) REFERENCES `groupe` (`idgroup`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `userGroup_ibfk_1` FOREIGN KEY (`user`) REFERENCES `users` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
