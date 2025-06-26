package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.mappers.CustomerMapper;
import guru.springframework.juniemvc.models.CustomerDto;
import guru.springframework.juniemvc.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService that uses CustomerRepository for persistence
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Transactional
    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Transactional
    @Override
    public Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Update the existing customer with new values
                    existingCustomer.setName(customerDto.getName());
                    existingCustomer.setEmail(customerDto.getEmail());

                    // Save the updated customer
                    Customer savedCustomer = customerRepository.save(existingCustomer);
                    return customerMapper.customerToCustomerDto(savedCustomer);
                });
    }

    @Transactional
    @Override
    public boolean deleteCustomerById(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}