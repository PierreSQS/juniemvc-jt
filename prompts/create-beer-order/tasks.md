# Beer Ordering System Implementation Tasks

This document contains a detailed task list for implementing the beer ordering system based on the plan in `plan.md`.

## 1. Entity Layer Implementation
1. [ ] Beer entity (already implemented)
2. [ ] Create Customer entity
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define name field (String)
   - [ ] Define email field (String)
   - [ ] Define orders field (OneToMany relationship with BeerOrder)
   - [ ] Define createdDate and lastModifiedDate fields
   - [ ] Add JPA annotations
   - [ ] Implement equals/hashCode methods
   - [ ] Add Lombok annotations
3. [ ] Create BeerOrder entity
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define orderStatus field (Enum)
   - [ ] Define customer field (ManyToOne relationship with Customer)
   - [ ] Define orderLines field (OneToMany relationship with BeerOrderLine)
   - [ ] Define createdDate and lastModifiedDate fields
   - [ ] Add JPA annotations
   - [ ] Implement helper method to maintain bidirectional relationship with BeerOrderLine
   - [ ] Implement equals/hashCode methods
   - [ ] Add Lombok annotations
4. [ ] Create BeerOrderLine entity
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define orderQuantity field (Integer)
   - [ ] Define beerOrder field (ManyToOne relationship with BeerOrder)
   - [ ] Define beer field (ManyToOne relationship with Beer)
   - [ ] Define createdDate and lastModifiedDate fields
   - [ ] Add JPA annotations
   - [ ] Implement equals/hashCode methods
   - [ ] Add Lombok annotations

## 2. Repository Layer Implementation
1. [ ] BeerRepository (already implemented)
2. [ ] Create CustomerRepository
   - [ ] Extend JpaRepository<Customer, Integer>
   - [ ] Add any custom query methods if needed
3. [ ] Create BeerOrderRepository
   - [ ] Extend JpaRepository<BeerOrder, Integer>
   - [ ] Add method to find orders by customer
   - [ ] Add any other custom query methods if needed
4. [ ] Create BeerOrderLineRepository
   - [ ] Extend JpaRepository<BeerOrderLine, Integer>
   - [ ] Add any custom query methods if needed

## 3. DTO Layer Implementation
1. [ ] BeerDto (already implemented)
2. [ ] Create CustomerDto
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define name field (String)
   - [ ] Define email field (String)
   - [ ] Define createdDate and lastModifiedDate fields
   - [ ] Add validation annotations
   - [ ] Add Lombok annotations
3. [ ] Create BeerOrderDto
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define orderStatus field (Enum)
   - [ ] Define customerId field (Integer)
   - [ ] Define customerName field (String)
   - [ ] Define orderLines field (List of BeerOrderLineDto)
   - [ ] Define createdDate and lastModifiedDate fields
   - [ ] Add validation annotations
   - [ ] Add Lombok annotations
4. [ ] Create BeerOrderLineDto
   - [ ] Define id field (Integer)
   - [ ] Define version field (Integer)
   - [ ] Define orderId field (Integer)
   - [ ] Define beerId field (Integer)
   - [ ] Define beerName field (String)
   - [ ] Define orderQuantity field (Integer)
   - [ ] Add validation annotations
   - [ ] Add Lombok annotations

## 4. Mapper Layer Implementation
1. [ ] BeerMapper (already implemented)
2. [ ] Create CustomerMapper
   - [ ] Define interface with MapStruct annotations
   - [ ] Add method to convert Customer to CustomerDto
   - [ ] Add method to convert CustomerDto to Customer
3. [ ] Create BeerOrderMapper
   - [ ] Define interface with MapStruct annotations
   - [ ] Add method to convert BeerOrder to BeerOrderDto
   - [ ] Add method to convert BeerOrderDto to BeerOrder
   - [ ] Configure to use BeerOrderLineMapper and CustomerMapper
4. [ ] Create BeerOrderLineMapper
   - [ ] Define interface with MapStruct annotations
   - [ ] Add method to convert BeerOrderLine to BeerOrderLineDto
   - [ ] Add method to convert BeerOrderLineDto to BeerOrderLine
   - [ ] Configure to use BeerMapper

## 5. Service Layer Implementation
1. [ ] BeerService and BeerServiceImpl (already implemented)
2. [ ] Create CustomerService interface
   - [ ] Define method to get all customers
   - [ ] Define method to get customer by ID
   - [ ] Define method to save customer
   - [ ] Define method to update customer
   - [ ] Define method to delete customer
3. [ ] Create CustomerServiceImpl
   - [ ] Implement CustomerService interface
   - [ ] Add constructor injection for CustomerRepository and CustomerMapper
   - [ ] Add @Transactional annotations
   - [ ] Add @Transactional(readOnly = true) for query-only methods
   - [ ] Implement business logic for all methods
4. [ ] Create BeerOrderService interface
   - [ ] Define method to get all orders
   - [ ] Define method to get order by ID
   - [ ] Define method to get orders by customer
   - [ ] Define method to save order
   - [ ] Define method to update order
   - [ ] Define method to delete order
5. [ ] Create BeerOrderServiceImpl
   - [ ] Implement BeerOrderService interface
   - [ ] Add constructor injection for BeerOrderRepository, BeerOrderMapper, and other dependencies
   - [ ] Add @Transactional annotations
   - [ ] Add @Transactional(readOnly = true) for query-only methods
   - [ ] Implement business logic for all methods
   - [ ] Handle relationships between entities

## 6. Controller Layer Implementation
1. [ ] BeerController (already implemented)
2. [ ] Create CustomerController
   - [ ] Add constructor injection for CustomerService
   - [ ] Implement endpoint to get all customers
   - [ ] Implement endpoint to get customer by ID
   - [ ] Implement endpoint to create customer
   - [ ] Implement endpoint to update customer
   - [ ] Implement endpoint to delete customer
   - [ ] Add appropriate HTTP status codes
   - [ ] Add validation handling
3. [ ] Create BeerOrderController
   - [ ] Add constructor injection for BeerOrderService
   - [ ] Implement endpoint to get all orders
   - [ ] Implement endpoint to get order by ID
   - [ ] Implement endpoint to get orders by customer
   - [ ] Implement endpoint to create order
   - [ ] Implement endpoint to update order
   - [ ] Implement endpoint to delete order
   - [ ] Add appropriate HTTP status codes
   - [ ] Add validation handling

## 7. Testing
1. [ ] Repository Tests
   - [ ] Create CustomerRepositoryTest
   - [ ] Create BeerOrderRepositoryTest
   - [ ] Create BeerOrderLineRepositoryTest
   - [ ] Test CRUD operations for each repository
   - [ ] Test custom query methods
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