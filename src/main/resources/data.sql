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

ALTER TABLE food_category AUTO_INCREMENT = 6;

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

ALTER TABLE `food_type` AUTO_INCREMENT=16;

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

ALTER TABLE `dish` AUTO_INCREMENT=5;

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

ALTER TABLE `dish_component` AUTO_INCREMENT=28;

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

ALTER TABLE `meal`AUTO_INCREMENT=6;

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

ALTER TABLE `meal_component` AUTO_INCREMENT=44;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-28 13:51:46

