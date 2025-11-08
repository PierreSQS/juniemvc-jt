# Implementation Plan for BeerOrderShipment

This document outlines the detailed plan for implementing the BeerOrderShipment entity and related components as specified in the requirements.

## 1. Create BeerOrderShipment Entity

### 1.1 Create BeerOrderShipment Entity Class
- Create a new entity class `BeerOrderShipment` in the `guru.springframework.juniemvc.entities` package
- Extend `BaseEntity` to inherit common fields (id, version, createdDate, updateDate)
- Add required properties:
  - `shipmentDate` (LocalDateTime) - not null
  - `carrier` (String)
  - `trackingNumber` (String)
- Add ManyToOne relationship with BeerOrder
- Add Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)
- Add JPA annotations (@Entity, @ManyToOne, @JoinColumn)

### 1.2 Update BeerOrder Entity
- Add OneToMany relationship to BeerOrderShipment
- Add helper methods for adding and removing shipments (similar to BeerOrderLine)

## 2. Create Flyway Migration Script

### 2.1 Create Migration Script
- Create a new migration script `V3__add_beer_order_shipment_table.sql` in `src/main/resources/db/migration`
- Create the beer_order_shipment table with all required columns
- Add foreign key constraint to reference beer_order table

## 3. Create DTOs

### 3.1 Create BeerOrderShipmentDto
- Create a new DTO class `BeerOrderShipmentDto` in the `guru.springframework.juniemvc.models` package
- Extend `BaseEntityDto`
- Add properties matching the entity (shipmentDate, carrier, trackingNumber)
- Add Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 4. Create Mappers

### 4.1 Create BeerOrderShipmentMapper
- Create a new mapper interface `BeerOrderShipmentMapper` in the `guru.springframework.juniemvc.mappers` package
- Use MapStruct to generate the implementation
- Add methods for mapping between entity and DTO in both directions

### 4.2 Update BeerOrderMapper
- Update the BeerOrderMapper to include mapping for the shipments collection

## 5. Create Repository

### 5.1 Create BeerOrderShipmentRepository
- Create a new repository interface `BeerOrderShipmentRepository` in the `guru.springframework.juniemvc.repositories` package
- Extend JpaRepository with BeerOrderShipment and Integer types
- Add method to find shipments by beer order id

## 6. Create Service Layer

### 6.1 Create BeerOrderShipmentService Interface
- Create a new service interface `BeerOrderShipmentService` in the `guru.springframework.juniemvc.services` package
- Define CRUD operations for BeerOrderShipment
- Include methods that take beerOrderId as a parameter

### 6.2 Create BeerOrderShipmentServiceImpl
- Create a new service implementation `BeerOrderShipmentServiceImpl` in the `guru.springframework.juniemvc.services` package
- Implement the BeerOrderShipmentService interface
- Use constructor injection for dependencies (repository, mapper)
- Implement all CRUD operations
- Add proper error handling and validation

## 7. Create Controller

### 7.1 Create BeerOrderShipmentController
- Create a new controller `BeerOrderShipmentController` in the `guru.springframework.juniemvc.controllers` package
- Use the path "/api/v1/beer-orders/{beerOrderId}/shipments" as specified in requirements
- Implement CRUD endpoints (GET, POST, PUT, DELETE)
- Use proper HTTP status codes and response entities
- Add validation for request bodies

## 8. Create Tests

### 8.1 Create Entity Tests
- Create tests for the BeerOrderShipment entity
- Test the relationship with BeerOrder

### 8.2 Create Repository Tests
- Create tests for the BeerOrderShipmentRepository
- Test finding shipments by beer order id

### 8.3 Create Mapper Tests
- Create tests for the BeerOrderShipmentMapper
- Test mapping between entity and DTO in both directions

### 8.4 Create Service Tests
- Create tests for the BeerOrderShipmentServiceImpl
- Test all CRUD operations
- Test error handling and validation

### 8.5 Create Controller Tests
- Create tests for the BeerOrderShipmentController
- Test all endpoints
- Test validation and error handling

## 9. Update OpenAPI Documentation

### 9.1 Update OpenAPI Specification
- Create a new path file for the BeerOrderShipment endpoints in the OpenAPI documentation
- Add schema definitions for BeerOrderShipmentDto
- Update the existing BeerOrder schema to include the shipments collection
- Add proper descriptions, examples, and response definitions

## 10. Verify Implementation

### 10.1 Run All Tests
- Run all tests to ensure everything is working correctly
- Fix any issues that arise

### 10.2 Manual Testing
- Manually test the API endpoints using a tool like Postman or curl
- Verify that all CRUD operations work as expected
- Verify that the relationship between BeerOrder and BeerOrderShipment is maintained correctly