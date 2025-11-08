# Adding DTOs to the Beer API

## Overview
This document outlines the requirements for implementing Data Transfer Objects (DTOs) in the Beer API application. The goal is to separate the web layer from the persistence layer by introducing DTOs, following Spring Boot best practices.

## Background
Currently, the application exposes JPA entities directly through the REST API, which tightly couples the API contract to the database schema. This makes it difficult to evolve the API and database independently. By introducing DTOs, we can decouple these layers and gain more flexibility.

## Requirements

### 1. Create DTO Classes
- Create a new package `guru.springframework.juniemvc.models` to contain all DTO classes
- Create a `BeerDto` class with the following properties:
  - Integer id
  - Integer version
  - String beerName
  - String beerStyle
  - String upc
  - Integer quantityOnHand
  - BigDecimal price
  - LocalDateTime createdDate
  - LocalDateTime updateDate
- Use Lombok annotations to reduce boilerplate:
  - `@Data` (or `@Getter` and `@Setter`)
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`
  - `@Builder`

### 2. Create MapStruct Mapper
- Create a new package `guru.springframework.juniemvc.mappers` to contain all mapper interfaces
- Create a `BeerMapper` interface using MapStruct:
  ```java
  @Mapper
  public interface BeerMapper {
      BeerDto beerToBeerDto(Beer beer);
      
      // When mapping from DTO to entity, ignore id, createdDate, and updateDate
      // as these are managed by the persistence layer
      @Mapping(target = "id", ignore = true)
      @Mapping(target = "createdDate", ignore = true)
      @Mapping(target = "updateDate", ignore = true)
      Beer beerDtoToBeer(BeerDto beerDto);
  }
  ```

### 3. Update Service Layer
- Modify the `BeerService` interface to use DTOs instead of entities:
  ```java
  public interface BeerService {
      List<BeerDto> getAllBeers();
      Optional<BeerDto> getBeerById(Integer id);
      BeerDto saveBeer(BeerDto beerDto);
      void deleteBeerById(Integer id);
  }
  ```
- Update `BeerServiceImpl` to:
  - Inject the `BeerMapper`
  - Convert between entities and DTOs using the mapper
  - Continue using the repository with entities

### 4. Update Controller Layer
- Modify `BeerController` to use DTOs instead of entities:
  - Update method signatures to accept and return DTOs
  - Remove any direct manipulation of entity-specific properties
  - Maintain the same REST API contract (endpoints, HTTP methods, status codes)

### 5. Validation
- Add Jakarta Validation annotations to the `BeerDto` class:
  - `@NotBlank` for String fields that shouldn't be empty
  - `@NotNull` for required fields
  - `@Positive` or `@PositiveOrZero` for numeric fields as appropriate
- Update the controller to use `@Valid` annotation on request bodies

### 6. Testing
- Update existing tests to work with DTOs instead of entities
- Ensure all tests pass with the new implementation

## Implementation Guidelines
- Follow the constructor injection pattern for all dependencies
- Use package-private visibility where appropriate
- Maintain clear transaction boundaries in the service layer
- Ensure proper error handling

## Acceptance Criteria
- All API endpoints continue to function as before, but now use DTOs instead of entities
- The web layer is completely decoupled from the persistence layer
- All tests pass
- The application builds successfully