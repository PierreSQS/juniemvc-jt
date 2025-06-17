package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.BeerDto;

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
