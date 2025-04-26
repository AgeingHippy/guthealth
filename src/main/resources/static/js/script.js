//
//          Build selection options for a dropdown list
//
function buildSelectOptions(
  selectId,   //id of HTML select component
  collection, //Array containing list of objects to use to populate dropdown
  valueProp,  //name of the object property to assign to value when selected
  textProp,   //name of property to display in dropdown
  allowNull   //allow null selection - create first selection as empty string
) {
  let select = document.getElementById(selectId);
  let selection = [];

  if (allowNull) {
    let item = document.createElement("option");
    item.setAttribute("value", "");
    item.innerText = "";
    selection.push(item);
  }

  collection.forEach((element) => {
    let item = document.createElement("option");
    item.setAttribute("value", element[valueProp]);
    item.innerText = element[textProp];
    selection.push(item);
  });

  select.replaceChildren(...selection);
}