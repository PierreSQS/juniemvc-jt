package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.BeerOrderMapper;
import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import guru.springframework.juniemvc.repositories.BeerOrderRepository;
import guru.springframework.juniemvc.repositories.BeerRepository;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for BeerOrderServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class BeerOrderServiceImplTest {

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerOrderMapper beerOrderMapper;

    @InjectMocks
    BeerOrderServiceImpl beerOrderService;

    Customer testCustomer;
    Beer testBeer;
    BeerOrder testBeerOrder;
    BeerOrderDto testBeerOrderDto;
    BeerOrderLineDto testBeerOrderLineDto;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .build();

        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .build();

        testBeerOrder = BeerOrder.builder()
                .id(1)
                .customer(testCustomer)
                .orderStatus("NEW")
                .build();

        testBeerOrderLineDto = BeerOrderLineDto.builder()
                .id(1)
                .beerId(1)
                .orderQuantity(10)
                .build();

        Set<BeerOrderLineDto> orderLines = new HashSet<>();
        orderLines.add(testBeerOrderLineDto);

        testBeerOrderDto = BeerOrderDto.builder()
                .id(1)
                .customerId(1)
                .orderStatus("NEW")
                .orderLines(orderLines)
                .build();
    }

    @Test
    void getAllBeerOrders() {
        // Given
        List<BeerOrder> beerOrders = Arrays.asList(testBeerOrder);
        when(beerOrderRepository.findAll()).thenReturn(beerOrders);
        when(beerOrderMapper.beerOrderToBeerOrderDto(any(BeerOrder.class))).thenReturn(testBeerOrderDto);

        // When
        List<BeerOrderDto> result = beerOrderService.getAllBeerOrders();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(beerOrderRepository).findAll();
    }

    @Test
    void getBeerOrdersByCustomerId() {
        // Given
        List<BeerOrder> beerOrders = Arrays.asList(testBeerOrder);
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(beerOrderRepository.findAllByCustomer(any(Customer.class))).thenReturn(beerOrders);
        when(beerOrderMapper.beerOrderToBeerOrderDto(any(BeerOrder.class))).thenReturn(testBeerOrderDto);

        // When
        List<BeerOrderDto> result = beerOrderService.getBeerOrdersByCustomerId(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(customerRepository).findById(1);
        verify(beerOrderRepository).findAllByCustomer(testCustomer);
    }

    @Test
    void getBeerOrderById() {
        // Given
        when(beerOrderRepository.findById(anyInt())).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderMapper.beerOrderToBeerOrderDto(any(BeerOrder.class))).thenReturn(testBeerOrderDto);

        // When
        Optional<BeerOrderDto> result = beerOrderService.getBeerOrderById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        verify(beerOrderRepository).findById(1);
    }

    @Test
    void createBeerOrder() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(beerRepository.findById(anyInt())).thenReturn(Optional.of(testBeer));
        when(beerOrderRepository.save(any(BeerOrder.class))).thenReturn(testBeerOrder);
        when(beerOrderMapper.beerOrderToBeerOrderDto(any(BeerOrder.class))).thenReturn(testBeerOrderDto);

        // When
        BeerOrderDto result = beerOrderService.createBeerOrder(testBeerOrderDto);

        // Then
        assertThat(result).isNotNull();
        verify(customerRepository).findById(1);
        verify(beerRepository).findById(1);
        verify(beerOrderRepository).save(any(BeerOrder.class));
    }

    @Test
    void createBeerOrder_CustomerNotFound() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> beerOrderService.createBeerOrder(testBeerOrderDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void updateBeerOrder() {
        // Given
        when(beerOrderRepository.findById(anyInt())).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderRepository.save(any(BeerOrder.class))).thenReturn(testBeerOrder);
        when(beerOrderMapper.beerOrderToBeerOrderDto(any(BeerOrder.class))).thenReturn(testBeerOrderDto);

        // When
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrder(1, testBeerOrderDto);

        // Then
        assertThat(result).isPresent();
        verify(beerOrderRepository).findById(1);
        verify(beerOrderRepository).save(any(BeerOrder.class));
    }

    @Test
    void deleteBeerOrderById() {
        // Given
        when(beerOrderRepository.existsById(anyInt())).thenReturn(true);

        // When
        boolean result = beerOrderService.deleteBeerOrderById(1);

        // Then
        assertThat(result).isTrue();
        verify(beerOrderRepository).existsById(1);
        verify(beerOrderRepository).deleteById(1);
    }

    @Test
    void deleteBeerOrderById_NotFound() {
        // Given
        when(beerOrderRepository.existsById(anyInt())).thenReturn(false);

        // When
        boolean result = beerOrderService.deleteBeerOrderById(1);

        // Then
        assertThat(result).isFalse();
        verify(beerOrderRepository).existsById(1);
    }
}