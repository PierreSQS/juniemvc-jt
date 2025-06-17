package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.mappers.BeerMapper;
import guru.springframework.juniemvc.models.BeerDto;
import guru.springframework.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    BeerDto testBeerDto;
    List<BeerDto> testBeerDtos;
    Beer testBeer;
    List<Beer> testBeers;

    @BeforeEach
    void setUp() {
        // Setup Beer entities
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer testBeer2 = Beer.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        testBeers = Arrays.asList(testBeer, testBeer2);

        // Setup BeerDto objects
        testBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        BeerDto testBeerDto2 = BeerDto.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        testBeerDtos = Arrays.asList(testBeerDto, testBeerDto2);
    }

    @Test
    void getAllBeers() {
        // Given
        when(beerRepository.findAll()).thenReturn(testBeers);
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);
        when(beerMapper.beerToBeerDto(testBeers.get(1))).thenReturn(testBeerDtos.get(1));

        // When
        List<BeerDto> beers = beerService.getAllBeers();

        // Then
        assertThat(beers).hasSize(2);
        assertThat(beers).isEqualTo(testBeerDtos);
        verify(beerRepository, times(1)).findAll();
        verify(beerMapper, times(2)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void getBeerById() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        Optional<BeerDto> beerOptional = beerService.getBeerById(1);

        // Then
        assertThat(beerOptional).isPresent();
        assertThat(beerOptional.get()).isEqualTo(testBeerDto);
        verify(beerRepository, times(1)).findById(1);
        verify(beerMapper, times(1)).beerToBeerDto(testBeer);
    }

    @Test
    void getBeerByIdNotFound() {
        // Given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerDto> beerOptional = beerService.getBeerById(999);

        // Then
        assertThat(beerOptional).isEmpty();
        verify(beerRepository, times(1)).findById(999);
        verify(beerMapper, never()).beerToBeerDto(any(Beer.class));
    }

    @Test
    void saveBeer() {
        // Given
        BeerDto beerDtoToSave = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        Beer beerToSave = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        Beer savedBeer = Beer.builder()
                .id(3)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        BeerDto savedBeerDto = BeerDto.builder()
                .id(3)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        when(beerMapper.beerDtoToBeer(beerDtoToSave)).thenReturn(beerToSave);
        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);
        when(beerMapper.beerToBeerDto(savedBeer)).thenReturn(savedBeerDto);

        // When
        BeerDto result = beerService.saveBeer(beerDtoToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        verify(beerMapper, times(1)).beerDtoToBeer(any(BeerDto.class));
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void updateBeerFound() {
        // Given
        BeerDto updatedBeerDto = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        Beer existingBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        BeerDto savedBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        when(beerRepository.findById(1)).thenReturn(Optional.of(existingBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);
        when(beerMapper.beerToBeerDto(savedBeer)).thenReturn(savedBeerDto);

        // When
        Optional<BeerDto> result = beerService.updateBeer(1, updatedBeerDto);

        // Then
        assertThat(result).isPresent();
        BeerDto resultBeerDto = result.get();
        assertThat(resultBeerDto.getId()).isEqualTo(1);
        assertThat(resultBeerDto.getBeerName()).isEqualTo("Updated Beer");
        assertThat(resultBeerDto.getBeerStyle()).isEqualTo("Updated Style");
        verify(beerRepository, times(1)).findById(1);
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void updateBeerNotFound() {
        // Given
        BeerDto updatedBeerDto = BeerDto.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerDto> result = beerService.updateBeer(999, updatedBeerDto);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository, times(1)).findById(999);
        verify(beerRepository, never()).save(any(Beer.class));
        verify(beerMapper, never()).beerToBeerDto(any(Beer.class));
    }

    @Test
    void deleteBeerByIdFound() {
        // Given
        when(beerRepository.existsById(1)).thenReturn(true);
        doNothing().when(beerRepository).deleteById(1);

        // When
        boolean result = beerService.deleteBeerById(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository, times(1)).existsById(1);
        verify(beerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteBeerByIdNotFound() {
        // Given
        when(beerRepository.existsById(999)).thenReturn(false);

        // When
        boolean result = beerService.deleteBeerById(999);

        // Then
        assertThat(result).isFalse();
        verify(beerRepository, times(1)).existsById(999);
        verify(beerRepository, never()).deleteById(anyInt());
    }
}
