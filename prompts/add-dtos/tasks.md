# DTO Implementation Task List

## 1. Project Setup
- [x] 1.1. Add Lombok dependency to pom.xml (if not already present)
- [x] 1.2. Configure Maven compiler plugin for annotation processing

## 2. Create DTO Structure
- [x] 2.1. Create package `guru.springframework.juniemvc.models`
- [x] 2.2. Create `BeerDto` class with the following:
  - [x] 2.2.1. Add all required properties (id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, updateDate)
  - [x] 2.2.2. Apply Lombok annotations (@Getter, @Setter, @Builder, @NoArgsConstructor, @AllArgsConstructor)

## 3. Create Mapper
- [x] 3.1. Create package `guru.springframework.juniemvc.mappers`
- [x] 3.2. Create `BeerMapper` interface with:
  - [x] 3.2.1. Method to convert from Beer to BeerDto
  - [x] 3.2.2. Method to convert from BeerDto to Beer
  - [x] 3.2.3. Configuration to ignore id, createdDate, and updateDate when mapping from BeerDto to Beer
  - [x] 3.2.4. Add @Mapper annotation with Spring component model

## 4. Update Service Layer
- [x] 4.1. Modify `BeerService` interface:
  - [x] 4.1.1. Change return types from `Beer` to `BeerDto`
  - [x] 4.1.2. Change parameter types from `Beer` to `BeerDto`
- [x] 4.2. Update `BeerServiceImpl` class:
  - [x] 4.2.1. Inject `BeerMapper` using constructor injection
  - [x] 4.2.2. Update implementation to use mapper for entity-DTO conversions
  - [x] 4.2.3. Ensure all methods return DTOs instead of entities

## 5. Update Controller Layer
- [x] 5.1. Modify `BeerController`:
  - [x] 5.1.1. Update imports to include BeerDto
  - [x] 5.1.2. Change return types from `Beer` to `BeerDto`
  - [x] 5.1.3. Change parameter types from `Beer` to `BeerDto`
  - [x] 5.1.4. Verify endpoint paths and HTTP methods remain unchanged

## 6. Update Tests
- [x] 6.1. Update `BeerControllerTest`:
  - [x] 6.1.1. Update test setup to use BeerDto
  - [x] 6.1.2. Update mock service method calls to use BeerDto
  - [x] 6.1.3. Update JSON path expectations
- [x] 6.2. Update `BeerServiceImplTest`:
  - [x] 6.2.1. Update test setup to use BeerDto
  - [x] 6.2.2. Update mock repository method calls to handle DTO-entity conversions
  - [x] 6.2.3. Update assertions to verify DTO properties

## 7. Verification and Testing
- [x] 7.1. Run all unit tests to ensure they pass
- [x] 7.2. Run integration tests if available
- [x] 7.3. Manually test API endpoints:
  - [x] 7.3.1. Test GET all beers endpoint
  - [x] 7.3.2. Test GET beer by ID endpoint
  - [x] 7.3.3. Test POST endpoint for creating a beer
  - [x] 7.3.4. Test PUT endpoint for updating a beer
  - [x] 7.3.5. Test DELETE endpoint for deleting a beer

## 8. Documentation and Cleanup
- [x] 8.1. Update API documentation (if any) to reflect DTO usage
- [x] 8.2. Review code for any remaining entity exposures in the API
- [x] 8.3. Ensure consistent naming conventions across DTOs and entities
- [x] 8.4. Remove any unused imports or code
