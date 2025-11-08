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

    Beer testBeer;
    BeerDto testBeerDto;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        testBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void listBeers_withoutFilter_usesFindAll() {
        // Given
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(beerRepository.findAll(pageable)).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(testBeer), pageable, 1));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        var page = beerService.listBeers(null, null, pageable);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findAll(pageable);
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void listBeers_withFilter_usesNameQuery() {
        // Given
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(beerRepository.findAllByBeerNameContainingIgnoreCase("Test", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(testBeer), pageable, 1));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        var page = beerService.listBeers("Test", null, pageable);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findAllByBeerNameContainingIgnoreCase("Test", pageable);
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void listBeers_withStyleFilter_usesStyleQuery() {
        // Given
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(beerRepository.findAllByBeerStyleContainingIgnoreCase("IPA", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(testBeer), pageable, 1));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        var page = beerService.listBeers(null, "IPA", pageable);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getBeerStyle()).isEqualTo("IPA");
        verify(beerRepository, times(1)).findAllByBeerStyleContainingIgnoreCase("IPA", pageable);
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void listBeers_withNameAndStyleFilter_usesCombinedQuery() {
        // Given
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase("Test", "IPA", pageable))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(testBeer), pageable, 1));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        var page = beerService.listBeers("Test", "IPA", pageable);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase("Test", "IPA", pageable);
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
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
        assertThat(beerOptional.get().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository, times(1)).findById(1);
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void getBeerByIdNotFound() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.empty());

        // When
        Optional<BeerDto> beerOptional = beerService.getBeerById(1);

        // Then
        assertThat(beerOptional).isEmpty();
        verify(beerRepository, times(1)).findById(1);
    }

    @Test
    void saveBeer() {
        // Given
        BeerDto beerDtoToSave = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer beerToSave = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer savedBeer = Beer.builder()
                .id(2)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        BeerDto savedBeerDto = BeerDto.builder()
                .id(2)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        when(beerMapper.beerDtoToBeer(beerDtoToSave)).thenReturn(beerToSave);
        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);
        when(beerMapper.beerToBeerDto(savedBeer)).thenReturn(savedBeerDto);

        // When
        BeerDto result = beerService.saveBeer(beerDtoToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getBeerName()).isEqualTo("New Beer");
        verify(beerMapper, times(1)).beerDtoToBeer(any(BeerDto.class));
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void updateBeer() {
        // Given
        BeerDto beerDtoToUpdate = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        Beer beerToUpdate = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        Beer updatedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        BeerDto updatedBeerDto = BeerDto.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        when(beerMapper.beerDtoToBeer(beerDtoToUpdate)).thenReturn(beerToUpdate);
        when(beerRepository.save(any(Beer.class))).thenReturn(updatedBeer);
        when(beerMapper.beerToBeerDto(updatedBeer)).thenReturn(updatedBeerDto);

        // When
        BeerDto result = beerService.saveBeer(beerDtoToUpdate);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getBeerName()).isEqualTo("Updated Beer");
        verify(beerMapper, times(1)).beerDtoToBeer(any(BeerDto.class));
        verify(beerRepository, times(1)).save(any(Beer.class));
        verify(beerMapper, times(1)).beerToBeerDto(any(Beer.class));
    }

    @Test
    void deleteBeerById() {
        // Given
        doNothing().when(beerRepository).deleteById(anyInt());

        // When
        beerService.deleteBeerById(1);

        // Then
        verify(beerRepository, times(1)).deleteById(1);
    }
}
