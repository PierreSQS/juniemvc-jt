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
}