--
-- Table structure for table `user_meta`
--
CREATE TABLE IF NOT EXISTS `user_meta` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255),
  `bio` VARCHAR(500),
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `user`
--
CREATE TABLE IF NOT EXISTS `principle` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(100),
  `oauth2_provider` VARCHAR(100),
  `user_meta_id` INT NOT NULL,
  `account_non_expired` TINYINT NOT NULL DEFAULT 1,
  `account_non_locked` TINYINT NOT NULL DEFAULT 1,
  `credentials_non_expired` TINYINT NOT NULL DEFAULT 1,
  `enabled` TINYINT NOT NULL DEFAULT 1,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_meta_principle1_idx` (`user_meta_id`),
  CONSTRAINT `fk_user_meta_principle1`
    FOREIGN KEY (`user_meta_id`)
    REFERENCES `user_meta` (`id`)
  );

--
-- Table structure for table `role`
--
CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `authority` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_uk1` (`authority`));

--
-- Table structure for table `user_role`
--
CREATE TABLE IF NOT EXISTS `principle_roles` (
  `principle_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  KEY `fk_principle_roles_principle1_idx` (`principle_id`),
  KEY `fk_principle_roles_role1_idx` (`role_id`),
  UNIQUE KEY `principle_roles_uk` (`principle_id`, `role_id`),
  CONSTRAINT `fk_principle_roles_principle1`
    FOREIGN KEY (`principle_id`)
    REFERENCES `principle` (`id`),
  CONSTRAINT `fk_principle_roles_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
);

--
-- Table structure for table `preparation_technique`
--

DROP TABLE IF EXISTS `preparation_technique`;

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
  `principle_id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_food_category_principle1` FOREIGN KEY (`principle_id`) REFERENCES `principle` (`id`),
  UNIQUE KEY `uk_food_category_name` (`principle_id`, `name`)
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
  UNIQUE KEY `uk_food_type_name` (`food_category_id`,`name`),
  KEY `fk_food_type_food_category1_idx` (`food_category_id`),
  CONSTRAINT `fk_food_type_food_category1` FOREIGN KEY (`food_category_id`) REFERENCES `food_category` (`id`)
);

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;

CREATE TABLE `dish` (
  `id` int NOT NULL AUTO_INCREMENT,
  `principle_id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(200) NOT NULL,
  `preparation_technique_code` varchar(20) NOT NULL COMMENT 'Manner of preparation. e.g. roast, fry, grill, poach, unprepared',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dish_name` (`principle_id`, `name`),
  KEY `fk_dish_principle1_idx` (`principle_id`),
  KEY `fk_dish_preparation_technique1_idx` (`preparation_technique_code`),
  CONSTRAINT `fk_dish_principle1` FOREIGN KEY (`principle_id`) REFERENCES `principle` (`id`),
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




