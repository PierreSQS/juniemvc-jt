# BeerOrderShipment Implementation Tasks

## 1. Create BeerOrderShipment Entity
- [x] 1.1. Create BeerOrderShipment Entity Class
  - [x] Create a new entity class `BeerOrderShipment` in the `guru.springframework.juniemvc.entities` package
  - [x] Extend `BaseEntity` to inherit common fields (id, version, createdDate, updateDate)
  - [x] Add required properties:
    - [x] `shipmentDate` (LocalDateTime) - not null
    - [x] `carrier` (String)
    - [x] `trackingNumber` (String)
  - [x] Add ManyToOne relationship with BeerOrder
  - [x] Add Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)
  - [x] Add JPA annotations (@Entity, @ManyToOne, @JoinColumn)

- [x] 1.2. Update BeerOrder Entity
  - [x] Add OneToMany relationship to BeerOrderShipment
  - [x] Add helper methods for adding and removing shipments (similar to BeerOrderLine)

## 2. Create Flyway Migration Script
- [x] 2.1. Create Migration Script
  - [x] Create a new migration script `V3__add_beer_order_shipment_table.sql` in `src/main/resources/db/migration`
  - [x] Create the beer_order_shipment table with all required columns
  - [x] Add foreign key constraint to reference beer_order table

## 3. Create DTOs
- [x] 3.1. Create BeerOrderShipmentDto
  - [x] Create a new DTO class `BeerOrderShipmentDto` in the `guru.springframework.juniemvc.models` package
  - [x] Extend `BaseEntityDto`
  - [x] Add properties matching the entity (shipmentDate, carrier, trackingNumber)
  - [x] Add Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder)

## 4. Create Mappers
- [x] 4.1. Create BeerOrderShipmentMapper
  - [x] Create a new mapper interface `BeerOrderShipmentMapper` in the `guru.springframework.juniemvc.mappers` package
  - [x] Use MapStruct to generate the implementation
  - [x] Add methods for mapping between entity and DTO in both directions

- [x] 4.2. Update BeerOrderMapper
  - [x] Update the BeerOrderMapper to include mapping for the shipments collection

## 5. Create Repository
- [x] 5.1. Create BeerOrderShipmentRepository
  - [x] Create a new repository interface `BeerOrderShipmentRepository` in the `guru.springframework.juniemvc.repositories` package
  - [x] Extend JpaRepository with BeerOrderShipment and Integer types
  - [x] Add method to find shipments by beer order id

## 6. Create Service Layer
- [x] 6.1. Create BeerOrderShipmentService Interface
  - [x] Create a new service interface `BeerOrderShipmentService` in the `guru.springframework.juniemvc.services` package
  - [x] Define CRUD operations for BeerOrderShipment
  - [x] Include methods that take beerOrderId as a parameter

- [x] 6.2. Create BeerOrderShipmentServiceImpl
  - [x] Create a new service implementation `BeerOrderShipmentServiceImpl` in the `guru.springframework.juniemvc.services` package
  - [x] Implement the BeerOrderShipmentService interface
  - [x] Use constructor injection for dependencies (repository, mapper)
  - [x] Implement all CRUD operations
  - [x] Add proper error handling and validation

## 7. Create Controller
- [x] 7.1. Create BeerOrderShipmentController
  - [x] Create a new controller `BeerOrderShipmentController` in the `guru.springframework.juniemvc.controllers` package
  - [x] Use the path "/api/v1/beer-orders/{beerOrderId}/shipments" as specified in requirements
  - [x] Implement CRUD endpoints (GET, POST, PUT, DELETE)
  - [x] Use proper HTTP status codes and response entities
  - [x] Add validation for request bodies

## 8. Create Tests
- [x] 8.1. Create Entity Tests
  - [x] Create tests for the BeerOrderShipment entity
  - [x] Test the relationship with BeerOrder

- [x] 8.2. Create Repository Tests
  - [x] Create tests for the BeerOrderShipmentRepository
  - [x] Test finding shipments by beer order id

- [x] 8.3. Create Mapper Tests
  - [x] Create tests for the BeerOrderShipmentMapper
  - [x] Test mapping between entity and DTO in both directions

- [x] 8.4. Create Service Tests
  - [x] Create tests for the BeerOrderShipmentServiceImpl
  - [x] Test all CRUD operations
  - [x] Test error handling and validation

- [x] 8.5. Create Controller Tests
  - [x] Create tests for the BeerOrderShipmentController
  - [x] Test all endpoints
  - [x] Test validation and error handling

## 9. Update OpenAPI Documentation
- [x] 9.1. Update OpenAPI Specification
  - [x] Create a new path file for the BeerOrderShipment endpoints in the OpenAPI documentation
  - [x] Add schema definitions for BeerOrderShipmentDto
  - [x] Update the existing BeerOrder schema to include the shipments collection
  - [x] Add proper descriptions, examples, and response definitions

## 10. Verify Implementation
- [x] 10.1. Run All Tests
  - [x] Run all tests to ensure everything is working correctly
  - [x] Fix any issues that arise

- [x] 10.2. Manual Testing
  - [x] Manually test the API endpoints using a tool like Postman or curl
  - [x] Verify that all CRUD operations work as expected
  - [x] Verify that the relationship between BeerOrder and BeerOrderShipment is maintained correctly
