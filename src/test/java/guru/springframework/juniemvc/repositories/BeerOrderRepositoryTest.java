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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for BeerOrderRepository
 */
@DataJpaTest
class BeerOrderRepositoryTest {

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer;

    @BeforeEach
    void setUp() {
        // Create and save a test customer
        testCustomer = customerRepository.save(Customer.builder()
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
    }

    @Test
    void testSaveBeerOrder() {
        // Given
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .orderStatus("NEW")
                .build();

        BeerOrderLine orderLine = BeerOrderLine.builder()
                .beer(testBeer)
                .orderQuantity(10)
                .build();

        beerOrder.addOrderLine(orderLine);

        // When
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        // Then
        assertThat(savedBeerOrder).isNotNull();
        assertThat(savedBeerOrder.getId()).isNotNull();
        assertThat(savedBeerOrder.getCustomer().getId()).isEqualTo(testCustomer.getId());
        assertThat(savedBeerOrder.getOrderStatus()).isEqualTo("NEW");
        assertThat(savedBeerOrder.getOrderLines()).hasSize(1);
    }

    @Test
    void testFindAllByCustomer() {
        // Given
        BeerOrder beerOrder1 = BeerOrder.builder()
                .customer(testCustomer)
                .orderStatus("NEW")
                .build();

        BeerOrder beerOrder2 = BeerOrder.builder()
                .customer(testCustomer)
                .orderStatus("PROCESSING")
                .build();

        beerOrderRepository.save(beerOrder1);
        beerOrderRepository.save(beerOrder2);

        // When
        List<BeerOrder> customerOrders = beerOrderRepository.findAllByCustomer(testCustomer);

        // Then
        assertThat(customerOrders).isNotNull();
        assertThat(customerOrders.size()).isGreaterThanOrEqualTo(2);
        assertThat(customerOrders).allMatch(order -> order.getCustomer().getId().equals(testCustomer.getId()));
    }
}