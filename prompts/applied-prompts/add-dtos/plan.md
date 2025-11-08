# Implementation Plan for Adding DTOs to the Beer API

## 1. Project Analysis

### Current Implementation
- The application currently exposes JPA entities directly through the REST API
- The `Beer` entity is used throughout all layers (controller, service, repository)
- No validation is currently implemented on incoming requests
- Tests directly use the `Beer` entity

### Issues with Current Implementation
- Tight coupling between API contract and database schema
- Difficult to evolve API and database independently
- No input validation
- Changes to the entity affect all layers

## 2. Implementation Steps

### Step 1: Create DTO Classes
1. Create a new package `guru.springframework.juniemvc.models`
2. Create a `BeerDto` class with the following:
   - Properties matching the requirements (id, version, beerName, etc.)
   - Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor, @Builder)
   - Jakarta Validation annotations (@NotBlank, @NotNull, @Positive, etc.)

### Step 2: Implement MapStruct Mapper
1. Add MapStruct dependencies to pom.xml
2. Create a new package `guru.springframework.juniemvc.mappers`
3. Create a `BeerMapper` interface with:
   - Method to convert from Beer entity to BeerDto
   - Method to convert from BeerDto to Beer entity with appropriate @Mapping annotations to ignore id, createdDate, and updateDate

### Step 3: Update Service Layer
1. Modify the `BeerService` interface to use DTOs instead of entities
   - Update method signatures to accept and return DTOs
2. Update `BeerServiceImpl` to:
   - Inject the `BeerMapper`
   - Convert between entities and DTOs using the mapper
   - Continue using the repository with entities

### Step 4: Update Controller Layer
1. Modify `BeerController` to use DTOs instead of entities
   - Update method signatures to accept and return DTOs
   - Add @Valid annotation to request bodies
   - Maintain the same REST API contract (endpoints, HTTP methods, status codes)

### Step 5: Update Tests
1. Update `BeerControllerTest`:
   - Use DTOs instead of entities in test setup and assertions
   - Ensure all tests pass with the new implementation
2. Update `BeerServiceImplTest`:
   - Modify tests to use DTOs
   - Mock the mapper in addition to the repository
   - Ensure all tests pass with the new implementation

## 3. Implementation Details

### BeerDto Class
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
    private Integer id;
    private Integer version;
    
    @NotBlank(message = "Beer name is required")
    private String beerName;
    
    @NotBlank(message = "Beer style is required")
    private String beerStyle;
    
    @NotBlank(message = "UPC is required")
    private String upc;
    
    @NotNull(message = "Quantity on hand is required")
    @PositiveOrZero(message = "Quantity on hand must be zero or positive")
    private Integer quantityOnHand;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

### BeerMapper Interface
```java
@Mapper
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}
```

### BeerService Interface
```java
public interface BeerService {
    List<BeerDto> getAllBeers();
    Optional<BeerDto> getBeerById(Integer id);
    BeerDto saveBeer(BeerDto beerDto);
    void deleteBeerById(Integer id);
}
```

### BeerServiceImpl Class
```java
@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    
    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }
    
    @Override
    public List<BeerDto> getAllBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }
    
    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }
    
    @Override
    public void deleteBeerById(Integer id) {
        beerRepository.deleteById(id);
    }
}
```

### BeerController Class
```java
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {
    private final BeerService beerService;
    
    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }
    
    @GetMapping
    public List<BeerDto> getAllBeers() {
        return beerService.getAllBeers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable Integer id) {
        Optional<BeerDto> beerOptional = beerService.getBeerById(id);
        
        return beerOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDto createBeer(@Valid @RequestBody BeerDto beerDto) {
        // Ensure a new beer is created, not an update
        beerDto.setId(null);
        return beerService.saveBeer(beerDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable Integer id, @Valid @RequestBody BeerDto beerDto) {
        Optional<BeerDto> beerOptional = beerService.getBeerById(id);
        
        if (beerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Ensure we're updating the correct beer
        beerDto.setId(id);
        BeerDto updatedBeer = beerService.saveBeer(beerDto);
        return ResponseEntity.ok(updatedBeer);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        Optional<BeerDto> beerOptional = beerService.getBeerById(id);
        
        if (beerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        beerService.deleteBeerById(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 4. Testing Strategy

### Controller Tests
- Update test setup to use DTOs instead of entities
- Ensure all existing test cases still pass
- Add tests for validation errors

### Service Tests
- Update test setup to use DTOs instead of entities
- Mock the mapper to convert between entities and DTOs
- Ensure all existing test cases still pass

## 5. Benefits of Implementation

- Decoupled web layer from persistence layer
- Ability to evolve API and database independently
- Input validation for API requests
- Clear separation of concerns
- Better adherence to Spring Boot best practices

## 6. Potential Challenges

- Ensuring all tests pass with the new implementation
- Handling validation errors properly
- Ensuring the mapper correctly maps between entities and DTOs

## 7. Future Improvements

- Add pagination for the getAllBeers endpoint
- Add filtering and sorting capabilities
- Implement error handling with a global exception handler
- Add API documentation with Swagger/OpenAPI