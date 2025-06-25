
# Spring Boot Implementation Guide for Beer Ordering System

Based on the ERD for a beer ordering system, here are detailed instructions for implementing a complete Spring Boot application with JPA entities, repositories, services, DTOs, mappers, controllers, and tests.

## 1. Entity Structure Overview

The system requires the following entities:
- Beer (already implemented)
- Customer
- BeerOrder
- BeerOrderLine

## 2. JPA Entities Implementation

### 2.1 Beer Entity (Already Implemented)

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

### 2.2 Customer Entity

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

### 2.3 BeerOrder Entity

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

### 2.4 BeerOrderLine Entity

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

### 2.5 Key Entity Implementation Notes

#### Lombok Annotations
- `@Getter` and `@Setter`: Automatically generate getters and setters for all fields
- `@Builder`: Enables the builder pattern for object creation
- `@NoArgsConstructor`: Creates a no-args constructor (required by JPA)
- `@AllArgsConstructor`: Creates a constructor with all fields as parameters
- `@Builder.Default`: Used with collection fields to ensure they're initialized with empty collections

#### JPA Relationship Annotations
1. **@OneToMany**: Used for one-to-many relationships
    - `mappedBy`: Specifies the field in the target entity that owns the relationship
    - This creates a bidirectional relationship

2. **@ManyToOne**: Used for many-to-one relationships
    - The owning side of the relationship
    - Typically contains the foreign key

3. **@ManyToMany**: (Not used in this example but would be used for many-to-many relationships)
    - Often requires a join table

#### Cascading Operations
- `cascade = CascadeType.ALL`: Propagates all operations (persist, remove, refresh, merge, detach) from parent to child entities
- Use carefully to avoid unintended deletions

#### Bidirectional Relationship Management
- Add helper methods (like `addOrderLine()` in BeerOrder) to maintain both sides of bidirectional relationships
- This ensures data consistency

#### Collection Initialization
- Use `@Builder.Default` with an initialized collection to avoid null pointer exceptions
- This is especially important when using the builder pattern with Lombok

#### Fetch Types
- Default fetch types are:
    - EAGER for @ManyToOne and @OneToOne
    - LAZY for @OneToMany and @ManyToMany
- Consider explicitly setting fetch types based on your application needs

## 3. Repository Interfaces

Create a repository interface for each entity by extending JpaRepository. This provides basic CRUD operations automatically.

### 3.1 BeerRepository (Already Implemented)

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Additional custom query methods can be added here if needed
}
```

### 3.2 CustomerRepository

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Custom query methods can be added here
}
```

### 3.3 BeerOrderRepository

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    List<BeerOrder> findAllByCustomer(Customer customer);
}
```

### 3.4 BeerOrderLineRepository

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
}
```

## 4. Data Transfer Objects (DTOs)

Create DTOs to separate your API from your domain model. DTOs should contain only the data needed for the specific use case.

### 4.1 BeerDto (Already Implemented)

```java
package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDto {
    private Integer id;
    private Integer version;
    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

### 4.2 CustomerDto

```java
package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private Integer version;
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

### 4.3 BeerOrderDto

```java
package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrderDto {
    private Integer id;
    private Integer version;
    private String orderStatus;
    private Integer customerId;
    private String customerName; // For convenience
    private Set<BeerOrderLineDto> orderLines;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

### 4.4 BeerOrderLineDto

```java
package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrderLineDto {
    private Integer id;
    private Integer version;
    private Integer orderId;
    private Integer beerId;
    private String beerName; // For convenience
    private Integer orderQuantity;
}
```

## 5. Mappers

Use MapStruct to create mappers that convert between entities and DTOs. MapStruct generates the implementation code at compile time.

### 5.1 BeerMapper (Already Implemented)

```java
package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.models.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}
```

### 5.2 CustomerMapper

```java
package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.models.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto customerToCustomerDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
```

### 5.3 BeerOrderMapper

```java
package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.models.BeerOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class, CustomerMapper.class})
public interface BeerOrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
}
```

### 5.4 BeerOrderLineMapper

```java
package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerOrderLineMapper {
    @Mapping(target = "beerId", source = "beer.id")
    @Mapping(target = "beerName", source = "beer.beerName")
    @Mapping(target = "orderId", source = "beerOrder.id")
    BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "beer", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}
```

## 6. Service Layer

Create service interfaces and implementations for each entity to handle business logic.

### 6.1 BeerService (Already Implemented)

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.BeerDto;

import java.util.List;
import java.util.Optional;

public interface BeerService {
    /**
     * Get all beers
     * @return List of all beers
     */
    List<BeerDto> getAllBeers();

    /**
     * Get a beer by its ID
     * @param id The beer ID
     * @return Optional containing the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Save a beer (create or update)
     * @param beerDto The beer to save
     * @return The saved beer
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Update an existing beer
     * @param id The ID of the beer to update
     * @param beerDto The updated beer data
     * @return Optional containing the updated beer if found and updated
     */
    Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto);

    /**
     * Delete a beer by its ID
     * @param id The ID of the beer to delete
     * @return true if the beer was deleted, false if not found
     */
    boolean deleteBeerById(Integer id);
}
```

### 6.2 BeerServiceImpl (Already Implemented)

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.mappers.BeerMapper;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Transactional
    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Transactional
    @Override
    public Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beerDto.getBeerName());
                    existingBeer.setBeerStyle(beerDto.getBeerStyle());
                    existingBeer.setUpc(beerDto.getUpc());
                    existingBeer.setPrice(beerDto.getPrice());
                    existingBeer.setQuantityOnHand(beerDto.getQuantityOnHand());

                    // Save the updated beer
                    Beer savedBeer = beerRepository.save(existingBeer);
                    return beerMapper.beerToBeerDto(savedBeer);
                });
    }

    @Transactional
    @Override
    public boolean deleteBeerById(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### 6.3 CustomerService

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    /**
     * Get all customers
     * @return List of all customers
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Get a customer by its ID
     * @param id The customer ID
     * @return Optional containing the customer if found
     */
    Optional<CustomerDto> getCustomerById(Integer id);

    /**
     * Save a customer (create or update)
     * @param customerDto The customer to save
     * @return The saved customer
     */
    CustomerDto saveCustomer(CustomerDto customerDto);

    /**
     * Update an existing customer
     * @param id The ID of the customer to update
     * @param customerDto The updated customer data
     * @return Optional containing the updated customer if found and updated
     */
    Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto);

    /**
     * Delete a customer by its ID
     * @param id The ID of the customer to delete
     * @return true if the customer was deleted, false if not found
     */
    boolean deleteCustomerById(Integer id);
}
```

### 6.4 CustomerServiceImpl

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.CustomerMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Transactional
    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Transactional
    @Override
    public Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Update the existing customer with new values
                    existingCustomer.setName(customerDto.getName());
                    existingCustomer.setEmail(customerDto.getEmail());

                    // Save the updated customer
                    Customer savedCustomer = customerRepository.save(existingCustomer);
                    return customerMapper.customerToCustomerDto(savedCustomer);
                });
    }

    @Transactional
    @Override
    public boolean deleteCustomerById(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

### 6.5 BeerOrderService

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.BeerOrderDto;

import java.util.List;
import java.util.Optional;

public interface BeerOrderService {
    /**
     * Get all beer orders
     * @return List of all beer orders
     */
    List<BeerOrderDto> getAllBeerOrders();

    /**
     * Get all beer orders for a customer
     * @param customerId The customer ID
     * @return List of beer orders for the customer
     */
    List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId);

    /**
     * Get a beer order by its ID
     * @param id The beer order ID
     * @return Optional containing the beer order if found
     */
    Optional<BeerOrderDto> getBeerOrderById(Integer id);

    /**
     * Create a new beer order
     * @param beerOrderDto The beer order to create
     * @return The created beer order
     */
    BeerOrderDto createBeerOrder(BeerOrderDto beerOrderDto);

    /**
     * Update an existing beer order
     * @param id The ID of the beer order to update
     * @param beerOrderDto The updated beer order data
     * @return Optional containing the updated beer order if found and updated
     */
    Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto);

    /**
     * Delete a beer order by its ID
     * @param id The ID of the beer order to delete
     * @return true if the beer order was deleted, false if not found
     */
    boolean deleteBeerOrderById(Integer id);
}
```

## 7. REST Controllers

Create REST controllers to expose your services as HTTP endpoints.

### 7.1 BeerController (Already Implemented)

```java
package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return beerService.getBeerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDto createBeer(@RequestBody BeerDto beerDto) {
        // Ensure a new beer is created, not an update
        beerDto.setId(null);
        return beerService.saveBeer(beerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable Integer id, @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(id, beerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        return beerService.deleteBeerById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
```

### 7.2 CustomerController

```java
package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        // Ensure a new customer is created, not an update
        customerDto.setId(null);
        return customerService.saveCustomer(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
```

### 7.3 BeerOrderController

```java
package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.services.BeerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class BeerOrderController {

    private final BeerOrderService beerOrderService;

    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @GetMapping
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderService.getAllBeerOrders();
    }

    @GetMapping("/customer/{customerId}")
    public List<BeerOrderDto> getBeerOrdersByCustomerId(@PathVariable Integer customerId) {
        return beerOrderService.getBeerOrdersByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable Integer id) {
        return beerOrderService.getBeerOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerOrderDto createBeerOrder(@RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.createBeerOrder(beerOrderDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeerOrderDto> updateBeerOrder(@PathVariable Integer id, @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.updateBeerOrder(id, beerOrderDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable Integer id) {
        return beerOrderService.deleteBeerOrderById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
```

## 8. Testing

Create comprehensive tests for each layer of your application.

### 8.1 Repository Tests

Example for BeerRepository test:

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    // Spring will automatically inject the repository
    // @Autowired is implicitly added by Spring Boot Test
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Beer savedBeer = beerRepository.save(beer);

        // Then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void testFindAll() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Beer 1")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Beer 2")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        // When
        List<Beer> beers = beerRepository.findAll();

        // Then
        assertThat(beers).isNotNull();
        assertThat(beers.size()).isGreaterThanOrEqualTo(2);
    }
}
```

### 8.2 Service Tests

Example for BeerService test:

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.mappers.BeerMapper;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        testBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void getAllBeers() {
        // Given
        List<Beer> beers = Arrays.asList(testBeer);
        when(beerRepository.findAll()).thenReturn(beers);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);

        // When
        List<BeerDto> result = beerService.getAllBeers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(beerRepository).findAll();
    }

    @Test
    void getBeerById() {
        // Given
        when(beerRepository.findById(anyInt())).thenReturn(Optional.of(testBeer));
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);

        // When
        Optional<BeerDto> result = beerService.getBeerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository).findById(1);
    }

    @Test
    void saveBeer() {
        // Given
        when(beerMapper.beerDtoToBeer(any(BeerDto.class))).thenReturn(testBeer);
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);

        // When
        BeerDto savedBeer = beerService.saveBeer(testBeerDto);

        // Then
        assertThat(savedBeer).isNotNull();
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void updateBeer() {
        // Given
        when(beerRepository.findById(anyInt())).thenReturn(Optional.of(testBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);

        // When
        Optional<BeerDto> updatedBeer = beerService.updateBeer(1, testBeerDto);

        // Then
        assertThat(updatedBeer).isPresent();
        verify(beerRepository).findById(1);
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void deleteBeerById() {
        // Given
        given(beerRepository.existsById(anyInt())).willReturn(true);

        // When
        boolean result = beerService.deleteBeerById(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository).deleteById(1);
    }
}
```

### 8.3 Controller Tests

Example for BeerController test:

```java
package guru.springframework.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.juniemvc.controllers.BeerController;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Example of a controller test using standalone MockMvc setup
 * This approach doesn't require a Spring context and avoids potential IDE warnings
 */
class BeerControllerTest {

    private MockMvc mockMvc;
    private BeerService beerService;
    private ObjectMapper objectMapper;
    private BeerDto testBeer;
    private List<BeerDto> testBeers;

    @BeforeEach
    void setUp() {
        // Create mocks and test data
        beerService = Mockito.mock(BeerService.class);
        objectMapper = new ObjectMapper();

        // Initialize the controller with mocked service
        BeerController beerController = new BeerController(beerService);

        // Set up MockMvc with standalone configuration
        mockMvc = MockMvcBuilders.standaloneSetup(beerController).build();

        // Create test data
        testBeer = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        BeerDto testBeer2 = BeerDto.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        testBeers = Arrays.asList(testBeer, testBeer2);
    }

    @Test
    void getAllBeers() throws Exception {
        // Given
        given(beerService.getAllBeers()).willReturn(testBeers);

        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].beerName", is("Another Beer")))
                .andDo(print());
    }

    @Test
    void getBeerById() throws Exception {
        // Given
        given(beerService.getBeerById(1)).willReturn(Optional.of(testBeer));

        // When/Then
        mockMvc.perform(get("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")))
                .andDo(print());
    }

    @Test
    void createBeer() throws Exception {
        // Given
        BeerDto newBeer = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        BeerDto savedBeer = BeerDto.builder()
                .id(3)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        given(beerService.saveBeer(any(BeerDto.class))).willReturn(savedBeer);

        // When/Then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBeer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.beerName", is("New Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Stout")))
                .andDo(print());
    }

    @Test
    void updateBeer() throws Exception {
        // Given
        BeerDto updatedBeer = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        BeerDto savedBeer = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        when(beerService.updateBeer(anyInt(), any(BeerDto.class))).thenReturn(Optional.of(savedBeer));

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBeer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Updated Style")));
    }

    @Test
    void deleteBeer() throws Exception {
        // Given
        given(beerService.deleteBeerById(1)).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
```

## 9. CRUD Operations Implementation Summary

When implementing CRUD (Create, Read, Update, Delete) operations for each entity, follow these patterns:

### 9.1 Create (C)
1. Define a POST endpoint in the controller
2. Set the HTTP status to 201 (Created) for successful creation
3. Ensure IDs are not provided in the request (set to null)
4. Map the DTO to an entity
5. Save the entity using the repository
6. Map the saved entity back to a DTO and return it

### 9.2 Read (R)
1. Define GET endpoints for retrieving all resources and individual resources by ID
2. Use appropriate query methods in the repository
3. Map entities to DTOs before returning them
4. Return 404 Not Found if a resource doesn't exist

### 9.3 Update (U)
1. Define a PUT endpoint for updating a resource by ID
2. Check if the resource exists
3. Update the existing entity with values from the DTO
4. Save the updated entity
5. Map the updated entity to a DTO and return it
6. Return 404 Not Found if the resource doesn't exist

### 9.4 Delete (D)
1. Define a DELETE endpoint for removing a resource by ID
2. Check if the resource exists
3. Delete the resource if it exists
4. Return 204 No Content for successful deletion
5. Return 404 Not Found if the resource doesn't exist

## 10. Best Practices

1. **Separation of Concerns**: Keep entities, DTOs, repositories, services, and controllers in separate packages
2. **Use DTOs**: Don't expose entities directly in your API
3. **Validation**: Add validation to DTOs using Jakarta Validation annotations
4. **Error Handling**: Implement global exception handling
5. **Transactions**: Use @Transactional for data-modifying operations
6. **Testing**: Write tests for all layers (repository, service, controller)
7. **Documentation**: Document your API using Swagger/OpenAPI
8. **Versioning**: Version your API (e.g., /api/v1/...)
9. **Pagination**: Implement pagination for endpoints that return collections
10. **Security**: Secure your API with appropriate authentication and authorization

This implementation follows Spring Boot best practices and provides a solid foundation for a beer ordering system with proper JPA relationships, DTOs, services, controllers, and tests.
