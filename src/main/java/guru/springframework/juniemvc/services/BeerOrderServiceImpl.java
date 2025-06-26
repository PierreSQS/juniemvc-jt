package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.mappers.BeerOrderLineMapper;
import guru.springframework.juniemvc.mappers.BeerOrderMapper;
import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.repositories.BeerOrderRepository;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of BeerOrderService that uses BeerOrderRepository for persistence
 */
@Service
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final BeerOrderLineMapper beerOrderLineMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository,
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper,
                               BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll().stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional
    public BeerOrderDto saveBeerOrder(BeerOrderDto beerOrderDto) {
        BeerOrder beerOrder = beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDto);
        
        // Process beer order lines
        if (beerOrderDto.getBeerOrderLines() != null) {
            beerOrderDto.getBeerOrderLines().forEach(lineDto -> {
                // Create a new beer order line
                BeerOrderLine line = beerOrderLineMapper.beerOrderLineDtoToBeerOrderLine(lineDto);
                
                // Find and set the beer reference
                if (lineDto.getBeerId() != null) {
                    Optional<Beer> beerOptional = beerRepository.findById(lineDto.getBeerId());
                    beerOptional.ifPresent(line::setBeer);
                }
                
                // Add the line to the order
                beerOrder.addBeerOrderLine(line);
            });
        }
        
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Override
    @Transactional
    public void deleteBeerOrderById(Integer id) {
        beerOrderRepository.deleteById(id);
    }
}