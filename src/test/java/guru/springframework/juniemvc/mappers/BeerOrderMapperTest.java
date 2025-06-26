package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderLine;
import guru.springframework.juniemvc.models.BeerOrderDto;
import guru.springframework.juniemvc.models.BeerOrderLineDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class BeerOrderMapperTest {

    private BeerOrderMapper beerOrderMapper;
    private BeerOrderLineMapper beerOrderLineMapper;
    private BeerOrder testBeerOrder;
    private Beer testBeer;

    @BeforeEach
    void setUp() {
        beerOrderMapper = Mappers.getMapper(BeerOrderMapper.class);
        beerOrderLineMapper = Mappers.getMapper(BeerOrderLineMapper.class);

        ReflectionTestUtils.setField(beerOrderMapper, "beerOrderLineMapper", beerOrderLineMapper);

        // Create test beer
        testBeer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testBeer.setId(1);

        // Create test beer order
        testBeerOrder = BeerOrder.builder()
                .customerRef("Test Customer")
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .build();
        testBeerOrder.setId(1);
        testBeerOrder.setCreatedDate(LocalDateTime.now());
        testBeerOrder.setUpdateDate(LocalDateTime.now());

        // Create test beer order line
        BeerOrderLine testBeerOrderLine = BeerOrderLine.builder()
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .beer(testBeer)
                .build();
        testBeerOrderLine.setId(1);

        // Add beer order line to beer order
        testBeerOrder.addBeerOrderLine(testBeerOrderLine);
    }

    @Test
    void testBeerOrderToBeerOrderDto() {
        // When
        BeerOrderDto beerOrderDto = beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder);

        // Then
        assertThat(beerOrderDto).isNotNull();
        assertThat(beerOrderDto.getId()).isEqualTo(testBeerOrder.getId());
        assertThat(beerOrderDto.getCustomerRef()).isEqualTo(testBeerOrder.getCustomerRef());
        assertThat(beerOrderDto.getPaymentAmount()).isEqualTo(testBeerOrder.getPaymentAmount());
        assertThat(beerOrderDto.getStatus()).isEqualTo(testBeerOrder.getStatus());
        assertThat(beerOrderDto.getBeerOrderLines()).hasSize(1);

        BeerOrderLineDto lineDto = beerOrderDto.getBeerOrderLines().iterator().next();
        assertThat(lineDto.getBeerId()).isEqualTo(testBeer.getId());
        assertThat(lineDto.getBeerName()).isEqualTo(testBeer.getBeerName());
        assertThat(lineDto.getBeerStyle()).isEqualTo(testBeer.getBeerStyle());
        assertThat(lineDto.getUpc()).isEqualTo(testBeer.getUpc());
        assertThat(lineDto.getOrderQuantity()).isEqualTo(2);
        assertThat(lineDto.getQuantityAllocated()).isEqualTo(2);
        assertThat(lineDto.getStatus()).isEqualTo("ALLOCATED");
    }

    @Test
    void testBeerOrderDtoToBeerOrder() {
        // Given
        BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                .id(2)
                .customerRef("New Customer")
                .paymentAmount(new BigDecimal("39.97"))
                .status("PENDING")
                .beerOrderLines(new HashSet<>())
                .build();

        BeerOrderLineDto lineDto = BeerOrderLineDto.builder()
                .id(2)
                .beerId(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();

        beerOrderDto.getBeerOrderLines().add(lineDto);

        // When
        BeerOrder beerOrder = beerOrderMapper.beerOrderDtoToBeerOrder(beerOrderDto);
        beerOrder = beerOrderMapper.addBeerOrderLines(beerOrder, beerOrderDto, beerOrderLineMapper);

        // Then
        assertThat(beerOrder).isNotNull();
        assertThat(beerOrder.getId()).isNull(); // ID should be ignored in mapping
        assertThat(beerOrder.getCustomerRef()).isEqualTo(beerOrderDto.getCustomerRef());
        assertThat(beerOrder.getPaymentAmount()).isEqualTo(beerOrderDto.getPaymentAmount());
        assertThat(beerOrder.getStatus()).isEqualTo(beerOrderDto.getStatus());
        assertThat(beerOrder.getBeerOrderLines()).hasSize(1);

        BeerOrderLine line = beerOrder.getBeerOrderLines().iterator().next();
        assertThat(line.getOrderQuantity()).isEqualTo(3);
        assertThat(line.getQuantityAllocated()).isEqualTo(0);
        assertThat(line.getStatus()).isEqualTo("NEW");
        assertThat(line.getBeerOrder()).isEqualTo(beerOrder); // Bidirectional relationship should be set
    }
}
