
# JPA Entity Relationship Implementation Guide with Lombok

Based on the ERD for a beer ordering system, here are detailed instructions for implementing the relationships in JPA with Lombok.

## Entity Structure Overview

The system appears to require the following entities:
- Beer (already implemented)
- Customer
- BeerOrder
- BeerOrderLine

## Implementation Instructions

### 1. Beer Entity (Already Implemented)

The Beer entity is already implemented with the following structure:
```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

### 2. Customer Entity

```java
package guru.springframework.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String email;
    
    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private Set<BeerOrder> orders = new java.util.HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

### 3. BeerOrder Entity

```java
package guru.springframework.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BeerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String orderStatus;
    
    @ManyToOne
    private Customer customer;
    
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BeerOrderLine> orderLines = new java.util.HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
    
    // Convenience method to add order line and maintain relationship
    public void addOrderLine(BeerOrderLine orderLine) {
        orderLine.setBeerOrder(this);
        this.orderLines.add(orderLine);
    }
}
```

### 4. BeerOrderLine Entity

```java
package guru.springframework.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BeerOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private Integer orderQuantity;
    
    @ManyToOne
    private BeerOrder beerOrder;
    
    @ManyToOne
    private Beer beer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

## Key Implementation Notes

### Lombok Annotations
- `@Getter` and `@Setter`: Automatically generate getters and setters for all fields
- `@Builder`: Enables the builder pattern for object creation
- `@NoArgsConstructor`: Creates a no-args constructor (required by JPA)
- `@AllArgsConstructor`: Creates a constructor with all fields as parameters
- `@Builder.Default`: Used with collection fields to ensure they're initialized with empty collections

### JPA Relationship Annotations
1. **@OneToMany**: Used for one-to-many relationships
    - `mappedBy`: Specifies the field in the target entity that owns the relationship
    - This creates a bidirectional relationship

2. **@ManyToOne**: Used for many-to-one relationships
    - The owning side of the relationship
    - Typically contains the foreign key

3. **@ManyToMany**: (Not used in this example but would be used for many-to-many relationships)
    - Often requires a join table

### Cascading Operations
- `cascade = CascadeType.ALL`: Propagates all operations (persist, remove, refresh, merge, detach) from parent to child entities
- Use carefully to avoid unintended deletions

### Bidirectional Relationship Management
- Add helper methods (like `addOrderLine()` in BeerOrder) to maintain both sides of bidirectional relationships
- This ensures data consistency

### Collection Initialization
- Use `@Builder.Default` with an initialized collection to avoid null pointer exceptions
- This is especially important when using the builder pattern with Lombok

### Fetch Types
- Default fetch types are:
    - EAGER for @ManyToOne and @OneToOne
    - LAZY for @OneToMany and @ManyToMany
- Consider explicitly setting fetch types based on your application needs

## Repository Interfaces

For each entity, create a corresponding repository interface:

```java
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    List<BeerOrder> findAllByCustomer(Customer customer);
}

public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
}
```

This implementation follows Spring Boot best practices and provides a solid foundation for a beer ordering system with proper JPA relationships and Lombok integration.