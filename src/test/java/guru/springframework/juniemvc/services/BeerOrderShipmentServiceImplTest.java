package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.BeerOrderShipment;
import guru.springframework.juniemvc.exceptions.NotFoundException;
import guru.springframework.juniemvc.mappers.BeerOrderShipmentMapper;
import guru.springframework.juniemvc.models.BeerOrderShipmentDto;
import guru.springframework.juniemvc.repositories.BeerOrderRepository;
import guru.springframework.juniemvc.repositories.BeerOrderShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for BeerOrderShipmentServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class BeerOrderShipmentServiceImplTest {

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Mock
    BeerOrderShipmentMapper beerOrderShipmentMapper;

    @InjectMocks
    BeerOrderShipmentServiceImpl beerOrderShipmentService;

    BeerOrder testBeerOrder;
    BeerOrderShipment testBeerOrderShipment;
    BeerOrderShipmentDto testBeerOrderShipmentDto;
    LocalDateTime testShipmentDate;

    @BeforeEach
    void setUp() {
        // Create a test shipment date
        testShipmentDate = LocalDateTime.now();

        // Create a test beer order
        testBeerOrder = BeerOrder.builder()
                .status("COMPLETED")
                .build();
        testBeerOrder.setId(1);

        // Create a test beer order shipment
        testBeerOrderShipment = BeerOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .beerOrder(testBeerOrder)
                .build();
        testBeerOrderShipment.setId(1);

        // Create a test beer order shipment DTO
        testBeerOrderShipmentDto = BeerOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .build();
    }

    @Test
    void getAllShipments() {
        // Given
        when(beerOrderShipmentRepository.findByBeerOrderId(1)).thenReturn(List.of(testBeerOrderShipment));
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).thenReturn(testBeerOrderShipmentDto);

        // When
        List<BeerOrderShipmentDto> shipments = beerOrderShipmentService.getAllShipments(1);

        // Then
        assertThat(shipments).hasSize(1);
        assertThat(shipments.getFirst().getCarrier()).isEqualTo("FedEx");
        assertThat(shipments.getFirst().getTrackingNumber()).isEqualTo("123456789");
        verify(beerOrderShipmentRepository, times(1)).findByBeerOrderId(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any(BeerOrderShipment.class));
    }

    @Test
    void getShipmentById() {
        // Given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testBeerOrderShipment));
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).thenReturn(testBeerOrderShipmentDto);

        // When
        BeerOrderShipmentDto shipmentDto = beerOrderShipmentService.getShipmentById(1, 1);

        // Then
        assertThat(shipmentDto).isNotNull();
        assertThat(shipmentDto.getCarrier()).isEqualTo("FedEx");
        assertThat(shipmentDto.getTrackingNumber()).isEqualTo("123456789");
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any(BeerOrderShipment.class));
    }

    @Test
    void getShipmentByIdNotFound() {
        // Given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> beerOrderShipmentService.getShipmentById(1, 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Shipment not found with id: 1");
    }

    @Test
    void getShipmentByIdWrongBeerOrder() {
        // Given
        BeerOrder anotherBeerOrder = BeerOrder.builder().status("NEW").build();
        anotherBeerOrder.setId(2);
        
        BeerOrderShipment shipment = BeerOrderShipment.builder()
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("987654321")
                .beerOrder(anotherBeerOrder)
                .build();
        shipment.setId(1);
        
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(shipment));

        // Then
        assertThatThrownBy(() -> beerOrderShipmentService.getShipmentById(1, 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Shipment not found for Beer Order with id: 1");
    }

    @Test
    void createShipment() {
        // Given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderShipmentMapper.beerOrderShipmentDtoToBeerOrderShipment(testBeerOrderShipmentDto)).thenReturn(testBeerOrderShipment);
        when(beerOrderShipmentRepository.save(any(BeerOrderShipment.class))).thenReturn(testBeerOrderShipment);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).thenReturn(testBeerOrderShipmentDto);

        // When
        BeerOrderShipmentDto createdShipmentDto = beerOrderShipmentService.createShipment(1, testBeerOrderShipmentDto);

        // Then
        assertThat(createdShipmentDto).isNotNull();
        assertThat(createdShipmentDto.getCarrier()).isEqualTo("FedEx");
        assertThat(createdShipmentDto.getTrackingNumber()).isEqualTo("123456789");
        verify(beerOrderRepository, times(1)).findById(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentDtoToBeerOrderShipment(any(BeerOrderShipmentDto.class));
        verify(beerOrderShipmentRepository, times(1)).save(any(BeerOrderShipment.class));
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any(BeerOrderShipment.class));
    }

    @Test
    void createShipmentBeerOrderNotFound() {
        // Given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> beerOrderShipmentService.createShipment(1, testBeerOrderShipmentDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Beer Order not found with id: 1");
    }

    @Test
    void updateShipment() {
        // Given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testBeerOrderShipment));
        when(beerOrderShipmentRepository.save(any(BeerOrderShipment.class))).thenReturn(testBeerOrderShipment);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(testBeerOrderShipment)).thenReturn(testBeerOrderShipmentDto);

        // Create updated DTO
        BeerOrderShipmentDto updatedDto = BeerOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("UPDATED123")
                .build();

        // When
        BeerOrderShipmentDto updatedShipmentDto = beerOrderShipmentService.updateShipment(1, 1, updatedDto);

        // Then
        assertThat(updatedShipmentDto).isNotNull();
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderShipmentRepository, times(1)).save(any(BeerOrderShipment.class));
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any(BeerOrderShipment.class));
    }

    @Test
    void deleteShipment() {
        // Given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(testBeerOrderShipment));

        // When
        beerOrderShipmentService.deleteShipment(1, 1);

        // Then
        verify(beerOrderShipmentRepository, times(1)).findById(1);
        verify(beerOrderShipmentRepository, times(1)).delete(any(BeerOrderShipment.class));
    }
}