package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.CustomerDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Customer operations
 */
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