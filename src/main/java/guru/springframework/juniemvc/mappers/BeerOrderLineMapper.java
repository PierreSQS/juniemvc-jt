package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for BeerOrderLine entity and BeerOrderLineDto
 */
@Mapper(componentModel = "spring")
public interface BeerOrderLineMapper {
    /**
     * Convert BeerOrderLine entity to BeerOrderLineDto
     * @param beerOrderLine The BeerOrderLine entity to convert
     * @return The converted BeerOrderLineDto
     */
    @Mapping(target = "beerId", source = "beer.id")
    @Mapping(target = "beerName", source = "beer.beerName")
    @Mapping(target = "orderId", source = "beerOrder.id")
    BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);

    /**
     * Convert BeerOrderLineDto to BeerOrderLine entity
     * @param beerOrderLineDto The BeerOrderLineDto to convert
     * @return The converted BeerOrderLine entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "beer", ignore = true)
    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}