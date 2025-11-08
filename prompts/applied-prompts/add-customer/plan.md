# Customer Entity Implementation Plan

## Overview
This plan outlines the steps required to add a new Customer entity to the project, including all necessary components for a complete CRUD implementation following Spring Boot best practices.

## 1. Create Customer Entity
- Create a new Customer JPA entity that extends BaseEntity
- Implement the following properties:
  - name (String, not null)
  - email (String)
  - phoneNumber (String)
  - addressLine1 (String, not null)
  - addressLine2 (String)
  - city (String, not null)
  - state (String, not null)
  - postalCode (String, not null)
- Establish a OneToMany relationship with BeerOrder

## 2. Update BeerOrder Entity
- Add a ManyToOne relationship to Customer
- Update the BeerOrder entity to reference Customer instead of using customerRef

## 3. Create Flyway Migration Script
- Create a new migration script (V2__add_customer_table.sql) to:
  - Create the customer table with all required fields
  - Alter the beer_order table to add a foreign key reference to the customer table

## 4. Create DTO and Mapper
- Create CustomerDto class extending BaseEntityDto
- Create CustomerMapper interface using MapStruct
- Implement bidirectional mapping between Customer entity and CustomerDto

## 5. Create Repository
- Create CustomerRepository interface extending JpaRepository
- Add any necessary custom query methods

## 6. Create Service Layer
- Create CustomerService interface
- Create CustomerServiceImpl class implementing CustomerService
- Implement CRUD operations:
  - getAllCustomers
  - getCustomerById
  - saveCustomer
  - updateCustomer
  - deleteCustomer

## 7. Create Controller
- Create CustomerController class
- Implement RESTful endpoints:
  - GET /api/v1/customers - Get all customers
  - GET /api/v1/customers/{id} - Get customer by ID
  - POST /api/v1/customers - Create new customer
  - PUT /api/v1/customers/{id} - Update existing customer
  - DELETE /api/v1/customers/{id} - Delete customer

## 8. Update OpenAPI Documentation
- Add Customer tag to openapi.yaml
- Create path files for Customer operations:
  - customers.yaml (GET all, POST)
  - customers_{id}.yaml (GET by ID, PUT, DELETE)
- Create schema file for CustomerDto

## 9. Write Tests
- Write unit tests for:
  - CustomerMapper
  - CustomerService
  - CustomerController
- Write integration tests for:
  - CustomerRepository
  - CustomerController (with MockMvc)

## 10. Verify Implementation
- Run all tests to ensure they pass
- Verify that the application builds successfully
- Test the API endpoints manually using a tool like Postman or curl

## Implementation Guidelines
Follow these Spring Boot best practices:

1. **Constructor Injection**: Use constructor injection for dependencies, making fields final.
2. **Package-private Visibility**: Use package-private visibility for components when possible.
3. **Typed Properties**: Group configuration properties with a common prefix.
4. **Clear Transaction Boundaries**: Define service methods as transactional units.
5. **Disable Open Session in View**: Set spring.jpa.open-in-view=false.
6. **Separate Web and Persistence Layers**: Don't expose entities directly in controllers.
7. **REST API Design**: Follow REST principles for endpoint design.
8. **Command Objects**: Use purpose-built command objects for business operations.
9. **Centralized Exception Handling**: Implement global exception handling.
10. **Proper Logging**: Use SLF4J for logging, not System.out.println().