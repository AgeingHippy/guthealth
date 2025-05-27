SELECT id INTO @id FROM principle WHERE username = 'system';

INSERT INTO `preparation_technique` (`principle_id`,`code`,`description`)
VALUES 	(@id, 'Boil','is the highest temperature for submersion (100°C).   The water should have many large and vigorous bubbles.'),
		(@id,'Braising','Starts with dry-heat cooking (pan frying or sautéing) the meat to ensure proper caramelization. Once caramelized, liquid, such as a stock, is added until it reaches the bottom third of the product.'),
		(@id,'Broil','uses radiant dry heat from above the food being cooked'),
		(@id,'Deep Fry','Produce immersed in a deep bath of oil'),
		(@id,'Dry','To air dry, possibly in a dehumidifier'),
		(@id,'Grill','Grilling works best on smaller cuts of meat.  Cast iron grills are preferred over stainless steel for the grill marks they leave on the food.'),
		(@id,'Pan Fry','Fry in a thin layer of oil'),
		(@id,'Poach','s the lowest temperature method (70°C to 80°C). The water should show slight movement and no bubbles making it perfect for delicate foods like eggs.'),
		(@id,'Raw','No cooking applied. Potentially wash and chop only.'),
		(@id,'Roast/Bake','uses the air to transfer heat to an ingredient. Roast meat or vegetables. Bake bread and cakes etc.'),
		(@id,'Simmer','is the middle temperature range (85°C to 96°C). The water should have small bubbles breaking through the surface. It is great for releasing flavors in stews, meats and soups.'),
		(@id,'Slow Cook','To cook in a slow cooker. Analogous to stewing '),
		(@id,'Toast','Toasting is a dry cooking method, where the food is exposed to high heat to brown and crisp the outside');