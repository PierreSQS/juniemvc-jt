package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.BeerOrderMapper;
import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import guru.springframework.juniemvc.repositories.BeerOrderRepository;
import guru.springframework.juniemvc.repositories.BeerRepository;
import guru.springframework.juniemvc.repositories.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository,
                               CustomerRepository customerRepository,
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.customerRepository = customerRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll()
                .stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> beerOrderRepository.findAllByCustomer(customer)
                        .stream()
                        .map(beerOrderMapper::beerOrderToBeerOrderDto)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Transactional
    @Override
    public BeerOrderDto createBeerOrder(BeerOrderDto beerOrderDto) {
        Optional<Customer> customerOptional = customerRepository.findById(beerOrderDto.getCustomerId());

        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found: " + beerOrderDto.getCustomerId());
        }

        Customer customer = customerOptional.get();

        BeerOrder beerOrder = BeerOrder.builder()
                .customer(customer)
                .orderStatus("NEW")
                .build();

        // Add order lines
        if (beerOrderDto.getOrderLines() != null) {
            beerOrderDto.getOrderLines().forEach(lineDto -> {
                Optional<Beer> beerOptional = beerRepository.findById(lineDto.getBeerId());

                if (beerOptional.isEmpty()) {
                    throw new RuntimeException("Beer not found: " + lineDto.getBeerId());
                }

                BeerOrderLine line = BeerOrderLine.builder()
                        .beer(beerOptional.get())
                        .orderQuantity(lineDto.getOrderQuantity())
                        .build();

                beerOrder.addOrderLine(line);
            });
        }

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Transactional
    @Override
    public Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .map(existingOrder -> {
                    // Update order status if provided
                    if (beerOrderDto.getOrderStatus() != null) {
                        existingOrder.setOrderStatus(beerOrderDto.getOrderStatus());
                    }

                    // Save the updated order
                    BeerOrder savedOrder = beerOrderRepository.save(existingOrder);
                    return beerOrderMapper.beerOrderToBeerOrderDto(savedOrder);
                });
    }

    @Transactional
    @Override
    public boolean deleteBeerOrderById(Integer id) {
        if (beerOrderRepository.existsById(id)) {
            beerOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}