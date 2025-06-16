package guru.springframework.juniemvc.services;

import guru.springframework.juniemvc.entities.Beer;
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

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    List<Beer> testBeers;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void getAllBeers() {
        // Given
        when(beerRepository.findAll()).thenReturn(testBeers);

        // When
        List<Beer> beers = beerService.getAllBeers();

        // Then
        assertThat(beers).hasSize(2);
        assertThat(beers).isEqualTo(testBeers);
        verify(beerRepository, times(1)).findAll();
    }

    @Test
    void getBeerById() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));

        // When
        Optional<Beer> beerOptional = beerService.getBeerById(1);

        // Then
        assertThat(beerOptional).isPresent();
        assertThat(beerOptional.get()).isEqualTo(testBeer);
        verify(beerRepository, times(1)).findById(1);
    }

    @Test
    void getBeerByIdNotFound() {
        // Given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<Beer> beerOptional = beerService.getBeerById(999);

        // Then
        assertThat(beerOptional).isEmpty();
        verify(beerRepository, times(1)).findById(999);
    }

    @Test
    void saveBeer() {
        // Given
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

        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);

        // When
        Beer result = beerService.saveBeer(beerToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeerFound() {
        // Given
        Beer updatedBeer = Beer.builder()
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

        when(beerRepository.findById(1)).thenReturn(Optional.of(existingBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);

        // When
        Optional<Beer> result = beerService.updateBeer(1, updatedBeer);

        // Then
        assertThat(result).isPresent();
        Beer resultBeer = result.get();
        assertThat(resultBeer.getId()).isEqualTo(1);
        assertThat(resultBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(resultBeer.getBeerStyle()).isEqualTo("Updated Style");
        verify(beerRepository, times(1)).findById(1);
        verify(beerRepository, times(1)).save(any(Beer.class));
    }

    @Test
    void updateBeerNotFound() {
        // Given
        Beer updatedBeer = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<Beer> result = beerService.updateBeer(999, updatedBeer);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository, times(1)).findById(999);
        verify(beerRepository, never()).save(any(Beer.class));
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