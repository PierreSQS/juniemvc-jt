# Junie MVC Developer Guidelines

## Project Overview
Junie MVC is a Spring Boot application that demonstrates a RESTful API for managing beer data. It follows a standard MVC architecture with a layered approach (controller, service, repository) and uses Spring Data JPA for database operations.

## Tech Stack
- **Java 21**: Core programming language
- **Spring Boot 3.5.0**: Application framework
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM framework
- **H2 Database**: In-memory database for testing
- **Lombok**: Reduces boilerplate code
- **MapStruct**: Object mapping between layers
- **Flyway**: Database migration
- **Maven**: Build and dependency management

## Project Structure
```
src/
├── main/
│   ├── java/guru/springframework/juniemvc/
│   │   ├── controllers/     # REST API endpoints
│   │   ├── entities/        # JPA entity classes
│   │   ├── repositories/    # Spring Data JPA repositories
│   │   ├── services/        # Business logic layer
│   │   └── JuniemvcApplication.java  # Application entry point
│   └── resources/
│       └── application.properties    # Application configuration
└── test/
    ├── java/guru/springframework/juniemvc/
    │   ├── controllers/     # Controller tests
    │   ├── repositories/    # Repository tests
    │   ├── services/        # Service tests
    │   └── JuniemvcApplicationTests.java
    └── resources/
        └── application.properties    # Test configuration
```

## Running the Application
1. **Prerequisites**: Java 21, Maven 3.9.6+
2. **Build the application**:
   ```
   ./mvnw clean install
   ```
3. **Run the application**:
   ```
   ./mvnw spring-boot:run
   ```
4. **Access the API**: The REST API is available at `http://localhost:8080/api/v1/beers`

## Testing
1. **Run all tests**:
   ```
   ./mvnw test
   ```
2. **Run specific test class**:
   ```
   ./mvnw test -Dtest=BeerControllerTest
   ```
3. **Test endpoints with curl**:
   ```
   # Get all beers
   curl http://localhost:8080/api/v1/beers

   # Get beer by ID
   curl http://localhost:8080/api/v1/beers/1

   # Create new beer
   curl -X POST http://localhost:8080/api/v1/beers -H "Content-Type: application/json" -d '{"beerName":"Test Beer","beerStyle":"IPA","upc":"123456","price":12.99,"quantityOnHand":100}'
   ```

## Best Practices
1. **Code Organization**:
   - Follow the existing package structure
   - Keep controllers thin, with business logic in services
   - Use interfaces for services to maintain loose coupling

2. **Testing**:
   - Write unit tests for all new functionality
   - Use appropriate test scope (unit, integration)
   - Test both happy path and edge cases

3. **Database**:
   - Use JPA annotations for entity mapping
   - Create Flyway migration scripts for schema changes
   - Use transactions appropriately (@Transactional)

4. **API Design**:
   - Follow RESTful principles
   - Use appropriate HTTP methods and status codes
   - Document APIs with clear comments

5. **Error Handling**:
   - Use proper exception handling
   - Return appropriate HTTP status codes
   - Provide meaningful error messages
