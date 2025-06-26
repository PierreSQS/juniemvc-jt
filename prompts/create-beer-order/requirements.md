# Spring Boot Implementation Guide for Beer Ordering System

Based on the ERD for a beer ordering system, this guide provides detailed instructions for implementing a complete Spring Boot application with JPA entities, repositories, services, DTOs, mappers, controllers, and tests.

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

### 6.2 CustomerService

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

### 6.3 CustomerServiceImpl

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
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
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

### 6.4 BeerOrderService

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

### 6.5 BeerOrderServiceImpl

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.BeerOrderMapper;
import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import guru.springframework.juniemvc.repositories.BeerOrderRepository;
import guru.springframework.juniemvc.repositories.BeerRepository;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository,
                               CustomerRepository customerRepository,
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.customerRepository = customerRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll()
                .stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> beerOrderRepository.findAllByCustomer(customer)
                        .stream()
                        .map(beerOrderMapper::beerOrderToBeerOrderDto)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Transactional
    @Override
    public BeerOrderDto createBeerOrder(BeerOrderDto beerOrderDto) {
        Optional<Customer> customerOptional = customerRepository.findById(beerOrderDto.getCustomerId());

        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found: " + beerOrderDto.getCustomerId());
        }

        Customer customer = customerOptional.get();

        BeerOrder beerOrder = BeerOrder.builder()
                .customer(customer)
                .orderStatus("NEW")
                .build();

        // Add order lines
        if (beerOrderDto.getOrderLines() != null) {
            beerOrderDto.getOrderLines().forEach(lineDto -> {
                Optional<Beer> beerOptional = beerRepository.findById(lineDto.getBeerId());

                if (beerOptional.isEmpty()) {
                    throw new RuntimeException("Beer not found: " + lineDto.getBeerId());
                }

                BeerOrderLine line = BeerOrderLine.builder()
                        .beer(beerOptional.get())
                        .orderQuantity(lineDto.getOrderQuantity())
                        .build();

                beerOrder.addOrderLine(line);
            });
        }

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Transactional
    @Override
    public Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .map(existingOrder -> {
                    // Update order status if provided
                    if (beerOrderDto.getOrderStatus() != null) {
                        existingOrder.setOrderStatus(beerOrderDto.getOrderStatus());
                    }

                    // Save the updated order
                    BeerOrder savedOrder = beerOrderRepository.save(existingOrder);
                    return beerOrderMapper.beerOrderToBeerOrderDto(savedOrder);
                });
    }

    @Transactional
    @Override
    public boolean deleteBeerOrderById(Integer id) {
        if (beerOrderRepository.existsById(id)) {
            beerOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
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

Example for CustomerRepository test:

```java
package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    // Constructor injection can be used instead of @Autowired
    private final CustomerRepository customerRepository;

    CustomerRepositoryTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Test
    void testSaveCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .build();

        // When
        Customer savedCustomer = customerRepository.save(customer);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Test Customer");
        assertThat(savedCustomer.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindAll() {
        // Given
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .email("customer1@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .email("customer2@example.com")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // When
        List<Customer> customers = customerRepository.findAll();

        // Then
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isGreaterThanOrEqualTo(2);
    }
}
```

### 8.2 Service Tests

Example for CustomerService test:

```java
package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.CustomerMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer testCustomer;
    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .build();

        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .build();
    }

    @Test
    void getAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        List<CustomerDto> result = customerService.getAllCustomers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        Optional<CustomerDto> result = customerService.getCustomerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("Test Customer");
        verify(customerRepository).findById(1);
    }

    @Test
    void saveCustomer() {
        // Given
        when(customerMapper.customerDtoToCustomer(any(CustomerDto.class))).thenReturn(testCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        CustomerDto savedCustomer = customerService.saveCustomer(testCustomerDto);

        // Then
        assertThat(savedCustomer).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }
}
```

### 8.3 Controller Tests

Example for CustomerController test:

```java
package guru.springframework.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    // Constructor injection can be used instead of @Autowired
    private final MockMvc mockMvc;
    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    CustomerControllerTest(MockMvc mockMvc, 
                          @MockBean CustomerService customerService,
                          ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.customerService = customerService;
        this.objectMapper = objectMapper;
    }

    private CustomerDto testCustomer;
    private List<CustomerDto> testCustomers;

    @BeforeEach
    void setUp() {
        testCustomer = CustomerDto.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .build();

        CustomerDto testCustomer2 = CustomerDto.builder()
                .id(2)
                .name("Another Customer")
                .email("another@example.com")
                .build();

        testCustomers = Arrays.asList(testCustomer, testCustomer2);
    }

    @Test
    void getAllCustomers() throws Exception {
        // Given
        given(customerService.getAllCustomers()).willReturn(testCustomers);

        // When/Then
        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Customer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Another Customer")))
                .andDo(print());
    }

    @Test
    void getCustomerById() throws Exception {
        // Given
        given(customerService.getCustomerById(1)).willReturn(Optional.of(testCustomer));

        // When/Then
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Customer")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andDo(print());
    }

    @Test
    void createCustomer() throws Exception {
        // Given
        CustomerDto newCustomer = CustomerDto.builder()
                .name("New Customer")
                .email("new@example.com")
                .build();

        CustomerDto savedCustomer = CustomerDto.builder()
                .id(3)
                .name("New Customer")
                .email("new@example.com")
                .build();

        given(customerService.saveCustomer(any(CustomerDto.class))).willReturn(savedCustomer);

        // When/Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Customer")))
                .andExpect(jsonPath("$.email", is("new@example.com")))
                .andDo(print());
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

### 10.1 Dependency Injection
- Use constructor injection for required dependencies
- Make dependencies final to ensure immutability
- Avoid field injection with @Autowired

### 10.2 Transaction Management
- Use @Transactional for service methods that modify data
- Use @Transactional(readOnly = true) for query-only methods
- Keep transaction boundaries at the service layer

### 10.3 Data Transfer Objects
- Use DTOs to separate your API from your domain model
- Don't expose entities directly in your API
- Use mappers to convert between entities and DTOs

### 10.4 REST API Design
- Use appropriate HTTP methods (GET, POST, PUT, DELETE)
- Return appropriate HTTP status codes
- Use consistent URL patterns
- Version your API (e.g., /api/v1/...)

### 10.5 Testing
- Write tests for all layers (repository, service, controller)
- Use @DataJpaTest for repository tests
- Use MockMvc for controller tests
- Use Mockito for service tests

### 10.6 Error Handling
- Implement global exception handling
- Return consistent error responses
- Use appropriate HTTP status codes for errors

### 10.7 Validation
- Add validation to DTOs using Jakarta Validation annotations
- Validate input at the controller level

### 10.8 Documentation
- Document your API using Swagger/OpenAPI
- Add Javadoc comments to interfaces and public methods

This implementation follows Spring Boot best practices and provides a solid foundation for a beer ordering system with proper JPA relationships, DTOs, services, controllers, and tests.
