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
-- Dumping data for table `preparation_technique`
--

/*!40000 ALTER TABLE `preparation_technique` DISABLE KEYS */;
INSERT
INTO `preparation_technique`
    (code, description)
VALUES
    ('PrepType1','Preparation type one description'),
    ('PrepType2','Preparation type two description'),
    ('PrepType3','Preparation type three description'),
    ('PrepType4','Preparation type four description')
/*!40000 ALTER TABLE `preparation_technique` ENABLE KEYS */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


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
) AUTO_INCREMENT=6;

--
-- Dumping data for table `food_category`
--

/*!40000 ALTER TABLE `food_category` DISABLE KEYS */;
INSERT
INTO `food_category`
    (id, name, description)
VALUES
	(1,'foodCategory1_name','Food Category one description'),
	(2,'foodCategory2_name','Food Category two description'),
	(3,'foodCategory3_name','Food Category three description'),
	(4,'foodCategory4_name','Food Category four description'),
	(5,'foodCategory5_name','Food Category five description');
/*!40000 ALTER TABLE `food_category` ENABLE KEYS */;


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
) AUTO_INCREMENT=16;


--
-- Dumping data for table `food_type`
--

/*!40000 ALTER TABLE `food_type` DISABLE KEYS */;
INSERT
INTO `food_type`
    (id, food_category_id, name, description)
VALUES
    (1,1,'foodType1','Food Type one Description'),
    (2,1,'foodType2','Food Type two Description'),
    (3,1,'foodType3','Food Type three Description'),
    (4,1,'foodType4','Food Type four Description'),
    (5,1,'foodType5','Food Type five Description'),
    (6,2,'foodType6','Food Type six Description'),
    (7,3,'foodType7','Food Type seven Description'),
    (8,3,'foodType8','Food Type eight Description'),
    (9,3,'foodType9','Food Type nine Description'),
    (10,3,'foodType10','Food Type ten Description'),
    (11,4,'foodType11','Food Type eleven Description'),
    (12,4,'foodType12','Food Type twelve Description'),
    (13,4,'foodType13','Food Type thirteen Description'),
    (14,4,'foodType14','Food Type fourteen Description'),
    (15,4,'foodType15','Food Type fifteen Description');


/*!40000 ALTER TABLE `food_type` ENABLE KEYS */;


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
) AUTO_INCREMENT=5;

--
-- Dumping data for table `dish`
--

/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT
INTO `dish`
    (id, name, description, preparation_technique_code)
VALUES
    (1,'Dish1','Dish one Description','PrepType1'),
    (2,'Dish2','Dish two Description','PrepType1'),
    (3,'Dish3','Dish three Description','PrepType2'),
    (4,'Dish4','Dish four Description','PrepType3');
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;

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
) AUTO_INCREMENT=28;


--
-- Dumping data for table `dish_component`
--

/*!40000 ALTER TABLE `dish_component` DISABLE KEYS */;
INSERT
INTO `dish_component`
    (id, dish_id, food_type_id, proportion)
VALUES
    (1,1,1, 100),
    (2,1,2, 100),
    (3,1,3, 100),
    (4,1,4, 100),

    (5,2,1, 100),
    (6,2,3, 200),
    (7,2,5, 300),
    (8,2,7, 400),

    (9,3,1, 1),
    (10,3,10, 2);


/*!40000 ALTER TABLE `dish_component` ENABLE KEYS */;


--
-- Table structure for table `meal`
--

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=6;

--
-- Dumping data for table `meal`
--

/*!40000 ALTER TABLE `meal` DISABLE KEYS */;
INSERT
INTO `meal`
    (id, date, time)
VALUES
    (1,'2024-12-02','20:15:00'),
    (2,'2024-12-20','13:30:00'),
    (3,'2024-12-03','21:30:00'),
    (4,'2024-12-08','13:00:00'),
    (5,'2024-12-08','14:00:00');
/*!40000 ALTER TABLE `meal` ENABLE KEYS */;

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
) AUTO_INCREMENT=44;

--
-- Dumping data for table `meal_component`
--

/*!40000 ALTER TABLE `meal_component` DISABLE KEYS */;
INSERT
INTO `meal_component`
    (id, meal_id, food_type_id, preparation_technique_code, volume)
VALUES
    (1,1,1,'PrepType1',200),
    (2,1,2,'PrepType1',200),
    (3,1,3,'PrepType1',200),
    (4,1,4,'PrepType1',200),

    (5,2,1,'PrepType1',100),
    (6,2,2,'PrepType1',100),
    (7,2,3,'PrepType1',100),
    (8,2,4,'PrepType1',100),
    (9,2,1,'PrepType3',200),
    (10,2,5,'PrepType3',200),

    (11,3,15,'PrepType2',1000),

    (12,4,1,'PrepType1',100),
    (13,4,10,'PrepType3',500),
    (14,4,15,'PrepType2',200),

    (15,5,11,'PrepType2', 2000);
/*!40000 ALTER TABLE `meal_component` ENABLE KEYS */;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-28 13:51:46

