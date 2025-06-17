# Requirements for Adding DTOs to the Beer API

## Overview
This document outlines the requirements for refactoring the Beer API to use Data Transfer Objects (DTOs) instead of directly exposing JPA entities in the REST API. This change will improve the separation of concerns, enhance API stability, and provide better control over what data is exposed to clients.

## Detailed Requirements

### 1. Create BeerDto Class
- Create a new POJO class called `BeerDto` in a new package `guru.springframework.juniemvc.models`
- The DTO should include the following properties:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal price
  - LocalDateTime createdDate
  - LocalDateTime updateDate
- Apply the following Lombok annotations to the DTO:
  - `@Getter`
  - `@Setter`
  - `@Builder`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

### 2. Create MapStruct Mapper
- Create a new MapStruct mapper interface called `BeerMapper` in a new package `guru.springframework.juniemvc.mappers`
- The mapper should provide methods to convert between `Beer` entity and `BeerDto`
- When mapping from `BeerDto` to `Beer` entity, ignore the following properties:
  - id
  - createdDate
  - updateDate
- Configure the mapper with the `@Mapper` annotation using `componentModel = "spring"` to make it available for dependency injection

### 3. Update Service Layer
- Modify the `BeerService` interface to use `BeerDto` instead of `Beer` entity:
  - Change return types from `Beer` to `BeerDto`
  - Change parameter types from `Beer` to `BeerDto`
- Update the `BeerServiceImpl` class to:
  - Inject the `BeerMapper`
  - Use the mapper to convert between DTOs and entities
  - Maintain the same business logic but operate on DTOs at the service boundary

### 4. Update Controller Layer
- Modify the `BeerController` to use `BeerDto` instead of `Beer` entity:
  - Change return types from `Beer` to `BeerDto`
  - Change parameter types from `Beer` to `BeerDto`
- Ensure all endpoints continue to function as before but with DTOs instead of entities

### 5. Testing
- Update existing tests to work with the new DTO-based approach
- Ensure all tests pass with the refactored code

## Implementation Guidelines
- Use constructor injection for all dependencies
- Maintain the existing transaction boundaries
- Ensure that the API contract remains stable from the client's perspective
- Follow the existing code style and conventions

## Benefits
- Improved separation of concerns between API and persistence layers
- Better control over what data is exposed to clients
- Enhanced API stability by decoupling it from the database schema
- Reduced risk of accidentally exposing sensitive data or internal implementation details