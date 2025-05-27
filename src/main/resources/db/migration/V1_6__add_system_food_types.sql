SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Dairy';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Cheese','Dairy product made by coagulating milk protein'),
		(@foodCategoryId,'Chocolate','Yes. Chocolate!'),
		(@foodCategoryId,'Ice Cream','Ice Cream'),
		(@foodCategoryId,'Soured Cream','Soured Cream is regular cream fermented with certain lactic acids which sours and thickens the cream');

SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Fruits';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Apple','One a day keeps the doctor away.'),
		(@foodCategoryId,'Banana','Curvy yellow thing. Much liked by monkeys.'),
		(@foodCategoryId,'Orange','Orange flesh is 87% water, 12% carbohydrates, 1% protein, and contains negligible fat'),
		(@foodCategoryId,'Pear','Pears are consumed fresh, canned, as juice, and dried.');

SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Grains';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Brown Bread','Bread made from wholemeal flour'),
		(@foodCategoryId,'Red Kidney Beans','Red kidney beans '),
		(@foodCategoryId,'White Rice','Refined white rice. All types including Basmati, Thai Fragrant, Long Grain etc.');

SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Herbs and Spices';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Beef Stock','Beef Stock'),
		(@foodCategoryId,'Chillies','Crushed or powdered chillies'),
		(@foodCategoryId,'Cumin','Cumin is the dried seed of the herb Cuminum cyminum, a member of the parsley family.'),
		(@foodCategoryId,'Garlic','Includes garlic grains and fresh garlic'),
		(@foodCategoryId,'Pepper','As a spice, it''s the dried fruit of the Piper nigrum vine, often used as a seasoning.'),
		(@foodCategoryId,'Salt','Sweat contains between 2.25 and 3.4 grams of salt per liter. Just in case you''r in a bind :-p'),
		(@foodCategoryId,'Sugar','A sweet-tasting, soluble carbohydrate');

SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Protein';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Beef','Beef is the culinary name for meat from cattle.'),
		(@foodCategoryId,'Chicken','Chicken meat'),
		(@foodCategoryId,'Pork','Flesh of the pig');


SELECT id INTO @foodCategoryId FROM food_category WHERE name = 'Vegetables';

INSERT INTO `food_type`
		(`food_category_id`,`name`,`description`)
VALUES
		(@foodCategoryId,'Bell Pepper','Also known as capsicum includes green, red and orange peppers as well as others.'),
		(@foodCategoryId,'Broccoli','Category includes various versions such as purple sprouting broccoli etc'),
		(@foodCategoryId,'Caper','Covers capers preserved in brine or salt'),
		(@foodCategoryId,'Carrot','A root vegetable, typically orange in colour'),
		(@foodCategoryId,'Celery','Celery is a crunchy, water-rich vegetable which can be eaten raw or cooked'),
		(@foodCategoryId,'Mange Tout','Includes Sugar peas and snow peas. Peas picked very young and eaten still in the pod.'),
		(@foodCategoryId,'Olive','Olives cultivated for consumption, or table olives.'),
		(@foodCategoryId,'Onions','All onions'),
		(@foodCategoryId,'Potato','All forms of potato whether mashed, baked, roasted etc'),
		(@foodCategoryId,'Salad leaves','Generic mixture of leaves from lettuce, spinach, rocket, watercress, endives etc.'),
		(@foodCategoryId,'Sweetcorn','Covers any form including baby corn, fresh and tinned sweetcorn.'),
		(@foodCategoryId,'Tomato','A tomato is actually both a fruit And vegetable. Falling on the side of culinary definition a opposed to the botanical definition.');
