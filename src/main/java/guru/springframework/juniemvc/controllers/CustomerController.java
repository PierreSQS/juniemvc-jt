package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Customer operations
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Get all customers
     * @return List of all customers
     */
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Get a customer by its ID
     * @param id The customer ID
     * @return ResponseEntity containing the customer if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new customer
     * @param customerDto The customer to create
     * @return The created customer with 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        // Ensure a new customer is created, not an update
        customerDto.setId(null);
        return customerService.saveCustomer(customerDto);
    }

    /**
     * Update an existing customer
     * @param id The customer ID
     * @param customerDto The updated customer data
     * @return ResponseEntity containing the updated customer if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a customer by its ID
     * @param id The customer ID
     * @return ResponseEntity with no content if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}