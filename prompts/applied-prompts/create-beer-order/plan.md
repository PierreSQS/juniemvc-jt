# Beer Order Management System Implementation Plan

## Overview
This document outlines the detailed implementation plan for adding Beer Order functionality to the existing Spring Boot application. The plan follows the requirements specified in `requirements.md` and aligns with the current project structure and patterns.

## Current State Analysis
The project currently has:
- Beer entity with basic fields
- BeerDto with validation annotations
- BeerMapper using MapStruct
- BeerRepository extending JpaRepository
- BeerService interface with basic CRUD operations
- BeerServiceImpl implementing BeerService
- BeerController with RESTful endpoints

## Implementation Plan

### 1. Create Base Entity
- Create an abstract `BaseEntity` class with common fields (id, version, createdDate, updateDate)
- Refactor the existing `Beer` entity to extend `BaseEntity`

### 2. Implement Entity Model
- Create `BeerOrder` entity with:
  - Fields: customerRef, paymentAmount, status
  - One-to-Many relationship with BeerOrderLine
  - Helper methods for managing bidirectional relationship
- Create `BeerOrderLine` entity with:
  - Fields: orderQuantity, quantityAllocated, status
  - Many-to-One relationship with BeerOrder
  - Many-to-One relationship with Beer
- Update `Beer` entity to include:
  - One-to-Many relationship with BeerOrderLine

### 3. Create Data Transfer Objects (DTOs)
- Create `BaseEntityDto` record with common fields
- Create `BeerOrderDto` record with:
  - Fields from BeerOrder
  - Collection of BeerOrderLineDto
  - Validation annotations
- Create `BeerOrderLineDto` record with:
  - Fields from BeerOrderLine
  - Validation annotations
- Update `BeerDto` to extend from `BaseEntityDto`

### 4. Implement Object Mappers
- Create `BeerOrderMapper` interface using MapStruct
- Create `BeerOrderLineMapper` interface using MapStruct
- Update `BeerMapper` to handle the relationship with BeerOrderLine

### 5. Create Repositories
- Create `BeerOrderRepository` extending JpaRepository
- Create `BeerOrderLineRepository` extending JpaRepository

### 6. Implement Service Layer
- Create `BeerOrderService` interface with methods for:
  - Getting all beer orders
  - Getting a beer order by ID
  - Creating a new beer order
  - Updating an existing beer order
  - Deleting a beer order
- Create `BeerOrderServiceImpl` implementing `BeerOrderService` with:
  - Constructor injection for dependencies
  - Transaction management annotations
  - Proper error handling and validation

### 7. Create Web Layer
- Create `BeerOrderController` with RESTful endpoints:
  - GET /api/v1/beer-orders - Get all beer orders
  - GET /api/v1/beer-orders/{id} - Get a beer order by ID
  - POST /api/v1/beer-orders - Create a new beer order
  - PUT /api/v1/beer-orders/{id} - Update an existing beer order
  - DELETE /api/v1/beer-orders/{id} - Delete a beer order
- Implement proper error handling and validation

### 8. Write Tests
- Write unit tests for:
  - Mappers
  - Repositories
  - Services
  - Controllers
- Write integration tests using Testcontainers

### 9. Configure JPA
- Ensure `spring.jpa.open-in-view=false` is set in application.properties
- Configure fetch strategies to avoid N+1 select problems

### 10. Implement Exception Handling
- Create a centralized exception handler with `@RestControllerAdvice`
- Define exception classes for domain-specific errors
- Return consistent error responses

## Implementation Sequence
1. Create BaseEntity and refactor Beer entity
2. Implement BeerOrder and BeerOrderLine entities
3. Create DTOs for the new entities
4. Implement Mappers for the new entities
5. Create Repositories for the new entities
6. Implement Service layer for beer orders
7. Create Controllers for beer orders
8. Write tests for all components
9. Configure JPA properties
10. Implement exception handling

## Best Practices to Follow
- Use constructor injection for all dependencies
- Declare dependencies as final fields
- Use package-private visibility when possible
- Define clear transaction boundaries at the service layer
- Use DTOs for request and response objects
- Apply consistent naming conventions
- Use proper logging with SLF4J

## Potential Challenges and Solutions
- **Bidirectional Relationship Management**: Implement helper methods in entities to manage both sides of relationships
- **N+1 Select Problem**: Use fetch joins or entity graphs in repository queries
- **Transaction Management**: Define clear transaction boundaries and use appropriate isolation levels
- **Error Handling**: Implement a centralized exception handler with consistent error responses