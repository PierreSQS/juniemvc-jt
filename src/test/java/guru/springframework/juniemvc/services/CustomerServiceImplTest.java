package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.CustomerMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for CustomerServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer testCustomer;
    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .build();

        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .build();
    }

    @Test
    void getAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        List<CustomerDto> result = customerService.getAllCustomers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        Optional<CustomerDto> result = customerService.getCustomerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("Test Customer");
        verify(customerRepository).findById(1);
    }

    @Test
    void saveCustomer() {
        // Given
        when(customerMapper.customerDtoToCustomer(any(CustomerDto.class))).thenReturn(testCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        CustomerDto savedCustomer = customerService.saveCustomer(testCustomerDto);

        // Then
        assertThat(savedCustomer).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(testCustomerDto);

        // When
        Optional<CustomerDto> result = customerService.updateCustomer(1, testCustomerDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        verify(customerRepository).findById(1);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deleteCustomerById() {
        // Given
        when(customerRepository.existsById(anyInt())).thenReturn(true);

        // When
        boolean result = customerService.deleteCustomerById(1);

        // Then
        assertThat(result).isTrue();
        verify(customerRepository).existsById(1);
        verify(customerRepository).deleteById(1);
    }

    @Test
    void deleteCustomerById_NotFound() {
        // Given
        when(customerRepository.existsById(anyInt())).thenReturn(false);

        // When
        boolean result = customerService.deleteCustomerById(1);

        // Then
        assertThat(result).isFalse();
        verify(customerRepository).existsById(1);
    }
}