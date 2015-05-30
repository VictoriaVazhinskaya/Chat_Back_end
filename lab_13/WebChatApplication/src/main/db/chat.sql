-- MySQL dump 10.13  Distrib 5.6.21, for Win64 (x86_64)
--
-- Host: localhost    Database: chat
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `text` text,
  `date` datetime DEFAULT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (116423155,'Yeah.','2015-02-20 22:44:51',999111999),(122176824,'Oh, hello, Sponge Bob! Let s go to Krasty Krabs.','2015-05-02 10:27:28',999977794),(173598259,' Things are still rough with the divorce, huh? That, uh, that is tough. Are you still talkin to Rachel? Yeah, well, hey, happy almost birthday!','2015-05-20 14:21:05',665934811),(186466739,'NOOOO! We never drink before a show, never!','2015-05-27 11:49:12',999111999),(199833362,'It is a little iychy. What is it made out of?','2015-05-02 09:01:35',373315937),(228564196,'Oh, hello, Squidward! Sponge Bob with you?','2015-05-25 11:35:24',999977794),(241831126,'Come out of the closet!','2015-05-11 18:26:25',161876395),(265728764,'Knowledge can never replace friendship. I prefer to be an idiot!','2015-05-03 19:24:16',999977794),(331579186,'I have a stomack ache','2015-05-11 18:28:14',739888633),(341282199,'Money, money, money, money...','2015-05-12 21:51:58',888316512),(376119563,'This is, I believes called, Food Libraries. Food Libraries.','2015-05-25 13:07:24',669824772),(378199945,'You can not manage to stop me, Krabs!','2015-05-20 16:12:44',851722464),(461118933,'hello, Patrick! Let s go to catch jellyfish!','2015-05-02 10:25:16',181496675),(481645873,'Watermellow','2015-05-26 15:48:23',448262846),(562114218,'We will give you half... of nothing!','2015-05-14 23:14:38',999111999),(641915775,'And you know what I got at the beach?Sand!','2015-05-14 20:15:46',182433689),(657121424,'So what you are saying is we do the opposite of bleak and dark.','2015-02-20 22:43:11',444448899),(728816441,'meow','2015-05-07 00:00:00',948226848),(746529614,'What is this place called?','2015-05-25 13:06:52',444448899),(814652285,'Eyelashes.','2015-05-02 09:01:37',181496675),(826114859,'We are here to make coffee metal. We will make everything metal! Blacker then the blackest black, times infinity!','2015-05-06 17:22:08',999111999),(833126612,'I am Dr. Rockso, the rock and roll clown! I do cocaine!','2015-05-16 12:20:13',987654321),(835422617,'What is the opposite of tradegy?','2015-02-20 22:45:08',444448899),(836571094,'Look. The wol-ev-es eat him.','2015-05-22 21:46:22',444448899),(912155638,'Tradegy','2015-02-20 22:45:34',999111999),(985633872,'I made you this sweater... Do you love it?','2015-05-02 09:01:20',181496675),(987224612,'Go earn my food!','2015-05-18 10:38:12',999977794);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (161876395,'Barnacle Boy'),(181496675,'Sponge Bob'),(182433689,'Pickles'),(373315937,'Squidward Tentacles'),(444448899,'Toki Wartooth'),(448262846,'Sandy Cheeks'),(665934811,'William Murderface'),(669824772,'Skwisgaar Skwigelf'),(739888633,'Mermaid Man'),(851722464,'Plankton'),(888316512,'Mr. Krabs'),(948226848,'Garry'),(987654321,'Dr. Rockso'),(999111999,'Nathan Explosion'),(999977794,'Patrick Star');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-30 12:22:04
