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
INSERT INTO `preparation_technique` VALUES ('Boil','is the highest temperature for submersion (100Â°C).   The water should have many large and vigorous bubbles.'),('Braising','Starts with dry-heat cooking (pan frying or sautÃ©ing) the meat to ensure proper caramelization. Once caramelized, liquid, such as a stock, is added until it reaches the bottom third of the product.'),('Broil','uses radiant dry heat from above the food being cooked'),('Deep Fry','Produce immersed in a deep bath of oil'),('Dry','To air dry, possibly in a dehumidifier'),('Grill','Grilling works best on smaller cuts of meat.  Cast iron grills are preferred over stainless steel for the grill marks they leave on the food.'),('Pan Fry','Fry in a thin layer of oil'),('Poach','s the lowest temperature method (70Â°C â€“ 80Â°C). The water should show slight movement and no bubbles making it perfect for delicate foods like eggs.'),('Raw','No cooking applied. Potentially wash and chop only.'),('Roast/Bake','uses the air to transfer heat to an ingredient. Roast meat or vegetables. Bake bread and cakes etc.'),('Simmer','is the middle temperature range (85Â°C â€“ 96Â°C).  The water should have small bubbles breaking through the surface.   It is great for releasing flavors in stews, meats and soups.'),('Slow Cook','To cook in a slow cooker. Analogous to stewing '),('Toast','Toasting is a dry cooking method, where the food is exposed to high heat to brown and crisp the outside');
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
) AUTO_INCREMENT=15;

--
-- Dumping data for table `food_category`
--

/*!40000 ALTER TABLE `food_category` DISABLE KEYS */;
INSERT INTO `food_category` VALUES
	(1,'Dairy','Milk products or products made from milk alternatives'),
	(2,'Protein','Meat and eggs'),
	(3,'Fruits','Include apples, oranges, bananas, berries and lemons. Fruits contain carbohydrates, mostly in the form of non-free sugar, as well as important vitamins and minerals.'),
	(4,'Vegetables','Examples include spinach, carrots, onions, and broccoli.'),
	(6,'Grains','Cereals and legumes. Include wheat, rice, oats, barley, beans. bread and pasta'),
	(7,'Herbs and Spices','Herbs and Spices used to flavour a meal. Examples include salt, pepper, paprika, thyme, rosmary, origanum and so on.'),
	(14,'stringY','stringY desc');
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
) AUTO_INCREMENT=40;


--
-- Dumping data for table `food_type`
--

/*!40000 ALTER TABLE `food_type` DISABLE KEYS */;
INSERT INTO `food_type` VALUES (1,1,'Cheese','dairy product made by coagulating milk protein'),(3,3,'Banana','curvy yellow thing'),(5,7,'Salt','Salt'),(6,7,'Pepper','pepper'),(7,7,'chillies','crushed or powdered chillies'),(8,7,'Beef Stock','Beef Stock'),(9,6,'White Rice','Refined white rice. All types including Basmati, Thai Fragrant, Long Grain etc.'),(10,7,'garlic','includes garlic grains and fresh garlic'),(12,4,'Onions','All onions'),(13,4,'Tomato','a Tomato Is Actually Both a Fruit And Vegetable. Falling on the side of cullinary definition a opposed to the botanical definition.'),(14,4,'Bell Pepper','Also known as capsicum includes green, red and orange peppers as well as others.'),(15,6,'Red Kidney Beans','Red kidney beans '),(16,7,'sugar','sugar granules'),(17,4,'Olive','Olives cultivated for consumption, or table olives.'),(18,4,'Caper','Covers capers preserved in brine or salt'),(19,4,'Carrot',' root vegetable, typically orange in colour'),(20,4,'Salad leaves',' Generic mixture of leaves from lettuce, spinach, rocket, watercress, endives etc.'),(21,4,'Sweetcorn','Covers any form including baby corn, fresh and tinned sweetcorn.'),(22,4,'Broccoli','Category includes various versions such as purple sprouting broccoli etc'),(23,4,'Mange Tout','Includes Sugar peas and snow peas. Peas picked very young and eaten still in the pod.'),(24,4,'Celery','Celery is a crunchy, water-rich vegetable which can be eaten raw or cooked'),(25,4,'Potato','All forms of potato whether mashed, baked, roasted etc'),(26,1,'Soured Cream','Soured Cream is regular cream fermented with certain lactic acids which sours and thickens the cream'),(27,2,'Beef','Beef is the culinary name for meat from cattle.'),(28,7,'Cumin','Cumin is the dried seed of the herb Cuminum cyminum, a member of the parsley family. '),(29,6,'Brown Bread','Bread made from wholemeal flour'),(30,1,'Ice Cream','Ice Cream'),(31,1,'Chocolate','Yes. Chocolate!'),(32,2,'Chicken','Chicken meat'),(33,3,'Apple','One a day keeps the doctor away.'),(34,3,'Orange','The color was named after the fruit. Orange flesh is 87% water, 12% carbohydrates, 1% protein, and contains negligible fat'),(35,3,'Pear','Pears are consumed fresh, canned, as juice, and dried.'),(37,14,'string1','string1 desc'),(38,14,'string2','string2 desc');
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
) AUTO_INCREMENT=6;

--
-- Dumping data for table `dish`
--

/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish`
	VALUES 	(1,'Chilli con carne','Chilli made with minced beef, onions, tomato and red peppers','Braising'),
		(2,'Boiled white rice','Boiled white rice','Boil'),
		(4,'Signature Salad','Signature salad consists of numerous vegetables, leaf types and has mayonaise stirred in.','Raw'),
		(5,'Beef Biltong','Biltong made from beef which is first spiced and then dried','Dry');
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
INSERT INTO `dish_component` VALUES
	(1,1,27,1000),
	(2,1,12,200),
	(3,1,13,800),
	(4,1,15,800),
	(5,1,14,200),
	(6,1,10,20),
	(7,1,5,8),
	(8,1,6,5),
	(9,1,7,15),
	(10,2,9,100),
	(11,1,8,600),
	(15,4,13,100),
	(16,4,14,50),
	(17,4,5,0),
	(18,4,6,0),
	(19,4,1,50),
	(21,5,27,95),
	(22,5,5,2),
	(23,5,6,2),
	(24,5,7,1),
	(25,5,28,2);
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
INSERT INTO `meal` VALUES (1,'2024-12-02','20:15:00'),(2,'2024-12-20','13:30:00'),(3,'2024-12-03','21:30:00'),(4,'2024-12-08','13:00:00'),(5,'2024-12-08','14:00:00');
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
INSERT INTO `meal_component` VALUES (1,1,27,'Grill',300),(3,1,25,'Roast/Bake',150),(5,2,19,'Raw',50),(6,2,22,'Raw',50),(7,3,27,'Braising',82),(8,3,12,'Braising',16),(9,3,13,'Braising',65),(10,3,15,'Braising',65),(11,3,14,'Braising',16),(12,3,10,'Braising',1),(13,3,5,'Braising',0),(14,3,6,'Braising',0),(15,3,7,'Braising',1),(16,3,8,'Braising',49),(17,3,9,'Boil',400),(18,3,13,'Raw',225),(19,3,14,'Raw',112),(20,3,5,'Raw',0),(21,3,6,'Raw',0),(22,3,1,'Raw',112),(23,3,26,'Raw',50),(24,4,27,'Dry',93),(25,4,5,'Dry',1),(26,4,6,'Dry',1),(27,4,7,'Dry',0),(28,4,28,'Dry',1),(31,5,27,'Braising',82),(32,5,12,'Braising',16),(33,5,13,'Braising',65),(34,5,15,'Braising',65),(35,5,14,'Braising',16),(36,5,10,'Braising',1),(37,5,5,'Braising',0),(38,5,6,'Braising',0),(39,5,7,'Braising',1),(40,5,8,'Braising',49),(41,5,9,'Boil',200),(42,5,26,'Raw',50),(43,5,30,'Raw',200);
/*!40000 ALTER TABLE `meal_component` ENABLE KEYS */;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-28 13:51:46

