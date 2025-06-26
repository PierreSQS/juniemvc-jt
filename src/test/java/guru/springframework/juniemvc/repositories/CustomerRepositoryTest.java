package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for CustomerRepository
 */
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .build();

        // When
        Customer savedCustomer = customerRepository.save(customer);

        // Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("Test Customer");
        assertThat(savedCustomer.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindAll() {
        // Given
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .email("customer1@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .email("customer2@example.com")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // When
        List<Customer> customers = customerRepository.findAll();

        // Then
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isGreaterThanOrEqualTo(2);
    }
}