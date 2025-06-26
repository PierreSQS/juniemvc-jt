package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Additional custom query methods can be added here if needed
}