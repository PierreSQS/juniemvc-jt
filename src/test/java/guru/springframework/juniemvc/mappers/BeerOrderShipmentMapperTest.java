package guru.springframework.juniemvc.mappers;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderShipment;
import guru.springframework.juniemvc.models.BeerOrderShipmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for BeerOrderShipmentMapper
 */
class BeerOrderShipmentMapperTest {

    private BeerOrderShipmentMapper beerOrderShipmentMapper;
    private BeerOrderShipment testBeerOrderShipment;
    private LocalDateTime testShipmentDate;

    @BeforeEach
    void setUp() {
        beerOrderShipmentMapper = Mappers.getMapper(BeerOrderShipmentMapper.class);
        
        // Create a test beer order
        BeerOrder testBeerOrder = BeerOrder.builder()
                .status("COMPLETED")
                .build();
        testBeerOrder.setId(1);
        
        // Create a test shipment date
        testShipmentDate = LocalDateTime.now();
        
        // Create a test beer order shipment
        testBeerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .beerOrder(testBeerOrder)
                .build();
        testBeerOrderShipment.setId(1);
    }

    @Test
    void testBeerOrderShipmentToBeerOrderShipmentDto() {
        // When
        BeerOrderShipmentDto shipmentDto = beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment);
        
        // Then
        assertThat(shipmentDto).isNotNull();
        assertThat(shipmentDto.getId()).isEqualTo(testBeerOrderShipment.getId());
        assertThat(shipmentDto.getShipmentDate()).isEqualTo(testBeerOrderShipment.getShipmentDate());
        assertThat(shipmentDto.getCarrier()).isEqualTo(testBeerOrderShipment.getCarrier());
        assertThat(shipmentDto.getTrackingNumber()).isEqualTo(testBeerOrderShipment.getTrackingNumber());
    }

    @Test
    void testBeerOrderShipmentDtoToBeerOrderShipment() {
        // Given
        BeerOrderShipmentDto shipmentDto = BeerOrderShipmentDto.builder()
                .id(2)
                .shipmentDate(testShipmentDate.plusDays(1))
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();
        
        // When
        BeerOrderShipment shipment = beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(shipmentDto);
        
        // Then
        assertThat(shipment).isNotNull();
        assertThat(shipment.getId()).isNull(); // ID should be ignored in mapping
        assertThat(shipment.getBeerOrder()).isNull(); // BeerOrder should be ignored in mapping
        assertThat(shipment.getShipmentDate()).isEqualTo(shipmentDto.getShipmentDate());
        assertThat(shipment.getCarrier()).isEqualTo(shipmentDto.getCarrier());
        assertThat(shipment.getTrackingNumber()).isEqualTo(shipmentDto.getTrackingNumber());
    }
}