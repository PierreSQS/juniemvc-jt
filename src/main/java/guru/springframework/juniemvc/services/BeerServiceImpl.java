package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.mappers.BeerMapper;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Page<BeerDto> listBeers(String beerName, String beerStyle, Pageable pageable) {
        boolean hasName = beerName != null && !beerName.isBlank();
        boolean hasStyle = beerStyle != null && !beerStyle.isBlank();

        Page<Beer> page;
        if (hasName && hasStyle) {
            page = beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(beerName, beerStyle, pageable);
        } else if (hasName) {
            page = beerRepository.findAllByBeerNameContainingIgnoreCase(beerName, pageable);
        } else if (hasStyle) {
            page = beerRepository.findAllByBeerStyleContainingIgnoreCase(beerStyle, pageable);
        } else {
            page = beerRepository.findAll(pageable);
        }
        return page.map(beerMapper::beerToBeerDto);
    }

    @Override
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    public void deleteBeerById(Integer id) {
        beerRepository.deleteById(id);
    }
}
