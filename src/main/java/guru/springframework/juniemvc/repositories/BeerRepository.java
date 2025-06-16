package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Beer entity
 */
@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Additional custom query methods can be added here if needed
}