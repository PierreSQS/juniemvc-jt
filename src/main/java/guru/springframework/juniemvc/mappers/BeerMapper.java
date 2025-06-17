package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.models.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
}