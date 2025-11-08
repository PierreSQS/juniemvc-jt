package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Beer entity
 */
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will implement basic CRUD operations

    Page<Beer> findAllByBeerNameContainingIgnoreCase(String beerName, Pageable pageable);
}