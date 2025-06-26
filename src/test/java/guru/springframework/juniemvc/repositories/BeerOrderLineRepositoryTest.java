package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for BeerOrderLineRepository
 */
@DataJpaTest
class BeerOrderLineRepositoryTest {

    @Autowired
    private BeerOrderLineRepository beerOrderLineRepository;

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    private Beer testBeer;
    private BeerOrder testBeerOrder;

    @BeforeEach
    void setUp() {
        // Create and save a test customer
        Customer testCustomer = customerRepository.save(Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .build());

        // Create and save a test beer
        testBeer = beerRepository.save(Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(100)
                .build());

        // Create and save a test beer order
        testBeerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .orderStatus("NEW")
                .build();

        beerOrderRepository.save(testBeerOrder);
    }

    @Test
    void testSaveBeerOrderLine() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beerOrder(testBeerOrder)
                .beer(testBeer)
                .orderQuantity(10)
                .build();

        // When
        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // Then
        assertThat(savedBeerOrderLine).isNotNull();
        assertThat(savedBeerOrderLine.getId()).isNotNull();
        assertThat(savedBeerOrderLine.getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
        assertThat(savedBeerOrderLine.getBeer().getId()).isEqualTo(testBeer.getId());
        assertThat(savedBeerOrderLine.getOrderQuantity()).isEqualTo(10);
    }

    @Test
    void testFindById() {
        // Given
        BeerOrderLine beerOrderLine = BeerOrderLine.builder()
                .beerOrder(testBeerOrder)
                .beer(testBeer)
                .orderQuantity(10)
                .build();

        BeerOrderLine savedBeerOrderLine = beerOrderLineRepository.save(beerOrderLine);

        // When
        Optional<BeerOrderLine> foundBeerOrderLine = beerOrderLineRepository.findById(savedBeerOrderLine.getId());

        // Then
        assertThat(foundBeerOrderLine).isPresent();
        assertThat(foundBeerOrderLine.get().getId()).isEqualTo(savedBeerOrderLine.getId());
        assertThat(foundBeerOrderLine.get().getBeerOrder().getId()).isEqualTo(testBeerOrder.getId());
        assertThat(foundBeerOrderLine.get().getBeer().getId()).isEqualTo(testBeer.getId());
        assertThat(foundBeerOrderLine.get().getOrderQuantity()).isEqualTo(10);
    }
}