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


-- ROLE

/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role`
    ( id, authority )
VALUES
    ( 1, 'ROLE_ADMIN'),
    ( 2, 'ROLE_USER'),
    ( 3, 'ROLE_GUEST');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
ALTER TABLE `role` AUTO_INCREMENT=34;

--
-- USER-META
--

/*!40000 ALTER TABLE `user_meta` DISABLE KEYS */;

INSERT INTO user_meta
    ( id, name, email, bio)
VALUES
    ( 1, 'Bob','bob@home.com','Likes building'),
    ( 2, 'Bill','bill@home.com',null),
    ( 3, 'Betty','betty@home.com','Does the boop'),
    ( 4, 'SYSTEM',null,'System Data Owner');
--    ( 5, 'Bruce', null, 'alternative user');

/*!40000 ALTER TABLE `user_meta` ENABLE KEYS */;
ALTER TABLE `user_meta` AUTO_INCREMENT=5;

--
-- PRINCIPLE
--
/*!40000 ALTER TABLE `principle` DISABLE KEYS */;

INSERT INTO principle
    (id, username, password, oauth2_provider, user_meta_id)
VALUES
    (1, 'admin', '$2a$10$MfPAJs3UgQApzYSUoYioFOXyLegPvusXibZqnfZq9EBgQu1h3W/BW', null, 1),
    (2, 'basic', '$2a$10$l5kuu4MxbDGfgjGwkov6HemySH3.uHgeVjYrEZOuzkzo20t01nVHa', null, 2),
    (3, 'guest', '$2a$10$al7wMSlApNl0hfjVAUAgfeJPc/wr02ynl7J0INBjFIopAxCro316K', null, 3),
    (4, 'system','$2a$10$W9PJKMvG1O1MM79JxE3VBOCq0m7DOZLD/ky8mWmtcQ3qziR/KR7KK', null, 4);
--    (5, 'alternative','$2a$10$peG6NHRkAuUDlsmR8Y0YCeZeTSmq6PVqNomtn0oMf75HY8/O6iY86', null, 5);

/*!40000 ALTER TABLE `principle` ENABLE KEYS */;
ALTER TABLE `principle` AUTO_INCREMENT=5;

--
-- PRINCIPLE_ROLES
--

/*!40000 ALTER TABLE `principle_roles` DISABLE KEYS */;

INSERT INTO principle_roles
    (principle_id, role_id)
VALUES
    (1,1),
    (2,3),
    (2,2),
    (3,3),
    (4,2);
--    (5,2);

/*!40000 ALTER TABLE `principle_roles` ENABLE KEYS */;

--
-- Dumping data for table `preparation_technique`
--

/*!40000 ALTER TABLE `preparation_technique` DISABLE KEYS */;
INSERT
INTO `preparation_technique`
    (id, principle_id, code, description)
VALUES
    (1, 2, 'PrepType1','Preparation type one description'),
    (2, 2, 'PrepType2','Preparation type two description'),
    (3, 2, 'PrepType3','Preparation type three description'),
    (4, 2, 'PrepType4','Preparation type four description'),
    (5, 4, 'PrepType5','Preparation type five description'),
    (6, 4, 'PrepType6','Preparation type six description');

/*!40000 ALTER TABLE `preparation_technique` ENABLE KEYS */;
ALTER TABLE `preparation_technique` AUTO_INCREMENT=7;

--
-- Dumping data for table `food_category`
--

/*!40000 ALTER TABLE `food_category` DISABLE KEYS */;
INSERT
INTO `food_category`
    (id, principle_id, name, description)
VALUES
	(1,2,'foodCategory1_name','Food Category one description'),
	(2,2,'foodCategory2_name','Food Category two description'),
	(3,2,'foodCategory3_name','Food Category three description'),
	(4,2,'foodCategory4_name','Food Category four description'),
	(5,2,'foodCategory5_name','Food Category five description'),
	(6,4,'foodCategory6_name','Food Category six description'),
	(7,4,'foodCategory7_name','Food Category seven description'),
	(8,4,'foodCategory8_name','Food Category eight description'),
	(9,4,'foodCategory9_name','Food Category nine description');

/*!40000 ALTER TABLE `food_category` ENABLE KEYS */;

ALTER TABLE food_category AUTO_INCREMENT = 10;

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
    (15,4,'foodType15','Food Type fifteen Description'),
    (16,6,'foodType16','Food Type sixteen Description'),
    (17,7,'foodType17','Food Type seventeen Description'),
    (18,7,'foodType18','Food Type eighteen Description'),
    (19,8,'foodType19','Food Type nineteen Description'),
    (20,8,'foodType20','Food Type twenty Description'),
    (21,8,'foodType21','Food Type twentyOne Description');



/*!40000 ALTER TABLE `food_type` ENABLE KEYS */;

ALTER TABLE `food_type` AUTO_INCREMENT=22;

--
-- Dumping data for table `dish`
--

/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT
INTO `dish`
    (id, principle_id, name, description, preparation_technique_id)
VALUES
    (1,2,'Dish1','Dish one Description',1),
    (2,2,'Dish2','Dish two Description',1),
    (3,2,'Dish3','Dish three Description',2),
    (4,2,'Dish4','Dish four Description',3);
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
    (id, principle_id, description, date, time)
VALUES
    (1, 2,'Meal one description','2024-12-02','20:15:00'),
    (2, 2,'Meal two description','2024-12-20','13:30:00'),
    (3, 2,'Meal three description','2024-12-03','21:30:00'),
    (4, 2,'Meal four description','2024-12-08','13:00:00');
/*!40000 ALTER TABLE `meal` ENABLE KEYS */;

ALTER TABLE `meal`AUTO_INCREMENT=6;

--
-- Dumping data for table `meal_component`
--

/*!40000 ALTER TABLE `meal_component` DISABLE KEYS */;
INSERT
INTO `meal_component`
    (id, meal_id, food_type_id, preparation_technique_id, volume)
VALUES
    (1,1,1,1,100),
    (2,1,2,1,100),
    (3,1,3,1,100),

    (4,2,3,2,200),
    (5,2,4,2,200),

    (6,3,5,3,100),
    (7,3,6,4,200);
/*!40000 ALTER TABLE `meal_component` ENABLE KEYS */;

ALTER TABLE `meal_component` AUTO_INCREMENT=8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



