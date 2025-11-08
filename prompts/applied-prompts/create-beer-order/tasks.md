# Beer Order Management System Implementation Tasks

This document contains a detailed task list for implementing the Beer Order Management System according to the plan outlined in `plan.md` and the requirements in `requirements.md`.

## 1. Create Base Entity and Refactor Beer Entity
- [x] 1.1. Create abstract `BaseEntity` class with common fields:
  - [x] 1.1.1. ID field with auto-generation
  - [x] 1.1.2. Version field for optimistic locking
  - [x] 1.1.3. Creation timestamp
  - [x] 1.1.4. Update timestamp
- [x] 1.2. Refactor existing `Beer` entity to extend `BaseEntity`
- [x] 1.3. Add proper annotations (JPA, Lombok)

## 2. Implement Entity Model
- [x] 2.1. Create `BeerOrder` entity:
  - [x] 2.1.1. Add fields: customerRef, paymentAmount, status
  - [x] 2.1.2. Configure One-to-Many relationship with BeerOrderLine
  - [x] 2.1.3. Implement helper methods for managing bidirectional relationship
  - [x] 2.1.4. Add proper annotations (JPA, Lombok)
- [x] 2.2. Create `BeerOrderLine` entity:
  - [x] 2.2.1. Add fields: orderQuantity, quantityAllocated, status
  - [x] 2.2.2. Configure Many-to-One relationship with BeerOrder
  - [x] 2.2.3. Configure Many-to-One relationship with Beer
  - [x] 2.2.4. Add proper annotations (JPA, Lombok)
- [x] 2.3. Update `Beer` entity:
  - [x] 2.3.1. Add One-to-Many relationship with BeerOrderLine
  - [x] 2.3.2. Ensure proper initialization of collections

## 3. Create Data Transfer Objects (DTOs)
- [x] 3.1. Create `BaseEntityDto` record with common fields
- [x] 3.2. Create `BeerOrderDto` record:
  - [x] 3.2.1. Include fields from BeerOrder
  - [x] 3.2.2. Add collection of BeerOrderLineDto
  - [x] 3.2.3. Apply validation annotations
- [x] 3.3. Create `BeerOrderLineDto` record:
  - [x] 3.3.1. Include fields from BeerOrderLine
  - [x] 3.3.2. Apply validation annotations
- [x] 3.4. Update `BeerDto` to extend from `BaseEntityDto`

## 4. Implement Object Mappers
- [x] 4.1. Create `BeerOrderMapper` interface using MapStruct:
  - [x] 4.1.1. Define methods for mapping between BeerOrder and BeerOrderDto
  - [x] 4.1.2. Configure proper handling of bidirectional relationships
- [x] 4.2. Create `BeerOrderLineMapper` interface using MapStruct:
  - [x] 4.2.1. Define methods for mapping between BeerOrderLine and BeerOrderLineDto
- [x] 4.3. Update `BeerMapper` to handle the relationship with BeerOrderLine

## 5. Create Repositories
- [x] 5.1. Create `BeerOrderRepository` extending JpaRepository:
  - [x] 5.1.1. Define any custom query methods if needed
- [x] 5.2. Create `BeerOrderLineRepository` extending JpaRepository:
  - [x] 5.2.1. Define any custom query methods if needed

## 6. Implement Service Layer
- [x] 6.1. Create `BeerOrderService` interface with methods for:
  - [x] 6.1.1. Getting all beer orders
  - [x] 6.1.2. Getting a beer order by ID
  - [x] 6.1.3. Creating a new beer order
  - [x] 6.1.4. Updating an existing beer order
  - [x] 6.1.5. Deleting a beer order
- [x] 6.2. Create `BeerOrderServiceImpl` implementing `BeerOrderService`:
  - [x] 6.2.1. Implement constructor injection for dependencies
  - [x] 6.2.2. Add transaction management annotations
  - [x] 6.2.3. Implement proper error handling and validation
  - [x] 6.2.4. Implement all required methods

## 7. Create Web Layer
- [x] 7.1. Create `BeerOrderController` with RESTful endpoints:
  - [x] 7.1.1. GET /api/v1/beer-orders - Get all beer orders
  - [x] 7.1.2. GET /api/v1/beer-orders/{id} - Get a beer order by ID
  - [x] 7.1.3. POST /api/v1/beer-orders - Create a new beer order
  - [x] 7.1.4. PUT /api/v1/beer-orders/{id} - Update an existing beer order
  - [x] 7.1.5. DELETE /api/v1/beer-orders/{id} - Delete a beer order
- [x] 7.2. Implement proper error handling and validation
- [x] 7.3. Use ResponseEntity<T> to return proper status codes

## 8. Write Tests
- [x] 8.1. Write unit tests for mappers:
  - [x] 8.1.1. Test BeerOrderMapper
  - [x] 8.1.2. Test BeerOrderLineMapper
  - [x] 8.1.3. Test updated BeerMapper
- [x] 8.2. Write unit tests for repositories:
  - [x] 8.2.1. Test BeerOrderRepository
  - [x] 8.2.2. Test BeerOrderLineRepository
- [x] 8.3. Write unit tests for services:
  - [x] 8.3.1. Test BeerOrderServiceImpl
- [x] 8.4. Write unit tests for controllers:
  - [x] 8.4.1. Test BeerOrderController
- [ ] 8.5. Write integration tests using Testcontainers

## 9. Configure JPA
- [x] 9.1. Ensure `spring.jpa.open-in-view=false` is set in application.properties
- [x] 9.2. Configure fetch strategies to avoid N+1 select problems:
  - [x] 9.2.1. Use fetch joins or entity graphs in repository queries
  - [x] 9.2.2. Configure appropriate fetch types for entity relationships

## 10. Implement Exception Handling
- [x] 10.1. Create a centralized exception handler with `@RestControllerAdvice`
- [x] 10.2. Define exception classes for domain-specific errors
- [x] 10.3. Return consistent error responses
- [x] 10.4. Consider using the ProblemDetails format (RFC 9457)
