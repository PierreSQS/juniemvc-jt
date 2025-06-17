# DTO Implementation Task List

## 1. Project Setup
- [ ] 1.1. Add Lombok dependency to pom.xml (if not already present)
- [ ] 1.2. Configure Maven compiler plugin for annotation processing

## 2. Create DTO Structure
- [ ] 2.1. Create package `guru.springframework.juniemvc.models`
- [ ] 2.2. Create `BeerDto` class with the following:
  - [ ] 2.2.1. Add all required properties (id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, updateDate)
  - [ ] 2.2.2. Apply Lombok annotations (@Getter, @Setter, @Builder, @NoArgsConstructor, @AllArgsConstructor)

## 3. Create Mapper
- [ ] 3.1. Create package `guru.springframework.juniemvc.mappers`
- [ ] 3.2. Create `BeerMapper` interface with:
  - [ ] 3.2.1. Method to convert from Beer to BeerDto
  - [ ] 3.2.2. Method to convert from BeerDto to Beer
  - [ ] 3.2.3. Configuration to ignore id, createdDate, and updateDate when mapping from BeerDto to Beer
  - [ ] 3.2.4. Add @Mapper annotation with Spring component model

## 4. Update Service Layer
- [ ] 4.1. Modify `BeerService` interface:
  - [ ] 4.1.1. Change return types from `Beer` to `BeerDto`
  - [ ] 4.1.2. Change parameter types from `Beer` to `BeerDto`
- [ ] 4.2. Update `BeerServiceImpl` class:
  - [ ] 4.2.1. Inject `BeerMapper` using constructor injection
  - [ ] 4.2.2. Update implementation to use mapper for entity-DTO conversions
  - [ ] 4.2.3. Ensure all methods return DTOs instead of entities

## 5. Update Controller Layer
- [ ] 5.1. Modify `BeerController`:
  - [ ] 5.1.1. Update imports to include BeerDto
  - [ ] 5.1.2. Change return types from `Beer` to `BeerDto`
  - [ ] 5.1.3. Change parameter types from `Beer` to `BeerDto`
  - [ ] 5.1.4. Verify endpoint paths and HTTP methods remain unchanged

## 6. Update Tests
- [ ] 6.1. Update `BeerControllerTest`:
  - [ ] 6.1.1. Update test setup to use BeerDto
  - [ ] 6.1.2. Update mock service method calls to use BeerDto
  - [ ] 6.1.3. Update JSON path expectations
- [ ] 6.2. Update `BeerServiceImplTest`:
  - [ ] 6.2.1. Update test setup to use BeerDto
  - [ ] 6.2.2. Update mock repository method calls to handle DTO-entity conversions
  - [ ] 6.2.3. Update assertions to verify DTO properties

## 7. Verification and Testing
- [ ] 7.1. Run all unit tests to ensure they pass
- [ ] 7.2. Run integration tests if available
- [ ] 7.3. Manually test API endpoints:
  - [ ] 7.3.1. Test GET all beers endpoint
  - [ ] 7.3.2. Test GET beer by ID endpoint
  - [ ] 7.3.3. Test POST endpoint for creating a beer
  - [ ] 7.3.4. Test PUT endpoint for updating a beer
  - [ ] 7.3.5. Test DELETE endpoint for deleting a beer

## 8. Documentation and Cleanup
- [ ] 8.1. Update API documentation (if any) to reflect DTO usage
- [ ] 8.2. Review code for any remaining entity exposures in the API
- [ ] 8.3. Ensure consistent naming conventions across DTOs and entities
- [ ] 8.4. Remove any unused imports or code