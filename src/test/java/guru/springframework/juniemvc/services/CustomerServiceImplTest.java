package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.exceptions.NotFoundException;
import guru.springframework.juniemvc.mappers.CustomerMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        // Create a test customer
        testCustomer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        testCustomer.setId(1);

        // Create test customer DTO
        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
    }

    @Test
    void getAllCustomers() {
        // Given
        when(customerRepository.findAll()).thenReturn(List.of(testCustomer));
        when(customerMapper.customerToCustomerDto(testCustomer)).thenReturn(testCustomerDto);

        // When
        List<CustomerDto> customers = customerService.getAllCustomers();

        // Then
        assertThat(customers).hasSize(1);
        assertThat(customers.getFirst().getName()).isEqualTo("John Doe");
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).customerToCustomerDto(any(Customer.class));
    }

    @Test
    void getCustomerById() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(customerMapper.customerToCustomerDto(testCustomer)).thenReturn(testCustomerDto);

        // When
        Optional<CustomerDto> customerOptional = customerService.getCustomerById(1);

        // Then
        assertThat(customerOptional).isPresent();
        assertThat(customerOptional.get().getName()).isEqualTo("John Doe");
        verify(customerRepository, times(1)).findById(1);
        verify(customerMapper, times(1)).customerToCustomerDto(any(Customer.class));
    }

    @Test
    void getCustomerByIdNotFound() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> customerOptional = customerService.getCustomerById(1);

        // Then
        assertThat(customerOptional).isEmpty();
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void saveCustomer() {
        // Given
        when(customerMapper.customerDtoToCustomer(testCustomerDto)).thenReturn(testCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(customerMapper.customerToCustomerDto(testCustomer)).thenReturn(testCustomerDto);

        // When
        CustomerDto savedCustomerDto = customerService.saveCustomer(testCustomerDto);

        // Then
        assertThat(savedCustomerDto).isNotNull();
        assertThat(savedCustomerDto.getName()).isEqualTo("John Doe");
        verify(customerMapper, times(1)).customerDtoToCustomer(any(CustomerDto.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).customerToCustomerDto(any(Customer.class));
    }

    @Test
    void updateCustomer() {
        // Given
        CustomerDto updateDto = CustomerDto.builder()
                .name("John Doe Updated")
                .email("john.updated@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Suite 101")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();

        Customer existingCustomer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        existingCustomer.setId(1);

        Customer updatedCustomer = Customer.builder()
                .name("John Doe Updated")
                .email("john.updated@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Suite 101")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();
        updatedCustomer.setId(1);

        CustomerDto updatedDto = CustomerDto.builder()
                .id(1)
                .name("John Doe Updated")
                .email("john.updated@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .addressLine2("Suite 101")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();

        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.customerToCustomerDto(updatedCustomer)).thenReturn(updatedDto);

        // When
        Optional<CustomerDto> result = customerService.updateCustomer(1, updateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe Updated");
        assertThat(result.get().getEmail()).isEqualTo("john.updated@example.com");
        verify(customerRepository, times(1)).findById(1);
        verify(customerMapper, times(1)).updateCustomerFromDto(any(CustomerDto.class), any(Customer.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).customerToCustomerDto(any(Customer.class));
    }

    @Test
    void updateCustomerNotFound() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        // When/Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(1, testCustomerDto));

        // Verify exception message
        assertThat(exception.getMessage()).contains("Customer not found with id: 1");

        // Verify repository was called
        verify(customerRepository, times(1)).findById(1);
        verify(customerMapper, times(0)).updateCustomerFromDto(any(CustomerDto.class), any(Customer.class));
        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    void deleteCustomerById() {
        // When
        customerService.deleteCustomerById(1);

        // Then
        verify(customerRepository, times(1)).deleteById(1);
    }
}
