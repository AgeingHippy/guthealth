/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table `preparation_technique`
--

DROP TABLE IF EXISTS `preparation_technique`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preparation_technique` (
  `code` varchar(20) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`code`)
);


--
-- Table structure for table `food_category`
--

DROP TABLE IF EXISTS `food_category`;

CREATE TABLE `food_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_food_category_name` (`name`)
);


--
-- Table structure for table `food_type`
--

DROP TABLE IF EXISTS `food_type`;

CREATE TABLE `food_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `food_category_id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_food_type_name` (`name`),
  KEY `fk_food_type_food_category1_idx` (`food_category_id`),
  CONSTRAINT `fk_food_type_food_category1` FOREIGN KEY (`food_category_id`) REFERENCES `food_category` (`id`)
);

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;

CREATE TABLE `dish` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  `preparation_technique_code` varchar(20) NOT NULL COMMENT 'Manner of preparation. e.g. roast, fry, grill, poach, unprepared',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dish_name` (`name`),
  KEY `fk_dish_preparation_technique1_idx` (`preparation_technique_code`),
  CONSTRAINT `fk_dish_preparation_technique1` FOREIGN KEY (`preparation_technique_code`) REFERENCES `preparation_technique` (`code`)
);

--
-- Table structure for table `dish_component`
--

DROP TABLE IF EXISTS `dish_component`;

CREATE TABLE `dish_component` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dish_id` int NOT NULL,
  `food_type_id` int NOT NULL,
  `proportion` int NOT NULL COMMENT 'Proportion this food type is of the whole dish. When calculating meal_components the meal.volume = serving volume * (proportion / total of dish proportions). Set negligible to 0',
  PRIMARY KEY (`id`),
  KEY `fk_dish_component_dish1_idx` (`dish_id`),
  KEY `fk_dish_component_food_type1_idx` (`food_type_id`),
  CONSTRAINT `fk_dish_component_dish1` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`),
  CONSTRAINT `fk_dish_component_food_type1` FOREIGN KEY (`food_type_id`) REFERENCES `food_type` (`id`)
);

--
-- Table structure for table `meal`
--

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `meal_component`
--

DROP TABLE IF EXISTS `meal_component`;

CREATE TABLE `meal_component` (
  `id` int NOT NULL AUTO_INCREMENT,
  `meal_id` int NOT NULL,
  `food_type_id` int NOT NULL,
  `preparation_technique_code` varchar(20) NOT NULL COMMENT 'Manner of preparation. e.g. roast, fry, grill, poach, unprepared',
  `volume` int NOT NULL COMMENT 'Volume of food in grammes',
  PRIMARY KEY (`id`),
  KEY `fk_meal_component_meal_idx` (`meal_id`),
  KEY `fk_meal_component_food_type1_idx` (`food_type_id`),
  KEY `fk_meal_component_preparation_technique1_idx` (`preparation_technique_code`),
  CONSTRAINT `fk_meal_component_food_type1` FOREIGN KEY (`food_type_id`) REFERENCES `food_type` (`id`),
  CONSTRAINT `fk_meal_component_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`),
  CONSTRAINT `fk_meal_component_preparation_technique1` FOREIGN KEY (`preparation_technique_code`) REFERENCES `preparation_technique` (`code`)
);


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



