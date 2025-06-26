package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Customer;
import guru.springframework.juniemvc.models.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for Customer entity and CustomerDto
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    /**
     * Convert Customer entity to CustomerDto
     * @param customer The Customer entity to convert
     * @return The converted CustomerDto
     */
    CustomerDto customerToCustomerDto(Customer customer);

    /**
     * Convert CustomerDto to Customer entity
     * @param customerDto The CustomerDto to convert
     * @return The converted Customer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}