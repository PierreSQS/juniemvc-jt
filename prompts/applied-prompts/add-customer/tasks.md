# Customer Entity Implementation Tasks

## 1. Create Customer Entity
- [x] 1.1. Create a new Customer JPA entity that extends BaseEntity
- [x] 1.2. Implement the following properties:
  - [x] 1.2.1. name (String, not null)
  - [x] 1.2.2. email (String)
  - [x] 1.2.3. phoneNumber (String)
  - [x] 1.2.4. addressLine1 (String, not null)
  - [x] 1.2.5. addressLine2 (String)
  - [x] 1.2.6. city (String, not null)
  - [x] 1.2.7. state (String, not null)
  - [x] 1.2.8. postalCode (String, not null)
- [x] 1.3. Establish a OneToMany relationship with BeerOrder

## 2. Update BeerOrder Entity
- [x] 2.1. Add a ManyToOne relationship to Customer
- [x] 2.2. Update the BeerOrder entity to reference Customer instead of using customerRef

## 3. Create Flyway Migration Script
- [x] 3.1. Create a new migration script (V2__add_customer_table.sql) to:
  - [x] 3.1.1. Create the customer table with all required fields
  - [x] 3.1.2. Alter the beer_order table to add a foreign key reference to the customer table

## 4. Create DTO and Mapper
- [x] 4.1. Create CustomerDto class extending BaseEntityDto
- [x] 4.2. Create CustomerMapper interface using MapStruct
- [x] 4.3. Implement bidirectional mapping between Customer entity and CustomerDto

## 5. Create Repository
- [x] 5.1. Create CustomerRepository interface extending JpaRepository
- [x] 5.2. Add any necessary custom query methods

## 6. Create Service Layer
- [x] 6.1. Create CustomerService interface
- [x] 6.2. Create CustomerServiceImpl class implementing CustomerService
- [x] 6.3. Implement CRUD operations:
  - [x] 6.3.1. getAllCustomers
  - [x] 6.3.2. getCustomerById
  - [x] 6.3.3. saveCustomer
  - [x] 6.3.4. updateCustomer
  - [x] 6.3.5. deleteCustomer

## 7. Create Controller
- [x] 7.1. Create CustomerController class
- [x] 7.2. Implement RESTful endpoints:
  - [x] 7.2.1. GET /api/v1/customers - Get all customers
  - [x] 7.2.2. GET /api/v1/customers/{id} - Get customer by ID
  - [x] 7.2.3. POST /api/v1/customers - Create new customer
  - [x] 7.2.4. PUT /api/v1/customers/{id} - Update existing customer
  - [x] 7.2.5. DELETE /api/v1/customers/{id} - Delete customer

## 8. Update OpenAPI Documentation
- [x] 8.1. Add Customer tag to openapi.yaml
- [x] 8.2. Create path files for Customer operations:
  - [x] 8.2.1. customers.yaml (GET all, POST)
  - [x] 8.2.2. customers_{id}.yaml (GET by ID, PUT, DELETE)
- [x] 8.3. Create schema file for CustomerDto

## 9. Write Tests
- [x] 9.1. Write unit tests for:
  - [x] 9.1.1. CustomerMapper
  - [x] 9.1.2. CustomerService
  - [x] 9.1.3. CustomerController
- [x] 9.2. Write integration tests for:
  - [x] 9.2.1. CustomerRepository
  - [x] 9.2.2. CustomerController (with MockMvc)

## 10. Verify Implementation
- [x] 10.1. Run all tests to ensure they pass
- [x] 10.2. Verify that the application builds successfully
- [x] 10.3. Test the API endpoints manually using a tool like Postman or curl

## Implementation Guidelines
- [x] 11.1. Use constructor injection for dependencies, making fields final
- [x] 11.2. Use package-private visibility for components when possible
- [x] 11.3. Group configuration properties with a common prefix
- [x] 11.4. Define service methods as transactional units
- [x] 11.5. Set spring.jpa.open-in-view=false
- [x] 11.6. Don't expose entities directly in controllers
- [x] 11.7. Follow REST principles for endpoint design
- [x] 11.8. Use purpose-built command objects for business operations
- [x] 11.9. Implement global exception handling
- [x] 11.10. Use SLF4J for logging, not System.out.println()
