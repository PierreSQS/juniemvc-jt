# Task List for Adding DTOs to the Beer API

## 1. Create DTO Classes
- [x] 1.1. Create a new package `guru.springframework.juniemvc.models`
- [x] 1.2. Create a `BeerDto` class with the following:
  - [x] 1.2.1. Add properties (id, version, beerName, beerStyle, upc, quantityOnHand, price, createdDate, updateDate)
  - [x] 1.2.2. Add Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder)
  - [x] 1.2.3. Add Jakarta Validation annotations:
    - [x] 1.2.3.1. @NotBlank for beerName, beerStyle, and upc
    - [x] 1.2.3.2. @NotNull for quantityOnHand and price
    - [x] 1.2.3.3. @PositiveOrZero for quantityOnHand
    - [x] 1.2.3.4. @Positive for price
  - [x] 1.2.4. Add appropriate validation error messages

## 2. Implement MapStruct Mapper
- [x] 2.1. Add MapStruct dependencies to pom.xml:
  - [x] 2.1.1. Add mapstruct dependency
  - [x] 2.1.2. Add mapstruct-processor dependency
  - [x] 2.1.3. Configure annotation processor path in maven-compiler-plugin
- [x] 2.2. Create a new package `guru.springframework.juniemvc.mappers`
- [x] 2.3. Create a `BeerMapper` interface with:
  - [x] 2.3.1. Add @Mapper annotation
  - [x] 2.3.2. Add method to convert from Beer entity to BeerDto
  - [x] 2.3.3. Add method to convert from BeerDto to Beer entity
  - [x] 2.3.4. Add @Mapping annotations to ignore id, createdDate, and updateDate fields

## 3. Update Service Layer
- [x] 3.1. Modify the `BeerService` interface:
  - [x] 3.1.1. Update getAllBeers() to return List<BeerDto>
  - [x] 3.1.2. Update getBeerById() to return Optional<BeerDto>
  - [x] 3.1.3. Update saveBeer() to accept and return BeerDto
  - [x] 3.1.4. Keep deleteBeerById() unchanged
- [x] 3.2. Update `BeerServiceImpl` class:
  - [x] 3.2.1. Add BeerMapper as a dependency
  - [x] 3.2.2. Update constructor to inject BeerMapper
  - [x] 3.2.3. Update getAllBeers() to convert entities to DTOs
  - [x] 3.2.4. Update getBeerById() to convert entity to DTO
  - [x] 3.2.5. Update saveBeer() to convert between DTO and entity
  - [x] 3.2.6. Keep deleteBeerById() unchanged

## 4. Update Controller Layer
- [x] 4.1. Modify `BeerController`:
  - [x] 4.1.1. Update getAllBeers() to return List<BeerDto>
  - [x] 4.1.2. Update getBeerById() to use BeerDto
  - [x] 4.1.3. Update createBeer() to accept and return BeerDto
  - [x] 4.1.4. Add @Valid annotation to request bodies
  - [x] 4.1.5. Update updateBeer() to use BeerDto
  - [x] 4.1.6. Update deleteBeer() to use BeerDto for checking existence

## 5. Update Tests
- [x] 5.1. Update `BeerControllerTest`:
  - [x] 5.1.1. Modify test setup to use DTOs instead of entities
  - [x] 5.1.2. Update assertions to verify DTO properties
  - [x] 5.1.3. Add tests for validation errors
  - [x] 5.1.4. Ensure all tests pass with the new implementation
- [x] 5.2. Update `BeerServiceImplTest`:
  - [x] 5.2.1. Modify test setup to use DTOs
  - [x] 5.2.2. Mock the BeerMapper
  - [x] 5.2.3. Update assertions to verify DTO properties
  - [x] 5.2.4. Ensure all tests pass with the new implementation

## 6. Additional Tasks
- [x] 6.1. Verify all functionality works as expected
- [x] 6.2. Ensure proper error handling for validation errors
- [x] 6.3. Review code for any missed conversion points
- [x] 6.4. Update documentation if necessary
