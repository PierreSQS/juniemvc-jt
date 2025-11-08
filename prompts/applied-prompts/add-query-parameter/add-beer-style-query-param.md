# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* controller_name = `BeerController`
* parameter_name = `beerStyle`

## Task Description
Your task is to implement query parameters to the list operation of existing controllers. Once all the Java code is implemented,
the OpenAPI documentation should be updated to reflect the changes. Use guidelines from the file `.junie/guidelines.md`.

### Task Steps
* Inspect the existing controller `${controller_name}` and identify the list operation.
* The existing list operation will be modified to accept the query parameter `${parameter_name}` using Spring MVC / Spring Data.
* The data type of `${parameter_name}` should match the data type of the corresponding property in the DTO being returned.
* The query parameter should be optional and default to `null` if not provided.
* Update the existing list operation, do not create a new one or new API path.
* The controller list operation should return a `Page<T>` object from the existing package `org.springframework.data.domain`.
* The service layer should be updated to support the `${parameter_name}` on the list operation. Update the list method  
  to accept the `${parameter_name}` parameter.
* The service layer should consider the query parameters as optional values, which may be `null` or empty.
* A new Spring Data Repository method should be created to support the query parameter in a `findAll` method accepting a
  `Pageable` and the `${parameter_name}` return a `Page<T>`.
* Excluding the parameter values for `page` and `size` which are required for pagination, logic should be added to allow
  for any combination of the other parameters to be `null` or empty. For example if there are two values for query
  parameters, provide logic for `parameter1` and `parameter2` or only `parameter1` or `parameter2` or if both are `null`
  or empty. This is to allow for the flexibility in combinations of the query parameters.
* Update the unit tests for the controller, service implementation, and repository to cover the new paging functionality.
* Create new unit tests for the controller, service implementation, and repository to cover the new paging functionality.
* Verify the updated unit tests are passing.
* Update the OpenAPI documentation to reflect the changes made to the controller.
* Update the parameters of the list operation in the OpenAPI documentation to include the new query parameter.
* Verify the OpenAPI documentation is valid.