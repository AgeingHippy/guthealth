# GutHealth

GutHealth is the seeds of an applications which will track consumption and gut health and finally use AI to detect links between the two.

This is also initially used as the Capstone poject for CodingNomads Java 301 module.

Note. Only implementing methods included in 301 at this point. No Spring, no tests, no logging and no data access optimisation.

The current phase allows the user to capture meal data using the command line interface as well as by file (Only FoodType records at this point).


### Data categories:-
- Lookup values
- - Preparation Technique - This describes how the food was prepared such as Boiled, Pan Fried, Roasted etc. We expect minimal changes to this dataset once it is created. 
- - Food Category - This is used to break the food into various categories such as Dairy, Protein, Grains, Fruits and Vegetables etc. but it is user configurable. Again, we expect minimal modifications once the dataset is created.
- - Food Type - These are various food types such as Carrots, Broccoli, Beef Steak, Beef Mince, Potatoes, Salt etc. This can be as general as you like. For example, it may be simpler to have a food type of 'Beef' rather than different types of beef in order to keep the number of elements to a minimum. This dataset is expected to grow as time passes, and is used as a lookup when creating the components of dishes and meals. We do filter by category to reduce the volume of records displayed when selecting.
- Main data values
- - Dish - The parent record of a particular dish such as Chilli con Carne or a particular Signature Salad. The name is unique and will specify the preparation technique, such as Braised for the former and Raw for the latter. Given the preparation technique is applicable to all the components of a particular dish, it might als be considered a 'Pot' of food.
- - Dish Component - This is a component belonging to a particular dish. Of particular interest here is the **proportion** field. Please see more about this below.
- - Meal - The meal parent record for the food (and drink) consumed in a particular sitting. This is currently identified by a date and time. 
- - Meal component - These describe the individual food items consumed during a meal. Meal components can be added individually, or from copied from one or more dishes. The preparation technique is relevant to the food item and not the meal as the meal could consist of multiple dishes, each prepared in a different manner. The **volume** here indicates the volume of the particular food item consumed during the meal.

The only matter that may need further explanation is how the volume of a particular meal component is determined when adding components based on a dish. The volume of the meal is specified by the user, and then the volume of the component is calculated as dishVolume*dishComponentProportion/SumOf(dishComponentProportion). Note: This may not sum up exactly to the volume specified due to rounding during the calculation mentioned.

### File load:
- Food Types. 
- - Fields are pipe delimited and minimal validation is applied.
- - Record format is: `<foodCategoryName>|<foodTypeName>|<foodTypeDescription>`
- - `<fileName>.err` file is created containing records that have failed load along with a textual description of why they failed to load


## Data Model
Please see the attached ER diagram for further detail.

![er-diagram.png](src/main/resources/er-diagram.png)