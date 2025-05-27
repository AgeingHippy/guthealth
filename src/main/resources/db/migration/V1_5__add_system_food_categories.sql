SELECT id INTO @id FROM principle WHERE username = 'system';

INSERT INTO `food_category`
		(`principle_id`,`name`,`description`)
VALUES
		(@id, 'Dairy','Milk products or products made from milk alternatives'),
		(@id, 'Fluids','Fluids such as water, oil, alcohol and fruit juices.'),
		(@id, 'Fruits','Include apples, oranges, bananas, berries and lemons. Fruits contain carbohydrates, mostly in the form of non-free sugar, as well as important vitamins and minerals.'),
		(@id, 'Grains','Cereals and legumes. Include wheat, rice, oats, barley, beans. bread and pasta'),
		(@id, 'Herbs and Spices','Herbs and Spices used to flavour a meal. Examples include salt, pepper, paprika, thyme, rosmary, origanum and so on.'),
		(@id, 'Protein','Meat and eggs'),
		(@id, 'Vegetables','Examples include spinach, carrots, onions, and broccoli.');