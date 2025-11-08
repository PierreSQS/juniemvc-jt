package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.BeerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for Beer operations
 */
public interface BeerService {

    /**
     * List beers with optional filtering by beerName and pagination
     * @param beerName optional beer name filter (may be null or blank)
     * @param pageable pagination information
     * @return Page of beers
     */
    Page<BeerDto> listBeers(String beerName, Pageable pageable);

    /**
     * Get a beer by its ID
     * @param id the beer ID
     * @return Optional containing the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Save a new beer or update an existing one
     * @param beerDto the beer to save
     * @return the saved beer
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Delete a beer by its ID
     * @param id the beer ID
     */
    void deleteBeerById(Integer id);
}
