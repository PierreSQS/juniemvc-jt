package guru.springframework.juniemvc.controllers;

import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Beer operations
 */
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    /**
     * Get all beers
     * @return List of all beers
     */
    @GetMapping
    public List<BeerDto> getAllBeers() {
        return beerService.getAllBeers();
    }

    /**
     * Get a beer by its ID
     * @param id The beer ID
     * @return ResponseEntity containing the beer if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable Integer id) {
        return beerService.getBeerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new beer
     * @param beerDto The beer to create
     * @return The created beer with 201 Created status
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDto createBeer(@RequestBody BeerDto beerDto) {
        // Ensure a new beer is created, not an update
        beerDto.setId(null);
        return beerService.saveBeer(beerDto);
    }

    /**
     * Update an existing beer
     * @param id The beer ID
     * @param beerDto The updated beer data
     * @return ResponseEntity containing the updated beer if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable Integer id, @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(id, beerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a beer by its ID
     * @param id The beer ID
     * @return ResponseEntity with no content if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Integer id) {
        return beerService.deleteBeerById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
