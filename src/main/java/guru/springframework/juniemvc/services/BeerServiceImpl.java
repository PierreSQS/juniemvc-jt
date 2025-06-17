package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.mappers.BeerMapper;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of BeerService that uses BeerRepository for persistence
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    public List<BeerDto> getAllBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Transactional
    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Transactional
    @Override
    public Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beerDto.getBeerName());
                    existingBeer.setBeerStyle(beerDto.getBeerStyle());
                    existingBeer.setUpc(beerDto.getUpc());
                    existingBeer.setPrice(beerDto.getPrice());
                    existingBeer.setQuantityOnHand(beerDto.getQuantityOnHand());

                    // Save the updated beer
                    Beer savedBeer = beerRepository.save(existingBeer);
                    return beerMapper.beerToBeerDto(savedBeer);
                });
    }

    @Transactional
    @Override
    public boolean deleteBeerById(Integer id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
