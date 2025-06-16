package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of BeerService that uses BeerRepository for persistence
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    public BeerServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Optional<Beer> getBeerById(Integer id) {
        return beerRepository.findById(id);
    }

    @Override
    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public Optional<Beer> updateBeer(Integer id, Beer beer) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beer.getBeerName());
                    existingBeer.setBeerStyle(beer.getBeerStyle());
                    existingBeer.setUpc(beer.getUpc());
                    existingBeer.setPrice(beer.getPrice());
                    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());

                    // Save the updated beer
                    return beerRepository.save(existingBeer);
                });
    }

    @Override
    public boolean deleteBeerById(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
