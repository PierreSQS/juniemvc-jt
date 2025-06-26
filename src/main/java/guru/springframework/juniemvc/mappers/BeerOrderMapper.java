package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.models.BeerOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for BeerOrder entity and BeerOrderDto
 */
@Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class, CustomerMapper.class})
public interface BeerOrderMapper {
    /**
     * Convert BeerOrder entity to BeerOrderDto
     * @param beerOrder The BeerOrder entity to convert
     * @return The converted BeerOrderDto
     */
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);

    /**
     * Convert BeerOrderDto to BeerOrder entity
     * @param beerOrderDto The BeerOrderDto to convert
     * @return The converted BeerOrder entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
}