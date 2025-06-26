# Beer Ordering System Implementation Tasks

This document contains a detailed task list for implementing the beer ordering system based on the plan in `plan.md`.

## 1. Entity Layer Implementation
1. [x] Beer entity (already implemented)
2. [x] Create Customer entity
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define name field (String)
   - [x] Define email field (String)
   - [x] Define orders field (OneToMany relationship with BeerOrder)
   - [x] Define createdDate and lastModifiedDate fields
   - [x] Add JPA annotations
   - [ ] Implement equals/hashCode methods
   - [x] Add Lombok annotations
3. [x] Create BeerOrder entity
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define orderStatus field (Enum)
   - [x] Define customer field (ManyToOne relationship with Customer)
   - [x] Define orderLines field (OneToMany relationship with BeerOrderLine)
   - [x] Define createdDate and lastModifiedDate fields
   - [x] Add JPA annotations
   - [x] Implement helper method to maintain bidirectional relationship with BeerOrderLine
   - [ ] Implement equals/hashCode methods
   - [x] Add Lombok annotations
4. [x] Create BeerOrderLine entity
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define orderQuantity field (Integer)
   - [x] Define beerOrder field (ManyToOne relationship with BeerOrder)
   - [x] Define beer field (ManyToOne relationship with Beer)
   - [x] Define createdDate and lastModifiedDate fields
   - [x] Add JPA annotations
   - [ ] Implement equals/hashCode methods
   - [x] Add Lombok annotations

## 2. Repository Layer Implementation
1. [x] BeerRepository (already implemented)
2. [x] Create CustomerRepository
   - [x] Extend JpaRepository<Customer, Integer>
   - [x] Add any custom query methods if needed
3. [x] Create BeerOrderRepository
   - [x] Extend JpaRepository<BeerOrder, Integer>
   - [x] Add method to find orders by customer
   - [x] Add any other custom query methods if needed
4. [x] Create BeerOrderLineRepository
   - [x] Extend JpaRepository<BeerOrderLine, Integer>
   - [x] Add any custom query methods if needed

## 3. DTO Layer Implementation
1. [x] BeerDto (already implemented)
2. [x] Create CustomerDto
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define name field (String)
   - [x] Define email field (String)
   - [x] Define createdDate and lastModifiedDate fields
   - [ ] Add validation annotations
   - [x] Add Lombok annotations
3. [x] Create BeerOrderDto
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define orderStatus field (Enum)
   - [x] Define customerId field (Integer)
   - [x] Define customerName field (String)
   - [x] Define orderLines field (List of BeerOrderLineDto)
   - [x] Define createdDate and lastModifiedDate fields
   - [ ] Add validation annotations
   - [x] Add Lombok annotations
4. [x] Create BeerOrderLineDto
   - [x] Define id field (Integer)
   - [x] Define version field (Integer)
   - [x] Define orderId field (Integer)
   - [x] Define beerId field (Integer)
   - [x] Define beerName field (String)
   - [x] Define orderQuantity field (Integer)
   - [ ] Add validation annotations
   - [x] Add Lombok annotations

## 4. Mapper Layer Implementation
1. [x] BeerMapper (already implemented)
2. [x] Create CustomerMapper
   - [x] Define interface with MapStruct annotations
   - [x] Add method to convert Customer to CustomerDto
   - [x] Add method to convert CustomerDto to Customer
3. [x] Create BeerOrderMapper
   - [x] Define interface with MapStruct annotations
   - [x] Add method to convert BeerOrder to BeerOrderDto
   - [x] Add method to convert BeerOrderDto to BeerOrder
   - [x] Configure to use BeerOrderLineMapper and CustomerMapper
4. [x] Create BeerOrderLineMapper
   - [x] Define interface with MapStruct annotations
   - [x] Add method to convert BeerOrderLine to BeerOrderLineDto
   - [x] Add method to convert BeerOrderLineDto to BeerOrderLine
   - [x] Configure to use BeerMapper

## 5. Service Layer Implementation
1. [x] BeerService and BeerServiceImpl (already implemented)
2. [x] Create CustomerService interface
   - [x] Define method to get all customers
   - [x] Define method to get customer by ID
   - [x] Define method to save customer
   - [x] Define method to update customer
   - [x] Define method to delete customer
3. [x] Create CustomerServiceImpl
   - [x] Implement CustomerService interface
   - [x] Add constructor injection for CustomerRepository and CustomerMapper
   - [x] Add @Transactional annotations
   - [x] Add @Transactional(readOnly = true) for query-only methods
   - [x] Implement business logic for all methods
4. [x] Create BeerOrderService interface
   - [x] Define method to get all orders
   - [x] Define method to get order by ID
   - [x] Define method to get orders by customer
   - [x] Define method to save order
   - [x] Define method to update order
   - [x] Define method to delete order
5. [x] Create BeerOrderServiceImpl
   - [x] Implement BeerOrderService interface
   - [x] Add constructor injection for BeerOrderRepository, BeerOrderMapper, and other dependencies
   - [x] Add @Transactional annotations
   - [x] Add @Transactional(readOnly = true) for query-only methods
   - [x] Implement business logic for all methods
   - [x] Handle relationships between entities

## 6. Controller Layer Implementation
1. [x] BeerController (already implemented)
2. [x] Create CustomerController
   - [x] Add constructor injection for CustomerService
   - [x] Implement endpoint to get all customers
   - [x] Implement endpoint to get customer by ID
   - [x] Implement endpoint to create customer
   - [x] Implement endpoint to update customer
   - [x] Implement endpoint to delete customer
   - [x] Add appropriate HTTP status codes
   - [ ] Add validation handling
3. [x] Create BeerOrderController
   - [x] Add constructor injection for BeerOrderService
   - [x] Implement endpoint to get all orders
   - [x] Implement endpoint to get order by ID
   - [x] Implement endpoint to get orders by customer
   - [x] Implement endpoint to create order
   - [x] Implement endpoint to update order
   - [x] Implement endpoint to delete order
   - [x] Add appropriate HTTP status codes
   - [ ] Add validation handling

## 7. Testing
1. [x] Repository Tests
   - [x] Create CustomerRepositoryTest
   - [x] Create BeerOrderRepositoryTest
   - [x] Create BeerOrderLineRepositoryTest
   - [x] Test CRUD operations for each repository
   - [x] Test custom query methods
2. [ ] Service Tests
   - [ ] Create CustomerServiceImplTest
   - [ ] Create BeerOrderServiceImplTest
   - [ ] Test business logic in service implementations
   - [ ] Test error handling
3. [ ] Controller Tests
   - [ ] Create CustomerControllerTest
   - [ ] Create BeerOrderControllerTest
   - [ ] Test RESTful endpoints
   - [ ] Test validation handling
   - [ ] Test error responses

## 8. Documentation and Validation
1. [ ] Add Javadoc comments to all interfaces and public methods
2. [ ] Add validation annotations to DTOs
   - [ ] Add validation to CustomerDto
   - [ ] Add validation to BeerOrderDto
   - [ ] Add validation to BeerOrderLineDto
3. [ ] Configure global exception handling
   - [ ] Create GlobalExceptionHandler class
   - [ ] Handle validation exceptions
   - [ ] Handle resource not found exceptions
   - [ ] Handle general exceptions
   - [ ] Return appropriate HTTP status codes and error messages

## 9. Final Review and Testing
1. [ ] Review code for adherence to best practices
2. [ ] Run all tests to ensure functionality
3. [ ] Perform manual testing of the API
4. [ ] Check for any performance issues
5. [ ] Ensure proper error handling throughout the application
