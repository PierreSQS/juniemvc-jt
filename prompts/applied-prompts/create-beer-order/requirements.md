
# Beer Order Management System Requirements

## Overview
This document outlines the requirements for implementing a Beer Order Management System as part of the existing Spring Boot application. The system will allow creating, reading, updating, and deleting beer orders and their associated order lines.

## Entity Model

### Entity Relationships
The system will consist of the following entities with these relationships:
- **Beer**: Represents a beer product with properties like name, style, price, etc.
- **BeerOrder**: Represents an order containing multiple beer order lines
- **BeerOrderLine**: Represents a line item in a beer order, linking a beer with its ordered quantity

The relationships between these entities are:
- One **BeerOrder** can have many **BeerOrderLine** items (One-to-Many)
- Each **BeerOrderLine** belongs to exactly one **BeerOrder** (Many-to-One)
- Each **BeerOrderLine** references exactly one **Beer** (Many-to-One)
- One **Beer** can be referenced by many **BeerOrderLine** items (One-to-Many)

### Common Entity Structure
All entities should inherit from a common base entity class that provides:
- ID field with auto-generation
- Version field for optimistic locking
- Creation timestamp
- Update timestamp

## Technical Requirements

### 1. JPA Entity Implementation
- Create a `BaseEntity` abstract class with common fields
- Implement the `Beer`, `BeerOrder`, and `BeerOrderLine` entities with proper JPA annotations
- Configure bidirectional relationships with proper cascade operations
- Use Lombok annotations to reduce boilerplate code
- Ensure proper initialization of collections to avoid null pointer exceptions

### 2. Data Transfer Objects (DTOs)
- Create DTOs for all entities to separate the persistence layer from the web layer
- Use Java records for immutability and conciseness
- Apply Jakarta Validation annotations on request DTOs to enforce input validation rules
- Create separate DTOs for different use cases if needed (e.g., create, update, response)

### 3. Object Mapping
- Use MapStruct for mapping between entities and DTOs
- Configure MapStruct to generate compile-time implementations
- Handle bidirectional relationships properly in the mappers

### 4. Data Access Layer
- Create Spring Data JPA repositories for all entities
- Define custom query methods if needed

### 5. Service Layer
- Define service interfaces for all business operations
- Implement service classes with proper transaction management
    - Use `@Transactional(readOnly = true)` for read-only operations
    - Use `@Transactional` for data-modifying operations
- Use constructor injection for dependencies
- Implement proper error handling and validation

### 6. Web Layer
- Create RESTful controllers for all entities
- Follow REST API design principles:
    - Use versioned, resource-oriented URLs (e.g., `/api/v1/beer-orders`)
    - Use proper HTTP methods (GET, POST, PUT, DELETE)
    - Return appropriate HTTP status codes
    - Use pagination for collection resources
- Use `ResponseEntity<T>` to return proper status codes and response bodies
- Implement proper error handling

### 7. Testing
- Write comprehensive unit tests for all components:
    - Mappers
    - Repositories
    - Services
    - Controllers
- Use Testcontainers for integration tests with a real database
- Use random ports for integration tests to avoid conflicts

## Implementation Guidelines

### 1. Code Organization
- Follow package-by-feature organization
- Use package-private visibility for components when possible
- Separate concerns properly (web, service, repository layers)

### 2. Dependency Injection
- Use constructor injection for all dependencies
- Declare dependencies as final fields
- Avoid field and setter injection

### 3. Transaction Management
- Define clear transaction boundaries at the service layer
- Keep transactions as brief as possible
- Use read-only transactions for queries

### 4. JPA Configuration
- Disable Open Session in View pattern by setting `spring.jpa.open-in-view=false`
- Use fetch joins or entity graphs to avoid N+1 select problems

### 5. API Design
- Don't expose entities directly in controllers
- Use DTOs for request and response objects
- Apply consistent naming conventions for API endpoints
- Use camelCase for JSON property names

### 6. Exception Handling
- Implement centralized exception handling with `@RestControllerAdvice`
- Return consistent error responses
- Consider using the ProblemDetails format (RFC 9457)

### 7. Logging
- Use SLF4J for logging
- Protect sensitive data in logs
- Guard expensive log calls with level checks

## Entity Details

### BaseEntity
```java
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

### Beer Entity
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer extends BaseEntity {
    @Column(nullable = false)
    private String beerName;

    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "beer")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();
}
```

### BeerOrder Entity
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrder extends BaseEntity {
    private String customerRef;

    @Column(precision = 19, scale = 2)
    private BigDecimal paymentAmount;

    private String status;

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

    // Helper methods for managing bidirectional relationship
    public void addBeerOrderLine(BeerOrderLine line) {
        if (beerOrderLines == null) {
            beerOrderLines = new HashSet<>();
        }
        beerOrderLines.add(line);
        line.setBeerOrder(this);
    }

    public void removeBeerOrderLine(BeerOrderLine line) {
        beerOrderLines.remove(line);
        line.setBeerOrder(null);
    }
}
```

### BeerOrderLine Entity
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderLine extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "beer_order_id")
    private BeerOrder beerOrder;

    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    private Integer orderQuantity;
    private Integer quantityAllocated;
    private String status;
}
```