# Prompt Variables
Apply the following variables to placeholders in the prompt. Placeholders are denoted by `${variable}` syntax.

# Placeholders Definitions.
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name, and
value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following list:

* entity_name = `FooEntity`
* property_name = `a_property_name`
* property_type = `a_property_type`

## Task Description
Your task is to add a new property to the JPA Entity `${entity_name}`. The property name to add
is `${property_name}` with a type of `${property_type}`.

Complete the following tasks:
* Create a new flyway migration script to add the property to the database table.
* Update the JPA Entity with the new property named `${property_name}`.
* Update the corresponding DTO with the new property named `${property_name}`.
* Update the OpenAPI documentation with the new property named `${property_name}` .
* Verify all tests are passing.
* Verify the OpenAPI specification is valid with npm test.