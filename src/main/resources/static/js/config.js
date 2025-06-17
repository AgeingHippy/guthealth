//###################################################
//#              CONFIGURATION DATA
//#
//# NOTE that 'contextPath' is initialised in layout.html <head> section
//#      before this script is referenced by the following, leveraging Thymeleaf to do so
//#
//#  <script th:inline="javascript">const contextPath=[[@{/}]];</script>
//#
//###################################################

const preparationTechniquesUri = contextPath + "api/v1/preparation-techniques";
const foodCategoriesUri = contextPath + "api/v1/food-categories";
const foodTypesUri = contextPath + "api/v1/food-types";
const tasksUri = contextPath + "api/v1/tasks";
const testUri = contextPath + "test/time";
const userPasswordUri = contextPath + "api/v1/user/password";
const userRegisterUri = contextPath + "api/v1/user/register";
const adminUserPasswordUri = contextPath + "api/v1/user/{id}/password";