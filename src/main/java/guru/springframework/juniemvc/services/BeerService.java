package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Beer operations
 */
public interface BeerService {

    /**
     * Get all beers
     * @return List of all beers
     */
    List<Beer> getAllBeers();

    /**
     * Get a beer by its ID
     * @param id The beer ID
     * @return Optional containing the beer if found
     */
    Optional<Beer> getBeerById(Integer id);

    /**
     * Save a beer (create or update)
     * @param beer The beer to save
     * @return The saved beer
     */
    Beer saveBeer(Beer beer);

    /**
     * Update an existing beer
     * @param id The ID of the beer to update
     * @param beer The updated beer data
     * @return Optional containing the updated beer if found and updated
     */
    Optional<Beer> updateBeer(Integer id, Beer beer);

    /**
     * Delete a beer by its ID
     * @param id The ID of the beer to delete
     * @return true if the beer was deleted, false if not found
     */
    boolean deleteBeerById(Integer id);
}
