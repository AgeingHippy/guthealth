//###################################################
//#              CONFIGURATION DATA
//#
//# NOTE that 'contextPath' is initialised in layout.html <head> section using
//#
//#  <script th:inline="javascript">const contextPath=[[@{/}]];</script>
//#
//#      before this script is referenced, leveraging Thymeleaf to do so
//###################################################

const preparationTechniquesUri = contextPath + "api/v1/preparation-techniques";
const foodCategoriesUri = contextPath + "api/v1/food-categories";
const foodTypesUri = contextPath + "api/v1/food-types";
const tasksUri = contextPath + "api/v1/tasks";
const testUri = contextPath + "test/time";
const userPasswordUri = contextPath + "api/v1/users/password";
const userRegisterUri = contextPath + "api/v1/users/register";
const adminUserPasswordUri = contextPath + "api/v1/users/{id}/password";
const usersUri = contextPath + "api/v1/users";