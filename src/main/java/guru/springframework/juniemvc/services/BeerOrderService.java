package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.models.BeerOrderDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for BeerOrder operations
 */
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