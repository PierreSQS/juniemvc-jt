package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.services.BeerOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for BeerOrder operations
 */
@RestController
@RequestMapping("/api/v1/orders")
public class BeerOrderController {

    private final BeerOrderService beerOrderService;

    public BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    /**
     * Get all beer orders
     * @return List of all beer orders
     */
    @GetMapping
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderService.getAllBeerOrders();
    }

    /**
     * Get beer orders by customer ID
     * @param customerId The customer ID
     * @return List of beer orders for the customer
     */
    @GetMapping("/customer/{customerId}")
    public List<BeerOrderDto> getBeerOrdersByCustomerId(@PathVariable Integer customerId) {
        return beerOrderService.getBeerOrdersByCustomerId(customerId);
    }

    /**
     * Get a beer order by its ID
     * @param id The beer order ID
     * @return ResponseEntity containing the beer order if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable Integer id) {
        return beerOrderService.getBeerOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new beer order
     * @param beerOrderDto The beer order to create
     * @return The created beer order with 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerOrderDto createBeerOrder(@RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.createBeerOrder(beerOrderDto);
    }

    /**
     * Update an existing beer order
     * @param id The beer order ID
     * @param beerOrderDto The updated beer order data
     * @return ResponseEntity containing the updated beer order if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<BeerOrderDto> updateBeerOrder(@PathVariable Integer id, @RequestBody BeerOrderDto beerOrderDto) {
        return beerOrderService.updateBeerOrder(id, beerOrderDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a beer order by its ID
     * @param id The beer order ID
     * @return ResponseEntity with no content if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable Integer id) {
        return beerOrderService.deleteBeerOrderById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}