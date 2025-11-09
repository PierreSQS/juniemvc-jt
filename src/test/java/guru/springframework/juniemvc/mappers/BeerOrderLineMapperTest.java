package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BeerOrderLineMapperTest {

    private BeerOrderLineMapper beerOrderLineMapper;
    private BeerOrderLine testBeerOrderLine;
    private Beer testBeer;

    @BeforeEach
    void setUp() {
        beerOrderLineMapper = Mappers.getMapper(BeerOrderLineMapper.class);
        
        // Create test beer
        testBeer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testBeer.setId(1);
        
        // Create a test beer order line
        testBeerOrderLine = BeerOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .beer(testBeer)
                .build();
        testBeerOrderLine.setId(1);
    }

    @Test
    void testBeerOrderLineToBeerOrderLineDto() {
        // When
        BeerOrderLineDto beerOrderLineDto = beerOrderLineMapper.beerOrderLineToBeerOrderLineDto(testBeerOrderLine);
        
        // Then
        assertThat(beerOrderLineDto).isNotNull();
        assertThat(beerOrderLineDto.getId()).isEqualTo(testBeerOrderLine.getId());
        assertThat(beerOrderLineDto.getBeerId()).isEqualTo(testBeer.getId());
        assertThat(beerOrderLineDto.getBeerName()).isEqualTo(testBeer.getBeerName());
        assertThat(beerOrderLineDto.getBeerStyle()).isEqualTo(testBeer.getBeerStyle());
        assertThat(beerOrderLineDto.getUpc()).isEqualTo(testBeer.getUpc());
        assertThat(beerOrderLineDto.getOrderQuantity()).isEqualTo(testBeerOrderLine.getOrderQuantity());
        assertThat(beerOrderLineDto.getQuantityAllocated()).isEqualTo(testBeerOrderLine.getQuantityAllocated());
        assertThat(beerOrderLineDto.getStatus()).isEqualTo(testBeerOrderLine.getStatus());
    }

    @Test
    void testBeerOrderLineDtoToBeerOrderLine() {
        // Given
        BeerOrderLineDto beerOrderLineDto = BeerOrderLineDto.builder()
                .id(2)
                .beerId(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();
        
        // When
        BeerOrderLine beerOrderLine = beerOrderLineMapper.beerOrderLineDtoToBeerOrderLine(beerOrderLineDto);
        
        // Then
        assertThat(beerOrderLine).isNotNull();
        assertThat(beerOrderLine.getId()).isNull(); // ID should be ignored in mapping
        assertThat(beerOrderLine.getBeer()).isNull(); // Beer should be ignored in mapping
        assertThat(beerOrderLine.getOrderQuantity()).isEqualTo(beerOrderLineDto.getOrderQuantity());
        assertThat(beerOrderLine.getQuantityAllocated()).isEqualTo(beerOrderLineDto.getQuantityAllocated());
        assertThat(beerOrderLine.getStatus()).isEqualTo(beerOrderLineDto.getStatus());
    }
}