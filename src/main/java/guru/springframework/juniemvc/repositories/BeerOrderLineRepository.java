package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for BeerOrderLine entity
 */
@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Additional custom query methods can be added here if needed
}